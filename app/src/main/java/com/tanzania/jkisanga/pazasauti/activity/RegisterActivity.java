package com.tanzania.jkisanga.pazasauti.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.app.ApiConfig;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.app.SQLiteHandler;
import com.tanzania.jkisanga.pazasauti.app.SessionManager;
import com.tanzania.jkisanga.pazasauti.pojo.Complain;
import com.tanzania.jkisanga.pazasauti.pojo.Registration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, WallActivity.class);
            startActivity(intent);
            finish();
        }else {
            postRegistration();
        }

    }

    private void postRegistration() {
        progressDialog.show();

        Registration reg = new Registration();
        reg.setName("Joshua Kisanga");
        reg.setPhoneNo("0713991800");
        reg.setPhoneId("345678876523456786");
        reg.setToken("456666387837483783478374");

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<Registration> call = getResponse.postRegistation(reg);
        call.enqueue(new Callback<Registration>() {
            @Override
            public void onResponse(Call<Registration> call, Response<Registration> response) {
                Registration serverResponse = response.body();
                if(response.isSuccessful() && serverResponse != null){
                    Toast.makeText(RegisterActivity.this, "Server response", Toast.LENGTH_SHORT).show();
                    db.addUser(serverResponse.getId(), serverResponse.getName(), serverResponse.getPhoneNo(), serverResponse.getPhoneId());

                    session.setLogin(true);
                    Intent intent = new Intent(RegisterActivity.this,
                            WallActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    assert serverResponse != null;
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t) {

            }
        });
    }


}
