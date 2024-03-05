package com.otto.mart.ui.activity.tokoOttopay;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Cart;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.ReOrderProduct;
import com.otto.mart.model.APIModel.Request.tokoOttopay.AddToCardRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.ProductListRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.RemoveCardRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.tokoOttopay.CartResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.ReOrderProductResponse;
import com.otto.mart.presenter.dao.TokoOttopayDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.ReOrderProductAdapter;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.ReOrderProductAdapter.OnCartButtonListener;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_ADD_TO_CART;
import static com.otto.mart.keys.AppKeys.API_KEY_CART;
import static com.otto.mart.keys.AppKeys.API_KEY_REMOVE_FROM_CART;
import static com.otto.mart.keys.AppKeys.API_KEY_REORDER_PRODUCT_LIST;

public class ReOrderActivity extends AppActivity {

    public static final String KEY_STORE_DATA = "store_data";

    private LinearLayout btnBack;
    private ViewAnimator viewAnimator;
    private TextView tvTotal;
    private Button btnNext;
    private RecyclerView recyclerview;

    private String mStoreName = "";
    private int mCurrentPosition = 0;
    private List<ReOrderProduct> mProductList = new ArrayList();

    private List<Cart> mCartList = new ArrayList();

    private int mSupplierId = 0;
    public static final int MINIMUM_ORDER = 100000;
    private int mTotalOrder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder);

        initView();
        initRecyclerView();

        mSupplierId = 3;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProductList.clear();
        getCart();
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        viewAnimator = findViewById(R.id.view_animator);
        recyclerview = findViewById(R.id.recyclerview);
        tvTotal = findViewById(R.id.tv_total);
        btnNext = findViewById(R.id.btn_next);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnNext.setOnClickListener(v -> {
            if (mTotalOrder >= MINIMUM_ORDER) {
                startActivity(new Intent(this, CartActivity.class));
            } else {
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Pesanan belum memenuhi minimal order", "");
                dialog.show();
            }
        });
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);

        final LinearLayoutManager menuListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(menuListLayoutManager);
    }

    private void displayProduct(List<ReOrderProduct> data) {
        mProductList = data;

        int i = 0;
        for (ReOrderProduct product : mProductList) {
            for (Cart cart : mCartList) {
                if (cart.getSku().equalsIgnoreCase(product.getSku())) {
                    mProductList.get(i).setQty(cart.getQuantity());
                }
            }
            i++;
        }

        ReOrderProductAdapter adapter = new ReOrderProductAdapter(ReOrderActivity.this, mProductList);
        adapter.setmMinOrder(MINIMUM_ORDER);
        recyclerview.setAdapter(adapter);
        recyclerview.setItemViewCacheSize(mProductList.size());

        adapter.setCartButtonListener(new OnCartButtonListener() {
            @Override
            public void onPlusButtonCLicked(ReOrderProduct reOrderProduct, int qty, int position) {
                mCurrentPosition = position;
                //addToCart(reOrderProduct.getSku(), qty);
            }

            @Override
            public void onMinButtonCLicked(ReOrderProduct reOrderProduct, int qty, int position) {
                mCurrentPosition = position;
                //addToCart(reOrderProduct.getSku(), qty);
            }

            @Override
            public void onQtyChanged(ReOrderProduct reOrderProduct, int qty, int position) {
                mCurrentPosition = position;
                if (qty > 0) {
                    addToCart(reOrderProduct.getSku(), qty);
                } else {
                    int cartItemId = getCartItemIdBySKU(reOrderProduct.getSku());
                    if(cartItemId != 0){
                        removeFromCart(getCartItemIdBySKU(reOrderProduct.getSku()));
                    }
                }
            }
        });

        viewAnimator.setDisplayedChild(1);
    }

    private int getCartItemIdBySKU(String sku) {
        int cartItemId = 0;

        for(Cart cart: mCartList){
            if(cart.getSku().equals(sku)){
                cartItemId = cart.getCart_item_id();
            }
        }
        return cartItemId;
    }

    private void updateQuantity() {
        int i = 0;
        for (ReOrderProduct product : mProductList) {
            for (Cart cart : mCartList) {
                if (cart.getSku().equalsIgnoreCase(product.getSku())) {
                    mProductList.get(i).setQty(cart.getQuantity());
                }
            }
            i++;
        }

//        ReOrderProductAdapter adapter = (ReOrderProductAdapter) recyclerview.getAdapter();
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        }
    }

    private void displayOrderInfo() {
        mTotalOrder = 0;

        for (Cart cart : mCartList) {
            mTotalOrder += cart.getQuantity() * Integer.valueOf(cart.getItem_price());
        }
        tvTotal.setText(DataUtil.convertCurrency(mTotalOrder));
    }

    private void addToCart(String sku, int qty) {
        //ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();

        AddToCardRequest addToCardRequest = new AddToCardRequest();
        addToCardRequest.setSku(Integer.valueOf(sku));
        addToCardRequest.setQuantity(qty);

        TokoOttopayDao dao = new TokoOttopayDao(this);
        dao.getAddToCart(mStoreName, addToCardRequest, BaseDao.getInstance(this, API_KEY_ADD_TO_CART).callback);
    }

    private void removeFromCart(int cartItemId) {
        //ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();

        RemoveCardRequest removeCardRequest = new RemoveCardRequest();
        removeCardRequest.setCart_item_id(cartItemId);

        TokoOttopayDao dao = new TokoOttopayDao(this);
        dao.getRemoveFromCart(mStoreName, removeCardRequest, BaseDao.getInstance(this, API_KEY_REMOVE_FROM_CART).callback);
    }

    private void getCart() {
        new TokoOttopayDao(this).getCart(BaseDao.getInstance(this, API_KEY_CART).callback);
    }

    private void getProductList() {
        ProductListRequest productListRequest = new ProductListRequest();
        productListRequest.setSupplier_id(mSupplierId);

        new TokoOttopayDao(this).getReOrderProductList(productListRequest, BaseDao.getInstance(this, API_KEY_REORDER_PRODUCT_LIST).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_CART:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (((CartResponse) br).getData().getCart().size() > 0) {
                            mCartList = ((CartResponse) br).getData().getCart().get(0).getCart_items();
                            displayOrderInfo();
                        }

                        if (mProductList.size() > 0) {
                            updateQuantity();
                        } else {
                            getProductList();
                        }
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case API_KEY_REORDER_PRODUCT_LIST:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayProduct(((ReOrderProductResponse) br).getData().getProducts());
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case API_KEY_ADD_TO_CART:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        getCart();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case API_KEY_REMOVE_FROM_CART:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                            getCart();
                        } else {
                            ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                            dialog.show();
                        }
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
