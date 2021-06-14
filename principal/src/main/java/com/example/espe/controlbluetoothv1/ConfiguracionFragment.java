package com.example.espe.controlbluetoothv1;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.internal.widget.ButtonBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.espe.musica_control.Configuracion_interface;


public class ConfiguracionFragment extends Fragment implements View.OnClickListener {

    private String TAG="Configuracion";
    public static final int PLAY=0;
    public static final int PAUSA=1;
    public static final int STOP=2;
    LinearLayout skyworld,protectors_of_the_earth,sons_of_war,bloodandsteel;
    View view;
    Button button_pausar,button_parar;
    private Configuracion_interface CI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_configuracion, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        habilitar_botones();

    }

    public void habilitar_botones() {
        Log.d(TAG, "habilitar_botones");
        skyworld = (LinearLayout) view.findViewById(R.id.skyworld);
        skyworld.setOnClickListener(this);
        protectors_of_the_earth = (LinearLayout) view.findViewById(R.id.protectors_of_the_earth);
        protectors_of_the_earth.setOnClickListener(this);
        sons_of_war = (LinearLayout) view.findViewById(R.id.sons_of_war);
        sons_of_war.setOnClickListener(this);
        bloodandsteel = (LinearLayout) view.findViewById(R.id.bloodandsteel);
        bloodandsteel.setOnClickListener(this);
        button_pausar = (Button) view.findViewById(R.id.button_pausar);
        button_pausar.setOnClickListener(this);
        button_parar = (Button) view.findViewById(R.id.button_parar);
        button_parar.setOnClickListener(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /**
        try{
            CI = (Configuracion_interface)activity;
        }catch(ClassCastException e){
            throw  new ClassCastException("Implementar Metodo accion");
        }
         */
    }

    @Override
    public void onClick(View v) {

        int identificacion = v.getId();
        switch (identificacion) {
            case R.id.skyworld:
                CI.accion(PLAY,R.raw.skyworld);
                /*mediaPlayer = MediaPlayer.create(getActivity(), R.raw.skyworld);
                mediaPlayer.start();
                Toast.makeText(getActivity(), R.string.skyworld
                        , Toast.LENGTH_SHORT).show();*/
                break;
            case R.id.protectors_of_the_earth:
                CI.accion(PLAY,R.raw.protectors_of_the_earth);
                break;
            case R.id.sons_of_war:
                CI.accion(PLAY,R.raw.sons_of_war);
                break;
            case R.id.bloodandsteel:
                CI.accion(PLAY,R.raw.bloodandsteel);
                break;
            case R.id.button_pausar:
                CI.accion(PAUSA,0);
                break;
            case R.id.button_parar:
                CI.accion(STOP,0);
                break;
        }
    }


}
