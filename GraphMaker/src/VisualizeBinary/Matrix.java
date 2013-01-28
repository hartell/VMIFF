/**
 * 
 */
package VisualizeBinary;

import java.util.ArrayList;
import VisualizeBinary.Features.Feature;

/**
 * @author hartell
 *
 */
public class Matrix {

	private int granularity;
	private byte[] bytes;
	private int range;
	private int numGrains;
	private double[][] matrix;
	
	/**
	 * Constructor
	 * @param bytes - the bytes we're graphing
	 * @param granularity - 0 to 8, calculated 4^granularity
	 */
	@SuppressWarnings("unchecked")
	public Matrix(int granularity, byte[] theBytes, Feature feature){
		//Check to make sure granularity is between the required amounts
		if(granularity < 0 || granularity > 8){
			throw new IllegalArgumentException("Granularity must be between 0 & 8");
		}
		
		//Assign local private variables
		this.granularity = granularity;
		bytes = theBytes;
		range = (int) Math.sqrt(Math.pow(4, granularity));
		numGrains = (int) Math.pow(4, granularity);
		ArrayList<Point>[][] grains = (ArrayList<Point>[][])new ArrayList[range][range];
		
		//Initialize the grains array
		for(int i = 0; i < grains.length; i++){
			for(int j = 0; j < grains[i].length; j++){
				grains[i][j] = new ArrayList<Point>();
			}
		}
		
		//Calculate the totals for the matrix
		for(int i = 0; i < bytes.length - 1; i = i + 2){
			//Get the X, Y
			int x = unsignByte(bytes[i]);
			int y = unsignByte(bytes[i+1]);
			
			//Debug:
			//System.out.println(x + ","+ y);
			
			//Make a new point
			Point p = new Point(i, x, y);
			
			//Determine the correct bucket for the given grain.
			grains[getGrainBucket(x, range)][getGrainBucket(y, range)].add(p);
		}
		
		//Go through each grain and calculate the feature
		matrix = new double[range][range];
		for(int i = 0; i < grains.length; i++){
			for(int j = 0; j < grains[i].length; j++){
				 matrix[i][j] = feature.computeFeature(grains[i][j]);
			}
		}
	}
	
	/**
	 * Determines the correct bucket given the x or y value
	 * @param value - the x or y value
	 * @param range 
	 * @return
	 */
	private int getGrainBucket(int value, int range){
		int bucket = 0;
		int boundary = 256/range;
		for(int i = 0; i < 256; i = i + boundary){
			if(value >= i && value < (i + boundary)){
				return bucket;
			}
			else{
				bucket++;
			}
		}
		return -1; //Error;
	}
	
	/**
	 * Returns the unsigned bytes
	 * @param b - the signed bytes
	 * @return - the unsigned bytes
	 */
	public static int unsignByte(byte b) {
	    return b & 0xFF;
	}
	
	/**
	 * Returns the number of Sectors
	 * @return
	 */
	public int getGranularity(){
		return granularity;
	}
	
	/**
	 * Returns the number of grains (buckets)
	 * @return
	 */
	public int getNumGrains(){
		return numGrains;
	}
	
	/**
	 * Returns the bytes
	 * @return
	 */
	public byte[] getBytes(){
		return bytes;
	}
	
	/**
	 * Returns the matrix!
	 * @return
	 */
	public double[][] getMatrix(){
		return matrix;
	}
	
	/**
	 * Returns the range
	 * @return
	 */
	public int getRange(){
		return range;
	}
	
	/**
	 * Returns the binary representation of the byte.
	 * @param b - the byte
	 * @return
	 */
	public static String byteToBinaryString(byte b){
		String result = "";
		byte[] reference = new byte[]{(byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
		for (int i = 0; i < 8; i++) {
			if ((reference[i] & b) != 0) {
				result += "1";
			}
			else {
				result += "0";
			}
		}
		return result;
	}

}
