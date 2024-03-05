package com.otto.mart.ui.activity.tokoOttopay;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Cart;
import com.otto.mart.model.APIModel.Request.tokoOttopay.AddToCardRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.RemoveCardRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.tokoOttopay.CartResponse;
import com.otto.mart.presenter.dao.TokoOttopayDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.widget.recyclerView.ItemClickSupport;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.CartAdapter;
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
import static com.otto.mart.ui.activity.tokoOttopay.ReOrderActivity.MINIMUM_ORDER;

public class CartActivity extends AppActivity {

    public static final String KEY_STORE_DATA = "store_data";

    private LinearLayout btnBack;
    private CheckBox checkBox;
    private ViewAnimator viewAnimator;
    private TextView tvTotal;
    private Button btnNext;
    private RecyclerView recyclerview;

    private String mStoreName = "";

    private int mTotalOrder = 0;
    private int mCurrentPosition = 0;
    private List<Cart> mCartList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        initRecyclerView();

        getCart();
        displayOrderInfo();
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        viewAnimator = findViewById(R.id.view_animator);
        checkBox = findViewById(R.id.checkbox);
        recyclerview = findViewById(R.id.recyclerview);
        tvTotal = findViewById(R.id.tv_total);
        btnNext = findViewById(R.id.btn_next);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        checkBox.setOnClickListener(v -> {
            selectAllProduct();
        });

        btnNext.setOnClickListener(v -> {
            if (mTotalOrder >= MINIMUM_ORDER) {
                startActivity(new Intent(this, CheckoutActivity.class));
            } else {
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Pesanan belum memenuhi minimal order", "");
                dialog.show();
            }
        });
    }

    private void selectAllProduct() {
        int i = 0;
        for (Cart cart : mCartList) {
            mCartList.get(i).setSelected(checkBox.isChecked());
            i++;
        }

        CartAdapter adapter = (CartAdapter) recyclerview.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);

        final LinearLayoutManager menuListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(menuListLayoutManager);
        recyclerview.setNestedScrollingEnabled(false);
    }

    private void displayProduct(List<Cart> data) {
        mCartList = data;

        CartAdapter adapter = new CartAdapter(CartActivity.this, data);
        recyclerview.setAdapter(adapter);

        adapter.setCartButtonListener(new CartAdapter.OnCartButtonListener() {
            @Override
            public void onPlusButtonCLicked(Cart cart, int qty, int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onMinButtonCLicked(Cart cart, int qty, int position) {
                mCurrentPosition = position;
                //removeFromCart(cart.getCart_item_id());
            }

            @Override
            public void deleteClicked(Cart item, int position) {
                removeFromCart(mCartList.get(position).getCart_item_id());
            }

            @Override
            public void checkBoxClicked(Cart item, int position) {
//                mCartList.get(position).setSelected(!mCartList.get(position).isSelected());
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onQtyChanged(Cart cart, int qty, int position) {
                mCurrentPosition = position;
                if (qty > 0) {
                    addToCart(cart.getSku(), qty);
                } else {
                    removeFromCart(cart.getCart_item_id());
                }
            }
        });

        ItemClickSupport.addTo(recyclerview).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });

        checkBox.performClick();
        viewAnimator.setDisplayedChild(1);
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

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_CART:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (((CartResponse) br).getData().getCart().get(0).getCart_items().size() > 0) {
                            displayProduct(((CartResponse) br).getData().getCart().get(0).getCart_items());
                            displayOrderInfo();
                        } else {
                            finish();
                        }
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
                        }
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
