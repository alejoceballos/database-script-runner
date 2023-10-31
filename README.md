# Database Script Runner

That's it. A simple Java project to run database scripts. Currently tested using a containerized Oracle database.

#### Database script runner application parameters:

Mandatory:
```
  --app.properties.path : Path to database properties file with database connection.
  --db.scripts.paths    : Comma separated database script paths.
```
Optional:
```
  --db.properties.prefix: (default) empty. Prefix in case of a custom properties file. The rest 
                          of the property name must remain according.
                          Properties to be used by the script runner are:
                            - <prefix.>db.driverClassName (e.g. oracle.jdbc.driver.OracleDriver)
                            - <prefix.>db.urlFormat (e.g. jdbc:oracle:thin:@%s:%s:%s)
                            - <prefix.>db.hostname (e.g. localhost)
                            - <prefix.>db.port (e.g. 1521)
                            - <prefix.>db.sid (e.g. XE)
                            - <prefix.>db.username (e.g. sys as sysdba)
                            - <prefix.>db.password (e.g. mysecurepassword)
  --db.strategy         : Defines the Java strategy solution to run the database scripts.
                          Possible values: (default) 'dbstatements' or 'dbpopulator'
```
Example
```shell
    java -jar oracle-script-runner \
    --app.properties.path=<path-to-properties-file> \
    --db.scripts.paths=<path-to-script-1>,<path-to-script-2>,<path-to-script-3> \
    --db.strategy=dbpopulator"
```
