package com.tanzania.jkisanga.pazasauti.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.app.ApiConfig;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.app.ServerResponse;
import com.tanzania.jkisanga.pazasauti.pojo.Attachment;
import com.tanzania.jkisanga.pazasauti.pojo.Complain;
import com.tanzania.jkisanga.pazasauti.pojo.ImageData;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.adapters.GalleryImagesAdapter;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;
import com.vlk.multimager.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cyd.awesome.material.AwesomeButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickerActivity extends AppCompatActivity {

    public static final String TAG = "PickerActivity";
    RecyclerView recyclerView;
    LinearLayout parentLayout;
    int selectedColor;
    int institutionId;
    String institutionName, phoneNumbe;
    AwesomeButton multiCaptureButton;
    AwesomeButton multiPickerButton;
    AwesomeButton btnCall, btnSms;
    ImageView callImageView;
    ImageView messageImageView;
    ArrayList<Image> imagesList = new ArrayList<>();
    ProgressDialog progressDialog;
    EditText txtUjumbe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
        btnCall = (AwesomeButton) findViewById(R.id.phone_call);
        progressDialog.setMessage("uploading...");
    Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            institutionId = bundle.getInt(AppConfig.ID);
            //institutionId = 2;
            institutionName = bundle.getString(AppConfig.NAME);
            phoneNumbe = bundle.getString(AppConfig.PHONE);
            toolbar.setTitle(institutionName);
        }
        toolbar.setLogo(getResources().getDrawable(R.drawable.ic_action_paza));
        setSupportActionBar(toolbar);

        btnSms = (AwesomeButton) findViewById(R.id.message);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        multiCaptureButton = (AwesomeButton) findViewById(R.id.multiCaptureButton);
        multiPickerButton = (AwesomeButton) findViewById(R.id.multiPickerButton);
        AwesomeButton b = (AwesomeButton) findViewById(R.id.btn_send);
        //callImageView = (ImageView) findViewById(R.id.call);
       // messageImageView = (ImageView) findViewById(R.id.message);
        //contactUsTextView = (TextView) findViewById(R.id.contact_us);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComplain();
            }
        });



        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtUjumbe = (EditText) findViewById(R.id.ujumbe);
//
//
//                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//                smsIntent.setType("vnd.android-dir/mms-sms");
//                smsIntent.putExtra("address", phoneNumbe);
//                smsIntent.putExtra("sms_body",txtUjumbe.getText().toString());
//                startActivity(smsIntent);

                Intent intent = new Intent(PickerActivity.this, RegisterActivity.class);
                startActivity(intent);



            }
        });



        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ phoneNumbe));
                startActivity(intent);
            }
        });
        multiCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.hasCameraHardware(PickerActivity.this)){
                    initiateMultiCapture();
                }
                else
                    Utils.showLongSnack(parentLayout, "Sorry. Your device does not have a camera.");
            }
        });

        multiPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateMultiPicker();

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case AppConfig.TYPE_MULTI_CAPTURE:
                handleResponseIntent(intent);

                break;
            case AppConfig.TYPE_MULTI_PICKER:
                handleResponseIntent(intent);
                break;
        }
    }

    private void handleResponseIntent(Intent intent) {
       imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getColumnCount(), GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(mLayoutManager);
        GalleryImagesAdapter imageAdapter = new GalleryImagesAdapter(this, imagesList, getColumnCount(), new Params());
        recyclerView.setAdapter(imageAdapter);

    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float thumbnailDpWidth = getResources().getDimension(R.dimen.thumbnail_width) / displayMetrics.density;
        return (int) (dpWidth / thumbnailDpWidth);
    }



    private void initiateMultiCapture() {
        Intent intent = new Intent(this, MultiCameraActivity.class);
        Params params = new Params();
        params.setCaptureLimit(10);
        params.setToolbarColor(selectedColor);
        params.setActionButtonColor(selectedColor);
        params.setButtonTextColor(selectedColor);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_CAPTURE);
    }

    private void initiateMultiPicker() {
        Intent intent = new Intent(this, GalleryActivity.class);
        Params params = new Params();
        params.setCaptureLimit(10);
        params.setPickerLimit(10);
        params.setToolbarColor(selectedColor);
        params.setActionButtonColor(selectedColor);
        params.setButtonTextColor(selectedColor);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
    }

   // Uploading Image/Video
    private void uploadMultipleFiles(int complain) {

        for (int i = 0; i < imagesList.size(); i++) {


            File file = new File(imagesList.get(i).imagePath);

            progressDialog.show();


            // Parsing any Media type file
            final RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);

            MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file_path", file.getName(), requestBody1);

            Log.d(TAG, "uploadMultipleFiles: " + complain);

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<Attachment> call = getResponse.uploadMulFile(fileToUpload1,complain);
            call.enqueue(new Callback<Attachment>() {
                @Override
                public void onResponse(Call<Attachment> call, Response<Attachment> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Attachment serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.isSuccess()) {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        assert serverResponse != null;
                        Log.v("Response", serverResponse.toString());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Attachment> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }
            });
        }

    }



    // Uploading Image/Video
    private void uploadComplain() {
        txtUjumbe = (EditText) findViewById(R.id.ujumbe);
            progressDialog.show();

            Complain complain = new Complain();
            complain.setDesc(txtUjumbe.getText().toString().trim());
            complain.setInstitutionId(institutionId);

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<Complain> call = getResponse.postComplains(complain);
            call.enqueue(new Callback<Complain>() {
                @Override
                public void onResponse(Call<Complain> call, Response<Complain> response) {
                    Complain serverResponse = response.body();
                    if(response.isSuccessful() && serverResponse != null){

                        uploadMultipleFiles(serverResponse.getId());
                    }else {
                        assert serverResponse != null;
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Complain> call, Throwable t) {

                }
            });
        }


}
