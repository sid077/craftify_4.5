package com.siddhant.craftifywallpapers.views.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.views.adapter.FavoriteRecyclerViewAdapter;

public class SettingsFragment extends PreferenceFragmentCompat {
    InterstitialAd interstitialAd;
    private static final String INTERSTITIAL_ID = "ca-app-pub-2724635946881674/5001333401";
    private Intent i;
    MainActivity mainActivity ;
    private float rating;
    private int stars;
    private String feedback;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference preferenceAutoChange = this.findPreference("autoChange");
        interstitialAd = new InterstitialAd(getActivity().getApplicationContext());
        mainActivity = (MainActivity) getActivity();
        interstitialAd.setAdUnitId(INTERSTITIAL_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        i = new Intent();
//                    int timeInMillis= 1000;
//                    i.putExtra("timeInMillis",timeInMillis);
//                    i.putExtra("timeFormat",timeFormat);
        i.setComponent(new ComponentName("com.siddhant.craftifywallpapers", "com.siddhant.craftifywallpapers.views.service.AutoWallpaperChanger"));
        preferenceAutoChange.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(((boolean)newValue)) {
                    if (!FavoriteRecyclerViewAdapter.hasFav) {
                        Toast.makeText(getActivity().getApplicationContext(), "It seems you have no wallpapers as favourites", Toast.LENGTH_LONG).show();

                        return false;
                    }




//                    ScreenOnReciever reciever = new ScreenOnReciever();
//                    IntentFilter filter = new IntentFilter();
//                    filter.addAction(Intent.ACTION_SCREEN_ON);
//                    getActivity().registerReceiver(reciever,filter);

                    if (i.getComponent() == null) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.siddhant.craftifywallpapers")));
                        Snackbar.make(getView(), "Please download our primary app", Snackbar.LENGTH_SHORT).show();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Snackbar.make(getView(), "Automatic Wallpaper changer enabled", Snackbar.LENGTH_SHORT).show();
                        if (interstitialAd.isLoaded())
                            interstitialAd.show();
                        getActivity().startForegroundService(i);

                    } else {
                        getActivity().startService(i);
                        if (interstitialAd.isLoaded())
                            interstitialAd.show();
                        Snackbar.make(getView(), "Automatic Wallpaper changer enabled", Snackbar.LENGTH_SHORT).show();
                    }
                    return true;
                }


                    else {

                        getActivity().stopService(i);
                        if(interstitialAd.isLoaded())
                            interstitialAd.show();
                        Snackbar.make(getView(),"Automatic Wallpaper changer disabled",Snackbar.LENGTH_SHORT).show();

                        return  true;

                    }



        }
        });
        Preference preferenceContact = findPreference("contact");
        preferenceContact.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setDataAndType(Uri.parse("siddhant.d777@gmail.com"), "text/plain");
                    startActivity(Intent.createChooser(intent, "choose app to send email"));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),"unable to contact the developer",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        Preference preferenceSwift = findPreference("swift");
        preferenceSwift.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.craft.craftifyswift"));
                startActivity(intent);
                return false;
            }
        });
        Preference preferenceReview= findPreference("review");
        preferenceReview.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                if(!PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("reviewDialog",true)){
//                    return false;
//                }
                View view = LayoutInflater.from(mainActivity).inflate(R.layout.feedback,null);
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mainActivity);
                final AlertDialog dialog = builder.setView(view).create();
                       dialog .show();
                final RatingBar ratingBar = view.findViewById(R.id.ratingBar);
                CheckBox checkBox = view.findViewById(R.id.checkBoxDoNotShowAgain);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putBoolean("reviewDialog",false).apply();
                        }
                        else{
                            PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putBoolean("reviewDialog",true).apply();

                        }

                    }
                });
               MaterialTextView buttonCancel = view.findViewById(R.id.buttonCancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       dialog.dismiss();

                    }

                });
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
                        mainActivity.viewModel.addFeedbackToFirebase(ratingBar.getRating(), ratingBar.getNumStars(), feedback);
                        dialog.dismiss();

            }
        });
                return false;
        }
        });
    }
}


