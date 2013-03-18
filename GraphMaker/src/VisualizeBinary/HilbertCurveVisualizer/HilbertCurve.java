package VisualizeBinary.HilbertCurveVisualizer;

import java.awt.*;
import java.applet.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class HilbertCurve extends Applet {
	private SimpleGraphics sg=null;
	private int dist0=512, dist=dist0;
	int counter = 0;
	ArrayList<Integer> dVals = new ArrayList<Integer>();

	public void init() {
		dist0 = 512;
		resize ( dist0, dist0 );
		
//		//For individual tests:
//		byte[] bytes = new byte[]{(byte) 255,(byte) 255};
//		dVals = HilbertCurveUtils.drawGraph(bytes, "test");
//		sg = new SimpleGraphics(getGraphics(), dVals);

		//Create a new JFileChooser and set it's starting directory
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("H://SVM"));
		int choice = chooser.showOpenDialog(null);
		
		//Wait until the user has selected a file
		File file = null;
		if(choice == JFileChooser.APPROVE_OPTION){
			//Get the selected File 
			file = chooser.getSelectedFile();
			
			//Get the path of the file
			Path path = Paths.get(file.getAbsolutePath());
			
			//Read in all the bytes of that file
			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes(path);
			} catch (IOException e) {
				e.printStackTrace();
			}	
			dVals = HilbertCurveUtils.drawGraph(bytes, "test");
			sg = new SimpleGraphics(getGraphics(), dVals);
		}
	}

	public void paint(Graphics g) {
		int level=8;
		dist=dist0;
		for (int i=level;i>0;i--){
			dist /= 2;
		}
		sg.goToXY ( dist/2, dist/2 );
		HilbertA(level); // start recursion
	}
	private void HilbertA (int level) {
		if (level > 0) {
			HilbertB(level-1);    sg.lineRel(0,dist, counter);
			counter++;
			HilbertA(level-1);    sg.lineRel(dist,0, counter);
			counter++;
			HilbertA(level-1);    sg.lineRel(0,-dist, counter);
			counter++;
			HilbertC(level-1);
		}
	}

	private void HilbertB (int level) {
		if (level > 0) {
			HilbertA(level-1);    sg.lineRel(dist,0, counter);
			counter++;
			HilbertB(level-1);    sg.lineRel(0,dist, counter);
			counter++;
			HilbertB(level-1);    sg.lineRel(-dist,0, counter);
			counter++;
			HilbertD(level-1);
		}
	}

	private void HilbertC (int level) {
		if (level > 0) {
			HilbertD(level-1);    sg.lineRel(-dist,0, counter);
			counter++;
			HilbertC(level-1);    sg.lineRel(0,-dist, counter);
			counter++;
			HilbertC(level-1);    sg.lineRel(dist,0, counter);
			counter++;
			HilbertA(level-1);
		}
	}

	private void HilbertD (int level) {
		if (level > 0) {
			HilbertC(level-1);    sg.lineRel(0,-dist, counter);
			counter++;
			HilbertD(level-1);    sg.lineRel(-dist,0, counter);
			counter++;
			HilbertD(level-1);    sg.lineRel(0,dist, counter);
			counter++;
			HilbertB(level-1);
		}
	}
}

class SimpleGraphics {
	private Graphics g = null;
	private int x = 0, y = 0;
	ArrayList<Integer> dVals;

	public SimpleGraphics(Graphics g, ArrayList<Integer> dVals) { 
		this.g = g;
		this.dVals = dVals;
	}
	public void goToXY(int x, int y) { 
		this.x = x;   this.y = y; 
	}

	public void lineRel(int deltaX, int deltaY, int counter) {
		if(dVals.contains(counter)){
			//g.drawLine ( x, y, x+deltaX, y+deltaY );
			System.out.println("Found Point at: " + counter);
			g.fillRect(x, y, 2, 2);
		}
		//g.drawLine ( x, y, x, y);
		x += deltaX;    y += deltaY;
	}
}