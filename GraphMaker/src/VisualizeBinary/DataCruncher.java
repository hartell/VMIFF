/**
 * 
 */
package VisualizeBinary;

import java.io.File;
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

/**
 * This class will use the output from the matrix generator to analyse the data with Weka
 * @author hartell
 *
 */
public class DataCruncher {
	
	private static ArrayList<File> fragDirs;
	private static ArrayList<String> arffFile;
	private static ArrayList<Instance> instances;
	private static int granularity = 1;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Please select a dataset to load...");
		
		//Declare Globals
		fragDirs = new ArrayList<File>();
		arffFile = new ArrayList<String>();
		instances = new ArrayList<Instance>();
		
		//Ask the user to build their dataSets by adding fragmented Files
		boolean done = false;
		while(!done){
			done = ArffGenerator.selectDataSets(fragDirs);
		}
		
		//Ask users what they want this relation called (@relation)
		String rName = JOptionPane.showInputDialog("Name of Relation?");
		arffFile.add("@RELATION " + rName);
		arffFile.add(" ");

		//Ask the user what metrics they want to run on the files	
		ArrayList<JCheckBox> theBoxes = new ArrayList<JCheckBox>();
	    theBoxes.add(new JCheckBox("Total Feature"));  
	    theBoxes.add(new JCheckBox("Percentage Feature"));
	    theBoxes.add(new JCheckBox("Total Age Feature"));
	    theBoxes.add(new JCheckBox("Average Age Feature"));
	    String message = "Which attributes/features would you like to calculate?";  
	    Object[] params = new Object[theBoxes.size() + 1];
	    params[0] = message;
	    for(int i = 0; i < theBoxes.size(); i++){
	    	params[i+1] = theBoxes.get(i);
	    }
	    JOptionPane.showOptionDialog(null, params, "Available attributes:", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"OK"}, "default");
	    
		//Create the @Attributes section of Arff file, with a specified granularity
	    arffFile = ArffGenerator.createAttributes(arffFile, fragDirs, theBoxes, granularity);

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
				String result = ArffGenerator.calcAttributes(f, theBoxes);
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
		System.out.println(data.size());
		
		//Build Classifier
		System.out.println("Building Classifier...");
		Classifier randForest = new RandomForest(10);
		//Classifier naiveBayes = new NaiveBayesClassifier(true,true,false);
		//naiveBayes.buildClassifier(data);
		
		//Build the 10-fold default CrossValidation
		System.out.println("Performing Cross Validation...");
		//CrossValidation cv = new CrossValidation(naiveBayes);
		CrossValidation cv = new CrossValidation(randForest);
		
		//Get Results of cross validation
		System.out.println("Maping results...");
		Map<Object, PerformanceMeasure> map = cv.crossValidation(data);
		
		//Print the results:
		System.out.println("MAP 1 ------------------");
		System.out.println("Accuracy: " + map.get(1).getAccuracy());
		System.out.println("BCR: "+ map.get(1).getBCR());
		System.out.println("Correlation: "+ map.get(1).getCorrelation());
		System.out.println("Coefficient: "+ map.get(1).getCorrelationCoefficient());
		System.out.println("Cost: "+ map.get(1).getCost());
		System.out.println("Error Rate: "+ map.get(1).getErrorRate());
		System.out.println("FMeasure: "+ map.get(1).getFMeasure());
		System.out.println("FN Rate: "+ map.get(1).getFNRate());
		System.out.println("FP Rate: "+ map.get(1).getFPRate());
		System.out.println("Precision: "+ map.get(1).getPrecision());
		System.out.println("Q9: "+ map.get(1).getQ9());
		System.out.println("Recall: "+ map.get(1).getRecall());
		System.out.println("TN Rate: "+ map.get(1).getTNRate());
		System.out.println("Total: "+ map.get(1).getTotal());
		System.out.println("TP Rate: "+ map.get(1).getTPRate());
		
		System.out.println();
		
		System.out.println("MAP 2 ------------------");
		System.out.println("Accuracy: " + map.get(2).getAccuracy());
		System.out.println("BCR: "+ map.get(2).getBCR());
		System.out.println("Correlation: "+ map.get(2).getCorrelation());
		System.out.println("Coefficient: "+ map.get(2).getCorrelationCoefficient());
		System.out.println("Cost: "+ map.get(2).getCost());
		System.out.println("Error Rate: "+ map.get(2).getErrorRate());
		System.out.println("FMeasure: "+ map.get(2).getFMeasure());
		System.out.println("FN Rate: "+ map.get(2).getFNRate());
		System.out.println("FP Rate: "+ map.get(2).getFPRate());
		System.out.println("Precision: "+ map.get(2).getPrecision());
		System.out.println("Q9: "+ map.get(2).getQ9());
		System.out.println("Recall: "+ map.get(2).getRecall());
		System.out.println("TN Rate: "+ map.get(2).getTNRate());
		System.out.println("Total: "+ map.get(2).getTotal());
		System.out.println("TP Rate: "+ map.get(2).getTPRate());
			
		//Write out the newly created Arff file
		ArffGenerator.makeArffFile(rName, arffFile);
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
