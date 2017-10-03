package com.tanzania.jkisanga.pazasauti.app;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Dell on 10/3/2017.
 */

public interface ApiConfig {

    @Multipart
    @POST("upload_image.php")
    Call uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @Multipart
    @POST("upload_multiple_file.php")
    Call <ServerResponse> uploadMulFile(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2);
}
