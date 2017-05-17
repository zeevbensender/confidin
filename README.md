# confidin

This web application utilizes the [OAuth 2.0 protocol](https://oauth.net/2/ "OAuth 2.0 protocol") for authentication with Linkedin 

1. Before running the application make sure that *clientId* and *clientSecret* are either defined as environment variables or
passed as JVM options (*-DclientSecret=... -DclientId=...*)

2. In order to run the server use the command:
*mvn spring-boot:run -DskipTests=true*

For network traffic debug use this:
*mvn spring-boot:run -DskipTests=true -Djavax.net.debug=all -Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2*

3. In order to test the server, navigate with your browser to this URL: *http://localhost:8080*

4. The application can be accessed at *http://ec2-34-208-3-27.us-west-2.compute.amazonaws.com:8080*

