package basicStockExchange;

import annotation.Path;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import controller.Controller;
import controller.StockController;
import controller.UserController;
import model.ApiMapObject;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.apache.commons.dbcp.BasicDataSource;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        PropertiesService propertiesService = new PropertiesService();
        Names.bindProperties(binder(), propertiesService.properties);
        bind(PropertiesService.class).toInstance(propertiesService);
    }

    public static class PropertiesService {
        private final Properties properties;

        PropertiesService() {
            properties = loadConfig();
        }

        private static Properties loadConfig() {
            try {
                Properties properties = new Properties();
                Configuration config = new PropertiesConfiguration("settings.properties");
                Iterator<String> keys = config.getKeys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    properties.put(key, config.getString(key));
                }

                return properties;
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        public Properties properties() {
            return properties;
        }
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
                    processorMap.put(pathAnnotation.path(), new ApiMapObject(map.get(controller), method));
                }
            }
        }
        return processorMap;
    }

    @Provides
    ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(10);
    }
}