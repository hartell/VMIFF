package VisualizeBinary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

/**
 * This is main class of the binary visualization program.
 * @author Ellen Hartstack
 *
 */
public class BinaryReader {

	private JFrame frame;
	private JLabel fileName;
	private JLabel fileExt;
	private JLabel fileSize;
	
	private Graph2D graph;
	private JPanel fileInfo;
	private JPanel graphPanel;
	
	/**
	 * Makes a new main window
	 */
	public BinaryReader() {
		//Make a JFrame
		frame = new JFrame("Binary Reader");
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(1000,800);
		
		//Make the menu
		makeMenu();
		
		//Display the file info
		addFileInfo();
		
		//Make Chart Panel (2D or 3D)
		addGraphPanel();
		
		//Add Navigation Panel
		addNavigationPanel();
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
				//Some action
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
			updateFileInfo(file);
			
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
				graph = new Graph2D();
				graphPanel.removeAll();
				graphPanel.add(graph.drawGraph(bytes, file.getName()));
			}
			if(viewType == 3){
				//my3DGraph = new Graph3D(frame);
				//my3DGraph.drawGraph(frame, bytes, file.getName());
				System.out.println("To Be Completed!");
			}
		}
	}
	
	/**
	 * Left hand panel to display information about the file.
	 */
	public void addFileInfo(){
		//Make a new JPanel & Layout
		fileInfo = new JPanel();
		fileInfo.setLayout(new GridBagLayout());
		fileInfo.setBackground(Color.YELLOW);

		GridBagConstraints c = new GridBagConstraints();
		
		//Make Labels
		JLabel title = new JLabel("File Information: ");
		fileName = new JLabel("Name: No File Loaded");
		fileExt = new JLabel ("Type: N/A");
		fileSize = new JLabel("Size: N/A");
		
		//Add labels to panel;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		fileInfo.add(title, c);
		c.gridy = 1;
		fileInfo.add(fileName, c);
		c.gridy = 2;
		c.weighty = 0;
		fileInfo.add(fileExt, c);
		c.gridy = 3;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		fileInfo.add(fileSize, c);
		
		//Add panel to frame
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		frame.getContentPane().add(fileInfo, c);		
	}
	
	public void updateFileInfo(File file){
		fileName.setText("Name: " + file.getName());
		//TODO: Fix this path!
		fileExt.setText("Type: " + ".txt");
		//TODO: Fix this size!
		fileSize.setText("Size: " + "1000 bytes");
	}
	
	public void addGraphPanel(){
		graphPanel = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.BOTH;

		frame.getContentPane().add(graphPanel, c);
	}
	
	
	public void addNavigationPanel(){
		JPanel navPanel = new JPanel();
		navPanel.setBackground(Color.RED);
		
		JButton button = new JButton ("C Button");
		navPanel.add(button);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		frame.getContentPane().add(navPanel, c);
	}
	
	
	/**
	 * This method will start up the program.
	 * @param args
	 */
	public static void main(String[] args) throws IOException {		
		new BinaryReader();		
	}
}

