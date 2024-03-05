package com.otto.mart.model.APIModel.Response.olshop.cart;

public class FailedCart {
    private int new_sales_commission;
    private int old_sales_commission;
    private boolean status;

    public int getNew_sales_commission() {
        return new_sales_commission;
    }

    public void setNew_sales_commission(int new_sales_commission) {
        this.new_sales_commission = new_sales_commission;
    }

    public int getOld_sales_commission() {
        return old_sales_commission;
    }

    public void setOld_sales_commission(int old_sales_commission) {
        this.old_sales_commission = old_sales_commission;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
