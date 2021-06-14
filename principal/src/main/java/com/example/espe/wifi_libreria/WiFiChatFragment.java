
package com.example.espe.wifi_libreria;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.espe.controlbluetoothv1.R;

import java.util.ArrayList;
import java.util.List;
/**
 * This fragment handles chat related UI which includes a list view for messages
 * and a message entry field with send button.
 */
public class WiFiChatFragment extends Fragment implements View.OnClickListener {

    String TAG="WiFiChatFragment";
    private View view;
    private ChatManager chatManager;
    private TextView chatLine,a;
    private ListView listView;
    private Button btn_enviar,button_desconectar_wifi;
    ChatMessageAdapter adapter = null;
    private List<String> items = new ArrayList<String>();
    wifi_fragmento wf;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            wf = (wifi_fragmento)activity;
        }catch(ClassCastException e){
            throw  new ClassCastException("Implementar Metodo finalizar_coneccion_wifi");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatLine = (TextView) view.findViewById(R.id.txtChatLine);
        listView = (ListView) view.findViewById(android.R.id.list);
        adapter = new ChatMessageAdapter(getActivity(), android.R.id.text1,items);
        listView.setAdapter(adapter);
        btn_enviar= (Button) view.findViewById(R.id.btn_enviar);
        btn_enviar.setOnClickListener(this);
        button_desconectar_wifi=(Button) view.findViewById(R.id.button_desconectar_wifi);
        button_desconectar_wifi.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==button_desconectar_wifi.getId()){
            wf.finalizar_coneccion_wifi();
        }else {
            if (chatManager != null) {
                Log.d(TAG, "chat: " + chatLine.getText().toString());
                chatManager.write(chatLine.getText().toString().getBytes());
                pushMessage("Me: " + chatLine.getText().toString());
                Log.d(TAG, "chat: " + chatLine.getText().toString());
                //MessageTarget.enviar_wifi_mi_dato(chatLine.getText().toString());
                //chatLine.setText("");
                chatLine.clearFocus();

            } else {
                Log.d(TAG, "chat: null");
            }
        }
    }
        /**  SOLO PRUEBA*/
        public ChatManager getChatManager() {
            return chatManager;
        }
    /**  SOLO PRUEBA*/
    public interface MessageTarget {
        public Handler getHandler();
        void finalizar_coneccion_wifi();
        //void recibido_wifi_mi_dato(String dato);
    }
    public interface wifi_fragmento {
        public void finalizar_coneccion_wifi();
    }

    public void setChatManager(ChatManager obj) {
        chatManager = obj;
    }

    public void pushMessage(String readMessage) {
        adapter.add(readMessage);
        adapter.notifyDataSetChanged();
    }

    /**
     * ArrayAdapter to manage chat messages.
     */
    public class ChatMessageAdapter extends ArrayAdapter<String> {

        List<String> messages = null;

        public ChatMessageAdapter(Context context, int textViewResourceId,
                List<String> items) {
            super(context, textViewResourceId, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }
            String message = items.get(position);
            if (message != null && !message.isEmpty()) {
                TextView nameText = (TextView) v
                        .findViewById(android.R.id.text1);

                if (nameText != null) {
                    nameText.setText(message);
                    if (message.startsWith("Me: ")) {
                        nameText.setTextAppearance(getActivity(),
                                R.style.normalText);
                    } else {
                        nameText.setTextAppearance(getActivity(),
                                R.style.boldText);
                    }
                }
            }
            return v;
        }
    }
}
