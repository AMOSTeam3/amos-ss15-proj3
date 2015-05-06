package de.fau.osr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.CSVFileDataSource;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.Commit;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.CommitState;
import de.fau.osr.core.vcs.base.VcsEnvironment;
import de.fau.osr.core.vcs.interfaces.VcsClient;

import javax.swing.JButton;

public class Viewer extends JFrame {
	private JScrollPane RequirementID_scrollPane;
	private JScrollPane Commit_scrollPane;
	private JScrollPane Files_scrollPane;
	private JScrollPane Code_scrollPane;
	
	private JList<String> requirements_JList;
	private JList<String> commits_JList;
	private JList<String> files_JList;
	
	private ArrayList<Commit> commits;
	private List<CommitFile> commitFiles;
	
	
	
	private JPanel contentPane;
	
	private JLabel RequirementID_label;

	JList jList1;
	ArrayList<CommitFile> totalCommitFiles;
	private String reqPattern;
    // TODO @Florian: We will need a "String dataSourceFileName"
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

					Path repoFilePath = Paths.get(chooser.getSelectedFile().getAbsolutePath());
					
					try {
					VcsClient client = VcsClient.connect(VcsEnvironment.GIT, repoFilePath.toString());
						Tracker tracker = new Tracker(client);
						Path dataSrcFilePath= repoFilePath.getParent().resolve("dataSource.csv");
                        DataRetriever dataRetriever = null;
						try {
                            DataSource dataSrc = new CSVFileDataSource(dataSrcFilePath.toFile());
                            dataRetriever = new DataRetriever(client, tracker, dataSrc);
						}  catch (Exception e) {
                            e.printStackTrace();
                        }

						String reqPattern = JOptionPane
								.showInputDialog("Choose Requirment Pattern", "Req-(\\d+)");
						if (reqPattern != null && reqPattern.length() > 0) {
							try {
								Viewer frame = new Viewer(dataRetriever,
										reqPattern);
								
								frame.ShowAllRequirements();

								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch(RuntimeException e) {
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
		JPanel panel = new JPanel(new GridLayout());
		String requirementID = requirements_JList.getSelectedValue();
		commits = dataRetriever.getCommitsForRequirementID(requirementID, reqPattern);
		
		String[] commitMessagesArray = new String[commits.size()];
		for(int i = 0; i<commits.size(); i++){
			commitMessagesArray[i] = commits.get(i).message;
		}
		commits_JList = new JList<String>(commitMessagesArray);
		
		panel.add(commits_JList);
		Commit_scrollPane.setViewportView(panel);
		
		MouseEvent listener = new MouseEvent(this, commits_JList, Action.ShowFiles);
		commits_JList.addMouseListener(listener);
	}

	protected void ShowFiles() {
		JPanel panel = new JPanel(new GridLayout());
		commitFiles = commits.get(commits_JList.getSelectedIndex()).files;
		
		
		String[] commitfilesArray = new String[commitFiles.size()];
		for (int i = 0; i < commitFiles.size(); i++) {
			commitfilesArray[i] = commitFiles.get(i).oldPath + " "
					+ commitFiles.get(i).commitState + " "
					+ commitFiles.get(i).newPath;
		}
		files_JList = new JList<String>(commitfilesArray);
		
		panel.add(files_JList);
		Files_scrollPane.setViewportView(panel);
		
		MouseEvent listener = new MouseEvent(this, files_JList, Action.ShowCode);
		files_JList.addMouseListener(listener);
	}
	
	protected void ShowAllFiles() {
		JPanel panel = new JPanel(new GridLayout());
		
		Set<String> files = dataRetriever.getAllFiles();
		
		String[] filesArray = new String[files.size()];
		files_JList = new JList<String>(files.toArray(filesArray));
		panel.add(files_JList);
		Files_scrollPane.setViewportView(panel);
		
		MouseEvent listener = new MouseEvent(this, files_JList, Action.ShowRequirements);
		files_JList.addMouseListener(listener);
		
		
		JPanel panelcommit = new JPanel(new GridLayout());
		Commit_scrollPane.setViewportView(panelcommit);
		
		JPanel panelcode = new JPanel(new GridLayout());
		Code_scrollPane.setViewportView(panelcode);
		
		JPanel panelreq = new JPanel(new GridLayout());
		RequirementID_scrollPane.setViewportView(panelreq);
	}

	protected void ShowAllRequirements() {
		JPanel panel = new JPanel(new GridLayout());
		
		ArrayList<String> requirements = dataRetriever.getRequirementIDs();
		String[] requirementsArray = new String[requirements.size()];
		requirements_JList = new JList<String>(requirements.toArray(requirementsArray));
		panel.add(requirements_JList);
		RequirementID_scrollPane.setViewportView(panel);
		
		MouseEvent listener = new MouseEvent(this, requirements_JList, Action.ShowCommits);
		requirements_JList.addMouseListener(listener);
		
		JPanel panelcommit = new JPanel(new GridLayout());
		Commit_scrollPane.setViewportView(panelcommit);
		
		JPanel panelfiles = new JPanel(new GridLayout());
		Files_scrollPane.setViewportView(panelfiles);
		
		JPanel panelcode = new JPanel(new GridLayout());
		Code_scrollPane.setViewportView(panelcode);
	}
	
	protected void ShowRequirements(){
		JPanel panel = new JPanel(new GridLayout());
		
		List<Integer> requirements = dataRetriever.getRequirementIDsForFile(files_JList.getSelectedValue().replace("\\", "/"));

		String[] requirementsArray = new String[requirements.size()];
		
		requirements_JList = new JList<String>(requirements.toArray(requirementsArray));
		if(requirementsArray.length > 0){
			panel.setBackground(Color.BLUE);
			RequirementID_scrollPane.setBackground(Color.BLUE);
			requirements_JList.setBackground(Color.BLUE);
		}
		panel.add(requirements_JList);
		RequirementID_scrollPane.setViewportView(panel);
	}
	
	protected void ShowCode(){
		JPanel panel = new JPanel(new GridLayout());
		CommitFile commitFile = commitFiles.get(files_JList.getSelectedIndex());
		JTextArea Code_textArea = new JTextArea(commitFile.changedData);
		panel.add(Code_textArea);
		Code_scrollPane.setViewportView(panel);
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

		RequirementID_label = new JLabel("RequirementID");
		
		Commit_scrollPane = new JScrollPane();
		Commit_scrollPane.setBackground(Color.WHITE);
		
		RequirementID_scrollPane = new JScrollPane();
		RequirementID_scrollPane.setBackground(Color.WHITE);
		
		JLabel Commit_label = new JLabel("Commit");
		
		JLabel Files_label = new JLabel("Files");
		
		Code_label = new JLabel("Code");
		
		ImpactPercentage_label = new JLabel("Impact Percentage");
		
		ImpactPercentage_scrollPane = new JScrollPane();
		ImpactPercentage_scrollPane.setBackground(Color.WHITE);
		
		Code_scrollPane = new JScrollPane();
		
		JButton Files_button = new JButton("Show all");
		Files_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ShowAllFiles();
			}
		});
		
		JButton Commit_button = new JButton("Show all");
		
		JButton RequirementID_button = new JButton("Show all");
		RequirementID_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ShowAllRequirements();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(RequirementID_scrollPane, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(RequirementID_button, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
							.addComponent(RequirementID_label)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(Commit_scrollPane, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Files_scrollPane, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Code_scrollPane, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ImpactPercentage_scrollPane, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
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
						.addComponent(Files_scrollPane, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(RequirementID_scrollPane, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(Commit_scrollPane, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(ImpactPercentage_scrollPane, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
						.addComponent(Code_scrollPane, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE))
					.addContainerGap())
		);

		contentPane.setLayout(gl_contentPane);
		
		// commitFilePane.getViewport().getView().addMouseListener(a);
	}
}
