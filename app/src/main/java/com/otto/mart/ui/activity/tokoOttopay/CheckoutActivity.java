package com.otto.mart.ui.activity.tokoOttopay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Cart;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderConfirmRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderPayOffRequest;
import com.otto.mart.model.APIModel.Request.tokoOttopay.OrderPaymentRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.model.APIModel.Response.tokoOttopay.CartResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderConfirmResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderPayOffResponse;
import com.otto.mart.model.APIModel.Response.tokoOttopay.OrderPaymentResponse;
import com.otto.mart.presenter.dao.TokoOttopayDao;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.widget.recyclerView.ItemClickSupport;
import com.otto.mart.ui.Partials.adapter.tokoOttopay.CheckoutAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_CART;
import static com.otto.mart.keys.AppKeys.API_KEY_ORDER_CONFIRM;
import static com.otto.mart.keys.AppKeys.API_KEY_ORDER_PAYMENT;
import static com.otto.mart.keys.AppKeys.API_KEY_ORDER_PAY_OFF;

public class CheckoutActivity extends AppActivity {

    private final int KET_API_WALLET_INFO = 100;

    private LinearLayout btnBack;
    private TextView tvAccountNumber;
    private TextView tvAccountName;
    private TextView tvBalance;
    private TextView tvWalletName;
    private ExpandableLayout eplBayarSekarang;
    private TextView tvTotalPembayaran;
    private RecyclerView recyclerview;
    private Button btnBayarSekarang;


    private int mWalletId = 0;
    private long mWalletBalance = 0;
    private long mTotalOrder = 0;
    private int mSupplierId = 0;
    private String mPinCode = "";
    List<Integer> mOrderItemList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mSupplierId = 3;

