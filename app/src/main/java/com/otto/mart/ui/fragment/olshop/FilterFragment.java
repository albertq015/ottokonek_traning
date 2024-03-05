package com.otto.mart.ui.fragment.olshop;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.support.util.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterFragment extends BottomSheetDialogFragment {

    private BottomSheetBehavior<FrameLayout> behavior;
    private ViewGroup categoryContainer;
    private List<Category> categories = new ArrayList<>();
    CategoryFragment categoryFragment;
    private Category selectedCategory;
    TextView apply, reset, categoryName, f1, f2, f3;
    EditText lowPrice, highPrice;
    Long lowPriceValue, highPriceValue;
    private TextWatcher lowPriceWatcher;
    private TextWatcher highPriceWatcher;

    public static FilterFragment newInstance() {

        Bundle args = new Bundle();

        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        categoryContainer = view.findViewById(R.id.categoryContainer);
        categoryFragment = CategoryFragment.newInstance();
        categoryFragment.setCategories(categories);
        categoryContainer.setOnClickListener(v -> categoryFragment.show(getChildFragmentManager(), "category"));

        categoryName = view.findViewById(R.id.categoryName);
        lowPrice = view.findViewById(R.id.lowPrice);
        highPrice = view.findViewById(R.id.highPrice);

        f1 = view.findViewById(R.id.f1);
        f2 = view.findViewById(R.id.f2);
        f3 = view.findViewById(R.id.f3);

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPriceValue(0, 75000);
            }
        });

        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPriceValue(75000, 150000);
            }
        });

        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPriceValue(150000, 200000);
            }
        });

        categoryName.setText(selectedCategory.getName());

        apply = view.findViewById(R.id.filterAppy);
        apply.setOnClickListener(v -> {
            if (!lowPrice.getText().toString().isEmpty()) {
                String field = lowPrice.getText().toString().replace(",", "");
                lowPriceValue = DataUtil.getLong(field);
            } else lowPriceValue = null;

            if (!highPrice.getText().toString().isEmpty()) {
                String field = highPrice.getText().toString().replace(",", "");
                highPriceValue = DataUtil.getLong(field);
            } else highPriceValue = null;

            try {
                ((AllCatalogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("all")).setSelectedCategory(null, lowPriceValue, highPriceValue);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                dismiss();
            }
        });

        lowPriceWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long amount = DataUtil.getLong(s.toString().replace(getString(R.string.text_currency), ""));

                lowPrice.removeTextChangedListener(this);
                if (amount > 0) {
                    lowPrice.setText(DataUtil.convertCurrency(amount));
                } else lowPrice.setText("");

                lowPrice.setSelection(lowPrice.getText().length());
                lowPrice.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        highPriceWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long amount = DataUtil.getLong(s.toString().replace(getString(R.string.text_currency), ""));

                highPrice.removeTextChangedListener(this);
                if (amount > 0) {
                    highPrice.setText(DataUtil.convertCurrency(amount));
                } else highPrice.setText("");

                highPrice.setSelection(highPrice.getText().length());
                highPrice.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        lowPrice.addTextChangedListener(lowPriceWatcher);
        highPrice.addTextChangedListener(highPriceWatcher);

        reset = view.findViewById(R.id.filterReset);
        reset.setOnClickListener(v -> {
            lowPrice.setText("");
            highPrice.setText("");
            highPriceValue = null;
            lowPriceValue = null;
            selectedCategory = categories.get(0);
            categoryName.setText(selectedCategory.getName());
            categoryFragment.reset();
            try {
                ((AllCatalogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("all")).setSelectedCategory(null, lowPriceValue, highPriceValue);
//                ((AllCatalogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("all")).resetPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    private void setPriceValue(int min, int max) {
        lowPrice.setText(String.valueOf(min));
        highPrice.setText(String.valueOf(max));
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        Objects.requireNonNull(bottomSheet).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        if (categoryFragment != null) {
            categoryFragment.setCategories(categories);
        }

        if (categories != null) {
            for (Category item : categories) {
                if (item.isSelected()) {
                    selectedCategory = item;
                }
            }
        }
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        if (categoryFragment != null) {
            categoryFragment.dismiss();
            categoryName.setText(selectedCategory.getName());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (categoryFragment.isVisible())
            categoryFragment.dismiss();
    }
}
