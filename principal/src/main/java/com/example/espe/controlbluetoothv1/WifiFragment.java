package com.example.espe.controlbluetoothv1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class WifiFragment extends Fragment implements View.OnClickListener {

    private String TAG="WifiFragment";
    LinearLayout frag_wifi;
    View view;
    ProgressDialog progressDialog = null;
    ImageView boton_inicio_wifi;
    Envio_Inicio_Wifi SM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wifi,container,false);
        frag_wifi =(LinearLayout) view.findViewById(R.id.frag_wifi);
        //habilitar_botones();
        //return inflater.inflate(R.layout.fragment_controles, container, false);
        boton_inicio_wifi =(ImageView) view.findViewById(R.id.boton_inicio_wifi);
        boton_inicio_wifi.getBackground().clearColorFilter();
        boton_inicio_wifi.setScaleX(1f);
        boton_inicio_wifi.setScaleY(1f);
        boton_inicio_wifi.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            SM= (Envio_Inicio_Wifi)activity;
        }catch(ClassCastException e){
            throw  new ClassCastException("Implementar Metodo envio_inicio_wifi");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    interface Envio_Inicio_Wifi{
        void start_wifi();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        int identificacion = v.getId();
        switch (identificacion) {
            case R.id.boton_inicio_wifi:
                //animacionboton(v);
                v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                v.setScaleX(0.9f);
                v.setScaleY(0.9f);
                boton_inicio_wifi.setClickable(false);
                SM.start_wifi();
                break;
        }
    }

    public void animacionboton(View v){
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.setScaleX(0.9f);
                        v.setScaleY(0.9f);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //v.getBackground().clearColorFilter();
                        v.setScaleX(1f);
                        v.setScaleY(1f);
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

}
