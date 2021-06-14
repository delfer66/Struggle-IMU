package com.example.espe.musica_control;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;
import android.widget.Toast;

import com.example.espe.controlbluetoothv1.ComunicacionFragment;
import com.example.espe.controlbluetoothv1.MainActivity;
import com.example.espe.controlbluetoothv1.R;

import java.io.IOException;

public class Musica implements Configuracion_interface {//implements OnCompletionListener, OnErrorListener{
    public MediaPlayer mediaPlayer=new MediaPlayer();
    private Context activity;
    private String TAG="Musica";
    private int randomNum;
    private int next=0;
    private int lose=0;
    float volumen_general=0.5f;
    Boolean Reproduccion_Continua=true; // reproduccion continua de musica
    Configuracion_interface CI;


    public static final int PLAY=0;
    public static final int PAUSA=1;
    public static final int STOP=2;

    public static final int Audio_Reproduciendo=0;
    public static final int Musica_Fondo=21;
    public static final int Musica_Juego=22;
    public static final int Musica_Jugador_Perdiendo=23;
    public static final int Sonido_Inicio_Pelea=11;
    public static final int Sonido_Victoria=12;
    public static final int Sonido_Derrota=13;
    public static final int Sonido_Botones_Default=5;
    public static final int Sonido_Botones_Tipo1=6;
    public static final int Sonido_Botones_Tipo2=7;

    /**
     * estado=0 - Musica de fondo;
     * estado=1 - Musica de juego;
     * estado=2 - Musica de jugador Perdiendo
     * estado=3 - Musica de inicio de
     */

    /*
    public Musica(Context current) {
        this.activity = current;
    }
*/



    public Musica(Context current){
        this.activity = current;


/*
        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2) {
                // TODO Auto-generated method stub
                //your code if any error occurs while
                // playing even you can show an alert to user
                Log.d(TAG, "Error: " + paramInt1 + "//" + paramInt2);
                Toast.makeText(activity, "Error al reproducir: "
                        + paramInt1 + "//" + paramInt2, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "Iniciando Nueva Musica de Fondo");
                mp.stop();
                musica_fondo_random();
                mediaPlayer.setOnCompletionListener(this);
            }
        });*/
    }


    public Boolean isPlayed(){
        return mediaPlayer.isPlaying();
    }

    public void musica_fondo_next(){

        lista_canciones(next);

        if(mediaPlayer!=null){
            volumen_General();
            mediaPlayer.start();
        }
        next++;
        if(next>=9){
            next=0;
        }
    }


    public void musica_fondo_random(){
    lista_canciones(numero_random_sin_repeticion());
        if(mediaPlayer!=null){
            volumen_General();
            mediaPlayer.start();
            //mediaPlayer.setVolume(0.5f,0.5f);
        }
    }


    public void musica_lose(){
        lista_canciones_loseing(lose);
        if(mediaPlayer!=null){
            mediaPlayer.setVolume(1.0f,1.0f); // es la unica al 100% de volumen
            mediaPlayer.start();
        }
        lose++;
        if(lose>1){
            lose=0;
        }
    }

    public void musica_creditos(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.si_nesecita_reggaeton_dale);
        if(mediaPlayer!=null) {
            mediaPlayer.setVolume(1.0f, 1.0f); // es la unica al 100% de volumen
            mediaPlayer.start();
        }
    }

    public void lista_canciones(int num){
        switch (num) {
            case 0: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.skyworld);
                //mediaPlayer = MediaPlayer.create(activity, R.raw.prueba);

                Toast.makeText(activity, R.string.skyworld
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 1: {

                //mediaPlayer = MediaPlayer.create(activity, R.raw.prueba);
                mediaPlayer = MediaPlayer.create(activity, R.raw.protectors_of_the_earth);
                Toast.makeText(activity,R.string.protectors_of_the_earth
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.sons_of_war);
                //mediaPlayer = MediaPlayer.create(activity, R.raw.prueba);
                Toast.makeText(activity,R.string.sons_of_war
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.blackheart);
                Toast.makeText(activity,R.string.blackheart
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 4: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.strength_of_a_thousand_men);
                Toast.makeText(activity,R.string.strength_of_a_thousand_men
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 5: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.imperitum);
                Toast.makeText(activity,R.string.imperitum
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 6: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.mortalgladiator);
                Toast.makeText(activity,R.string.mortalgladiator
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 7: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.lacrimosa_dominae);
                Toast.makeText(activity,R.string.bloodandsteel
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 8: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.bloodandsteel);
                Toast.makeText(activity,R.string.bloodandsteel
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case 9: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.bloodandsteel);
                Toast.makeText(activity,R.string.bloodandsteel
                        , Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public void lista_canciones_loseing(int num) {
        switch (num) {
            case 0: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.sons_of_war);
                break;
            }
            case 1: {
                mediaPlayer = MediaPlayer.create(activity, R.raw.bloodandsteel);
                break;
            }
        }
    }

    public int numero_random_sin_repeticion(){
        int aleatorio;
        int maximum=9,minimum=0;
        do {
            aleatorio = minimum + (int)(Math.random()*maximum);
        }while (aleatorio==randomNum);
        randomNum=aleatorio;
        Log.d(TAG, "Random: " + randomNum);
        return randomNum;
    }

    public void reproducir_nueva_musica_fondo(int id_musica) {
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, id_musica);
        volumen_General();
        mediaPlayer.start();
    }

    public void play_pausar_musica_fondo(){
        //No recomendado
        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Toast.makeText(activity, "Pausa Musica"
                        , Toast.LENGTH_SHORT).show();
            } else {
                volumen_General();
                mediaPlayer.start();
                Toast.makeText(activity, "Play Musica"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parar_musica_fondo(){
        liberar_recursos();
            Toast.makeText(activity,"Stop Musica"
                    , Toast.LENGTH_SHORT).show();
    }

    public void liberar_recursos(){
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void Reproduccion_Finalizada(){
        /**implementar un control de produccion Finalizada
         * */
        liberar_recursos();
        musica_fondo_random();
    }

    public void next_random_song(){
        /**implementar un control de produccion Finalizada
         * */
        liberar_recursos();
        musica_fondo_random();
    }

    public void next_song(){
        /**implementar un control de produccion Finalizada
         * */
        liberar_recursos();
        musica_fondo_next();
    }
    public void next_lose_song(){
        /**implementar un control de produccion Finalizada
         * */
        liberar_recursos();
        musica_lose();
    }

    public Boolean Continuar_Reproduccion_Continua(){
        //Se debe continuar de reproducir?
        return Reproduccion_Continua;
    }

    @Override
    public String accion(int accion, int id_accion) {

        switch (accion) {
            case PLAY: {
                break;
            }
            case PAUSA: {
                break;
            }
            case STOP: {
                break;
            }
        }

        return null;
    }

    public void volumen_General(){
        mediaPlayer.setVolume(volumen_general,volumen_general);
    }
}
