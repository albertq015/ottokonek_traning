package com.otto.mart.ui.fragment.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.model.APIModel.Misc.inbox.Inbox;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse;
import com.otto.mart.support.util.ApiCallback;
import com.otto.mart.ui.Partials.adapter.inbox.InboxAdapter;
import com.otto.mart.ui.activity.dashboard.IDashboard;
import com.otto.mart.ui.activity.inbox.MessageActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.fragment.AppFragment;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_INBOX_LIST;
import static com.otto.mart.ui.activity.inbox.MessageActivity.KEY_INBOX_DATA;

public class DashboardInboxFragment extends AppFragment {
    private Context mContext;
    private View mView;
    private TextView tvReadBadge, btnMark;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;
    private Bundle viewState;

    private boolean mIsLoading = false;
    private boolean mIsNomoreData = false;
    private int mPage = 1;
    List<Inbox> mInboxList = new ArrayList();
    private InboxAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_dashboard_message, container, false);
        this.viewState = savedInstanceState;
        ((IDashboard) getActivity()).isMainPage(false);

        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPage = 1;
        mInboxList.clear();
        adapter.notifyDataSetChanged();
        getInbox();
    }

    private void initView() {
        tvReadBadge = mView.findViewById(R.id.tv_read_badge);
        btnMark = mView.findViewById(R.id.btnMark);
        viewAnimator = mView.findViewById(R.id.view_animator);
        recyclerView = mView.findViewById(R.id.recyclerview);
        emptyLayout = mView.findViewById(R.id.empty_layout);

        adapter = new InboxAdapter(getActivity(), mInboxList);
        adapter.setmOnViewClickListener((inbox, position) -> {
            if (position < mInboxList.size()) {
                inboxSelected(mInboxList.get(position));
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        //Set RecyclerView Scroll Listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int offsetItem = 0;

                    if ((!mIsLoading) && (!mIsNomoreData)) {
                        if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - offsetItem)) {
                            mIsLoading = true;
                            getInbox();
                        }
                    }
                }
            }
        });

        btnMark.setOnClickListener(v -> editInbox());
    }

    private void displayInbox(List<Inbox> inboxList) {
        if (inboxList.size() > 0) {

            recyclerView.getRecycledViewPool().clear();
            if (mPage == 1) {
                mInboxList.clear();
                mInboxList.addAll(inboxList);
                adapter.notifyDataSetChanged();
            } else {
                mInboxList.addAll(inboxList);
                int last = adapter.getItemCount();
                // adapter.notifyItemRangeInserted(last, mInboxList.size());
                adapter.notifyDataSetChanged();
            }

            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else {
            if (mPage == 1) {
                recyclerView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                mIsNomoreData = true;
            }
        }

        viewAnimator.setDisplayedChild(1);
        mIsLoading = false;
        mPage++;
    }

    private void displayReadBadge(int unreadCount) {
        if (unreadCount > 0) {
            tvReadBadge.setText(unreadCount + "");
//            tvReadBadge.setVisibility(View.VISIBLE);
        } else {
            tvReadBadge.setVisibility(View.GONE);
        }
    }

    private void inboxSelected(Inbox inbox) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra(KEY_INBOX_DATA, new Gson().toJson(inbox));
        startActivity(intent);
    }

    private void getInbox() {
        //ProgressDialogComponent.showProgressDialog(getActivity(), getString(R.string.loading_message), false);
        mIsLoading = true;

        if (mPage == 1) {
            mInboxList.clear();
            mIsNomoreData = false;
        }

//        new InboxDao(getActivity()).getInboxList(mPage, BaseDao.getInstance(this, API_KEY_INBOX_LIST).callback);
        OttoKonekAPI.notificationList(getContext(), mPage, new ApiCallback<InboxResponse>() {
            @Override
            public void onResponseSuccess(InboxResponse body) {
                if (isSuccess200) {
                    displayInbox(body.getData().getNotifications());
                    displayReadBadge(body.getData().getUnread());
                } else
                    new ErrorDialog(getActivity(), getActivity(), false, false, body.getMeta().getMessage(), "").show();
                dismissLoading();
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissLoading();
            }
        });
    }

    public void editInbox() {
        ProgressDialogComponent.showProgressDialog(getContext(), getString(R.string.loading_message), false);
        OttoKonekAPI.notificationActionAll(getContext(), "read", new ApiCallback<InboxResponse>() {
            @Override
            public void onResponseSuccess(InboxResponse body) {
                dismissLoading();
                if (isSuccess200) {
                    mPage = 1;
                    displayInbox(body.getData().getNotifications());
                    displayReadBadge(body.getData().getUnread());
                } else
                    new ErrorDialog(getContext(), getActivity(), false, false, body.getMeta().getMessage(), "").show();
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                onApiResponseError();
                dismissLoading();
            }
        });
//        if (mInboxList.size() > 0) {
//            InboxResponse inboxResponse = new InboxResponse();
//            InboxResponse.Data data = new InboxResponse.Data();
//            data.setNotifications(mInboxList);
//            inboxResponse.setData(data);
//
//            Intent intent = new Intent(getActivity(), InboxEditActivity.class);
//            intent.putExtra(AppKeys.KEY_INBOX_DATA, new Gson().toJson(inboxResponse));
//            startActivity(intent);
//        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_INBOX_LIST:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        displayInbox(((InboxResponse) br).getData().getNotifications());
                        displayReadBadge(((InboxResponse) br).getData().getUnread());
                    } else {
                        ErrorDialog dialog = new ErrorDialog(getActivity(), getActivity(), false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message) {
        //onApiResponseError();
    }

    private void dismissLoading() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        }
    }
}