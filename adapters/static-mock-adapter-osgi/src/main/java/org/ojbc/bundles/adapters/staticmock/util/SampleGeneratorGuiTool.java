/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractSampleGenerator;



public class SampleGeneratorGuiTool {
	
	
	private static final String[] A_SAMPLE_TYPES = {"Incident","CriminalHistory","Warrant","Firearm",
			"JuvenileHistory","Custody", "CustodyMatthews", "CourtCase","VehicleCrash","VehicleCrashMatthews","All"};
	
	private Logger logger = Logger.getLogger(SampleGeneratorGuiTool.class.getName()); 
	
	private String outputDirPath;
	
	private JPanel mainPanel = new JPanel();
	
	private JComboBox<String> sampleBox = new JComboBox<String>(A_SAMPLE_TYPES);
	
	private JLabel sampleTypeLabel = new JLabel("Type:");
	
	private JLabel sampleCountLabel = new JLabel("Count:");
	
	private JFormattedTextField sampleCountField = new JFormattedTextField(NumberFormat.getInstance());
	
	private JButton okButton = new JButton("Ok");
	
	private JTextField pathTextField= new JTextField();
	
	private JButton pathButton = new JButton("Dir");
	
	
	public static void main(String[] args) {
		
		SampleGeneratorGuiTool guiTool = new SampleGeneratorGuiTool();
		
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				guiTool.launch();				
			}
		});
	}
	
	
	private void launch(){
		
		JFrame mainFrame = new JFrame();		
		
		mainFrame.addWindowListener(getGeneratorWindowListener());
		
		mainFrame.setTitle("Sample Generator");
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setResizable(false);
		
		mainFrame.setContentPane(mainPanel);
		
		GroupLayout layout = new GroupLayout(mainPanel);		
		mainPanel.setLayout(layout);		
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);		
						
		sampleBox.setSelectedItem("Custody");
		sampleBox.setMaximumSize(new Dimension(250,40));
				
		sampleCountField.setText("10");//default
		sampleCountField.setMaximumSize(new Dimension(40, 20));
						
		okButton.addActionListener(getOkButtonListener());
						
		sampleCountField.setPreferredSize(new Dimension(40, 20));
				
		pathTextField.setMaximumSize(new Dimension(250, 40));
		
		pathTextField.setEditable(false);
		
		String sUserDir = System.getProperty("user.dir");	
		outputDirPath = sUserDir;
		pathTextField.setText(outputDirPath);//default		
					
		pathButton.addActionListener(getPathButtonListener(mainFrame));

		initLayout(layout);
										
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	
	
	private ActionListener getOkButtonListener(){
		
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String type = sampleBox.getSelectedItem().toString();
				
				String count = sampleCountField.getText();
								
				String[] args = {type, count, outputDirPath};
				
				try {
					AbstractSampleGenerator.runGenerator(args);
					
					JOptionPane.showMessageDialog(mainPanel, "Generated Samples!", "Status", JOptionPane.PLAIN_MESSAGE);	
															
				} catch (Exception e1) {
										
					e1.printStackTrace();
					
					JOptionPane.showMessageDialog(mainPanel, "Failed!", "Failure", JOptionPane.ERROR_MESSAGE);
				}	
				
				System.exit(0);							
			}
		};
	}
	
	private ActionListener getPathButtonListener(JFrame mainFrame){
		
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int returnVal = fileChooser.showOpenDialog(mainFrame);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fileChooser.getSelectedFile();

					if(selectedFile != null){
					
						outputDirPath = selectedFile.getPath();
						
						logger.info("Picked: " + outputDirPath);
						
						pathTextField.setText(outputDirPath);						
					}
				}else{
					logger.warning("Invalid Selection");
				}
			}
		};
	}
		
	private void initLayout(GroupLayout layout){
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(sampleTypeLabel)
						.addComponent(sampleCountLabel)
						.addComponent(pathButton)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(sampleBox)
						.addComponent(sampleCountField)
						.addComponent(pathTextField)
				)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(okButton)		
						.addGap(10, 10, GroupLayout.PREFERRED_SIZE)
						.addGap(10, 10, GroupLayout.PREFERRED_SIZE)
				)			
		);				
				
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(sampleTypeLabel)
						.addComponent(sampleBox)	
						.addComponent(okButton))
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(sampleCountLabel)
						.addComponent(sampleCountField)
						.addGap(10, 10, GroupLayout.PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(pathButton)
						.addComponent(pathTextField)
						.addGap(10, 10, GroupLayout.PREFERRED_SIZE)
				)
		);		
		
	}
	
	private WindowListener getGeneratorWindowListener(){
		
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		};
	}

}
