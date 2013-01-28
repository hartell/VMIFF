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
public abstract class Feature {
	
	public Feature(){
	}
	
	public abstract double computeFeature(ArrayList<Point> points);

}
