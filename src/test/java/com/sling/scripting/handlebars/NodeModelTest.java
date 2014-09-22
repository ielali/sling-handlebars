/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sling.scripting.handlebars;

import org.junit.Before;
import org.junit.Test;

import javax.jcr.Node;

import static org.junit.Assert.assertEquals;

/**
 * Test helper node model.
 */
public class NodeModelTest extends HandlebarsTestBase {
    private Node node1;
    private Node node2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        node1 = rootNode.addNode("child1");
        node1.setProperty("text", "Test-" + System.currentTimeMillis());
        node2 = rootNode.addNode("child2", "nt:unstructured");
    }

    @Test
    public void testDefaultValue() throws Exception {
        assertEquals(rootNode.getPath(), helper.evalToString("{{node.path}}"));
    }

}
