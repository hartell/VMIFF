package VisualizeBinary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This is main class of the binary visualization program.
 * @author Ellen Hartstack
 *
 */
public class BinaryReader {

	/**
	 * This method will start up the program.
	 * @param args
	 */
	public static void main(String[] args) throws IOException {		
		BinaryReader br = new BinaryReader();
		br.makeMenu();
	}
	
	JFrame frame;
	Graph2D my2DGraph;
	Graph3D my3DGraph;
	/**
	 * Makes a new main window
	 */
	public BinaryReader() {
		//Make a JFrame
		frame = new JFrame("Binary Reader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(1000,800);
	}
	/**
	 * Creates a the main window menu bar
	 * File Menu includes: Open, Exit
	 */
	public void makeMenu(){
		
		//Make a JMenuBar
		JMenuBar bar = new JMenuBar();
		
		//Make a menu File->
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		
		//Make a menu item: File -> Open 2D
		JMenuItem open2DOpt = new JMenuItem("Open 2D");
		open2DOpt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Open the JFileChooser to allow the user to select a file
				openFile(2);
			}			
		});
		fileMenu.add(open2DOpt);
		
		//Make a menu item: File -> Open 3D
		JMenuItem open3DOpt = new JMenuItem("Open 3D");
		open3DOpt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Open the JFileChooser to allow the user to select a file to display in 3D
				openFile(3);
			}
		});
		fileMenu.add(open3DOpt);
		
		//Make a menu item: File -> Close Graph
		//TODO: Get Close Option to work...
		JMenuItem closeOpt = new JMenuItem("Close Graph");
		closeOpt.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				//Close the graphs;
				
			}
		});
		//fileMenu.add(closeOpt);
		
		//Make a menu item: File -> Exit
		JMenuItem exitOpt = new JMenuItem("Exit");
		exitOpt.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Close the program
				frame.dispose();
			}
		});
		fileMenu.add(exitOpt);
		
		//Make a menu: Options
		JMenu optionMenu = new JMenu("Options");
		bar.add(optionMenu);
		
		//Add menu bar to frame;
		frame.setJMenuBar(bar);
		frame.validate();
	}
	
	/**
	 * Allows the user to select a file to open.
	 * Currently displays the graph in 2D Mode
	 * @param viewType - an integer value; 2 = draw 2D graph, 3= draw 3D Graph
	 */
	public void openFile(int viewType){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("C:/SVM/TestFiles/"));
		int choice = chooser.showOpenDialog(null);
		
		//Wait until the user has selected a file
		File file = null;
		if(choice == JFileChooser.APPROVE_OPTION){
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
			
			//Create a scatter plot graph of all the bytes
			//If the user wants 2D Graph...
			if(viewType == 2){
				my2DGraph = new Graph2D(frame);
				my2DGraph.drawGraph(frame, bytes, file.getName());
			}
			if(viewType == 3){
				my3DGraph = new Graph3D(frame);
				my3DGraph.drawGraph(frame, bytes, file.getName());
			}
		}
	}		
}

