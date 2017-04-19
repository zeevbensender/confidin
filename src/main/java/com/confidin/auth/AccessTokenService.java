package com.confidin.auth;

import com.confidin.config.FilterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * Created by bensende on 16/04/2017.
 */
public class AccessTokenService {
    private final static Logger LOG = LoggerFactory.getLogger(AccessTokenService.class);
    private FilterHelper filterHelper = new FilterHelper();
    public URL buildAccessTokenUrl(String base, String code, String redirect, String clientId, String clientSecret) {
        String path = String.format("%s?grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s&client_secret=%s",
                base, code, redirect, clientId, clientSecret);
        try {
            return new URL(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to build access token request URL", e);
        }
    }

    public boolean obtainAccessToken(HttpServletRequest req, String accessTokenUri, String clientId, String clientSecret){
        String code = req.getParameter("code");
        if(code == null || code.length() == 0)
            return false;
        URL url = buildAccessTokenUrl(accessTokenUri, code, filterHelper.getRootPage(req), clientId, clientSecret);
        String token = obtainAccessToken(url);
        if(token != null) {
            req.getSession().setAttribute(FilterConfiguration.ACCESS_TOKEN, token);
            LOG.info(">>>>>>>>>>>>>>>>>>>>>> token >>>>>>>>>>>> " + token);
            return true;
        }
        return false;
    }

    public String obtainAccessToken(URL url){
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            LOG.info("###### ENABLED PROTOCOLS ARE: {}", System.getProperty("https.protocols"));
//            todo: remove the next line before production
            LOG.info("###### THE REQUEST IS: {}", url.toString());
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.write("");
            out.flush();
            out.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                LOG.info("Access token request succeeded");
            }
            else{
                LOG.info("Access token request failed with {} code", connection.getResponseCode());
                StringBuilder errBuffer = new StringBuilder();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errBuffer.append(errorLine);
                }
                String message = "Failed to obtain access token: \n" + errBuffer;
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
            return sb.toString();


        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain access token", e);
        }

    }
}
