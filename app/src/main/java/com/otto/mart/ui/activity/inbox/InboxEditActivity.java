package com.otto.mart.ui.activity.inbox;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.inbox.Inbox;
import com.otto.mart.model.APIModel.Request.inbox.InboxUpdateBulkRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.DataEmptyResponseModel;
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse;
import com.otto.mart.presenter.dao.InboxDao;
import com.otto.mart.ui.Partials.adapter.inbox.InboxEditAdapter;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_INBOX_DELETE_BULK;
import static com.otto.mart.keys.AppKeys.API_KEY_INBOX_READ_BULK;

public class InboxEditActivity extends AppActivity {

    private final String TAG = this.getClass().getSimpleName();

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private CheckBox checkBox;
    private TextView tvCounter;
    private LinearLayout btnBack;
    private TextView tvTitle;
    private LinearLayout btnRead;
    private LinearLayout btnDelete;

    List<Inbox> mInboxList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_edit);

        if (getIntent().getStringExtra(AppKeys.KEY_INBOX_DATA) != null) {
            String inboxDataJSON = getIntent().getExtras().getString(AppKeys.KEY_INBOX_DATA);
            InboxResponse inboxResponse = new Gson().fromJson(inboxDataJSON, InboxResponse.class);
            mInboxList = inboxResponse.getData().getNotifications();
        }

        initComponent();
        initContent();

        displayInbox(mInboxList);
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvTitle = findViewById(R.id.tv_title);
        viewAnimator = findViewById(R.id.view_animator);
        recyclerView = findViewById(R.id.recyclerview);
        checkBox = findViewById(R.id.checkbox);
        tvCounter = findViewById(R.id.tv_counter);
        btnBack = findViewById(R.id.btn_back);
        btnRead = findViewById(R.id.btn_read);
        btnDelete = findViewById(R.id.btn_delete);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InboxEditActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initContent() {

        tvToolbarTitle.setText(getString(R.string.title_activity_inbox_edit));
        tvCounter.setText(getString(R.string.inbox_edit_labeel_counter, 0));

        btnToolbarBack.setOnClickListener(v -> {
            onBackPressed();
        });

        checkBox.setOnClickListener(v -> {
            selectAllProduct();
        });

        btnRead.setOnClickListener(v -> {
            if (getSelectedInbox().size() > 0) {
                readBulk(getSelectedInbox());
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (getSelectedInbox().size() > 0) {
                deleteBulk(getSelectedInbox());
            }
        });
    }

    private void displayInbox(List<Inbox> inboxList) {
        InboxEditAdapter adapter = new InboxEditAdapter(InboxEditActivity.this, inboxList);
        recyclerView.setAdapter(adapter);
        viewAnimator.setDisplayedChild(1);

        adapter.setOnInboxButtonListener((item, position) -> {
            mInboxList.get(position).setSelected(!mInboxList.get(position).isSelected());
            adapter.notifyDataSetChanged();

            updateCounter();
        });
    }

    private void selectAllProduct() {
        int counter = 0;
        int i = 0;

        for (Inbox inbox : mInboxList) {
            mInboxList.get(i).setSelected(checkBox.isChecked());
            i++;
        }

        InboxEditAdapter adapter = (InboxEditAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (checkBox.isChecked()) {
            counter = mInboxList.size();
        }

        tvCounter.setText(getString(R.string.inbox_edit_labeel_counter, counter));
    }

    private void updateCounter() {
        int selected = 0;

        for (Inbox inbox : mInboxList) {
            if (inbox.isSelected()) {
                selected++;
            }
        }

        if (selected == mInboxList.size()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        tvCounter.setText(getString(R.string.inbox_edit_labeel_counter, selected));
    }

    private List<Integer> getSelectedInbox(){
        List<Integer> inboxIds = new ArrayList();

        for (Inbox inbox : mInboxList) {
            if (inbox.isSelected()) {
                inboxIds.add(inbox.getId());
            }
        }

        return inboxIds;
    }


    /**
     * API Call
     */

    //region API Call

    private void readBulk(List<Integer> inboxIds) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);

        InboxUpdateBulkRequest inboxUpdateBulkRequest = new InboxUpdateBulkRequest();
        inboxUpdateBulkRequest.setNotification_ids(inboxIds);

        new InboxDao(this).inboxReadBulk(inboxUpdateBulkRequest, BaseDao.getInstance(this, API_KEY_INBOX_READ_BULK).callback);
    }

    private void deleteBulk(List<Integer> inboxIds) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);

        InboxUpdateBulkRequest inboxUpdateBulkRequest = new InboxUpdateBulkRequest();
        inboxUpdateBulkRequest.setNotification_ids(inboxIds);

        new InboxDao(this).inboxDeleteBulk(inboxUpdateBulkRequest, BaseDao.getInstance(this, API_KEY_INBOX_DELETE_BULK).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_INBOX_READ_BULK:
                    if (((DataEmptyResponseModel) br).getMeta().getCode() == 200) {
                        Log.d(TAG, "Inbox Read Bulk Success");
                        finish();
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
                case API_KEY_INBOX_DELETE_BULK:
                    if (((DataEmptyResponseModel) br).getMeta().getCode() == 200) {
                        Log.d(TAG, "Inbox Delete Bulk Success");
                        finish();
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

    //endregion API Call
}
