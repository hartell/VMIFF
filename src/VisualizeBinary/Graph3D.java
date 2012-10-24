/**
 * 
 */
package VisualizeBinary;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;

/**
 * This class renders a 3D graph of the given bytes
 * @author Ellen Hartstack
 *
 */
public class Graph3D {

	/**
	 * /**
	 * Constructor - sets the cursor to a wait icon
	 * @param frame - the container in which the wait icon should occur
	 */
	public Graph3D (JFrame frame){
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		System.out.println("Created 3D Graph obj");
	}
	
	/**
	 * Draws the 3D graph given the bytes
	 * Pairs up bytes into pairs {(1st byte, 2nd byte, 3rd byte), (4rd byte, 5th byte, 6th byte), ...}
	 * @param frame - where to draw the graph1
	 * @param bytes - the bytes to render into the graph
	 * @param title - the name of the graph (typically the file/fragment name)
	 */
	public void drawGraph(JFrame frame, byte[] bytes, String title){
		//Create the x,y,z points
		float x;
		float y;
		float z;
		
		//Create 3D coordinates to hold all the points
		//Coord3d[] points = new Coord3d[bytes.length/3];
		Coord3d[] points = new Coord3d[bytes.length/3];
		System.out.println("Points [] size = " + points.length);
		
		//Go through all given bytes and add them to the series of XYZ points
		//Set up as (1st byte, 2nd byte, 3rd byte), (4rd byte, 5th byte, 6th byte), ...
		int i = 0;
		for(int k = 0; k < bytes.length - 2; k = k+3){
			x = (float) bytes[k];
			y = (float) bytes[k+1];
			z = (float) bytes[k+2];
			points[i] = new Coord3d(x, y, z);
			System.out.println("Added point at Loc " + i + ": " + x + ", " + y + ", " + z);
			i++;
		}
		
		//Create a new scatter plot
		Scatter scatter = new Scatter(points);
		
		//Create a new chart
		Chart chart = new Chart();
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getScene().add(scatter);

		//Add chart to the JFrame
		JPanel panel = new JPanel();
		panel.add((Component) chart.getCanvas());
		panel.validate();
		panel.repaint();
		
		//Add the panel to the frame:
		frame.setContentPane(panel);
		
		//Repaint
		frame.validate();
		frame.repaint();
		System.out.println("Repainted Frame");
		
		//Remove the wait cursor (as the process has finished);
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		//ChartLauncher.openChart(chart);
	}

	//Possible Methods:
	//AddPoint:
	//RemovePoint:
	//Refresh;
	//Strip FileSignatureHeader;
}
