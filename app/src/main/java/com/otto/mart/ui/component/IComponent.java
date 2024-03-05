package com.otto.mart.ui.component;
import android.view.View;

public interface IComponent {

    String getTextContent();

    View getComponent();

    void setTitle(String text);
}
