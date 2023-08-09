package momo2x.orarunner.config;

public class AppProperties {

    private String driverClassName;
    private String urlFormat;
    private String host;
    private String port;
    private String sid;
    private String username;
    private String password;

    private AppProperties() {
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrlFormat() {
        return urlFormat;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getSid() {
        return sid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final AppProperties subject;

        private Builder() {
            this.subject = new AppProperties();
        }

        public Builder driverClassName(final String driverClassName) {
            this.subject.driverClassName = driverClassName;
            return this;
        }

        public Builder urlFormat(final String urlFormat) {
            this.subject.urlFormat = urlFormat;
            return this;
        }

        public Builder host(final String host) {
            this.subject.host = host;
            return this;
        }

        public Builder port(final String port) {
            this.subject.port = port;
            return this;
        }

        public Builder sid(final String sid) {
            this.subject.sid = sid;
            return this;
        }

        public Builder username(final String username) {
            this.subject.username = username;
            return this;
        }

        public Builder password(final String password) {
            this.subject.password = password;
            return this;
        }

        public AppProperties build() {
            return this.subject;
        }

    }
}
