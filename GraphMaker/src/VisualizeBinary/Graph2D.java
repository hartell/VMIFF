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
//		int quad1 = 0;
//		int quad2 = 0;
//		int quad3 = 0;
//		int quad4 = 0;
//		int origin = 0;
		int x = 0;
		int y = 0;
//		int avg = 0;
			
		for(int i = 0; i < bytes.length - 1; i= i+2){
			//Get x and y values
			x = bytes[i];
			y = bytes[i+1];
			//Add x, y to series.
			series.add(x, y);
			//avg = avg + x + y;
			
//			//determine their quadrant
//			if(x > 0 && y > 0){
//				//System.out.println(x + ", " +  y + "= quad 1");
//				quad1++;
//			}
//			else if(x < 0 && y > 0)	{
//				//System.out.println(x + ", " +  y + "= quad 2");
//				quad2++;
//			}
//			else if(x < 0 && y < 0){
//				//System.out.println(x + ", " +  y + "= quad 3");
//				quad3++;
//			}
//			else if(x > 0 && y < 0){
//				//System.out.println(x + ", " +  y + "= quad 4");
//				quad4++;
//			}
//			else if(x == 0 && y == 0){
//				origin++;
//			}
		}
		
//		System.out.println("Quadrant Info: ");
//		System.out.println("Origin = " + origin);
//		System.out.println("Quadrant 1 = " + quad1);
//		System.out.println("Quadrant 2 = " + quad2);
//		System.out.println("Quadrant 3 = " + quad3);
//		System.out.println("Quadrant 4 = " + quad4);
//		System.out.println("Average = " + (avg/bytes.length));
		
		
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
