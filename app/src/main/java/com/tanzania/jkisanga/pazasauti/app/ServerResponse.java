package com.tanzania.jkisanga.pazasauti.app;

import com.google.gson.annotations.Expose;
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

    @SerializedName("complain_id")
    private int complainId;

    @SerializedName("file_path")
    private String filePath;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

    public boolean isSuccess() {
       // 46937
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
