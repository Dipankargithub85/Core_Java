package graph;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ReadXmlFile;

import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

public class XpathWindow {

	private JFrame Xpath;
	private JTextField textField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnXpathAsSfs;
	private JRadioButton rdbtnPicRepresentation;
	ReadXmlFile rd;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					XpathWindow window = new XpathWindow();
					window.Xpath.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public XpathWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Xpath = new JFrame();
		Xpath.setTitle("XpathWindow");
		Xpath.setBounds(100, 100, 484, 304);
		Xpath.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField = new JTextField();
		
		JLabel lblSe = new JLabel("Select File");
		Xpath.getContentPane().setLayout(new MigLayout("", "[48px][][][][15px][4px][47px][139px][14px][53px][10px][][][67px]", "[23px][23px][][][23px][][][][]"));
		Xpath.getContentPane().add(lblSe, "cell 0 0,alignx left,aligny center");
		Xpath.getContentPane().add(textField, "cell 1 0 9 1,growx,aligny bottom");
		
		
		JButton btnNewButton = new JButton("Browse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose xml file");
				jfc.setFileFilter(new FileTypeFilter(".xml", "Xml File"));
				int returnValue = jfc.showOpenDialog(null);
				// int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					textField.setText(selectedFile.getAbsolutePath());
					textField.setColumns(15);
					//System.out.println(selectedFile.getAbsolutePath());
				}
			}
		});
		Xpath.getContentPane().add(btnNewButton, "cell 12 0,alignx left,aligny top");
		
		rdbtnNewRadioButton = new JRadioButton("Xpath");
		buttonGroup.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setSelected(true);
		Xpath.getContentPane().add(rdbtnNewRadioButton, "cell 1 2,alignx right,aligny top");
		
		rdbtnXpathAsSfs = new JRadioButton("Xpath as SFS");
		buttonGroup.add(rdbtnXpathAsSfs);
		Xpath.getContentPane().add(rdbtnXpathAsSfs, "cell 7 2,alignx left,aligny top");
		
		rdbtnPicRepresentation = new JRadioButton("Pic Representation");
		buttonGroup.add(rdbtnPicRepresentation);
		Xpath.getContentPane().add(rdbtnPicRepresentation, "cell 12 2,alignx left,aligny top");
		
		JButton btnNewButton_1 = new JButton("Click Me");
		btnNewButton_1.setBackground(new Color(222, 184, 135));
		buttonGroup.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textField.getText();
				if (text.equals("")){
					JOptionPane.showMessageDialog(null, "Please Choose the file");
				}
				else if (!text.endsWith("xml"))
				{
					JOptionPane.showMessageDialog(null, "Please Choose  xml file");
				}
				else if (rdbtnXpathAsSfs.isSelected())
				{
					JOptionPane.showMessageDialog(null, "Opps!!! Working in progress. Not Yet Ready");
				}
				else if (rdbtnPicRepresentation.isSelected())
				{
					JOptionPane.showMessageDialog(null, "Not yet Ready");
				}
				else{
					rd = new ReadXmlFile();
					String path;
					try {
						path = rd.readfile(text);
						if (path.equals("error"))
						{
							JOptionPane.showMessageDialog(null, "Error in excel");
						}
						else {
							JOptionPane.showMessageDialog(null, "The Xpath File " + path  + " is created");
						}
					} catch (ParserConfigurationException | SAXException
							| IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null,e1.getMessage());
					}
							
				}
				
			}
		});
		Xpath.getContentPane().add(btnNewButton_1, "cell 7 6,alignx right,aligny top");
	}
}
