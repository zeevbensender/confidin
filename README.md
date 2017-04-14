# confidin
PoC
1. Before running the server edit the *application.yml* file and set the *clientId* and *clientSecret* properties

2. In order to run the server use the command:
*mvn spring-boot:run -DskipTests=true -Djavax.net.debug=all -Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2*


