package com.ampaiva.mcsheep.detection.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ampaiva.mcsheep.detection.controller.DataManager;
import com.ampaiva.mcsheep.detection.model.Repository;

public class DataManagerTest {

    private static final String PU_NAME = "metricsdatamanagerTEST";
    private static final String PROJECT_LOCATION = "/somewhere/TestProject.zip";

    @Test
    public void testEmpty() throws Exception {
        DataManager dataManager = new DataManager(PU_NAME);
        dataManager.open();
        Repository repository = new Repository();
        repository.setLocation(PROJECT_LOCATION);
        dataManager.persist(repository);
        dataManager.commit();

        int id1 = repository.getId();
        assertTrue(id1 > 0);
        Repository p1 = dataManager.find(Repository.class, id1);
        assertNotNull(p1);
        assertEquals(id1, repository.getId());
        assertEquals(PROJECT_LOCATION, repository.getLocation());
        Repository p1_2 = dataManager.find(Repository.class, id1);
        assertNotNull(p1_2);
        dataManager.begin();
        dataManager.remove(p1_2);
        dataManager.commit();
        p1_2 = dataManager.find(Repository.class, repository.getId());
        assertNull(p1_2);
        dataManager.close();
    }
}
