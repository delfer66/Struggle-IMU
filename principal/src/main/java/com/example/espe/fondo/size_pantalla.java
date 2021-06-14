package com.example.espe.fondo;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by CNT on 19-dic-15.
 */
public class size_pantalla {
    static String TAG="fondo_size";
    /*Context context;

    public size_pantalla(Context context){
        this.context=context;
    }
    */
    public size_pantalla(){

    }

    public static int getSize(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        log_size(screenLayout);
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return 1;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return 2;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return 3;
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return 4;
            default:
                return 1;
        }
    }

    public static void log_size(int screenLayout){
        //Log.d(TAG, "tamaño: " + String.valueOf(screenLayout));
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Log.i(TAG, "tamaño: Small [1]");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Log.i(TAG, "tamaño: Normal [2]");
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Log.i(TAG, "tamaño: Large [3]");
                break;
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                Log.i(TAG, "tamaño: xLarge [4]");
                break;
            default:
                Log.e(TAG, "tamaño: indefinido [2]");
                break;
        }
    }

}
