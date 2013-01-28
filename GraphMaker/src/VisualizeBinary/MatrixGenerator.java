/**
 * 
 */
package VisualizeBinary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import VisualizeBinary.Features.Feature;
import VisualizeBinary.Features.TotalFeature;
import VisualizeBinary.Matrix.Matrix;


/**
 * This program will take a fragment/file and generate a matrix representation of that fragment/file
 * @author hartell
 *
 */
public class MatrixGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Create a new JFileChooser and set it's starting directory
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("H://SVM"));
		int choice = chooser.showOpenDialog(null);

		//Wait until the user has selected a file
		File file = null;
		byte[] bytes = null;
		if(choice == JFileChooser.APPROVE_OPTION){
			//Get the selected File 
			file = chooser.getSelectedFile();

			//Get the path of the file
			Path path = Paths.get(file.getAbsolutePath());

			//Read in all the bytes of that file
			try {
				bytes = Files.readAllBytes(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	

		Feature feature = new TotalFeature();
		//Feature feature = new PercentageFeature();
		Matrix m = new Matrix(2, bytes, feature);
		double[][] matrix = m.getMatrix();
		
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				 System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
}
