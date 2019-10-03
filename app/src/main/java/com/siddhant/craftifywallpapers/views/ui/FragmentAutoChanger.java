package com.siddhant.craftifywallpapers.views.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.siddhant.craftifywallpapers.R;
import com.siddhant.craftifywallpapers.models.WallpaperCatagoryPojo;
import com.siddhant.craftifywallpapers.viewmodel.MainViewModel;
import com.siddhant.craftifywallpapers.views.service.AutoWallpaperChanger;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.ACTIVITY_SERVICE;

public class FragmentAutoChanger extends Fragment {
    private int firstDigit,lastDigit;
    private Spinner spinnerFirstDigit,spinnerLastDigit,spinnerCatagories,spinnerTimeFormat;
    private MainActivity mainActivity;
    private MainViewModel viewModel;
    private ArrayList<String> categoryName;
    private String timeFormat;
    private String category;
    private TextView textView;
    private Switch switchStartSevice;
    private int processId;
    private AdView adViewBanner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auto_changer_fragment,container,false);
        spinnerFirstDigit = view.findViewById(R.id.spinnerFirstDigit);
        spinnerLastDigit = view.findViewById(R.id.spinnerSecondDigit);
        spinnerCatagories = view.findViewById(R.id.spinnerWallpaperCatagory);
        spinnerTimeFormat = view.findViewById(R.id.spinnerTimeFormat);
        textView = view.findViewById(R.id.textViewPromtUserAuto);
        switchStartSevice = view.findViewById(R.id.switchStartServiceAuto);
        adViewBanner = view.findViewById(R.id.adView);
        mainActivity = (MainActivity) getActivity();

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewBanner.loadAd(adRequest);




        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.fetchCategory();
        final Observer<ArrayList<WallpaperCatagoryPojo>> observer = new Observer<ArrayList<WallpaperCatagoryPojo>>() {
            @Override
            public void onChanged(ArrayList<WallpaperCatagoryPojo> arrayList) {
                categoryName = new ArrayList<>();
                int i;
                for( i=0;i<arrayList.size()-1;i++)
                 categoryName.add(arrayList.get(i).getName());
                categoryName.removeAll(Collections.singleton(null));

                spinnerCatagories.setAdapter(new ArrayAdapter<>(mainActivity.getApplicationContext(),R.layout.simple_spinner, categoryName));


            }
        };
        viewModel.getLiveDataCatagories().observe(this,observer);

        spinnerFirstDigit.setAdapter(ArrayAdapter.createFromResource(getActivity().getApplicationContext(),R.array.numbers_array,R.layout.simple_spinner));

        spinnerLastDigit.setAdapter(ArrayAdapter.createFromResource(getActivity().getApplicationContext(),R.array.numbers_array,R.layout.simple_spinner));
        spinnerTimeFormat.setAdapter(ArrayAdapter.createFromResource(getActivity().getApplicationContext(),R.array.time_format_array,R.layout.simple_spinner));
        if(isServiceRunning())
            switchStartSevice.setChecked(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();


        spinnerFirstDigit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstDigit = Integer.parseInt(spinnerFirstDigit.getSelectedItem().toString());
                try {
                    if(firstDigit==0 && timeFormat.equalsIgnoreCase("minutes")) {
                        textView.setText("Time in seconds is not allowed,please select a number greater than zero.");
                        switchStartSevice.setEnabled(false);
                    }
                    else {
                        switchStartSevice.setEnabled(true);
                        textView.setText("Better to set duration greater than one hour.");
                    }


                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerLastDigit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lastDigit = Integer.parseInt(spinnerLastDigit.getSelectedItem().toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        spinnerCatagories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinnerCatagories.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTimeFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               timeFormat = spinnerTimeFormat.getSelectedItem().toString();

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }) ;
        switchStartSevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Intent intent = new Intent(getActivity().getApplicationContext(),AutoWallpaperChanger.class);
                if(isChecked){

                    mainActivity.viewModel.startServiceForAutoChange(mainActivity,firstDigit,lastDigit,timeFormat,category,intent);
                    if(isServiceRunning())
                        Toast.makeText(mainActivity.getApplicationContext(),"Auto Wallpaper changer started",Toast.LENGTH_SHORT).show();


                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity()
                                    .getApplicationContext().stopService(intent);
                        }
                    }).start();

                }
            }
        });

    }
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AutoWallpaperChanger.class.getName().equals(service.service.getClassName())) {
                processId = service.pid;
                return true;
            }
        }
        return false;
    }
}
