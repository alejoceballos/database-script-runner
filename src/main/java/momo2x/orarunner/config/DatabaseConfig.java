package momo2x.orarunner.config;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.jdbc.DataSourceBuilder.create;

@Configuration
public class DatabaseConfig {

    private static final Logger LOGGER = getLogger(DatabaseConfig.class);

    private final AppProperties properties;

    public DatabaseConfig(@Lazy final AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Lazy
    public DataSource dataSource() {
        LOGGER.info(">> Creating datasource");

        final DataSource dataSource = create()
                .driverClassName(this.properties.getDriverClassName())
                .url(this.generateUrl())
                .username(this.properties.getUsername())
                .password(this.properties.getPassword())
                .build();

        LOGGER.info("<< Datasource created");

        return dataSource;
    }

    @Bean
    @Lazy
    public ConnectionFactory connectionFactory() {
        LOGGER.info(">> Creating connection factory");

        final ConnectionFactory connectionFactory = new ConnectionFactory(
                this.generateUrl(),
                this.properties.getUsername(),
                this.properties.getPassword(),
                this.properties.getDriverClassName());

        LOGGER.info("<< Connection factory created");

        return connectionFactory;
    }

    private String generateUrl() {
        return format(
                this.properties.getUrlFormat(),
                this.properties.getHost(),
                this.properties.getPort(),
                this.properties.getSid());
    }
}
