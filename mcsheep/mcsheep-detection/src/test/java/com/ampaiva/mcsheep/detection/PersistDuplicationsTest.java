package com.ampaiva.mcsheep.detection;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.junit.Ignore;

import com.ampaiva.mcsheep.detection.PersistDuplications;
import com.ampaiva.mcsheep.detection.controller.DataManager;
import com.ampaiva.mcsheep.detection.model.Analyse;
import com.ampaiva.mcsheep.detection.model.Repository;
import com.github.javaparser.ParseException;

public class PersistDuplicationsTest {
    private static final String PU_NAME = "metricsdatamanagerTEST";

    @Ignore
    public void testRun() throws IOException, ParseException {
        DataManager dataManager = new DataManager(PU_NAME);
        PersistDuplications persistDuplications = new PersistDuplications(dataManager, 3, 3);
        persistDuplications.run("src/test/resources/com/ampaiva/metricsdatamanager/util/ZipTest5.zip", false, false);
        dataManager.open();
        Collection<Analyse> analysis = dataManager.findAll(Analyse.class);
        assertEquals(1, analysis.size());
        Collection<Repository> repositories = dataManager.findAll(Repository.class);
        assertEquals(1, repositories.size());
        dataManager.remove(repositories.iterator().next());
        dataManager.close();
    }

}
