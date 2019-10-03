package com.siddhant.craftifywallpapers.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FetchCategoriesRepo {

    ArrayList<String> arrayListCatagories;

    public ArrayList<String> getArrayListCatagories() {
        return arrayListCatagories;
    }

    public void fetchCatagories(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("catagories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCatagories = new ArrayList<>();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    arrayListCatagories.add(String.valueOf(d.getValue()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
