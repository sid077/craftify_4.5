package com.siddhant.craftifywallpapers.views.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;
import com.siddhant.craftifywallpapers.models.database.WallpaperFavPojo;
import com.siddhant.craftifywallpapers.views.ui.FragmentAllTrending;
import com.siddhant.craftifywallpapers.views.ui.FragmentAutoChanger;
import com.siddhant.craftifywallpapers.views.ui.FragmentCategories;
import com.siddhant.craftifywallpapers.views.ui.FragmentFavourites;
import com.siddhant.craftifywallpapers.views.ui.FragmentTrending;
import com.siddhant.craftifywallpapers.views.ui.MainActivity;
import com.siddhant.craftifywallpapers.views.ui.SettingsFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapterMain extends FragmentStatePagerAdapter {
    FragmentManager fragmentManager;
    MainActivity activity;
    List<WallpaperFavPojo> wallpaperFavPojos;
    List<WallpaperPBPojo.Hits> pbWallpaperList;
    public ViewPagerAdapterMain(FragmentManager fm, MainActivity activity) {
        super(fm);
        fragmentManager =fm;

    }


//    }

    public ViewPagerAdapterMain(FragmentManager supportFragmentManager, MainActivity activity, List<WallpaperPBPojo.Hits> hits) {
        super(supportFragmentManager);
        pbWallpaperList = hits;

        this.activity = activity;
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

            case 1:

                if(wallpaperFavPojos == null)
                return new FragmentFavourites();
                else {
                    FragmentFavourites fragmentFavourites = new FragmentFavourites();
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("wallpaperFav", (Serializable) wallpaperFavPojos);
                    return fragmentFavourites;
                }
            case 2:

                return new SettingsFragment();

        }
        return null;
    }



}
