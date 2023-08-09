package momo2x.orarunner.runner;

import momo2x.orarunner.config.AppArguments;
import momo2x.orarunner.config.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileUrlResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute;

@Component
public class DbScriptRunner {

    private static final Logger LOGGER = getLogger(DbScriptRunner.class);

    private final String dbStrategy;
    private final DataSource dataSource;
    private final ConnectionFactory connectionFactory;
    private final AppArguments arguments;

    public DbScriptRunner(
            @Value("${db.strategy:dbstatements}") final String dbStrategy,
            @Lazy final DataSource dataSource,
            @Lazy final ConnectionFactory connectionFactory,
            @Lazy final AppArguments arguments) {
        this.dbStrategy = dbStrategy;
        this.dataSource = dataSource;
        this.connectionFactory = connectionFactory;
        this.arguments = arguments;
    }

    @PostConstruct
    private void runScripts() {
        LOGGER.info(">> Preparing to run scripts. Strategy: {}", this.dbStrategy);

        if ("dbstatements".equals(this.dbStrategy)) {
            this.runUsingJdbConnectionAndStatements();

        } else if ("dbpopulator".equals(this.dbStrategy)) {
            this.runUsingDatabasePopulator();

        } else {
            throw new RuntimeException(format("Database running strategy %s is not known", this.dbStrategy));
        }
    }

    private void runUsingDatabasePopulator() {
        final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);

        LOGGER.info(">> Preparing scripts to populate database");

        this.transformPathToUrlResources().forEach(databasePopulator::addScript);

        LOGGER.info("<< Scripts ready to populate database");

        LOGGER.info(">> Executing database scripts");

        execute(databasePopulator, this.dataSource);

        LOGGER.info("<< Database scripts executed");
    }

    private void runUsingJdbConnectionAndStatements() {
        Connection conn = null;

        try {
            LOGGER.info(">> Creating connection");

            conn = this.connectionFactory.createConnection();

            LOGGER.info("<< Connection created");

            LOGGER.info(">> Executing SQL statements");

            try (final Statement stmt = conn.createStatement()) {
                this.extractSqlStatements().forEach(sql -> {
                    try {
                        LOGGER.info(">> Executing SQL statement: {}", sql);

                        stmt.execute(sql);

                        LOGGER.info("<< SQL statement executed");
                    } catch (final SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            LOGGER.info("<< Finished executing statements");

        } catch (final SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    LOGGER.info(">> Closing connection");

                    conn.close();

                    LOGGER.info("<< Connection closed");
                }
            } catch (final SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private Stream<FileUrlResource> transformPathToUrlResources() {
        final Function<String, FileUrlResource> toFileUrlResource = path -> {
            try {
                LOGGER.info(">> Transforming file {} into URL Resource", path);

                final FileUrlResource fileUrlResource = new FileUrlResource(path);

                LOGGER.info("<< Finished transforming file {}", path);

                return fileUrlResource;
            } catch (final MalformedURLException e) {
                throw new RuntimeException(format("Couldn't find database script: %s", path), e);
            }
        };

        return this.arguments.getDbScriptPaths().stream().map(toFileUrlResource);
    }

    private Stream<String> extractSqlStatements() {
        final Function<String, Stream<String>> toSqlStatements = path -> {
            try {
                LOGGER.info(">> Extracting statement from file {}", path);

                final String content = new String(Files.readAllBytes(Paths.get(path)));

                LOGGER.info("<< Finished extracting statements from file {}", path);

                return Stream.of(content.split(";"));
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        };

        return this.arguments
                .getDbScriptPaths()
                .stream()
                .flatMap(toSqlStatements)
                .filter(StringUtils::isNotBlank);
    }
}
