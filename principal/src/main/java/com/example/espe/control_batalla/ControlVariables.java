package com.example.espe.control_batalla;

/**
 private static final int pulsado_start_battle = 1;
 private static final int wait_enemy = 2;
 private static final int confirmed_enemy = 3;
 private static final int starting_battle = 4;
 private static final int started_battle = 5;
 private static final int finish_battle = 6;
 private static final int I_WIN = 1;
 private static final int I_LOST = 2;
 private static final int ENEMY_WIN = 3;
 private static final int ENEMY_LOST = 4;
 */
public class ControlVariables {

    public Boolean BLUETOOTH_CONECTED = false;
    public Boolean WIFI_CONECTED = false;
    public Boolean estan_peleando = false;
    public Boolean Cortar_Comunicacion_Robot = false; //paro el envio de datos al robot
    public Boolean recibido_fight = false;     //si se toca un boton se descarta
    public Boolean recibido_reiniciar = false; //si se toca un boton se descarta

    //public int recibido_resultado = 0;    //prioridad1
    //public int enviado_resultado = 0;     //mi resultado
    private static final int WIN = 21;
    private static final int LOST = 22;
    private static final int EMPATE = 23;


    //public int info_pelea = 0;       //prioridad2
    public final static int DESCONECT = 1;
    public final static int CONECTADO = 2;
    public final static int ENVIADOFIGHT = 3;
    public final static int READY = 4;
    public final static int FIGHT = 5;
    public final static int FIN = 6;
    public final static int CANCELADO = 7;
    public final static int ERROR = 8;
    public final static int PEDIDOREINICIAR = 9;
    public final static int ENVIADOREINCIAR = 10;

//datos
//public float vida=1.0f;
//public float vida_enemigo=1.0f;
//public int victorias=0;
//public int derrotas=0;

//
//public int numero_pulsaciones=0;

    public String texto_info_pelea(int dato) {
        switch (dato) {
            case 0:

                return "Default";
            case DESCONECT:

                return "DESCONECT";
            case CONECTADO:

                return "CONECTADO";
            case ENVIADOFIGHT:

                return "ENVIADOFIGHT";
            case READY:

                return "READY";
            case FIGHT:
                //deberia ser FIGHT
                return "FIGHTING";
            case FIN:

                return "FIN";
            case CANCELADO:

                return "CANCELADO";
            case ERROR:

                return "ERROR";
            case PEDIDOREINICIAR:

                return "PEDIDOREINICIAR";
            case ENVIADOREINCIAR:

                return "ENVIADOREINCIAR";
            default:

                return "ERROR DE RANGO";
        }
    }

    public String texto_resultado(int dato) {
        switch (dato) {
            case 0:

                return "Default";
            case WIN:

                return "WIN";
            case LOST:

                return "LOST";
            case EMPATE:

                return "EMPATE";
            default:

                return "ERROR DE RANGO";
        }
    }

    public int DESCONECT(){
        return DESCONECT;
    }

    public int CONECTADO(){
        return CONECTADO;
    }
    public int ENVIADOFIGHT(){
        return ENVIADOFIGHT;
    }
    public int READY(){
        return READY;
    }
    public int FIGHT(){
        return FIGHT;
    }
    public int FIN(){
        return FIN;
    }
    public int CANCELADO(){
        return CANCELADO;
    }
    public int ERROR(){
        return ERROR;
    }
    public int PEDIDOREINICIAR(){
        return PEDIDOREINICIAR;
    }
    public int ENVIADOREINCIAR(){
        return ENVIADOREINCIAR;
    }
}
