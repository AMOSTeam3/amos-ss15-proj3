package de.fau.osr.gui.Model.DataElements;

/**
 * @author Gayathery
 * This is a data element for application configuration
 *
 */
public class Configuration {

    private String repoPath;
    private String reqPattern;
    private String dbUsername;
    private String dbPassword;
    
    
    public String getRepoPath() {
        return repoPath;
    }
    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }
    public String getReqPattern() {
        return reqPattern;
    }
    public void setReqPattern(String reqPattern) {
        this.reqPattern = reqPattern;
    }
    public String getDbUsername() {
        return dbUsername;
    }
    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }
    public String getDbPassword() {
        return dbPassword;
    }
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    @Override
    public String toString() {
        return "Configuration [repoPath=" + repoPath + ", reqPattern="
                + reqPattern + ", dbUsername=" + dbUsername + ", dbPassword="
                + dbPassword + "]";
    }
    
    
    
}
