package com.otto.mart.ui.fragment;

import com.otto.mart.R;
import com.otto.mart.support.util.Connectivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.Popup;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseFragment;
import app.beelabs.com.codebase.component.ProgressDialogComponent;

public class AppFragment extends BaseFragment {

    @Override
    protected void onApiFailureCallback(String message) {
        //super.onApiFailureCallback(message);
        if (Connectivity.isConnected(getActivity())) {
            dialogServerError();
        } else {
            dialogInternetConnectionLost();
        }
    }

    protected void onApiResponseError() {
        if (getActivity() != null) {
            if (Connectivity.isConnected(getActivity())) {
                dialogServerError();
            } else {
                dialogInternetConnectionLost();
            }
        }
    }

    //region Dialog Error Message

    protected void dialogNoInternetConnection() {
        ErrorDialog dialog = new ErrorDialog(getActivity(), getActivity(), false,
                false, getString(R.string.error_ms_no_internet_connection), "");
        dialog.show();
    }

    protected void dialogInternetConnectionLost() {
        ErrorDialog dialog = new ErrorDialog(getActivity(), getActivity(), false,
                false, getString(R.string.error_msg_internet_connection_lost), "");
        dialog.show();
    }

    protected void dialogServerError() {
        ErrorDialog dialog = new ErrorDialog(getActivity(), getActivity(), false,
                false, getString(R.string.error_msg_response_error_title), getString(R.string.error_msg_response_error));
        dialog.show();
    }

    //endregion

    protected void dismissProgressDialog() {
        if (getActivity() != null)
            if (!getActivity().isFinishing() && !getActivity().isDestroyed())
                ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
    }

    protected void showProgressDialog(String message) {
        if (getContext() != null)
            ProgressDialogComponent.showProgressDialog(getContext(), message, false);
    }

    protected void showProgressDialog(int message) {
        if (getContext() != null) {
            showProgressDialog(getString(message));
        }
    }

    protected ErrorDialog showErrorDialog(String message) {
        if (getActivity() != null && getContext() != null) {
            ErrorDialog errorDialog = new ErrorDialog(getContext(), getActivity(), false, false, message, "");
            errorDialog.show();
            return errorDialog;
        }
        return null;
    }

    protected void comingSoonDialogBaseFragment() {

        Popup.showInfo(getFragmentManager(), "Feature is coming soon", "", null);

    }

}
