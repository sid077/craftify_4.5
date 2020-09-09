package com.siddhant.craftifywallpapers.views.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.views.ui.BottomSheetSpecialWalp;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;

import java.util.List;

public class RVCatSpecialAdapter extends RecyclerView.Adapter<RVCatSpecialAdapter.ViewHolder> {
    public List<String> getSpecialCat() {
        return specialCat;
    }

    public void setSpecialCat(List<String> specialCat) {
        this.specialCat = specialCat;
    }

    List<String > specialCat;
    Context context;
    FragmentManager fm;
    MainActivity mainActivity;
    RVCatSpecialAdapter adapter = this;
    public RVCatSpecialAdapter(List<String> specialCat, FragmentManager fm, MainActivity mainActivity) {
        this.specialCat = specialCat;
        this.fm = fm;
        this.mainActivity = mainActivity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view  = LayoutInflater.from(context).inflate(R.layout.specials_raw,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        if(holder.textView.getVisibility()==View.VISIBLE){
            holder.view.setVisibility(View.VISIBLE);

        }
        else {
            holder.view.setVisibility(View.GONE);
        }
        if(position==0){
            mainActivity.viewModel.loadPbPhotos(specialCat.get(position),"vertical",null,0,0,null,null);

        }
        holder.textView.setText(specialCat.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Bundle bundle = new Bundle();
//                bundle.putString("cat",specialCat.get(position));
//                BottomSheetDialogFragment fragment = new BottomSheetSpecialWalp();
//                fragment.setArguments(bundle);
//                fragment.show(fm,"SP");

//                        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                holder.cardView.setLayoutParams(cardViewParams);
//                ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams)holder. cardView.getLayoutParams();
//                cardViewMarginParams.setMargins(4, 4, 4, 4);
//                holder.cardView.requestLayout();
                String str = specialCat.get(position);
                specialCat.remove(specialCat.get(position));
                specialCat.add(specialCat.size(),str);
                mainActivity.viewModel.loadPbPhotos(specialCat.get(position),"vertical",null,0,0,null,null);

             notifyDataSetChanged();
//
//                holder.view.setVisibility(View.VISIBLE);
//                for(int i=0;i<specialCat.size();i++){
//                    if(specialCat.get(position).equals(holder.textView.getText().toString())){
//                        holder.view.setVisibility(View.VISIBLE);
//                        mainActivity.viewModel.loadPbPhotos(specialCat.get(position),"vertical",null,0,0,null,null);
//                        notifyDataSetChanged();
//
//
//                    }
//                    else {
//                        holder.view.setVisibility(View.INVISIBLE);
//                    }
//                }
            }
        });
//        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mainActivity.viewModel.loadPbPhotos(specialCat.get(position),"vertical",null,0,0,null,null);
//                return false;
//            }
//        });

    }



    @Override
    public int getItemCount() {
        if(specialCat!=null)
        return specialCat.size();
        else  return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView = itemView.findViewById(R.id.textViewSPCat);
        CardView cardView = itemView.findViewById(R.id.cardViewSpCat);
        View view = itemView.findViewById(R.id.divider2);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
