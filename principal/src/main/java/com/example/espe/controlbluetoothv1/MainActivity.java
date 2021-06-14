package com.example.espe.controlbluetoothv1;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener;

import com.example.espe.fondo.MatrixImageView;
import com.example.espe.musica_control.Configuracion_interface;
import com.example.espe.musica_control.Sonidos;
import com.example.espe.wifi_libreria.ChatManager;
import com.example.espe.wifi_libreria.ClientSocketHandler;
import com.example.espe.wifi_libreria.GroupOwnerSocketHandler;
import com.example.espe.wifi_libreria.WiFiChatFragment;
import com.example.espe.wifi_libreria.WiFiChatFragment.MessageTarget;
import com.example.espe.wifi_libreria.WiFiChatFragment.wifi_fragmento;
import com.example.espe.wifi_libreria.WiFiDirectBroadcastReceiver;
import com.example.espe.wifi_libreria.WiFiDirectServicesList;
import com.example.espe.wifi_libreria.WiFiDirectServicesList.DeviceClickListener;
import com.example.espe.wifi_libreria.WiFiDirectServicesList.WiFiDevicesAdapter;
import com.example.espe.wifi_libreria.WiFiP2pService;

import com.example.espe.musica_control.Musica;
import com.example.espe.fondo.fondo_animacion;
import com.example.espe.fondo.random_fondo;
import com.example.espe.fondo.size_pantalla;
import com.example.espe.control_batalla.ControlVariables;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


