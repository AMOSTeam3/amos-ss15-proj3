package de.fau.osr.gui.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Window.Type;

/**
 * This class is a generic progress bar that can be utilized for any progress bar functionality
 * @author Gayathery Sathya
 */
public class SpiceTraceabilityProgressBar extends JFrame {

	private JPanel contentPane;
	private JProgressBar progressBar;
	private JLabel lblPercentage;
	private JLabel lblProgress;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpiceTraceabilityProgressBar frame = new SpiceTraceabilityProgressBar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SpiceTraceabilityProgressBar() {
		setType(Type.POPUP);
		setResizable(false);
		setTitle("Spice Traceability : Progress");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 217);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		progressBar = new JProgressBar();
		
		lblProgress = new JLabel("Progress...");
		lblProgress.setForeground(Color.BLUE);
		
		lblPercentage = new JLabel("0%");
		lblPercentage.setForeground(new Color(205, 92, 92));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblProgress)
							.addGap(34)
							.addComponent(lblPercentage)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProgress)
						.addComponent(lblPercentage))
					.addPreferredGap(ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * @param percentage value of the progress
	 */
	public void setProgressBarValue(int percentage){
		progressBar.setValue(percentage);
		lblPercentage.setText(""+percentage+"%");
	}
	
	/**
	 * @param content string data to show while progress bar is progressing
	 */
	public void setProgressBarContent(String content){
		lblProgress.setText(content + "...");
	}

}
