package com.lcj.commonlib.view.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lcj.commonlib.R;
import com.lcj.commonlib.utils.image.ImageUtil;

import java.util.ArrayList;
/** This class customs a ViewGroup to display more picutres which looks like WeChat Moments page.
 * @author lcj
 * @version 1.1
 * */
public class MultiPicturesView extends ViewGroup {
    private int mColumnCount;
    private final float mSpacing;
    private float mItemAspectRatio;
    private int mItemWidth;
    private int mItemHeight;
    private static final int MAX_COLUMN =3;

    public MultiPicturesView(Context context) {
        this(context, null);
    }

    public MultiPicturesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiPicturesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final float DEFAULT_SPACING = 2.5f;
        mSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SPACING,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final float MAX_WIDTH_PERCENTAGE = 270f / 350;
        int count = getChildCount();
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        if (count < MAX_COLUMN) {
            mColumnCount = count;
        } else {
            mColumnCount = MAX_COLUMN;
        }
        if (count == 1) {
            int mItemMaxWidth = (int) (width * MAX_WIDTH_PERCENTAGE);
            if (mItemAspectRatio < 1) {
                mItemHeight = mItemMaxWidth;
                mItemWidth = (int) (mItemHeight * mItemAspectRatio);
            } else {
                mItemWidth = mItemMaxWidth;
                mItemHeight = (int) (mItemMaxWidth / mItemAspectRatio);
            }
        } else {
            if (count == 2) {
                mItemWidth = (int) ((width - getPaddingLeft() - getPaddingRight() - mSpacing) / mColumnCount);
            } else {
                mItemWidth = (int) ((width - getPaddingLeft() - getPaddingRight() - 2 * mSpacing) / mColumnCount);
            }
            mItemHeight = (int) (mItemWidth / mItemAspectRatio);
        }


        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = mItemWidth;
            layoutParams.height = mItemHeight;
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getDesiredHeight(mItemHeight), MeasureSpec.EXACTLY);
        }

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(
                    getDesiredWidth(mItemWidth), MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        final LayoutParams lp = child.getLayoutParams();
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private int getDesiredHeight(int mItemHeight) {
        int totalHeight = getPaddingTop() + getPaddingBottom();
        int count = getChildCount();

        if (count > 0 && mColumnCount>0) {
            int row = (count - 1) / mColumnCount;
            totalHeight = (int) ((row + 1) * mItemHeight + (row) * mSpacing) + totalHeight;
        }
        return totalHeight;
    }

    private int getDesiredWidth(int mItemWidth) {
        int totalWidth = getPaddingLeft() + getPaddingRight();
        int count = getChildCount();
        if (count > 0) {
            if(count <3) {
                totalWidth = (int) (count * mItemWidth + (count - 1) * mSpacing) + totalWidth;
            }else{
                totalWidth = (int) (3 * mItemWidth + 2 * mSpacing) + totalWidth;
            }
        }
        return totalWidth;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View imageView = getChildAt(i);
            int column = i % mColumnCount;
            int row = i / mColumnCount;
            int left = (int) (getPaddingLeft() + column * (mSpacing + mItemWidth));
            int top = (int) (getPaddingTop() + row * (mSpacing + mItemHeight));
            imageView.layout(left, top, left + mItemWidth, top + mItemHeight);
        }
    }


    /**
     * 显示图片
     */
    public void setImageUrls(final ArrayList<String> imageUrls) {
        removeAllViews();
        if (imageUrls.size() == 0) {
            return;
        }
        int count = imageUrls.size();
        if (count == 1) {
            mItemAspectRatio = 1000 / 1376f;
        } else {
            mItemAspectRatio = 1;
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            ImageUtil.loadImage((Activity) getContext(), imageView, imageUrls.get(i), R.drawable.no_picture);
            addView(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}
