package com.sling.scripting.handlebars;

import org.apache.sling.commons.testing.jcr.RepositoryUtil;
import org.apache.sling.jcr.api.SlingRepository;
import org.junit.After;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.naming.NamingException;

public class RepositoryTestBase {
    private static SlingRepository repository;
    protected Node testRoot;
    protected Session session;
    private int counter;

    public RepositoryTestBase() {
    }

    private static class ShutdownThread
            extends Thread {
        private ShutdownThread(Object o) {
        }

        public void run() {
            try {

            } catch (Exception e) {
                System.out.println("Exception in ShutdownThread:" + e);
            }
        }
    }

    @After
    public void tearDown()
            throws Exception {
        if (this.session != null) {
            this.session.logout();
        }
    }

    protected Session getSession()
            throws RepositoryException, NamingException {
        if (this.session == null) {
            this.session = getRepository().loginAdministrative(null);
        }
        return this.session;
    }

    protected Node getTestRootNode()
            throws RepositoryException, NamingException {
        if (this.testRoot == null) {
            Node root = getSession().getRootNode();
            Node classRoot = root.addNode(getClass().getSimpleName());
            this.testRoot = classRoot.addNode(System.currentTimeMillis() + "_" + ++this.counter);
        }
        return this.testRoot;
    }

    protected SlingRepository getRepository()
            throws RepositoryException, NamingException {
        if (repository != null) {
            return repository;
        }
        synchronized (RepositoryTestBase.class) {
            if (repository == null) {
                RepositoryUtil.startRepository();
                repository = RepositoryUtil.getRepository();
                Runtime.getRuntime().addShutdownHook(new ShutdownThread(null));
            }
            return repository;
        }
    }
}

