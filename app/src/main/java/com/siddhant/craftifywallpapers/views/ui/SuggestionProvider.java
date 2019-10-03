package com.siddhant.craftifywallpapers.views.ui;

import android.content.SearchRecentSuggestionsProvider;

public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.siddhant.cratifywallpapers.view.ui.SuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY,MODE);
    }
}
