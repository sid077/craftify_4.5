package com.siddhant.craftifywallpapers.views.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.adapter.ViewPagerAdapterMain;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CheckBox checkboxChangeTheme;
    private CheckBox checkboxNav;
    ViewPager viewPagerMain;
    TabLayout tabLayoutMain;
    MainViewModel viewModel;
    private SearchView searchView;
    private FragmentTrending fragmentTrending;
    private SearchRecentSuggestions suggestions;
    private SearchManager searchManager;
   private FloatingActionButton fabPexels;
    public static boolean isNightModeEnabled;
    private static final String ADMOB_ID="ca-app-pub-2724635946881674~2482573510";
    private SharedPreferences preferences;
    private ConstraintLayout constraintLayout;
    private ExtendedFloatingActionButton fabChangeThemen;
    public TextView textViewTitle;


    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        MobileAds.initialize(this, ADMOB_ID);
        setContentView(R.layout.activity_main);
        tabLayoutMain = findViewById(R.id.tabLayoutMain);
        viewPagerMain = findViewById(R.id.viewPagerMain);
        checkboxNav = findViewById(R.id.checkboxNav);
        fabChangeThemen = findViewById(R.id.fabChTheme);
        textViewTitle = findViewById(R.id.textViewTitle);
        fabPexels = findViewById(R.id.fabPexels);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        checkboxChangeTheme = findViewById(R.id.checkBoxchangeTheme);

//
        constraintLayout = findViewById(R.id.linearLayout2);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        final NavigationView navigationView = findViewById(R.id.nav_view_main);
//
        navigationView.setNavigationItemSelectedListener(this);
//



        preferences = getSharedPreferences("statePref",MODE_PRIVATE);
        if(isNightModeEnabled=preferences.getBoolean("NIGHT_MODE",false)) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            checkboxChangeTheme.setChecked(true);

        }
        suggestions = new SearchRecentSuggestions(this,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        MainActivity mainActivity = this;

        viewModel = ViewModelProviders.of(mainActivity).get(MainViewModel.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database = viewModel.getDatabaseInstance(getApplicationContext());
                viewModel.setAllFav(database);
                viewModel.setWallpaperFavLiveData();
            }
        }).start();
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);







        PagerAdapter pagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(),this);
        viewPagerMain.setAdapter(pagerAdapter);
        viewPagerMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain));


        tabLayoutMain.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerMain.setCurrentItem(tab.getPosition());


                 switch (tab.getPosition()){
                     case 0:
                         textViewTitle.setText("Gallery");
                         tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_border_tab_layout_24dp);
                         break;
                     case 1:
                         textViewTitle.setText("Favourites");
                         tab.setIcon(R.drawable.ic_favorite_black_tablayout_24dp);
                         break;
                     case 2:
                         textViewTitle.setText("Automatic Wallpapers");
                         tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_border_tab_layout_24dp);
                         break;
                     default:
                         textViewTitle.setText("Craftify");
                         tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_border_tab_layout_24dp);
                 }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"vp clicked",Toast.LENGTH_SHORT).show();
            }
        });




    checkboxNav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                drawer.openDrawer(Gravity.LEFT);
            else
                drawer.closeDrawer(Gravity.LEFT);
        }
    });
    drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            if(!checkboxNav.isChecked())
                checkboxNav.setChecked(true);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            if(checkboxNav.isChecked())
                checkboxNav.setChecked(false);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    });

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                suggestions.saveRecentQuery(query,null);
                fragmentTrending = new FragmentTrending();
                Bundle bundle = new Bundle();
                bundle.putString("query",query);
                fragmentTrending.setArguments(bundle);
                fragmentTrending.show(getSupportFragmentManager(),query);
                searchView.setIconifiedByDefault(true);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {


                    return true;
                } else {

                    return false;
                }
            }

        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    searchView.setIconifiedByDefault(true);
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter selectedView = searchView.getSuggestionsAdapter();
                Cursor cursor = (Cursor) selectedView.getItem(position);
                int index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
                searchView.setQuery(cursor.getString(index), true);
                return true;
            }
        });
        checkboxChangeTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isNightModeEnabled = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferences.edit().putBoolean("NIGHT_MODE", isNightModeEnabled).commit();
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    fabPexels.setImageResource(R.drawable.pexels_white);

                }
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else{
                    isNightModeEnabled = false;

                    preferences.edit().putBoolean("NIGHT_MODE", isNightModeEnabled).commit();
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    fabPexels.setImageResource(R.drawable.pexels_black);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }


            }
        });

    fabChangeThemen.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNightModeEnabled){
                fabChangeThemen.setText("Light ");
                fabChangeThemen.extend();
                fabChangeThemen.setIcon(getResources().getDrawable(R.drawable.ic_wb_sunny_black_24dp));
                fabChangeThemen.animate().rotationBy(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        checkboxChangeTheme.setChecked(false);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            else {
                fabChangeThemen.setText("Dark");
                fabChangeThemen.extend();
                fabChangeThemen.setIcon(getResources().getDrawable(R.drawable.ic_brightness_3_black_24dp));
                fabChangeThemen.animate().rotationBy(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        checkboxChangeTheme.setChecked(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }
    });
        ;

    }
    public void changeTheme(){
        Log.i("onclick","change theme");
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.closeDatabase(database);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem item = menu.findItem(R.id.app_bar_switch_theme);







//


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.app_bar_search)
            searchView = findViewById(R.id.app_bar_search);
        if(id == R.id.app_bar_switch_theme){
            if(item.isChecked())
                item.setChecked(false);

            else
                item.setChecked(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            viewPagerMain.setCurrentItem(0);
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START);// Handle the camera action

        } else if (id == R.id.contact_us) {
            new BottomSheetContactUs().show(getSupportFragmentManager(),"contactUs");

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, try Craftify for some awesome looking wallpapers.\n"+"https://play.google.com/store/apps/details?id=com.siddhant.craftifywallpapers");
            startActivity(shareIntent);

        }
        else if(id==R.id.rate_us)
        {
            Uri uri = Uri.parse("market://details?id="+getApplicationContext().getPackageName());
            Intent i = new Intent(Intent.ACTION_VIEW,uri);
            try {
                startActivity(i);
            }
            catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
            }


        }
        else if(id==R.id.privacy_policy){
            String policyIntent = "https://craftify-wallpapers.blogspot.com/2019/02/privacy-policy-siddhant-dubey-built.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(policyIntent));
            startActivity(i);


        }
        else if(id==R.id.nav_favorites){
            viewPagerMain.setCurrentItem(1);
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START);

        }
        else if(id== R.id.nav_auto_wallpaper){
            viewPagerMain.setCurrentItem(2);
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(id == R.id.editor_trending_nav){
            fragmentTrending = new FragmentTrending();
            Bundle bundle = new Bundle();
            bundle.putString("query","wallpapers");
            fragmentTrending.setArguments(bundle);
            if(!fragmentTrending.isVisible())
                fragmentTrending.show(getSupportFragmentManager(),"trending");

        }


        return true;
    }


    public void openPexelSite(View view) {
        String url = "https://www.pexels.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
