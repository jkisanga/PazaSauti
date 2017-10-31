package com.tanzania.jkisanga.pazasauti.app;

import com.tanzania.jkisanga.pazasauti.pojo.Attachment;
import com.tanzania.jkisanga.pazasauti.pojo.Comment;
import com.tanzania.jkisanga.pazasauti.pojo.Complain;
import com.tanzania.jkisanga.pazasauti.pojo.Institution;
import com.tanzania.jkisanga.pazasauti.pojo.Like;
import com.tanzania.jkisanga.pazasauti.pojo.Post;
import com.tanzania.jkisanga.pazasauti.pojo.Registration;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Dell on 10/3/2017.
 */

public interface ApiConfig {

    @Multipart
    @POST("upload_image.php")
    Call uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
//
//    @Multipart
//    @POST("upload_multiple_file.php")
//    Call <ServerResponse> uploadMulFile(@Part MultipartBody.Part file);


    @Multipart
    @POST("api/attachments")
    Call <Attachment> uploadMulFile(@Part MultipartBody.Part filePath,
                                    @Part("complain_id") int complain_id);


    @POST("api/complains")
    Call <Complain> postComplains(@Body Complain complain);


//
//    @Multipart
//    @POST("upload_multiple_file.php")
//    Call <ServerResponse> uploadMulFile(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2);

    @GET("api/institutions")
    Call<List<Institution>> getInstitutions();

    @GET("api/comments/{id}")
    Call<List<Comment>> getComments(@Path("id") int id);

    @GET("api/complains")
    Call<List<Post>> getPosts();

 @POST("api/regstrations")
    Call<Registration> postRegistation(@Body Registration registration);

 @POST("api/comments")
    Call<Comment> postComments(@Body Comment comment);

 @POST("api/votes")
    Call<Like> postLiks(@Body Like like);

 @POST("api/complains/{id}/{viewCounter}")
    Call<Complain> postViewCounter(@Path("id") int id, @Path("viewCounter") int viewCounter);

}
