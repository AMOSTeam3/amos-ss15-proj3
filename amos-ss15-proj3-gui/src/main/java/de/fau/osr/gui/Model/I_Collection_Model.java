/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.Model;

import com.google.common.base.Predicate;
import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.Model.DataElements.Commit;
import de.fau.osr.gui.Model.DataElements.DataElement;
import de.fau.osr.gui.Model.DataElements.Requirement;
import de.fau.osr.gui.Model.DataElements.PathDE;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Adapter Pattern used. Since the Functionlibrary itself
 * can not simply provide the given return types correlating to
 * the specific formatted input, this class defines the Interface
 * to be provided by the adapter
 */

public interface I_Collection_Model {

    Collection<? extends DataElement> getRequirements(Predicate<Requirement> requirementIDFiltering) throws IOException;

    Collection<? extends DataElement> getCommitsByRequirement(Collection<Requirement> requirements) throws IOException;

    List<? extends DataElement> getFilePaths();

    Collection<? extends DataElement> getRequirementsByFile(Collection<PathDE> files) throws IOException;

    Collection<? extends DataElement> getCommitsByFile(Collection<PathDE> files);

    List<? extends DataElement> getFilesByCommit(Collection<Commit> commits) throws FileNotFoundException;

    String getChangeDataFromFileIndex(PathDE file) throws FileNotFoundException;

    Pattern getCurrentRequirementPattern();

    void setCurrentRequirementPattern(Pattern pattern);

    String getCurrentRepositoryPath();

    Collection<? extends DataElement> getCommitsFromDB();

    Collection<? extends DataElement> getRequirementsByCommit(Collection<Commit> commits) throws IOException;

    Collection<? extends DataElement> getCommitsByRequirementAndFile(Collection<Requirement> requirements, Collection<PathDE> pathDE) throws IOException;

    Collection<? extends DataElement> getRequirementsByFileAndCommit(Collection<Commit> commits, Collection<PathDE> files) throws IOException;

    List<? extends DataElement> getFilesByRequirement(Collection<Requirement> requirements) throws IOException;

    void addRequirementCommitLinkage(Requirement requirement, Commit commit) throws Exception;

    Collection<? extends DataElement> getAnnotatedLinesByFile(Collection<PathDE> files) throws FileNotFoundException, IOException, GitAPIException ;

    RequirementsTraceabilityMatrix getRequirementsTraceability() throws IOException;

    RequirementsTraceabilityMatrixByImpact getRequirementsTraceabilityByImpact() throws IOException;

    boolean updateRequirement(String id, String title, String description);
    
    List<? extends DataElement> getImpactForRequirementAndFile(Collection<Requirement> requirements, List<PathDE> path);
}
