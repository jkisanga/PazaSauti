package com.tanzania.jkisanga.pazasauti.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 10/3/2017.
 */

public class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }
}
