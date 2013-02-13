/**
 * 
 */
package VisualizeBinary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.classification.tree.RandomForest;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.weka.WekaClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;

/**
 * This class will use the output from the matrix generator to analyze the data with Weka
 * @author hartell
 *
 */
public class DataCruncher {

	private static int granularity = 1;
	private static boolean useGUI = true;

	public static void main(String[] args){
		//If the user wants to use the gui....
		if(useGUI){
			getCrunchyGUI();
		} else {
			//Format [group types], [group2 types]:nameOfFile:numberFragsPerGroup:Type:SizeOfFrags:DeleteOldFrags:[Features]
			//Example: [PDF],[GIF DOC]:PdfVsGifDoc:5000:[TotalPoints TotalAge]
			
			//Read in text file: 
			String test = "[PDF],[PPT]:PdfVsPpt:100:Mid Fragments:512:true:[TotalPointsFeature TotalAgeFeature]";
			
			//Split the line by ":" marker into seven sections (folders, filename, numberofFrags, type, sizeOfFrags, booleanDeleteOld, features).
			String[] section = test.split(":");
			
			//For each section
			String[] groups = getFolderNames(section[0]);
			String fileName = section[1];
			int numOfFrags = Integer.parseInt(section[2]);
			String typeOfFrag = section[3];
			int sizeOfFrag = Integer.parseInt(section[4]);
			boolean deleteOld = Boolean.parseBoolean(section[5]);
			String[] features = getFeatures(section[6]);
			
			//Use Fragmenter to fragment the files!
			for(String name : groups){
				//Split on space
				String[] folders = name.split(" ");
				numOfFrags = numOfFrags / folders.length;
				for(String f : folders){
					System.out.println("Preparing to fragment: " + f);
					Fragmenter.fragmenterCL(f, numOfFrags, typeOfFrag, sizeOfFrag, deleteOld);
					System.out.println("");
				}
			}
			
			//TODO for all the lines in the script
			
			//Now files are fragmented we just need to add them to the dataset
			//Build their dataSets by adding fragmented Files
			//ArrayList<File> fragDirs = ArffGenerator.selectDataSets(groups);
			ArrayList<String> arffFile = new ArrayList<String>();
			ArrayList<Instance> instances = new ArrayList<Instance>();
			
			//Ask users what they want this relation called (@relation)
			String rName = fileName;
			arffFile.add("@RELATION " + rName);
			arffFile.add(" ");
			
			//Determine the features:
			//Create the @Attributes section of Arff file, with a specified granularity
			arffFile = ArffGenerator.createAttribute(arffFile, groups.length, features, granularity);
			
			
			ArffGenerator.makeArffFile(rName, arffFile);
			
			//For each group:
			for(int j = 0; j < groups.length; j ++){
				String[] folders = groups[j].split(" ");
				System.out.println("Group:" + groups[j]);
				//For each folder in that group
				for(String folder : folders){
					System.out.println("Folder: H://SVM/" + folder + "/Fragmented");
					//Get all the files
					File dir = new File("H://SVM/" + folder + "/Fragmented");
					File[] files = dir.listFiles();
					
					//Go through all the files
					for (int i = 0; i < files.length; i++) {
						File f = files[i];
						//Get the metrics, given the features, and granularity
						String result = ArffGenerator.calcAttributes(f, features, granularity);
						System.out.println("Result = " + result);
						int dirNum = j+1;

						//Add it to the Arff File
						arffFile.add(f.getName() + "," + result + dirNum);

						//Create the instance from the result;
						String[] stringData = result.split(",");
						double[] data = new double[stringData.length];

						for(int m = 0; m < stringData.length; m++){
							data[m] = Double.parseDouble(stringData[m]);
							//System.out.println("adding " + stringData[m]);
						}
						instances.add(new DenseInstance(data, dirNum));
					}
				}

			}
			
			//Write out the newly created Arff file
			ArffGenerator.makeArffFile(rName, arffFile);	
			
			//Create a dataset from all the instances
			System.out.println("Making dataset...");
			Dataset data = makeDataSet(instances);

			//Run a classifier
			//runRandomForestClassifier(data, arffFile, rName);
			//runNaiveBaysClassifier(data, arffFile, rName);
			runJ48Classifier(data, arffFile, rName);		
		}
	}
	
	private static String[] getFeatures(String f) {
		//First we need the directory, and remove the brackets
		String[] features = f.split(" ");
		for(int i = 0; i < features.length; i++){
			features[i] = features[i].replace("[", "");
			features[i] = features[i].replace("]", "");
		}
		return features;
	}

