package com.otto.mart.ui.Partials.listener;

import android.text.Editable;
import android.text.TextWatcher;

import com.otto.mart.ui.Partials.adapter.InstantAutocompleteOttoPPobAdapter;

public class AutocompleteTextWatcher implements TextWatcher {
    private InstantAutocompleteOttoPPobAdapter kAdapter;

    public AutocompleteTextWatcher(InstantAutocompleteOttoPPobAdapter kAdapter) {
        this.kAdapter = kAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        kAdapter.getFilter().filter(s.toString().toLowerCase());
    }
}

