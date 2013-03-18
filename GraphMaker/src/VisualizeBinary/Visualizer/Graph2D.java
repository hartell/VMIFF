package VisualizeBinary.Visualizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import VisualizeBinary.Matrix.Matrix;

/**
 * This class renders a 2D graph of the given bytes
 * @author Ellen Hartstack
 *
 * Maps - N/S/E/W tiles mapping (visually easy to determine)
 * Keep in mind the story, see if you can show how this method will be better than conventinal methods.
 * Using the visualization to in order to help us order the blocks by involving the human actor in the file carving process.
 * Computers - high false positive, humans can lower this.
 */

public class Graph2D {
	/**
	 * Constructor - sets the cursor to a wait icon
	 * @param frame - - the container in which the wait icon should occur
	 */
	public Graph2D(){
		//frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
		
	/**
	 * Draws the 2D graph given the bytes
	 * Pairs up bytes into pairs {(1st byte, 2nd byte), (3rd byte, 4th byte), (5th byte, 6th byte) ...}
	 * @param bytes - the bytes to render into the graph
	 * @param title - the name of the graph (typically the file/fragment name)
	 * @return a panel of the graph
	 */
	public ChartPanel drawGraph(byte[] bytes, String title){
		//Create a new collect for XY points
		XYSeriesCollection data = new XYSeriesCollection();
		XYSeries series = new XYSeries("DataSet");
		
		//Go through all given bytes and add them to the series of XY points
		//Set up as (1st byte, 2nd byte), (3rd byte, 4th byte), ...
		int x = 0;
		int y = 0;
		System.out.println("Generating points");
		int pointCounter = 0;
		for(int i = 0; i < bytes.length - 1; i= i+2){
			//Get x and y values
			x = Matrix.unsignByte(bytes[i]);
			y = Matrix.unsignByte(bytes[i+1]);
			//Add x, y to series.
			series.add(x, y);
			//System.out.println(x + "," + y);
			
			//Add progress printout.
			pointCounter++;	
			if(pointCounter % 100 == 0){
				System.out.println("Calculated " + pointCounter + " points");
			}
		}

		//Add the series to the collection
		data.addSeries(series);
		
		//Create a new Scatter Plot Chart
		JFreeChart scatter = ChartFactory.createScatterPlot(title, "X-Axis", "Y-Axis", data, PlotOrientation.VERTICAL, false, false, false);
		
		//Attempt to change colors -- Does whole chart...
//		XYPlot plot = scatter.getXYPlot();
//		XYLineAndShapeRenderer xyRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
//		xyRenderer.setSeriesPaint(0, Color.BLUE);
		
		//Change the colors to be a gradient.
//		XYPlot plot = scatter.getXYPlot();
//		XYLineAndShapeRenderer xyRenderer = (XYLineAndShapeRenderer) new XYRendererGradient(bytes.length);
//		//Remove lines (so only have dots)
//		xyRenderer.setBaseLinesVisible(false);
//		plot.setRenderer(xyRenderer);
		
		//Add the chart to the panel
		ChartPanel panel = new ChartPanel(scatter, true);
		
		//Add the panel to the frame
		return panel;
	}
}
