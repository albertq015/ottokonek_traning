package com.otto.mart.ui.activity.olshop;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.OrderConfirmationRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.cart.UpdateCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.cart.UpdateItem;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.cart.CartResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartGroup;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartItem;
import com.otto.mart.model.APIModel.Response.olshop.order.ConfirmOrderResponseModel;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.Partials.adapter.olshop.CartAdapter;
import com.otto.mart.ui.Partials.adapter.olshop.ItemSelectedListener;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;

import static com.otto.mart.GLOBAL.CART_COUNT;

public class CartOlshopActivity extends AppActivity implements CartAdapter.CartItemListener {

    private final int CART_LIST_RC = 1;
    private final int CART_DELETE_RC = 2;
    private final int CART_UPDATE_RC = 3;
    private final int CONFIRM_ORDER_RC = 4;

    private RecyclerView cartList;
    private TextView grandTotal, dialogTitle, buttonBuy, title;
    private CartAdapter adapter;
    private int parentPos;
    private int childPos;
    private View chooseAll, backhitbox;
    private CheckBox cbChooseAll;
    private LazyDialog confirmDialog;
    private SupplierCartItem selectedItem;
    private PublishSubject<UpdateItem> publishSubject;
    private CartResponseModel.AddCartResponseData cartData;
    private CompoundButton.OnCheckedChangeListener checkListener;
    private long total = 0;
    private ArrayList<AddCartRequestModel> confirmOrderList;
    private LazyDialog errorDialog;
    private TextView errorTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_olshop_cart);

        cartList = findViewById(R.id.cartList);
        grandTotal = findViewById(R.id.grandTotal);
        chooseAll = findViewById(R.id.chooseAll);
        cbChooseAll = findViewById(R.id.cbChooseAll);
        buttonBuy = findViewById(R.id.buttonBuy);
        backhitbox = findViewById(R.id.btnToolbarBack);
        title = findViewById(R.id.tvToolbarTitle);

        title.setText(getString(R.string.text_cart));

        cartList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, new ItemSelectedListener() {

            @Override
            public void isSelectedAll(boolean isSelected) {
                cbChooseAll.setOnCheckedChangeListener(null);
                cbChooseAll.setChecked(isSelected);
                cbChooseAll.setOnCheckedChangeListener(checkListener);
            }

            @Override
            public void onItemChange(long before, long after, boolean isChecked, int parentPos, int adapterPosition) {
//                if (total - before >= 0) {
//                    total -= before;
//                }
//                total += after;
                cartData.getCart().get(parentPos).getCart_items().get(adapterPosition).setSelected(isChecked);

                total = 0;
                for (SupplierCartGroup supplierCartGroup : cartData.getCart()) {
                    for (SupplierCartItem cart_item : supplierCartGroup.getCart_items()) {
                        if (cart_item.isSelected()) {
                            total += cart_item.getPrice() * cart_item.getQuantity();
                        }
                    }
                }

                grandTotal.setText(DataUtil.convertCurrency(total));
            }
        });
        cartList.setHasFixedSize(true);
        cartList.setAdapter(adapter);
        cartList.setNestedScrollingEnabled(false);

        checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                total = 0;
                for (SupplierCartGroup supplierCartGroup : cartData.getCart()) {
                    for (SupplierCartItem cart_item : supplierCartGroup.getCart_items()) {
                        if (cart_item.isSelected()) {
                            total += cart_item.getPrice() * cart_item.getQuantity();
                        }
                    }

                }

                grandTotal.setText(DataUtil.convertCurrency(total));
                adapter.setChecked(isChecked);
            }
        };

        chooseAll.setOnClickListener(v -> cbChooseAll.performClick());
        cbChooseAll.setOnCheckedChangeListener(checkListener);

        callCartAPI();
        publishSubject = PublishSubject.create();
        iniDialog();
        initErrorDialog();
        initItemObserver();

        buttonBuy.setOnClickListener(v -> confirmOrder());
        backhitbox.setOnClickListener(v -> finish());
    }

    private void callCartAPI() {
        ProgressDialogComponent.showProgressDialog(this, "Mohon Tunggu", false);
        new OlshopDao(this).getCartList(BaseDao.getInstance(this, CART_LIST_RC).callback);
    }

    private void iniDialog() {
        confirmDialog = new LazyDialog(this, this, true, true);
        confirmDialog.setContainerView(LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null));
        dialogTitle = confirmDialog.findViewById(R.id.dialogTvTitle);
        dialogTitle.setText(getString(R.string.text_delete_selected_item));
        confirmDialog.findViewById(R.id.negajing).setOnClickListener(v -> {
            confirmDialog.dismiss();
        });
        confirmDialog.findViewById(R.id.posijing).setOnClickListener(v -> {
            ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
            new OlshopDao(this).deleteCart(String.valueOf(selectedItem.getCart_item_id()), BaseDao.getInstance(this, CART_DELETE_RC).callback);
            confirmDialog.dismiss();
        });
    }

    private void initErrorDialog() {
        errorDialog = new LazyDialog(this, this, true, true);
        View errorView = LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null);
        errorTitle = errorView.findViewById(R.id.dialogTvTitle);
        errorDialog.setContainerView(errorView);
        errorView.findViewById(R.id.negajing).setVisibility(View.GONE);
        ((TextView) errorView.findViewById(R.id.posijing)).setText(getString(R.string.text_ok));
        errorView.findViewById(R.id.posijing).setOnClickListener(v -> errorDialog.dismiss());
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                callCartAPI();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void initItemObserver() {
        publishSubject = PublishSubject.create();
        publishSubject
                .debounce(900, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartData -> {
                    new OlshopDao(CartOlshopActivity.this).updateCart(
                            new UpdateCartRequestModel(Collections.singletonList(cartData)),
                            BaseDao.getInstance(CartOlshopActivity.this, CART_UPDATE_RC).callback);
                    ProgressDialogComponent.showProgressDialog(CartOlshopActivity.this, "Mohon tunggu", false);
                });
    }

    private void confirmOrder() {
        AddCartRequestModel requestModel;
        confirmOrderList = new ArrayList<>();
        for (SupplierCartGroup group : adapter.getSupplier()) {
            for (int i = 0; i < group.getCart_items().size(); i++) {
                if (group.getCart_items().get(i).isSelected()) {
                    if (group.getCart_items().get(i).getQuantity() <= 0) {
                        Toast.makeText(this, "Jumlah barang min 1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    requestModel = new AddCartRequestModel();
                    requestModel.setSku(group.getCart_items().get(i).getSku());
                    requestModel.setSales_commission((long) Double.parseDouble(group.getCart_items().get(i).getSales_commission()));
                    requestModel.setQuantity(group.getCart_items().get(i).getQuantity());
                    requestModel.setProduct_id(group.getCart_items().get(i).getProduct_id());
                    requestModel.setId(group.getCart_items().get(i).getCart_item_id());
                    confirmOrderList.add(requestModel);
                }
            }
        }

        if (confirmOrderList.size() > 0) {
            ProgressDialogComponent.showProgressDialog(this, "Mohon Tunggu", false);
            new OlshopDao(this).orderConfirmation(new OrderConfirmationRequestModel(confirmOrderList), BaseDao.getInstance(this, CONFIRM_ORDER_RC).callback);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Pilih produk yang akan dibayar", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (br != null) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                if (responseCode == CART_LIST_RC) {
                    handleCartData((CartResponseModel) br);
                } else if (responseCode == CART_DELETE_RC) {
                    handleDeletedCart((CartResponseModel) br);
                } else if (responseCode == CART_UPDATE_RC) {
//                handleCartData((CartResponseModel) br);
                } else if (responseCode == CONFIRM_ORDER_RC) {
                    handleConfirmOrder((ConfirmOrderResponseModel) br);
                }
            } else {
                if (responseCode == CONFIRM_ORDER_RC) {
                    errorTitle.setText(((BaseResponseModel) br).getMeta().getMessage());
                    errorDialog.show();
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(this, this, true, true, "Pemberitahuan", ((BaseResponseModel) br).getMeta().getMessage());
                    errorDialog.show();
                }
            }
        }
    }

    private void handleConfirmOrder(ConfirmOrderResponseModel br) {
        Intent intent = new Intent(this, CheckoutOlshopActivity.class);
        intent.putExtra("cartData", new Gson().toJson(cartData));
        intent.putExtra("data", new Gson().toJson(br));
        startActivity(intent);
    }

    private void handleDeletedCart(CartResponseModel cart) {
        SupplierCartItem product = adapter.getItem(parentPos, childPos);
        if (total - product.getPrice() * product.getQuantity() >= 0 && product.isSelected()) {
            total -= product.getPrice() * product.getQuantity();
        }
        int count = 0;
        for (SupplierCartGroup group : cart.getData().getCart()) count += group.getTotal_item();
        grandTotal.setText(DataUtil.convertCurrency(total));
        adapter.deleteItem(parentPos, childPos);
        Pref.getPreference().putInt(CART_COUNT, count);
//        cartData = cart.getData();
        long total = 0;
        List<SupplierCartGroup> cartList = cart.getData().getCart();
//        for (SupplierCartGroup item : cartList) {
//            double price = Double.parseDouble(item.getSub_total());
//            total += price;
//        }
    }

    private void handleCartData(CartResponseModel cart) {
        cartData = cart.getData();
        int count = 0;
        for (SupplierCartGroup group : cart.getData().getCart()) count += group.getTotal_item();
        Pref.getPreference().putInt(CART_COUNT, count);
        List<SupplierCartGroup> cartList = cart.getData().getCart();
        if (cartList != null) {
            adapter.setSupplier(cartList);
            long total = 0;
//            for (SupplierCartGroup item : cartList) {
//                double price = Double.parseDouble(item.getSub_total());
//                total += price;
//            }
//            grandTotal.setText(DataUtil.convertCurrency(total));
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        super.onApiFailureCallback(message, ac);
        ProgressDialogComponent.dismissProgressDialog(this);
    }

    @Override
    public void onItemDelete(int parentPos, int childPos, Object data) {
        this.parentPos = parentPos;
        this.childPos = childPos;
        selectedItem = (SupplierCartItem) data;
        confirmDialog.show();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onItemChanged(int cartItemId, int quantity) {
        publishSubject.onNext(new UpdateItem(cartItemId, quantity));
    }

    @Override
    public void onItemChecked(int pos, boolean isChecked, int parentPos) {
        adapter.getSupplier().get(parentPos).getCart_items().get(pos).setSelected(isChecked);
    }
}
