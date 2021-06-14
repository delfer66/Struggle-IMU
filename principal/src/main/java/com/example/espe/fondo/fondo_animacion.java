package com.example.espe.fondo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.espe.controlbluetoothv1.MainActivity;
import com.example.espe.controlbluetoothv1.R;

/**
 * Created by CNT on 19-dic-15.
 */
public class fondo_animacion extends MainActivity{
/*
    private static final int RightToLeft = 1;
    private static final int LeftToRight = 2;
    private static final int DURATION = 5000;
    public int mDirection = RightToLeft;

    public fondo_animacion() {

    }

    public void iniciar(){

    }

    public void animate() {
        updateDisplayRect();
        mDisplayRect.set(0, 0, fondo.getDrERRORe().getIntrinsicWidth(), fondo.ERRORkjn().getIntrinsicHeight());
        mMatrix.mapRect(mDisplayRect);
        if(mDirection == RightToLeft) {
            animate(mDisplayRect.left, mDisplayRect.left - (mDisplayRect.right - fondo.getWidth()));
        } else {
            animate(mDisplayRect.left, 0.0f);
        }
    }

    private void animate(float from, float to) {
        mCurrentAnimator = ValueAnimator.ofFloat(from, to);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();

                mMatrix.reset();
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mMatrix.postTranslate(value, 0);

                fondo.setImageMatrix(mMatrix);

            }
        });
        mCurrentAnimator.setDuration(DURATION);
        mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(mDirection == RightToLeft)
                    mDirection = LeftToRight;
                else
                    mDirection = RightToLeft;

                animate();
            }
        });
        mCurrentAnimator.start();
    }

    private void updateDisplayRect() {
        mDisplayRect.set(0, 0, fondo.getBackground().getIntrinsicWidth(), fondo.getBackground().getIntrinsicHeight());
        mMatrix.mapRect(mDisplayRect);
    }


    */
public void prueba() {
    if (fondo == null) {
        Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
    } else {
        if (fondo.getHeight() != 0) {
            Log.d("fondo", String.valueOf(fondo.getHeight()));
            Log.d("fondo", String.valueOf(fondo.getDrawable().getIntrinsicHeight()));
            Log.d("fondo", String.valueOf(fondo.getDrawable().getIntrinsicWidth()));
        }
    }
}
    }
