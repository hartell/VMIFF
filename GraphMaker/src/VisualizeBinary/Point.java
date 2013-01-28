/**
 * 
 */
package VisualizeBinary;

/**
 * @author hartell
 *
 */
public class Point {
	
	//Private variables:
	private int color; // Color = the age of the point as it is read in from the stream of bytes
	private int x;
	private int y;
	
	/**
	 * A point
	 * @param color - the age of the point, based on the order it is read in from the stream of bytes
	 * @param x - the x location
	 * @param y - the y location
	 */
	public Point(int color, int x, int y){
		this.color = color;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the color of the point
	 * @return
	 */
	public int getColor(){
		return color;
	}

	/**
	 * Returns the x value of the point
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Returns the y value of the point
	 * @return
	 */
	public int getY(){
		return y;
	}
}
