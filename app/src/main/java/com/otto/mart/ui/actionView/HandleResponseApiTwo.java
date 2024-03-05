package com.otto.mart.ui.actionView;

import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

public interface HandleResponseApiTwo {

    void resultApiSuccess(BaseResponse br, Response response);

    void resultApiFailed(String message, int responseCodeHttp, int metaCode);
}
