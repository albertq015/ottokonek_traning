package com.otto.mart.ui.activity.olshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.Breadcrumb;
import com.otto.mart.model.APIModel.Response.olshop.ImagesItem;
import com.otto.mart.model.APIModel.Response.olshop.ProductDetail;
import com.otto.mart.model.APIModel.Response.olshop.ProductDetailResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.SubCategory;
import com.otto.mart.model.APIModel.Response.olshop.cart.CartResponseModel;
import com.otto.mart.model.APIModel.Response.olshop.product.CourierItem;
import com.otto.mart.model.APIModel.Response.olshop.product.Product;
import com.otto.mart.presenter.dao.olshop.OlshopDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.Partials.adapter.ImageAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.AppBarStateListener;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.LazyTextview;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.olshop.AddCartFragment;
import com.rd.PageIndicatorView;

import java.util.Collections;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.GLOBAL.CART_COUNT;

public class ProductDetailActivity extends AppActivity implements View.OnClickListener {

    private final int PRODUCT_RC = 0;
    private final int Add_CART_RC = 1;

    RecyclerView recyclerView;
    private TextView toolbarTitle, discountValue, buyNow, productDescription, supplierName, productName, productPrice, productSpecialPrice, cart_tv;
    private View badgeDiscount, backButton, cartButton, buttonContainer,addToCart;
    private PageIndicatorView pageIndicatorView;
    private Toolbar toolbar;
    private ImageView supplierThumb;
    private int productId;
    private ImageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private LazyTextview productCategory, shipping, weight, minOrder, maxOrder;
    private AddCartFragment addCartFragment;
    private AddCartRequestModel cartModel;
    private boolean isAddCart;
    private LazyDialog shopConfirmation;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_product_detail);

        initComponent();
        initContent();
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.pager);
        discountValue = findViewById(R.id.discountValue);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);
        badgeDiscount = findViewById(R.id.badgeDiscount);
        productDescription = findViewById(R.id.productDescription);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        supplierName = findViewById(R.id.supplierName);
        supplierThumb = findViewById(R.id.supplierThumb);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productSpecialPrice = findViewById(R.id.productSpecialPrice);
        productCategory = findViewById(R.id.productCategory);
        backButton = findViewById(R.id.backButton);
        cartButton = findViewById(R.id.cartButton);
        cart_tv = findViewById(R.id.cart_tv);
        buttonContainer = findViewById(R.id.buttonContainer);
        shipping = findViewById(R.id.shippingMethod);
        webView = findViewById(R.id.webView);
        weight = findViewById(R.id.productWeight);
        minOrder = findViewById(R.id.minOrder);
        maxOrder = findViewById(R.id.maxOrder);
    }

    private void initContent() {
        adapter = new ImageAdapter();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, int state) {
                if (state == AppBarStateListener.STATE_COLLAPSED) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.GONE);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                pageIndicatorView.setSelected(layoutManager.findFirstVisibleItemPosition());
            }
        });

        buyNow.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        backButton.setOnClickListener(this);
        cartButton.setOnClickListener(this);

        productId = getIntent().getIntExtra("productId", 0);
        ProgressDialogComponent.showProgressDialog(this, "Memuat", false);
        new OlshopDao(this).getProductDetail(BaseDao.getInstance(this, PRODUCT_RC).callback, productId);
        buildDialog();
    }

    private void buildDialog() {
        shopConfirmation = new LazyDialog(this, this, true, true);
        View dialogView=LayoutInflater.from(this).inflate(R.layout.add_to_cart_confirmation, null);
        dialogView.findViewById(R.id.actionContinue).setOnClickListener(v -> {
            shopConfirmation.dismiss();
            finish();
        });
        dialogView.findViewById(R.id.actionCart).setOnClickListener(v ->
                startActivity(new Intent(ProductDetailActivity.this, CartOlshopActivity.class))
        );
        shopConfirmation.setContainerView(dialogView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int count = Pref.getPreference().getInt(CART_COUNT);
        if (count > 0) {
            cart_tv.setVisibility(View.VISIBLE);
            cart_tv.setText(String.valueOf(count));
        } else cart_tv.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyNow: {
                isAddCart = false;
                if (addCartFragment != null) {
                    addCartFragment.setLabel("BELI");
                    addCartFragment.show(getSupportFragmentManager(), "addcart");
                }
                break;
            }
            case R.id.addToCart: {
                isAddCart = true;
                if (addCartFragment != null) {
                    addCartFragment.setLabel("TAMBAH");
                    addCartFragment.show(getSupportFragmentManager(), "addcart");
                }
                break;
            }
            case R.id.backButton: {
                finish();
                break;
            }
            case R.id.cartButton: {
                startActivity(new Intent(this, CartOlshopActivity.class));
                break;
            }
        }
    }

    private void callAddToCartAPI() {
        new OlshopDao(this).getCartList(BaseDao.getInstance(this, 2).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (((BaseResponseModel) br).getMeta().getCode() == 200) {
            switch (responseCode) {
                case PRODUCT_RC: {
                    handleProductDetailResponse((ProductDetailResponseModel) br);
                    break;
                }
                case Add_CART_RC: {
                    handleCartResponse((CartResponseModel) br);
//                    if (!isAddCart) {
//                        startActivity(new Intent(this, CartActivity.class));
//                    } else {
//                        shopConfirmation.show();
//                    }
                    break;
                }
            }
        } else {
            ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), ((BaseResponseModel) br).getMeta().getMessage());
            errorDialog.show();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        super.onApiFailureCallback(message, ac);
        ProgressDialogComponent.dismissProgressDialog(this);
        if (!isAddCart) {
            startActivity(new Intent(this, CartOlshopActivity.class));
        }
    }

    private void handleProductDetailResponse(ProductDetailResponseModel br) {
        ProductDetail data = br.getData().getProduct();
        Product product = br.getData().getProduct().getDetails();
        String image = "";
        if (data != null) {
            setProductDetailValue(data);
//            if (data.getImages() != null) {
//                if (data.getImages().size() > 0) {
            image = product.getImage_url();
//                }
//            }

            addCartFragment = AddCartFragment.newInstance(
                    image, data.getTitle(), DataUtil.convertCurrency(data.getOttomart_price()), DataUtil.convertCurrency(data.getOttomart_discount_price()), product.getSell_min_limit_quantity(), product.getSell_limit_quantity());
        }
        //inside if
        productId = data.getId();
        buttonContainer.setVisibility(View.VISIBLE);
        addCartFragment = AddCartFragment.newInstance(
                image, product.getProduct_name(), DataUtil.convertCurrency(data.getOttomart_price()), DataUtil.convertCurrency(data.getOttomart_discount_price()), product.getSell_min_limit_quantity(), product.getSell_limit_quantity());
        cartModel = new AddCartRequestModel(1, br.getData().getProduct().getId(), 0, product.getSku());
        addCartFragment.setCartModel(cartModel);
        addCartFragment.setVariant(product.getOptions());
        Glide.with(supplierThumb).load("http://ottopay-mart.clappingape.com" + data.getSupplier().getLogo().getThumb().getUrl()).into(supplierThumb);
        supplierName.setText(data.getSupplier().getName());

        setProductDetailValue(product);
    }

    private void setProductDetailValue(Product product) {
        productName.setText(product.getProduct_name());
        toolbarTitle.setText(product.getProduct_name());
        productDescription.setText(product.getDescription());
        pageIndicatorView.setCount(0);
        adapter.setImageList(Collections.singletonList((new ImagesItem(product.getImage_url()))));
        if (product.getSell_min_limit_quantity() > 0) {
            minOrder.setText(String.valueOf(product.getSell_min_limit_quantity()));
            minOrder.setVisibility(View.VISIBLE);
        }

        if (product.getSell_limit_quantity() > 0) {
            maxOrder.setText(String.valueOf(product.getSell_limit_quantity()));
            maxOrder.setVisibility(View.VISIBLE);
        }
//        productCategory.setText(breadcrumbBuilder(data.getBreadcrumb()));

        long price = product.getOriginal_sale_price();
        long special = product.getDiscounted_product_price();
        int discountPercentage;

//        if (special > 0) {
        discountPercentage = (int) (((float) (price - special) / price) * 100);
//            setPriceValue(product.getOriginal_sale_price(), product.getDiscounted_product_price(), discountPercentage);
//        } else
//            setPriceValue(product.getOriginal_sale_price(), product.getDiscounted_product_price(), 0);

        StringBuilder shippingMethod = new StringBuilder();
        for (CourierItem courier : product.getCouriers()) {
            if (shippingMethod.length() > 0) {
                shippingMethod.append(" / ");
            }
            shippingMethod.append(courier.getCourier_name());
        }

        if (shippingMethod.length() > 0) {
            shipping.setText(shippingMethod.toString());
        } else shipping.setVisibility(View.GONE);
        weight.setText(product.getWeight() + " g");
        webView.setVisibility(View.GONE);
        webView.loadData(product.getDescription(), "text/html", "UTF-8");
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setProductDetailValue(ProductDetail data) {
        productId = data.getId();
        productName.setText(data.getTitle());
        toolbarTitle.setText(data.getTitle());
        productDescription.setText(data.getDescription());
//        pageIndicatorView.setCount(data.getImages().size());
//        adapter.setImageList(Collections.singletonList(new ImagesItem()));
        productCategory.setText(breadcrumbBuilder(data.getBreadcrumb()));


        long price = data.getOttomart_price();
        long special = data.getOttomart_discount_price();
        int discountPercentage;

        if (special > 0) {
            discountPercentage = (int) (((float) (price - special) / price) * 100);
            setPriceValue(price, special, discountPercentage);
        } else setPriceValue(price, special, 0);
    }

    private void setPriceValue(long price, long specialPrice, int discount) {
        if (discount > 0) {
            productPrice.setText(DataUtil.convertCurrency(specialPrice));
            productSpecialPrice.setText(DataUtil.convertCurrency(price));
            discountValue.setText(discount + "%");
//            badgeDiscount.setVisibility(View.VISIBLE);
            productSpecialPrice.setVisibility(View.VISIBLE);
        } else {
            productPrice.setText(DataUtil.convertCurrency(price));
            badgeDiscount.setVisibility(View.GONE);
            productSpecialPrice.setVisibility(View.GONE);
        }
    }

    private String breadcrumbBuilder(Breadcrumb breadcrumb) {
        StringBuilder value = new StringBuilder();
        value.append(breadcrumb.getName());

        SubCategory sub = breadcrumb.getSub_category();

        while (sub.getName() != null) {
            value.append(" > ");
            value.append(sub.getName());
            sub = sub.getSub_category();
        }

        return value.toString();
    }

    private void handleCartResponse(CartResponseModel cart) {
        if (cart.getFaileds().isStatus()) {
            ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, "Anda tidak dapat menambahkan komisi yang berbeda untuk produk yang sama dalam satu keranjang belanja. Jika Anda ingin mengubah komisi untuk produk yang sama, hapus terlebih dahulu produk dari keranjang belanja Anda\n\n" +
                    "Komisi sebelumnya sebesar " + DataUtil.convertCurrency(cart.getFaileds().getOld_sales_commission()),"");
            errorDialog.show();
        } else if (isAddCart) {
            shopConfirmation.show();
        } else {
            startActivity(new Intent(this, CartOlshopActivity.class));
        }
    }

    public void addToCart(String selectedSKU, String variant) {
        if (selectedSKU != null) {
            cartModel.setSku(selectedSKU);
            if (variant != null) {
                cartModel.setVariant(variant);
            }
        }
        ProgressDialogComponent.showProgressDialog(this, "Mohon Tunggu", false);
        new OlshopDao(this).addCart(cartModel, BaseDao.getInstance(this, Add_CART_RC).callback);
    }
}
