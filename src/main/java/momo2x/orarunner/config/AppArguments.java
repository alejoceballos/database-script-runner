package momo2x.orarunner.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class AppArguments {

    private static final Logger LOGGER = getLogger(AppArguments.class);

    private final String appPropertiesPath;
    private final String dbPropertiesPrefix;
    private final String[] dbScriptPaths;

    public AppArguments(
            @Value("${app.properties.path}") final String appPropertiesPath,
            @Value("${db.properties.prefix}") final String dbPropertiesPrefix,
            @Value("${db.scripts.paths}") final String[] dbScriptsPaths
    ) {
        LOGGER.info(">> Loading properties file");
        LOGGER.info("  >> app.properties.path: {}", appPropertiesPath);
        LOGGER.info("  >> db.properties.prefix: {}", dbPropertiesPrefix);
        LOGGER.info("  >> db.scripts.paths: {}", Arrays.toString(dbScriptsPaths));

        this.appPropertiesPath = appPropertiesPath;
        this.dbPropertiesPrefix = dbPropertiesPrefix;
        this.dbScriptPaths = dbScriptsPaths;
    }

    public String getAppPropertiesPath() {
        return appPropertiesPath;
    }

    public String getDbPropertiesPrefix() {
        return dbPropertiesPrefix;
    }

    public List<String> getDbScriptPaths() {
        return Arrays.asList(this.dbScriptPaths);
    }
}
