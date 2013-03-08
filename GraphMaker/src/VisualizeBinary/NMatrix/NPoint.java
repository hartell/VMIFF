/**
 * 
 */
package VisualizeBinary.NMatrix;

/**
 * @author hartell
 *
 */
public class NPoint {
	
	// Private variables:
	private int age;
	private NCoordinates coordinates;

	/**
	 * A point
	 * @param age - the age of the point, based on the order it is read in from the stream of bytes
	 * @param coordinates - 
	 */
	public NPoint(int age, NCoordinates coordinates){
		this.age = age;
		this.coordinates = coordinates;
	}
	
	/**
	 * Returns the age of the point, based on the order it is read in from the stream of bytes
	 * @return
	 */
	public int getAge(){
		return age;
	}

	/**
	 * Returns the n coordinates of this point
	 * @return
	 */
	public NCoordinates getCoordinates(){
		return coordinates;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NPoint other = (NPoint) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		return true;
	}

}
