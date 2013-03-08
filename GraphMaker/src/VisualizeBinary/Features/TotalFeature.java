/**
 * 
 */
package VisualizeBinary.Features;

import java.util.ArrayList;
import VisualizeBinary.Matrix.Point;
import VisualizeBinary.NMatrix.NPoint;

/**
 * @author hartell
 *
 */
public class TotalFeature extends Feature {

	public TotalFeature() {
		super();
	}

	/**
	 * Returns the number of points per bucket
	 */
	public double computeFeature(ArrayList<Point> points) {
		return points.size();
	}

	@Override
	public double computeNFeature(ArrayList<NPoint> points) {
		return points.size();
	}

}
