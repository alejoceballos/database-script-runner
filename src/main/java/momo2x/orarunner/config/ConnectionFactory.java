package momo2x.orarunner.config;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class ConnectionFactory {

    private static final Logger LOGGER = getLogger(ConnectionFactory.class);

    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;

    public ConnectionFactory(
            final String url,
            final String username,
            final String password,
            final String driverClassName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    public Connection createConnection() throws SQLException {
        try {
            LOGGER.info(">> Loading {}", this.driverClassName);

            Class.forName(this.driverClassName);

            LOGGER.info("<< {} loaded", this.driverClassName);
            LOGGER.info(">> Getting connection from driver manager [username: {}, password: {}, url: {}]",
                    this.username,
                    this.password,
                    this.url);

            final Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            LOGGER.info("<< Connection from driver manager retrieved");

            return connection;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
