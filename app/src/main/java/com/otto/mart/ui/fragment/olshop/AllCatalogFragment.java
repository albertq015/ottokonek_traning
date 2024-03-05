package com.otto.mart.ui.fragment.olshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.otto.mart.BuildConfig;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.Product;
import com.otto.mart.model.APIModel.Request.olshop.ProductListRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.Category;
import com.otto.mart.model.APIModel.Response.olshop.ProductListResponseModel;
import com.otto.mart.model.localmodel.ui.SortModel;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.support.util.InfiniteScroll;
import com.otto.mart.ui.Partials.RecyclerViewListener;
import com.otto.mart.ui.Partials.adapter.olshop.SlideCategoryAdapter;
import com.otto.mart.ui.Partials.decorator.GridDecorator;
import com.otto.mart.ui.activity.olshop.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;


public class AllCatalogFragment extends BaseFragment implements RecyclerViewListener, SwipeRefreshLayout.OnRefreshListener {

    private View sortButton, filterButton, categoryButton;
    private RecyclerView recyclerView;
    private List<Category> categories;
    private FilterFragment filterFragment;
    private List<Product> productList;
    private Category selectedCategory = new Category();
    private Long lowPrice, highPrice;
    private DummyAdapter adapter;
    private String path;
    private SortModel sortModel;
    private SortFragment sortFragment;
    private String query;
    private boolean isPageAvailable;
    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private ProductListRequestModel model;
    private NestedScrollView nestedScrollView;

    /*Event*/
    private ImageView ivEventBg, ivEventLogo;
    private RecyclerView rvSlideCategory;
    private SlideCategoryAdapter slideCategoryAdapter;
    private boolean isSpecialEvent = false;


    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;

    public static AllCatalogFragment newInstance() {

        Bundle args = new Bundle();
        AllCatalogFragment fragment = new AllCatalogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_catalog, container, false);

