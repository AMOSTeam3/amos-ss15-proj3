package spicetraceability.view;

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

public class Viewer extends JFrame {

	private JPanel contentPane;
	private JTextField uritextField;
	JScrollPane commitFilePane;
	private JButton getInfoButton;
	private JTextField reqtextField;
	VcsController vcsController;
	Tracker tracker;
	JList jList1;
	ArrayList<CommitFile> totalCommitFiles;
	private JLabel lblRepositoryuri;
	private JLabel lblRquirementid;
	private JButton btnConnect;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JTextField reqPattern;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Viewer frame = new Viewer();
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
	public Viewer() {
		setTitle("Spice Traceability");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 882, 584);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		
		 commitFilePane = new JScrollPane();
		 commitFilePane.setBackground(Color.WHITE);
		 commitFilePane.setBorder(new BevelBorder(BevelBorder.RAISED, SystemColor.activeCaption, null, null, null));
		


		uritextField = new JTextField();
		uritextField.setColumns(10);
		
		
		
		reqtextField = new JTextField();
		reqtextField.setText("");
		reqtextField.setColumns(10);
		
		getInfoButton = new JButton("Get Info");
		getInfoButton.setEnabled(false);
		getInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				ShowCommitFiles(reqtextField.getText());
			}
		});
		lblRepositoryuri = new JLabel("RepositoryURI");
		
		lblRquirementid = new JLabel("RequirementID");
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInfoButton.setEnabled(false);
				if(Connect())
				{
					ShowInfoMessage("Connected Successfully");
					getInfoButton.setEnabled(true);
				}
				else
					ShowErrorMessage("Connect Failed, Check the Repository URI");
			}
		});
		textArea = new JTextArea();
		textArea.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		textArea.setBackground(Color.WHITE);
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED, SystemColor.activeCaption, null, null, null));
		scrollPane.setViewportView(textArea);
		
		reqPattern = new JTextField();
		reqPattern.setText("Req-(\\d+)");
		reqPattern.setColumns(10);
		
		JLabel lblRquirementPattern = new JLabel("Rquirement Pattern");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblRepositoryuri)
						.addComponent(uritextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect)
						.addComponent(getInfoButton)
						.addComponent(reqtextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRquirementid)
						.addComponent(reqPattern, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRquirementPattern))
					.addGap(37)
					.addComponent(commitFilePane, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 345, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(56)
						.addComponent(lblRepositoryuri)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(uritextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnConnect)
						.addGap(21)
						.addComponent(lblRquirementPattern)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(reqPattern, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblRquirementid)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(reqtextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(getInfoButton)
						.addContainerGap(253, Short.MAX_VALUE))
					.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
							.addComponent(commitFilePane, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))))
		);
		
		contentPane.setLayout(gl_contentPane);
		
		
		
		
		//commitFilePane.getViewport().getView().addMouseListener(a);
	}
	public void ShowErrorMessage(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void ShowInfoMessage(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	public Boolean Connect(){
		
		vcsController = new VcsController(VcsEnvironment.GIT);
		if(vcsController.Connect(uritextField.getText()))
		{
			tracker = new Tracker(vcsController);
			return true;
		}
		else
			return false;
		
	}
	public void ShowCommitFiles(String requirmentID)
	{
		
		ArrayList<String> commitFileStringList = new ArrayList<String>();
		DataRetriever dataRetriver = new DataRetriever(vcsController, tracker);
		totalCommitFiles = dataRetriver.getCommitFilesForRequirementID(requirmentID,reqPattern.getText());
		Iterator<CommitFile> commitFileIterator = totalCommitFiles.iterator();
		while(commitFileIterator.hasNext())
		{
			commitFileStringList.add(commitFileIterator.next().newPath.getPath());
		}
		final ArrayList<String> commitFileStringListtemp = commitFileStringList;
		jList1 = new javax.swing.JList();
		jList1.setModel(new javax.swing.AbstractListModel() {
			ArrayList<String> stringFinal = commitFileStringListtemp;
            
            public int getSize() { return stringFinal.size(); }
            public Object getElementAt(int i) { return stringFinal.get(i); }
        });

		commitFilePane.setViewportView(jList1);
		
		MouseEvent listener = new MouseEvent(this,jList1);
		jList1.addMouseListener(listener);
	}
	
	public void ShowDiff(Point p)
	{
		textArea.setText("");
		
		int index = jList1.locationToIndex(p);
		if(index <= totalCommitFiles.size()){
			textArea.setText(totalCommitFiles.get(index).changedData);
		}
	}
}
