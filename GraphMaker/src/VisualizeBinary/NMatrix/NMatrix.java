/**
 * 
 */
package VisualizeBinary.NMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import VisualizeBinary.Constants;
import VisualizeBinary.Features.Feature;



/**
 * @author hartell
 *
 */
public class NMatrix {

	private int granularity;
	private byte[] bytes;
	private int range;
	private int numGrains;
	private TreeMap<NCoordinates, Double> matrix;
	
	/**
	 * Constructor
	 * @param n - the number of dimensions
	 * @param bytes - the bytes we're graphing
	 * @param granularity - 0 to 8, calculated 4^granularity
	 */
	public NMatrix(int n, int granularity, byte[] bytes, Feature feature){
		// check to make sure granularity is between the required amounts
		if(granularity < 0 || granularity > 8){
			throw new IllegalArgumentException("Granularity must be between 0 & 8");
		}
		
		// check to make sure the block bytes can be evenly divided into the number of dimensions
		if(Constants.BLOCK_SIZE % n != 0){
			throw new IllegalArgumentException("The block bytes must evenly divided into the number of dimensions.");
		}
		
		// assign local private variables
		this.granularity = granularity;
		this.bytes = bytes;
		range = (int) Math.sqrt(Math.pow(4, granularity));
		numGrains = (int) Math.pow(4, granularity);
		
		// initialize our grains HashMap with all possible coordinate values as keys and empty points ArrayList as values 
		HashMap<NCoordinates, ArrayList<NPoint>> grains = new HashMap<NCoordinates, ArrayList<NPoint>>();
		
		// calculate the totals for the matrix
		for(int i = 0; i < bytes.length; i = i + n){
			
			// get the NPoint coordinates
			int[] coordinates = new int[n];
			for(int j=0; j<n; j++){
				coordinates[j] = getGrainBucket(unsignByte(bytes[i+j]), range);
			}
			
			// make a new point
			NCoordinates c = new NCoordinates(coordinates);
			NPoint p = new NPoint(i, c);
			
			// gets the correct bucket and adds point p to it.
			if(grains.get(c) != null){
				grains.get(c).add(p);
			} else {
				ArrayList<NPoint> points = new ArrayList<NPoint>();
				points.add(p);
				grains.put(c, points);
			}
		}
		
		matrix = new TreeMap<NCoordinates, Double>();
		
		for (int i=0; i<(int) Math.pow(range,n); i++) {
        	int[] temp = new int[n];
        	int index = 0;
            for (int j=n-1; j>=0; j--) {
            	temp[index++] = (i/(int) Math.pow(range, j)) % range;
            }
            NCoordinates coordinates = new NCoordinates(temp);
            ArrayList<NPoint> points = grains.get(coordinates);
            if(points != null){
            	matrix.put(coordinates, feature.computeNFeature(points));
            } else {
            	matrix.put(coordinates, feature.computeNFeature(new ArrayList<NPoint>()));
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
		int boundary = Constants.MAX_BYTES/range;
		for(int i = 0; i < Constants.MAX_BYTES; i = i + boundary){
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
	
	public TreeMap<NCoordinates, Double> getMatrix(){
		return matrix;
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
	 * Returns the range
	 * @return
	 */
	public int getRange(){
		return range;
	}

}
