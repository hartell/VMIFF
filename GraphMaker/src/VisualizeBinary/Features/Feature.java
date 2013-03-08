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
public abstract class Feature {
	
	public Feature(){
	}
	
	public abstract double computeFeature(ArrayList<Point> points);
	public abstract double computeNFeature(ArrayList<NPoint> points);

}
