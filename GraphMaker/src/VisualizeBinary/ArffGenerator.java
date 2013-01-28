/**
 * 
 */
package VisualizeBinary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import VisualizeBinary.Features.Feature;
import VisualizeBinary.Features.PercentageFeature;
import VisualizeBinary.Features.TotalFeature;
import VisualizeBinary.Matrix.Matrix;

/**
 * This class will allow users to generate an .arff file from fragments
 * @author hartell
 *
 */
public class ArffGenerator {
	
	private static ArrayList<File> fragDirs;
	private static ArrayList<String> arffFile;
	
	/**
	 * MAIN
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Please select a dataset to load...");
		
		//Declare Globals
		fragDirs = new ArrayList<File>();
		arffFile = new ArrayList<String>();
		
		//Ask the user to build their dataSets by adding fragmented Files
		boolean done = false;
		while(!done){
			done = selectDataSets();
		}
		
		//Ask users what they want this relation called (@relation)
		String rName = JOptionPane.showInputDialog("Name of Relation?");
		arffFile.add("@RELATION " + rName);
		arffFile.add(" ");

		//Ask the user what metrics they want to run on the files	
		ArrayList<JCheckBox> theBoxes = new ArrayList<JCheckBox>();
	    theBoxes.add(new JCheckBox("Total Feature"));  
	    theBoxes.add(new JCheckBox("Percentage Feature"));
	    String message = "Which attributes/features would you like to calculate?";  
	    Object[] params = new Object[theBoxes.size() + 1];
	    params[0] = message;
	    for(int i = 0; i < theBoxes.size(); i++){
	    	params[i+1] = theBoxes.get(i);
	    }
	    JOptionPane.showOptionDialog(null, params, "Available attributes:", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK"}, "default");
	    
		//Create the @Attributes section of Arff file, with a specifed granularity (if needed)
	    createAttributes(theBoxes, 1);

		// Run the metrics on the file
		// For each directory of fragments...
		for (int i = 0; i < fragDirs.size(); i++) {
			// Get all files
			File[] files = fragDirs.get(i).listFiles();

			// Go through all the files
			for (int j = 0; j < files.length; j++) {
				// Grab each file
				File f = files[j];
				// Get the metrics: from the file f, with the all the attrib in theBoxes, from directory i
				String result = calcAttributes(f, theBoxes, (i+1));
				arffFile.add(result);
			}
		}
		
		
		//Write out the newly created Arff file
		makeArffFile(rName);
		
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
	
	/**
	 * Creates all the attributes for the arff file
	 * Example: @ ATTRIBUTE avgBytes numeric
	 * @param theBoxes
	 */
	public static void createAttributes(ArrayList<JCheckBox> theBoxes, int granularity){
		
		//Add in a file about the fileName (key attribute) -- will be removed from within weka
		arffFile.add("@ATTRIBUTE fName string");
		
		//Go through all the Attributes from the checkboxes and add them..
		for(JCheckBox b : theBoxes){
			
			//Get the name w/o spaces...
			String aName = b.getText();
			aName = aName.replaceAll("\\s", "");
			
			//Add the attribute to the arff file:
			if(b.isSelected()){
				int totalNumAttributes = (int) Math.pow(4, granularity);
				for(int i = 0 ; i < totalNumAttributes; i++){
					//Add an attribute for each granularity selected
					arffFile.add("@ATTRIBUTE " + aName + i + " numeric");
				}
			}
		}
		
		//Add the number of types (current based off of the number of Directories added.
		String type = "{";
		for(int i = 0; i < fragDirs.size(); i++){
			type = type + (i+1) +",";
		}
		type = type.substring(0, type.length()-1);
		type = type + "}";
		
		
		arffFile.add("@ATTRIBUTE type " + type);
		arffFile.add("");
		
		//Create the @data Section
		arffFile.add("@DATA");
	}
	
	/**
	 * Calculates each of the Attributes selected
	 * @param theBoxes - a list of JCheckBoxes containing the possible attributes.
	 */
	public static String calcAttributes(File f, ArrayList<JCheckBox> theBoxes, int dirNum){
		//Get the name of the file (key)
		String result = f.getName() + ",";
		
		//Determine which attributes we need to use
		for(int i = 0; i < theBoxes.size(); i++){
			//For all the attributes selected..
			if(theBoxes.get(i).isSelected()){
				//Get the name of the Attribute & remove all Whitespaces (for ease of equals method)
				String name = theBoxes.get(i).getText();
				name = name.replaceAll("\\s", "");				
				
				//Get the path of the file
				Path path = Paths.get(f.getAbsolutePath());
				
				//Read in all the bytes of that file
				byte[] bytes = null;
				try {
					bytes = Files.readAllBytes(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Feature feature = null;
				if(name.equalsIgnoreCase("TotalFeature")){
					//Set the feature
					feature = new TotalFeature();
				}
				else if (name.equalsIgnoreCase("PercentageFeature")){
					//Set the feature
					feature = new PercentageFeature();
				}
				else{
					System.out.println("Error: No Features Selected!");
				}
				//ADD MORE ATTRIBUTES HERE!!!!
				//Create the matrix using the feature
				Matrix m = new Matrix(1, bytes, feature);
				//Return the results of the feature
				double[][] matrix = m.getMatrix();
				
				//Add the feature's result to the arff results
				for(int k = 0; k < matrix.length; k++){
					for(int j = 0; j < matrix[k].length; j++){
						 result = result + matrix[k][j] + ",";
						 //System.out.println(matrix[k][j]);
					}
				}
			}
		}
		
		//Add the type of file this is:
		result = result + dirNum;
		
		return result;	
	}
	
	/**
	 * Generates and saves the Arff File to a predetermined location
	 * @param name - the name of the arff File
	 */
	public static void makeArffFile(String name){
		try{
			//Create a Writer, write each line out to a file.
			FileWriter fStream = new FileWriter("H://SVM/ARFF/" + name + ".arff");
			BufferedWriter out = new BufferedWriter(fStream);
			for(String s : arffFile){
				out.write(s);
				out.newLine();
			}
			out.close();
			fStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
