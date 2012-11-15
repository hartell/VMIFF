/**
 * 
 */
package VisualizeBinary;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class will allow users to generate an .arff file from fragments
 * @author hartell
 *
 */
public class ArffGenerator {
	
	private static ArrayList<File> fragDirs;
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Please select a dataset to load...");
		
		fragDirs = new ArrayList<File>();
		
		//Ask the user to build their dataSets by adding fragmented Files
		boolean done = false;
		while(!done){
			done = selectDataSets();
		}
		System.out.println("Dataset size = " + fragDirs.size());
		
		//Ask the user what metrics they want to run on the files
		//TODO: Maybe JCheckBox?
		
		//Create the @Attributes section of Arff file
		
		//Create the @data Section
		//Run the metrics on the file
		
		//Write out the newly created Arff file	
		
	}
	
	/**
	 * Allows the user to select the data sets for the arff file
	 * @return boolean - if the user is finished selecting sets
	 */
	public static boolean selectDataSets(){
		boolean done = false;
		
		//Create a JFileChooser to allow the user to navigate to the selectable dataset directory
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a dataset");
		chooser.setCurrentDirectory(new File("H://SVM/"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int option = chooser.showOpenDialog(null);
		
		//If the user has added a folder to the train data
		if(option == JFileChooser.APPROVE_OPTION){
			//Add the files to the list
			File dir = chooser.getSelectedFile();
			fragDirs.add(dir);
			System.out.println("Added to dataset: " + dir.getAbsolutePath());
		}
		//If the user has selected cancel
		else if(option == JFileChooser.CANCEL_OPTION){
			//TODO: Cancel
		}
		else{
			//TODO: Quit the program?
		}
		
		//Ask the user if the want to add more data to the set?
		int addMore = JOptionPane.showConfirmDialog(null, "Add another data set?", "Add more?", JOptionPane.YES_NO_OPTION);
		if(addMore == JOptionPane.YES_OPTION){
			done = false;
		}
		else if(addMore == JOptionPane.NO_OPTION){
			done = true;
		}
		else{
			//TODO: Quit the program?
		}
		return done;
	}

}
