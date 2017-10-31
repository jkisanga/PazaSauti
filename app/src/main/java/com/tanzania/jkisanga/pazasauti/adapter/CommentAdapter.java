package com.tanzania.jkisanga.pazasauti.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.activity.PostDetailActivity;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.pojo.Comment;
import com.tanzania.jkisanga.pazasauti.pojo.Post;
import com.tanzania.jkisanga.pazasauti.utils.FormatterUtil;
import com.tanzania.jkisanga.pazasauti.views.ExpandableTextView;

import java.util.List;

/**
 * Created by Dell on 10/28/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<Comment> comments;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImageView;
        ExpandableTextView commentTextView;
        TextView dateTextView;

        public MyViewHolder(View view) {
            super(view);
             avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            commentTextView = (ExpandableTextView) view.findViewById(R.id.commentText);
            dateTextView = (TextView) view.findViewById(R.id.dateTextView);


        }
    }


    public CommentAdapter(List<Comment> commentList, Activity activity) {
        this.activity = activity;
        this.comments = commentList;
    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        CharSequence date = FormatterUtil.getRelativeTimeSpanString(activity, comment.getCreatedAt());

        holder.commentTextView.setText(comment.getComment());
        holder.dateTextView.setText(date);

    }



    @Override
    public int getItemCount() {
        return comments.size();
    }
}
