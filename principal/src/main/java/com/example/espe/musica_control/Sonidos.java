package com.example.espe.musica_control;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.espe.controlbluetoothv1.R;

/**
 * Created by Fer on 15-dic-15.
 */
public class Sonidos {
    public MediaPlayer mediaPlayer=new MediaPlayer();
    private Context activity;
    private String TAG="Sonidos";
    private int randomNum;
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

    public Sonidos(Context current){
        this.activity = current;
    }

    public MediaPlayer Sonidos(Context current){
        this.activity = current;
        return mediaPlayer;
    }

    public void iniciar_batalla(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.fight);
        mediaPlayer.start();
    }

    public void iniciar_desconectado(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.tambor_misterio);
        mediaPlayer.start();
    }

    public void iniciar_reiniciar(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.boom);
        mediaPlayer.setVolume(1.0f,1.0f);
        mediaPlayer.start();
    }

    public void sonido_you_lose(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.you_lose);
        mediaPlayer.setVolume(1.0f,1.0f);
        mediaPlayer.start();
    }

    public void sonido_you_win(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.you_win);
        mediaPlayer.setVolume(1.0f,1.0f);
        mediaPlayer.start();
    }

    public void sonido_boom(){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, R.raw.boom);
        mediaPlayer.setVolume(1.0f,1.0f);
        mediaPlayer.start();
    }

    public void reproducir_sig_sonido(int id,float vol){
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, id);
        mediaPlayer.setVolume(vol,vol);
        mediaPlayer.start();
    }

    public void sonido_random(){
            switch (numero_random_sin_repeticion()) {
                case 0: {
                    mediaPlayer = MediaPlayer.create(activity, R.raw.button1);
                    mediaPlayer.start();
                    Toast.makeText(activity, R.string.skyworld
                            , Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1: {

                    mediaPlayer = MediaPlayer.create(activity, R.raw.button2);
                    mediaPlayer.start();
                    Toast.makeText(activity,R.string.protectors_of_the_earth
                            , Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    mediaPlayer = MediaPlayer.create(activity, R.raw.button3);
                    mediaPlayer.start();
                    Toast.makeText(activity,R.string.sons_of_war
                            , Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    mediaPlayer = MediaPlayer.create(activity, R.raw.button4);
                    mediaPlayer.start();
                    Toast.makeText(activity,R.string.bloodandsteel
                            , Toast.LENGTH_SHORT).show();
                    break;
                }
            }
    }

    public int numero_random_sin_repeticion(){
        int aleatorio;
        int maximum=4,minimum=0;
        do {
            aleatorio = minimum + (int)(Math.random()*maximum);
        }while (aleatorio==randomNum);
        randomNum = aleatorio;
        Log.d(TAG, "Random: " + randomNum);
        return randomNum;
    }

    public void reproducir_nuevo_sonido(int id_musica) {
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, id_musica);
        mediaPlayer.start();
    }
    public void reproducir_nuevo_sonido(int id_musica,float volumen) {
        liberar_recursos();
        mediaPlayer = MediaPlayer.create(activity, id_musica);
        mediaPlayer.setVolume(volumen,volumen);
        mediaPlayer.start();
    }

    public void play_pausar_sonido_fondo(){
        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Toast.makeText(activity, "Pausa Musica"
                        , Toast.LENGTH_SHORT).show();
            } else {
                mediaPlayer.start();
                Toast.makeText(activity, "Play Musica"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parar_sonido_fondo(){
        liberar_recursos();
            Toast.makeText(activity,"Stop Musica"
                    , Toast.LENGTH_SHORT).show();
    }

    public void Reproduccion_Finalizada(MediaPlayer mp){
        /**implementar un control de produccion Finalizada
         * */
        liberar_recursos();
        sonido_random();
    }

    public Boolean Continuar_Reproduccion_Continua(){
        //Se debe continuar de reproducir?
        return Reproduccion_Continua;
    }

    public void liberar_recursos(){
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG,"sonidos liberados");
        }
    }

}
