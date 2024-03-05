package com.otto.mart.ui.activity.olshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.fragment.olshop.AllCatalogFragment;
import com.otto.mart.ui.fragment.olshop.MainCatalogFragment;

import java.util.ArrayList;
import java.util.List;

import static com.otto.mart.GLOBAL.CART_COUNT;

public class CatalogActivity extends AppActivity {

    List<Category> categories = new ArrayList<>();
    AllCatalogFragment allCatalogFragment;
    MainCatalogFragment mainCatalogFragment;
    EditText search;
    View back, cartMenu, cartCountContainer, orderHistoryMenu, historyCountContainer;
    Category selected;
    TextView cartCount, historyOrderCount;
    boolean isMainView = true;
    boolean isSpecialEvent = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_catalog);

        search = findViewById(R.id.tokol_search);
        back = findViewById(R.id.btnToolbarBack);
        cartMenu = findViewById(R.id.cartMenu);
        cartCountContainer = findViewById(R.id.cart_tv_container);
        cartCount = findViewById(R.id.cartCount);
        orderHistoryMenu = findViewById(R.id.OrderHistoryMenu);
        historyCountContainer = findViewById(R.id.historyCountContainer);
        historyOrderCount = findViewById(R.id.historyOrderCount);

        back.setOnClickListener(v -> onBackPressed());

        isSpecialEvent = getIntent().getBooleanExtra("isSpecialEvent", false);

        search.setSingleLine();
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isMainView) {
                    allCatalogFragment.setPath("all");
                    allCatalogFragment.setCategories(selected.getSub_categories());
                    allCatalogFragment.setSelectedCategory(selected);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, allCatalogFragment, "all").addToBackStack("all").commit();
                    isMainView = false;
                    return true;
                }
                return false;
            }
        });

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (search.getText().toString().isEmpty()) {
                allCatalogFragment.setQuery(null);
            } else {
                allCatalogFragment.setQuery(search.getText().toString());
            }
            return true;
        });

        cartMenu.setOnClickListener(v -> startActivity(new Intent(CatalogActivity.this, CartOlshopActivity.class)));
        orderHistoryMenu.setOnClickListener(v->startActivity(new Intent(CatalogActivity.this, OrderStatusActivity.class)));

        allCatalogFragment = AllCatalogFragment.newInstance();
        mainCatalogFragment = MainCatalogFragment.newInstance((selectedCategory, path) -> {
            selected = selectedCategory;
            if (selectedCategory.getSub_categories().size() > 0) {
                if (!selectedCategory.getSub_categories().get(0).getName().contains("Semua ")) {
                    Category all = new Category(new ArrayList<>(), "Semua " + selectedCategory.getName(), selectedCategory.getIcon(), selectedCategory.getId(), false);
                    all.setSelected(true);
                    selectedCategory.getSub_categories().add(0, all);
                }
            }
            allCatalogFragment.setPath(path);
            allCatalogFragment.setCategories(selectedCategory.getSub_categories());
            allCatalogFragment.setSelectedCategory(selectedCategory.getSub_categories().get(0));
            getSupportFragmentManager().beginTransaction().add(R.id.frame, allCatalogFragment, "all").addToBackStack("all").commit();
            isMainView = false;
        });

        try {
            selected = new Gson().fromJson(getIntent().getStringExtra("selectedCategory"), new TypeToken<Category>() {
            }.getType());
            if (selected != null) {
//                this.selected = selected;
                mainCatalogFragment.setSelectedCategory(selected);
//                allCatalogFragment.setCategories(selected.getSub_categories());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        try {
            categories = new Gson().fromJson(getIntent().getStringExtra("category"), new TypeToken<List<Category>>() {
            }.getType());
            if (categories != null && categories.size() > 0) {
//                this.setSelected(categories.get(0));
                if (!isSpecialEvent)
                    allCatalogFragment.setSelectedCategory(categories.get(0));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        mainCatalogFragment.setCategories(categories);

        if (isSpecialEvent) {
            isMainView=false;
            allCatalogFragment.setPath("special-event");
            allCatalogFragment.setCategories(categories);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, allCatalogFragment,"all").disallowAddToBackStack().commit();
        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, mainCatalogFragment).disallowAddToBackStack().commit();
    }

    public void setSelected(Category selected) {
        if (selected.getSub_categories().size() > 0) {
            if (!selected.getSub_categories().get(0).getName().contains("Semua ")) {
                Category all = new Category(new ArrayList<>(), "Semua " + selected.getName(), selected.getIcon(), selected.getId(), false);
                all.setSelected(true);
                selected.getSub_categories().add(0, all);
            }
        }
        this.selected = selected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int count = Pref.getPreference().getInt(CART_COUNT);
        if (count > 0) {
            cartCountContainer.setVisibility(View.VISIBLE);
            cartCount.setText(String.valueOf(count));
        } else cartCountContainer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (!isMainView) {
            if (allCatalogFragment != null) {
                allCatalogFragment.resetFilter();
            }
        }
        super.onBackPressed();
        isMainView = true;
    }

    public interface CatalogPageListener {
        void onPageChange(Category selectedCategory, String path);
    }
}