	/**
	 * Gets the folder names from the given string
	 * @param fNames - the section containing all the file names to fragment
	 * @return
	 */
	private static String[] getFolderNames(String fNames) {
		//First we need the directory, and remove the brackets
		String[] folderNames = fNames.split(",");
		for(int i = 0; i < folderNames.length; i++){
			folderNames[i] = folderNames[i].replace("[", "");
			folderNames[i] = folderNames[i].replace("]", "");
		}
		return folderNames;
	}

	public static void getCrunchyGUI() {
		System.out.println("Please select a dataset to load...");

		//Declare Globals
		ArrayList<File> fragDirs = new ArrayList<File>();
		ArrayList<String> arffFile = new ArrayList<String>();
		ArrayList<Instance> instances = new ArrayList<Instance>();

		//Ask the user to build their dataSets by adding fragmented Files
		boolean done = false;
		while(!done){
			done = ArffGenerator.selectDataSetsGUI(fragDirs);
		}

		//Ask users what they want this relation called (@relation)
		String rName = JOptionPane.showInputDialog("Name of Relation?");
		arffFile.add("@RELATION " + rName);
		arffFile.add(" ");

		//Ask the user what metrics they want to run on the files	
		ArrayList<JCheckBox> theBoxes = new ArrayList<JCheckBox>();
		theBoxes.add(new JCheckBox("Total Points Feature", true));  
		//theBoxes.add(new JCheckBox("Percentage Feature")); // same as total Feature
		theBoxes.add(new JCheckBox("Total Age Feature",true));
		theBoxes.add(new JCheckBox("Average Age Feature"));
		String message = "Which attributes/features would you like to calculate?";  
		Object[] params = new Object[theBoxes.size() + 1];
		params[0] = message;
		for(int i = 0; i < theBoxes.size(); i++){
			params[i+1] = theBoxes.get(i);
		}
		JOptionPane.showOptionDialog(null, params, "Available attributes:", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK"}, "default");

		//Create the @Attributes section of Arff file, with a specified granularity
		arffFile = ArffGenerator.createAttributesGUI(arffFile, fragDirs, theBoxes, granularity);

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
				String result = ArffGenerator.calcAttributesGUI(f, theBoxes, granularity);
				int dirNum = i+1;
				arffFile.add(f.getName() + "," + result + dirNum);
				//Create the instance from the result;
				String[] stringData = result.split(",");
				double[] data = new double[stringData.length];
				for(int m = 0; m < stringData.length; m++){
					data[m] = Double.parseDouble(stringData[m]);
					//System.out.println("adding " + stringData[m]);
				}
				instances.add(new DenseInstance(data, dirNum));
			}
		}

		//Create a dataset from all the instances
		System.out.println("Making dataset...");
		Dataset data = makeDataSet(instances);

		//Run a classifier
		//runRandomForestClassifier(data, arffFile, rName);
		//runNaiveBaysClassifier(data, arffFile, rName);
		runJ48Classifier(data, arffFile, rName);

