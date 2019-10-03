package com.siddhant.craftifywallpapers.views.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperCatagoryPojo;
import com.siddhant.craftifywallpapers.views.ui.FragmentTrending;


import java.util.ArrayList;

public class CatagoryRecyclerVIewAdapter extends RecyclerView.Adapter<CatagoryRecyclerVIewAdapter.ViewHolder> {
    ArrayList<WallpaperCatagoryPojo> arrayList ;
    FragmentManager fragmentManager ;
    public CatagoryRecyclerVIewAdapter(Context context, FragmentManager fragmentManager, ArrayList<WallpaperCatagoryPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragmentManager = fragmentManager;
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catagories_raw,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context)
                .load(arrayList.get(position).getUrl())
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.constraintLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        holder.textViewName.setText(arrayList.get(position).getName());
//        Picasso.get().load(arrayList.get(position).getUrl()).fit().into(holder.imageView);
        //Glide.with(context).load(arrayList.get(position).getUrl()).placeholder(R.drawable.th1).thumbnail(0.1f).into(holder.imageView);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTrending fragment = new FragmentTrending();
                Bundle bundle = new Bundle();
                bundle.putString("query",arrayList.get(position).getName());
                fragment.setArguments(bundle);
                fragment.show(fragmentManager,"category");
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewCategoryNameRaw);

            constraintLayout = itemView.findViewById(R.id.constraitLayoutCatagory);

        }
    }
}
