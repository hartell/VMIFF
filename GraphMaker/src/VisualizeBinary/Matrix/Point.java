/**
 * 
 */
package VisualizeBinary.Matrix;

/**
 * @author hartell
 *
 */
public class Point {
	
	//Private variables:
	private int age;
	private int x;
	private int y;
	
	/**
	 * A point
	 * @param age - the age of the point, based on the order it is read in from the stream of bytes
	 * @param x - the x location
	 * @param y - the y location
	 */
	public Point(int age, int x, int y){
		this.age = age;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the age of the point, based on the order it is read in from the stream of bytes
	 * @return
	 */
	public int getAge(){
		return age;
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
