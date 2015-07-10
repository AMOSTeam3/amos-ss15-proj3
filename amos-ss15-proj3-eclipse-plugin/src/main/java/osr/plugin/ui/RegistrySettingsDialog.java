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
package osr.plugin.ui;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import osr.core.RegistrySettings;
import de.fau.osr.util.AppProperties;

/**
 * @author Gayathery
 * This class is a custom dialog for getting all user settings for the SPICE plugin.
 */
public class RegistrySettingsDialog extends TitleAreaDialog  {
    
    private static Text TdbUsername;
    private Text TdbPassword;
    private Text TgitURL;
    private Text TrequirementPattern;
    
    private String dbUsername;
    private String dbPassword;
    private String gitURL;
    private String requirementPattern;

    public RegistrySettingsDialog(Shell parentShell) {
        super(parentShell);
    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#create()
     * This method creates a dialog and sets the title and type of the JFace dialog
     */
    @Override
    public void create() {
      super.create();
      setTitle("ReqTracker Configuration");
      setMessage("Configuration", IMessageProvider.INFORMATION);
      
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
      Composite area = (Composite) super.createDialogArea(parent);
      Composite container = new Composite(area, SWT.NONE);
      container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      GridLayout layout = new GridLayout(2, false);
      container.setLayout(layout);
     
      createRepositoryURL(container);
      createRequirementPattern(container);
      createDBName(container);
      createDBPassword(container);
   
      return area;
    }
    
    private void createDBName(Composite container) {
        Label lbtLastName = new Label(container, SWT.NONE);
        lbtLastName.setText("DB Username");
        
        GridData dataUsername = new GridData();
        dataUsername.grabExcessHorizontalSpace = true;
        dataUsername.horizontalAlignment = GridData.FILL;
        
        TdbUsername = new Text(container, SWT.BORDER);
        TdbUsername.setLayoutData(dataUsername);
      }

    private void createDBPassword(Composite container) {
        Label lbtLastName = new Label(container, SWT.NONE);
        lbtLastName.setText("DB Password");
        
        GridData dataPassword = new GridData();
        dataPassword.grabExcessHorizontalSpace = true;
        dataPassword.horizontalAlignment = GridData.FILL;
        
        TdbPassword = new Text(container, SWT.PASSWORD|SWT.BORDER);
        TdbPassword.setLayoutData(dataPassword);
      }
    
    private void createRepositoryURL(Composite container) {
        Label lbtLastName = new Label(container, SWT.NONE);
        lbtLastName.setText("Git Repository URL");
        
        GridData dataURL = new GridData();
        dataURL.grabExcessHorizontalSpace = true;
        dataURL.horizontalAlignment = GridData.FILL;
        
        TgitURL = new Text(container, SWT.BORDER);
        TgitURL.setLayoutData(dataURL);
        TgitURL.setText(RegistrySettings.repoURL);
      }
    
    private void createRequirementPattern(Composite container) {
        Label lbtLastName = new Label(container, SWT.NONE);
        lbtLastName.setText("Requirement Pattern");
        
        GridData dataPattern = new GridData();
        dataPattern.grabExcessHorizontalSpace = true;
        dataPattern.horizontalAlignment = GridData.FILL;
        
        TrequirementPattern = new Text(container, SWT.BORDER);
        TrequirementPattern.setLayoutData(dataPattern);
        TrequirementPattern.setText(AppProperties.GetValue("RequirementPattern"));
      }
  
    
   
    private void saveInput() {
     
      dbUsername = TdbUsername.getText();
      dbPassword = TdbPassword.getText();
      requirementPattern = TrequirementPattern.getText();
      gitURL = TgitURL.getText();

    }
    
    @Override
    protected void okPressed() {
      saveInput();
      super.okPressed();
    }


    @Override
    protected boolean isResizable() {
      return true;
    }
    
    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getGitURL() {
        return gitURL;
    }

    public String getRequirementPattern() {
        return requirementPattern;
    }

}
