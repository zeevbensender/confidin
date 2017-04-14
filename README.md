# confidin
PoC
Before running the server edit the *application.yml* file and set the *clientId* and *clientSecret* properties
In order to run the server use the command:
mvn spring-boot:run -DskipTests=true -Djavax.net.debug=all -Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2
