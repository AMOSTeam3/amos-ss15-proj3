package de.fau.osr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.SystemColor;
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

	private JPanel contentPane;
	JScrollPane commitFilePane;
	private JButton getInfoButton;
	private JTextField reqtextField;
	private JLabel lblRquirementid;
	private JScrollPane scrollPane;
	private JTextArea textArea;

	JList jList1;
	ArrayList<CommitFile> totalCommitFiles;
	private String reqPattern;
	private DataRetriever dataRetriever;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFileChooser chooser = new JFileChooser();
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
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
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

	/**
	 * Create the frame.
	 */
	public Viewer(DataRetriever dataRetriever, String reqPattern) {
		this.reqPattern = reqPattern;
		this.dataRetriever = dataRetriever;

		setTitle("Spice Traceability");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 882, 584);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		setContentPane(contentPane);

		commitFilePane = new JScrollPane();
		commitFilePane.setBackground(Color.WHITE);
		commitFilePane.setBorder(new BevelBorder(BevelBorder.RAISED,
				SystemColor.activeCaption, null, null, null));

		reqtextField = new JTextField();
		reqtextField.setText("");
		reqtextField.setColumns(10);

		getInfoButton = new JButton("Get Info");
		getInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				ShowCommitFiles(reqtextField.getText());
			}
		});

		lblRquirementid = new JLabel("RequirementID");
		textArea = new JTextArea();
		textArea.setBorder(new BevelBorder(BevelBorder.RAISED, null, null,
				null, null));
		textArea.setBackground(Color.WHITE);
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED,
				SystemColor.activeCaption, null, null, null));
		scrollPane.setViewportView(textArea);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.LEADING)
										.addComponent(getInfoButton)
										.addComponent(reqtextField,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblRquirementid))
						.addGap(45)
						.addComponent(commitFilePane,
								GroupLayout.PREFERRED_SIZE, 313,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 53,
								Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
								345, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addComponent(
																				lblRquirementid)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				reqtextField,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(18)
																		.addComponent(
																				getInfoButton)
																		.addContainerGap())
														.addGroup(
																gl_contentPane
																		.createParallelGroup(
																				Alignment.LEADING)
																		.addComponent(
																				scrollPane,
																				GroupLayout.DEFAULT_SIZE,
																				531,
																				Short.MAX_VALUE)
																		.addComponent(
																				commitFilePane,
																				GroupLayout.DEFAULT_SIZE,
																				531,
																				Short.MAX_VALUE)))));

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

		commitFilePane.setViewportView(jList1);

		MouseEvent listener = new MouseEvent(this, jList1);
		jList1.addMouseListener(listener);
	}

	public void ShowDiff(Point p) {
		textArea.setText("");

		int index = jList1.locationToIndex(p);
		if (index <= totalCommitFiles.size()) {
			textArea.setText(totalCommitFiles.get(index).changedData);
		}
	}
}
