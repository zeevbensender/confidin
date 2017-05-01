package com.confidin.api;

import com.confidin.auth.TokenHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by bensende on 23/04/2017.
 */
public class LinkedinClient {
    private final static Logger LOG = LoggerFactory.getLogger(LinkedinClient.class);
    //    todo: spring initialization
    private TokenHolder tokenHolder = new TokenHolder();

    public ApiCallResult obtainToken(String request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(request).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.write("");
        out.flush();
        out.close();
        ApiCallResult result = new ApiCallResult();
        result.setStatus(connection.getResponseCode());
        if (!handleError(result, connection, "obtain token")) {
            LOG.info("{} request succeeded", "obtain token");
        }
        else if(result.getErrorMessage().indexOf("authorization code expired") >= 0){
            return null;
        }
        return readInputStream(connection, result);
    }

    public ApiCallResult sendGet(String request, String apiName) throws IOException {

        URL url = new URL(request + Optional.ofNullable(tokenHolder.getToken()).
                map(tkn -> "&oauth2_access_token=" + tkn).
                orElse(""));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        ApiCallResult result = new ApiCallResult();
        result.setStatus(connection.getResponseCode());
        if (handleError(result, connection, apiName)) {
            LOG.info("{} request succeeded", apiName);
        }
        return readInputStream(connection, result);
    }


    private ApiCallResult readInputStream(HttpURLConnection connection, ApiCallResult result) throws IOException {
        InputStream is = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String li;
        StringBuilder sb = new StringBuilder();
        while ((li = reader.readLine()) != null) {
            sb.append(li);
        }
        result.setResponse(sb.toString());
        return result;
    }

    private boolean handleError(ApiCallResult result , HttpURLConnection connection, String apiName) throws IOException {
        if(result.getStatus() == HttpURLConnection.HTTP_OK)
            return false;
        LOG.info("{} request failed with {} code", apiName, result.getStatus());
        StringBuilder errBuffer = new StringBuilder();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errBuffer.append(errorLine);
        }
        result.setErrorMessage(errBuffer.toString());
        String message = String.format("{} request failed : \n{}", apiName, result.getErrorMessage());
        LOG.error(message);
        return true;
    }

}
