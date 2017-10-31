package com.tanzania.jkisanga.pazasauti.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.activity.PickerActivity;
import com.tanzania.jkisanga.pazasauti.activity.PostDetailActivity;
import com.tanzania.jkisanga.pazasauti.app.ApiConfig;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.pojo.Complain;
import com.tanzania.jkisanga.pazasauti.pojo.Institution;
import com.tanzania.jkisanga.pazasauti.pojo.Like;
import com.tanzania.jkisanga.pazasauti.pojo.Post;
import com.tanzania.jkisanga.pazasauti.utils.FormatterUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tanzania.jkisanga.pazasauti.activity.WallActivity.TAG;

/**
 * Created by Dell on 10/22/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> complains;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView detailsTextView, titleTextView, likeCounterTextView, commentsCountTextView, dateTextView, watcherCounterTextView;
        LinearLayout linearLayout, commentsCounterContainer,likesContainer;
        private ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.top_layout);
            likesContainer = (LinearLayout) view.findViewById(R.id.likesContainer);
            detailsTextView = (TextView) view.findViewById(R.id.detailsTextView);
            imageView = (ImageView) view.findViewById(R.id.postImageView);
            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            commentsCounterContainer = (LinearLayout) view.findViewById(R.id.commentsCounterContainer);
            likeCounterTextView = (TextView) view.findViewById(R.id.likeCounterTextView);
            watcherCounterTextView = (TextView) view.findViewById(R.id.watcherCounterTextView);
            commentsCountTextView = (TextView) view.findViewById(R.id.commentsCountTextView);
            dateTextView = (TextView) view.findViewById(R.id.dateTextView);


        }
    }


    public PostAdapter(List<Post> complains, Activity activity) {
        this.activity = activity;
        this.complains = complains;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
        final Post complain = complains.get(position);
        holder.watcherCounterTextView.setText(Integer.toString(complain.getViewCount()));
        holder.detailsTextView.setText(complain.getDesc());
        holder.titleTextView.setText("kwa : " + complain.getInstitution());
        holder.likeCounterTextView.setText(Integer.toString(complain.getLikeCount()));
        holder.commentsCountTextView.setText(Integer.toString(complain.getCommentCount()));
        final CharSequence date = FormatterUtil.getRelativeTimeSpanStringShort(activity,complain.getCreatedAt());
        holder.dateTextView.setText(date);
       Picasso.with(activity).load(AppConfig.BASE_URL +"api/document/" +complain.getImage1Path()).into(holder.imageView);

        holder.commentsCounterContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, PostDetailActivity.class);
                intent.putExtra(AppConfig.COMPLAIN_ID, complain.getId());
                intent.putExtra(AppConfig.DATE_CREATED, complain.getCreatedAt());
                intent.putExtra(AppConfig.NAME, complain.getUser());
                intent.putExtra(AppConfig.DESC, complain.getDesc());
                intent.putExtra(AppConfig.IMAGE, complain.getImage1Path());
                intent.putExtra(AppConfig.VIEWS, complain.getViewCount());
                intent.putExtra(AppConfig.LIKE, complain.getLikeCount());
                intent.putExtra(AppConfig.INSTITUTION, complain.getInstitution());
                intent.putExtra(AppConfig.COMMENTS, complain.getCommentCount());
                activity.startActivity(intent);

            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            int viewCounter = 1;
            @Override
            public void onClick(View v) {
                // viewCounter++;

                postViewCouter(complain.getId(), viewCounter);
                Intent intent = new Intent(activity, PostDetailActivity.class);
                intent.putExtra(AppConfig.COMPLAIN_ID, complain.getId());
                intent.putExtra(AppConfig.DATE_CREATED, complain.getCreatedAt());
                intent.putExtra(AppConfig.NAME, complain.getUser());
                intent.putExtra(AppConfig.DESC, complain.getDesc());
                intent.putExtra(AppConfig.IMAGE, complain.getImage1Path());
                intent.putExtra(AppConfig.LIKE, complain.getLikeCount());
                intent.putExtra(AppConfig.VIEWS, complain.getViewCount());
                intent.putExtra(AppConfig.INSTITUTION, complain.getInstitution());
                intent.putExtra(AppConfig.COMMENTS, complain.getCommentCount());
                activity.startActivity(intent);

            }
        });

        holder.likesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLike(complain.getId(),1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return complains.size();
    }


    public void setFilter(List<Complain> complains) {
        complains = new ArrayList<>();
        complains.addAll(complains);
        notifyDataSetChanged();
    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void postLike(int complainId, int vote) {


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
                    Toast.makeText(activity, "liked", Toast.LENGTH_SHORT).show();
                    activity.finish();
                    activity.startActivity(activity.getIntent());

                }else {
                    assert serverResponse != null;
                }


            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {

            }
        });
    }


    private void postViewCouter(int complainId, int viewCounter) {

        Log.d(TAG, "onBindViewHolderTest : " + viewCounter);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<Complain> call = getResponse.postViewCounter(complainId, viewCounter);
        call.enqueue(new Callback<Complain>() {
            @Override
            public void onResponse(Call<Complain> call, Response<Complain> response) {
                Complain serverResponse = response.body();
                if(response.isSuccessful() && serverResponse != null){
                   // activity.finish();
                    //activity.startActivity(activity.getIntent());

                }else {
                    assert serverResponse != null;
                }


            }

            @Override
            public void onFailure(Call<Complain> call, Throwable t) {
                Log.d(TAG, "onBindViewHolderTest : " + t.toString());
            }
        });
    }


}
