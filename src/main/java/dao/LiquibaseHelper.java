package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class LiquibaseHelper {
    private final String driver;
    private final String url;
    private final String changeLogFilePath;
    private final String username;
    private final String password;

    @Inject
    public LiquibaseHelper(@Named("db.driver") String driver,
                           @Named("db.url") String url,
                           @Named("db.changelog.path") String changeLogFilePath,
                           @Named("db.username") String username,
                           @Named("db.password") String password) {
        this.driver = driver;
        this.url = url;
        this.changeLogFilePath = changeLogFilePath;
        this.username = username;
        this.password = password;
    }

    public void update() {
        try {
            liquibase.integration.commandline.Main.run(new String[]{"--driver=" + driver,
                    "--changeLogFile=" + changeLogFilePath, "--url=" + url, "--username=" + username, "--password=" + password, "update"});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}