# confidin

This web application utilizes the [OAuth 2.0 protocol](https://oauth.net/2/ "OAuth 2.0 protocol") for authentication with Linkedin 

1. Before running the server edit the *application.yml* file and set the *clientId* and *clientSecret* properties

2. In order to run the server use the command:
*mvn spring-boot:run -DskipTests=true -Djavax.net.debug=all -Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2*

3. In order to test the server, navigate with your browser to this URL: *http://localhost:8080*

