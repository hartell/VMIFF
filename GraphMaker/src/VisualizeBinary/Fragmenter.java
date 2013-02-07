/**
 * 
 */
package VisualizeBinary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class will take a single file, multiple files or an entire directory and fragment it into 512 byte fragments.
 * @author hartell
 */
public class Fragmenter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Ask the user if they want to fragment specific file/s or an entire directory..
		Object[] options = {"Directory", "File"};
		int answer = JOptionPane.showOptionDialog(null, "Fragment Directory or File/s?", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		
		//Create a new file chooser based for either directories or files.
		JFileChooser chooser = new JFileChooser();
		//Starts the program in a specific directory
		chooser.setCurrentDirectory(new File("H://SVM/"));
		//If the user wants to fragment a directory...
		if(answer == 0){
			System.out.println("User selected a fragment a Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		//If the user quits, shut down the program
		else if(answer == JOptionPane.CLOSED_OPTION){
			System.out.println("User Quit");
			System.exit(0);
		}
		//Otherwise fragment the files they selected...
		else{
			System.out.println("User selected to fragment File/s");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(true);
		}
		
		//Ask the user to select a file/s or a directory.
		int selection = chooser.showOpenDialog(null);
		
		//Ok they've selected something, get what they've selected.
		if(selection == JFileChooser.APPROVE_OPTION){
			File[] files = null;
			//If file/s were selected...
			if(answer == 1){ 
				files = chooser.getSelectedFiles();
			}
			//it's a directory so grab all ONLY files (no directories) inside of the directory
			else {
				File dir = chooser.getSelectedFile();
				System.out.println("Dir Path: " + dir.getAbsolutePath());
				if(dir.isDirectory()){
					//Get a list of all the files/directories inside the directory
					File[] listOfFiles = dir.listFiles();
					ArrayList<File> tempFileHolder = new ArrayList<File>();
					//Remove all directories
					for(File f : listOfFiles){
						if(!f.isDirectory()){
							tempFileHolder.add(f);
						}
					}
					//Add the just the files (no directories) into the list of selected files.
					files = new File[tempFileHolder.size()];
					for(int i = 0; i < tempFileHolder.size(); i++){
						files[i] = tempFileHolder.get(i);
					}
				}
				else{
					//Otherwise something went wrong...
					System.out.println("Directory not selected");
				}
			}
			
			//Create a Directory to put the file fragments, start naming at fragment1
			File output = new File(files[0].getParentFile() + "\\Fragmented");
			int fragCounter = 1;
			
			//Check to see if the directory exists...
			//If not, make it.
			if(!output.isDirectory()){
				output.mkdirs();
			} 
			//If it does exist, then ask the user if they want to delete current fragments or append to the current fragments
			else if(output.isDirectory()){
				//Ask user if they want to delete currently existing fragments
				int emptyFolder = JOptionPane.showConfirmDialog(null, "Delete current Fragments?", "Delete?", JOptionPane.YES_NO_OPTION);
				
				//Delete files in Directory
				if(emptyFolder == JOptionPane.YES_OPTION){
					System.out.println("Deleting Old Fragments...");
					//Empty the folder
					File[] fileList = output.listFiles();
					for(File myFile : fileList){
						//Delete only non-directories...
						if(!myFile.isDirectory()){
							myFile.delete();
						}
					}
				}
				//If the user quits, shut down the program
				else if(emptyFolder == JOptionPane.CLOSED_OPTION){
					System.out.println("User Quit");
					System.exit(0);
				}
				//Leave the current fragments alone and start off numbering where you left off.
				else{
					//Grab all the files
					File[] theFragments = output.listFiles();
					//Arrays.sort(theFragments);
					
					ArrayList<Integer> theNums = new ArrayList<Integer>();
					//Get all the files' indexes
					for(File f : theFragments){
						String name = f.getName();
						String lastNum = name.substring(4, name.length());
						theNums.add(Integer.parseInt(lastNum));
					}
					
					//Sort the list lowest to greatest
					Collections.sort(theNums);
					
					//Set the counter to the next available index
					fragCounter = theNums.get(theNums.size()-1) + 1;
					System.out.println("Next frag = " + fragCounter);
					
				}
			}
			else {
				//Something went wrong
				throw new IllegalArgumentException("Output directory is not a directory!");
			}			
			
			//Ask the user how to fragment file
			String[] fragMethod = {"Whole File", "Header Fragments", "Mid Fragments", "Footer Fragments"};
			String howToFrag = (String) JOptionPane.showInputDialog(null, "Select fragment method: ", "How would you like to fragment the file(s)", JOptionPane.QUESTION_MESSAGE, null, fragMethod, fragMethod[0]);
			
			if(howToFrag.equals(JOptionPane.CLOSED_OPTION) || howToFrag.equals(JOptionPane.CANCEL_OPTION)){
				System.out.println("User closed the Select Fragment method. Program exiting.");
				System.exit(0);
			} else {
			
				//Iterate through the files the user wants to fragment, name them fragment + (fragCounter - aka wherever the last filename left off);
				int i = 1;
				for(File f : files){
					System.out.println("Fragmenting file #" + i);
					System.out.println("Input: " + f.getAbsolutePath());
					System.out.println("Output: " + f.getParentFile() + "\\Fragmented");
					int temp = 0;
					//Determine how the user selected to fragment the file.
					if(howToFrag.equals("Whole File")) {
						System.out.println("User selected to fragment whole file");
						temp = fragmentFiles(f, output, fragCounter);
					} else if(howToFrag.equals("Header Fragments")) {
						System.out.println("User selected to fragment header fragments from the file");
						temp = fragmentHeaders(f, output, fragCounter);
					} else if(howToFrag.equals("Mid Fragments")) {
						System.out.println("User selected to fragment middle fragments from the file");
						temp = fragmentMids(f, output, fragCounter);
					} else if (howToFrag.equals("Footer Fragments")) {
						System.out.println("User selected to fragment footers fragments from the file");
						temp = fragmentFooters(f, output, fragCounter);
					} else {
						System.err.println("Error: User didn't specify how to fragment file");
					}
					fragCounter = temp;
					i++;
				}
			}
		}
		//If the user quits, shut down the program
		else if(selection == JFileChooser.CANCEL_OPTION){
			System.out.println("User Quit");
			System.exit(0);
		}
		System.out.println("Fragmentation Complete");
	}
	
	/**
	 * This method fragments a file into 512 bytes sections, but only outputs the first 512 bytes (header).
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentHeaders(File input, File output, int fragCounter){
		//Only use the 512 bytes :)	
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//Check to see if the file is > 512 bytes
			if(file.getFilePointer() + 512 < file.length()){
				//Read in the 512 bytes and output them.
				//Read in 512 bytes
				byte[] buffer = new byte[512];
				file.readFully(buffer);
				//Output the 512 byte fragment to a incremented file named Fragment + fileCount
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
				fileCounter++;
				out.write(buffer);
				out.close();
			}
			//Otherwise the file will be less than 512 bytes... so we're going to skip it?
			else {
				//Do nothing? Panic? Throw chickens?
			}
			file.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return fileCounter;
	}
	
	/**
	 * This method fragments a file into 512 bytes sections, but removes the first 512 bytes (header) and the last 512 bytes (footer)
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentMids(File input, File output, int fragCounter){
		//Skip the first 512 bytes (header), the output fragments, skip the last on (footer)
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//Check to see if the file is greater than 1024 (512 for header, 512 for mid, +whatever for footer)
			//if > 1024
			if(file.getFilePointer() + 1024 < file.length()){
				//skip 512 bytes (header)
				file.seek(512);
				//while Pointer+512 < file length... output fragment
				while(file.getFilePointer()+512 < file.length()){
					//Read in 512 bytes
					byte[] buffer = new byte[512];
					file.readFully(buffer);
					//Output the 512 byte fragment to a incremented file named Fragment + fileCount
					OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
					fileCounter++;
					out.write(buffer);
					out.close();
				}
			}
			else{
				//Do nothing? Panic? Throw chickens?
			}
			file.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return fileCounter;
	}
	
	/**
	 * This method fragments a file into 512 bytes sections, but only outputs the last 512 bytes (footer)
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentFooters(File input, File output, int fragCounter){
		//Skip the first 512 bytes (header),skipp the output fragments, output the last on (footer)
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//Check to see if the file is greater than 1024 (512 for header, 512 for mid, + whatever footer)
			if(file.getFilePointer() + 1024 < file.length()){
				//Seek 512 bytes (header)
				file.skipBytes(512);
				// while file.getFilePointer + 512 < file.length)
				while(file.getFilePointer() + 512 < file.length()){
					System.out.println(file.getFilePointer());
					file.skipBytes(512);
					System.out.println(file.getFilePointer());
				}
				//IF the file has any leftover bytes < 512 left...
				//output the leftover bytes (footer)
				if(file.getFilePointer() != file.length()){
					//Read in whatever is left and pad with 0s up to 512 bytes.
					byte[] buffer = new byte[512];
					int end = (int) (file.length() - file.getFilePointer());
					file.readFully(buffer, 0 , end);
					//Output the 512 byte fragment to a incremented file named Fragment + fileCount
					OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
					fileCounter++;
					out.write(buffer);
					out.close();
				}
			}
			else {
				//Do nothing? Panic? Throw chickens?
			}
			
			file.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return fileCounter;
	}
	
	/**
	 * This method fragments a file into 512 bytes sections. Remnants of files < 512 bytes are padded with zeros
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentFiles(File input, File output, int fragCounter){
		//Thanks to Ben Holland for assistance with RandomAccessFiles!
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//While the file has more than 512 bytes left...
			while(file.getFilePointer() + 512 < file.length()){
				//Read in 512 bytes
				byte[] buffer = new byte[512];
				file.readFully(buffer);
				//Output the 512 byte fragment to a incremented file named Fragment + fileCount
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
				fileCounter++;
				out.write(buffer);
				out.close();
			}
			//IF the file has any leftover bytes < 512 left...
			if(file.getFilePointer() != file.length()){
				//Read in whatever is left and pad with 0s up to 512 bytes.
				byte[] buffer = new byte[512];
				int end = (int) (file.length() - file.getFilePointer());
				file.readFully(buffer, 0 , end);
				//Output the 512 byte fragment to a incremented file named Fragment + fileCount
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
				fileCounter++;
				out.write(buffer);
				out.close();
			}
			
			file.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return fileCounter;
	}

}
