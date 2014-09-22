package com.sling.scripting.handlebars;

import org.junit.Before;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;

/**
 * Created by imad.elali on 22/09/2014.
 */
public class HandlebarsTestBase extends RepositoryTestBase {
    private int counter;
    protected Node rootNode;
    protected EngineHelper helper;

    @Before
    public void setUp() throws Exception {
        rootNode = getTestRootNode();
        helper = new EngineHelper(rootNode);
    }

    protected Node getNewNode() throws RepositoryException, NamingException {
        return getTestRootNode().addNode("test-" + (++counter));
    }

}
