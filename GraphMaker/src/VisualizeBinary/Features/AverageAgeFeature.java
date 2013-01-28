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
public class AverageAgeFeature extends Feature {

	/**
	 * This feature will calculate a average age for each grain/bucket of points
	 */
	public AverageAgeFeature() {
		super();
	}

	@Override
	/**
	 * Traverse through all the given points and return a the average age of all points.
	 * @return - the total age
	 */
	public double computeFeature(ArrayList<Point> points) {
		double totalAge = 0.0;
		//Go through all the points and return the total age of the points
		for(Point p : points){
			totalAge = totalAge + p.getAge();
		}
		
		double result = totalAge / ((double) points.size());
		
		return result;
	}
}
