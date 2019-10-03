package com.siddhant.craftifywallpapers.views.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.views.ui.FragmentAutoChanger;
import com.siddhant.craftifywallpapers.views.ui.FragmentCategories;
import com.siddhant.craftifywallpapers.views.ui.FragmentFavourites;
import com.siddhant.craftifywallpapers.views.ui.FragmentTrending;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapterMain extends FragmentStatePagerAdapter {
    FragmentManager fragmentManager;
    List<WallpaperFavPojo> wallpaperFavPojos;
    public ViewPagerAdapterMain(FragmentManager fm) {
        super(fm);
        fragmentManager =fm;
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
                return new FragmentCategories();
               // return new FragmentTrending();
            case 1:
                if(wallpaperFavPojos == null)
                return new FragmentFavourites();
                else {
                    FragmentFavourites fragmentFavourites = new FragmentFavourites();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wallpaperFav", (Serializable) wallpaperFavPojos);
                    return fragmentFavourites;
                }
            case 2:
                return new FragmentAutoChanger();

        }
        return null;
    }



}
