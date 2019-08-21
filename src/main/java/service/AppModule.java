package service;

import annotation.Path;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import controller.Controller;
import controller.StockController;
import controller.UserController;
import model.ApiMapObject;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.apache.commons.dbcp.BasicDataSource;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    ServletHandler getServletHandler() {
        return new ServletHandler();
    }

    @Provides
    QueuedThreadPool getQueuedThreadPool() {
        int maxThreads = 20;
        int minThreads = 10;
        int idleTimeout = 100;
        return new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
    }

    @Provides
    @Singleton
    BasicDataSource mysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/databaseConnectionTestDB");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Provides
    Map<String, ApiMapObject> getPathToClassMapping(final StockController stockController,
                                                    final UserController userController) {
        Map<Class, Controller> map = new HashMap<>();
        Map<String, ApiMapObject> processorMap = new HashMap<>();
        map.put(stockController.getClass(), stockController);
        map.put(userController.getClass(), userController);

        Reflections reflections = new Reflections("");
        for (Class controller : reflections.getSubTypesOf(Controller.class)) {
            for (Method method : controller.getMethods()) {
                Path pathAnnotation = (Path) method.getAnnotation(Path.class);
                if (pathAnnotation != null) {
                    System.out.println("entry is made "+pathAnnotation.path()+" "+map.get(controller)+" "+method);
                    processorMap.put(pathAnnotation.path(),new ApiMapObject(map.get(controller),method));
                }
            }
        }
        return processorMap;
    }
}
