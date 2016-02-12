package com.ampaiva.mcsheep.detection.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ampaiva.mcsheep.detection.util.Conventions;

public class ConventionsTest {

    @Test
    public void testFileNameInRepository() {
        assertEquals("generic/Class.java",
                Conventions.fileNameInRepository("c:\\Temp", "c:\\Temp\\generic\\Class.java"));
    }

}
