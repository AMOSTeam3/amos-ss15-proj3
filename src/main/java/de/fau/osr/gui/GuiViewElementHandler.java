package de.fau.osr.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class GuiViewElementHandler extends JFrame{
	private static final long serialVersionUID = 1L;
	private JScrollPane RequirementID_scrollPane;
	private JScrollPane Commit_scrollPane;
	private JScrollPane Files_scrollPane;
	private JScrollPane Code_scrollPane;
	private JScrollPane ImpactPercentage_scrollPane;
	
	private JLabel RequirementID_label;
	private JLabel Code_label;
	private JLabel ImpactPercentage_label;
	private JLabel Commit_label;
	private JLabel Files_label;
	
	private JButton Files_button;
	private JButton Commit_button;
	private JButton RequirementID_button;
	
	private GuiController guiController;

	public GuiViewElementHandler() {
		JPanel contentPane = createMainFrame();
		createLayout(contentPane);
	}

	public JScrollPane getRequirementID_scrollPane() {
		return RequirementID_scrollPane;
	}

	public JScrollPane getCommit_scrollPane() {
		return Commit_scrollPane;
	}

	public JScrollPane getFiles_scrollPane() {
		return Files_scrollPane;
	}

	public JScrollPane getCode_scrollPane() {
		return Code_scrollPane;
	}

	public JScrollPane getImpactPercentage_scrollPane() {
		return ImpactPercentage_scrollPane;
	}
	
	/**
	 * @param contentPane
	 */
	public void createLayout(JPanel contentPane) {
		createScrollPanes();
		
		createLabels();
	
		createButtons();
		
		positionElements(contentPane);
	}

	/**
	 * @param contentPane
	 */
	private void positionElements(JPanel contentPane) {
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(getRequirementID_scrollPane(), GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(RequirementID_button, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
							.addComponent(RequirementID_label)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(getCommit_scrollPane(), GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(getFiles_scrollPane(), GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(getCode_scrollPane(), GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(getImpactPercentage_scrollPane(), GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(93)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addGap(14)
									.addComponent(Commit_label, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
									.addGap(272)
									.addComponent(Files_label)
									.addPreferredGap(ComponentPlacement.RELATED, 523, Short.MAX_VALUE)
									.addComponent(Code_label)
									.addGap(404)
									.addComponent(ImpactPercentage_label, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(Commit_button, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
									.addGap(239)
									.addComponent(Files_button)))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(ImpactPercentage_label)
						.addComponent(RequirementID_label)
						.addComponent(Code_label)
						.addComponent(Commit_label)
						.addComponent(Files_label))
					.addGap(1)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(RequirementID_button)
						.addComponent(Commit_button)
						.addComponent(Files_button))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(getFiles_scrollPane(), GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(getRequirementID_scrollPane(), GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(getCommit_scrollPane(), GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(getImpactPercentage_scrollPane(), GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(getCode_scrollPane(), GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE))
					.addContainerGap())
		);

		contentPane.setLayout(gl_contentPane);
	}

	/**
	 * 
	 */
	private void createButtons() {
		Files_button = new JButton("Show all");
		Commit_button = new JButton("Show all");
		RequirementID_button = new JButton("Show all");
	}

	/**
	 * 
	 */
	private void createLabels() {
		RequirementID_label = new JLabel("RequirementID");
		Code_label = new JLabel("Code");
		ImpactPercentage_label = new JLabel("Impact Percentage");
		Commit_label = new JLabel("Commit");
		Files_label = new JLabel("Files");
	}
	
	private void createScrollPanes() {
		Files_scrollPane = new JScrollPane();
		Files_scrollPane.setBackground(Color.WHITE);
		
		Commit_scrollPane = new JScrollPane();
		Commit_scrollPane.setBackground(Color.WHITE);
		
		RequirementID_scrollPane = new JScrollPane();
		RequirementID_scrollPane.setBackground(Color.WHITE);
		
		ImpactPercentage_scrollPane = new JScrollPane();
		ImpactPercentage_scrollPane.setBackground(Color.WHITE);
		
		Code_scrollPane = new JScrollPane();
	}
	
	private JPanel createMainFrame() {
		setTitle("Spice Traceability");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		JPanel contentPane = new JPanel();
		contentPane.setPreferredSize(
                Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        setResizable(false);
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		return contentPane;
	}

	void initializeButtonActions(GuiController guiControllertemp) {
		this.guiController = guiControllertemp;
		Files_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				guiController.filesFromDB();
			}
		});
		
		RequirementID_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				guiController.requirementsFromDB();
			}
		});
		
	}
}