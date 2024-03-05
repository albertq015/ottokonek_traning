package com.otto.mart.ui.Partials.adapter.olshop;

public interface ItemSelectedListener {
    void isSelectedAll(boolean isSelected);

    void onItemChange(long before, long after, boolean isChecked, int parentPos, int adapterPosition);
}
