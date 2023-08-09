# Oracle Docker Container

Reference:
- https://blogs.oracle.com/connect/post/deliver-oracle-database-18c-express-edition-in-containers

Following the steps of the page above, only version 21 was able to run under WSL using:

```shell
docker run --name oraclexe \
-p 1521:1521 \
-p 5500:5500 \
-e ORACLE_PWD=mysecurepassword \
-e ORACLE_CHARACTERSET=AL32UTF8 \
oracle/database:21.3.0-xe
```

## IntelliJ Oracle connection

- Host: localhost
- Port: 1521
- SID: XE
- Driver: Thin
- User: sys as sysdba
- Password: mysecurepassword
- Save: Forever
- URL: jdbc:oracle:thin:@localhost:1521:XE

### Not tested:

```shell
docker run --name <container name> \
-p 1521:1521 \
-p 5500:5500 \
-e ORACLE_PWD=mysecurepassword \
-e ORACLE_CHARACTERSET=AL32UTF8 \
container-registry.oracle.com/database/express:21.3.0-xe
```

