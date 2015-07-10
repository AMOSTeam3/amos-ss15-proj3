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
    private boolean enableIndex;
    
    
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
    
    public boolean isEnableIndex() {
        return enableIndex;
    }
    public void setEnableIndex(boolean enableIndex) {
        this.enableIndex = enableIndex;
    }
    @Override
    public String toString() {
        return "Configuration [repoPath=" + repoPath + ", reqPattern="
                + reqPattern + ", dbUsername=" + dbUsername + ", dbPassword="
                + dbPassword + "]";
    }
    
    
    
}
