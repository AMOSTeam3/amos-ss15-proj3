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
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GuiViewElementHandler extends JFrame{
	private static final long serialVersionUID = 1L;
	private GuiController guiController;
	
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
	private JButton Linkage_button;
	
	private JTextField RequirementID_textField;
	private JTextField Commit_textField;
	private JMenuBar menuBar;
	private JMenu mnTools;
	private JMenuItem mntmConfigure;

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
	
	public JTextField getRequirementID_textField() {
		return RequirementID_textField;
	}
	
	public JTextField getCommit_textField() {
		return Commit_textField;
	}
	
	/**
	 * @param contentPane
	 */
	public void createLayout(JPanel contentPane) {
		createScrollPanes();
		
		createLabels();
	
		createButtons();
		
		createTextFields();
		
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
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(RequirementID_scrollPane, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Commit_scrollPane, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(27)
							.addComponent(RequirementID_label)
							.addGap(112)
							.addComponent(Commit_label, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(RequirementID_textField, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(Commit_textField, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(Linkage_button))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(RequirementID_button, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(Commit_button, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)))))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(6)
									.addComponent(Files_scrollPane, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(Code_scrollPane, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(581)
									.addComponent(Code_label)
									.addGap(484)))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(ImpactPercentage_label, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(ImpactPercentage_scrollPane, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(81)
							.addComponent(Files_button))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(128)
							.addComponent(Files_label)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(Code_label)
							.addComponent(ImpactPercentage_label))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(RequirementID_label)
								.addComponent(Commit_label)
								.addComponent(Files_label))
							.addGap(6)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(RequirementID_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(Commit_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(Linkage_button))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(RequirementID_button)
								.addComponent(Files_button)
								.addComponent(Commit_button))))
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(RequirementID_scrollPane, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
						.addComponent(Code_scrollPane, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
						.addComponent(ImpactPercentage_scrollPane, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
						.addComponent(Files_scrollPane, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
						.addComponent(Commit_scrollPane, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE))
					.addContainerGap())
		);

		contentPane.setLayout(gl_contentPane);
	}

	
	private void createTextFields() {
		RequirementID_textField = new JTextField();
		RequirementID_textField.setEditable(false);
		RequirementID_textField.setColumns(10);
		
		Commit_textField = new JTextField();
		Commit_textField.setEditable(false);
		Commit_textField.setColumns(10);
	}

	
	private void createButtons() {
		Files_button = new JButton("Navigate From File");
		Commit_button = new JButton("Navigate From Commit");
		RequirementID_button = new JButton("Navigate From ID");
		Linkage_button = new JButton("Add Linkage");
	}

	
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
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		mntmConfigure = new JMenuItem("Configure");
		mnTools.add(mntmConfigure);
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
		
		Commit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				guiController.commitsFromDB();
			}
		});
		
		Linkage_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				guiController.requirementsAndCommitsFromDB();
			}
		});
		
		mntmConfigure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				guiController.reConfigure();
			}
		});
		
	}
}