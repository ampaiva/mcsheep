package com.ampaiva.mcsheep.detection.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.ampaiva.mcsheep.detection.util.ZipUtil;

public class ZipUtilTest {

    @Test
    public void testGetFilesInZip() throws IOException {
        ZipUtil zipUtil = new ZipUtil("src/test/resources/com/ampaiva/metricsdatamanager/util/ZipTest1.zip");
        Map<String, String> files = zipUtil.getCodeSource();
        assertNotNull(files);
        assertEquals(2, files.size());
    }

}