//public class MainActivity extends AppCompatActivity implements OnClickListener {
public class MainActivity extends AppCompatActivity implements
        Controles.SendMessage, Controles.act_inteface_control
        ,Bluetooth.SendMessage
        ,WifiFragment.Envio_Inicio_Wifi
        ,DeviceClickListener, Handler.Callback, MessageTarget,
        WifiP2pManager.ConnectionInfoListener
        ,MediaPlayer.OnCompletionListener
        ,wifi_fragmento {

    /**
     * Variable Wi-Fi
     */
    // TXT RECORD properties
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    //public static final String SERVICE_INSTANCE = "_wifidemotest";
    public static final String SERVICE_INSTANCE = "_ServidorFer";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    public static final int MESSAGE_READ = 0x400 + 1;
    public static final int MY_HANDLE = 0x400 + 2;
    private WifiP2pManager manager;

    static final int SERVER_PORT = 4545;

    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;

    private Handler handler = new Handler(this);
    private WiFiChatFragment chatFragment;
    private WiFiDirectServicesList servicesList;
    private WifiManager wifiManager;//controla el wifi en genereal
    private ChatManager chatManager; //necesario para enviar datos por wifi
    private WifiP2pDnsSdServiceInfo copiaservice; //necesario para parar LocalService
    private int estado_coneccion_wifi = 0;
    private int add_coneccion_wifi = 0;
    private Boolean continuar_wifi = false; //variable q permite continuar si todo esta correto
    private Boolean debe_apagar_wifi = false; //variable q permite continuar si todo esta correto
    private static final int localServer_añadido = 1;
    private static final int serviceRequest_añadido = 2;
    private static final int group_añadido = 3;
    private static final int buscando = 1;
    private static final int conectando = 2;
    private static final int conectado = 3;

    public TextView consola_control_wifi, consola_control_bluetooth;
    public Boolean group, clear, ser;

    @Override
    public Handler getHandler() {
        return handler;
    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Termina Variable Wi-Fi
     */
    private int posicion = 0; //fragmento en el cual se encuentra el usuario
    private int posicion_act_wifi = 0;    //estado de la comunicacion Wi-fi
    private String TAG = "MainActivity";
    private String TAGWIFI = "WIFI_log";
    BluetoothSPP bt;
    Boolean bluetooth_activity_parear = false;
    View v;
    private Bluetooth Bluetooth = new Bluetooth();
    private WifiFragment WifiFragment;
    private Controles Controles = new Controles();
    public String enviadosave = "Ninguno", consola_blue = "Consola",
            status_conect = "No Conectado",
            dato_recibido_bluetooth = "Ninguno";   //
    public String wifi_mi_dato = "MI", wifi_consola = "Consola", wifi_status = "status", dato_recibido_wifi = "Otro";

    Musica musica = new Musica(this);
    Sonidos sonidos = new Sonidos(this);
    fondo_animacion fondo_animacion;


    private static final int RightToLeft = 1;
    private static final int LeftToRight = 2;
    private static final int DURATION = 25000;
    public int mDirection = RightToLeft;

    public ValueAnimator mCurrentAnimator;
    public Matrix mMatrix;
    public ImageView fondo;
    public RectF mDisplayRect = new RectF();
    public float mScaleFactor;

    Boolean existe_conec_bluetooth = false;   //Determina si se a conectado por bluetooth a un robot
    Object objg;
    Bundle args, argswifi;
    FragmentTransaction transicion;
    Fragment fragmento;
    Menu menu;
    public Matrix matrix;
    random_fondo random_fondo = new random_fondo(this);
    Animation anim_cambio_img;
    public Resources resources;
    public Context context;
    //MediaPlayer mediaPlayer=new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .replace(R.id.principal, Controles, "principaltag").commit();


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//Horizontal
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//Vertical
        consola_control_bluetooth = (TextView) findViewById(R.id.consola_control_bluetooth);
        consola_control_wifi = (TextView) findViewById(R.id.consola_control_wifi);

        /** onCreate Wi-Fi*/
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        /** onCreate Wi-Fi fin*/
        /**Fondo_animado*/
        anim_cambio_img = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fondo = (ImageView) findViewById(R.id.fondo);
        mMatrix = fondo.getImageMatrix();
        fondo_animacion = new fondo_animacion();
        context = getApplicationContext();

        resources = context.getResources();
        //oojo esto es lo ultimo q cambie
        //random_fondo.setresources(resources);
        //fondo.setImageDrawable(random_fondo.fondo_random(resources));
        fondo.post(new Runnable() {
            @Override
            public void run() {
                //prueba();
                medidas_pantalla();
                fondo.setImageDrawable(random_fondo.fondo_random(resources));
                mScaleFactor = (float) fondo.getHeight() / (float) fondo.getDrawable().getIntrinsicHeight();
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                fondo.setImageMatrix(mMatrix);
                animate();
            }
        });
        /**Fondo animado*/
        bt = new BluetoothSPP(this);

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                consola_blue += message + "\n";
                dato_recibido_bluetooth = message;
                recibi_NUEVO_DATO_bluetooth(message);
                Actualizar_ventana_bluetooth();      //envia datos al fragmento
                actualizar_ventana_bluetooth_frame();//actualiza frament bluetooth
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                Log.v(TAG, "No conectado o desconeccion de dispositivo");
                existe_conec_bluetooth = false;
                status_conect = "No Conectado";
                Actualizar_ventana_bluetooth();      //envia datos al fragmento
                actualizar_ventana_bluetooth_frame();//actualiza frament bluetooth
            }

            public void onDeviceConnectionFailed() {
                existe_conec_bluetooth = false;
                status_conect = "Conección Fallida";
                Actualizar_ventana_bluetooth();      //envia datos al fragmento
                actualizar_ventana_bluetooth_frame();//actualiza frament bluetooth
            }

            public void onDeviceConnected(String name, String address) {
                status_conect = "Conectado: " + name;
                Toast.makeText(getApplicationContext(), status_conect, Toast.LENGTH_SHORT).show();
                existe_conec_bluetooth = true;
                Actualizar_ventana_bluetooth();       //envia datos al fragmento
                actualizar_ventana_bluetooth_frame(); //actualiza frament bluetooth
            }
        });

    }

    public void medidas_pantalla() {
        size_pantalla size = new size_pantalla();
        size.getSize(this);
        Log.d("fondo_size", "Height " + String.valueOf(fondo.getHeight()));
        Log.d("fondo_size", "Width " + String.valueOf(fondo.getWidth()));
        Log.d("fondo_size", "Intrins Height " + String.valueOf(fondo.getDrawable().getIntrinsicHeight()));
        Log.d("fondo_size", "Intrins Width " + String.valueOf(fondo.getDrawable().getIntrinsicWidth()));
    }

    public void prueba() {
        if (fondo == null) {
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
        } else {
            if (fondo.getHeight() != 0) {

                Drawable drawable = resources.getDrawable(R.drawable.mortal_kombat_logo);
                drawable = resize(drawable);
                drawable = cut(drawable);
                fondo.setImageDrawable(drawable);
                //fondo.setImageDrawable(resources.getDrawable(R.drawable.dragon_fly));
                Log.d("fondo", "Height " + String.valueOf(fondo.getHeight()));
                Log.d("fondo", "Width " + String.valueOf(fondo.getWidth()));
                Log.d("fondo", "Intrins Height " + String.valueOf(fondo.getDrawable().getIntrinsicHeight()));
                Log.d("fondo", "Intrins Width " + String.valueOf(fondo.getDrawable().getIntrinsicWidth()));

            }
        }
    }

    private Drawable resize(Drawable image) {
        //800/376
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 800, 350, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    private Drawable cut(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap croppedBitmap = Bitmap
                .createBitmap(b, 0, 25, image.getIntrinsicWidth(), image.getIntrinsicHeight() - 50);
        return new BitmapDrawable(getResources(), croppedBitmap);
    }

    public void animate() {
        updateDisplayRect();
        mDisplayRect.set(0, 0, fondo.getDrawable().getIntrinsicWidth(), fondo.getDrawable().getIntrinsicHeight());
        mMatrix.mapRect(mDisplayRect);
        if (mDirection == RightToLeft) {
            animate(mDisplayRect.left, mDisplayRect.left - (mDisplayRect.right - fondo.getWidth()));
        } else {
            animate(mDisplayRect.left, 0.0f);
        }
    }

    private void animate(float from, float to) {
        final MatrixImageView matrixImageView = new MatrixImageView(fondo, mScaleFactor, mMatrix);
        mCurrentAnimator = ObjectAnimator.ofFloat(matrixImageView, "matrixTranslateX", from, to);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                matrixImageView.setMatrixTranslateX(value);
            }
        });
        mCurrentAnimator.setDuration(DURATION);
        mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mDirection == RightToLeft) {
                    mDirection = LeftToRight;
                } else {
                    mDirection = RightToLeft;
                }
                //resources = getApplicationContext().getResources();

                fondo.setImageDrawable(random_fondo.fondo_random(resources));
                fondo.startAnimation(anim_cambio_img);
                animate();
            }
        });
        mCurrentAnimator.start();
    }

    private void animate1(float from, float to) {
        mCurrentAnimator = ValueAnimator.ofFloat(from, to);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mMatrix.reset();
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mMatrix.postTranslate(value, 0);
                fondo.setImageMatrix(mMatrix);
            }
        });
        mCurrentAnimator.setDuration(DURATION);
        mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (mDirection == RightToLeft)
                    mDirection = LeftToRight;
                else
                    mDirection = RightToLeft;
                animate();
            }
        });
        mCurrentAnimator.start();
    }

    private void updateDisplayRect() {
        mDisplayRect.set(0, 0, fondo.getDrawable().getIntrinsicWidth(), fondo.getDrawable().getIntrinsicHeight());
        mMatrix.mapRect(mDisplayRect);
    }

    /**
     * Actualiza el Fragmento si hay un nuevo dato para el bluetooth
     * EXCLUSIVAMENTE EN LA POSICION 2 o CUANDO EL FRAGMENTO BLUETOOTH
     * SEA VISIBLE
     */
    private void actualizar_ventana_bluetooth_frame() {

        if (posicion == 2 && !bluetooth_activity_parear) {
            transicion = getFragmentManager().beginTransaction();
            Bluetooth = new Bluetooth();
            Bluetooth.setArguments(args);
            transicion.replace(R.id.principal, Bluetooth).commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "onCreateOptionsMenu");
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_coneccion, menu);
        ActionBar actionBar = getActionBar();
        //actionBar.setElevation();getHeight()
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        //Fragment frag = getFragmentManager().findFragmentById(R.id.principal);
        transicion = getFragmentManager().beginTransaction();
        switch (item.getItemId()) {

            case R.id.menu_controles:
                posicion = 0;
                Log.d(TAG, "posicion: " + posicion);
                Actualizar_datos_Controles();
                Controles =new Controles();
                Controles.setArguments(args);
                transicion.setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out);
                transicion.replace(R.id.principal, Controles);
                transicion.commit();
                //Toast.makeText(getApplicationContext(), "Controles", Toast.LENGTH_SHORT).show();
                reproducir_sonido(R.raw.button2, 1.0f);
                return true;
            case R.id.menu_wifi:
                posicion = 1;
                Log.d(TAG, "posicion: " + posicion);
                cambio_wifi(transicion);
                //Toast.makeText(getApplicationContext(), "Wifi", Toast.LENGTH_SHORT).show();
                reproducir_sonido(R.raw.button2, 1.0f);
                return true;
            case R.id.menu_bluetooth:
                posicion = 2;
                Log.d(TAG, "posicion: " + posicion);
                Actualizar_ventana_bluetooth();
                //encender_bluetooth();
                Bluetooth = new Bluetooth();
                Bluetooth.setArguments(args);
                transicion.setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out);
                transicion.replace(R.id.principal, Bluetooth).commit();
                //Toast.makeText(getApplicationContext(), "Bluetooth", Toast.LENGTH_SHORT).show();
                reproducir_sonido(R.raw.button2, 1.0f);
                return true;

            case R.id.menu_configurar_wifi:
                if (posicion_act_wifi != 2) {
                    if (manager != null && channel != null) {

                        // Since this is the system wireless settings activity, it's
                        // not going to send us a result. We will be notified by
                        // WiFiDeviceBroadcastReceiver instead.

                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    } else {
                        Log.e(TAG, "channel or manager is null");
                    }
                    Toast.makeText(getApplicationContext(), "Configuracion Wi-Fi", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Si existe algun problema reinicie la aplicacion", Toast.LENGTH_SHORT).show();
                return true;
            /*case R.id.menu_Ayuda:

                Toast.makeText(getApplicationContext(), "Ayuda", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.menu_creditos:
                if(activar_linear_test==1){
                    activar_linear_test=0;
                }else{
                    activar_linear_test=1;
                    //lol XD XD
                    musica.musica_creditos();
                    musica.mediaPlayer.setOnCompletionListener(this);
                }
                Toast.makeText(getApplicationContext(), "Programador: Fernado Muñoz", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Diseñador y Constructor:Andrés Trujillo", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Diseñador y Constructor: Mauri Rodríguez", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.reiniciar_wifi_iniciado:

                Toast.makeText(getApplicationContext(), "Reiniciado Proceso", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.parar_wifi_iniciado:
                parar_wifi_iniciado_fragment();
                Toast.makeText(getApplicationContext(), "Conección Wi-Fi Terminada", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cambiar_musica:
                musica.next_song();
                //Continuar Con la reproduccion?
                if (musica.Continuar_Reproduccion_Continua()) {
                    musica.mediaPlayer.setOnCompletionListener(this);
                }
                return true;
            /**
             case R.id.menu_parar_musica:
             musica.parar_musica_fondo();
             return true;
             */
            case R.id.menu_pausar_musica:
                musica.play_pausar_musica_fondo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void cambio_wifi(FragmentTransaction trans_wifi) {
        switch (posicion_act_wifi) {
            case 0:
                trans_wifi.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                WifiFragment = new WifiFragment();
                WifiFragment.setArguments(argswifi);
                trans_wifi.replace(R.id.principal, WifiFragment).commit();
                break;
            case 1:
                posicion_act_wifi = 0;
                break;
            case 2:
                /**NO CREAR UN NUEVO chatFragment=new xq se borra el chatManager*/
                transicion = getFragmentManager().beginTransaction();
                transicion.setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out);
                transicion.replace(R.id.principal, chatFragment).commit();
                posicion_act_wifi = 2;
                break;
        }
    }



    public void actualizar_dato_enviado(String textovalor) {
        if (bt.isBluetoothEnabled() && existe_conec_bluetooth == true) {
            consola_main_activity_bluetooth(textovalor);
            bt.send(textovalor, false);
        }
    }

    @Override
    public void sendData2(String message) {
        this.enviadosave=message;
        actualizar_dato_enviado(message);
    }

    @Override
    public void buscar_dispositivo_bluetooth() {
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        }
        if(bt.isBluetoothEnabled() && !bt.isServiceAvailable()) {
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_ANDROID);
        }
        if (bt.isBluetoothEnabled() && bt.isServiceAvailable()) {
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
            bluetooth_activity_parear = true;
            Log.i("ACTIVITY_PAREAR ", "bluetooth_activity_parear " + bluetooth_activity_parear);
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        Log.v(TAG, "Prueba Test");
        Log.e("BluetoothSPP", "onActivityResult" + requestCode + " " + resultCode + " " + data);
        Log.e("BluetoothSPP", "EXTRA_DEVICE_ADDRESS: " + data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS));
        Log.e("BluetoothSPP", "DEVICE_NAME: " + data.getExtras().getString(BluetoothState.DEVICE_NAME));
        Log.e("BluetoothSPP", "DEVICE_ANDROID: " + data.getExtras().getString(String.valueOf(BluetoothState.DEVICE_ANDROID)));
        Log.e("BluetoothSPP", "DEVICE_OTHER: " + data.getExtras().getString(String.valueOf(BluetoothState.DEVICE_OTHER)));
        */
        Log.v(TAG, "onActivityResult");
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                bt.connect(data);
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                //habilitar_botones();
            } else {
                Toast.makeText(getApplicationContext()
                        , "La aplicacion necesita bluetooth Bluetooth"
                        , Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
        //No se pone xq onStart sive q es falso aunq deberia ser verdadero
        //bluetooth_activity_parear=false;
        Log.i("ACTIVITY_PAREAR ", "bluetooth_activity_parear " + bluetooth_activity_parear);
    }

    @Override
    public void encender_bluetooth() {
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            }
        }
        Toast.makeText(getApplicationContext(), "Bluetooth Encendido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void desconectar_bluetooth() {
        if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
            bt.disconnect();
        Toast.makeText(getApplicationContext(), "Desconectado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void off_bluetooth() {
        bt.stopService();
        bt.disconnect();            //desconecta del dispositivo conectado
        bt.desconectarbluetooth();  //apaga bluetooth
        Toast.makeText(getApplicationContext(), "Bluetooth Apagado", Toast.LENGTH_SHORT).show();
    }

    private void Actualizar_datos_Controles_frame(){
        Actualizar_datos_Controles();
        if (posicion == 0) {
            transicion = getFragmentManager().beginTransaction();
            Controles= new Controles();
            Controles.setArguments(args);
            transicion.replace(R.id.principal, Controles).commit();
        }
    }
    int activar_linear_test=0;
    public void Actualizar_datos_Controles(){
        args = new Bundle();
        args.putInt("info_pelea", info_pelea);
        args.putFloat("vida", vida);
        args.putFloat("vida_enemigo", vida_enemigo);
        args.putInt("victorias", victorias);
        args.putInt("derrotas", derrotas);
        args.putInt("activar_linear_test", activar_linear_test);
    }

    /**Envia argumento al fragmento bluetooth*/
    public void Actualizar_ventana_bluetooth(){
        args = new Bundle();
        args.putString("enviadosave", enviadosave);
        args.putString("consola_blue", consola_blue);
        args.putString("status_conect", status_conect);
    }

    @Override
    public String sendenviado(String message) {
        this.enviadosave=message;
        consola_main_activity_bluetooth(message);
        Toast.makeText(getApplicationContext(), "Main: "+message, Toast.LENGTH_SHORT).show();
        return "OK";
    }

    private void consola_main_activity_bluetooth(String message) {
        consola_control_bluetooth.setText(message);
    }
    /**------------------------Comienza Wifi ------------------------------*/
    /**WIFI*/
    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

        if (bluetooth_activity_parear == true) {
            //se finalizo el pareamiento bluetooth
            bluetooth_activity_parear = false;
        } else {
            musica.next_random_song();
            musica.mediaPlayer.setOnCompletionListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
        //////////////////////oJOJOJOJOJ

        if(musica.mediaPlayer!=null){
            if(!musica.mediaPlayer.isPlaying()) {
                musica.next_random_song();
                musica.mediaPlayer.setOnCompletionListener(this);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAGWIFI, "onPause");
        if(mCurrentAnimator!=null) {
            mCurrentAnimator.cancel();
        }
        if(!bluetooth_activity_parear) {
            posicion_act_wifi = 0;
            musica.liberar_recursos();
            sonidos.liberar_recursos();
        }
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        if(!bluetooth_activity_parear) {
            // se desconecta del servidor WIFI cuando la aplicacion se para
            //para la busqueda de servidor y elimina el servidor
            if(estan_peleando==false) { //si estan peleando no debo desconectarme
                OnStop_wifi(); //como es asincrono se encarga de elimar local y request verificandolos
                posicion_act_wifi = 0;
                musica.liberar_recursos();
                sonidos.liberar_recursos();
            }
            if(debe_apagar_wifi==true){
                ON_OFF_WIFI(false);
                debe_apagar_wifi=false;
            }
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        bt.stopService();
        bt.disconnect();            //desconecta del dispositivo conectado
        bt.desconectarbluetooth();  //apaga bluetooth
        if(!bluetooth_activity_parear) {
            // se desconecta del servidor WIFI cuando la aplicacion se para
            //para la busqueda de servidor y elimina el servidor
                OnStop_wifi(); //como es asincrono se encarga de elimar local y request verificandolos
                posicion_act_wifi = 0;
                musica.liberar_recursos();
                sonidos.liberar_recursos();
            if(debe_apagar_wifi==true){
                ON_OFF_WIFI(false);
                debe_apagar_wifi=false;
            }
        }

    }

    /**
     * Registers a local service and then initiates a service discovery
     */
    private void startRegistrationAndDiscovery() {
            parar_handler();
            manager = null;
            channel = null;
            manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
            channel = manager.initialize(this, getMainLooper(), null);

            Map<String, String> record = new HashMap<String, String>();
            record.put(TXTRECORD_PROP_AVAILABLE, "visible");
            WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                    SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
            copiaservice = service;   //mi codigo para eliminar el LocalService
            manager.addLocalService(channel, service, new ActionListener() {

                @Override
                public void onSuccess() {
                    appendStatus("Servidor Local Añadido");
                    add_coneccion_wifi = localServer_añadido;
                    discoverService();
                }

                @Override
                public void onFailure(int error) {
                    appendStatus("Falla al Añadir Servidor");
                    Fail_comunicacion_wifi("error", error, "addLocalService");
                    Toast.makeText(getApplicationContext(), "" +
                            "Verifique si el Wi-Fi esta Activado", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void discoverService() {

        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */
        manager.setDnsSdResponseListeners(channel,
                new DnsSdServiceResponseListener() {

                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {

                        // A service has been discovered. Is this our app?
                        posicion_act_wifi = 1;
                        if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {

                            // update the UI and add the item the discovered
                            // device.
                            WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager()
                                    .findFragmentById(R.id.ventana_comunicacion_wifi);
                            if (fragment != null) {
                                WiFiDevicesAdapter adapter = ((WiFiDevicesAdapter) fragment
                                        .getListAdapter());
                                WiFiP2pService service = new WiFiP2pService();
                                service.device = srcDevice;
                                service.instanceName = instanceName;
                                service.serviceRegistrationType = registrationType;
                                adapter.add(service);
                                adapter.notifyDataSetChanged();
                                Log.d(TAGWIFI, "onBonjourServiceAvailable " + instanceName);
                            }
                        }

                    }
                }, new DnsSdTxtRecordListener() {

                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {
                        Log.d(TAGWIFI, device.deviceName + " is " + record.get(TXTRECORD_PROP_AVAILABLE));
                    }
                });

        // After attaching listeners, create a service request and initiate
        // discovery.
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest,
                //peticion de servicios añadido
                new ActionListener() {

                    @Override
                    public void onSuccess() {
                        //addServiceRequest
                        // se encarga de añadir los servicios q encuentra discoverservices
                        //appendStatus("Added service discovery request");
                        appendStatus("Solicitud de descubrimiento de servicios Añadido");
                    }

                    @Override
                    public void onFailure(int arg0) {
                        //appendStatus("Failed adding service discovery request");
                        appendStatus("No se ha podido añadir petición de descubrimiento de servicios");
                        Fail_comunicacion_wifi("arg0", arg0, "addServiceRequest");
                    }
                });
        manager.discoverServices(channel, new ActionListener() {
            //descubrir servicios añadido
            @Override
            public void onSuccess() {
                estado_coneccion_wifi = buscando;
                add_coneccion_wifi = serviceRequest_añadido;
                //removeServiceRequest lo finaliza
                //appendStatus("Service discovery initiated");
                appendStatus("Iniciado Busqueda de Servicio");
            }

            @Override
            public void onFailure(int arg0) {
                //appendStatus("Service discovery failed");
                appendStatus("Fallo en Busqueda de Servicio");
                Fail_comunicacion_wifi("arg0", arg0, "discoverServices");
            }
        });
    }

    @Override
    public void connectP2p(WiFiP2pService service) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = service.device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        if (serviceRequest != null) {
            manager.removeServiceRequest(channel, serviceRequest,
                    new ActionListener() {

                        @Override
                        public void onSuccess() {
                            add_coneccion_wifi = localServer_añadido;
                        }

                        @Override
                        public void onFailure(int arg0) {
                            Fail_comunicacion_wifi("arg0", arg0, "removeServiceRequest");
                        }
                    });
        }
        estado_coneccion_wifi = conectando;
        manager.connect(channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                appendStatus("Conectando al Servicio");
            }

            @Override
            public void onFailure(int errorCode) {
                appendStatus("Fallo al Conectarse al Servicio");
                Fail_comunicacion_wifi("errorCode", errorCode, "connect");
                debe_apagar_wifi = true;
                parar_wifi_iniciado_fragment();
            }
        });
    }

    private void Fail_comunicacion_wifi(String tipo,int entero,String mensaje){
        String string="ERROR: "+mensaje+" Tipo: " + tipo + " " + entero;
        Log.d(TAGWIFI, string);
        Toast.makeText(getApplicationContext(), string
                , Toast.LENGTH_SHORT).show();
    }

    private void parar_wifi_iniciado_fragment() {
         /** ATENCION no llamar desde otra
         * clase solo sirve para el proceso
         * de vinculacion wifi de inicio*/
        //PELIGRO
        parar_handler();
        Log.i(TAGWIFI, "estado_coneccion_wifi: "+estado_coneccion_wifi);
        switch (estado_coneccion_wifi) {
            case 0:
                debe_apagar_wifi=true;
                Toast.makeText(getApplicationContext(), "ERROR INESPERADO estado_coneccion_wifi=0"
                        , Toast.LENGTH_SHORT).show();
                Log.e(TAGWIFI, "menu no deberia estar habilitado estado_coneccion_wifi=0");
                break;
            case buscando:
                if (serviceRequest != null) {
                    manager.removeServiceRequest(channel, serviceRequest,
                            new ActionListener() {

                                @Override
                                public void onSuccess() {
                                    estado_coneccion_wifi=0;
                                    add_coneccion_wifi = localServer_añadido;
                                }

                                @Override
                                public void onFailure(int arg0) {
                                    Fail_comunicacion_wifi("arg0", arg0, "removeServiceRequest");
                                    Log.e(TAGWIFI, "ERROR al cancelada busqueda");
                                }
                            });
                }
                break;

            case conectando:
                manager.cancelConnect(channel, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAGWIFI, "coneccion cancelada");
                        estado_coneccion_wifi=0;
                    }

                    @Override
                    public void onFailure(int reason) {
                        debe_apagar_wifi=true;
                        Log.e(TAGWIFI, "ERROR al cancelar coneccion");
                        Toast.makeText(getApplicationContext(),
                                "ERROR al cancelada conección"
                                , Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case conectado:
                debe_apagar_wifi=true;
                Toast.makeText(getApplicationContext(), "ERROR INESPERADO --Conectado--"
                        , Toast.LENGTH_SHORT).show();
                Log.e(TAGWIFI, "menu no deberia estar habilitado estado_coneccion_wifi=conectado");
                break;
        }
        Log.d(TAGWIFI, "add_coneccion_wifi: "+add_coneccion_wifi);
        switch (add_coneccion_wifi){
            case 0:
                Toast.makeText(getApplicationContext(), "Encendiendo Wi-Fi"
                        , Toast.LENGTH_SHORT).show();
                ON_OFF_WIFI(true);
                Log.wtf(TAGWIFI, "Wifi posiblemente no encendido");
                break;
            case localServer_añadido:
                eliminar_localServer();
                break;
            case serviceRequest_añadido:
                eliminar_ServiceRequest();
                eliminar_localServer();
                break;
        }
        Log.i(TAGWIFI, "Finalizado Proceso Wi-Fi");
        Toast.makeText(getApplicationContext(), "Finalizado Proceso Wi-Fi"
                , Toast.LENGTH_SHORT).show();
        //disconnect_wifi();
        FragmentTransaction trans_wifi = getFragmentManager().beginTransaction();
        trans_wifi.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        WifiFragment = new WifiFragment();
        WifiFragment.setArguments(argswifi);
        trans_wifi.replace(R.id.principal, WifiFragment).commit();
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_coneccion, menu);
        posicion_act_wifi=0;

    }
    public void removerGroup(){
        if (manager != null && channel != null) {
            manager.removeGroup(channel, new ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d(TAGWIFI, "Me desconecte del grupo");
                    estado_coneccion_wifi = 0;
                    add_coneccion_wifi = localServer_añadido;
                }

                @Override
                public void onFailure(int reasonCode) {
                    estado_coneccion_wifi = 0;
                    add_coneccion_wifi = localServer_añadido;
                    Log.e(TAGWIFI, "Desconeccion Fallida. Devido a: " + reasonCode);
                    Log.e(TAGWIFI, "Posiblemente el otro usuario se desconecto. Devido a: " + reasonCode);
                    /*Toast.makeText(getApplicationContext(),
                            "ERROR GRAVE: removeGroup"
                            , Toast.LENGTH_SHORT).show();
                     */
                    //debe_apagar_wifi = true;
                }
            });
        }else{
            Log.e(TAGWIFI, "Error se esta elimando channel y service en alguna parte removerGroup");
            Toast.makeText(getApplicationContext(), "ERROR null: removerGroup"
                    , Toast.LENGTH_SHORT).show();
        }

    }
    private void eliminar_localServer(){
        if(channel!=null && copiaservice!=null) {
            manager.removeLocalService(channel, copiaservice, new ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAGWIFI, "localServer eliminado :)");
                    add_coneccion_wifi = 0;
                    estado_coneccion_wifi = 0;
                }

                @Override
                public void onFailure(int reason) {
                    Log.e(TAGWIFI, "Error al elimnar localServer");
                    Toast.makeText(getApplicationContext(),
                            "ERROR GRAVE: removeLocalService"
                            , Toast.LENGTH_SHORT).show();
                    debe_apagar_wifi = true;
                }
            });
        }else{
            Log.e(TAGWIFI, "Error se esta elimando channel y service en alguna parte removeLocalService");
            Toast.makeText(getApplicationContext(), "ERROR null: removeLocalService"
                    , Toast.LENGTH_SHORT).show();
        }
    }
    private void eliminar_ServiceRequest(){
        if (serviceRequest != null) {
            manager.removeServiceRequest(channel, serviceRequest,
                    new ActionListener() {

                        @Override
                        public void onSuccess() {
                            Log.d(TAGWIFI, "serviceRequest elimandos");
                            add_coneccion_wifi = localServer_añadido;
                        }

                        @Override
                        public void onFailure(int arg0) {
                            Log.e(TAGWIFI, "Error al eliminar serviceRequest");
                            Toast.makeText(getApplicationContext(),
                                    "ERROR GRAVE: removeServiceRequest"
                                    , Toast.LENGTH_SHORT).show();
                            debe_apagar_wifi = true;
                        }
                    });
        }else{
            Log.e(TAGWIFI, "Error se esta elimando Discovery service en alguna parte removeServiceRequest");
            Toast.makeText(getApplicationContext(), "ERROR null: removeServiceRequest"
                    , Toast.LENGTH_SHORT).show();
        }
    }
    private void OnStop_wifi(){
        //Peligroso
        parar_handler();
        //parar_chatManager();
        if (manager != null && channel != null) {
            if(estado_coneccion_wifi==conectado) {
                removerGroup();
            }
            if (add_coneccion_wifi == serviceRequest_añadido) {
                eliminar_ServiceRequest();
            }
            if (add_coneccion_wifi == localServer_añadido) {
                eliminar_localServer();
            }
        }
    }


    @Override
    public void finalizar_coneccion_wifi() {
        group=false;clear=false; ser=false;
        if(posicion_act_wifi==2);
        {
            //Peligroso
            parar_handler();
            //parar_chatManager();
            if (channel != null && copiaservice != null) {
                removerGroup();
                manager.clearLocalServices(channel, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAGWIFI, "OK clearLocalServices");
                        clear=true;
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.i(TAGWIFI, "fail clearLocalServices");
                        clear=false;
                    }
                });
                manager.removeLocalService(channel, copiaservice, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAGWIFI, "OK localServer elimandos");
                        add_coneccion_wifi = 0;
                        ser=true;
                        FragmentTransaction trans_wifi = getFragmentManager().beginTransaction();
                        trans_wifi.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                        WifiFragment = new WifiFragment();
                        WifiFragment.setArguments(argswifi);
                        trans_wifi.replace(R.id.principal, WifiFragment).commit();
                        menu.clear();
                        getMenuInflater().inflate(R.menu.menu_coneccion, menu);
                        posicion_act_wifi = 0;
                        estado_coneccion_wifi=0;
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e(TAGWIFI, "Error al elimnar localServer");
                    }
                });

            } else {
                Log.e(TAGWIFI, "Error se esta elimando channel y service en alguna parte");
                Toast.makeText(getApplicationContext(), "ERROR: AL ELIMINAR LOCALSERVER"
                        , Toast.LENGTH_SHORT).show();
            }

            /**
            if(group==true && clear==true && ser==true) {

            }else{
                Log.d(TAGWIFI, "ERROR EN: "
                +"desconectar grupo: "+group+" Lipiar server: "+clear+" LocarServer: "+ser
                );

            }**/
        }
    }
