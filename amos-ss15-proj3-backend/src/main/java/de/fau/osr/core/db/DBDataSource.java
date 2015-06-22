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

    private CommitDao commitDao;
    private final RequirementDao reqDao;

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
        //create commit if not exist
        Commit commitToAdd = new Commit();
        commitToAdd.setId(commitId);
        commitDao.persist(DBOperation.ADD, commitToAdd);

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
