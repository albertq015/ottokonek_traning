package com.otto.mart.model.APIModel.Misc;

import java.util.List;

public class PaymentConfirmationModel {
    private PaymentPriceModel paymentPriceModel;
    private PulsaPaketModel pulsaPaketModel;
    private ListrikConfirmationModel listrikModel;
    private List<WidgetBuilderModel> donasiConfirmationList;

    public PulsaPaketModel getPulsaPaketModel() {
        return pulsaPaketModel;
    }

    public void setPulsaPaketModel(PulsaPaketModel pulsaPaketModel) {
        this.pulsaPaketModel = pulsaPaketModel;
    }

    public PaymentPriceModel getPaymentPriceModel() {
        return paymentPriceModel;
    }

    public void setPaymentPriceModel(PaymentPriceModel paymentPriceModel) {
        this.paymentPriceModel = paymentPriceModel;
    }

    public ListrikConfirmationModel getListrikModel() {
        return listrikModel;
    }

    public void setListrikModel(ListrikConfirmationModel listrikModel) {
        this.listrikModel = listrikModel;
    }

    public List<WidgetBuilderModel> getDonasiConfirmationList() {
        return donasiConfirmationList;
    }

    public void setDonasiConfirmationList(List<WidgetBuilderModel> donasiConfirmationList) {
        this.donasiConfirmationList = donasiConfirmationList;
    }
}
