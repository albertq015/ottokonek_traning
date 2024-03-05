package com.otto.mart.ui.activity.olshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.PaymentData;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.AddressRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ConfirmPaid;
import com.otto.mart.model.APIModel.Request.olshop.PaymentJurnalRequestModel;
import com.otto.mart.model.APIModel.Request.olshop.ShippingCostRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel;
import com.otto.mart.model.APIModel.Response.WalletResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.MailNoResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.cart.SupplierCartItem;
import com.otto.mart.model.APIModel.Response.olshop.confirm.FinalConfirmOrderResponse;
import com.otto.mart.model.APIModel.Response.olshop.order.ConfirmOrderResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingCostResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.order.ShippingItem;
import com.otto.mart.model.APIModel.Response.olshop.payment.PaymentJournalOlshopResponseModel;
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse;
import com.otto.mart.model.localmodel.ppob.PpobPayment;
import com.otto.mart.presenter.dao.WalletDao;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.presenter.dao.olshop.PaymentDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.Partials.adapter.olshop.CheckoutAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyEdittext;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.olshop.ShippingFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.GLOBAL.CART_COUNT;
import static com.otto.mart.GLOBAL.KEY_API_ORDER_CONFIRMATION;
import static com.otto.mart.GLOBAL.KEY_API_PAYMENT_JURNAL;
import static com.otto.mart.ui.activity.profile.AddEditAddressActivity.KEY_CHECKOUT_ADD_ADDRESS;

public class CheckoutOlshopActivity extends AppActivity implements View.OnClickListener {

    private final int KEY_ADD_ADDRESS_REQUEST_CODE = 100;
    private final int KEY_PIN_REQUEST_CODE = 101;
    private final int KEY_SHIPPING_REQUEST_CODE = 102;
    private final int KEY_CONFIRM_PAID_CODE = 103;
    private final int KEY_MAIL_NO_CODE = 104;
    private final int KEY_WALLET = 105;

    private View backhitbox, changeAddress;
    private NestedScrollView mainScrollview;
    private LazyEdittext leNamaPelanggan;
    private LazyEdittext leNoTelepon, email;
    private TextView tvAddressName;
    private TextView tvAddress;
    private LinearLayout addressLayout;
    private TextView tvSubtotalProduct;
    private TextView tvSubtotalPengiriman;
    private TextView tvTotalPembayaran;
    private TextView tvBalance;
    private TextView tvWalletName;
    private TextView title;
    private TextView tvSNK;
    private Button btnNext;
    private Button btnBayarSekarang;
    private RecyclerView recyclerview;

    private ExpandableLayout eplBayarSekarang;
    private LinearLayoutManager layoutManager;

