package com.tanzania.jkisanga.pazasauti.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 10/3/2017.
 */

public class Attachment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("complain_id")
    @Expose
    private Integer complainId;
    @SerializedName("file_path")
    @Expose
    private String filePath;


    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComplainId() {
        return complainId;
    }

    public void setComplainId(Integer complainId) {
        this.complainId = complainId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

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
}
