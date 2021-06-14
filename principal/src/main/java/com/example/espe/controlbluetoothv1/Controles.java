package com.example.espe.controlbluetoothv1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.espe.control_batalla.ControlVariables;
import com.example.espe.musica_control.Musica;
import com.example.espe.musica_control.Sonidos;


public class Controles extends Fragment implements OnClickListener
        ,MediaPlayer.OnCompletionListener{


    private String TAG="Controles";
    View view;
    ImageView up,down,der,izq,ataque_derecha,ataque_izquierda;
    TextView txt_boton_pulsado,txt_nota_controles;
    TextView txt_victorias,txt_derrotas,text_estado_battle;
    CheckBox mantener_envio_bluetooth,mantener_envio_bluetooth_const;
    ImageView start_battle,image_reiniciar,imagen_no_rendirme;
    LinearLayout linear_barra_vida,linear_barra_vida_enemigo;
    LinearLayout linear_confirmacion_rendirme;
    LinearLayout linear_test;
    EditText txt_comandos;
    Button button_probar_juego;
    FrameLayout linear_you_win,linear_you_lose;
    FrameLayout frame_confirmacion_rendirme;
    SendMessage SM;
    act_inteface_control act;
    MainActivity MA;
    Boolean hold=false;
    Boolean hold_constante=false;
    Sonidos sonidos;
    Thread hilo_act;
    int activar_linear_test=0;
    int rendirme_count=0;
    private int info_pelea=0;
    public float vida=1.0f;
    public float vida_enemigo=1.0f;
    private int victorias=0;
    private int derrotas=0;
    ControlVariables constante=new ControlVariables();
    int status=0;
    //public MediaPlayer mediaPlayer;
    //public MediaPlayer mediaPlayer=new MediaPlayer();

    interface SendMessage{
         String sendData(String message);
         int start_battle();
         int rendicion_battle();
         void start_battle_consola_comandos(String dato);
        // se puede crear las funciones del tipo q quiera
    }
    interface act_inteface_control{
        boolean confirmacion();
        int estado();
        //float actualizar_barra();
        //float actualizar_barra_enemigo();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            SM = (SendMessage)activity;
            act=(act_inteface_control)activity;
        }catch(ClassCastException e){
            throw  new ClassCastException("Implementar Metodo send data y act interface");
        }
        if (getArguments() != null) {
            info_pelea = getArguments().getInt("info_pelea");
            vida = getArguments().getFloat("vida");
            vida_enemigo = getArguments().getFloat("vida_enemigo");
            victorias = getArguments().getInt("victorias");
            derrotas = getArguments().getInt("derrotas");
            activar_linear_test = getArguments().getInt("activar_linear_test");
            Log.d(TAG,"enviado_arguments= "+ info_pelea+"--"+vida+"--"
                    +vida_enemigo+"--"+victorias+"--"+derrotas+"--"+activar_linear_test);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_controles,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        habilitar_botones();
        if(activar_linear_test==1){
            linear_test.setVisibility(View.VISIBLE);
                    linear_test=(LinearLayout)view.findViewById(R.id.linear_test);
            txt_comandos=(EditText)view.findViewById(R.id.txt_comandos);
            button_probar_juego=(Button)view.findViewById(R.id.button_probar_juego);
        }else{
            linear_test.setVisibility(View.GONE);
        }
        control_txt(info_pelea,false);
        txt_victorias.setText(String.valueOf(victorias));
        txt_derrotas.setText(String.valueOf(derrotas));
        sonidos=new Sonidos(getActivity());
        sonidos.mediaPlayer.setOnCompletionListener(this);
    }

    private void actualizar_barra_inter(float v) {
        disminuir_barra();
    }

    private void actualizar_barra_inter_enemigo(float v) {
        disminuir_barra_enemigo();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        sonidos.liberar_recursos();
        sonidos=new Sonidos(getActivity());
        sonidos.mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        //habilitar_botones();
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        sonidos.liberar_recursos();
    }

    @Override
    public void onClick(View v) {
        //Log.d(TAG, "onClick");
        int identificacion = v.getId();
        switch (identificacion) {
            case R.id.up:
                animacionboton(v,"a");
                sonidos.reproducir_nuevo_sonido(R.raw.button3,0.5f);
                mostrar_consola(SM.sendData("a"));
                break;
            case R.id.down:
                animacionboton(v,"t");
                sonidos.reproducir_nuevo_sonido(R.raw.button3,0.5f);
                mostrar_consola(SM.sendData("t"));
                break;
            case R.id.der:
                animacionboton(v,"d");
                sonidos.reproducir_nuevo_sonido(R.raw.button4, 0.5f);
                mostrar_consola(SM.sendData("d"));
                break;
            case R.id.izq:
                animacionboton(v,"i");
                sonidos.reproducir_nuevo_sonido(R.raw.button4,0.5f);
                mostrar_consola(SM.sendData("i"));
                break;
            case R.id.ataque_derecho:
                animacionboton(v, "v");
                sonidos.reproducir_nuevo_sonido(R.raw.button6);
                vibrar(200);
                mostrar_consola(SM.sendData("v"));
                break;
            case R.id.ataque_izquierdo:
                animacionboton(v, "b");
                sonidos.reproducir_nuevo_sonido(R.raw.button6);
                vibrar(200);
                mostrar_consola(SM.sendData("b"));
                break;
            case R.id.mantener_envio_bluetooth:
                if(mantener_envio_bluetooth.isChecked()){
                    hold=true;
                    txt_nota_controles.setText(R.string.string_controles_hold);
                }else{
                    hold=false;
                    txt_nota_controles.setText(R.string.string_controles_vacia);
                }
                break;
            case R.id.mantener_envio_bluetooth_const:
                if(mantener_envio_bluetooth_const.isChecked()){
                    hold_constante=true;
                    txt_nota_controles.setText(R.string.string_controles_hold_const);
                }else{
                    hold_constante=false;
                    txt_nota_controles.setText(R.string.string_controles_vacia);
                }
                break;
            /*case R.id.linear_barra_vida:
                sonidos.reproducir_nuevo_sonido(R.raw.button2);
                break;*/
            case R.id.start_battle:
                animacionboton(v);
                status=SM.start_battle();
                control_txt(status, true);

                //animacionbotonbarra(v);
                break;
            case R.id.image_reiniciar:
                animacionboton(v);
                rendirme_count++;
                if(rendirme_count==1){
                //esta seguro?
                    frame_confirmacion_rendirme.setVisibility(View.VISIBLE);
                    linear_confirmacion_rendirme.setVisibility(View.VISIBLE);
                }
                if(rendirme_count==2){
                    animacionboton(v);
                    rendirme_count=0;
                    frame_confirmacion_rendirme.setVisibility(View.GONE);
                    linear_confirmacion_rendirme.setVisibility(View.GONE);
                    SM.rendicion_battle();
                }
                break;
            case R.id.imagen_no_rendirme:
                animacionboton(v);
                rendirme_count=0;
                frame_confirmacion_rendirme.setVisibility(View.GONE);
                linear_confirmacion_rendirme.setVisibility(View.GONE);
                break;
            case R.id.button_probar_juego:
                SM.start_battle_consola_comandos(txt_comandos.getText().toString());
                txt_comandos.clearFocus();
                break;
        }
    }

    public void control_txt(int status,Boolean reproducir) {
        int id=0;
        float vol=1.0f;
        switch (status) {
            case 0:
                id=R.raw.tambor_misterio;
                text_estado_battle.setText(R.string.start_text);
                txt_nota_controles.setText(R.string.start_text_ayuda);
                break;
            case ControlVariables.DESCONECT:
                //id=R.raw.tambor_misterio;
                text_estado_battle.setText(R.string.start_text);
                txt_nota_controles.setText(R.string.start_text_ayuda);
                break;
            case ControlVariables.CONECTADO:
                //no poner sonido
                text_estado_battle.setText(R.string.start_text);
                txt_nota_controles.setText(R.string.start_text_ayuda);
                break;
            case ControlVariables.ENVIADOFIGHT:
                //id=R.raw.tambor_misterio;
                text_estado_battle.setText(R.string.cancel_text);
                txt_nota_controles.setText(R.string.cancelar_ayuda);
                break;
            case ControlVariables.READY:
                //??? no utiliza
                txt_nota_controles.setText(R.string.cancelar_ayuda);
                break;
            case ControlVariables.FIGHT:
                //no poner sonido
                text_estado_battle.setText(R.string.reiniciar_text);
                txt_nota_controles.setText(R.string.rendirme_ayuda);
                break;
            case ControlVariables.FIN:
                //no poner sonido
                text_estado_battle.setText(R.string.start_text);
                txt_nota_controles.setText(R.string.start_text_ayuda);
                break;
            case ControlVariables.CANCELADO:
                //id=R.raw.boom;
                text_estado_battle.setText(R.string.cancel_text);
                txt_nota_controles.setText(R.string.cancelar_ayuda);
                break;
            case ControlVariables.ERROR:
                id=R.raw.sierra;
                text_estado_battle.setText("ERROR");
                txt_nota_controles.setText("A existido un error en el Juego :(");
                break;
            case ControlVariables.PEDIDOREINICIAR:
                //id=R.raw.button6;
                text_estado_battle.setText(R.string.reiniciar_confirmacion_text);
                txt_nota_controles.setText(R.string.reiniciar_ayuda);
                break;
            case ControlVariables.ENVIADOREINCIAR:
                //id=R.raw.button5;
                text_estado_battle.setText(R.string.reiniciar_enviado_text);
                txt_nota_controles.setText(R.string.reiniciar_ayuda);
                break;
            default:
                break;
            }
        if(id!=0) {
            if(reproducir) {
                sonidos.reproducir_sig_sonido(id, vol);
            }
        }
    }

    private void mostrar_consola(String dato) {
        txt_boton_pulsado.setText(dato);
        /**Para probar barra*/
        //if(dato=="v" || dato=="b")
        //disminuir_barra();
    }

    public void disminuir_barra(){
        vida-=0.05f;
        if(vida>0) {
            linear_barra_vida.setScaleX(vida);
        }else{
            linear_barra_vida.setScaleX(0f);
            vida=0;
        }
    }

    public void disminuir_barra_enemigo(){
        vida_enemigo-=0.05f;
        if(vida_enemigo>0) {
            linear_barra_vida_enemigo.setScaleX(vida_enemigo);
        }else{
            linear_barra_vida_enemigo.setScaleX(0f);
            vida_enemigo=0;
        }
    }


    public void habilitar_botones(){
        Log.d(TAG, "habilitar_botones");
        up =(ImageView) view.findViewById(R.id.up);
        up.setOnClickListener(this);
        down =(ImageView) view.findViewById(R.id.down);
        down.setOnClickListener(this);
        der =(ImageView) view.findViewById(R.id.der);
        der.setOnClickListener(this);
        izq =(ImageView) view.findViewById(R.id.izq);
        izq.setOnClickListener(this);
        ataque_derecha =(ImageView) view.findViewById(R.id.ataque_derecho);
        ataque_derecha.setOnClickListener(this);
        ataque_izquierda =(ImageView) view.findViewById(R.id.ataque_izquierdo);
        ataque_izquierda.setOnClickListener(this);

        txt_boton_pulsado = (TextView) view.findViewById(R.id.txt_boton_pulsado);
        mantener_envio_bluetooth = (CheckBox) view.findViewById(R.id.mantener_envio_bluetooth);
        mantener_envio_bluetooth.setOnClickListener(this);
        mantener_envio_bluetooth_const = (CheckBox) view.findViewById(R.id.mantener_envio_bluetooth_const);
        mantener_envio_bluetooth.setOnClickListener(this);
        txt_nota_controles = (TextView) view.findViewById(R.id.txt_nota_controles);

        linear_barra_vida=(LinearLayout)view.findViewById(R.id.linear_barra_vida);
        linear_barra_vida.setScaleX(vida);
        linear_barra_vida_enemigo=(LinearLayout)view.findViewById(R.id.linear_barra_vida_enemigo);
        linear_barra_vida_enemigo.setScaleX(vida_enemigo);
        linear_barra_vida.getBackground().setAlpha(Math.round(vida*255));
        linear_barra_vida_enemigo.getBackground().setAlpha(Math.round(vida * 255));

        txt_victorias=(TextView)view.findViewById(R.id.txt_victorias);
        txt_derrotas=(TextView)view.findViewById(R.id.txt_derrotas);
        text_estado_battle=(TextView)view.findViewById(R.id.text_estado_battle);
        start_battle = (ImageView) view.findViewById(R.id.start_battle);
        start_battle.setOnClickListener(this);

        image_reiniciar = (ImageView) view.findViewById(R.id.image_reiniciar);
        image_reiniciar.setOnClickListener(this);
        frame_confirmacion_rendirme=(FrameLayout)view.findViewById(R.id.frame_confirmacion_rendirme);
        frame_confirmacion_rendirme.setVisibility(View.GONE);
        linear_confirmacion_rendirme=(LinearLayout)view.findViewById(R.id.linear_confirmacion_rendirme);
        linear_confirmacion_rendirme.setVisibility(View.GONE);
        imagen_no_rendirme = (ImageView) view.findViewById(R.id.imagen_no_rendirme);
        imagen_no_rendirme.setOnClickListener(this);
        linear_you_win=(FrameLayout)view.findViewById(R.id.linear_you_win);
        linear_you_win.setVisibility(View.GONE);
        linear_you_lose=(FrameLayout)view.findViewById(R.id.linear_you_lose);
        linear_you_lose.setVisibility(View.GONE);

        linear_test=(LinearLayout)view.findViewById(R.id.linear_test);
        txt_comandos=(EditText)view.findViewById(R.id.txt_comandos);
        button_probar_juego=(Button)view.findViewById(R.id.button_probar_juego);
        button_probar_juego.setOnClickListener(this);

    }

    public void animacionboton(View v, final String dato){
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(hold){
                            mostrar_consola(SM.sendData(dato));
                        }
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

    public void animacionboton(View v){
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.setScaleX(0.8f);
                        v.setScaleY(0.8f);
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

    public void animacionbotonbarra(View v){
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.setScaleX(1f);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.setScaleX(1f);
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void vibrar(int tiempo){
        Vibrator v = (Vibrator)  getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(tiempo);
    }

    public void sonido_iniciar_batalla(){
        sonidos.iniciar_batalla();
    }
    public void sonido_you_lose(){
        sonidos.sonido_you_lose();
    }
    public void sonido_you_win(){
        sonidos.sonido_you_win();
    }
}
