package de.fau.osr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.impl.GitVcsClient;

public class Viewer extends JFrame {
	private JScrollPane RequirementID_scrollPane;
	
	private JPanel contentPane;
	JScrollPane Files_scrollPane;
	private JLabel RequirementID_label;
	private JTextArea Code_textArea;

	JList jList1;
	ArrayList<CommitFile> totalCommitFiles;
	private String reqPattern;
	private DataRetriever dataRetriever;
	private JLabel Code_label;
	private JLabel ImpactPercentage_label;
	private JScrollPane ImpactPercentage_scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFileChooser chooser = new JFileChooser("..");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				int returnValue = chooser.showDialog(null,
						"Auswahl des Repository");
				
				if (returnValue == chooser.APPROVE_OPTION) {
					VcsController vcsController = new VcsController(
							VcsEnvironment.GIT);
					if (vcsController.Connect(chooser.getSelectedFile()
							.getAbsolutePath())) {
						Tracker tracker = new Tracker(vcsController);
						DataRetriever dataRetriever = new DataRetriever(
								vcsController, tracker);

						String reqPattern = JOptionPane
								.showInputDialog("Choose Requirment Pattern", "Req-(\\d+)");
						if (reqPattern != null && reqPattern.length() > 0) {
							try {
								Viewer frame = new Viewer(dataRetriever,
										reqPattern);
								
								frame.ShowRequirements();
								frame.ShowCommits();
								frame.ShowFiles();
								
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{
						JOptionPane.showMessageDialog(null,
								"Sie müssen ein Repository auswählen!",
								"Fehler bei er Repository Auswahl",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (returnValue == chooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null,
							"Sie müssen ein Repository auswählen!",
							"Fehler bei er Repository Auswahl",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	protected void ShowCommits() {
		// TODO Auto-generated method stub
		
	}

	protected void ShowFiles() {
		// TODO Auto-generated method stub
		
	}

	protected void ShowRequirements() {
		ArrayList<String> requirements = dataRetriever.getRequirementIDs();
		for(String requirement: requirements){
			RequirementID_scrollPane.add(new JLabel(requirement));
		}
	}

	/**
	 * Create the frame.
	 */
	public Viewer(DataRetriever dataRetriever, String reqPattern) {
		this.reqPattern = reqPattern;
		this.dataRetriever = dataRetriever;

		setTitle("Spice Traceability");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setPreferredSize(
                Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        setResizable(false);
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);

		Files_scrollPane = new JScrollPane();
		Files_scrollPane.setBackground(Color.WHITE);
		Files_scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED,
				SystemColor.activeCaption, null, null, null));

		RequirementID_label = new JLabel("RequirementID");
		
		JScrollPane Commit_scrollPane = new JScrollPane();
		
		RequirementID_scrollPane = new JScrollPane();
		
		JLabel Commit_label = new JLabel("Commit");
		
		JLabel Files_label = new JLabel("Files");
		
		Code_label = new JLabel("Code");
		
		ImpactPercentage_label = new JLabel("Impact Percentage");
		
		ImpactPercentage_scrollPane = new JScrollPane();
		
		Code_textArea = new JTextArea();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(RequirementID_scrollPane, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addComponent(RequirementID_label))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(118)
							.addComponent(Commit_label, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 268, Short.MAX_VALUE)
							.addComponent(Files_label)
							.addGap(574)
							.addComponent(Code_label)
							.addGap(346)
							.addComponent(ImpactPercentage_label, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(Commit_scrollPane, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Files_scrollPane, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Code_textArea, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ImpactPercentage_scrollPane, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(Commit_label)
						.addComponent(Files_label)
						.addComponent(Code_label)
						.addComponent(ImpactPercentage_label)
						.addComponent(RequirementID_label))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(Files_scrollPane, GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
						.addComponent(RequirementID_scrollPane, GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
						.addComponent(Commit_scrollPane, GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
						.addComponent(ImpactPercentage_scrollPane, GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
						.addComponent(Code_textArea, GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE))
					.addContainerGap())
		);

		contentPane.setLayout(gl_contentPane);
		
		// commitFilePane.getViewport().getView().addMouseListener(a);
	}

	public void ShowErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public void ShowInfoMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public void ShowCommitFiles(String requirmentID) {

		ArrayList<String> commitFileStringList = new ArrayList<String>();
		totalCommitFiles = dataRetriever.getCommitFilesForRequirementID(
				requirmentID, reqPattern);
		Iterator<CommitFile> commitFileIterator = totalCommitFiles.iterator();
		while (commitFileIterator.hasNext()) {
			commitFileStringList.add(commitFileIterator.next().newPath
					.getPath());
		}
		final ArrayList<String> commitFileStringListtemp = commitFileStringList;
		jList1 = new javax.swing.JList();
		jList1.setModel(new javax.swing.AbstractListModel() {
			ArrayList<String> stringFinal = commitFileStringListtemp;

			public int getSize() {
				return stringFinal.size();
			}

			public Object getElementAt(int i) {
				return stringFinal.get(i);
			}
		});

		Files_scrollPane.setViewportView(jList1);

		MouseEvent listener = new MouseEvent(this, jList1);
		jList1.addMouseListener(listener);
	}

	public void ShowDiff(Point p) {
		Code_textArea.setText("");

		int index = jList1.locationToIndex(p);
		if (index <= totalCommitFiles.size()) {
			Code_textArea.setText(totalCommitFiles.get(index).changedData);
		}
	}
}
