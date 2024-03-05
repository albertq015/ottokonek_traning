package com.otto.mart.ui.fragment.olshop;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otto.mart.BuildConfig;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.Product;
import com.otto.mart.model.APIModel.Request.olshop.ProductListRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.model.APIModel.Response.olshop.ProductListResponseModel;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.otto.mart.ui.Partials.adapter.olshop.SlideCategoryAdapter;
import com.otto.mart.ui.Partials.decorator.GridDividerDecorator;
import com.otto.mart.ui.Partials.decorator.HorizontalDivider;
import com.otto.mart.ui.Partials.decorator.SpaceDecorator;
import com.otto.mart.ui.activity.olshop.CatalogActivity;
import com.otto.mart.ui.activity.olshop.ProductDetailActivity;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;


public class MainCatalogFragment extends BaseFragment implements View.OnClickListener, RecyclerViewListener {

    public final String All_PATH = "all";
    private final String DAILY_DEAL_PATH = "daily-deals";
    private final String TOP_100_PATH = "top100";
    private final String PROMO_PATH = "promotion";
    private final int DAILY_DEAL_KEY = 1;
    private final int TOP_100_KEY = 2;
    private final int PROMO_KEY = 3;
    private final int All_KEY = 4;

    private CatalogActivity.CatalogPageListener pageListener;
    private TextView allDaily, allTop, allProduct, categoryName, promoLabel;
    private RecyclerView promoList, topList, dailyList, allList, categoryList, slideCategoryList;
    private List<Category> categories = new ArrayList<>();
    private ViewGroup categoryContainer, dailyDealsContainer, top100Container;
    private ExpandableLayout categoryExpand;
    private Category selectedCategory;
    private List<Product> all, daily, top, promo;
    private DummyAdapter allAdapter, dailyAdpter, topAdapter, promoAdapter;
    private ImageView catImage;
    private SlideCategoryAdapter slideCategoryAdapter;

    public void setPageListener(CatalogActivity.CatalogPageListener pageListener) {
        this.pageListener = pageListener;
    }

    public static MainCatalogFragment newInstance(CatalogActivity.CatalogPageListener pageListener) {

        Bundle args = new Bundle();

        MainCatalogFragment fragment = new MainCatalogFragment();
        fragment.setPageListener(pageListener);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_catalog, container, false);

        initComponent(view);
        initContent();

