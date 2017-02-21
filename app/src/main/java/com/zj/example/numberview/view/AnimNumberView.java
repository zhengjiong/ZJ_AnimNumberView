package com.zj.example.numberview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zj.example.numberview.R;

/**
 * Title: AnimNumberView
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/14  17:54
 *
 * @author 郑炯
 * @version 1.0
 */
public class AnimNumberView extends LinearLayout implements View.OnClickListener, Animator.AnimatorListener {
    public static final int DURATION = 400;
    public static final String ROTATION = "rotation";
    public static final String TRANSLATION_X = "translationX";
    public static final String ALPHA = "alpha";

    private ImageView mImgAdd;
    private ImageView mImgMin;
    private EditText mEditText;

    private ObjectAnimator mAddRightAnim;
    private ObjectAnimator mAddLeftAnim;
    private ObjectAnimator mMinLeftAnim;
    private ObjectAnimator mMinRightAnim;
    private ObjectAnimator mEditShowAnim;
    private ObjectAnimator mEditHideAnim;

    private OnNumberValueChanged mOnNumberValueChanged;

    public interface OnNumberValueChanged{
        /**
         *
         * @param value
         * @return
         */
        public boolean numberChanged(int value);
    }

    public AnimNumberView(Context context) {
        this(context, null);
    }

    public AnimNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.carts_number_layout, this);
        mImgAdd = (ImageView) findViewById(R.id.btn_add);
        mImgMin = (ImageView) findViewById(R.id.btn_min);
        mEditText = (EditText) findViewById(R.id.edit_count);
        mImgAdd.setOnClickListener(this);
        mImgMin.setOnClickListener(this);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initAnim(w, h);
    }

    private void initAnim(int w, int h) {
        PropertyValuesHolder positiveRotationValuesHolder = PropertyValuesHolder.ofFloat(ROTATION, 0, 360);
        PropertyValuesHolder negativeRotationLeftValuesHolder = PropertyValuesHolder.ofFloat(ROTATION, 0, -360);
        PropertyValuesHolder addTranslationXRightValuesHolder = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0, w / 2 - mImgAdd.getMeasuredWidth() / 2);
        PropertyValuesHolder addTranslationXLeftValuesHolder = PropertyValuesHolder.ofFloat(TRANSLATION_X, w / 2 - mImgAdd.getMeasuredWidth() / 2, 0);

        PropertyValuesHolder minTranslationXLeftValuesHolder = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0, -1 * (w / 2 - mImgAdd.getMeasuredWidth() / 2));
        PropertyValuesHolder minTranslationXRightValuesHolder = PropertyValuesHolder.ofFloat(TRANSLATION_X, -1 * (w / 2 - mImgAdd.getMeasuredWidth() / 2), 0);

        mAddRightAnim = ObjectAnimator.ofPropertyValuesHolder(mImgAdd, positiveRotationValuesHolder, addTranslationXRightValuesHolder);
        mAddRightAnim.setDuration(DURATION);
        mAddRightAnim.addListener(this);

        mAddLeftAnim = ObjectAnimator.ofPropertyValuesHolder(mImgAdd, negativeRotationLeftValuesHolder, addTranslationXLeftValuesHolder);
        mAddLeftAnim.setDuration(DURATION);
        mAddLeftAnim.addListener(this);

        mMinLeftAnim = ObjectAnimator.ofPropertyValuesHolder(mImgMin, negativeRotationLeftValuesHolder, minTranslationXLeftValuesHolder);
        mMinLeftAnim.setDuration(DURATION);
        mMinLeftAnim.addListener(this);

        mMinRightAnim = ObjectAnimator.ofPropertyValuesHolder(mImgMin, positiveRotationValuesHolder, minTranslationXRightValuesHolder);
        mMinRightAnim.setDuration(DURATION);
        mMinRightAnim.addListener(this);

        mEditShowAnim = ObjectAnimator.ofFloat(mEditText, ALPHA, 0, 1f);
        mEditShowAnim.setDuration(DURATION);
        mEditShowAnim.addListener(this);

        mEditHideAnim = ObjectAnimator.ofFloat(mEditText, ALPHA, 1f, 0);
        mEditHideAnim.setDuration(DURATION);
        mEditHideAnim.addListener(this);
    }

    @Override
    public void onClick(View v) {
        int count = getCount(mEditText.getText().toString());
        if (mOnNumberValueChanged != null && !mOnNumberValueChanged.numberChanged(count)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_add:
                count++;
                mEditText.setText(String.valueOf(count));
                if (count == 1) {
                    mAddRightAnim.start();
                    mImgMin.setVisibility(View.VISIBLE);
                    mEditText.setVisibility(View.VISIBLE);
                    mMinLeftAnim.start();
                    mEditShowAnim.start();
                }
                break;
            case R.id.btn_min:
                count--;
                if (count <= 0) {
                    count = 0;
                    mEditHideAnim.start();
                    mMinRightAnim.start();
                    mAddLeftAnim.start();
                }
                mEditText.setText(String.valueOf(count));
                break;
        }
    }

    int getCount(String str) {
        if (!TextUtils.isEmpty(str)) {
            return Integer.parseInt(str);
        } else {
            return 0;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        setEnable(false);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        setEnable(true);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        setEnable(true);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    private void setEnable(boolean enabled) {
        mImgAdd.setEnabled(enabled);
        mImgMin.setEnabled(enabled);
        mEditText.setEnabled(enabled);
    }

    public void setOnNumberValueChanged(OnNumberValueChanged onNumberValueChanged) {
        this.mOnNumberValueChanged = onNumberValueChanged;
    }
}
