package momo2x.orarunner.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class PropertiesConfig {

    private static final Logger LOGGER = getLogger(PropertiesConfig.class);

    private static final String PROPERTY_LOG_MSG = "  >> {} = {}";

    private final AppArguments arguments;

    public PropertiesConfig(@Lazy final AppArguments arguments) {
        this.arguments = arguments;
    }

    @Bean
    @Lazy
    public AppProperties properties() {
        LOGGER.info(">> Loading properties file");

        try (final InputStream input = newInputStream(get(this.arguments.getAppPropertiesPath()))) {
            final Properties properties = new Properties();
            properties.load(input);

            LOGGER.info("<< Loaded {} properties from file", properties.size());
            LOGGER.info(">> Storing database properties");

            final String prefix = StringUtils.isNotBlank(this.arguments.getDbPropertiesPrefix())
                    ? this.arguments.getDbPropertiesPrefix() + "."
                    : "";

            final String driverClassNameProp = prefix + "db.driverClassName";
            final String driverClassName = properties.getProperty(driverClassNameProp, "oracle.jdbc.driver.OracleDriver");
            LOGGER.info(PROPERTY_LOG_MSG, driverClassNameProp, driverClassName);

            final String urlFormatProp = prefix + "db.urlFormat";
            final String urlFormat = properties.getProperty(urlFormatProp, "jdbc:oracle:thin:@%s:%s:%s");
            LOGGER.info(PROPERTY_LOG_MSG, urlFormatProp, urlFormat);

            final String hostnameProp = prefix + "db.hostname";
            final String hostname = properties.getProperty(hostnameProp);
            LOGGER.info(PROPERTY_LOG_MSG, hostnameProp, hostname);

            final String portProp = prefix + "db.port";
            final String port = properties.getProperty(portProp);
            LOGGER.info(PROPERTY_LOG_MSG, portProp, port);

            final String sidProp = prefix + "db.sid";
            final String sid = properties.getProperty(sidProp);
            LOGGER.info(PROPERTY_LOG_MSG, sidProp, sid);

            final String usernameProp = prefix + "db.username";
            final String username = properties.getProperty(usernameProp);
            LOGGER.info(PROPERTY_LOG_MSG, usernameProp, username);

            final String passwordProp = prefix + "db.password";
            final String password = properties.getProperty(passwordProp);
            LOGGER.info(PROPERTY_LOG_MSG, passwordProp, password);


            final AppProperties appProperties = AppProperties.builder()
                    .driverClassName(driverClassName)
                    .urlFormat(urlFormat)
                    .host(hostname)
                    .port(port)
                    .sid(sid)
                    .username(username)
                    .password(password)
                    .build();

            LOGGER.info("<< Database properties stored");

            return appProperties;

        } catch (final IOException e) {
            throw new RuntimeException(
                    format("Unable to read properties file: %s",
                            this.arguments.getAppPropertiesPath()), e);
        }
    }

}
