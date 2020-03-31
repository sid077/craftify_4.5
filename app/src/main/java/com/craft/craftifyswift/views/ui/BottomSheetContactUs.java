package com.craft.craftifyswift.views.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.craft.craftifyswift.R;

public class BottomSheetContactUs extends BottomSheetDialogFragment {
    TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_details,container,false);
        textView = view.findViewById(R.id.textViewIconDetails);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        textView.setText(Html.fromHtml("Icons made by <a href=\"https://www.flaticon.com/authors/roundicons\" title=\"Roundicons\">Roundicons</a> from <a href=\"https://www.flaticon.com/\" " +
                "title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://www.creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY- www.creativecommons.org/licenses/by/3.0/</a>"));



    }
}
