package com.otto.mart.ui.activity.Topup;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.FaqTopUpRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.faq.FAQResponseData;
import com.otto.mart.model.APIModel.Response.faq.FAQResponseModel;
import com.otto.mart.presenter.dao.PpobDao;
import com.otto.mart.ui.Partials.adapter.TopupDoubleAdapter;
import com.otto.mart.ui.activity.AppActivity;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public class TopupActivity extends AppActivity {

    public static String KEY_IS_OTTOPAY = "is_ottopay";

    private RecyclerView rv;
    private View backButton;
    private TextView title, desc;
    private TextView toolbar;
    private ImageView appBack;

    private boolean isOttoPay = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        if (getIntent().hasExtra(KEY_IS_OTTOPAY)) {
            isOttoPay = getIntent().getBooleanExtra(KEY_IS_OTTOPAY, false);
        }

        initComponent();
        initContent();
    }

    private void initComponent() {
        rv = findViewById(R.id.rv);
        backButton = findViewById(R.id.backhitbox);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        toolbar = findViewById(R.id.tvToolbarTitle);
        appBack = findViewById(R.id.imgToolbarLeft);

        if (isOttoPay) {
            toolbar.setText(getString(R.string.button_top_up_deposit));
            title.setText(getString(R.string.button_top_up_deposit));
            desc.setText(getString(R.string.text_guidance_topup_deposit));
        } else {
            toolbar.setText(getString(R.string.text_topup_oc));
            title.setText(getString(R.string.text_topup_oc_balance));
            desc.setText(getString(R.string.text_guidance_topup_oc));
        }

        appBack.setOnClickListener(view->{
            finish();
        });
    }

    private void initContent() {
        FaqTopUpRequest faqTopUpRequest = new FaqTopUpRequest();
        faqTopUpRequest.setOttopay_instruction(isOttoPay);

        new PpobDao(this).faqTopUp(faqTopUpRequest, BaseDao.getInstance(this).callback);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (br != null) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                FAQResponseData data = ((FAQResponseModel) br).getData();

                TopupDoubleAdapter adapter = new TopupDoubleAdapter(data.getTop_up_channels());
                rv.setAdapter(adapter);
            }
        }
    }
}
