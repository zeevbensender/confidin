package com.confidin.api;

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
import java.util.zip.GZIPInputStream;

/**
 * Created by bensende on 23/04/2017.
 */
public class LinkedinClient {
    private final static Logger LOG = LoggerFactory.getLogger(LinkedinClient.class);

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        LOG.trace("###### THE REQUEST IS: {}", url.toString());
        return connection;
    }

    public ApiCallResult invokeApi(URL url, String apiName) throws IOException {
        HttpURLConnection connection = getConnection(url);
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.write("");
        out.flush();
        out.close();
        ApiCallResult result = new ApiCallResult();
        result.setStatus(connection.getResponseCode());
        if (result.getStatus() == HttpURLConnection.HTTP_OK) {
            LOG.info("{} request succeeded", apiName);
        }
        else{
            LOG.info("{} request failed with {} code", apiName, result.getStatus());
            StringBuilder errBuffer = new StringBuilder();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errBuffer.append(errorLine);
            }
            result.setErrorMessage(errBuffer.toString());
            String message = String.format("{} request failed : \n{}", apiName, result.getErrorMessage());
//            todo: consider to move this validation to caller
            if(errBuffer.indexOf("authorization code expired") >= 0)
                return null;
            LOG.error(message);
        }
        String respEnc = connection.getContentEncoding();
        InputStream is;
        if (respEnc != null && respEnc.equalsIgnoreCase("gzip")) {
            is = new GZIPInputStream(connection.getInputStream());
        } else {
            is = new BufferedInputStream(connection.getInputStream());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String li;
        StringBuilder sb = new StringBuilder();
        while ((li = reader.readLine()) != null) {
            sb.append(li);
        }
        result.setResponse(sb.toString());
        return result;
    }
}
