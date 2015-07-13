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
package de.fau.osr.core.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import de.fau.osr.core.db.dao.CommitDao;
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.dao.impl.CommitDaoImplementation;
import de.fau.osr.core.db.dao.impl.RequirementDaoImplementation;
import de.fau.osr.core.db.domain.Commit;
import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.SessionFactory;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DataSource implementation, that gets data from Database
 * Created by Dmitry Gorelenkov on 01.06.2015.
 */
public class DBDataSource extends DataSource {

    protected final CommitDao commitDao;
    protected final RequirementDao reqDao;

    /**
     * used <tt>HibernateUtil.getSessionFactory()</tt> to get default database session factory
     */
    public DBDataSource() {
        this(HibernateUtil.getSessionFactory());
    }

    public DBDataSource(SessionFactory sf) {
        reqDao = new RequirementDaoImplementation(sf);
        commitDao = new CommitDaoImplementation(sf);
    }
    @Override
    protected SetMultimap<String, String> doGetAllReqCommitRelations() throws IOException {
        List<Requirement> reqs = reqDao.getAllRequirements();
        SetMultimap<String, String> result = HashMultimap.create();
        for (Requirement req : reqs){
            for (Commit commit : req.getCommits()){
                result.put(req.getId(), commit.getId());
            }
        }

        return result;
    }

    @Override
    protected void doAddReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        Requirement req = reqDao.getRequirementById(reqId);

        Commit commitToAdd = commitDao.getCommitById(commitId);
        //create commit if not exist
        if (commitToAdd == null) {
            commitToAdd = new Commit();
            commitToAdd.setId(commitId);
            commitDao.persist(DBOperation.ADD, commitToAdd);
        }

        //check if already linked
        else if (req.getCommits().contains(commitToAdd)) {
            throw new IOException("Linkage already exists");
        }

        //add to req
        req.getCommits().add(commitToAdd);

        //update req
        if (!reqDao.persist(DBOperation.UPDATE, req)){
            throw new IOException("Can not add requirement - commit relation");
        }
    }

    @Override
    protected void doRemoveReqCommitRelation(String reqId, String commitId) throws IOException, OperationNotSupportedException {
        Requirement req = reqDao.getRequirementById(reqId);
        if (req == null) {
            return;
        }

        Commit commit = commitDao.getCommitById(commitId);
        if (commit == null) {
            return;
        }

        req.getCommits().remove(commit);
        if (!reqDao.persist(DBOperation.UPDATE, req)){
            throw new IOException("Can not remove requirement - commit relation");
        }
    }

    @Override
    protected void doSaveOrUpdateRequirement(String id, String title, String description) throws IOException, OperationNotSupportedException {
        Requirement req = reqDao.getRequirementById(id);
        DBOperation oper = DBOperation.UPDATE;

        //create if not exists now
        if (req == null) {
            req = new Requirement();
            req.setId(id);
            oper = DBOperation.ADD;
        }

        //set values, and save
        req.setTitle(title);
        req.setDescription(description);

        if (!reqDao.persist(oper, req)){//todo let DAO throw the exceptions?
            throw new IOException("Cannot save requirement id: " + id);
        }
    }

    @Override
    protected Set<de.fau.osr.core.Requirement> doGetAllRequirements() throws IOException {
        List<Requirement> dbReqs = reqDao.getAllRequirements();
        Set<de.fau.osr.core.Requirement> result = new HashSet<>();

        for (Requirement r : dbReqs) {
            //convert commits to IDs only
            Set<String> commits = new HashSet<>();
            for (de.fau.osr.core.db.domain.Commit commit : r.getCommits()) {
                commits.add(commit.getId());
            }

            //create Core-Req by DB-Req
            result.add(new de.fau.osr.core.Requirement(
                    r.getId(), r.getDescription(), r.getTitle(), commits, r.getStoryPoint())
            );
        }

        return result;
    }
}