    private ConfirmOrderResponseModel mCartData;
    private AddressRequestModel mSelectedAddress = null;
    private int shippingPosition;
    private int parentPos;
    private CheckoutAdapter adapter;
    private ConfirmPaid confirmPaid;
    private String mailNo;
    private LazyEdittext customerName, customerPhone;
    private int shippingCost = 0;
    private long subTotal = 0;
    private int paymentId;
    private long total = 0;
    private LazyDialog tncDialog;
    private LazyDialog errorDialog;
    private TextView errorTitle;
    private long currentWalletBalance;
    private ShippingAddressData addressResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_olshop_checkout);

        if (getIntent().getStringExtra("data") != null) {
            String cartDataJSON = getIntent().getStringExtra("data");
            mCartData = new Gson().fromJson(cartDataJSON, ConfirmOrderResponseModel.class);
        } else {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_LONG).show();
            finish();
        }

        initView();
        callWalletInfo();
        displayContent();
        displayOrderDetail();
        initErrorDialog();
        buildTNCDialog();
    }

    private void initView() {
        backhitbox = findViewById(R.id.btnToolbarBack);
        mainScrollview = findViewById(R.id.main_scrollview);
        leNamaPelanggan = findViewById(R.id.le_nama_pelanggan);
        leNoTelepon = findViewById(R.id.le_no_telepon);
        tvAddressName = findViewById(R.id.tv_address_name);
        tvAddress = findViewById(R.id.tv_address);
        addressLayout = findViewById(R.id.address_layout);
        tvSubtotalProduct = findViewById(R.id.tv_subtotal_product);
        tvSubtotalPengiriman = findViewById(R.id.tv_subtotal_pengiriman);
        tvTotalPembayaran = findViewById(R.id.tv_total_pembayaran);
        btnNext = findViewById(R.id.btn_next);
        tvBalance = findViewById(R.id.tv_balance);
        tvWalletName = findViewById(R.id.tv_wallet_name);
        eplBayarSekarang = findViewById(R.id.epl_bayar_sekarang);
        btnBayarSekarang = findViewById(R.id.btn_bayar_sekarang);
        email = findViewById(R.id.email);
        title = findViewById(R.id.tvToolbarTitle);
        changeAddress = findViewById(R.id.changeAddress);
        tvSNK = findViewById(R.id.tvSNK);

        title.setText(getString(R.string.text_checkout));
        tvSNK.setText(Html.fromHtml(getString(R.string.msg_tnc_confirmation)));

        recyclerview = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setNestedScrollingEnabled(false);

        backhitbox.setOnClickListener(this);
        tvSNK.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnBayarSekarang.setOnClickListener(this);
        changeAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == backhitbox.getId()) {
            onBackPressed();
        } else if (id == addressLayout.getId()) {
            gotoAddAddress();
        } else if (id == btnNext.getId()) {
            eplBayarSekarang.setExpanded(true, true);
            mainScrollview.fullScroll(View.FOCUS_DOWN);
        } else if (id == btnBayarSekarang.getId()) {
            if ((subTotal + shippingCost) <= currentWalletBalance) {
                if (orderValidation("")) {
                    openPinConfirm();
                }
            } else {
                ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, "Saldo tidak mencukupi", "");
                errorDialog.show();
            }
        } else if (id == R.id.changeAddress) {
            gotoAddAddress();
        } else if (id == R.id.tvSNK) {
            gotoSnkPage();
        }
    }

    private void gotoSnkPage() {
        Intent intent = new Intent(this, SnKActivity.class);
        intent.putExtra(SnKActivity.KEY_URL_CONTENT, "https://opv3.s3-ap-southeast-1.amazonaws.com/toko-online/tnc.html");
        startActivity(intent);
    }

    private void openPinConfirm() {
        Intent jenk = new Intent(CheckoutOlshopActivity.this, RegisterPinResetActivity.class);
        jenk.putExtra("confirm", true);
        startActivityForResult(jenk, KEY_PIN_REQUEST_CODE);
    }

    private void displayContent() {
        leNamaPelanggan.setContentText("");
        leNoTelepon.setContentText("");
        tvAddressName.setText(getString(R.string.text_choose_shipping_address));
        tvAddress.setText("-");

        for (SupplierCartItem supplierCartItem : mCartData.getData().getCart_items()) {
            long commission = (long) Double.parseDouble(supplierCartItem.getSales_commission());
            long price = supplierCartItem.getOttomart_discount_price() > 0 ? Math.min(supplierCartItem.getOttomart_price(), supplierCartItem.getOttomart_discount_price()) : supplierCartItem.getOttomart_price();
            subTotal += (price + commission) * supplierCartItem.getQuantity();
        }

        showTotal();

        eplBayarSekarang.setExpanded(true);
    }

    private void showTotal() {
        tvSubtotalProduct.setText(DataUtil.convertCurrency(subTotal));
        tvSubtotalPengiriman.setText(DataUtil.convertCurrency(shippingCost));
        tvTotalPembayaran.setText(DataUtil.convertCurrency(subTotal + shippingCost));
    }

    private void displayOrderDetail() {
        adapter = new CheckoutAdapter(mCartData.getData().getCart_items());
        recyclerview.setAdapter(adapter);
        adapter.setItemClickListener(new CheckoutAdapter.ItemClickListener() {

            @Override
            public void isCheckedInsurance(long insuranceCost) {
                calculateShippingCost();
            }

            @Override
            public void shippingClicked(SupplierCartItem item, int parent, int position) {
                parentPos = parent;
                shippingPosition = position;
                callShippingCost(item, position);
            }
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
    }


    private void callShippingCost(SupplierCartItem item, int position) {
        if (mailNo != null) {
            ShippingCostRequestModel model = new ShippingCostRequestModel(
                    item.getProduct_id(),
                    item.getQuantity(),
                    String.valueOf(item.getSku()),
                    mailNo
            );
            ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
            new OlshopDao(this).shippingCost(model, BaseDao.getInstance(this, KEY_SHIPPING_REQUEST_CODE).callback);
        } else
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.text_input_address_first), Snackbar.LENGTH_SHORT).show();
    }

    private void gotoAddAddress() {
        Intent intent = new Intent(this, ShippingAddressActivity.class);
        intent.putExtra(KEY_CHECKOUT_ADD_ADDRESS, true);
        if (mSelectedAddress != null) {
            intent.putExtra("address", mSelectedAddress);
            intent.putExtra("addressId", mSelectedAddress.getId());
        }

        startActivityForResult(intent, KEY_ADD_ADDRESS_REQUEST_CODE);
    }

    private void displayAddress() {
        tvAddressName.setText(mSelectedAddress.getAddressName() + "\n\n" + mSelectedAddress.getAddress());
        tvAddress.setText(mSelectedAddress.getPostalCode());
        String builder = mSelectedAddress.getProvince() + " - " +
                "" + mSelectedAddress.getDistrict() + " - " + mSelectedAddress.getCity() + " - " + mSelectedAddress.getPostalCode();
        tvAddress.setText(builder);
    }

    private void displayWalletInfo(WalletDataModel model) {
        tvWalletName.setText(getString(R.string.text_deposit));
        tvBalance.setText(DataUtil.convertCurrency(DataUtil.convertLongFromCurrency(model.getBalance())));
        currentWalletBalance = DataUtil.convertLongFromCurrency(model.getBalance());
    }

    private boolean isValid() {
        String receiverName = leNamaPelanggan.getTextContent();
        String receiverPhone = leNoTelepon.getTextContent();

        if (receiverName.equals("")) {
            Toast.makeText(CheckoutOlshopActivity.this, getString(R.string.text_recipient_name_mandatory), Toast.LENGTH_LONG).show();
            return false;
        } else if (receiverPhone.equals("")) {
            Toast.makeText(CheckoutOlshopActivity.this, getString(R.string.text_phone_mandatory), Toast.LENGTH_LONG).show();
            return false;
        } else if (mSelectedAddress == null) {
            Toast.makeText(CheckoutOlshopActivity.this, getString(R.string.text_add_shipping_address), Toast.LENGTH_LONG).show();
            return false;
        }

        for (SupplierCartItem item : mCartData.getData().getCart_items()) {
            if (item.getShipping() == null) {
                Toast.makeText(CheckoutOlshopActivity.this, getString(R.string.text_choose_shipping_method), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == KEY_ADD_ADDRESS_REQUEST_CODE) {
                addressResult = new Gson().fromJson(data.getStringExtra("addressResult"), ShippingAddressData.class);
                if (mSelectedAddress == null)
                    mSelectedAddress = new AddressRequestModel();
                mSelectedAddress.setId(addressResult.getId());
                mSelectedAddress.setPostalCode(String.valueOf(addressResult.getZip_code()));
                mSelectedAddress.setAddress(addressResult.getDetail());
                mSelectedAddress.setAddressName(addressResult.getName());
                mSelectedAddress.setDistrict(addressResult.getDistrict().getName());
                mSelectedAddress.setCity(addressResult.getCity().getName());
                mSelectedAddress.setProvince(addressResult.getProvince().getName());
                mSelectedAddress.setDistrictId(addressResult.getDistrict().getId());
                clearShipping();
                displayAddress();
                callMailNoAPI();
            } else if (requestCode == KEY_PIN_REQUEST_CODE) {
//                callPaymentJurnal(((FinalConfirmOrderResponse) br).getData().getPayment().getId());
                ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
                new OlshopDao(this).confirmOrderPaid(confirmPaid, BaseDao.getInstance(this, KEY_CONFIRM_PAID_CODE).callback);
            }
        }
    }

    private void clearShipping() {
        for (SupplierCartItem item : mCartData.getData().getCart_items()) {
            item.setShipping(null);
        }
        adapter.notifyDataSetChanged();
        shippingCost = 0;
        showTotal();
    }

    private void callMailNoAPI() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
        new OlshopDao(this).getMailNo(mSelectedAddress.getDistrictId(), BaseDao.getInstance(this, KEY_MAIL_NO_CODE).callback);
    }


    //region api requet
    private void callWalletInfo() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
        new WalletDao(this).emoneySummary(BaseDao.getInstance(this, KEY_WALLET).callback);
    }

    private void callPaymentJurnal(int paymentId) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
        PaymentDao dao = new PaymentDao(this);
        dao.paymentJurnal(new PaymentJurnalRequestModel(paymentId), BaseDao.getInstance(CheckoutOlshopActivity.this, KEY_API_PAYMENT_JURNAL).callback);
    }

    private long getCost(String value) {
        if (value == null) {
            return 0;
        }
        return (long) Double.parseDouble(value.isEmpty() ? "0" : value);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (br != null) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                if (responseCode == KEY_API_PAYMENT_JURNAL) {
                    PaymentJournalOlshopResponseModel data = (PaymentJournalOlshopResponseModel) br;
                    if (data.getMeta().getCode() == 200) {
                        gotoSuccessPage();
                    } else if (data.getMeta().getMessage().equalsIgnoreCase("Duplicate transaction")) {
                        showErrorMessage(getString(R.string.invoice_already_paid), data.getMeta().getMessage());
                    } else {
                        Toast.makeText(this, getString(R.string.payment_journal_failed), Toast.LENGTH_SHORT).show();
                    }
                } else if (responseCode == KEY_WALLET) {
                    WalletResponseModel res = (WalletResponseModel) br;
                    if (res.getData() != null && res.getData().size() > 0) {
                        WalletDataModel model = res.getData().get(0);
                        displayWalletInfo(model);
                    }
                } else if (responseCode == KEY_API_ORDER_CONFIRMATION) {
//                    OrderConfirmationResponseModel data = (OrderConfirmationResponseModel) br;
//                    if (data.getMeta().getStatus()) {
//
//                    } else {
//                        Toast.makeText(this, "Pembayaran gagal", Toast.LENGTH_SHORT).show();
//                    }
                } else if (responseCode == KEY_SHIPPING_REQUEST_CODE) {
                    handleShipping((ShippingCostResponseModel) br);
                } else if (responseCode == KEY_CONFIRM_PAID_CODE) {
                    paymentId = ((FinalConfirmOrderResponse) br).getData().getPayment().getId();
                    String totalPaid = ((FinalConfirmOrderResponse) br).getData().getPayment().getTotal_paid();
                    total = (long) Double.parseDouble(totalPaid);
                    callPaymentJurnal(paymentId);
//                    callPaymentJurnal(((FinalConfirmOrderResponse)br).getData().getPayment().getId());
//                    long total = 0;
//                    for (SupplierCartItem group : mCartData.getData().getCart_items()) {
//                        total += (long) Double.parseDouble(group.getTotal_price());
//                    }
//                    Intent intent = new Intent(this, ThankYouPageActivity.class);
//                    intent.putExtra("total", total);
//                    startActivity(intent);
                } else if (responseCode == KEY_MAIL_NO_CODE) {
                    mailNo = ((MailNoResponseModel) br).getData().getMail_no();
                }
            } else {
                if (responseCode == KEY_CONFIRM_PAID_CODE) {
                    if (((FinalConfirmOrderResponse) br).getData().getPayment() != null && ((FinalConfirmOrderResponse) br).getData().getPayment().getOrders() != null) {
                        adapter.setUpdatedData(((FinalConfirmOrderResponse) br).getData().getPayment().getOrders());
                    }
                    errorTitle.setText(((BaseResponseModel) br).getMeta().getMessage());
                    errorDialog.show();
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), ((BaseResponseModel) br).getMeta().getMessage());
                    errorDialog.show();
                }
            }
        }
    }

    private void gotoSuccessPage() {
        Pref.getPreference().remove(CART_COUNT);

        List<WidgetBuilderModel> keyValueList = new ArrayList<>();
        WidgetBuilderModel keyValue;

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Tipe Pembayaran");
        keyValue.setValue("DEPOSIT");
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Tanggal Transaksi");
        keyValue.setValue(DataUtil.getDateString((Calendar.getInstance()).getTimeInMillis(), "dd-MMM-yyyy HH:mm:ss"));
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Nomor Pembayaran");
        keyValue.setValue(String.valueOf(paymentId));
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Jenis Layanan");
        keyValue.setValue("Toko Online");
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Status Pemesanan");
        keyValue.setValue("Konfirmasi Pembayaran");
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Nama Pembeli");
        keyValue.setValue(leNamaPelanggan.getTextContent());
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("No Hp Pembeli");
        keyValue.setValue(leNoTelepon.getTextContent());
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Email Pembeli");
        keyValue.setValue(email.getTextContent());
        keyValueList.add(keyValue);

        keyValue = new WidgetBuilderModel();
        keyValue.setKey("Jumlah Total");
        keyValue.setValue(DataUtil.convertCurrency(subTotal + shippingCost));
        keyValueList.add(keyValue);

        PpobOttoagPaymentResponseModel data = new PpobOttoagPaymentResponseModel();
        data.data = new PaymentData();
        data.data.setKeyValueList(keyValueList);

        PpobPayment paymentData = new PpobPayment();
        paymentData.setTotalPayment(subTotal + shippingCost + 0.0);

        PpobInquiryResponse response = new PpobInquiryResponse();
        response.setData(new PpobInquiryResponse.Data());
        response.getData().setTotal(DataUtil.convertCurrency(subTotal + shippingCost));

        Intent intent = new Intent(this, PpobPaymentSuccessActivity.class);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) keyValueList);
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, new Gson().toJson(data));
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, paymentData);
        intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA, new Gson().toJson(response));
        intent.putExtra("tokoonline", "Jika dalam 3 hari sejak pengiriman selesai tidak ada konfirmasi penerimaan, maka sistem secara otomatis akan mengkonfirmasi penerimaan barang. Untuk pengembalian barang, hubungi customer service OttoPay.");
        startActivity(intent);
    }

    private boolean orderValidation(String referenceNumber) {
        if (leNamaPelanggan.getTextContent().isEmpty()) {
            Toast.makeText(this, "Silakan masukan nama penerima", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (leNoTelepon.getTextContent().isEmpty()) {
            Toast.makeText(this, "Silakan masukan nomor telepon penerima", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (leNamaPelanggan.getTextContent().isEmpty()) {
            Toast.makeText(this, "Silakan masukan email penerima", Toast.LENGTH_SHORT).show();
            return false;
        }

        confirmPaid = new ConfirmPaid();
        List<AddCartRequestModel> modelList = new ArrayList<>();
        AddCartRequestModel model;
        for (SupplierCartItem item : adapter.getmDataset()) {
            ShippingItem shipping = item.getShipping();
            if (shipping == null) {
                Toast.makeText(CheckoutOlshopActivity.this, getString(R.string.text_choose_shipping_method), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                model = new AddCartRequestModel();
                model.setSku(item.getSku());
                model.setId(item.getCart_item_id());
                model.setQuantity(item.getQuantity());
                model.setDelivery_method_code(shipping.getDelivery_method_code());
                model.setShipping_cost(getCost(shipping.getShipping_cost()));
                model.setInsurance_cost(getCost(shipping.getInsurance_cost()));
                model.setInsurance_required(item.isInsuranceCheck());
                modelList.add(model);
                total += (long) Double.parseDouble(item.getTotal_price());
            }
        }

        confirmPaid.setCart_items_attributes(modelList);
        confirmPaid.setAddress(mSelectedAddress.getAddress());
//        confirmPaid.setMail_no("SMI032011");
        confirmPaid.setMail_no(mailNo);
        confirmPaid.setPostal_code(mSelectedAddress.getPostalCode());
        confirmPaid.setBuyer_name(leNamaPelanggan.getTextContent());
        confirmPaid.setBuyer_phone(leNoTelepon.getTextContent());
        confirmPaid.setPayment_code(referenceNumber);
        confirmPaid.setBuyer_email(email.getTextContent());
        return true;
//        new OlshopDao(this).confirmOrderPaid(confirmPaid, BaseDao.getInstance(this, KEY_CONFIRM_PAID_CODE).callback);
    }

    private void handleShipping(ShippingCostResponseModel shipping) {
        if (shipping.getData().getShipping_cost().size() > 0) {
            Fragment prevFragment = getSupportFragmentManager().findFragmentByTag("shipping");
            if (prevFragment == null) {
                ShippingFragment shippingFragment = ShippingFragment.newInstance((pos, data, fragment) -> {
                    fragment.dismiss();
                    adapter.setShipping(data, parentPos, shippingPosition);
                    shippingCost = 0;

                    calculateShippingCost();
                    showTotal();

                    try {
                        CheckoutOlshopActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                shippingFragment.setShipping(shipping.getData().getShipping_cost());
                shippingFragment.show(getSupportFragmentManager(), "shipping");
            }
        } else {
            ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, "Pengiriman tidak tersedia untuk area Anda", "Pengiriman tidak tersedia untuk area Anda");
            errorDialog.show();
        }
    }

    private void calculateShippingCost() {
        shippingCost = 0;
        for (SupplierCartItem supplierCartItem : mCartData.getData().getCart_items()) {
            if (supplierCartItem.getShipping() != null) {
                shippingCost += (Double.parseDouble(supplierCartItem.getShipping().getShipping_cost()));
                if (supplierCartItem.isInsuranceCheck()) {
                    shippingCost += Double.parseDouble(supplierCartItem.getShipping().getInsurance_cost());
                }
            }
        }
        showTotal();
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        super.onApiFailureCallback(message, ac);
        ProgressDialogComponent.dismissProgressDialog(this);
    }

    private void showErrorMessage(String title, String message) {
        ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, message, message);
        errorDialog.show();
        errorDialog.setOnDismissListener(dialog -> {

        });
    }

    private void buildTNCDialog() {
        tncDialog = new LazyDialog(this, this, true, true);
        View tncView = LayoutInflater.from(this).inflate(R.layout.dialog_checkout, null);
        WebView webView = tncView.findViewById(R.id.contentContainer);

        webView.setWebViewClient(new CheckoutWebClient(value -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(value));
            if (tncDialog != null && tncDialog.isShowing()) {
                tncDialog.dismiss();
            }
            startActivity(intent);
        }));
        webView.clearHistory();
        webView.loadUrl("https://opv3.s3-ap-southeast-1.amazonaws.com/toko-online/tnc.html");

        TextView negative = tncView.findViewById(R.id.negajing);
        TextView positive = tncView.findViewById(R.id.posijing);

        negative.setText(getString(R.string.text_disagree));
        positive.setText(getString(R.string.text_agree));

        negative.setOnClickListener(v -> tncDialog.dismiss());
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (orderValidation("")) {
                        Intent jenk = new Intent(CheckoutOlshopActivity.this, RegisterPinResetActivity.class);
                        jenk.putExtra("confirm", true);
                        startActivityForResult(jenk, KEY_PIN_REQUEST_CODE);
                        tncDialog.dismiss();
                    }
                }
            }
        });

        tncDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                webView.loadUrl("file:///android_asset/html/tnc.html");
            }
        });

        tncDialog.setContainerView(tncView);

    }

    //endregion
}
