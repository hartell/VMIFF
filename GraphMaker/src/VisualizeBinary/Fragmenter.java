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
 * This class will take a single file, multiple files or an entire directory and fragment it into fragmentSize byte fragments.
 * @author hartell
 */
public class Fragmenter {
	
	public static int fragmentSize = 0;
	public static int numOfFrags = 0;
	public static boolean useGUI = false;
	public static String newDir = "Problem2-WholeFrags";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(!useGUI){
			regenerateFragments();
		}
		else {
			
			int fragCounter = 1;
			
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
				
				//Create a Directory to put the file fragments, naming fragment starts at 1
				File output = new File(files[0].getParentFile() + "\\Fragmented");
				
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
						System.out.println("Deleting Old Fragments... (please be patient)");
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
				
				//Ask the user what size fragments to output:
				while(fragmentSize <= 0){
					String size = JOptionPane.showInputDialog("Size of fragments: ", "512");
					if(size.equals(JOptionPane.CLOSED_OPTION) || size.equals(JOptionPane.CANCEL_OPTION)){
						System.out.println("User Quit");
						System.exit(0);
					}
					//Test if the size input is actually an int.
					try{
						int test = Integer.parseInt(size);
						fragmentSize = test;
					} catch (NumberFormatException e){
						System.err.println("Not a number, please retry");
					}
				}
				
				//Ask the user how many fragments they want...
				while(numOfFrags <= 0){
					String size = JOptionPane.showInputDialog("How many fragments: ", "20000");
					if(size.equals(JOptionPane.CLOSED_OPTION) || size.equals(JOptionPane.CANCEL_OPTION)){
						System.out.println("User Quit");
						System.exit(0);
					}
					//Test if the size input is actually an int.
					try{
						int test = Integer.parseInt(size);
						numOfFrags = test;
					} catch (NumberFormatException e){
						System.err.println("Error: not a number, please retry");
					}
				}
				
				
				//Ask the user how to fragment file
				String[] fragMethod = {"Whole File", "Header Fragments", "Mid Fragments", "Footer Fragments"};
				String howToFrag = (String) JOptionPane.showInputDialog(null, "Select fragment method: ", "How would you like to fragment the file(s)", JOptionPane.QUESTION_MESSAGE, null, fragMethod, fragMethod[2]);
				
				if(howToFrag.equals(JOptionPane.CLOSED_OPTION) || howToFrag.equals(JOptionPane.CANCEL_OPTION)){
					System.out.println("User closed the Select Fragment method. Program exiting.");
					System.exit(0);
				} else {
				
					//Iterate through the files the user wants to fragment, name them fragment + (fragCounter - aka wherever the last filename left off);
					int i = 1;
					for(File f : files){
						//Check to see if we've reach the number of fragments the user wants:
						if(fragCounter < numOfFrags+1){
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
								temp = fragmentMids(f, output, fragCounter, i);
							} else if (howToFrag.equals("Footer Fragments")) {
								System.out.println("User selected to fragment footers fragments from the file");
								temp = fragmentFooters(f, output, fragCounter);
							} else {
								System.err.println("Error: User didn't specify how to fragment file");
							}
							fragCounter = temp;
							i++;
						} else {
							//Do nothing, we've reach the number of fragments we needed!
						}
					}
				}
			}
			//If the user quits, shut down the program
			else if(selection == JFileChooser.CANCEL_OPTION){
				System.out.println("User Quit");
				System.exit(0);
			}
			
			//Notify User if the number of fragments was less than the number s/he wanted.
			if(fragCounter < numOfFrags) {
				System.out.println("Notice: Not enough files to reach goal number of fragments...");
			}
			
			//Fragmenting is completed!
			System.out.println("Fragmentation Complete");
		}
	}
	
	private static void regenerateFragments() {
//		fragmenterCL("AVI", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("DOC", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("EXE", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("GIF", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("JPG", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("MOV", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("PDF", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("PPT", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("TXT", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("WAV", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("WMA", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("WMV", 100000, "Mid Fragments", 512, true);
//		fragmenterCL("ZIP", 100000, "Mid Fragments", 512, true);
		
		//TESTING
		fragmenterCL("DOC", 100000, "Mid Fragments", 512, true);
		fragmenterCL("GIF", 100000, "Mid Fragments", 512, true);
		fragmenterCL("JPG", 100000, "Mid Fragments", 512, true);
		fragmenterCL("WAV", 100000, "Mid Fragments", 512, true);
	}

	/**
	 * This is a streamline version of the fragmenter program. It runs via DataCruncher
	 * folder, numberofFrags, type, sizeOfFrags, booleanDeleteOld
	 */
	public static void fragmenterCL(String fName, int numberOfFrags, String type, int size, boolean deleteOld){
		//Starts the program in the specific directory
		String directory = "H://SVM/" + fName;
		File dir = new File(directory);
		File[] files = null;
		numOfFrags = numberOfFrags;
		fragmentSize = size;
		int fragCounter = 1;
		
		//Ok they've selected something, get what they've selected.
		//it's a directory so grab all ONLY files (no directories) inside of the directory

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
		
		//Create a Directory to put the file fragments, naming fragment starts at 1
		//File output = new File(files[0].getParentFile() + "\\Fragmented");
		File output = new File("H:\\SVM\\" + newDir + "\\" + files[0].getParentFile().getName());
		
		//Check to see if the directory exists...
		//If not, make it.
		if(!output.isDirectory()){
			output.mkdirs();
		} 
		//If it does exist, then see if the user wants to delete old frags?
		else if(output.isDirectory()){		
			//Delete files in Directory
			if(deleteOld){
				System.out.println("Deleting Old Fragments... (please be patient)");
				//Empty the folder
				File[] fileList = output.listFiles();
				for(File myFile : fileList){
					//Delete only non-directories...
					if(!myFile.isDirectory()){
						myFile.delete();
					}
				}
			}
			//Leave the current fragments alone and start off numbering where you left off.
			else {
				//Grab all the files
				File[] theFragments = output.listFiles();
				fragCounter = theFragments.length + 1;
				//System.out.println("Next frag = " + fragCounter);						
			}
		}
		else {
			//Something went wrong
			throw new IllegalArgumentException("Output directory is not a directory!");
		}	
		
		//fragCounter = fragmentForProblem1(files, fragCounter, output, type);
		fragCounter = fragmentForProblem2(files, fragCounter, output, type);

		//Notify User if the number of fragments was less than the number s/he wanted.
		if(fragCounter < numOfFrags) {
			System.out.println("Notice: Not enough files to reach goal number of fragments...");
			//TODO notify the original program to change the totalNumberOfFrags...
		}
		
		//Fragmenting is completed!
		System.out.println("Fragmentation Complete");
	}

	/**
	 * Fragments files for Problem 1 -> identifying the file type of 512 bytes
	 * @param files
	 * @param fragCounter
	 * @param output
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unused")
	private static int fragmentForProblem1(File[] files, int fragCounter, File output, String type) {	
		//Iterate through the files the user wants to fragment, name them fragment + (fragCounter - aka wherever the last filename left off);
		int i = 1;
		for(File f : files){
			//Check to see if we've reach the number of fragments the user wants:
			if(fragCounter < numOfFrags+1){
				System.out.println("Fragmenting file #" + i);
				System.out.println("Input: " + f.getAbsolutePath());
				System.out.println("Output: " + output);
				int temp = 0;
				//Determine how the user selected to fragment the file.
				if(type.equals("Whole File")) {
					System.out.println("User selected to fragment whole file");
					temp = fragmentFiles(f, output, fragCounter);
				} else if(type.equals("Header Fragments")) {
					System.out.println("User selected to fragment header fragments from the file");
					temp = fragmentHeaders(f, output, fragCounter);
				} else if(type.equals("Mid Fragments")) {
					System.out.println("User selected to fragment middle fragments from the file");
					temp = fragmentMids(f, output, fragCounter, i);
				} else if (type.equals("Footer Fragments")) {
					System.out.println("User selected to fragment footers fragments from the file");
					temp = fragmentFooters(f, output, fragCounter);
				} else {
					System.err.println("Error: User didn't specify how to fragment file");
				}
				fragCounter = temp;
				i++;
			} else {
				//Do nothing, we've reach the number of fragments we needed!
			}
		}
		return fragCounter;
	}

	private static int fragmentForProblem2(File[] files, int fragCounter, File output, String type) {
		//Iterate through the files the user wants to fragment, name them fragment + (fragCounter - aka wherever the last filename left off);
		int i = 1;
		//For each file: 
		for(File f : files){
			//Check to see if we've reach the number of fragments the user wants:
			if(fragCounter < numOfFrags+1){
				System.out.println("Fragmenting file #" + i);
				System.out.println("Input: " + f.getAbsolutePath());
				System.out.println("Output: " + output + " Fragmented\\File" + i);
				int temp = 0;
				
				//Determine if file is large enough to fragment...
				
				long size = 0;
				try {
					RandomAccessFile file = new RandomAccessFile(f, "r");
					size = file.length();
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(size > (512*5)){
					//Create a folder called Filei
					File dir = new File(output + "\\File" + i );
					dir.mkdir();
					//Determine how the user selected to fragment the file.
					if(type.equals("Whole File")) {
						System.out.println("User selected to fragment whole file");
						temp = fragmentFiles(f, dir, fragCounter);
					} else if(type.equals("Header Fragments")) {
						System.out.println("User selected to fragment header fragments from the file");
						temp = fragmentHeaders(f, dir, fragCounter);
					} else if(type.equals("Mid Fragments")) {
						System.out.println("User selected to fragment middle fragments from the file");
						temp = fragmentMids(f, dir, fragCounter, i);
					} else if (type.equals("Footer Fragments")) {
						System.out.println("User selected to fragment footers fragments from the file");
						temp = fragmentFooters(f, dir, fragCounter);
					} else {
						System.err.println("Error: User didn't specify how to fragment file");
					}
					fragCounter = temp;
					i++;
				} else {
					//Do nothing, we've reach the number of fragments we needed!
				}
			}
		}

		return fragCounter;
		
	}
	
	/**
	 * This method fragments a file into fragmentSize bytes sections, but only outputs the first fragmentSize bytes (header).
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentHeaders(File input, File output, int fragCounter){
		//Only use the fragmentSize bytes :)	
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//Check to see if the file is > fragmentSize bytes
			if(file.getFilePointer() + fragmentSize < file.length()){
				//Read in the fragmentSize bytes and output them.
				//Read in fragmentSize bytes
				byte[] buffer = new byte[fragmentSize];
				file.readFully(buffer);
				//Output the fragmentSize byte fragment to a incremented file named Fragment + fileCount
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
				fileCounter++;
				out.write(buffer);
				out.close();
			}
			//Otherwise the file will be less than fragmentSize bytes... so we're going to skip it?
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
	 * This method fragments a file into fragmentSize bytes sections, but removes the first fragmentSize bytes (header) and the last fragmentSize bytes (footer)
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @param i 
	 * @return int - value of the last file created.
	 */
	public static int fragmentMids(File input, File output, int fragCounter, int i){
		//Skip the first fragmentSize bytes (header), the output fragments, skip the last on (footer)
		int fileCounter = 1;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//Check to see if the file is greater than (fragmentSize*2) (fragmentSize for header, fragmentSize for mid, +whatever for footer)
			//if > (fragmentSize*2)
			if(file.getFilePointer() + (fragmentSize*2) < file.length()){
				//skip fragmentSize bytes (header)
				file.skipBytes(fragmentSize);
				//while Pointer+fragmentSize < file length... output fragment
				while(file.getFilePointer()+fragmentSize < file.length()){
					//Check to make sure we still need fragments!
					//If the fileCounter < numOfFrags, we need more, so chop them up.
					if(fragCounter < numOfFrags+1){
						//Read in fragmentSize bytes
						byte[] buffer = new byte[fragmentSize];
						file.readFully(buffer);
						//Output the fragmentSize byte fragment to a incremented file named Fragment + fileCount
						OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + i + "-" + fileCounter));
						fileCounter++;
						fragCounter++;
						out.write(buffer);
						out.close();
					} else {
						file.skipBytes(fragmentSize);
					}
				}
			}
			else{
				//Do nothing? Panic? Throw chickens?
			}
			file.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return fragCounter;
	}
	
	/**
	 * This method fragments a file into fragmentSize bytes sections, but only outputs the last fragmentSize bytes (footer)
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentFooters(File input, File output, int fragCounter){
		//Skip the first fragmentSize bytes (header),skip the output fragments, output the last on (footer)
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//Check to see if the file is greater than (fragmentSize*2) (fragmentSize for header, fragmentSize for mid, + whatever footer)
			if(file.getFilePointer() + (fragmentSize*2) < file.length()){
				//Seek fragmentSize bytes (header)
				file.skipBytes(fragmentSize);
				// while file.getFilePointer + fragmentSize < file.length)
				while(file.getFilePointer() + fragmentSize < file.length()){
					file.skipBytes(fragmentSize);
				}
				//IF the file has any leftover bytes < fragmentSize left...
				//output the leftover bytes (footer)
				if(file.getFilePointer() != file.length()){
					//Read in whatever is left and pad with 0s up to fragmentSize bytes.
					byte[] buffer = new byte[fragmentSize];
					int end = (int) (file.length() - file.getFilePointer());
					file.readFully(buffer, 0 , end);
					//Output the fragmentSize byte fragment to a incremented file named Fragment + fileCount
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
	 * This method fragments a file into fragmentSize bytes sections. Remnants of files < fragmentSize bytes are padded with zeros
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
			//While the file has more than fragmentSize bytes left...
			while(file.getFilePointer() + fragmentSize < file.length()){
				//If the fileCounter < numOfFrags, we need more, so chop them up.
				if(fileCounter < numOfFrags+1) {
					//Read in fragmentSize bytes
					byte[] buffer = new byte[fragmentSize];
					file.readFully(buffer);
					//Output the fragmentSize byte fragment to a incremented file named Fragment + fileCount
					OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Frag" + fileCounter));
					fileCounter++;
					out.write(buffer);
					out.close();
				} else {
					file.skipBytes(fragmentSize);
				}
			}
			//IF the file has any leftover bytes < fragmentSize left...
			if(file.getFilePointer() != file.length() && fileCounter < numOfFrags+1){
				//Read in whatever is left and pad with 0s up to fragmentSize bytes.
				byte[] buffer = new byte[fragmentSize];
				int end = (int) (file.length() - file.getFilePointer());
				file.readFully(buffer, 0 , end);
				//Output the fragmentSize byte fragment to a incremented file named Fragment + fileCount
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