		//Write out the newly created Arff file
		ArffGenerator.makeArffFile(rName, arffFile);
	}

	/**
	 * This will attempt to classifier the data using the J48 decision tree
	 * @param data
	 */
	private static void runJ48Classifier(Dataset data, ArrayList<String> arffFile, String rName) {
		//Build a classifier
		J48 j48 = new J48();
		Classifier nBays = new WekaClassifier(j48);

		//Build the 10-fold default CrossValidation
		System.out.println("Performing Cross Validation...");
		CrossValidation cv = new CrossValidation(nBays);

		//Get Results of cross validation
		System.out.println("Mapping results...");
		Map<Object, PerformanceMeasure> map = cv.crossValidation(data);

		//Print the results:
		outputResults(map, arffFile, rName);
		
	}

	/**
	 * This will attempt to classify the data using the Naive Bays Classifier
	 * @param data
	 */
	@SuppressWarnings("unused")
	private static void runNaiveBaysClassifier(Dataset data, ArrayList<String> arffFile, String rName) {
		//Build a classifier
		NaiveBayes naiveBays = new NaiveBayes();
		Classifier nBays = new WekaClassifier(naiveBays);

		//Build the 10-fold default CrossValidation
		System.out.println("Performing Cross Validation...");
		CrossValidation cv = new CrossValidation(nBays);

		//Get Results of cross validation
		System.out.println("Mapping results...");
		Map<Object, PerformanceMeasure> map = cv.crossValidation(data);

		//Print the results:
		outputResults(map, arffFile, rName);
	}


	/**
	 * This will attempt to classify the data using the Random Forest Classifier
	 * Random forest of 10 trees, each constructed considering 5 random features
	 * @param data
	 */
	@SuppressWarnings("unused")
	private static void runRandomForestClassifier(Dataset data, ArrayList<String> arffFile, String rName) {
		//Build Classifier
		System.out.println("Building Classifier...");
		Classifier randForest = new RandomForest(10);

		//Build the 10-fold default CrossValidation
		System.out.println("Performing Cross Validation...");
		CrossValidation cv = new CrossValidation(randForest);

		//Get Results of cross validation
		System.out.println("Mapping results...");
		Map<Object, PerformanceMeasure> map = cv.crossValidation(data);

		//Print the results:
		outputResults(map, arffFile, rName);
	}
	
	/**
	 * Prints stats for the classifier
	 * @param map - the results of the states
	 */
	private static void outputResults(Map<Object, PerformanceMeasure> map, ArrayList<String> arffFile, String rName) {
		
		ArrayList<Double> tpRates = new ArrayList<Double>();
		
		try{
			//Create a Writer, write each line out to a file.
			System.out.println("Outputting Results");
			FileWriter fStream = new FileWriter("H://SVM/Reports/" + rName + "Report.csv");
			BufferedWriter out = new BufferedWriter(fStream);
			//Write out the name of the relation
			out.write(arffFile.get(0));
			out.newLine();
			
			//Calculate all the things...
			for(int i = 1; i < 3; i++){
				System.out.println("MAP " + i + ": ------------------");
				double tPRate = map.get(i).getTPRate();
				tpRates.add(tPRate);
				double fPRate = map.get(i).getFPRate();
				double tNRate = map.get(i).getTNRate();
				double fNRate = map.get(i).getFNRate();
				double accuracy = map.get(i).getAccuracy();
				double bcr = map.get(i).getBCR();
				double correlation = map.get(i).getCorrelation();
				double corrCoefficient = map.get(i).getCorrelationCoefficient();
				double cost = map.get(i).getCost();
				double errorRate = map.get(i).getErrorRate();
				double fMeasure = map.get(i).getFMeasure();
				double precision = map.get(i).getPrecision();
				double q9 = map.get(i).getQ9();
				double recall = map.get(i).getRecall();
				double total = map.get(i).getTotal();
				
//				Output all the things:
				out.write("File type " + i + ": ");
				out.write("TP Rate: "+ tPRate + ",");
				out.write("FP Rate: "+ fPRate + ",");
				out.write("TN Rate: "+ tNRate + ",");
				out.write("FN Rate: "+ fNRate  + ",");
				out.write("Accuracy: " + accuracy + ",");
				out.write("BCR: "+ bcr + ",");
				out.write("Correlation: "+ correlation + ",");
				out.write("Coefficient: "+ corrCoefficient + ",");
				out.write("Cost: "+ cost + ",");
				out.write("Error Rate: "+ errorRate + ",");
				out.write("FMeasure: "+ fMeasure + ",");
				out.write("Precision: "+ precision + ",");
				out.write("Q9: "+ q9 + ",");
				out.write("Recall: "+ recall + ",");
				out.write("Total: "+ total  + ",");
				//Line Break
				out.newLine();
			}
			
			//Calculate the number of correct/incorrect classifications
			double avgTP = 0.0;
			for(Double d : tpRates){
				avgTP = avgTP + d;
			}
			avgTP = avgTP / tpRates.size();
			
			double total = map.get(1).getTotal();
			
			double correctClassifications = total * avgTP;
			double incorrectClassifications = total - correctClassifications;
			
			DecimalFormat df = new DecimalFormat("00.00%");
			
			out.newLine();
			out.write("Correct Classifications: " + correctClassifications + ", ");
			out.write("Percentage: " + (df.format(correctClassifications/total)));
			out.newLine();
			out.write("Incorrect Classifications: " + incorrectClassifications + ", ");
			out.write("Percentage: " + (df.format(incorrectClassifications/total)));
			
			
			//Close the important bits
			out.close();
			fStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Adds all the instances to the dataset
	 * @param instanceList - a list of all instances
	 * @return the dataset
	 */
	private static Dataset makeDataSet(ArrayList<Instance> instanceList) {
		Dataset dataset = new DefaultDataset();
		for(int i = 0; i < instanceList.size(); i++){
			dataset.add(instanceList.get(i));
		}

		return dataset;
	}
}
