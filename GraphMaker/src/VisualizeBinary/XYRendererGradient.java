/**
 * 
 */
package VisualizeBinary;

import java.util.List;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;

/**
 * @author hartell
 *
 */
@SuppressWarnings("serial")
public class XYRendererGradient extends XYLineAndShapeRenderer{
	
	double counter = 0;
	double interval;
	List<XYDataItem> list;
	
	public XYRendererGradient(int length, List<XYDataItem> list){
		super();
		this.list = list; 
		//Get the total number of points (pairs)
		double numOfPairs = length/2.0;
		System.out.println("Number of Pairs = " + numOfPairs);
		//Get the number of points per color
		interval = 255.0/numOfPairs;
		
		System.out.println("Interval = " + interval);
	}
	
//	public Paint getItemPaint(int x, int y){
//		
//		int c = (int) Math.floor(getIndex(x,y)*interval);
//		//counter += interval;
//		//System.out.println("Counter =" + counter);
//		
//		if(c > 255){
//			c = 255;
//		}
//		System.out.println("C= " + c);
//		
//		return new Color(c, c, c);
//	}
	
//	private int getIndex(int x, int y){
//		
//		for(XYDataItem item : list){
//			if(item.getX().intValue() == x && item.getY().intValue() == y){
//				return list.indexOf(item);
//			}
//		}
//		return -1;
//	}
}
