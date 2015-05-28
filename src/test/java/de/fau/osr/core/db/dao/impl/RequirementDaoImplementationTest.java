package de.fau.osr.core.db.dao.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.domain.Requirement;

public class RequirementDaoImplementationTest {

	RequirementDao dao = new RequirementDaoImplementation();
	
	
	@Test
	public void testAdd() {
		Requirement r = prepareData();
		assertTrue(dao.persist(DBOperation.ADD,r));
		
		List<Requirement> l = dao.getAllRequirement();
		System.out.println(l.size());
		assertNotNull(l);
		
		r.setDescription("upd");
		assertTrue(dao.persist(DBOperation.UPDATE, r));
		
		assertTrue(dao.persist(DBOperation.DELETE, r));
	}


	
	public Requirement prepareData(){
		Requirement r = new Requirement();
		r.setId("Test"+Math.random());
		r.setTitle("title-test");
		r.setDescription("desc-test");
		r.setStoryPoint(1);
		return r;
	}
	
	

}
