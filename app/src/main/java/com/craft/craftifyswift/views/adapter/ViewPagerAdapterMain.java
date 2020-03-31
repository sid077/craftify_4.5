package com.craft.craftifyswift.views.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.craft.craftifyswift.models.database.WallpaperFavPojo;
import com.craft.craftifyswift.views.ui.FragmentAutoChanger;
import com.craft.craftifyswift.views.ui.FragmentCategories;
import com.craft.craftifyswift.views.ui.FragmentFavourites;
import com.craft.craftifyswift.views.ui.MainActivity;

import java.io.Serializable;
import java.util.List;


public class ViewPagerAdapterMain extends FragmentStatePagerAdapter {
    FragmentManager fragmentManager;
    MainActivity activity;
    List<WallpaperFavPojo> wallpaperFavPojos;
    public ViewPagerAdapterMain(FragmentManager fm, MainActivity activity) {
        super(fm);
        fragmentManager =fm;
        this.activity = activity;
    }

    public ViewPagerAdapterMain(FragmentManager supportFragmentManager, List<WallpaperFavPojo> wallpaperFavPojo) {
        super(supportFragmentManager);
        wallpaperFavPojos = wallpaperFavPojo;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
               // activity.textViewTitle.setText("Gallery");
                return new FragmentCategories();
               // return new FragmentTrending();
            case 1:
             //   activity.textViewTitle.setText("Favourites");
                if(wallpaperFavPojos == null)
                return new FragmentFavourites();
                else {
                    FragmentFavourites fragmentFavourites = new FragmentFavourites();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wallpaperFav", (Serializable) wallpaperFavPojos);
                    return fragmentFavourites;
                }
            case 2:
              //  activity.textViewTitle.setText("Automatic Wallpapers");
                return new FragmentAutoChanger();

        }
        return null;
    }



}
