package com.otto.mart.ui.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.SecureQuestionModel;
import com.otto.mart.model.APIModel.Response.SecurityQuestionResponseModel;
import com.otto.mart.model.localmodel.ActionButtonModel;
import com.otto.mart.presenter.dao.EtcDao;
import com.otto.mart.ui.Partials.adapter.IntentActionAdapter;
import com.otto.mart.ui.activity.AppActivity;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

public class SecurityQuestionPickerActivity extends AppActivity {

    private View backhitbox;
    private RecyclerView rv_sq;

    IntentActionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secq_picker);
        initComponent();
        initContent();
        callAPI();
    }

    private void initComponent() {
        backhitbox = findViewById(R.id.backhitbox);
        rv_sq = findViewById(R.id.rv_sq);
    }

    private void initContent() {
        adapter = new IntentActionAdapter(R.layout.item_security_question, this);
        rv_sq.setAdapter(adapter);
        rv_sq.setLayoutManager(new LinearLayoutManager(this));
        backhitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    private void callAPI() {
        EtcDao dao = new EtcDao(this);
        dao.securityQuestionDAO(BaseDao.getInstance(this, 444).callback);
        ProgressDialogComponent.showProgressDialog(this, "Loading", false).show();
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        switch (responseCode) {
            case 444:
                if (response.isSuccessful()) {
                    List<SecureQuestionModel> data = ((SecurityQuestionResponseModel) br).getData();
                    List<ActionButtonModel> models = new ArrayList<>();
                    for (final SecureQuestionModel jink :
                            data) {
                        ActionButtonModel model = new ActionButtonModel();
                        model.setExtraData(jink);
                        model.setTextContent(jink.getQuestion());
                        model.setOnClickActionContent(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("question", jink.getQuestion());
                                returnIntent.putExtra("id", jink.getId());
                                setResult(Activity.RESULT_OK, returnIntent);
                                SecurityQuestionPickerActivity.this.finish();
                            }
                        });
                        models.add(model);
                    }
                    adapter.updateAdapter(models);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