        return view;
    }

    private void initComponent(View view) {
        allDaily = view.findViewById(R.id.allDaily);
        allTop = view.findViewById(R.id.allTop);
        allProduct = view.findViewById(R.id.all);
        promoLabel = view.findViewById(R.id.promoLabel);

        promoList = view.findViewById(R.id.promoList);
        dailyList = view.findViewById(R.id.dailyList);
        topList = view.findViewById(R.id.topList);
        allList = view.findViewById(R.id.allList);
        slideCategoryList = view.findViewById(R.id.slideCategoryList);

        categoryContainer = view.findViewById(R.id.categoryContainer);
        categoryExpand = view.findViewById(R.id.categoryExpand);
        categoryList = view.findViewById(R.id.categoryList);
        categoryName = view.findViewById(R.id.categoryName);
        catImage = view.findViewById(R.id.catImage);

        dailyDealsContainer = view.findViewById(R.id.dailyDealsContainer);
        top100Container = view.findViewById(R.id.top100Container);

        all = new ArrayList<>();
        daily = new ArrayList<>();
        promo = new ArrayList<>();
        top = new ArrayList<>();

        allAdapter = new DummyAdapter(R.layout.item_product_2, all, this);
        dailyAdpter = new DummyAdapter(R.layout.item_product_1, daily, this);
        topAdapter = new DummyAdapter(R.layout.item_product_1, top, this);
        promoAdapter = new DummyAdapter(R.layout.item_product_1, promo, this);
        slideCategoryAdapter = new SlideCategoryAdapter(this,false);

        promoList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dailyList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        allList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        slideCategoryList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        promoList.addItemDecoration(new HorizontalDivider(DeviceUtil.dpToPx(16)));
        dailyList.addItemDecoration(new HorizontalDivider(DeviceUtil.dpToPx(16)));
        topList.addItemDecoration(new HorizontalDivider(DeviceUtil.dpToPx(16)));
        allList.addItemDecoration(new GridDividerDecorator(DeviceUtil.dpToPx(8), 2));
        slideCategoryList.addItemDecoration(new SpaceDecorator(0, LinearLayout.HORIZONTAL, 0, DeviceUtil.dpToPx(16), DeviceUtil.dpToPx(16)));

        promoList.setAdapter(promoAdapter);
        dailyList.setAdapter(dailyAdpter);
        topList.setAdapter(topAdapter);
        allList.setAdapter(allAdapter);
        slideCategoryList.setAdapter(slideCategoryAdapter);

        promoList.setNestedScrollingEnabled(false);
        dailyList.setNestedScrollingEnabled(false);
        topList.setNestedScrollingEnabled(false);
        allList.setNestedScrollingEnabled(false);
    }

    private void initContent() {
        allDaily.setOnClickListener(this);
        allTop.setOnClickListener(this);
        allProduct.setOnClickListener(this);

        if (selectedCategory != null) {
//            categoryName.setText(selectedCategory.getName());
            setCategoryImage();
            slideCategoryAdapter.setCategoryList(new ArrayList<>(selectedCategory.getSub_categories()));
        }

//        MainCategoryAdapter adapter = new MainCategoryAdapter(categories, category -> {
//            selectedCategory = category;
//            categoryExpand.collapse(true);
////            categoryName.setText(category.getName());
//            callAPI();
//            setCategoryImage();
//            ProgressDialogComponent.showProgressDialog(MainCatalogFragment.this.getContext(), "Memuat", false);
//            try {
//                ((CatalogActivity) getActivity()).setSelected(category);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        categoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        categoryList.setAdapter(adapter);

//        categoryContainer.setOnClickListener(v -> {
//            if (categoryExpand.isExpanded()) {
//                categoryExpand.collapse(true);
//            } else categoryExpand.expand(true);
//        });

        getCacheData();
        callAPI();
    }

    private void getCacheData() {
        Type typeToken = new TypeToken<ArrayList<Product>>() {
        }.getType();
        List<Product> cAll = new Gson().fromJson(getStringPref(All_KEY), typeToken);
        if (cAll != null) {
            all.clear();
            all.addAll(cAll);
        }

        List<Product> cPromo = new Gson().fromJson(getStringPref(PROMO_KEY), typeToken);
        if (cPromo != null) {
            promo.clear();
            promo.addAll(cPromo);
        }

        List<Product> cDaily = new Gson().fromJson(getStringPref(DAILY_DEAL_KEY), typeToken);
        if (cDaily != null) {
            daily.clear();
            daily.addAll(cDaily);
            if (cDaily.size() > 0) dailyDealsContainer.setVisibility(View.VISIBLE);
        }

        List<Product> cTop = new Gson().fromJson(getStringPref(TOP_100_KEY), typeToken);
        if (cTop != null) {
            top.clear();
            top.addAll(cTop);
            if (cTop.size() > 0) top100Container.setVisibility(View.VISIBLE);
        }

    }

    private String getStringPref(int key) {
        String s = Pref.getPreference().getString(String.valueOf(selectedCategory.getId()) + String.valueOf(key));
        return s != null ? s : "";
    }

    private void setCategoryImage() {
        Glide.with(catImage.getContext()).load("http://ottopay-mart.clappingape.com" + selectedCategory.getIcon()).into(catImage);
    }

    private void callAPI() {
        callProductListAPI(All_PATH, All_KEY);
        callProductListAPI(DAILY_DEAL_PATH, DAILY_DEAL_KEY);
//        callProductListAPI(PROMO_PATH, PROMO_KEY);
        callProductListAPI(TOP_100_PATH, TOP_100_KEY);
    }

    private void callProductListAPI(String path, int resCode) {
        if (selectedCategory != null) {
            new OlshopDao(this).getProductList(BaseDao.getInstance(this, resCode).callback, path, new ProductListRequestModel(selectedCategory.getId()));
        }
//        new OlshopDao(this).getProductList(BaseDao.getInstance(this, resCode).callback, path, new ProductListRequestModel(selectedCategory.getId()));
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public void onClick(View v) {
        String path = "";
        if (v.getId() == R.id.allDaily) path = DAILY_DEAL_PATH;
        else if (v.getId() == R.id.allTop) path = TOP_100_PATH;
        else if (v.getId() == R.id.all) path = All_PATH;

        if (pageListener != null) {
            pageListener.onPageChange(selectedCategory, path);
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        if (br != null) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {

                List<Product> productList = new ArrayList<>();
                if (((ProductListResponseModel) br).getData().getProducts() != null) {
                    productList = ((ProductListResponseModel) br).getData().getProducts();
                }

                switch (responseCode) {
                    case All_KEY: {
                        all.clear();
                        all.addAll(productList);
                        Pref.getPreference().putString(selectedCategory.getId() + String.valueOf(All_KEY), new Gson().toJson(productList));
                        allAdapter.notifyDataSetChanged();
                        break;
                    }
                    case DAILY_DEAL_KEY: {
                        daily.clear();
                        daily.addAll(productList);
                        Pref.getPreference().putString(selectedCategory.getId() + String.valueOf(DAILY_DEAL_KEY), new Gson().toJson(productList));

                        if (daily.size() > 0) dailyDealsContainer.setVisibility(View.VISIBLE);
                        else dailyDealsContainer.setVisibility(View.GONE);
                        dailyAdpter.notifyDataSetChanged();
                        break;
                    }
                    case PROMO_KEY: {
                        promo.clear();
                        promo.addAll(productList);
                        Pref.getPreference().putString(selectedCategory.getId() + String.valueOf(PROMO_KEY), new Gson().toJson(productList));
                        if (productList.size() > 0) {
                            promoLabel.setVisibility(View.VISIBLE);
                            promoList.setVisibility(View.VISIBLE);
                        } else {
                            promoLabel.setVisibility(View.GONE);
                            promoList.setVisibility(View.GONE);
                        }
                        promoAdapter.notifyDataSetChanged();
                        break;
                    }
                    case TOP_100_KEY: {
                        top.clear();
                        top.addAll(productList);
                        Pref.getPreference().putString(selectedCategory.getId() + String.valueOf(TOP_100_KEY), new Gson().toJson(productList));

                        if (top.size() > 0) top100Container.setVisibility(View.VISIBLE);
                        else top100Container.setVisibility(View.GONE);
                        topAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        super.onApiFailureCallback(message);
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }

    @Override
    public void onItemClick(int id, int pos, Object data) {
        if (id==99){
            Category category=(Category)data;
            pageListener.onPageChange(category, All_PATH);
        }else {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            if (BuildConfig.FLAVOR.equals("development")) {
                intent.putExtra("productId", 268024);
            } else {
                intent.putExtra("productId", ((Product) data).getId());
            }
            startActivity(intent);
        }
    }
}
