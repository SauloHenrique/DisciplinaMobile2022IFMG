package com.example.testegps.ferramentas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;

public class ControlaGPS implements LocationListener {

    //ultima posicao do usuario
    private static double lat;
    private static double lng;

    private static TextView campoTxtLatLng;

    private static Context contexto;

    private static ControlaGPS instancia;
    private IMapController controleMapa;

    private static LocationManager gerenciadorEventosGPS;

    private ControlaGPS(Context cont) {
        contexto = cont;
    }

    public static ControlaGPS getInstance(Context contexto) {
        //sera que eu não criei um outro controla GPS antes?
        if (instancia == null) {
            instancia = new ControlaGPS(contexto);
        }

        return instancia;
    }

    public void atualizaGPS() {
        configuraGPS();
    }

    public void configuraGPS() {
        gerenciadorEventosGPS = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);

        Criteria confLocal = new Criteria();
        //confLocal.setAccuracy(Criteria.ACCURACY_COARSE);

        //sera que o usuario perm. o acesso a local.
        if (ActivityCompat.checkSelfPermission(contexto,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(contexto,"Sem permissão do acesso a gps",Toast.LENGTH_SHORT).show();
            return;
        }

        //primeira tentativa via sensor GPS
        String provider = LocationManager.GPS_PROVIDER;

        //recuperando a ultima loc. a partir do SENSOR GPS!!
        Location ultimaLocal = gerenciadorEventosGPS.getLastKnownLocation(provider);

        //não existe uma local. valida
        if(ultimaLocal == null){

            //vamos tentar recuperar a local. via rede
            provider = LocationManager.NETWORK_PROVIDER;
            ultimaLocal = gerenciadorEventosGPS.getLastKnownLocation(provider);
        }

        onLocationChanged(ultimaLocal);

        gerenciadorEventosGPS.requestLocationUpdates(provider,0,0,this);
    }

    public void atualizarCampoTexto(TextView campoLatLng){
        campoTxtLatLng = campoLatLng;
        configuraGPS();
    }

    @Override
    public void onLocationChanged(@NonNull Location novaLocalUsuario) {

        System.out.println("on location");

        if(novaLocalUsuario != null) {
            //aqui SEMPRE rec. a nova local. do usuário
            lat = novaLocalUsuario.getLatitude();
            lng = novaLocalUsuario.getLongitude();

            campoTxtLatLng.setText(String.format("%.4f", lat) + "  " + String.format("%.4f", lng));

            if(controleMapa != null){
                controleMapa.animateTo(new GeoPoint(lat,lng));
            }

        }
    }

    public void addControleMapa(IMapController novoControle){
        controleMapa = novoControle;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
