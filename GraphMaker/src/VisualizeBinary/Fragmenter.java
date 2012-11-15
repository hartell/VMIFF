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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class will take a single file, multiple files or an entire directory and fragment it into 512 byte fragments.
 * If a file is less than the 512 byte section, it fills the end in with zeros. (not sure if proper way to do this...)
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
			
			//Create a Directory to put the file fragments
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
				else{
					//Leave the folder and start off numbering where you left off.
				}
			}
			else {
				//Something went wrong
				throw new IllegalArgumentException("Output directory is not a directory!");
			}
			
			//Iterate through the files the user wants to fragment, name them fragment + (fragCounter - aka wherever the last filename left off);
			int i = 1;
			int fragCounter = 1;
			for(File f : files){
				System.out.println("Fragmenting file #" + i);
				System.out.println("Input: " + f.getAbsolutePath());
				System.out.println("Output: " + f.getParentFile() + "\\Fragmented");
				int temp = fragmentFile(f, output, fragCounter);
				fragCounter = temp;
				i++;
			}
		}
		System.out.println("Fragmentation Complete");
	}
	
	/**
	 * This method fragments a file into 512 bytes sections. Remnants of files < 512 bytes are padded with zeros (not sure if right...)
	 * @param input - the file to be fragmented
	 * @param output - the output location
	 * @param fragCounter - the last numerical fragment number
	 * @return int - value of the last file created.
	 */
	public static int fragmentFile(File input, File output, int fragCounter){
		//Thanks to Ben Holland for assistance with RandomAccessFiles!
		int fileCounter = fragCounter;
		try {
			//Create a randomAccessFile to fragment from
			RandomAccessFile file = new RandomAccessFile(input, "r");
			//While the file has more than 512 bytes left...
			while(file.getFilePointer()+512 < file.length()){
				//Read in 512 bytes
				byte[] buffer = new byte[512];
				file.readFully(buffer);
				//Output the 512 byte fragment to a incremented file named Fragment + fileCount
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Fragmented" + fileCounter));
				fileCounter++;
				out.write(buffer);
				out.close();
			}
			//IF the file has any leftover bytes < 512 left...
			if(file.getFilePointer() != file.length()-1){
				//Read in whatever is left and pad with 0s upto 512 bytes.
				byte[] buffer = new byte[512];
				int end = (int) (file.length() - 1 - file.getFilePointer());
				file.readFully(buffer, 0 , end);
				//Output the 512 byte fragment to a incremented file named Fragment + fileCount
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Fragmented" + fileCounter));
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
