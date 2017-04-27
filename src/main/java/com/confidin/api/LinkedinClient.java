package com.confidin.api;

import com.confidin.auth.AccessToken;
import com.confidin.config.FilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
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
import java.util.zip.GZIPInputStream;

/**
 * Created by bensende on 23/04/2017.
 */
public class LinkedinClient {
    private final static Logger LOG = LoggerFactory.getLogger(LinkedinClient.class);

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = null;
        HttpSession session = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        if(session != null && session.getAttribute(FilterConfiguration.ACCESS_TOKEN) != null){
            AccessToken token = (AccessToken) session.getAttribute(FilterConfiguration.ACCESS_TOKEN);
            String surl = url.toString();
            url = new URL(surl + "&oauth2_access_token=" + token.getValue());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
        }else {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
        }
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        LOG.trace("###### THE REQUEST IS: {}", url.toString());
        return connection;
    }

    public ApiCallResult obtainToken(String request, String apiName) throws IOException {
        HttpURLConnection connection = getConnection(new URL(request));
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

    public ApiCallResult sendGet(String request, String apiName) throws IOException {
        HttpSession session = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getSession(true);
        URL url = new URL(request +
                Optional.ofNullable(session.getAttribute(FilterConfiguration.ACCESS_TOKEN))
                        .map(t -> "&oauth2_access_token=" + ((AccessToken)t).getValue())
                        .orElse(""));
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
