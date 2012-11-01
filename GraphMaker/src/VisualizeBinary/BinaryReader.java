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

/**
 * This is main class of the binary visualization program.
 * @author Ellen Hartstack
 *
 */
public class BinaryReader {

	private JFrame frame;
	private JLabel fileName;
	private JLabel fileSize;
	
	private Graph2D graph2D;
	private Graph3D graph3D;
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
		
		//Now that all the components are added, revalidate them.
		frame.validate();
	}
	/**
	 * Creates a the main window menu bar
	 * File Menu includes: File, Options
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
		JMenuItem closeOpt = new JMenuItem("Close Graph");
		closeOpt.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				clearLoadedFile();
			}
		});
		fileMenu.add(closeOpt);
		
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
	 * @param viewType - an integer value; 2 = draw 2D graph, 3 = draw 3D Graph
	 */
	public void openFile(int viewType){
		//Create a new JFileChooser and set it's starting directory
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("C:/SVM/TestFiles/"));
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
			
			//Update the file's information panel with the new file.
			updateFileInfo(file, bytes.length);
			
			//Create a scatter plot graph of all the bytes
			//If the user wants 2D Graph...
			if(viewType == 2){
				//Create a new 2d Graph and remove any old ones.
				graph2D = new Graph2D();
				graphPanel.removeAll();
				
				//Layout the graph
				graphPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1.0;
				c.weighty = 1.0;
				
				//Add the newly created graph to the graph panel.
				graphPanel.add(graph2D.drawGraph(bytes, file.getName()), c);
			}
			//If the user wants to use the 3D Graph...
			if(viewType == 3){
				//Create a new 3d Graph and remove any old ones.
				graph3D = new Graph3D();
				graphPanel.removeAll();
				
				//Layout the 3d Graph
				graphPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1.0;
				c.weighty = 1.0;
				
				//Add the newly created graph to the graph panel.
				graphPanel.add(graph3D.drawGraph(bytes, file.getName()), c);
			}
		}
	}
	
	/**
	 * A Panel to display information about the loaded file or fragment
	 * Information includes: File Name, Size of File
	 */
	public void addFileInfo(){
		//Make a new JPanel & Layout
		fileInfo = new JPanel();
		fileInfo.setLayout(new GridBagLayout());
		fileInfo.setBackground(Color.WHITE);
		//New GridBagConstraints
		GridBagConstraints c = new GridBagConstraints();
		
		//Make Labels
		JLabel title = new JLabel("File Information: ");
		fileName = new JLabel("File: No File Loaded");
		fileSize = new JLabel("Size: N/A");
		
		//Add labels to panel;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		fileInfo.add(title, c);
		c.gridy = 1;
		fileInfo.add(fileName, c);
		c.gridy = 2;
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
	/**
	 * Update the file info panel with information about the loaded file
	 * @param file - the file to be loaded
	 * @param length - the length (in bytes) of this file
	 */
	public void updateFileInfo(File file, int length){
		fileName.setText("File: " + file.getName());
		fileSize.setText("Size: " + length + " bytes");
	}
	
	/**
	 * Clears all information about a file
	 * Used when the open file is closed.
	 */
	public void clearLoadedFile(){
		//Remove all components in the graph Panel
		graphPanel.removeAll();
		
		//Change the labels back to defaults
		fileName.setText("File: No File Loaded");
		fileSize.setText("Size: N/A");
		
		//Update the frame
		frame.validate();
		frame.repaint();
	}
	
	/**
	 * Adds the graph Panel where the visual data will be drawn.
	 */
	public void addGraphPanel(){
		//Panel for the graph
		graphPanel = new JPanel();
		
		//Layout the graph
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.BOTH;

		//Add graphPanel to frame
		frame.getContentPane().add(graphPanel, c);
	}
	
	/**
	 * Adds the navigationPanel - yet to be implemented fully.
	 * More of a placeholder
	 */
	public void addNavigationPanel(){
		//New navPanel
		JPanel navPanel = new JPanel();
		navPanel.setBackground(Color.RED);
		
		JButton button = new JButton ("To Be Completed");
		navPanel.add(button);
		
		//Sets the constraints for the panel
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		//Adds the panel to the frame
		frame.getContentPane().add(navPanel, c);
	}
	
	/**
	 * Main - This method will start up the program.
	 * @param args
	 */
	public static void main(String[] args) throws IOException {		
		new BinaryReader();		
	}
}

