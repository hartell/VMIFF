/**
 * 
 */
package VisualizeBinary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class will contain all the methods for calculating the various features we want to test
 * @author hartell
 *
 */
public class FeatureUtils {
	
	/**
	 * A method which gets the average byte value
	 * @return - the average byte value
	 */
	public static int getAvgByte(File f){
		
		//Get the path of the file
		Path path = Paths.get(f.getAbsolutePath());
		
		//Read in all the bytes of that file
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Average bytes
		int avg = 0;
		for(byte b : bytes){
			avg = avg + b;
		}
		
		avg = avg / 512;
		//System.out.println("Average Bytes: " + avg);
		
		return avg;
	}

}