        refreshLayout = view.findViewById(R.id.swipe);
        filterFragment = FilterFragment.newInstance();
        filterFragment.setCategories(categories);
        filterFragment.setSelectedCategory(selectedCategory);
        filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> {
            filterFragment.show(getChildFragmentManager(), "filter");
        });

        model = new ProductListRequestModel();
        sortFragment = SortFragment.newInstance();
        sortButton = view.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> {
            sortFragment.setSelectedSort(sortModel);
            sortFragment.show(getChildFragmentManager(), "sort");
        });

        categoryButton = view.findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(v -> {
            CategoryChooserFragment chooserFragment = CategoryChooserFragment.newInstanse(categories, category -> {
                setSelectedCategory(category, null, null);
                return null;
            });
            chooserFragment.show(getChildFragmentManager(), "category");
        });

        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        productList = new ArrayList<>();
        adapter = new DummyAdapter(R.layout.item_product_2, productList, this);
        recyclerView = view.findViewById(R.id.productList);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridDecorator(DeviceUtil.dpToPx(8), 2, DeviceUtil.dpToPx(16)));
        recyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (!refreshLayout.isRefreshing()) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if (!refreshLayout.isRefreshing()) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                refreshLayout.setRefreshing(true);
                                model.setPage(model.getPage() + 1);
                                callProductListAPI(true);
                            }
                        }
                    }
                }
            }

        });

        refreshLayout.setOnRefreshListener(this);

        /*Event*/
        ivEventBg = view.findViewById(R.id.ivEventBg);
        ivEventLogo = view.findViewById(R.id.ivEventLogo);
        rvSlideCategory = view.findViewById(R.id.rvSlideCategory);
        rvSlideCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getIntentData();

        callProductListAPI(false);

        return view;
    }

    private void callProductListAPI(boolean isSwipeRefresh) {
        if (path != null && selectedCategory != null) {
            if (!isSwipeRefresh)
                ProgressDialogComponent.showProgressDialog(getContext(), "Memuat", false);
            new OlshopDao(this).getProductList(BaseDao.getInstance(this, 1).callback, path, getQueryMap());
        }
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        if (filterFragment != null) {
            filterFragment.setCategories(categories);
        }
    }

    public void setSelectedCategory(Category selectedCategory, Long lowPrice, Long highPrice) {
        if (selectedCategory != null) {
            this.selectedCategory = selectedCategory;
        }
//        if (lowPrice != null)
        this.lowPrice = lowPrice;

//        if (highPrice != null)
        this.highPrice = highPrice;
//        if (filterFragment != null) {
//            filterFragment.dismiss();
//        }

        if (model != null) {
            model.setPage(1);
        }

        if (recyclerView != null) {
            productList.clear();
            adapter.notifyDataSetChanged();
            callProductListAPI(false);
        }
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSort(SortModel sortModel) {
        this.sortModel = sortModel;
        if (model != null) {
            model.setPage(1);
        }
        callProductListAPI(false);
        productList.clear();
        adapter.notifyDataSetChanged();
    }

    private ProductListRequestModel getQueryMap() {
        model.setCategory_id(selectedCategory.getId());
        model.setHigh_price(highPrice);
        model.setLow_price(lowPrice);
        model.setQuery(query);

        if (sortModel == null) {
            sortModel = new SortModel();
            sortModel.setKey("newest");
        }

        model.setSort(sortModel.getKey());
        return model;
    }

    public void setQuery(String query) {
        this.query = query;
        productList.clear();
        callProductListAPI(false);
    }

    public void resetFilter() {
        model = new ProductListRequestModel();
        model.setCategory_id(selectedCategory.getId());
        model.setPage(1);
        sortModel = null;
        lowPrice = null;
        highPrice = null;
        query = null;
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        refreshLayout.setRefreshing(false);
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        if (((BaseResponseModel) br).getMeta().getCode() == 200) {
            productList.addAll(((ProductListResponseModel) br).getData().getProducts());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        super.onApiFailureCallback(message);
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }

    @Override
    public void onItemClick(int id, int pos, Object data) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        if (BuildConfig.FLAVOR.equals("development")) {
            intent.putExtra("productId", 255614);
        } else {
            intent.putExtra("productId", ((Product) data).getId());
        }
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        filterFragment = null;
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        if (model != null) {
            model.setPage(1);
        }

        productList.clear();
        adapter.notifyDataSetChanged();
        callProductListAPI(true);
    }

    private void getIntentData() {
        if (getActivity() != null) {
            isSpecialEvent = getActivity().getIntent().getBooleanExtra("isSpecialEvent", false);

            if (isSpecialEvent) {
                showSpecialEventView();
                setCategory(new ArrayList<>(categories));
                recyclerView.setPadding(
                        recyclerView.getPaddingLeft(),
                        DeviceUtil.dpToPx(32),
                        recyclerView.getPaddingRight(),
                        recyclerView.getPaddingBottom()
                );
            }
        }
    }

    private void showSpecialEventView() {
        ivEventLogo.setVisibility(View.VISIBLE);
        ivEventBg.setVisibility(View.VISIBLE);

        if (getActivity() != null) {
            if (getActivity().getIntent().hasExtra("background") && getActivity().getIntent().hasExtra("logo")) {
                Glide.with(ivEventBg).load(getActivity().getIntent().getStringExtra("background")).into(ivEventBg);
                Glide.with(ivEventLogo).load(getActivity().getIntent().getStringExtra("logo")).into(ivEventLogo);
            }
        }
    }

    private void setCategory(List<Category> categories) {
        slideCategoryAdapter = new SlideCategoryAdapter((id, pos, data) -> {
            setSelectedCategory((Category) data, lowPrice, highPrice);
            this.categories = ((Category) data).getSub_categories();
        }
                , true);

        slideCategoryAdapter.setCategoryList(categories);
        rvSlideCategory.setAdapter(slideCategoryAdapter);
    }
}
