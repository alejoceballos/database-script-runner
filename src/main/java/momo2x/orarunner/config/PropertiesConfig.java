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

            final String driverClassName = prefix + "db.driverClassName";
            final String urlFormat = prefix + "db.urlFormat";
            final String hostname = prefix + "db.hostname";
            final String port = prefix + "db.port";
            final String sid = prefix + "db.sid";
            final String username = prefix + "db.username";
            final String password = prefix + "db.password";

            final AppProperties appProperties = AppProperties.builder()
                    .driverClassName(properties.getProperty(driverClassName))
                    .urlFormat(properties.getProperty(urlFormat))
                    .host(properties.getProperty(hostname))
                    .port(properties.getProperty(port))
                    .sid(properties.getProperty(sid))
                    .username(properties.getProperty(username))
                    .password(properties.getProperty(password))
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
