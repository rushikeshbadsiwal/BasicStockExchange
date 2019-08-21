package Server;

import Servlet.DispatcherServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class JettyServer {

    private ServletHandler servletHandler;
    private Server server;
    private ServerConnector serverConnector;
    private DispatcherServlet dispatcherServlet;

    @Inject
    public JettyServer(ServletHandler servletHandler, QueuedThreadPool threadPool, DispatcherServlet dispatcherServlet) {
        this.servletHandler = servletHandler;
        server = new Server(threadPool);
        serverConnector = new ServerConnector(server);
        this.dispatcherServlet = dispatcherServlet;
    }

    public void start() throws Exception {
        serverConnector.setPort(8090);
        server.setConnectors(new Connector[]{serverConnector});
        servletHandler.addServletWithMapping(new ServletHolder(dispatcherServlet), "/*");
        server.setHandler(servletHandler);
        server.start();
    }
}