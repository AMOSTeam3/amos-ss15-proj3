
package de.fau.osr.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.util.filtering.FilterByExactString;
import de.fau.osr.util.sorting.SortByCommitID;
import de.fau.osr.util.sorting.SortByFilename;

public class GuiViewElementHandler extends JFrame {
	public enum ButtonState{Deactivate, Activate}

	private static final long serialVersionUID = 1L;
	private GuiController guiController;

	private JLabel RequirementID_label = new JLabel("RequirementID");
	private JLabel Code_label = new JLabel("Code");
	private JLabel ImpactPercentage_label = new JLabel("Impact Percentage");
	private JLabel Commit_label = new JLabel("Commit");
	private JLabel Files_label = new JLabel("Files");
	
	private JButton Files_button = new JButton("Navigate From File");
	private JButton Commit_button = new JButton("Navigate From Commit");
	private JButton RequirementID_button = new JButton("Navigate From ID");
	private JButton Linkage_button = new JButton("Add Linkage");
	
	private JTextField RequirementID_textField = new JTextField();
	private JTextField Commit_textField = new JTextField();
	private JTextField RequirementSearch_textField = new JTextField();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mnTools = menuBar.add(new JMenu("Tools"));
	private JMenuItem mntmConfigure = mnTools.add(new JMenuItem("Configure"));
	final private String[] SORT_COMBOBOX_CHOICES = {
			"sort by chronic", "sort by filename"
	};
	
	//this enables eclipse to use the WindowBuilder
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox<String> FilesSort_combobox = new JComboBox(SORT_COMBOBOX_CHOICES);
	
	
	private JScrollPane RequirementID_scrollPane = new JScrollPane();
	private JScrollPane Commit_scrollPane = new JScrollPane();
	private JScrollPane Files_scrollPane = new JScrollPane();
	private JScrollPane Code_scrollPane = new JScrollPane();
	private JScrollPane ImpactPercentage_scrollPane = new JScrollPane();


	
	final private List<Comparator<CommitFile>> SORT_ALGORITHMS = Arrays.asList(
			new SortByCommitID(), new SortByFilename()
	);
	private JLabel FilesSort_label = new JLabel("Sort By:");

	public GuiViewElementHandler(GuiController guiController) {
		this.guiController = guiController;
		initializeButtonActions();
		initializeComboboxActions();
		initializeSearchTextField();
		setTitle("Spice Traceability");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBackground(Color.WHITE);
		setJMenuBar(menuBar);
		for(JTextField textField : new JTextField[]{RequirementID_textField, Commit_textField}) {
			textField.setEditable(false);
			textField.setColumns(10);
		}
		
		positionElements();

		pack();
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
	private void positionElements() {
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						//here follow the columns of the UI
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(RequirementID_textField)
								.addComponent(RequirementID_button)
								.addComponent(RequirementID_label)
								.addComponent(RequirementSearch_textField)
								.addComponent(RequirementID_scrollPane))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(Commit_textField)
								.addComponent(Commit_button)
								.addComponent(Commit_label)
								.addComponent(Commit_scrollPane, 10, 100, Short.MAX_VALUE))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(Linkage_button)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addComponent(Files_button)
										.addComponent(FilesSort_combobox)
										.addComponent(Files_label)
										.addComponent(Files_scrollPane, 10, 100, Short.MAX_VALUE)
										)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(Code_label)
								.addComponent(Code_scrollPane, 10, 400, Short.MAX_VALUE))
						)
		);
		
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						//two rows, one to create linkage, the other one for the rest
						.addGroup(layout.createParallelGroup()
								//GroupLayout.PREFERRED_SIZE in the three arguments prevents the TextFields from scaling to multirow input
								.addComponent(RequirementID_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(Commit_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(Linkage_button, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(RequirementID_button)
										.addComponent(RequirementID_label)
										.addComponent(RequirementSearch_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(RequirementID_scrollPane))
								.addGroup(layout.createSequentialGroup()
										.addComponent(Commit_button)
										.addComponent(Commit_label)
										.addComponent(Commit_scrollPane))
								.addGroup(layout.createSequentialGroup()
										.addComponent(Files_button)
										.addComponent(FilesSort_combobox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(Files_label)
										.addComponent(Files_scrollPane))
								.addGroup(layout.createSequentialGroup()
										.addComponent(Code_label)
										.addComponent(Code_scrollPane))
						)
				)
		);

		//make the requirement column non-resizable and have all elements with the same horizontal size
		layout.linkSize(SwingConstants.HORIZONTAL, RequirementID_button, RequirementID_scrollPane, RequirementID_textField, RequirementSearch_textField);
		
		setLayout(layout);
	}
	
	void initializeButtonActions() {
		Files_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				guiController.filesFromDB();
			}
		});
		
		RequirementID_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					guiController.requirementsFromDB();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
				try {
					guiController.reConfigure();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		RequirementSearch_textField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				guiController.setRequirementIDFiltering(new FilterByExactString(RequirementSearch_textField.getText()));
			}
		});
		
	}

	void initializeComboboxActions() {

		// Defining default selection.
		FilesSort_combobox.setSelectedIndex(0);
		guiController.setCommitFileSorting(SORT_ALGORITHMS.get(0));

		FilesSort_combobox.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						guiController.setCommitFileSorting(SORT_ALGORITHMS.get(FilesSort_combobox.getSelectedIndex()));

					}
				}
		);
	}

	void initializeSearchTextField() {
		//TODO insert search text field initialization
		guiController.setRequirementIDFiltering(new FilterByExactString());
	}
	
	void switchLinkageButton(ButtonState Linkage_ButtonState) {
		switch(Linkage_ButtonState){
		case Deactivate:
			Linkage_button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					guiController.requirementsAndCommitsFromDB();
				}
			});
			break;
		case Activate:
			Linkage_button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!guiController.requirements_JList.isSelectionEmpty() && !guiController.commitMessages_JList.isSelectionEmpty()){
						guiController.addLinkage(guiController.requirements_JList.getSelectedValue(), guiController.commitMessages_JList.getSelectedIndex());
					}
				}
			});
			break;
		}
	}
	
}
