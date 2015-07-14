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
package de.fau.osr.gui.Model;

import de.fau.osr.bl.RequirementsTraceabilityMatrix;
import de.fau.osr.bl.RequirementsTraceabilityMatrixByImpact;
import de.fau.osr.gui.Model.DataElements.*;

import java.util.Collection;
import java.util.regex.Pattern;

public interface I_Model {

    Collection<Requirement> getAllRequirements();

    Collection<Commit> getCommitsFromRequirement(Requirement requirement);

    Collection<PathDE> getFiles();

    Collection<Requirement> getRequirementsByFile(PathDE file);

    Collection<Commit> getCommitsByFile(PathDE file);

    Collection<PathDE> getFilesByCommit(Commit commit);

    Pattern getCurrentRequirementPattern();

    void setReqPatternString(Pattern reqPattern);

    String getCurrentRepositoryPath();

    Collection<Commit> getAllCommits();

    Collection<Requirement> getRequirementsFromCommit(Commit commit);

    float getImpactForRequirementAndFile(Requirement requ, PathDE path);

    Collection<PathDE> getFilesByRequirement(Requirement requirement);

    void addRequirementCommitRelation(Requirement requirement, Commit commit) throws Exception;

    void removeRequirementCommitLinkage(Requirement requirement, Commit commit) throws Exception;

    Collection<AnnotatedLine> getAnnotatedLines(PathDE filePath);

    RequirementsTraceabilityMatrix generateRequirementsTraceability();

    RequirementsTraceabilityMatrixByImpact generateRequirementsTraceabilityByImpact();

    boolean updateRequirement(String id, String title, String description);
}
