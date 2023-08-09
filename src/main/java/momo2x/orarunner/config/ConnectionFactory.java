package momo2x.orarunner.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

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
            Class.forName(this.driverClassName);
            return DriverManager.getConnection(this.url, this.username, this.password);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
