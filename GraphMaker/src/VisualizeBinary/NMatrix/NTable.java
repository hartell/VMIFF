package VisualizeBinary.NMatrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class NTable {

	/**
	 * @param n - the number of dimensions
	 * @param range - the max value of each coordinate
	 */
	private static HashMap<NCoordinates, ArrayList<NCoordinates>> allNCoordinates(int n, int range) {
		HashMap<NCoordinates, ArrayList<NCoordinates>> grainBucket = new HashMap<NCoordinates, ArrayList<NCoordinates>>();
        for (int i=0; i<(int) Math.pow(range,n); i++) {
        	int[] temp = new int[n];
        	int index = 0;
            for (int j=n-1; j>=0; j--) {
            	temp[index++] = (i/(int) Math.pow(range, j)) % range;
            }
            NCoordinates NCoordinates = new NCoordinates(temp);
            grainBucket.put(NCoordinates, new ArrayList<NCoordinates>());
        }
        return grainBucket;
    }
	
    public static void main(String[] args) {
    	int numDimensions = 2;
    	int range = 4;
    	Collection<NCoordinates> grainBucket = allNCoordinates(numDimensions, range).keySet();
    	
    	LinkedList<NCoordinates> sortedGrainBucket = new LinkedList<NCoordinates>();
    	for(NCoordinates NCoordinates : grainBucket){
    		sortedGrainBucket.add(NCoordinates);
    	}
    	Collections.sort(sortedGrainBucket);
    	
    	for(NCoordinates grain : sortedGrainBucket){
    		System.out.println(grain);
    	}
    }
}