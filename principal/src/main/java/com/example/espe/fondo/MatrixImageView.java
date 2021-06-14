package com.example.espe.fondo;

import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * Created by CNT on 19-dic-15.
 */
public class MatrixImageView {
    private final ImageView mImageView;
    private float mScaleFactor;
    private final Matrix mMatrix;

    public MatrixImageView(ImageView imageView, float scaleFactor, Matrix mMatrix) {
        this.mImageView = imageView;
        this.mScaleFactor = scaleFactor;
        this.mMatrix = mMatrix;
    }

    public void setMatrixTranslateX(float dx) {
        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postTranslate(dx, 0);
        mImageView.setImageMatrix(mMatrix);
    }
}
