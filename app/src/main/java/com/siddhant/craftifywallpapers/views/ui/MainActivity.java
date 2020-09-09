package com.siddhant.craftifywallpapers.views.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperPBPojo;
import com.siddhant.craftifywallpapers.repositories.AppDatabase;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.adapter.PBWallpapersRecyclerViewAdapter;
import com.siddhant.craftifywallpapers.views.adapter.RVCatSpecialAdapter;
import com.siddhant.craftifywallpapers.views.adapter.ViewPagerAdapterMain;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CheckBox checkboxChangeTheme;
    private CheckBox checkboxNav;
    ViewPager viewPagerMain;
    TabLayout tabLayoutMain;
    public MainViewModel viewModel;
    private SearchView searchView;
    private FragmentTrending fragmentTrending;
    private SearchRecentSuggestions suggestions;
    private SearchManager searchManager;
    RecyclerView recyclerViewSpecials;
   private FloatingActionButton fabPexels;
    public static boolean isNightModeEnabled;
    private static final String ADMOB_ID="ca-app-pub-2724635946881674~2482573510";
    private SharedPreferences preferences;
    private ConstraintLayout constraintLayout;
    private ExtendedFloatingActionButton fabChangeThemen;
    public TextView textViewTitle;
    private CardView cardViewTrending;
    private List<WallpaperPBPojo.Hits> hits1;
    private PagerAdapter pagerAdapter;
    private MainActivity mainActivity;
    private ImageView fabPixa;
    private int stars;
    private float rating;
    private String feedback;
    public static FirebaseAnalytics firebaseAnalytics;

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
        MobileAds.initialize(getApplicationContext(), ADMOB_ID);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        setContentView(R.layout.activity_main);



        cardViewTrending = findViewById(R.id.cardViewTrending);
        tabLayoutMain = findViewById(R.id.tabLayoutMain);
        viewPagerMain = findViewById(R.id.viewPagerMain);
        fabPixa = findViewById(R.id.floatingActionButtonPixa);
        checkboxNav = findViewById(R.id.checkboxNav);
        fabChangeThemen = findViewById(R.id.fabChTheme);
        textViewTitle = findViewById(R.id.textViewTitle);
        fabPexels = findViewById(R.id.fabPexels);
        recyclerViewSpecials = findViewById(R.id.rVSpecials);
        mainActivity = this;
        viewModel = ViewModelProviders.of(mainActivity).get(MainViewModel.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        checkboxChangeTheme = findViewById(R.id.checkBoxchangeTheme);

//
        constraintLayout = findViewById(R.id.linearLayout2);
     //   fabPixa.setImageDrawable(getResources().getDrawable(R.drawable.ic_logopixa));
        fabPixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.pixabay.com"));
                startActivity(intent);

            }
        });
        cardViewTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTrending = new FragmentTrending();
                Bundle bundle = new Bundle();
                bundle.putString("query","wallpapers");

                fragmentTrending.setArguments(bundle);
                if(!fragmentTrending.isVisible())
                    fragmentTrending.show(getSupportFragmentManager(),"trending");
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        final NavigationView navigationView = findViewById(R.id.nav_view_main);
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                getResources().getColor(R.color.colorSecondary),
                getResources().getColor(R.color.colorSecondary)

                ,
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryVarient)
        };
