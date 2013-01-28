/**
 * 
 */
package VisualizeBinary.Features;

import java.util.ArrayList;
import VisualizeBinary.Matrix.Point;

/**
 * @author hartell
 *
 */
public class TotalAgeFeature extends Feature {

	/**
	 * This feature will calculate a total age for each grain/bucket of points
	 */
	public TotalAgeFeature() {
		super();
	}

	
	
	@Override
	/**
	 * Traverse through all the given points and return a total age of all points.
	 * @return - the total age
	 */
	public double computeFeature(ArrayList<Point> points) {
		double result = 0.0;
		//Go through all the points and return the total age of the points
		for(Point p : points){
			result = result + p.getAge();
		}
		return result;
	}

}
