/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.graph;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import org.neo4j.graphdb.GraphDatabaseService;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

public class BaseGraphDetectorTest extends BaseDetectorTest {

    protected GraphDatabaseService db;

    @BeforeMethod
    public void initGraph() throws Exception {

        File tempDb = TempDatabase.createTempDirectory();
        db = GraphInstance.getInstance().init(tempDb.getCanonicalPath());
        GraphInstance.mustDeleteFileCreated = true;
        GraphInstance.mustShutdownDatabase = false;
    }

    @AfterMethod
    public void cleanUp() { //
        GraphInstance.getInstance().clearDatabase();
    }
}
