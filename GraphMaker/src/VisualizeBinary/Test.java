package VisualizeBinary;

import java.util.Random;
import VisualizeBinary.Features.TotalFeature;
import VisualizeBinary.Matrix.Matrix;
import VisualizeBinary.NMatrix.NMatrix;

public class Test {

	public static void main(String[] args) {
		
		/*
		byte[] bytes = new byte[Constants.BLOCK_SIZE];
		new Random().nextBytes(bytes);
		NMatrix matrix = new NMatrix(32, 1, bytes, new TotalFeature());
		*/
		
		/*
		byte[] bytes = new byte[Constants.BLOCK_SIZE];
		new Random().nextBytes(bytes);
		for(int i=0; i<Constants.BLOCK_SIZE; i++){
			try {
				NMatrix matrix = new NMatrix(i, 1, bytes, new TotalFeature());
				System.out.println(i + " - Valid");
			} catch (Exception e){
				System.out.println(i + " - Invalid");
			}
		}
		*/
		

		byte[] bytes = new byte[Constants.BLOCK_SIZE];
		new Random().nextBytes(bytes);
		
		Matrix matrixA = new Matrix(1, bytes, new TotalFeature());
		NMatrix matrixB = new NMatrix(2, 1, bytes, new TotalFeature());
		
		System.out.println("2D Matrix: ");
		
		// print matrix a
		for(int i = 0; i < matrixA.getMatrix().length; i++){
			for(int j = 0; j < matrixA.getMatrix()[i].length; j++){
				 System.out.print(matrixA.getMatrix()[i][j] + " ");
			}
		}
		
		System.out.println("\n\nN Matrix (n=2): ");
		
		// print matrix b
		for(Double value : matrixB.getMatrix().values()){
			System.out.print(value + " ");
		}
	}

}

