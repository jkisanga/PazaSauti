package com.tanzania.jkisanga.pazasauti.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanzania.jkisanga.pazasauti.R;
import com.tanzania.jkisanga.pazasauti.activity.MainActivity;
import com.tanzania.jkisanga.pazasauti.activity.PickerActivity;
import com.tanzania.jkisanga.pazasauti.app.AppConfig;
import com.tanzania.jkisanga.pazasauti.pojo.Institution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 10/3/2017.
 */

public class InstitutionAdapter extends RecyclerView.Adapter<InstitutionAdapter.MyViewHolder> {

    private List<Institution> institutions;

    Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, phone;
        RelativeLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            linearLayout = (RelativeLayout) view.findViewById(R.id.top_layout);

            title = (TextView) view.findViewById(R.id.title);
            phone = (TextView) view.findViewById(R.id.phone_no);


        }
    }


    public InstitutionAdapter(List<Institution> institutionList, Activity activity) {
        this.activity = activity;
        this.institutions = institutionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Institution institution = institutions.get(position);

        holder.title.setText(institution.getName());
         holder.phone.setText("No: " + institution.getDesc());
        if (position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#09A9FF"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#0A9B88"));
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, PickerActivity.class);
                intent.putExtra(AppConfig.ID, institution.getId());
                intent.putExtra(AppConfig.NAME, institution.getName());
                intent.putExtra(AppConfig.PHONE, institution.getDesc());

                activity.startActivity(intent);
//                profileDetailFragment.setArguments(bundle);
//                activity.getFragmentManager().beginTransaction()
//                        .replace(R.id.layout_container, profileDetailFragment,"FRAGMENT")
//                        .addToBackStack(null)
//                        .commit();
//
           }
        });


    }

    @Override
    public int getItemCount() {
        return institutions.size();
    }


    public void setFilter(List<Institution> docs) {
        institutions = new ArrayList<>();
        institutions.addAll(docs);
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
}