        initView();
        initRecyclerView();
        callWalletInfo();
        getCart();
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        recyclerview = findViewById(R.id.recyclerview);
        tvAccountNumber = findViewById(R.id.tv_account_number);
        tvAccountName = findViewById(R.id.tv_account_name);
        tvBalance = findViewById(R.id.tv_balance);
        tvWalletName = findViewById(R.id.tv_wallet_name);
        tvTotalPembayaran = findViewById(R.id.tv_total_pembayaran);
        eplBayarSekarang = findViewById(R.id.epl_bayar_sekarang);
        btnBayarSekarang = findViewById(R.id.btn_bayar_sekarang);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnBayarSekarang.setOnClickListener(v -> {
            bayarSekarang();
        });
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);

        final LinearLayoutManager menuListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(menuListLayoutManager);
        recyclerview.setNestedScrollingEnabled(false);
    }

    private void displayWalletInfo(WalletDataModel model) {
        mWalletId = model.getId();
        mWalletBalance = DataUtil.convertLongFromCurrency(model.getBalance());

        tvWalletName.setText(getString(R.string.text_ottopay));
        tvBalance.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getBalance())));
        tvAccountName.setText(model.getOwner_name());
        tvAccountNumber.setText(DataUtil.getXXXPhone(model.getAccount_number()));
        eplBayarSekarang.setExpanded(true);
    }

    private void displayProduct(List<Cart> data) {
        CheckoutAdapter adapter = new CheckoutAdapter(CheckoutActivity.this, data);
        recyclerview.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerview).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });
    }

    private void displayTotalPaymentInfo(List<Cart> data) {
        mTotalOrder = 0;

        for (Cart cart : data) {
            mTotalOrder += cart.getQuantity() * Integer.valueOf(cart.getItem_price());
        }

        tvTotalPembayaran.setText((DataUtil.convertCurrency(mTotalOrder)));

        orderConfirm(data);
    }

    private void bayarSekarang() {
        if(mWalletBalance >= mTotalOrder){
            Intent jenk = new Intent(CheckoutActivity.this, RegisterPinResetActivity.class);
            jenk.putExtra("confirm", true);
            startActivityForResult(jenk, AppKeys.KEY_PIN_COFIRMATION);
        } else {
            showErrorMessage("Saldo Tidak Mencukupi", "Silahkan melakukan Top Up saldo dompet anda!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case AppKeys.KEY_PIN_COFIRMATION:
                    mPinCode = data.getStringExtra(AppKeys.KEY_PIN_CODE);
                    orderPayment();
                    break;
                default:
                    break;
            }
        }
    }


    //region api requet

    private void callWalletInfo() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this, KET_API_WALLET_INFO).callback);
    }

    private void getCart() {
        new TokoOttopayDao(this).getCart(BaseDao.getInstance(this, API_KEY_CART).callback);
    }

    private void orderConfirm(List<Cart> cartItems) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);

        mOrderItemList.clear();

        for (Cart cart : cartItems) {
            mOrderItemList.add(cart.getCart_item_id());
        }

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setSupplier_id(mSupplierId);
        orderConfirmRequest.setCart_item_ids(mOrderItemList);

        new TokoOttopayDao(this).getOrderConfirm(orderConfirmRequest, BaseDao.getInstance(this, API_KEY_ORDER_CONFIRM).callback);
    }

    private void orderPayment() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);

        OrderPaymentRequest orderPaymentRequest = new OrderPaymentRequest();
        orderPaymentRequest.setWallet_id(mWalletId);
        orderPaymentRequest.setCart_item_ids(mOrderItemList);
        orderPaymentRequest.setAmount(mTotalOrder);
        orderPaymentRequest.setSupplier_id(mSupplierId);
        orderPaymentRequest.setPin(mPinCode);

        new TokoOttopayDao(this).getOrderPayment(orderPaymentRequest, BaseDao.getInstance(this, API_KEY_ORDER_PAYMENT).callback);
    }

    private void orderPayOff(List<WidgetBuilderModel> keyValueList) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);

        String orderNumber = "";

        for (WidgetBuilderModel data : keyValueList) {
            if (data.getKey().equalsIgnoreCase("No Order")) {
                orderNumber = data.getValue();
            }
        }

        OrderPayOffRequest orderPayOffRequest = new OrderPayOffRequest();
        orderPayOffRequest.setOrder_number(orderNumber);
        orderPayOffRequest.setWallet_id(mWalletId);
        orderPayOffRequest.setAmount(mTotalOrder);
        orderPayOffRequest.setPin(mPinCode);

        new TokoOttopayDao(this).getOrderPayOff(orderPayOffRequest, BaseDao.getInstance(this, API_KEY_ORDER_PAY_OFF).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case KET_API_WALLET_INFO:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        WalletResponseModel res = (WalletResponseModel) br;
                        if (res.getData() != null && res.getData().size() > 0) {
                            WalletDataModel model = res.getData().get(0);
                            displayWalletInfo(model);
                        }
                    }
                    break;
                case API_KEY_CART:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayProduct(((CartResponse) br).getData().getCart().get(0).getCart_items());
                        displayTotalPaymentInfo(((CartResponse) br).getData().getCart().get(0).getCart_items());
                    }
                    break;
                case API_KEY_ORDER_CONFIRM:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        //Order Confirm Success
                        OrderConfirmResponse orderConfirmResponse = (OrderConfirmResponse) br;
                    } else {
                        showErrorMessage("Terjadi Kesalahan", ((BaseResponseModel) br).getMeta().getMessage());
                    }
                    break;
                case API_KEY_ORDER_PAYMENT:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        //orderPayOff(((OrderPaymentResponse) br).getData().getKey_value_list());

                        Intent intent = new Intent(this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        Intent successIntent = new Intent(this, PayQRDetailActivity.class);
                        successIntent.putExtra("data", (ArrayList<? extends Parcelable>) ((OrderPaymentResponse) br).getData().getKey_value_list());
                        successIntent.putExtra(AppKeys.KEY_ORDER_PAYMENT_SUCCESS, true);
                        startActivity(successIntent);
                        finish();
                    }
                    break;
                case API_KEY_ORDER_PAY_OFF:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Intent intent = new Intent(this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        Intent successIntent = new Intent(this, PayQRDetailActivity.class);
                        successIntent.putExtra("data", (ArrayList<? extends Parcelable>) ((OrderPayOffResponse) br).getData().getKey_value_list());
                        successIntent.putExtra(AppKeys.KEY_ORDER_PAYMENT_SUCCESS, true);
                        startActivity(successIntent);
                        finish();
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

    private void showErrorMessage(String title, String message) {
        ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, title, message);
        errorDialog.show();
        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    //endregion

}