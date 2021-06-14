package com.example.espe.controlbluetoothv1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.LinearLayout;


public class PanelPrincipalFragment extends Fragment {

    View view;
    LinearLayout wifi_z, blue_y,controles_x;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_panel_principal, null);
        wifi_z =(LinearLayout) view.findViewById(R.id.wifi_z);
        //blue_y =(LinearLayout) view.findViewById(R.id.blue_y);
        controles_x =(LinearLayout) view.findViewById(R.id.controles_x);
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }


    public void wifidesaparecer(){

        wifi_z.setVisibility(View.GONE);
    }
    public void wifiaparecer(){
        wifi_z.setVisibility(View.VISIBLE);
    }
    public boolean iswifiVisibility(){
        int visible=wifi_z.getVisibility();
        if(visible==View.VISIBLE){
            return true;
        }else{
            return false;
        }
    }

    public void bluedesaparecer(){

        blue_y.setVisibility(View.GONE);
    }
    public void blueaparecer(){
        blue_y.setVisibility(View.VISIBLE);
    }
    public boolean isblueVisibility(){
        int visible=blue_y.getVisibility();
        if(visible==View.VISIBLE){
            return true;
        }else{
            return false;
        }
    }

    public void controlesdesaparecer(){

        controles_x.setVisibility(View.GONE);
    }
    public void controlesaparecer(){
        controles_x.setVisibility(View.VISIBLE);
    }
    public boolean iscontrolesVisibility(){
        int visible=controles_x.getVisibility();
        if(visible==View.VISIBLE){
            return true;
        }else{
            return false;
        }
    }

    public void encogerblue(){
        blue_y.getLayoutParams().width = Gallery.LayoutParams.WRAP_CONTENT;
    }
    public void explanderblue(){
        blue_y.getLayoutParams().width = Gallery.LayoutParams.MATCH_PARENT;
    }

}
