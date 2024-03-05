package com.otto.mart.ui.fragment.olshop;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.olshop.Variant;
import com.otto.mart.model.APIModel.Request.olshop.AddCartRequestModel;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.ui.Partials.adapter.olshop.VariantAdapter;
import com.otto.mart.ui.activity.olshop.ProductDetailActivity;
import com.otto.mart.ui.component.dialog.SalepriceDialog;
import com.otto.mart.ui.fragment.ppob.PpobSetKomisiFragment;
import com.rd.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCartFragment extends BottomSheetDialogFragment {

    private static final String IMAGE_KEY = "image";
    private static final String NAME_KEY = "name";
    private static final String PRICE_KEY = "price";
    private static final String SPECIAL_PRICE_KEY = "special";
    private static final String MIN_ORDER = "minOrder";
    private static final String MAX_ORDER = "maxOrder";

    private ImageView productImage;
    private TextView productSpecialPrice, productPrice, productName, buy, availableStock, totalPrice;
    private AddCartRequestModel cartModel;
    private View view, plus, minus;
    private EditText quantity;
    private SalepriceDialog comission;
    private PpobSetKomisiFragment komisiFragment;
    private long salePrice;
    private ChipCloud chip_cloud;
    private List<Variant> option;
    private ViewGroup containerLayout;
    private String selectedSKU;
    private String label;
    private List<String> variantBuilder;
    private int currentStock;
    private long price;
    private long specialPrice;

    public static AddCartFragment newInstance(String image, String name, String price, String specialPrice, int minOrder, int maxOrder) {

        Bundle args = new Bundle();
        args.putString(IMAGE_KEY, image);
        args.putString(NAME_KEY, name);
        args.putString(PRICE_KEY, price);
        args.putString(SPECIAL_PRICE_KEY, specialPrice);
        args.putInt(MIN_ORDER, minOrder);
        args.putInt(MAX_ORDER, maxOrder);

        AddCartFragment fragment = new AddCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_to_cart, container, false);

        productImage = view.findViewById(R.id.productImage);
        productSpecialPrice = view.findViewById(R.id.productSpecialPrice);
        productPrice = view.findViewById(R.id.productPrice);
        productName = view.findViewById(R.id.productName);
        quantity = view.findViewById(R.id.productQuantity);
        plus = view.findViewById(R.id.buttonPlus);
        minus = view.findViewById(R.id.buttonMinus);
        buy = view.findViewById(R.id.buttonBuy);
        chip_cloud = view.findViewById(R.id.chip_cloud);
        containerLayout = view.findViewById(R.id.container);
        availableStock = view.findViewById(R.id.availableStock);
        totalPrice = view.findViewById(R.id.totalPrice);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDefaultHeight(view);

        Bundle data = getArguments();
        String name = data.getString(NAME_KEY);
        price = DataUtil.getLong(data.getString(PRICE_KEY));
        specialPrice = DataUtil.getLong(data.getString(SPECIAL_PRICE_KEY));
        String image = data.getString(IMAGE_KEY);
        int minOrder = data.getInt(MIN_ORDER);
        int maxOrder = data.getInt(MAX_ORDER);

        productName.setText(name);
        if (label != null) buy.setText(label);
        if (specialPrice > 0 && specialPrice != price) {
            productPrice.setText(DataUtil.convertCurrency(specialPrice));
            productSpecialPrice.setText(DataUtil.convertCurrency(price));
            productSpecialPrice.setVisibility(View.VISIBLE);
            salePrice = specialPrice;
        } else {
            productPrice.setText(DataUtil.convertCurrency(price));
            salePrice = price;
            productSpecialPrice.setVisibility(View.GONE);
        }

        totalPrice.setText(DataUtil.convertCurrency(salePrice));

        Glide.with(view.getContext()).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.pos).error(R.drawable.pos))
                .load(image).into(productImage);

        minus.setOnClickListener(v -> {
            int pre = Integer.parseInt(quantity.getText().toString());
            if (pre - 1 > 0) {
                quantity.setText(String.valueOf(pre - 1));
            }
        });

        plus.setOnClickListener(v -> {
            int pre = Integer.parseInt(quantity.getText().toString());
            quantity.setText(String.valueOf(pre + 1));
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int qty = Integer.parseInt(s.toString());
                    long total = salePrice * qty;
                    totalPrice.setText(DataUtil.convertCurrency(total));
                } else {
                    totalPrice.setText("-");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        comission = new SalepriceDialog(getContext(), getActivity(), false);
        comission.setListener(new SalepriceDialog.salepriceDialogListener() {
            @Override
            public void onSalepriceDecided(long initPrice, long salePrice) {
                cartModel.setSales_commission(salePrice - initPrice);
                StringBuilder variant = new StringBuilder();
                try {
                    for (String variantName : variantBuilder) {
                        variant.append(variantName);
                        variant.append(" ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((ProductDetailActivity) getActivity()).addToCart(selectedSKU, variant.toString());
                comission.dismiss();
                dismiss();
            }

            @Override
            public void onDialogDismiss() {

            }
        });

        komisiFragment = new PpobSetKomisiFragment();
        komisiFragment.setKomisiSelectedClickListener(komisi -> {
            cartModel.setSales_commission(komisi);
            StringBuilder variant = new StringBuilder();
            try {
                for (String variantName : variantBuilder) {
                    variant.append(variantName);
                    variant.append(" ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ProductDetailActivity) getActivity()).addToCart(selectedSKU, variant.toString());
            dismiss();
        });

        buy.setOnClickListener(v -> {
            if (!quantity.getText().toString().isEmpty() && !quantity.getText().toString().equals("0")) {
                if (minOrder > 0 && Integer.parseInt(quantity.getText().toString()) < minOrder) {
                    Toast.makeText(getContext(), "Minimal barang sebanyak " + minOrder, Toast.LENGTH_SHORT).show();
                } else if (maxOrder > 0 && Integer.parseInt(quantity.getText().toString()) > maxOrder) {
                    Toast.makeText(getContext(), "Maksimal barang sebanyak " + maxOrder, Toast.LENGTH_SHORT).show();
                } else {
                    cartModel.setQuantity(Integer.parseInt(quantity.getText().toString()));
//                    comission.setInitVal(salePrice);
//                    comission.show();
                    komisiFragment.setPrice(salePrice);
                    komisiFragment.show(getChildFragmentManager(), komisiFragment.getTag());
                }
            }
        });

        if (option != null && option.size() > 0) {
            addChild(option, option.get(0).getName(), 0);
            variantBuilder = new ArrayList<>();
        }
    }

    private void setDefaultHeight(@NonNull View view) {
        boolean hasMenuKey = ViewConfiguration.get(getContext()).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        ViewGroup root = view.findViewById(R.id.addCartContainer);
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        int originalHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        if (!hasBackKey&&!hasMenuKey) {
            int bottomNavHeight = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (bottomNavHeight > 0) {
                root.setPadding(root.getPaddingLeft(), root.getTop(), root.getRight(), getResources().getDimensionPixelSize(bottomNavHeight));
            }
        }
        layoutParams.height = originalHeight;
        root.setLayoutParams(layoutParams);
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        Objects.requireNonNull(bottomSheet).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
        if (behavior != null) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void setCartModel(AddCartRequestModel cartModel) {
        this.cartModel = cartModel;
    }

    public void setVariant(List<Variant> option) {
        this.option = option;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private void addChild(List<Variant> variants, String title, int index) {
        TextView textView = new TextView(getContext());
        if (title.length() > 1) {
            title = title.substring(0, 1).toUpperCase() + title.substring(1);
        }
        textView.setText(title);
        textView.setTextAppearance(getContext(), R.style.TextView12sp);
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setPadding(DensityUtils.dpToPx(8), DensityUtils.dpToPx(18), DensityUtils.dpToPx(8), DensityUtils.dpToPx(8));
        setColor(textView);
        containerLayout.addView(textView);

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.setTag(index);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, DeviceUtil.dpToPx(18));
        recyclerView.setLayoutParams(layoutParams);

        VariantAdapter adapter = new VariantAdapter(variants, containerLayout.getChildCount() - 1 < 0 ? 0 : containerLayout.getChildCount());
        adapter.setMOnViewClickListener((variant, position, viewPos) -> {
            int count = containerLayout.getChildCount();
            if (count > 1 && count - 1 > viewPos) {
                while (containerLayout.getChildAt(viewPos + 1) != null && containerLayout.getChildAt(viewPos + 2) != null) {
                    containerLayout.removeViewAt(viewPos + 1);
                    containerLayout.removeViewAt(viewPos + 1);
                }
            }

            int tag = Integer.valueOf(String.valueOf(recyclerView.getTag()));
            if (variantBuilder.size() > tag) {
                variantBuilder.add(tag, variant.getValue());
                variantBuilder.subList(tag + 1, variantBuilder.size()).clear();
            } else variantBuilder.add(variant.getValue());

            if (variant.getChild().size() > 0) {
                containerLayout.post(() -> addChild(variant.getChild(), variant.getChild().get(0).getName(), tag + 1));
                selectedSKU = null;
            } else {
                selectedSKU = variant.getSku();
            }

            currentStock = DataUtil.getInt(variant.getStock());
            availableStock.setText(String.format(getString(R.string.text_stock_availibity_pcs), currentStock));
        });

        recyclerView.setAdapter(adapter);
        containerLayout.addView(recyclerView);

        View divider = new View(getContext());
        divider.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dpToPx(4)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            divider.setBackgroundColor(getContext().getColor(R.color.very_light_grey));
        } else {
            divider.setBackgroundColor(getContext().getResources().getColor(R.color.very_light_grey));
        }

        containerLayout.addView(divider);
    }

    private void setColor(TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(getContext().getColor(R.color.brown_grey));
        } else
            textView.setTextColor(getContext().getResources().getColor(R.color.brown_grey));
    }
}
