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
 * @author hartell
 *
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
		chooser.setCurrentDirectory(new File("H://SVM/"));
		if(answer == 0){
			System.out.println("User selected a fragment a Directory");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else{
			System.out.println("User selected to fragment File/s");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(true);
		}
		
		//Let the user select what they want to fragment
		int selection = chooser.showOpenDialog(null);
		
		if(selection == JFileChooser.APPROVE_OPTION){
			File[] files = null;
			//aka file/s were selected
			if(answer == 1){ 
				files = chooser.getSelectedFiles();
			}
			//it's a directory so grab all the files inside of the directory
			else {
				File dir = chooser.getSelectedFile();
				System.out.println("Dir Path: " + dir.getAbsolutePath());
				if(dir.isDirectory()){
					File[] listOfFiles = dir.listFiles();
					ArrayList<File> tempFileHolder = new ArrayList<File>();
					for(File f : listOfFiles){
						if(!f.isDirectory()){
							tempFileHolder.add(f);
						}
					}
					
					files = new File[tempFileHolder.size()];
					for(int i = 0; i < tempFileHolder.size(); i++){
						files[i] = tempFileHolder.get(i);
					}
					System.out.println(files.length);
				}
				else
					System.out.println("Directory not selected");
			}
			
			//Create a Directory to put the file fragments in if it's not already there...
			File output = new File(files[0].getParentFile() + "\\Fragmented");
			
			// create a directory to put file fragments in if its not already there
			if(!output.isDirectory()){
				output.mkdirs();
			} 
			else if(output.isDirectory()){
				//Ask user if they want to delete currently existing fragments
				int emptyFolder = JOptionPane.showConfirmDialog(null, "Delete current Fragments?", "Delete?", JOptionPane.YES_NO_OPTION);
				
				//TODO: Delete files in Directory
				if(emptyFolder == JOptionPane.YES_OPTION){
					System.out.println("Deleting Old Fragments");
					//Empty the folder
					File[] fileList = output.listFiles();
					for(File myFile : fileList){
						System.out.println("Trying to delete: " + myFile.getAbsolutePath());
						myFile.delete();
					}
				}
				else{
					//Leave the folder and start off numbering where you left off.
				}
			}
			else {
				throw new IllegalArgumentException("Output directory is not a directory!");
			}
			
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
	}
	
	public static int fragmentFile(File input, File output, int fragCounter){	
		int fileCounter = fragCounter;
		//Create a randomAccessFile to fragment from
		try {
			RandomAccessFile file = new RandomAccessFile(input, "r");
			
			while(file.getFilePointer()+512 < file.length()){
				byte[] buffer = new byte[512];
				file.readFully(buffer);
				OutputStream out = new FileOutputStream(new File(output.getAbsolutePath() + File.separatorChar + "Fragmented" + fileCounter));
				fileCounter++;
				out.write(buffer);
				out.close();
			}
			if(file.getFilePointer() != file.length()-1){
				byte[] buffer = new byte[512];
				int end = (int) (file.length() - 1 - file.getFilePointer());
				file.readFully(buffer, 0 , end);
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
