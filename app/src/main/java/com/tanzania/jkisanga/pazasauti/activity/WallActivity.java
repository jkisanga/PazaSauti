package com.tanzania.jkisanga.pazasauti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.adapter.InstitutionAdapter;
import com.tanzania.jkisanga.pazasauti.adapter.PostAdapter;
import com.tanzania.jkisanga.pazasauti.app.ApiConfig;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.pojo.Complain;
import com.tanzania.jkisanga.pazasauti.pojo.Institution;
import com.tanzania.jkisanga.pazasauti.pojo.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.LinearLayoutManager.*;

public class WallActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static final String TAG = "WallActivity";
    private RecyclerView recyclerView;
    private LinearLayoutManager lLayout;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;
    private FloatingActionButton addNewPostFab;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                   // startActivity(new Intent(WallActivity.this, InstitutionActivity.class));
                    return true;
                case R.id.navigation_notifications:
                   // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Paza Sauti");
//        toolbar.setLogo(getResources().getDrawable(R.drawable.ic_action_paza));
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addNewPostFab = (FloatingActionButton) findViewById(R.id.addNewPostFab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(WallActivity.this));
        getFromSever();

        addNewPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WallActivity.this, InstitutionActivity.class));
            }
        });

    }

    private void getFromSever() {
        try {
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            // Call<ServerResponse> call = getResponse.uploadMulFile(fileToUpload1, fileToUpload2);

            Call<List<Post>> call = getResponse.getPosts();
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


                    posts   = response.body();

                    postAdapter = new PostAdapter(posts,WallActivity.this);
                    Log.d(TAG, "onFailure: " + response.body());
                    recyclerView.setAdapter(postAdapter);

//                    }else{
//                        Log.d(TAG, "onFailure1: " + response.toString());
//                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }


            });
        }catch (Exception e){

        }
    }

}
