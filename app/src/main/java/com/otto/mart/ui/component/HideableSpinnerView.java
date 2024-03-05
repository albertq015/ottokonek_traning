package com.otto.mart.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.ui.Partials.adapter.SpinnerCategoryModelAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.lang.reflect.Field;
import java.util.List;

public class HideableSpinnerView extends LinearLayout implements IComponent {
    private Context mContext;
    private boolean hasTitle, hasCustomTypeface, hasCustomLayout;
    private boolean isBeingHidden, isKeyboardHidden;
    private String title;
    private Typeface customTypeface;
    private int spinnerLayout, dropdownLayout;
    private Custom1Spinner content;
    private ExpandableLayout expandableLayout, expandableLoadingLayout;
    private SpinnerCategoryModelAdapter adapter;

    private ViewGroup mainLayout;

    HidableSpinnerviewCallback listener = null;
    private TextView titleText;
    private ImageView imgIcon;

    public HideableSpinnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HideableSpinnerView);
        int count = typedArray.getIndexCount();
        int layout = R.layout.cw_spinner_hideable_default;
        spinnerLayout = R.layout.cw_spinnercontent_view_default;
        dropdownLayout = R.layout.cw_spinnercontent_dropdown_default;

        isKeyboardHidden = typedArray.getBoolean(R.styleable.HideableSpinnerView_hideKeyboard, false);

        try {
            for (int i = 0; i < count; ++i) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.HideableSpinnerView_title) {
                    hasTitle = true;
                    title = typedArray.getString(attr);
                    if (title.contains("@string")) {
                        title = getResources().getString(Integer.parseInt(title));
                    }
                } else if (attr == R.styleable.HideableSpinnerView_customLayout) {
                    hasCustomLayout = true;
                    layout = typedArray.getResourceId(attr, R.layout.cw_spinner_default);
                } /*else if (attr == R.styleable.HideableSpinnerView_customSpinnerLayout) {
                    spinnerLayout = typedArray.getResourceId(attr, R.layout.cw_spinnercontent_view_default);
                }*/ /*else if (attr == R.styleable.HideableSpinnerView_customDropdownLayout) {
                    dropdownLayout = typedArray.getResourceId(attr, R.layout.cw_spinnercontent_dropdown_default);
                }*/ else if (attr == R.styleable.HideableSpinnerView_typeface) {
                    hasCustomTypeface = true;
                    customTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + typedArray.getString(attr));
                } else if (attr == R.styleable.HideableSpinnerView_isHidden) {
                    isBeingHidden = typedArray.getBoolean(attr, false);
                }
            }
        } catch (Exception e) {
            Log.e("SearchEdittext", e.getMessage());
        } finally {
            typedArray.recycle();
        }
        inflater.inflate(layout, this);
        initComponent();
        initContent();
    }

    private void initComponent() {
        content = findViewById(R.id.contentContainer);

        titleText = findViewById(R.id.title);

        if (findViewById(R.id.img_icon) != null) {
            imgIcon = findViewById(R.id.img_icon);
        }

        if (hasTitle)
            titleText.setText(title);
        expandableLayout = findViewById(R.id.eLayout);
        if (isBeingHidden)
            expandableLayout.setExpanded(false);
        else
            expandableLayout.setExpanded(true);
        expandableLoadingLayout = findViewById(R.id.eLayout_loading);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initContent() {
        adapter = new SpinnerCategoryModelAdapter(mContext, spinnerLayout, dropdownLayout);
        content.setAdapter(adapter);
        if (isKeyboardHidden) {
            content.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return false;
                }
            });
        }


        content.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isCallbackReady()) {
                    if (listener != null) {
                        listener.onItemSelected(content, position, (SpinnerCategoryModelAdapter) content.getAdapter());
                    }
                }
                if (position == 0)
                    adapter.changeFirstItemToText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addData(List<CategoryModel> models) {
        adapter.addModels(models);
        adapter.notifyDataSetChanged();
        expandSpinner();
        if (listener != null) {
            listener.onDataLoaded(content, adapter);
        }
        if (adapter.getCount() > 5)
            try {
                Field popup = Spinner.class.getDeclaredField("mPopup");
                popup.setAccessible(true);
                android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(content);

                popupWindow.setHeight(DeviceUtil.dpToPx(250));

            } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
                Log.e("", "addData: " + e);
            }
    }

    public void removeData() {
        adapter.resetAdapter();
        hideSpinner();
        hideLoading();
    }

    public void resetSpinner(){
        adapter.resetAdapterNotNullingData();
    }

    public Long getSelectedItemId() {
        if (adapter.getItem(content.getSelectedItemPosition()) != null) {
            return ((CategoryModel) adapter.getItem((content.getSelectedItemPosition()))).getId();
        }
        return 0L;
    }

    public String getSelectedItemValue() {
        return ((CategoryModel) adapter.getItem((content.getSelectedItemPosition()))).getTitle();
    }

    public String getSelectedItemCode() {
        return ((CategoryModel) adapter.getItem((content.getSelectedItemPosition()))).getCode();
    }

    public Object getSelectedItemStoredModel() {
        return ((CategoryModel) adapter.getItem((content.getSelectedItemPosition()))).getSavedModel();
    }

    public int getSelectedItemPos() {
        return content.getSelectedItemPosition();
    }


    public void expandSpinner() {
        if (expandableLoadingLayout.isExpanded()) {
            hideLoading();
        }
        expandableLayout.setExpanded(true);
        if (listener != null) {
            listener.onExpand();
        }

    }

    public void expandLoading() {
        expandableLoadingLayout.setExpanded(true);
    }

    public void hideSpinner() {
        expandableLayout.setExpanded(false);
        if (listener != null) {
            listener.onHide();
        }
    }

    public void hideLoading() {
        expandableLoadingLayout.setExpanded(false);
    }

    public void setOnCallback(HidableSpinnerviewCallback listener) {
        this.listener = listener;
    }

    public Custom1Spinner getContent() {
        return content;
    }

    @Override
    public String getTextContent() {
        return getSelectedItemValue();
    }

    @Override
    public View getComponent() {
        return content;
    }

    @Override
    public void setTitle(String text) {
        titleText.setText(text);
    }

    public void setSelection(int sel) {
        content.setSelection(sel);
    }

    public interface HidableSpinnerviewCallback {
        void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter);

        void onDropdownOpen();

        void onHide();

        void onExpand();

        void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter);
    }
}