/*
    private void parar_chatManager(){
        //tambien modifique chatManager y le puse stop
        Log.v(TAGWIFI, "llego");
        if (posicion_act_wifi == 2) {
            if(chatManager!=null) {
                chatManager.setStop(false);
                log_Toast_wifi_normal(TAGWIFI,"e", "chatmanager es Stop :)");
            }else{
                log_Toast_wifi_normal(TAGWIFI,"e", "chatmanager es NULL");
            }
        }else{
            Log.e(TAGWIFI, "llamado desde posicion_act_wifi: " + posicion_act_wifi);
        }
    }
    */
    private void parar_handler(){
        switch (tipo_usuario) {
            case 0:
                Log.d(TAGWIFI,"parar_handler: "+tipo_usuario);
            return;
            case maestro:
                //log_Toast_wifi_normal(TAGWIFI,"i","Soy Maestro e intento eliminar handler");
                if (handlerx != null) {
                    handlerx.interrupt();
                    Log.i(TAGWIFI, "maestro handlerx interrumpido");
                } else {
                    Log.i(TAGWIFI, "maestro handlerx no creado");
                }
                tipo_usuario=0;
                return;
            case usuario:
                //log_Toast_wifi_normal(TAGWIFI, "i", "Soy Usuario e intento eliminar handler");
                if (handlerx != null) {
                    handlerx.interrupt();
                    Log.i(TAGWIFI, "handlerx interrumpido");
                } else {
                    Log.i(TAGWIFI, "handlerx no creado");
                }
                tipo_usuario=0;
                return;
            default:
                log_Toast_wifi_normal(TAGWIFI, "e", "" +
                        "A existido un error inesperado Reinicie la aplicación");
                debe_apagar_wifi=true;
                return;
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.i(TAG, readMessage);
                (chatFragment).pushMessage("Amigo: " + readMessage);
                recibido_wifi_otro_dato(readMessage);
                break;

            case MY_HANDLE:
                if(posicion_act_wifi==2)/// PELIGRO
                {
                    Object obj = msg.obj;
                    objg = msg.obj;
                    (chatFragment).setChatManager((ChatManager) obj);
                }
        }
        return true;
    }

    public void log_Toast_wifi_normal(String Tag,String tipo,String mensaje){
        if(tipo=="v")
            Log.v(Tag, mensaje);
        if(tipo=="i")
            Log.i(Tag, mensaje);
        if(tipo=="d")
            Log.d(Tag, mensaje);
        if(tipo=="w")
            Log.w(Tag, mensaje);
        if (tipo =="wtf")
            Log.wtf(Tag, mensaje);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    int tipo_usuario=0;
    private final static int maestro=1;
    private final static int usuario=2;

    Thread handlerx;
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {

        handlerx = null;
        //log_Toast_wifi_normal(TAGWIFI, "i", "Intentando Conección como Cliente o Maestro");
        if (p2pInfo.isGroupOwner) {
            Log.i(TAGWIFI, "Connected as group owner");
            //log_Toast_wifi_normal(TAGWIFI, "i", "Conectando como Maestro");
            try {
                handlerx = new GroupOwnerSocketHandler(
                        ((MessageTarget) this).getHandler());
                handlerx.start();
                tipo_usuario=maestro;
                debe_apagar_wifi=true;
                Toast.makeText(getApplicationContext(), "Exito Conección (Maestro)"
                        , Toast.LENGTH_SHORT).show();
                log_Toast_wifi_normal(TAGWIFI, "i", "Exito Conección (Maestro)");
            } catch (IOException e) {
                tipo_usuario=maestro;
                debe_apagar_wifi=true;
                parar_wifi_iniciado_fragment();
                log_Toast_wifi_normal(TAGWIFI, "e",
                        "Failed to create a server thread - " + e.getMessage());
                //??? wtf xq return
                //return;
            }
        } else {
            //Log.d(TAGWIFI, "Connected as peer");
            tipo_usuario=usuario;
            log_Toast_wifi_normal(TAGWIFI, "i", "Exito en la Conección (Usuario)");
            handlerx = new ClientSocketHandler(
                    ((MessageTarget) this).getHandler(),
                    p2pInfo.groupOwnerAddress);
            handlerx.start();
        }

        estado_coneccion_wifi=conectado;
        add_coneccion_wifi=localServer_añadido;
        chatFragment = new WiFiChatFragment();
        transicion=getFragmentManager().beginTransaction();
        transicion.setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out);
        transicion.replace(R.id.principal, chatFragment).commit();
        posicion_act_wifi=2;
        this.menu.clear();
        getMenuInflater().inflate(R.menu.menu_coneccion, menu);
        //statusTxtView.setVisibility(View.GONE);
    }

    public void appendStatus(String status) {
        Log.d(TAGWIFI, "appendStatus: " + status);
        if(posicion_act_wifi<=1) {
            TextView status_text_wifi = (TextView) findViewById(R.id.status_text_wifi);
            String current = status_text_wifi.getText().toString();
            status_text_wifi.setText(current + "\n" + status);
            ScrollView desplegar=(ScrollView) findViewById(R.id.desplegar);
            desplegar.fullScroll(View.FOCUS_DOWN);
        }else{
            Log.e(TAGWIFI,"ERROR ESTAN LLAMANDO A TEXT CUANDO NO EXISTE");
        }
    }

    /**WIFI*/

    public void Actualizar_ventana_wifi(){
        argswifi = new Bundle();
        argswifi.putString("midato", wifi_mi_dato);
        argswifi.putString("otrodato", dato_recibido_wifi);
        argswifi.putString("consola", wifi_consola);
        argswifi.putString("status_conect", wifi_status);
    }

    @Override
    public void start_wifi() {
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(debe_apagar_wifi==true){
            Log.d(TAGWIFI,"Se debe pagar el wifi :(");
            ON_OFF_WIFI(false);
            debe_apagar_wifi=false;
            reiniciar_fragment_wifi();
            if(tipo_usuario==maestro){
                Toast.makeText(getApplicationContext(),
                        "Se ha salido de la aplicacion para evitar errores"
                        , Toast.LENGTH_SHORT).show();
                finish();
                tipo_usuario=0;
            }else {
                Toast.makeText(getApplicationContext(),
                        "Se a Reiniciado el Wi-Fi debido a: Multiples Errores"
                        , Toast.LENGTH_SHORT).show();
            }
        }else {

            if (wifiManager.isWifiEnabled()) {
                posicion_act_wifi = 0;
                servicesList = new WiFiDirectServicesList();
                getFragmentManager().beginTransaction()
                        .replace(R.id.ventana_comunicacion_wifi, servicesList).commit();
                this.menu.clear();
                getMenuInflater().inflate(R.menu.menu_parar_wifi_iniciado, menu);
                startRegistrationAndDiscovery();
            } else {
                ON_OFF_WIFI(true);
                reiniciar_fragment_wifi();
                Toast.makeText(getApplicationContext(), "Verifique que el Wi-Fi este activado"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void reiniciar_fragment_wifi(){
        FragmentTransaction trans_wifi = getFragmentManager().beginTransaction();
        WifiFragment = new WifiFragment();
        WifiFragment.setArguments(argswifi);
        trans_wifi.replace(R.id.principal, WifiFragment).commit();
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_coneccion, menu);
    }

    public void recibido_wifi_otro_dato(String dato){
        dato_recibido_wifi=dato;
        recibi_NUEVO_DATO_wifi(dato);
        wifi_consola+="Amigo: "+dato;
        consola_control_wifi.setText("Amigo: " + dato);
    }

    public void ON_OFF_WIFI(boolean status) {
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }


    @Override
    /**Metodo de musica Determina si se a terminado de reproducir alguna cancion*/
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Iniciando Nueva Musica de Fondo");
        musica.Reproduccion_Finalizada();
        //Continuar Con la reproduccion?
        if(musica.Continuar_Reproduccion_Continua()){
            musica.mediaPlayer.setOnCompletionListener(this);
        }
    }

    @Override
    public String sendData(String message) {
        /**
         if(posicion==0)
         {
         TextView txt_boton_pulsado = (TextView) findViewById(R.id.txt_boton_pulsado);
         txt_boton_pulsado.setText(message);
         }
         */
        this.enviadosave = message;
        actualizar_dato_enviado(message);
        return message;
    }

    public void recibi_NUEVO_DATO_bluetooth(String dato){
        if (dato.equals(varGolpe) && estan_peleando==true) {
            robot_recibi_golpe(dato);
            return;
        }
        if (dato.equals(varCaida) && estan_peleando==true) {
            robot_fuera_de_combate(dato);
            return;
        }
    }
    Boolean emocion_musica=false;
    public void robot_recibi_golpe(String dato){
        vida-=0.05f;
        if(vida>0) {
            Log.d(TAG_battle, "robot_recibi_golpe: vida: "+vida+" dato bluetooth: "+dato);
            vibrar(200);
            if(vida<=0.5f){
                if(emocion_musica==false){
                    musica.next_lose_song();
                    emocion_musica=true;
                }
            }
            start_battle_enviar_dato_wifi(String.valueOf(Math.round(vida*100)));
            actualizar_barras();
        }else{
            vibrar(500);
            vida=0.0f; //no necesario
            rendicion_battle();
        }
    }

    public boolean isInt(String dato)
    {
        try
        {
            Integer.parseInt(dato);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public void robot_fuera_de_combate(String dato){
        rendicion_battle();
        Toast.makeText(getApplicationContext(), "Robot Fuera de Combate"
                , Toast.LENGTH_SHORT).show();
        Log.d(TAG_battle, "robot_fuera_de_combate: "+dato);
    }

    public Boolean start_battle_enviar_dato_wifi(String dato) {
        if (posicion_act_wifi == 2) {
            chatManager = (chatFragment).getChatManager();
            if (chatManager != null) {
                Log.d(TAG, "Enviado wifi: "+ dato);
                chatManager.write(dato.getBytes());
                (chatFragment).pushMessage("Me: " + dato);
                return true;
            } else {
                Log.e(TAG_battle, "start_battle_enviar_dato_wifi chat: null");
                return false;
            }
        } else {
            Log.e(TAG_battle, "start_battle_enviar_dato_wifi Todavia no estoy conectado por wifi");
            return false;
        }
    }

    public void restablecer_musica(){
        if(emocion_musica==true){
            emocion_musica=false;
            musica.next_song();
            //Continuar Con la reproduccion?
            if (musica.Continuar_Reproduccion_Continua()) {
                musica.mediaPlayer.setOnCompletionListener(this);
            }
        }
    }

    public void vibrar(int tiempo){
        Vibrator v = (Vibrator)  getSystemService(VIBRATOR_SERVICE);
        v.vibrate(tiempo);
    }

    public Boolean BLUETOOTH_CONECTED = false;
    public Boolean WIFI_CONECTED = false;

    public int recibido_resultado=0;    //prioridad1
    public int enviado_resultado=0;     //mi resultado
    private static final int WIN = 21;
    private static final int LOST = 22;
    private static final int EMPATE = 23;

    public Boolean estan_peleando=false;
    public int info_pelea = 0;       //prioridad2
    public final static int DESCONECT = 1;
    public final static int CONECTADO = 2;
    public final static int ENVIADOFIGHT = 3;
    //public final static int READY = 4;
    public final static int FIGHT = 5;
    public final static int FIN = 6;
    public final static int CANCELADO = 7;
    public final static int ERROR = 8;
    public final static int PEDIDOREINICIAR = 9;
    public final static int ENVIADOREINCIAR = 10;

    public Boolean Cortar_Comunicacion_Robot=false; //paro el envio de datos al robot
    //datos
    public float vida=1.0f;
    public float vida_enemigo=1.0f;
    public int victorias=0;
    public int derrotas=0;

    public int numero_pulsaciones=0;
    public Boolean recibido_fight=false;     //si se toca un boton se descarta
    public Boolean recibido_reiniciar=false; //si se toca un boton se descarta

    public final static String varGolpe="q";
    public final static String varCaida="w";

    ControlVariables constante=new ControlVariables();
    public Boolean en_modo_prueba=false;
    String TAG_battle="start_battle";

    @Override
    public int start_battle() {
        if (bt.isBluetoothEnabled() && existe_conec_bluetooth == true) {
            BLUETOOTH_CONECTED = true;
            Log.d(TAG_battle, "OK BLUETOOTH");
        } else {
            BLUETOOTH_CONECTED = true;
            Log.e(TAG_battle, "No hay Comunicación Bluetooth");
        }
        if (posicion_act_wifi == 2) {
            WIFI_CONECTED = true;
            Log.d(TAG_battle, "OK Wi-Fi");
        } else {
            WIFI_CONECTED = false;
            Log.e(TAG_battle, "No hay Comunicación Wi-Fi");
        }
        if (BLUETOOTH_CONECTED == true && WIFI_CONECTED == true) {
            en_modo_prueba=false;
            info_pelea=CONECTADO;
            return start_battle_OK();
        } else {
            if(BLUETOOTH_CONECTED==false && WIFI_CONECTED==false)
                Toast.makeText(getApplicationContext(), "Conéctese a un Robot y un Rival", Toast.LENGTH_SHORT).show();
            if(BLUETOOTH_CONECTED==false)
                Toast.makeText(getApplicationContext(), "Conéctese con su Robot", Toast.LENGTH_SHORT).show();
            if(WIFI_CONECTED==false)
                Toast.makeText(getApplicationContext(), "Conéctese con su Rival", Toast.LENGTH_SHORT).show();
            Log.e(TAG_battle, "Wi-Fi o BLUETOOTH no esta conectado");
            reproducir_sonido(R.raw.tambor_misterio,1.0f);
            return DESCONECT;
        }
    }

    @Override
    public void start_battle_consola_comandos(String dato){
        en_modo_prueba=true;
        if(dato.equals("z")){
            start_battle_OK();
        }else {
            recibi_NUEVO_DATO_wifi(dato);
        }
    }

    public void recibi_NUEVO_DATO_wifi(String dato){
        Log.d(TAG_battle, "dato: -"+dato+"-");
        if(isInt(dato)){
            int v=Integer.parseInt(dato);
            if(v!=0) {
                vida_enemigo = (float) v / 100;
                Log.d(TAG_battle, "vida_enemigo: -"+String.valueOf(vida_enemigo));
            }else{
                vida_enemigo=0.0f;
            }
            actualizar_barras();
        }else {
            if (dato.equals("YOU WIN") || dato.equals("win")) {
                recibido_resultado = WIN;
                start_battle_WIN();
            }
            if (dato.equals("Start Battle") || dato.equals("sb")) {
                recibido_fight = true;
                start_battle_empezar();
            } else {
                recibido_fight = false;
            }
            if (dato.equals("REINICIAR") || dato.equals("res")) {
                recibido_reiniciar = true;
                start_battle_reiniciar();
            } else {
                recibido_reiniciar = false;
            }
            /**Testeo
             * */
            if(dato.equals(varGolpe) || dato.equals(varCaida)){
                recibi_NUEVO_DATO_bluetooth(dato);
                Log.d(TAG_battle, "Bluetooth mediante WIFI: "+dato);
            }

        }
    }


    @Override
    public int rendicion_battle(){ // perder
            if (estan_peleando == true) {
                recibido_fight = false;
                info_pelea = FIN;
                estan_peleando = false;
                numero_pulsaciones = 0;
                recibido_reiniciar = false;
                enviado_resultado = 0;
                //recibido_resultado=0;
                derrotas++;
                vida = 0.0f;
                manejar_frament_win_lose(LOST);
                manejar_frament_control(getString(R.string.start_text));
                if (start_battle_enviar_dato_wifi("YOU WIN") == true) {
                    reproducir_sonido(R.raw.you_lose, 1.0f);
                }
            }else{
                Log.e(TAG_battle, "No estan peleando: WIFI_CONECTED= " + String.valueOf(WIFI_CONECTED));
            }
        Log.d(TAG_battle, "rendicion_battle o perder");
        log_start_battle();
        return info_pelea;
    }

    public int start_battle_OK() {
        numero_pulsaciones++;
        if (estan_peleando == true) {
            return start_battle_fighting();
        } else {
            return start_battle_esperando();
        }
    }

    public int start_battle_fighting(){
        if(numero_pulsaciones==1){
            info_pelea=PEDIDOREINICIAR;
            reproducir_sonido(R.raw.button6 ,1.0f);
            Log.d(TAG_battle, "PEDIDO REINICIAR: " + PEDIDOREINICIAR);
            return PEDIDOREINICIAR;
        }
        if(numero_pulsaciones==2){
            chatManager = (chatFragment).getChatManager();
            if (chatManager != null) {
                Log.d(TAG_battle, "ENVIADO REINCIAR: " + "REINICIAR");
                chatManager.write("REINICIAR".getBytes());
                (chatFragment).pushMessage("Me: " + "REINICIAR");
                info_pelea = ENVIADOREINCIAR;
                numero_pulsaciones=0;
                return start_battle_reiniciar();
            } else {
                log_start_battle();
                Log.e(TAG_battle, "chat start_battle_fighting: null" + numero_pulsaciones);
                numero_pulsaciones=0;
                info_pelea = ERROR;
                return ERROR;
            }
        }
        log_start_battle();
        Log.e(TAG_battle, "Error start_battle_fighting: "+numero_pulsaciones);
        numero_pulsaciones=0;
        info_pelea = ERROR;
        return ERROR;
    }

    public int start_battle_esperando(){
        if(numero_pulsaciones==1) {
            chatManager = (chatFragment).getChatManager();
            if (chatManager != null) {
                Log.d(TAG_battle, "ENVIADO FIGHT: " + info_pelea);
                chatManager.write("Start Battle".getBytes());
                (chatFragment).pushMessage("Me: " + "Start Battle");
                info_pelea = ENVIADOFIGHT;
                return start_battle_empezar();
            } else {
                log_start_battle();
                Log.e(TAG_battle, "start_battle_esperando 1 chat: null"+numero_pulsaciones);
                info_pelea = ERROR;
                return ERROR;
            }
        }
        if(numero_pulsaciones==2){
            chatManager = (chatFragment).getChatManager();
            if (chatManager != null) {
                Log.d(TAG_battle, "CANCELADO: " + "CANCELADO");
                chatManager.write("CANCELADO".getBytes());
                (chatFragment).pushMessage("Me: " + "CANCELADO");
                info_pelea = CANCELADO;
                numero_pulsaciones=0;
                reproducir_sonido(R.raw.boom, 1.0f);
                return CANCELADO;
            } else {
                log_start_battle();
                Log.e(TAG_battle, "start_battle_esperando 2 chat: null");
                info_pelea = ERROR;
                return ERROR;
            }
        }
        log_start_battle();
        Log.e(TAG_battle, "Error start_battle_esperando: "+numero_pulsaciones);
        numero_pulsaciones=0;
        info_pelea = ERROR;
        return ERROR;
    }

    public int start_battle_empezar() {
        // si las variables son correctas se iniciara la pelea
        if (recibido_fight == true && info_pelea == ENVIADOFIGHT) {
            recibido_fight = false;
            info_pelea = FIGHT;
            estan_peleando = true;
            numero_pulsaciones = 0;
            recibido_reiniciar = false;
            enviado_resultado=0;
            recibido_resultado=0;
            manejar_frament_win_lose(0);
            vida = 1.0f;
            vida_enemigo = 1.0f;
            manejar_frament_control(getString(R.string.reiniciar_text));
            reproducir_sonido(R.raw.fight, 1.0f);
            log_start_battle();
            return info_pelea;
        }
        //entonces info_pelea = ENVIADOFIGHT;
        reproducir_sonido(R.raw.tambor_misterio, 1.0f);
        return info_pelea;
    }

    public int start_battle_reiniciar(){
        // si las variables son correctas se reiniciara todo el juego
        if(recibido_reiniciar==true && info_pelea==ENVIADOREINCIAR) {
            info_pelea = CONECTADO;
            recibido_fight = false;
            estan_peleando = false;
            numero_pulsaciones = 0;
            recibido_reiniciar = false;
            recibido_resultado =0;
            victorias=0;
            derrotas=0;
            vida = 1.0f;
            vida_enemigo = 1.0f;
            manejar_frament_win_lose(0);
            manejar_frament_control(getString(R.string.start_text));
            reproducir_sonido(R.raw.boom, 1.0f);
            Toast.makeText(this,"Se a Reiniciado a los valores originales"
                    , Toast.LENGTH_SHORT).show();
            log_start_battle();
            return info_pelea;
        }
        //info_pelea = ENVIADOREINCIAR;
            reproducir_sonido(R.raw.button5,1.0f);
            return info_pelea;
    }

    public void start_battle_WIN(){
        if(enviado_resultado==0 || enviado_resultado==LOST){
            manejar_frament_win_lose(recibido_resultado);
            recibido_fight = false;
            info_pelea = FIN;
            estan_peleando = false;
            numero_pulsaciones = 0;
            recibido_reiniciar = false;
            enviado_resultado=0;
            victorias++;
            vida_enemigo = 0.0f;
            reproducir_sonido(R.raw.you_win,1.0f);
            manejar_frament_control(getString(R.string.start_text));
            return;
        }
        if(enviado_resultado==EMPATE || enviado_resultado==WIN) {
            manejar_frament_win_lose(recibido_resultado);
            recibido_fight = false;
            info_pelea = FIN;
            manejar_frament_info_pelea();
            estan_peleando = false;
            numero_pulsaciones = 0;
            recibido_reiniciar = false;
            enviado_resultado=0;
            manejar_frament_control(getString(R.string.start_text));
            return;
        }
        Log.e(TAG_battle, "ERROR al ganar start_battle_WIN");
        log_start_battle();
        return;
    }

    public void actualizar_barras() {
        if(posicion==0) {
            Drawable background;
            LinearLayout linear_barra_vida = (LinearLayout) findViewById(R.id.linear_barra_vida);
            LinearLayout linear_barra_vida_enemigo=(LinearLayout) findViewById(R.id.linear_barra_vida_enemigo);
            background=linear_barra_vida.getBackground();
            background.setAlpha(Math.round(vida_enemigo * 170)+80);
            linear_barra_vida.getBackground().setAlpha(Math.round(vida * 170)+80);
            linear_barra_vida_enemigo.getBackground().setAlpha(Math.round(vida_enemigo * 170)+80);
            linear_barra_vida.setScaleX(vida);
            linear_barra_vida_enemigo.setScaleX(vida_enemigo);
        }else{
            log_start_battle();
            Log.e(TAG_battle, "ERROR actualizar_barras");
        }
    }

    public void manejar_frament_win_lose(int result) {
        /**
         * Restableco la musica desde AQUI
         * */
        restablecer_musica();
        if (posicion == 0) {
            Log.d(TAG_battle, "manejar_frament_win_lose: result " + String.valueOf(result));
            FrameLayout linear_you_win = (FrameLayout) findViewById(R.id.linear_you_win);
            FrameLayout linear_you_lose = (FrameLayout) findViewById(R.id.linear_you_lose);
            switch (result) {
                case 0:
                    linear_you_win.setVisibility(View.GONE);
                    linear_you_lose.setVisibility(View.GONE);
                    return;
                case WIN:
                    linear_you_win.setVisibility(View.VISIBLE);
                    linear_you_lose.setVisibility(View.GONE);
                    return;
                case LOST:
                    linear_you_win.setVisibility(View.GONE);
                    linear_you_lose.setVisibility(View.VISIBLE);
                    return;
                case EMPATE:
                    linear_you_win.setVisibility(View.GONE);
                    linear_you_lose.setVisibility(View.GONE);
                    return;
                default:
                    log_start_battle();
                    Log.e(TAG_battle, "ERROR manejar_frament_win_lose: caso no definido = " + result);
                    return;
            }
        }else{
            log_start_battle();
            Log.e(TAG_battle, "ERROR manejar_frament_win_lose: " +
                    "ESTAN LLAMANDO CONTROLES CUANDO NO ESTA");
            }
        }

    public void manejar_frament_control(String id){
        if(posicion==0) {
            Log.d(TAG_battle, "manejar_frament_control: String id = " + id);
            LinearLayout linear_barra_vida = (LinearLayout) findViewById(R.id.linear_barra_vida);
            LinearLayout linear_barra_vida_enemigo=(LinearLayout) findViewById(R.id.linear_barra_vida_enemigo);
            linear_barra_vida.setScaleX(vida);
            linear_barra_vida_enemigo.setScaleX(vida_enemigo);
            TextView txt_victorias=(TextView)findViewById(R.id.txt_victorias);
            txt_victorias.setText(String.valueOf(victorias));
            TextView txt_derrotas=(TextView)findViewById(R.id.txt_derrotas);
            txt_derrotas.setText(String.valueOf(derrotas));
            TextView text_estado_battle=(TextView)findViewById(R.id.text_estado_battle);
            text_estado_battle.setText(id);
            TextView text_info_estado=(TextView)findViewById(R.id.text_info_estado);
            text_info_estado.setText(constante.texto_info_pelea(info_pelea));
            //text_info_estado.setText(String.valueOf(info_pelea));
            /*
            LinearLayout linear_you_win = (LinearLayout) findViewById(R.id.linear_you_win);
            LinearLayout linear_you_lose = (LinearLayout) findViewById(R.id.linear_you_lose);
            linear_you_win.setVisibility(View.GONE);
            linear_you_lose.setVisibility(View.GONE);

        }else{
            log_start_battle();
            Log.e("manejo fragment controles","ERROR ESTAN LLAMANDO CONTROLES CUANDO NO ESTA");
        */
        }

    }

    public void manejar_frament_info_pelea(){
        if(posicion==0) {
            Log.d(TAG_battle, "manejar_frament_info_pelea: "+
                    constante.texto_info_pelea(info_pelea)+" "+String.valueOf(info_pelea));
            TextView text_info_estado=(TextView)findViewById(R.id.text_info_estado);
            text_info_estado.setText(constante.texto_info_pelea(info_pelea));
            log_start_battle();
        }
    }
    @Override
    public boolean confirmacion() {
        return false;
    }

    @Override
    public int estado() {
        return 0;
    }

    public void log_start_battle(){
        Log.i(TAG_battle,
                        " info_pelea: "+String.valueOf(info_pelea)+
                        " estan_peleando: "+String.valueOf(estan_peleando));
        Log.d(TAG_battle,
                        " numero_pulsaciones: " + String.valueOf(numero_pulsaciones) +
                        " recibido_fight: " + String.valueOf(recibido_fight) +
                        " recibido_reiniciar: " + String.valueOf(recibido_reiniciar));
    }

    public void reproducir_sonido(int id ,float vol){
        sonidos.reproducir_sig_sonido(id, vol);
        onCompletion_sonidos();
    }

    public void onCompletion_sonidos(){
        sonidos.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                sonidos.liberar_recursos();
            }
        });
    }

}
