package com.tanzania.jkisanga.pazasauti.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.adapter.CommentAdapter;
import com.tanzania.jkisanga.pazasauti.adapter.PostAdapter;
import com.tanzania.jkisanga.pazasauti.app.ApiConfig;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.app.SQLiteHandler;
import com.tanzania.jkisanga.pazasauti.pojo.Comment;
import com.tanzania.jkisanga.pazasauti.pojo.Like;
import com.tanzania.jkisanga.pazasauti.pojo.Post;
import com.tanzania.jkisanga.pazasauti.pojo.Registration;
import com.tanzania.jkisanga.pazasauti.utils.FormatterUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tanzania.jkisanga.pazasauti.app.SQLiteHandler.KEY_NAME;

public class PostDetailActivity extends BaseActivity {
    public static final String POST_ID_EXTRA_KEY = "PostDetailsActivity.POST_ID_EXTRA_KEY";
    public static final String AUTHOR_ANIMATION_NEEDED_EXTRA_KEY = "PostDetailsActivity.AUTHOR_ANIMATION_NEEDED_EXTRA_KEY";
    private static final int TIME_OUT_LOADING_COMMENTS = 30000;
    public static final int UPDATE_POST_REQUEST = 1;
    public static final String POST_STATUS_EXTRA_KEY = "PostDetailsActivity.POST_STATUS_EXTRA_KEY";
    public static final String TAG = "PostDetailActivity";

    private EditText commentEditText;
    private int complainId;
    @Nullable
    private Post post;
    private ScrollView scrollView;
    private ViewGroup likesContainer;
    private ImageView likesImageView;
    private TextView commentsLabel;
    private TextView likeCounterTextView;
    private TextView commentsCountTextView;
    private TextView watcherCounterTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private ImageView authorImageView;
    private ProgressBar progressBar;
    private ImageView postImageView;
    private TextView titleTextView;
    private TextView descriptionEditText;
    private ProgressBar commentsProgressBar;
    private LinearLayout commentsContainer;
    private TextView warningCommentsTextView;
    private RecyclerView recyclerView;
    private List<Comment>  comments = new ArrayList<>();
    Button sendButton;

    private boolean attemptToLoadComments = false;
   // private CommentsAdapter commentsAdapter;

    private MenuItem complainActionMenuItem;
    private MenuItem editActionMenuItem;
    private MenuItem deleteActionMenuItem;

    private String postId;

   // private PostManager postManager;
    //private ProfileManager profileManager;
   // private LikeController likeController;
    private boolean postRemovingProcess = false;
    private boolean isPostExist;

    private boolean isAuthorAnimationRequired;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionEditText = (TextView) findViewById(R.id.descriptionEditText);
        postImageView = (ImageView) findViewById(R.id.postImageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        commentsContainer = (LinearLayout) findViewById(R.id.commentsContainer);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        commentsLabel = (TextView) findViewById(R.id.commentsLabel);
        commentEditText = (EditText) findViewById(R.id.commentEditText);
        likesContainer = (ViewGroup) findViewById(R.id.likesContainer);
        likesImageView = (ImageView) findViewById(R.id.likesImageView);
        authorImageView = (ImageView) findViewById(R.id.authorImageView);
        authorTextView = (TextView) findViewById(R.id.authorTextView);
        likeCounterTextView = (TextView) findViewById(R.id.likeCounterTextView);
        commentsCountTextView = (TextView) findViewById(R.id.commentsCountTextView);
        watcherCounterTextView = (TextView) findViewById(R.id.watcherCounterTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        commentsProgressBar = (ProgressBar) findViewById(R.id.commentsProgressBar);
        warningCommentsTextView = (TextView) findViewById(R.id.warningCommentsTextView);
         sendButton = (Button) findViewById(R.id.sendButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            final CharSequence date = FormatterUtil.getRelativeTimeSpanStringShort(PostDetailActivity.this,AppConfig.DATE_CREATED);

            Picasso.with(this).load(AppConfig.BASE_URL +"api/document/" + bundle.getString(AppConfig.IMAGE)).into(postImageView);
            descriptionEditText.setText(bundle.getString(AppConfig.DESC));
            authorTextView.setText(bundle.getString(AppConfig.NAME));
            likeCounterTextView.setText(Integer.toString(bundle.getInt(AppConfig.LIKE)));
            watcherCounterTextView.setText(Integer.toString(bundle.getInt(AppConfig.VIEWS)));
            dateTextView.setText(date);
            complainId = bundle.getInt(AppConfig.COMPLAIN_ID);
            toolbar.setTitle(bundle.getString(AppConfig.INSTITUTION));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        getFromSever(complainId);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isAuthorAnimationRequired) {
            authorImageView.setScaleX(0);
            authorImageView.setScaleY(0);


        }




        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendButton.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              postComment(commentEditText.getText().toString().trim());
               // Log.d(TAG, "onClick: dfsssssssssssssssss" + commentTxt);
            }
        });


        likesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLike(complainId,1);
            }
        });


    }

    private void postComment(String commentTxt) {

        progressDialog.show();

        Comment comment = new Comment();
        comment.setPhoneId(Build.SERIAL);
        comment.setComment(commentTxt);
        comment.setUser(db.getUserDetails().get(KEY_NAME));
        comment.setComplainId(complainId);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<Comment> call = getResponse.postComments(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Comment serverResponse = response.body();
                if(response.isSuccessful() && serverResponse != null){
                    Toast.makeText(PostDetailActivity.this, "comment imetumwa", Toast.LENGTH_SHORT).show();
                    commentEditText.setText(null);
                    commentEditText.clearFocus();
                    hideKeyBoard();
                    finish();
                    startActivity(getIntent());

                }else {
                    assert serverResponse != null;
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d(TAG, "onClick: dfsssssssssssssssss");
            }
        });
    }






    private void getFromSever(int complaintId) {
        try {
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            // Call<ServerResponse> call = getResponse.uploadMulFile(fileToUpload1, fileToUpload2);

            Call<List<Comment>> call = getResponse.getComments(complaintId);
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {


                    comments   = response.body();

                    if (comments.size() > 0) {
                        //commentsLabel.setVisibility(View.GONE);
                        commentsProgressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else if (commentsLabel.getVisibility() != View.VISIBLE) {
                        commentsLabel.setVisibility(View.VISIBLE);
                    }
                    commentsCountTextView.setText(Integer.toString(comments.size()));
                    CommentAdapter commentAdapter = new CommentAdapter(comments,PostDetailActivity.this);

                    recyclerView.setAdapter(commentAdapter);

                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }


            });
        }catch (Exception e){

        }
    }


    private void postLike(int complainId, int vote) {

        progressDialog.show();

        Like like = new Like();
        like.setPhoneId(Build.SERIAL);
        like.setComplainId(complainId);
        like.setVote(vote);


        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<Like> call = getResponse.postLiks(like);
        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                Like serverResponse = response.body();
                if(response.isSuccessful() && serverResponse != null){
                    likeCounterTextView.setText(Integer.toString(serverResponse.getVote() + 1));
                    Toast.makeText(PostDetailActivity.this, "liked", Toast.LENGTH_SHORT).show();
                    likesImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_active));
                    finish();
                    startActivity(getIntent());
                    progressDialog.dismiss();

                }else {
                    assert serverResponse != null;
                }


            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


}
