package com.otto.mart.support.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.otto.mart.R;

public class UIUtils {
    public static void handleImageEdittext(EditText view, String url) {
        if (url != null) {
            Glide.with(view.getContext()).load(url).into(new SimpleTarget<Drawable>(50, 50) {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    try {
                        view.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Resize View
     *
     * @param v      = view
     * @param width  = view width
     * @param height = view height
     */
    public static void resize(View v, int width, int height) {
        v.getLayoutParams().width = width;
        v.getLayoutParams().height = height;
    }

    /**
     * Response to get screen width of device
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void setHtmlClickable(TextView widget, String html, LinkClickListener listener) {
        String newHtmlValue = html.replace("\n", "<BR>");
        CharSequence content = HtmlCompat.fromHtml(newHtmlValue, HtmlCompat.FROM_HTML_MODE_COMPACT);
        SpannableStringBuilder spnStr = new SpannableStringBuilder(content);
        URLSpan[] urlSpans = spnStr.getSpans(0, content.length(), URLSpan.class);

        for (URLSpan urlSpan : urlSpans) {
            setClickableLink(spnStr, urlSpan, listener);
        }

        widget.setText(spnStr);
        widget.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static void setClickableLink(SpannableStringBuilder spn, URLSpan url, LinkClickListener listener) {
        int start = spn.getSpanStart(url);
        int end = spn.getSpanEnd(url);
        int flags = spn.getSpanFlags(url);
        ClickableSpan click = new ClickableSpan() {
            @Override
            public void onClick(@androidx.annotation.NonNull View widget) {
                if (listener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url.getURL());
                    listener.onLinkClicked(bundle);
                }
            }
        };

        spn.setSpan(click, start, end, flags);
        spn.removeSpan(url);
    }

    public interface LinkClickListener {
        void onLinkClicked(Bundle data);
    }
}
