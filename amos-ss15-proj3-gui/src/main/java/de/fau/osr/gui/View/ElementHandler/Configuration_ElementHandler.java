/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fau.osr.gui.View.ElementHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import de.fau.osr.gui.Controller.GuiController;
import de.fau.osr.gui.Model.DataElements.Configuration;
import de.fau.osr.gui.View.PopupManager;
import de.fau.osr.gui.util.FileUtil;
import de.fau.osr.util.AppProperties;

/**
 * @author Gayathery
 * This is a element handler for the Home Screen configurations.
 */
public class Configuration_ElementHandler extends JPanel {
    private JTextField input_repoPath;
    private JTextField txtrreqd;
    private JTextField input_dbusername;
    private JTextField input_dbpassword;
    private JCheckBox enableIndex;
    
    private GuiController guiController;
    private JTabbedPane jpane;
    
    private boolean result;
    
    private final Action action = new SwingAction();
    private final Action action_1 = new FileChooseAction();
    private Font font_bold = new Font("Tahoma", Font.BOLD, 18);
    private Font font_plain = new Font("Tahoma", Font.PLAIN, 18);
   
    public void setController(GuiController guiController,JTabbedPane jPane){
        this.guiController = guiController;
        this.jpane = jPane;
    }

    public boolean getResult(){
        return result;
    }
    /**
     * Create the panel.
     */
    public Configuration_ElementHandler() {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("350px"),},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,}));
        
        JLabel lblApplicationConfiguration = new JLabel("Application Configuration");
        lblApplicationConfiguration.setFont(font_bold);
        lblApplicationConfiguration.setForeground(Color.darkGray);
        add(lblApplicationConfiguration, "2, 2, center, default");
        
        JLabel lblNewLabel = new JLabel("Repository Path");
        lblNewLabel.setFont(font_plain);
        add(lblNewLabel, "2, 6");
        
        JButton btnChooseRepository = new JButton("Choose Repository");
        btnChooseRepository.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnChooseRepository.setFont(font_plain);
        btnChooseRepository.setAction(action_1);
        add(btnChooseRepository, "6, 6, 1, 2");
        
        input_repoPath = new JTextField();
        add(input_repoPath, "6, 8, fill, default");
        input_repoPath.setColumns(30);
        
        JTextArea txtrEnterPatternAs = new JTextArea();
        txtrEnterPatternAs.setEditable(false);
        txtrEnterPatternAs.setBackground(Color.lightGray);
        txtrEnterPatternAs.setText("Enter pattern as RegEx.For multiple patters seperate with '|'");
        add(txtrEnterPatternAs, "6, 10, left, center");
        
       
        JLabel lblNewLabel_1 = new JLabel("Requirement Pattern");
        lblNewLabel_1.setFont(font_plain);
        add(lblNewLabel_1, "2, 12");
        
        txtrreqd = new JTextField();
        txtrreqd.setText(AppProperties.GetValue("RequirementPattern"));
        
        add(txtrreqd, "6, 12, fill, default");
        txtrreqd.setColumns(30);
        
        JLabel lblPerformance = new JLabel("Performance Optimization");
        lblPerformance.setFont(font_plain);
        add(lblPerformance, "2, 14");
        
        enableIndex = new JCheckBox("Enable Indexing");
        enableIndex.setSelected(false);
        enableIndex.setFont(font_bold);
        add(enableIndex, "6, 14, fill, default");
        
        JLabel lblConfigureDatabase = new JLabel("Configure Database:");
        lblConfigureDatabase.setFont(font_bold);
        lblConfigureDatabase.setForeground(Color.darkGray);
        add(lblConfigureDatabase, "2, 18");
        
        JLabel lblDatabaseUsername = new JLabel("Database username");
        lblDatabaseUsername.setFont(font_plain);
        add(lblDatabaseUsername, "2, 22");
        
        input_dbusername = new JTextField();
        add(input_dbusername, "6, 22, fill, top");
        input_dbusername.setColumns(30);
        
        JLabel lblDatabasePassword = new JLabel("Database Password");
        lblDatabasePassword.setFont(font_plain);
        add(lblDatabasePassword, "2, 26");
        
        input_dbpassword = new JPasswordField();
        add(input_dbpassword, "6, 26, fill, top");
        input_dbpassword.setColumns(30);
        
        
        JButton btnConfigure = new JButton("Configure");
        btnConfigure.setAction(action);
        btnConfigure.setFont(font_plain);
        add(btnConfigure, "6, 30");
        setInitData();

    }
    
    public void setInitData(){
        FileUtil fileUtil = new FileUtil();
        List<String> configInfo = fileUtil.readConfigFile();
        if(configInfo.size() > 0){
            input_repoPath.setText(configInfo.get(0));
            txtrreqd.setText(configInfo.get(1));
            if(configInfo.get(2).equals("true")){
                enableIndex.setSelected(true);
            }
            else{
                enableIndex.setSelected(false);
            }
            input_dbusername.setText(configInfo.get(3));
            input_dbpassword.setText(configInfo.get(4));
                
        }
        else{
           /* PopupManager popupManager = new PopupManager();
            popupManager.showErrorDialog("Could not read from config file");*/
        }
    }

    class SwingAction extends AbstractAction {
        
        public SwingAction() {
            putValue(NAME, "Configure");
            putValue(SHORT_DESCRIPTION, "perform configuration");
        }
        public void actionPerformed(ActionEvent e) {
            String dbPassword = input_dbpassword.getText();
            String dbUsername = input_dbusername.getText();
            String repoPath = input_repoPath.getText();
            String reqPattern = txtrreqd.getText();
            String enableIndexingValue = "false";
            if(enableIndex.isSelected())
                enableIndexingValue = "true";
           
            Configuration configuration = new Configuration();
            configuration.setDbPassword(dbPassword);
            configuration.setDbUsername(dbUsername);
            configuration.setRepoPath(repoPath);
            configuration.setReqPattern(reqPattern);
            configuration.setEnableIndex(enableIndex.isSelected());
            List<String> configData = new ArrayList<String>();
            configData.add(repoPath);
            configData.add(reqPattern);
            configData.add(enableIndexingValue);
            configData.add(dbUsername);
            configData.add(dbPassword);
            
            if(guiController.configureApplication(configuration)){   
                PopupManager popupManager = new PopupManager();
                int retVal = popupManager.Configuration_Persist_OptionDialog();
                FileUtil fileUtil = new FileUtil();
                if(retVal == 0){                    
                    if(!fileUtil.writeConfigFile(configData)){                    
                        popupManager.showErrorDialog("Could not write to config file");
                    }
                }
                else if (retVal == 1){
                    fileUtil.cleanConfigFile();
                }
                    
                    
                jpane.setEnabledAt(1, true);                
                jpane.setEnabledAt(2,true);
                jpane.setEnabledAt(3, true);
                jpane.setEnabledAt(4, true);
                jpane.setSelectedIndex(1);
                
            }
               
            
               
        }
    }
    private class FileChooseAction extends AbstractAction {
        public FileChooseAction() {
            putValue(NAME, "Choose Repository");
            putValue(SHORT_DESCRIPTION, "Choose the repository");
        }
        public void actionPerformed(ActionEvent e) {
           
            JFileChooser chooser = new JFileChooser("src/main");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileHidingEnabled(false);
            int returnValue = chooser.showDialog(null,"Choose Repository");

            if (returnValue == JFileChooser.APPROVE_OPTION) 
                input_repoPath.setText(chooser.getSelectedFile().getAbsolutePath());
           
        }
    }
    
    
}

