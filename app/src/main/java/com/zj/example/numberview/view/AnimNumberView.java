package com.zj.example.numberview.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
public class AnimNumberView extends LinearLayout implements View.OnClickListener {
    private ImageView mImgAdd;
    private ImageView mImgMin;
    private EditText mEditText;

    private ObjectAnimator mAddRightAnim;

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
        PropertyValuesHolder rotationValuesHolder = PropertyValuesHolder.ofFloat("rotation", 0, 360);
        PropertyValuesHolder translationXValuesHolder = PropertyValuesHolder.ofFloat("translationX", 0, w / 2 - mImgAdd.getMeasuredWidth() / 2);
        mAddRightAnim = ObjectAnimator.ofPropertyValuesHolder(mImgAdd, rotationValuesHolder, translationXValuesHolder);
        mAddRightAnim.setDuration(2000);
    }

    @Override
    public void onClick(View v) {
        int count = getCount(mEditText.getText().toString());
        switch (v.getId()) {
            case R.id.btn_add:
                count++;
                mEditText.setText(String.valueOf(count));
                if (count == 1) {
                    mAddRightAnim.start();
                }
                break;
            case R.id.btn_min:
                count--;
                if (count <= 0) {
                    count = 0;
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
}
