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
		//byte[] bytes = {0, 0, 5, 0, 5, 0, 1, 5, -5, 2, -6, -6, 5, -6, - 7, 0};
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
		getPoints(bytes);
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static void getPoints(byte[] bytes){
		//Go through all given bytes and add them the 
		int[][] quad1 = new int[130][130]; // top right
		int[][] quad2 = new int[130][130]; // top left
		int[][] quad3 = new int[130][130]; // bottom left
		int[][] quad4 = new int[130][130]; // bottom right
		
		//Set up as (1st byte, 2nd byte), (3rd byte, 4th byte), ...
		int x = 0;
		int y = 0;

		for(int i = 0; i < bytes.length - 1; i= i+2){
			//Get x and y values
			x = bytes[i];
			y = bytes[i+1];
			
			//Determine which quad the point will go into
			if(x >= 0 && y >= 0){
				System.out.println("Plotting point: (" +  + x + ", " + y + ") in quad 1");
				quad1[x][y] = quad1[x][y] + 1;
			}
			else if(x < 0 && y > 0){
				System.out.println("Plotting point: (" +  + x + ", " + y + ") in quad 2");
				x = Math.abs(x);
				quad2[x][y] = quad2[x][y] + 1;
			}
			else if(x <= 0 && y <= 0){
				System.out.println("Plotting point: (" +  + x + ", " + y + ") in quad 3");
				x = Math.abs(x);
				y = Math.abs(y);
				quad3[x][y] = quad3[x][y] + 1;
			}
			else if(x > 0 && y < 0){
				System.out.println("Plotting point: (" +  + x + ", " + y + ") in quad 4");
				y = Math.abs(y);
				quad4[x][y] = quad4[x][y] + 1;
			}
			else{
				System.out.println("Error: unable to plot point: " + x + ", " + y);
			}
		}
		
		for(int i = 0; i<130; i++){
			for(int j = 0; j <130; j++){
				System.out.print(quad1[i][j] +",");
			}
			System.out.println();
		}	
	}
}