//        int[] colors = new int[] {
//                Color.WHITE,
//                Color.WHITE
//
//                ,
//                Color.WHITE,
//                Color.WHITE
//        };

        ColorStateList myList = new ColorStateList(states, colors);

      navigationView.setItemIconTintList(myList);
        navigationView.setItemTextColor(myList);
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




        new Thread(new Runnable() {
            @Override
            public void run() {
                database = viewModel.getDatabaseInstance(getApplicationContext());
                viewModel.setAllFav(database);
                viewModel.setWallpaperFavLiveData();
            }
        }).start();
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);



        mainActivity.viewModel.specialsCat.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                recyclerViewSpecials.setAdapter(new RVCatSpecialAdapter(strings,getSupportFragmentManager(), mainActivity));
                recyclerViewSpecials.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));

           //  recyclerViewSpecials.suppressLayout(true);
            }
        });

        mainActivity.viewModel.specialcatagory();
        pagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(), mainActivity,null);
        viewPagerMain.setAdapter(pagerAdapter);
        viewPagerMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain));
        tabLayoutMain.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerMain.setCurrentItem(tab.getPosition());


                 switch (tab.getPosition()){
                     case 0:
                         fabChangeThemen.setVisibility(View.VISIBLE);

                         textViewTitle.setText(R.string.craftify);
                        // recyclerViewSpecials.setVisibility(View.VISIBLE);
                        // tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_border_tab_layout_24dp);
                         tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_border_tab_layout_24dp);
                         break;
                     case 1:
                         fabChangeThemen.setVisibility(View.GONE);
                         textViewTitle.setText(R.string.fav);
                         tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_black_tablayout_24dp);
                        // recyclerViewSpecials.setVisibility(View.GONE);
                         break;
//                     case 2:
//                         textViewTitle.setText(R.string.fav);
//                        // recyclerViewSpecials.setVisibility(View.GONE);
//                         tab.setIcon(R.drawable.ic_favorite_black_tablayout_24dp);
//                         break;
                     case 2:
                         textViewTitle.setText(R.string.craftify);
                         fabChangeThemen.setVisibility(View.VISIBLE);

                         // recyclerViewSpecials.setVisibility(View.GONE);
                         tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_favorite_border_tab_layout_24dp);
                         break;
                     default:
                         textViewTitle.setText(R.string.craftify);
                         fabChangeThemen.setVisibility(View.VISIBLE);

                         //  recyclerViewSpecials.setVisibility(View.GONE);
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

        searchView = findViewById(R.id.searchView);
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
                return newText.length() > 0;
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width1 = displayMetrics.widthPixels;

        int height1 = displayMetrics.heightPixels;
        viewModel.loadPbPhotos("vector","vertical","dark",width1-50,height1-100,new String[]{"black"},"popular");

        //if(mainActivity.viewModel.mutableLiveDataWallpaperPB.getValue()!=null&&mainActivity.viewModel.mutableLiveDataWallpaperPB.getValue().size()<=0)
        viewModel.mutableLiveDataWallpaperPB.observe(MainActivity.this, new Observer<List<WallpaperPBPojo.Hits>>() {
            @Override
            public void onChanged(List<WallpaperPBPojo.Hits> hits) {
                hits1 = hits;
                pagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(), mainActivity,hits);
                viewPagerMain.setAdapter(pagerAdapter);


            }
        });

//
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
        ;

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("reviewDialog",true)){




            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.feedback,null);
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
            final AlertDialog alertDialog =   builder.setView(view).create();
            alertDialog.show();
           final RatingBar ratingBar = view.findViewById(R.id.ratingBar);
           ratingBar.setOnClickListener(new View.OnClickListener() {


               @Override
               public void onClick(View v) {
                   rating = ratingBar.getRating();
                    stars = ratingBar.getNumStars();
               }
           });
            final EditText editTextFeedback = view.findViewById(R.id.editTextFeedback);
            TextView buttonDone = view.findViewById(R.id.buttonOk);
            buttonDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                       feedback = editTextFeedback.getText().toString();
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    viewModel.addFeedbackToFirebase(ratingBar.getRating(), ratingBar.getNumStars(), feedback);
                    alertDialog.dismiss();
                    finish();


                }

            });
            CheckBox checkBox = view.findViewById(R.id.checkBoxDoNotShowAgain);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("reviewDialog",false).apply();
                    }
                    else{
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("reviewDialog",true).apply();

                    }

                }
            });
           TextView buttonCancel = view.findViewById(R.id.buttonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                    finish();

                }

            });

        }
        else {
            finish();
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
