package com.tanzania.jkisanga.pazasauti.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.adapter.InstitutionAdapter;
import com.tanzania.jkisanga.pazasauti.app.ApiConfig;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.app.ServerResponse;
import com.tanzania.jkisanga.pazasauti.pojo.Institution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InstitutionActivity extends AppCompatActivity {

    public static final String TAG = "InstitutionActivity";
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private List<Institution> institutions = new ArrayList<>();
    private InstitutionAdapter institutionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,getColumnCount()));
        getFromSever();
    }

    private void getFromSever() {

        try {
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
           // Call<ServerResponse> call = getResponse.uploadMulFile(fileToUpload1, fileToUpload2);

            Call<List<Institution>> call = getResponse.getInstitutions();
            call.enqueue(new Callback<List<Institution>>() {
                @Override
                public void onResponse(Call<List<Institution>> call, Response<List<Institution>> response) {

                   // if(response.isSuccessful()) {
                        //channelAdapter = new ChannelAdapter(rChannels,getActivity());
                    List<Institution>   institutions = response.body();

                        institutionAdapter = new InstitutionAdapter(institutions,InstitutionActivity.this);
                        Log.d(TAG, "onFailure: " + response.body());
                        recyclerView.setAdapter(institutionAdapter);

//                    }else{
//                        Log.d(TAG, "onFailure1: " + response.toString());
//                    }
                }

                @Override
                public void onFailure(Call<List<Institution>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }


            });
        }catch (Exception e){

        }

    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float thumbnailDpWidth = getResources().getDimension(R.dimen.thumbnail_width) / displayMetrics.density;
        return (int) (dpWidth / thumbnailDpWidth);
    }

}
