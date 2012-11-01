package VisualizeBinary;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * This class renders a 2D graph of the given bytes
 * @author Ellen Hartstack
 *
 */
public class Graph2D {
	/**
	 * Constructor - sets the cursor to a wait icon
	 * @param frame - - the container in which the wait icon should occur
	 */
	public Graph2D (){
		//frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	/**
	 * Draws the 2D graph given the bytes
	 * Pairs up bytes into pairs {(1st byte, 2nd byte), (3rd byte, 4th byte), (5th byte, 6th byte) ...}
	 * @param frame - where to draw the graph1
	 * @param bytes - the bytes to render into the graph
	 * @param title - the name of the graph (typically the file/fragment name)
	 * @return 
	 */
	public ChartPanel drawGraph(byte[] bytes, String title){
		//Create a new collect for XY points
		XYSeriesCollection data = new XYSeriesCollection();
		XYSeries series = new XYSeries("DataSet");
		
		//Go through all given bytes and add them to the series of XY points
		//Set up as (1st byte, 2nd byte), (3rd byte, 4th byte), ...
		for(int i = 0; i < bytes.length - 1; i= i+2){
			series.add(bytes[i], bytes[i+1]);
		}
		//Add the series to the collection
		data.addSeries(series);
		
		//Create a new Scatter Plot Chart
		JFreeChart scatter = ChartFactory.createScatterPlot(title, "X-Axis", "Y-Axis", data, PlotOrientation.VERTICAL, false, false, false);
		
		//Add the chart to the panel
		ChartPanel panel = new ChartPanel(scatter, true);
		
		//Add the panel to the frame
		return panel;
	}

	//Possible Methods:
	//AddPoint:
	//RemovePoint:
	//Refresh;
	//Strip FileSignatureHeader;
}
