package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import service.AppModule;

public class Main {

    public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(new AppModule());
            injector.getInstance(Bootstrapper.class).start();
        } catch (Exception e) {
            System.out.println("failed to start server " + e);
        }
    }

}
