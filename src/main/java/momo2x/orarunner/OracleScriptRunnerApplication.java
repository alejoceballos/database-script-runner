package momo2x.orarunner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class OracleScriptRunnerApplication {
    public static void main(final String... args) {
        final boolean hasNoArgument = Arrays
                .stream(args)
                .noneMatch(argument ->
                        argument.contains("--app.properties.path=")
                                || argument.equals("--db.scripts.paths="));

        System.out.println("\nDatabase script runner application parameters:\n");

        if (hasNoArgument) {
            System.out.println("Mandatory:\n"
                            + "  --app.properties.path : Path to database properties file with database connection.\n"
                            + "  --db.scripts.paths    : Comma separated database script paths.\n"
                            + "\n"
                            + "Optional:\n"
                            + "  --db.properties.prefix: (default) empty. Prefix in case of a custom properties file. The rest "
                            + "                          of the property name must remain according.\n"
                            + "                          Properties to be used by the script runner are:\n"
                            + "                            - <prefix.>db.driverClassName (e.g. oracle.jdbc.driver.OracleDriver)\n"
                            + "                            - <prefix.>db.urlFormat (e.g. jdbc:oracle:thin:@%s:%s:%s)\n"
                            + "                            - <prefix.>db.hostname (e.g. localhost)\n"
                            + "                            - <prefix.>db.port (e.g. 1521)\n"
                            + "                            - <prefix.>db.sid (e.g. XE)\n"
                            + "                            - <prefix.>db.username (e.g. sys as sysdba)\n"
                            + "                            - <prefix.>db.password (e.g. mysecurepassword)\n"
                            + "  --db.strategy         : Defines the Java strategy solution to run the database scripts.\n"
                            + "                          Possible values: (default) 'dbstatements' or 'dbpopulator'\n"
                            + "\n\n"
                            + "Example\n"
                            + "\n"
                            + "    java -jar oracle-script-runner \\ \n"
                            + "    --app.properties.path=<path-to-properties-file> \\ \n"
                            + "    --db.scripts.paths=<path-to-script-1>,<path-to-script-2>,<path-to-script-3> \\ \n"
                            + "    --db.strategy=dbpopulator\n"
            );
            System.exit(1);
        }

        Arrays
                .stream(args)
                .forEach(System.out::println);
        System.out.println();

        run(OracleScriptRunnerApplication.class, args);
    }
}
