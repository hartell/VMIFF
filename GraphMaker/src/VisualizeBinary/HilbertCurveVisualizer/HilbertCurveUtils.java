/**
 * 
 */
package VisualizeBinary.HilbertCurveVisualizer;

import java.util.ArrayList;
import VisualizeBinary.Matrix.Matrix;



/**
 * This class renders a 2D graph of the given bytes
 * @author Ellen Hartstack
 *
 * Maps a 2D XYplot to a Hilbert Curve
 */
public class HilbertCurveUtils {
	/**
	 * Draws the 2D graph given the bytes
	 * Pairs up bytes into pairs {(1st byte, 2nd byte), (3rd byte, 4th byte), (5th byte, 6th byte) ...}
	 * @param bytes - the bytes to render into the graph
	 * @param title - the name of the graph (typically the file/fragment name)
	 * @return 
	 */
	public static ArrayList<Integer> drawGraph(byte[] bytes, String title){			
		//Go through all given bytes and calculate their d value
		//Set up as (1st byte, 2nd byte), (3rd byte, 4th byte), ...
		ArrayList<Integer> dValues = new ArrayList<Integer>();
		
		int originalX = 0;
		int originalY = 0;
		System.out.println("Generating points");
		int pointCounter = 0;
		for(int i = 0; i < bytes.length - 1; i= i+2){
			//Get x and y values
			originalX = Matrix.unsignByte(bytes[i]);
			originalY = Matrix.unsignByte(bytes[i+1]);
			
			//Take x,y value and translate into d
			System.out.print(originalX + "," + originalY + " = ");
			int d = xy2d(originalX,originalY);
			System.out.println(d);
			dValues.add(d);

			//Add progress printout.
			pointCounter++;	
			if(pointCounter % 100 == 0){
				System.out.println("Calculated " + pointCounter + " points");
			}
		}
		return dValues;
	}

	/**
	 * This will take in a x & y coordinate and return its d value (or position along the curve)
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @return d - position along the curve
	 */
	public static int xy2d (int x, int y) {
		int n = 256; // size of the grid (0 - 255)
		int rx = 0;
		int ry = 0;
		int s = 0;
		int d = 0;
		Boolean rxB = false;
		Boolean ryB = false;
	    for (s=n/2; s>0; s= s/2) {
	        rxB = (x & s) > 0;
	        ryB = (y & s) > 0;
	        if(rxB == true){
	        	rx = 1;
	        }
	        else{
	        	rx=0;
	        }
	        if(ryB == true){
	        	ry = 1;
	        }
	        else{
	        	ry = 0;
	        }
	        
	        d += s * s * ((3 * rx) ^ ry);
	        //Rotate if necessary
	        int[] xyVals = rot(s, x, y, rx, ry);
	        x = xyVals[0];
	        y = xyVals[1];

	    }
	    return d;
	}
	
	/**
	 * This will take the d value and return the original x,y
	 * @param d - d value
	 * @return x , y int []
	 */
	public static int[] d2xy(int d) {
		int n = 256;
		int rx = d;
		int ry = d;
		int s = d;
		int t = d;
	    int x = 0;
	    int y = 0;
	    
	    int[] xyOutput = new int[2];
	    for (s=1; s<n; s= s*2) {
	        rx = 1 & (t/2);
	        ry = 1 & (t ^ rx);
	        //Rotate if necessary
	        int[] xyVals = rot(s, x, y, rx, ry);
	        x = xyVals[0];
	        y = xyVals[1];
	        
	        x += s * rx;
	        y += s * ry;
	        t /= 4;
	    }
	    xyOutput[0] = x;
	    xyOutput[1] = y;
	    return xyOutput;
	    
	}
	
	/**
	 * Rotate correctly
	 * @param n - 256 by default
	 * @param x - x value
	 * @param y - y value
	 * @param rx - rx value
	 * @param ry -ry value
	 * @return rotated x,y values int[]
	 */
	public static int[] rot(int n, int x, int y, int rx, int ry) {
		int[] xyArray = new int[2];
	    if (ry == 0) {
	        if (rx == 1) {
	            x = n-1 - x;
	            y = n-1 - y;
	        }
	 
	        //Swap x and y
	        int t = x;
	        x = y;
	        y = t;
	    }
	    
	    xyArray[0] = x;
	    xyArray[1] = y;
	    
	    return xyArray;
	}
	
	public static void main(String[] args){
		int x = 255;
		int y = 255;
		System.out.println(x + ", " + y);
		int d = xy2d(x,y);
		System.out.println(d);
		int[] results = d2xy(d);
		System.out.println(results[0] + ", " + results[1]);
		
//		for(int d = 0; d <16; d++){
//			int[] results = d2xy(d);
//			System.out.println(results[0] + ", " + results[1]);
//		}
		
	}
}
