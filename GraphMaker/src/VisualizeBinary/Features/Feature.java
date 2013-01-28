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
public abstract class Feature {
	
	public Feature(){
	}
	
	public abstract double computeFeature(ArrayList<Point> points);

}
