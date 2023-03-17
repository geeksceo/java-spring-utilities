## Utilities

For cli is compiled to use Java 8 it will throw the following error when the default Java-version is 10 (or higher):
```
Exception in thread "main" java.lang.NoClassDefFoundError: javax/xml/bind/DatatypeConverter
        at org.utplsql.api.reporter.Reporter.setAttributes(Reporter.java:80)
        at org.utplsql.api.reporter.Reporter.<init>(Reporter.java:29)
        at org.utplsql.api.reporter.DefaultReporter.<init>(DefaultReporter.java:15)
        at org.utplsql.api.reporter.ReporterFactory.createReporter(ReporterFactory.java:94)
        at org.utplsql.api.reporter.ReporterFactory.create(ReporterFactory.java:122)
        at oracle.jdbc.driver.Accessor.getORAData(Accessor.java:944)
        at oracle.jdbc.driver.OracleCallableStatement.getORAData(OracleCallableStatement.java:1864)
        at oracle.jdbc.driver.OracleCallableStatementWrapper.getORAData(OracleCallableStatementWrapper.java:789)
        at org.utplsql.api.reporter.Reporter.initDbReporter(Reporter.java:73)
        at org.utplsql.api.reporter.Reporter.init(Reporter.java:46)
        at org.utplsql.cli.ReporterManager.initReporters(ReporterManager.java:68)
        at org.utplsql.cli.RunCommand.run(RunCommand.java:158)
        at org.utplsql.cli.Cli.main(Cli.java:33)
Caused by: java.lang.ClassNotFoundException: javax.xml.bind.DatatypeConverter
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(Unknown Source)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(Unknown Source)
        at java.base/java.lang.ClassLoader.loadClass(Unknown Source)
        ... 13 more
```
Solution
```xml
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.9.1</version>
</dependency>
<dependency>
    <groupId>javax.xml.bind</groupId>
	<artifactId>jaxb-api</artifactId>
	<version>2.3.0</version>
</dependency>
```
