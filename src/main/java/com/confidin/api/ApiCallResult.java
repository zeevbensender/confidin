package com.confidin.api;

import org.springframework.http.HttpStatus;

import java.net.HttpURLConnection;

/**
 * Created by bensende on 23/04/2017.
 */
public class ApiCallResult {
    private int status;
    private String response;
    private String errorMessage;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public boolean hasError(){
        return HttpURLConnection.HTTP_OK == status;
    }
}
