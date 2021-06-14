package com.example.espe.controlbluetoothv1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener;


public class MainActivity1 extends AppCompatActivity implements OnClickListener {

    BluetoothSPP bt;
    TextView textStatus, textRead, enviado;
    ImageView up,down,der,izq,ataque_derecha,ataque_izquierda;
    Button inicio,parar,extra1;

    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//Horizontal
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Vertical
        Log.i("Check", "onCreate");

        textRead = (TextView)findViewById(R.id.textRead);
        textStatus = (TextView)findViewById(R.id.textStatus);
        enviado = (TextView)findViewById(R.id.enviado);
        habilitar_botones();
        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                textRead.append(message + "\n");
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                textStatus.setText("Status : No Conectado");
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_coneccion, menu);
            }

            public void onDeviceConnectionFailed() {
                textStatus.setText("Status : Conección Fallida");
            }

            public void onDeviceConnected(String name, String address) {
                textStatus.setText("Conectado: " + name);
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_disconnection, menu);
            }
        });
    }

    public void habilitar_botones(){
        up =(ImageView) findViewById(R.id.up);
        up.setOnClickListener(this);
        down =(ImageView) findViewById(R.id.down);
        down.setOnClickListener(this);
        der =(ImageView) findViewById(R.id.der);
        der.setOnClickListener(this);
        izq =(ImageView) findViewById(R.id.izq);
        izq.setOnClickListener(this);
        ataque_derecha =(ImageView) findViewById(R.id.ataque_derecho);
        ataque_derecha.setOnClickListener(this);
        ataque_izquierda =(ImageView) findViewById(R.id.ataque_izquierdo);
        ataque_izquierda.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int identificacion = v.getId();
        switch (identificacion) {
            case R.id.up:
                actualizar_dato_enviado("a");
                break;
            case R.id.down:
                actualizar_dato_enviado("t");
                break;
            case R.id.der:
                actualizar_dato_enviado("d");
                break;
            case R.id.izq:
                actualizar_dato_enviado("i");
                break;
            case R.id.ataque_derecho:
                actualizar_dato_enviado("1");
                break;
            case R.id.ataque_izquierdo:
                actualizar_dato_enviado("2");
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_coneccion, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        int id = item.getItemId();
        if(id == R.id.menu_device_connect) {
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        } else if(id == R.id.menu_aprender) {
            if (!bt.isBluetoothEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
            } else {
                if(!bt.isServiceAvailable()) {
                    bt.setupService();
                    bt.startService(BluetoothState.DEVICE_ANDROID);
                    //habilitar_botones();        // los botones solo se habilitan si el bluetooth esta prendido
                }
            }
        }

        else if(id == R.id.menu_disconnect) {
            if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
                bt.disconnect();
        }
        return super.onOptionsItemSelected(item);
        */
        switch (item.getItemId()) {
            /*case R.id.menu_prender_wifi:


                Toast.makeText(getApplicationContext()
                        , "enable wifi"
                        , Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.menu_device_connect:
                bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.menu_aprender:
                if (!bt.isBluetoothEnabled()) {
                    Intent intent1 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent1, BluetoothState.REQUEST_ENABLE_BT);
                } else {
                    if(!bt.isServiceAvailable()) {
                        bt.setupService();
                        bt.startService(BluetoothState.DEVICE_ANDROID);
                        //habilitar_botones();        // los botones solo se habilitan si el bluetooth esta prendido
                    }
                }
                return true;
        /*    case R.id.menu_disconnect:
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
                bt.disconnect();
                return true;
            case R.id.atn_direct_enable:
                Toast.makeText(getApplicationContext()
                        , "enable pulsado"
                        , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.atn_direct_discover:
                Toast.makeText(getApplicationContext()
                        , "discover pulsado"
                        , Toast.LENGTH_SHORT).show();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
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
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                //habilitar_botones();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
        bt.disconnect();            //desconecta del dispositivo conectado
        bt.desconectarbluetooth();  //apaga bluetooth
        Toast.makeText(getApplicationContext()
                , "onDestroy"
                , Toast.LENGTH_SHORT).show();
    }
    public void actualizar_dato_enviado(String textovalor){
        enviado.setText(textovalor.toString());
        if(bt.isBluetoothEnabled()){
            bt.send(textovalor, true);
        }
    }
}
