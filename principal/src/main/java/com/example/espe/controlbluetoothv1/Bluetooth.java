package com.example.espe.controlbluetoothv1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;


public class Bluetooth extends Fragment implements View.OnClickListener {

    private String TAG="Bluetooth";
    View view;
    TextView textStatus, textRead, enviado,textstart_blue;
    ScrollView desplegar;
    Button bt_test,bt_off_bluetooth,btn_enviar_bluetooth, bt_on_bluetooth,bt_desconectar;
    EditText editText_bluetooth;
    LinearLayout frag_blue;
    public MainActivity MainActivity;
    FrameLayout frame_grande;
    String enviadosave="Ninguno";
    String mainrecibido="";
    ImageView btn_buscar_bluetooth;

    private String mParam1;
    private String mParam2;
    private String mParam3;
    BluetoothSPP bt;

    SendMessage SM;

    interface SendMessage{
        String sendenviado(String message);
        void sendData2(String message);
        void buscar_dispositivo_bluetooth();
        void encender_bluetooth();
        void desconectar_bluetooth();
        void off_bluetooth();
        // se puede crear las funciones del tipo q quiera
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            SM= (SendMessage)activity;
        }catch(ClassCastException e){
            throw  new ClassCastException("Implementar Metodo sendenviado");
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString("enviadosave");
            mParam2 = getArguments().getString("consola_blue");
            mParam3 = getArguments().getString("status_conect");
            Log.d(TAG,"enviado_arguments= "+ mParam1+"--"+mParam2+"--"+mParam3);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_bluetooth,container,false);

        frag_blue =(LinearLayout) view.findViewById(R.id.frag_blue);
        frame_grande=(FrameLayout) view.findViewById(R.id.frame_grande);
        habilitar_botones();


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        enviado.setText(mParam1);
        textRead.setText(mParam2);
        textStatus.setText(mParam3);
        desplegar.fullScroll(View.FOCUS_DOWN);

        String dato=textStatus.getText().toString();
        if(dato!="No Conectado")
            textstart_blue.setVisibility(View.GONE);

    }

    public void habilitar_botones(){
        Log.d(TAG, "habilitar_botones");
        textRead = (TextView)view.findViewById(R.id.textRead);
        textStatus = (TextView)view.findViewById(R.id.textStatus);
        enviado = (TextView)view.findViewById(R.id.enviado);
        bt_test= (Button) view.findViewById(R.id.bt_test);
        bt_test.setOnClickListener(this);
        bt_off_bluetooth= (Button) view.findViewById(R.id.bt_off_bluetooth);
        bt_off_bluetooth.setOnClickListener(this);
        bt_on_bluetooth= (Button) view.findViewById(R.id.bt_on_bluetooth);
        bt_on_bluetooth.setOnClickListener(this);
        bt_desconectar= (Button) view.findViewById(R.id.bt_desconectar);
        bt_desconectar.setOnClickListener(this);
        btn_enviar_bluetooth= (Button) view.findViewById(R.id.btn_enviar_bluetooth);
        btn_enviar_bluetooth.setOnClickListener(this);
        btn_buscar_bluetooth =(ImageView) view.findViewById(R.id.btn_buscar_bluetooth);
        btn_buscar_bluetooth.setOnClickListener(this);
        editText_bluetooth =(EditText) view.findViewById(R.id.editText_bluetooth);
        textstart_blue= (TextView)view.findViewById(R.id.textstart_blue);
        desplegar=(ScrollView)view.findViewById(R.id.desplegar);
        desplegar.fullScroll(View.FOCUS_DOWN);

    }
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        int identificacion = v.getId();
        switch (identificacion) {
            case R.id.bt_test:
                enviado.setText("123456789");
                SM.sendData2("123456789");
                /**
                mainrecibido=SM.sendenviado("s");
                Controles fragInfo = new Controles();
                //fragInfo.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.consola_bluetooth, fragInfo).commit();
                //Log.d(TAG, mainrecibido);
                 */
                break;
            case R.id.bt_on_bluetooth:
                SM.encender_bluetooth();
                break;
            case R.id.bt_off_bluetooth:
                SM.off_bluetooth();
                break;
            case R.id.btn_enviar_bluetooth:
                String dato=editText_bluetooth.getText().toString();
                enviado.setText(dato);
                SM.sendData2(dato);
                break;
            case R.id.btn_buscar_bluetooth:
                textstart_blue.setVisibility(View.GONE);
                animacionboton(v);
                SM.buscar_dispositivo_bluetooth();
                break;
            case R.id.bt_desconectar:
                SM.desconectar_bluetooth();
                break;
        }
    }
/*
    public void actualizarenviado(String message){
        Log.d(TAG, "getData");
        enviado.setText(message);
    }*/

    public void getData(String message){
        Log.d(TAG, "getData");
        this.enviadosave=message;
                //+ "\n";

        //enviado.setText(message);
    }

    public void desaparecer(){
        //if(isVisibility())

        frag_blue.setVisibility(View.GONE);
        frag_blue.getLayoutParams().width = Gallery.LayoutParams.WRAP_CONTENT;
        //frame_grande.getLayoutParams().width = Gallery.LayoutParams.WRAP_CONTENT;
        //frame_grande.setVisibility(View.GONE);
    }
    public void aparecer(){
        //if(!isVisibility())
        frag_blue.setVisibility(View.VISIBLE);
        frag_blue.getLayoutParams().width = Gallery.LayoutParams.MATCH_PARENT;
//        frame_grande.setVisibility(View.VISIBLE);
        //frame_grande.getLayoutParams().width = Gallery.LayoutParams.MATCH_PARENT;
    }
    public boolean isVisibility(){
        int visible=frag_blue.getVisibility();
        if(visible==View.VISIBLE){
            return true;
        }else{
            return false;
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
                        v.getBackground().clearColorFilter();
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
