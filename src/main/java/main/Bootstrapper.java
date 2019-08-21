package main;

import Server.JettyServer;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class Bootstrapper {

    @Inject
    private JettyServer jettyServer;

    void start() throws Exception {
        jettyServer.start();
    }
}
