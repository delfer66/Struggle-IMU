package com.example.espe.fondo;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.espe.controlbluetoothv1.R;

/**
 * Created by CNT on 19-dic-15.
 */
public class random_fondo {
    String TAG = "random_fondo";
    private int randomNum=0;
    Drawable drawable;
    Context activity;
    Resources resources;
    size_pantalla size;
    public random_fondo(Context activity) {
        this.activity = activity;

        //this.res = activity.getDrawable();
    }
    public void setresources(Resources resources){
        this.resources=resources;
    }


    private int numero_random_sin_repeticion() {
        int aleatorio;
        int maximum = 32, minimum = 0;
        do {
            aleatorio = minimum + (int) (Math.random() * maximum);
        } while (aleatorio == randomNum);
        randomNum = aleatorio;
        Log.d(TAG, "Random fondo: " + randomNum);
        return randomNum;
    }

    public Drawable fondo_random(Resources resources){
        //siempre nesecito a resources
        this.resources=resources;
        //size=new size_pantalla();
        int id=escoger_fondo_random();
        Drawable drawable=resources.getDrawable(id);
        /*
        if(size.getSize(activity)<=2){
            drawable=resize(drawable);
            drawable=cut(drawable);
        }
        */
        return drawable;
    }

    private Drawable resize(Drawable image) {
        //800/376
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 800, 370, false);
        return new BitmapDrawable(resources, bitmapResized);
    }
    private Drawable cut(Drawable image){
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap croppedBitmap = Bitmap
                .createBitmap(b, 0, 25, image.getIntrinsicWidth(), image.getIntrinsicHeight() - 50);
        return new BitmapDrawable(resources,croppedBitmap);
    }

    private int escoger_fondo_random() {
        int id=0;
        switch (numero_random_sin_repeticion()) {
            case 0: {
                id=R.drawable.destruction;
                break;
            }
            case 1: {
                id=R.drawable.paisajeslunanocturnos;
                break;
            }
            case 2: {
                id=R.drawable.paisaje_azul_noche;
                //drawable = activity.getResources().getDrawable(R.drawable.paisaje_azul_noche);
                break;
            }
            case 3: {
                id=R.drawable.blackangel;
                break;
            }
            case 4: {
                id=R.drawable.nuclear;
                break;
            }
            case 5: {
                id=R.drawable.epic_fight_g;
                break;
            }
            case 6: {
                id=R.drawable.fight_for_glory;
                break;
            }
            case 7: {
                id=R.drawable.fist_fight;
                break;
            }
            case 8: {
                id=R.drawable.fist_fight_epic;
                break;
            }
            case 9: {
                id=R.drawable.fenix;
                break;
            }
            case 10: {
                id=R.drawable.god_of_war_asencion;
                break;
            }
            case 11: {
                id=R.drawable.god_of_war_back;
                break;
            }
            case 12: {
                id=R.drawable.god_of_war_fight;
                break;
            }
            case 13: {
                id=R.drawable.god_of_war_front;
                break;
            }
            case 14: {
                id=R.drawable.guilt_war;
                break;
            }
            case 15: {
                id=R.drawable.inferno;
                break;
            }
            case 16: {
                id=R.drawable.marvel;
                break;
            }
            case 17: {
                id=R.drawable.ufc_fight;
                break;
            }
            case 18: {
                id=R.drawable.blue_fire;
                break;
            }
            case 19: {
                id=R.drawable.dragon;
                break;
            }
            case 20: {
                id=R.drawable.epic_fight_armor;
                break;
            }
            case 21: {
                id=R.drawable.mortal_kombat;
                break;
            }
            case 22: {
                id=R.drawable.mortal_kombat_fight;
                break;
            }
            case 23: {
                id=R.drawable.undead_dragon;
                break;
            }
            case 24: {
                id=R.drawable.dragon_fly;
                break;
            }
            case 25: {
                id=R.drawable.cosmic_space;
                break;
            }
            case 26: {
                id=R.drawable.mortal_kombat_logo;
                break;
            }
            case 27: {
                id=R.drawable.raiden_scorpion;
                break;
            }
            case 28: {
                id=R.drawable.bosque_nublado;
                break;
            }
            case 29: {
                id=R.drawable.blackangel;
                break;
            }
            case 30: {
                id=R.drawable.cuasar;
                break;
            }
            case 31: {
                id=R.drawable.planet_destruction;
                break;
            }
            case 32: {
                id=R.drawable.planet_destruction;
                break;
            }
            default:
                return R.drawable.blackangel;
        }
        return id;
    }
}
