/**
 * 
 */
package VisualizeBinary;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JCheckBox;
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
	 * MAIN
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Please select a dataset to load...");
		
		//Declare Globals
		fragDirs = new ArrayList<File>();
		
		//Ask the user to build their dataSets by adding fragmented Files
		boolean done = false;
		while(!done){
			done = selectDataSets();
		}
		System.out.println("Dataset size = " + fragDirs.size());
		
		//Ask users what they want this relation called (@relation)
		String rName = JOptionPane.showInputDialog("Name of Relation?");
		System.out.println("@RELATION " + rName);
		
		System.out.println("");

		//TODO: Ask the user what metrics they want to run on the files	
		ArrayList<JCheckBox> theBoxes = new ArrayList<JCheckBox>();
	    theBoxes.add(new JCheckBox("Average Bytes"));  
	    theBoxes.add(new JCheckBox("Feature A"));
	    theBoxes.add(new JCheckBox("Feature B"));
	    String message = "Which attributes/features would you like to calculate?";  
	    Object[] params = new Object[theBoxes.size() + 1];
	    params[0] = message;
	    for(int i = 0; i < theBoxes.size(); i++){
	    	params[i+1] = theBoxes.get(i);
	    }
	    JOptionPane.showOptionDialog(null, params, "Available attributes:", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK"}, "default");
	    
		//TODO: Create the @Attributes section of Arff file
		System.out.println("@ATTRIBUTE fName string");
		System.out.println("@ATTRIBUTE avgBytes numeric");
		System.out.println("@ATTRIBUTE type {1,2,3,4,5,6,7}");
		
		System.out.println("");
		
		//TODO: Create the @data Section
		System.out.println("@DATA");
		//TODO: Run the metrics on the file
		//For each directory of fragments...
		for (int i = 0; i < fragDirs.size(); i++){
			//Get all files
			File[] files = fragDirs.get(i).listFiles();			
			
			//Go through all the files
			for(int j = 0; j < files.length; j++){
				//Grab each file
				File f = files[j];
				//Run metric
				int avg = AttributeUtils.getAvgByte(f);
				//Output value -- fName, avg, type(i + 1)
				System.out.println(f.getName() + "," + avg + "," + (i + 1));
			}
		}
		
		//TODO: Write out the newly created Arff file
		//TODO: Ask the user for name & save location?
		
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
			System.out.println("Added to dataset: " + dir.getAbsolutePath() + " as type = " + (fragDirs.size()) );
		}
		else{
			//Cancel the adding of a data set file
		}
		
		//Ask the user if the want to add more data to the set?
		int addMore = JOptionPane.showConfirmDialog(null, "Add another data set?", "Add more?", JOptionPane.YES_NO_OPTION);
		if(addMore == JOptionPane.YES_OPTION){
			done = false;
		}
		else if(addMore == JOptionPane.NO_OPTION){
			done = true;
		}
		else if(addMore == JOptionPane.CLOSED_OPTION){
			done = true;
		}
		
		return done;
	}
}
