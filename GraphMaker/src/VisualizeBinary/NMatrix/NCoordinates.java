/**
 * 
 */
package VisualizeBinary.NMatrix;

import java.util.Arrays;

/**
 * @author hartell
 *
 */
public class NCoordinates implements Comparable<NCoordinates> {
	// Private variables:
		private int[] coordinates;

		/**
		 * A point
		 * @param age - the age of the point, based on the order it is read in from the stream of bytes
		 * @param coordinates - 
		 */
		public NCoordinates(int[] coordinates){
			this.coordinates = coordinates;
		}
		
		@Override
		public String toString() {
			return Arrays.toString(coordinates);
		}

		@Override
		public int compareTo(NCoordinates other) {
			
			if(this.coordinates.length != other.coordinates.length){
				throw new IllegalArgumentException("Not comparable.");
			}
			
			for(int i=0; i<this.coordinates.length; i++){
				// not equal
				if(this.coordinates[i] != other.coordinates[i]){
					// return greater than or less than
					return this.coordinates[i] - other.coordinates[i];
				}
			}
			
			// equal
			return 0;
		}
		
		/**
		 * Returns the n coordinates of this point
		 * @return
		 */
		public int[] getCoordinates(){
			return coordinates;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(coordinates);
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
			NCoordinates other = (NCoordinates) obj;
			if (!Arrays.equals(coordinates, other.coordinates))
				return false;
			return true;
		}
	}
