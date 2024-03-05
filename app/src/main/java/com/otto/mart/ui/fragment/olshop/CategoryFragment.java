package com.otto.mart.ui.fragment.olshop;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.ui.Partials.adapter.olshop.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryFragment extends BottomSheetDialogFragment {

    private RecyclerView cat1, cat2;
    private CategoryAdapter adapter1, adapter2;
    private BottomSheetBehavior<FrameLayout> behavior;
    private List<Category> categories;

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_old, container, false);

        cat1 = view.findViewById(R.id.cat1);
        cat1.setNestedScrollingEnabled(false);
        cat2 = view.findViewById(R.id.cat2);

        adapter1 = new CategoryAdapter(1);
        adapter2 = new CategoryAdapter(2);

        adapter1.setListener(category -> {
            Category all = new Category(new ArrayList<>(), "Semua " + category.getName(), category.getIcon(), category.getId(), true);
            if (category.getSub_categories().size() > 0) {
                if (category.getSub_categories().get(0).getId() != category.getId()) {
                    category.getSub_categories().add(0, all);
                }
            } else category.getSub_categories().add(0, all);
            adapter2.setCategories(category.getSub_categories());
            adapter2.notifyDataSetChanged();
        });

        adapter2.setListener(new CategoryAdapter.OnItemCategorySelected() {
            @Override
            public void onItemSelected(Category category) {
                try {
                    if (getParentFragment() instanceof FilterFragment) {
                        ((FilterFragment) getParentFragment()).setSelectedCategory(category);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cat1.setLayoutManager(new LinearLayoutManager(getContext()));
        cat2.setLayoutManager(new LinearLayoutManager(getContext()));

        cat1.setAdapter(adapter1);
        cat2.setAdapter(adapter2);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        Objects.requireNonNull(bottomSheet).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        adapter1.setCategories(categories);
        adapter1.notifyDataSetChanged();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void reset() {
        if (adapter1 != null) {
            adapter1.reset(getContext());
        }
    }
}