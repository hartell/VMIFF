/**
 * 
 */
package VisualizeBinary.Features;

import java.util.ArrayList;
import VisualizeBinary.Point;

/**
 * @author hartell
 *
 */
public class PercentageFeature extends Feature {

	/**
	 * Calculates the percentage of points in that bucket
	 */
	public PercentageFeature() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double computeFeature(ArrayList<Point> points) {
		return (double)(points.size()) / 256.0;
	}

}
