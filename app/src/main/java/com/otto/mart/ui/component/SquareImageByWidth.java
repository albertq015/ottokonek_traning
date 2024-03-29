package com.otto.mart.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.otto.mart.R;
import com.otto.mart.support.util.DeviceUtil;

public class SquareImageByWidth extends ImageView {

    private Drawable drawable = null;
    private boolean isHorizontalOriented = false;
    private int roundedValue = DeviceUtil.dpToPx(8);

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        postInvalidate();
    }

    public SquareImageByWidth(Context context) {
        super(context);
    }

    public SquareImageByWidth(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageByWidth(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImage, defStyleAttr, 0);
        isHorizontalOriented = typedArray.getBoolean(R.styleable.SquareImage_horizontal, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void setRoundImage(Bitmap bm,int roundedValue) {
        setImageBitmap(getRoundedCornerBitmap(bm, DeviceUtil.dpToPx(roundedValue)));
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
