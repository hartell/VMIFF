/**
 * 
 */
package VisualizeBinary.Features;

import java.util.ArrayList;
import com.sun.org.apache.bcel.internal.Constants;
import VisualizeBinary.Matrix.Point;
import VisualizeBinary.NMatrix.NPoint;

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
		return (double)(points.size()) / Constants.MAX_BYTE;
	}

	@Override
	public double computeNFeature(ArrayList<NPoint> points) {
		return (double)(points.size()) / Constants.MAX_BYTE;
	}

}
