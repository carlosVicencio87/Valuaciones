package com.Valuaciones;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.backup.BackupDataOutput;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int PERMISO_LOCATION=1;
    private LocationManager locationManager;
    private double latitud,longitud,latUpdate,longUpdate;
    private TextView puntoPartida,valTierra,area_total,val_total_tierra,area_total_construccion,niveles_total,puntos_matriz,clase,
            valor_unitario,valor_dinero_m2,instalaciones;
    private EditText area,areaConstruccion,niveles;
    private ImageView area_aprobada,cambiar_area,cambiar_area_construccion,area_construccion_aprobada,cambiar_nivel,niveles_aprobados;
    private String direccion;
    private Button sigCalculo,sig_Calculo,sigPag,calcularPrecioConstr;
    private String calle,numeroAlcaldia, nombre_colonia,nombre_alcaldia,colonia_catastral,valor,cp,ciudad,pais;
    private Double areafinal,areaConsFinal,valorConstruccion,valor_muros,valor_materiales,valorMateriales,valor_pisos,valorPisos,
            valor_estruc,valor_estructura,valorAcabadosM,valor_cubiertas,valorCubiertas,valor_acabadosM,valorAcabadosP,valor_acabadosP,
            valorFachadas,valor_fachadas,valorVentanerias,valor_ventanerias,valorRecubrimiento,valor_recubrimiento,valorBanos,valor_banos,
            nivelesFinal,valor_total_matriz,tipo_clase,valor_unitario_total,precio_fisico_construccion,valor_instalaciones;
    private LatLng coord,coordenadas,latLong;
    private Marker marker;
    private LinearLayout mapaid,cajaEditararea,cajaArea,cajaAreaCons,cajaCons,cajaEditNiveles,cajaNiveles;
    private ScrollView valor_construccion,val_tierra,valor_acabados,valor_puntos,valor_m2;
    private Fragment map;
    private int check=0;
    private static String SERVIDOR_CONTROLADOR;
    private ExecutorService executorService;
    private JSONObject jsonObject;
    private JSONArray json_datos_catastrales;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String valorTotalTerreno,seleccion_estructura,seleccion_materiales,seleccion_pisos,
            seleccion_cubiertas,seleccion_acabadosM,seleccion_acabadosP,
            seleccion_fachadas,seleccion_ventaneria,seleccion_recubrimiento,seleccion_banos;
    private Mapa activity;
    public ArrayList<SpinnerModel> listaEstructura = new ArrayList<>();
    private Spinner tipo_estructura,materiales_muros,entrepisos,cubiertas,
            acabados_muros,acabados_pisos,acabados_fachadas,acabados_ventaneria
            ,acabados_recubrimiento,acabadosbanos;
    public ArrayList<SpinnerModel> listaMateriales = new ArrayList<>();
    public ArrayList<SpinnerModel> listaEntrepisos = new ArrayList<>();
    public ArrayList<SpinnerModel> listaCubiertas = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosMuros = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosPisos = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosFachadas = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosVentaneria = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosRecubrimiento= new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosBanos= new ArrayList<>();
    private AdapterTipoEstructura adapterTipoEstructura;
    private AdapterMaterialesMuros adapterMaterialesMuros;
    private AdapterEntrepisos adapterEntrepisos;
    private AdapterCubiertas adapterCubiertas;
    private AdapterAcabadosMuros adapterAcabadosMuros;
    private AdapterAcabadosPisos adapterAcabadosPisos;
    private AdapterAcabadosFachadas adapterAcabadosFachadas;
    private AdapterAcabadosVentaneria adapterAcabadosVentaneria;
    private AdapterAcabadosRecubrimientos adapterAcabadosRecubrimientos;
    private AdapterAcabadosBanos adapterAcabadosBanos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        val_total_tierra=findViewById(R.id.val_total_tierra);
        area_total=findViewById(R.id.area_total);
        cajaArea=findViewById(R.id.cajaArea);
        cambiar_area=findViewById(R.id.cambiar_area);
        area_aprobada=findViewById(R.id.area_aprobada);
        area=findViewById(R.id.area);
        puntoPartida=findViewById(R.id.puntoPartida);
        valTierra=findViewById(R.id.valTierra);
        val_tierra=findViewById(R.id.val_tierra);
        mapaid=findViewById(R.id.mapaid);
        SERVIDOR_CONTROLADOR = new Servidor().local;
        cajaEditararea=findViewById(R.id.cajaEditararea);
        sigCalculo=findViewById(R.id.sigCalculo);
        valor_construccion=findViewById(R.id.valor_construccion);
        cajaAreaCons=findViewById(R.id.cajaAreaCons);
        cajaCons=findViewById(R.id.cajaCons);
        cambiar_area_construccion=findViewById(R.id.cambiar_area_construccion);
        area_construccion_aprobada=findViewById(R.id.area_construccion_aprobada);
        area_total_construccion=findViewById(R.id.area_total_construccion);
        valor_acabados=findViewById(R.id.valor_acabados);
        tipo_estructura=findViewById(R.id.tipo_estructura);
        activity = this;
        materiales_muros=findViewById(R.id.materiales_muros);
        entrepisos=findViewById(R.id.entrepisos);
        cubiertas=findViewById(R.id.cubiertas);
        areaConstruccion=findViewById(R.id.areaConstruccion);
        area_total_construccion=findViewById(R.id.area_total_construccion);
        sig_Calculo=findViewById(R.id.sig_Calculo);
        acabados_muros=findViewById(R.id.acabados_muros);
        acabados_pisos=findViewById(R.id.acabados_pisos);
        acabados_fachadas=findViewById(R.id.acabados_fachadas);
        acabados_ventaneria=findViewById(R.id.acabados_ventaneria);
        acabados_recubrimiento=findViewById(R.id.acabados_recubrimiento);
        acabadosbanos=findViewById(R.id.acabadosbanos);
        sigPag=findViewById(R.id.sigPag);
        valor_puntos=findViewById(R.id.valor_puntos);
        cajaEditNiveles=findViewById(R.id.cajaEditNiveles);
        cajaNiveles=findViewById(R.id.cajaNiveles);
        niveles=findViewById(R.id.niveles);
        cambiar_nivel=findViewById(R.id.cambiar_nivel);
        puntos_matriz=findViewById(R.id.puntos_matriz);
        clase=findViewById(R.id.clase);
        valor_unitario=findViewById(R.id.valor_unitario);
        niveles_total=findViewById(R.id.niveles_total);
        valor_m2=findViewById(R.id.valor_m2);
        valor_dinero_m2=findViewById(R.id.valor_dinero_m2);
        calcularPrecioConstr=findViewById(R.id.calcularPrecioConstr);
        instalaciones=findViewById(R.id.instalaciones);
        setListaEstructura();
        setListaMateriales();
        setListaEntrepisos();
        setListaCubiertas();
        setListaAcabadosMuros();
        setListaAcabadosPisos();
        setListaAcabadosFachadas();
        setListaVentaneria();
        setListaAcabadosRecubrimiento();
        setListaAcabadosBanos();
        final int permisoLocacion = ContextCompat.checkSelfPermission(Mapa.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permisoLocacion!= PackageManager.PERMISSION_GRANTED) {
            solicitarPermisoLocation();

            Log.e("paso","paso");
        }
        sharedPreferences=getSharedPreferences("datos_catastrales",this.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        executorService= Executors.newSingleThreadExecutor();
        cambiar_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cajaEditararea.setVisibility(view.GONE);
                cajaArea.setVisibility(View.VISIBLE);
                areafinal= Double.parseDouble(area.getText().toString());
                Log.e("putotopo",areafinal+"");
                area_total.setText(String.valueOf(areafinal));
                Log.e("estoestabien",area_total.getText().toString()+"");
                Log.e("res",""+area.getText().toString());
                Log.e("12",""+valorTotalTerreno);
                Double valor_1=Double.parseDouble(area.getText().toString());
                Double valor_2 = Double.parseDouble(valorTotalTerreno);
                Double total = valor_1*valor_2;
                Log.e("total_area",total+"");
                val_total_tierra.setText("$"+total);
            }
        });
        area_aprobada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cajaAreaCons.setVisibility(view.GONE);
                cajaCons.setVisibility(View.VISIBLE);
            }
        });
        sigCalculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val_tierra.setVisibility(View.GONE);
                valor_construccion.setVisibility(View.VISIBLE);
            }
        });
        sig_Calculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valor_construccion.setVisibility(View.GONE);
                valor_acabados.setVisibility(View.VISIBLE);
            }
        });
        cambiar_area_construccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cajaAreaCons.setVisibility(View.GONE);
                cajaCons.setVisibility(View.VISIBLE);
                areaConsFinal=Double.parseDouble(areaConstruccion.getText().toString());
                Log.e("putotopo",areaConsFinal+"");
                area_total_construccion.setText(String.valueOf(areaConsFinal));
                Log.e("yaentendi",area_total_construccion.getText().toString()+"");
                valorConstruccion= Double.parseDouble(areaConstruccion.getText().toString());
                Log.e("melapelas",valorConstruccion+"");
            }
        });
        area_construccion_aprobada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cajaCons.setVisibility(View.GONE);
                cajaAreaCons.setVisibility(View.VISIBLE);

            }
        });
        adapterTipoEstructura = new AdapterTipoEstructura(activity,R.layout.tipo_estructura,listaEstructura,getResources());
        tipo_estructura.setAdapter(adapterTipoEstructura);


        adapterMaterialesMuros = new AdapterMaterialesMuros(activity,R.layout.tipo_materiales,listaMateriales,getResources());
        materiales_muros.setAdapter(adapterMaterialesMuros);

        adapterEntrepisos = new AdapterEntrepisos(activity,R.layout.tipo_entrepisos,listaEntrepisos,getResources());
        entrepisos.setAdapter(adapterEntrepisos);


        adapterCubiertas = new AdapterCubiertas(activity,R.layout.tipo_cubiertas,listaCubiertas,getResources());
        cubiertas.setAdapter(adapterCubiertas);

        adapterAcabadosMuros = new AdapterAcabadosMuros(activity,R.layout.acabados_muros,listaAcabadosMuros,getResources());
        acabados_muros.setAdapter(adapterAcabadosMuros);

        adapterAcabadosPisos = new AdapterAcabadosPisos(activity,R.layout.acabados_pisos,listaAcabadosPisos,getResources());
        acabados_pisos.setAdapter(adapterAcabadosPisos);

        adapterAcabadosFachadas = new AdapterAcabadosFachadas(activity,R.layout.acabaos_fachadas,listaAcabadosFachadas,getResources());
        acabados_fachadas.setAdapter(adapterAcabadosFachadas);

        adapterAcabadosVentaneria = new AdapterAcabadosVentaneria(activity,R.layout.acabados_ventaneria,listaAcabadosVentaneria,getResources());
        acabados_ventaneria.setAdapter(adapterAcabadosVentaneria);

        adapterAcabadosRecubrimientos = new AdapterAcabadosRecubrimientos(activity,R.layout.acabados_recubrimiento,listaAcabadosRecubrimiento,getResources());
        acabados_recubrimiento.setAdapter(adapterAcabadosRecubrimientos);

        adapterAcabadosBanos = new AdapterAcabadosBanos(activity,R.layout.acabados_banos,listaAcabadosBanos,getResources());
        acabadosbanos.setAdapter(adapterAcabadosBanos);

        tipo_estructura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_estruc=findViewById(R.id.texto);
                if (tipo_estruc==null)
                {
                    tipo_estruc=(TextView) view.findViewById(R.id.texto);
                }
                else
                {
                    tipo_estruc=(TextView) view.findViewById(R.id.texto);
                }
                if (position>0)
                {
                  seleccion_estructura= String.valueOf(1);
                    Log.e("val1",""+seleccion_estructura);
                     valor_estructura= Double.valueOf(seleccion_estructura);
                    valor_estruc=valor_estructura*valorConstruccion;
                    Log.e("val1",""+valor_estruc);
                    if(valor_estruc<51){
                        valor_estruc= Double.valueOf(7);
                        Log.e("val1",""+valor_estruc);
                    }
                    if(valor_estruc>51&&valor_estruc<250){
                        valor_estruc=Double.valueOf(8);
                        Log.e("val1",""+valor_estruc);
                    }
                    if(valor_estruc>251&&valor_estruc<650){
                        valor_estruc=Double.valueOf(9);
                        Log.e("val1",""+valor_estruc);
                    }
                    if(valor_estruc>651){
                        valor_estruc=Double.valueOf(11);
                        Log.e("val1",""+valor_estruc);
                    }
                }
                Log.e("tipo",""+seleccion_estructura);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        materiales_muros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_materiales=findViewById(R.id.tipoMateriales);
                if (tipo_materiales==null)
                {
                    tipo_materiales=(TextView) view.findViewById(R.id.tipoMateriales);
                }
                else
                {
                    tipo_materiales=(TextView) view.findViewById(R.id.tipoMateriales);
                }
                    seleccion_materiales =tipo_materiales.getText().toString();
                    Log.e("tipomat",""+seleccion_materiales);
                if(seleccion_materiales.equals("Lamina")){
                        seleccion_materiales= String.valueOf(1);
                        valor_materiales= Double.valueOf(seleccion_materiales);
                        valorMateriales=valor_materiales*valorConstruccion;
                        Log.e("val",""+valorMateriales);
                        if(valorMateriales>1){
                            valorMateriales= Double.valueOf(String.valueOf(8));
                            Log.e("val2",""+valorMateriales);
                        }
                    }
                if(seleccion_materiales.equals("Madera")){
                        seleccion_materiales= String.valueOf(1);
                        valor_materiales= Double.valueOf(seleccion_materiales);
                        valorMateriales=valor_materiales*valorConstruccion;
                        Log.e("val2",""+valorMateriales);
                        if(valorMateriales>1){
                            valorMateriales= Double.valueOf(String.valueOf(8));
                            Log.e("val2",""+valorMateriales);
                        }
                    }
                if(seleccion_materiales.equals("Tabicon")){
                    seleccion_materiales= String.valueOf(1);
                    valor_materiales= Double.valueOf(seleccion_materiales);
                    valorMateriales=valor_materiales*valorConstruccion;
                    Log.e("val2",""+valorMateriales);
                    if(valorMateriales>1&&valorMateriales<51){
                        valorMateriales= Double.valueOf(String.valueOf(8));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>51){
                        valorMateriales= Double.valueOf(String.valueOf(11));
                        Log.e("val2",""+valorMateriales);
                    }
                }
                if(seleccion_materiales.equals("Block")){
                    seleccion_materiales= String.valueOf(1);
                    valor_materiales= Double.valueOf(seleccion_materiales);
                    valorMateriales=valor_materiales*valorConstruccion;
                    Log.e("val2",""+valorMateriales);
                    if(valorMateriales>1&&valorMateriales<86){
                        valorMateriales= Double.valueOf(String.valueOf(11));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>86&&valorMateriales<151){
                        valorMateriales= Double.valueOf(String.valueOf(18));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>151&&valorMateriales<251){
                        valorMateriales= Double.valueOf(String.valueOf(19));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>251&&valorMateriales<451){
                        valorMateriales= Double.valueOf(String.valueOf(20));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>451&&valorMateriales<651){
                        valorMateriales= Double.valueOf(String.valueOf(21));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>651){
                        valorMateriales= Double.valueOf(String.valueOf(22));
                        Log.e("val2",""+valorMateriales);
                    }
                }
                if(seleccion_materiales.equals("Tabique")){
                    seleccion_materiales= String.valueOf(1);
                    valor_materiales= Double.valueOf(seleccion_materiales);
                    valorMateriales=valor_materiales*valorConstruccion;
                    Log.e("val2",""+valorMateriales);
                    if(valorMateriales>1&&valorMateriales<86){
                        valorMateriales= Double.valueOf(String.valueOf(11));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>86&&valorMateriales<151){
                        valorMateriales= Double.valueOf(String.valueOf(18));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>151&&valorMateriales<251){
                        valorMateriales= Double.valueOf(String.valueOf(19));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>251&&valorMateriales<451){
                        valorMateriales= Double.valueOf(String.valueOf(20));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>451&&valorMateriales<651){
                        valorMateriales= Double.valueOf(String.valueOf(21));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>651){
                        valorMateriales= Double.valueOf(String.valueOf(22));
                        Log.e("val2",""+valorMateriales);
                    }
                }
                if(seleccion_materiales.equals("Sillar de adobe")){
                    seleccion_materiales= String.valueOf(1);
                    valor_materiales= Double.valueOf(seleccion_materiales);
                    valorMateriales=valor_materiales*valorConstruccion;
                    Log.e("val2",""+valorMateriales);
                    if(valorMateriales>1&&valorMateriales<86){
                        valorMateriales= Double.valueOf(String.valueOf(11));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>86&&valorMateriales<151){
                        valorMateriales= Double.valueOf(String.valueOf(18));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>151&&valorMateriales<251){
                        valorMateriales= Double.valueOf(String.valueOf(19));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>251&&valorMateriales<451){
                        valorMateriales= Double.valueOf(String.valueOf(20));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>451&&valorMateriales<651){
                        valorMateriales= Double.valueOf(String.valueOf(21));
                        Log.e("val2",""+valorMateriales);
                    }
                    if(valorMateriales>651){
                        valorMateriales= Double.valueOf(String.valueOf(22));
                        Log.e("val2",""+valorMateriales);
                    }
                }
                Log.e("tipo",""+seleccion_estructura);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        entrepisos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_pisoss=findViewById(R.id.tipoEntrepisos);
                if (tipo_pisoss==null)
                {
                    tipo_pisoss=(TextView) view.findViewById(R.id.tipoEntrepisos);
                }
                else
                {
                    tipo_pisoss=(TextView) view.findViewById(R.id.tipoEntrepisos);
                }
                seleccion_pisos =tipo_pisoss.getText().toString();
                Log.e("tipomat",""+seleccion_pisos);
                if(seleccion_pisos.equals("Sin entrepisos")){
                    seleccion_pisos= String.valueOf(1);
                    valor_pisos= Double.valueOf(seleccion_pisos);
                    valorPisos=valor_pisos*valorConstruccion;
                    Log.e("val3",""+valorMateriales);
                    if(valorPisos>1){
                        valorPisos= Double.valueOf(String.valueOf(1));
                        Log.e("val3",""+valorMateriales);
                    }
                }

                if(seleccion_pisos.equals("Con o sin losa de concreto")){
                    seleccion_pisos= String.valueOf(1);
                    valor_pisos= Double.valueOf(seleccion_pisos);
                    valorPisos=valor_pisos*valorConstruccion;
                    Log.e("val3",""+valorPisos);
                    if(valorPisos>1&&valorPisos<86){
                        valorPisos= Double.valueOf(String.valueOf(4));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>86&&valorPisos<151){
                        valorPisos= Double.valueOf(String.valueOf(5));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>151&&valorPisos<251){
                        valorPisos= Double.valueOf(String.valueOf(7));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>251&&valorPisos<451){
                        valorPisos= Double.valueOf(String.valueOf(8));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>451&&valorPisos<651){
                        valorPisos= Double.valueOf(String.valueOf(9));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>651){
                        valorPisos= Double.valueOf(String.valueOf(10));
                        Log.e("val3",""+valorPisos);
                    }
                }
                if(seleccion_pisos.equals("Con/Sin L.C y/o losa aligerada")){
                    seleccion_pisos= String.valueOf(1);
                    valor_pisos= Double.valueOf(seleccion_pisos);
                    valorPisos=valor_pisos*valorConstruccion;
                    Log.e("val3",""+valorPisos);
                    if(valorPisos>1&&valorPisos<86){
                        valorPisos= Double.valueOf(String.valueOf(4));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>86&&valorPisos<151){
                        valorPisos= Double.valueOf(String.valueOf(5));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>151&&valorPisos<251){
                        valorPisos= Double.valueOf(String.valueOf(7));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>251&&valorPisos<451){
                        valorPisos= Double.valueOf(String.valueOf(8));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>451&&valorPisos<651){
                        valorPisos= Double.valueOf(String.valueOf(9));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>651){
                        valorPisos= Double.valueOf(String.valueOf(10));
                        Log.e("val3",""+valorPisos);
                    }
                }
                if(seleccion_pisos.equals("Con/Sin L.C y/o de Madera")){
                    seleccion_pisos= String.valueOf(1);
                    valor_pisos= Double.valueOf(seleccion_pisos);
                    valorPisos=valor_pisos*valorConstruccion;
                    Log.e("val3",""+valorPisos);
                    if(valorPisos>1&&valorPisos<86){
                        valorPisos= Double.valueOf(String.valueOf(4));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>86&&valorPisos<151){
                        valorPisos= Double.valueOf(String.valueOf(5));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>151&&valorPisos<251){
                        valorPisos= Double.valueOf(String.valueOf(7));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>251&&valorPisos<451){
                        valorPisos= Double.valueOf(String.valueOf(8));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>451&&valorPisos<651){
                        valorPisos= Double.valueOf(String.valueOf(9));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>651){
                        valorPisos= Double.valueOf(String.valueOf(10));
                        Log.e("val3",""+valorPisos);
                    }
                }
                if(seleccion_pisos.equals("Con/Sin L.C y/o losa reticular")){
                    seleccion_pisos= String.valueOf(1);
                    valor_pisos= Double.valueOf(seleccion_pisos);
                    valorPisos=valor_pisos*valorConstruccion;
                    Log.e("val3",""+valorMateriales);
                    if(valorPisos>1&&valorPisos<86){
                        valorPisos= Double.valueOf(String.valueOf(4));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>86&&valorPisos<151){
                        valorPisos= Double.valueOf(String.valueOf(5));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>151&&valorPisos<251){
                        valorPisos= Double.valueOf(String.valueOf(7));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>251&&valorPisos<451){
                        valorPisos= Double.valueOf(String.valueOf(8));
                        Log.e("val3",""+valorPisos);
                    }
                    if(valorPisos>451&&valorMateriales<651){
                        valorPisos= Double.valueOf(String.valueOf(9));
                        Log.e("val3",""+valorMateriales);
                    }
                    if(valorPisos>651){
                        valorPisos= Double.valueOf(String.valueOf(10));
                        Log.e("val3",""+valorPisos);
                    }
                }
                Log.e("tipo",""+seleccion_estructura);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cubiertas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_cubiertas=findViewById(R.id.tipoCubiertas);
                if (tipo_cubiertas==null)
                {
                    tipo_cubiertas=(TextView) view.findViewById(R.id.tipoCubiertas);
                }
                else
                {
                    tipo_cubiertas=(TextView) view.findViewById(R.id.tipoCubiertas);
                }
                seleccion_cubiertas =tipo_cubiertas.getText().toString();
                Log.e("tipomat",""+seleccion_cubiertas);
                if(seleccion_cubiertas.equals("Lamina")){
                    seleccion_cubiertas= String.valueOf(1);
                    valor_cubiertas= Double.valueOf(seleccion_cubiertas);
                    valorCubiertas=valor_cubiertas*valorConstruccion;
                    Log.e("val4",""+valorCubiertas);
                    if(valorCubiertas>1){
                        valorCubiertas= Double.valueOf(String.valueOf(4));
                        Log.e("val4",""+valorCubiertas);
                    }
                }

                if(seleccion_cubiertas.equals("Losa de concreto")){
                    seleccion_cubiertas= String.valueOf(1);
                    valor_cubiertas= Double.valueOf(seleccion_cubiertas);
                    valorCubiertas=valor_cubiertas*valorConstruccion;
                    Log.e("val4",""+valorCubiertas);
                    if(valorCubiertas>1&&valorCubiertas<86){
                        valorCubiertas= Double.valueOf(String.valueOf(5));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>86&&valorCubiertas<151){
                        valorCubiertas= Double.valueOf(String.valueOf(6));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>151&&valorCubiertas<251){
                        valorCubiertas= Double.valueOf(String.valueOf(9));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>251&&valorCubiertas<451){
                        valorCubiertas= Double.valueOf(String.valueOf(13));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>451&&valorCubiertas<651){
                        valorCubiertas= Double.valueOf(String.valueOf(14));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>651){
                        valorCubiertas= Double.valueOf(String.valueOf(16));
                        Log.e("val4",""+valorCubiertas);
                    }
                }
                if(seleccion_cubiertas.equals("L.C y/o Aligerada")){
                    seleccion_cubiertas= String.valueOf(1);
                    valor_cubiertas= Double.valueOf(seleccion_cubiertas);
                    valorCubiertas=valor_cubiertas*valorConstruccion;
                    Log.e("val4",""+valorCubiertas);
                    if(valorCubiertas>1&&valorCubiertas<86){
                        valorCubiertas= Double.valueOf(String.valueOf(5));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>86&&valorCubiertas<151){
                        valorCubiertas= Double.valueOf(String.valueOf(6));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>151&&valorCubiertas<251){
                        valorCubiertas= Double.valueOf(String.valueOf(9));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>251&&valorCubiertas<451){
                        valorCubiertas= Double.valueOf(String.valueOf(13));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>451&&valorCubiertas<651){
                        valorCubiertas= Double.valueOf(String.valueOf(14));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>651){
                        valorCubiertas= Double.valueOf(String.valueOf(15));
                        Log.e("val4",""+valorCubiertas);
                    }
                }
                if(seleccion_cubiertas.equals("L.C y/o Madera")){
                    seleccion_cubiertas= String.valueOf(1);
                    valor_cubiertas= Double.valueOf(seleccion_cubiertas);
                    valorCubiertas=valor_cubiertas*valorConstruccion;
                    Log.e("val4",""+valorCubiertas);
                    if(valorCubiertas>1&&valorCubiertas<86){
                        valorCubiertas= Double.valueOf(String.valueOf(5));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>86&&valorCubiertas<151){
                        valorCubiertas= Double.valueOf(String.valueOf(6));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>151&&valorCubiertas<251){
                        valorCubiertas= Double.valueOf(String.valueOf(9));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>251&&valorCubiertas<451){
                        valorCubiertas= Double.valueOf(String.valueOf(13));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>451&&valorCubiertas<651){
                        valorCubiertas= Double.valueOf(String.valueOf(14));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>651){
                        valorCubiertas= Double.valueOf(String.valueOf(16));
                        Log.e("val4",""+valorCubiertas);
                    }
                }
                if(seleccion_cubiertas.equals("L.C y/o Reticular")){
                    seleccion_cubiertas= String.valueOf(1);
                    valor_cubiertas= Double.valueOf(seleccion_cubiertas);
                    valorCubiertas=valor_cubiertas*valorConstruccion;
                    Log.e("val4",""+valorCubiertas);
                    if(valorCubiertas>1&&valorCubiertas<86){
                        valorCubiertas= Double.valueOf(String.valueOf(5));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>86&&valorCubiertas<151){
                        valorCubiertas= Double.valueOf(String.valueOf(6));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>151&&valorCubiertas<251){
                        valorCubiertas= Double.valueOf(String.valueOf(9));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>251&&valorCubiertas<451){
                        valorCubiertas= Double.valueOf(String.valueOf(13));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>451&&valorCubiertas<651){
                        valorCubiertas= Double.valueOf(String.valueOf(14));
                        Log.e("val4",""+valorCubiertas);
                    }
                    if(valorCubiertas>651){
                        valorCubiertas= Double.valueOf(String.valueOf(16));
                        Log.e("val4",""+valorCubiertas);
                    }
                }
                Log.e("tipo",""+seleccion_estructura);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabados_muros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView acabados_muro=findViewById(R.id.acabadosMuros);
                if (acabados_muro==null)
                {
                    acabados_muro=(TextView) view.findViewById(R.id.acabadosMuros);
                }
                else
                {
                    acabados_muro=(TextView) view.findViewById(R.id.acabadosMuros);
                }
                seleccion_acabadosM =acabados_muro.getText().toString();
                Log.e("tipomat",""+seleccion_acabadosM);
                if(seleccion_acabadosM.equals("No hay o muy escasos")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(0));
                        Log.e("val5",""+valorAcabadosM);
                    }
                }

                if(seleccion_acabadosM.equals("Tabique y/o block aparente")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosM);
                    }

                }
                if(seleccion_acabadosM.equals("Anteriores y/o aplanado yeso con pintura")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosM);
                    }

                }
                if(seleccion_acabadosM.equals("Anteriores y/o aplanado de mezcla con pintura")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosM);
                    }

                }
                if(seleccion_acabadosM.equals("Anteriores y/o pasta texturizada")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosM);
                    }
                }
                if(seleccion_acabadosM.equals("Anteriores y/o papel tapiz")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosM);
                    }
                }
                if(seleccion_acabadosM.equals("Anteriores y/o pasta texturizada con color integral")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(8));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o papel tapiz plastificado")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(8));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o tapiz de tela")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(9));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o cenefas de madera")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(9));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o  pastas quimicas con mezclas especiales")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(12));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("val5",""+seleccion_estructura);
                    Log.e("val5",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o tapiz de tela importado")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(12));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o estucos o frescos decorativos")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(12));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }

         }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabados_muros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView acabados_muro=findViewById(R.id.acabadosMuros);
                if (acabados_muro==null)
                {
                    acabados_muro=(TextView) view.findViewById(R.id.acabadosMuros);
                }
                else
                {
                    acabados_muro=(TextView) view.findViewById(R.id.acabadosMuros);
                }
                seleccion_acabadosM =acabados_muro.getText().toString();
                Log.e("tipomat",""+seleccion_acabadosM);
                if(seleccion_acabadosM.equals("No hay o muy escasos")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(0));
                        Log.e("val5",""+valorAcabadosM);
                    }
                }

                if(seleccion_acabadosM.equals("Tabique y/o block aparente")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosM);
                    }

                }
                if(seleccion_acabadosM.equals("Anteriores y/o aplanado yeso con pintura")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosM);
                    }

                }
                if(seleccion_acabadosM.equals("Anteriores y/o aplanado de mezcla con pintura")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosM);
                    }

                }
                if(seleccion_acabadosM.equals("Anteriores y/o pasta texturizada")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosM);
                    }
                }
                if(seleccion_acabadosM.equals("Anteriores y/o papel tapiz")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosM);
                    }
                }
                if(seleccion_acabadosM.equals("Anteriores y/o pasta texturizada con color integral")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(8));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o papel tapiz plastificado")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(8));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o tapiz de tela")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(9));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o cenefas de madera")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(9));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o  pastas quimicas con mezclas especiales")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(12));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("val5",""+seleccion_estructura);
                    Log.e("val5",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o tapiz de tela importado")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(12));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosM.equals("Anteriores y/o estucos o frescos decorativos")){
                    seleccion_acabadosM= String.valueOf(1);
                    valor_acabadosM= Double.valueOf(seleccion_acabadosM);
                    valorAcabadosM=valor_acabadosM*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosM>1){
                        valorAcabadosM= Double.valueOf(String.valueOf(12));
                        Log.e("val5",""+valorAcabadosM);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabados_pisos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView acabados_piso=findViewById(R.id.acabadosPisos);
                if (acabados_piso==null)
                {
                    acabados_piso=(TextView) view.findViewById(R.id.acabadosPisos);
                }
                else
                {
                    acabados_piso=(TextView) view.findViewById(R.id.acabadosPisos);
                }
                seleccion_acabadosP =acabados_piso.getText().toString();
                Log.e("tipomat",""+seleccion_acabadosP);
                if(seleccion_acabadosP.equals("Piso de tierra y/o Firme de mezcla")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(0));
                        Log.e("val5",""+valorAcabadosP);
                    }
                }

                if(seleccion_acabadosP.equals("Firme de concreto simple o pulido")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(2));
                        Log.e("val5",""+valorAcabadosP);
                    }

                }
                if(seleccion_acabadosP.equals("Firme de concreto simple o pulido y/o Loseta vinilica")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(2));
                        Log.e("val5",""+valorAcabadosP);
                    }

                }
                if(seleccion_acabadosP.equals("Firme de concreto simple o pulido y/o Linoleum")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(2));
                        Log.e("val5",""+valorAcabadosP);
                    }

                }
                if(seleccion_acabadosP.equals("Firme de concreto simple o pulido y/o Alfombra tipo A")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(2));
                        Log.e("val5",""+valorAcabadosP);
                    }
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Mosaico de pasta")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosP);
                    }
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Mosaico terrazo")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosM);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo B")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Loseta ceramica de 20x 20cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Duela de madera laminada")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Marmol de 10x30cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("val5",""+seleccion_estructura);
                    Log.e("val5",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o mosaico terrazo en placas o colado en sitio")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo C")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Loseta ceramica  de hasta 30x30cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Duela o parquet de madera")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o marmol de hasta 30x30cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo D")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Loseta ceramica  mayor de 30x30cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1&&valorAcabadosP<450){
                        valorAcabadosP= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosP);
                    }
                   if(valorAcabadosP>450){
                        valorAcabadosP= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o marmol mayor de 30x30cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o cantera laminada")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);

                    if(valorAcabadosP>1&&valorAcabadosP<450){
                        valorAcabadosP= Double.valueOf(String.valueOf(6));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    if(valorAcabadosP>450&&valorAcabadosP<650){
                        valorAcabadosP= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    if(valorAcabadosP>650){
                        valorAcabadosP= Double.valueOf(String.valueOf(10));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo E")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Duela o parquet de maderas finas")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o loseta de marmol de 40x40cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Loseta e porcelanato de  hasta 30x30cm o mayores")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o loseta de marmol mayor de de 40x40cm")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(10));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
                if(seleccion_acabadosP.equals("Anteriores y/o Loseta e porcelanato de  hasta 50x50cm o mayores")){
                    seleccion_acabadosP= String.valueOf(1);
                    valor_acabadosP= Double.valueOf(seleccion_acabadosP);
                    valorAcabadosP=valor_acabadosP*valorConstruccion;
                    Log.e("val5",""+valorAcabadosP);
                    if(valorAcabadosP>1){
                        valorAcabadosP= Double.valueOf(String.valueOf(10));
                        Log.e("val5",""+valorAcabadosP);
                    }
                    Log.e("tipo",""+seleccion_estructura);
                    Log.e("tipo",""+position);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabados_fachadas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_fachadas=findViewById(R.id.acabadosFachadas);
                if (tipo_fachadas==null)
                {
                    tipo_fachadas=(TextView) view.findViewById(R.id.acabadosFachadas);
                }
                else
                {
                    tipo_fachadas=(TextView) view.findViewById(R.id.acabadosFachadas);
                }
                seleccion_fachadas =tipo_fachadas.getText().toString();
                Log.e("tipomat",""+seleccion_fachadas);
                if(seleccion_fachadas.equals("Sin acabados")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(0));
                        Log.e("val5",""+valorFachadas);
                    }
                }
                if(seleccion_fachadas.equals("Material aperante")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(3));
                        Log.e("val5",""+valorFachadas);
                    }
                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o pasta con pintura")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Pasta con aplicaciones de cantera y/o marmol")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Pasta con aplicaciones de cantera y/o ceramica")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Pasta con aplicaciones de cantera y/o losas inclinadas")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o pasta")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o placa de cantera")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o marmoles")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o losas inclinadas")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o piedrin")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Aplanado de mezcla y/o precolados de concreto")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(7));
                        Log.e("val5",""+valorFachadas);
                    }

                }

                if(seleccion_fachadas.equals("Anteriores y/o laminas de aluminio esmaltado")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Anteriores y/o fachada integral de cristal")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Anteriores y/o balcones")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Anteriores y/o terrazas")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Anteriores y/o balcones techados")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(23));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                if(seleccion_fachadas.equals("Anteriores y/o terrazas techadas")){
                    seleccion_fachadas= String.valueOf(1);
                    valor_fachadas= Double.valueOf(seleccion_fachadas);
                    valorFachadas=valor_fachadas*valorConstruccion;
                    Log.e("val5",""+valorFachadas);
                    if(valorFachadas>1){
                        valorFachadas= Double.valueOf(String.valueOf(23));
                        Log.e("val5",""+valorFachadas);
                    }

                }
                Log.e("tipo",""+valor_fachadas);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabados_ventaneria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_ventaneria=findViewById(R.id.acabadosVentaneria);
                if (tipo_ventaneria==null)
                {
                    tipo_ventaneria=(TextView) view.findViewById(R.id.acabadosVentaneria);
                }
                else
                {
                    tipo_ventaneria=(TextView) view.findViewById(R.id.acabadosVentaneria);
                }
                seleccion_ventaneria =tipo_ventaneria.getText().toString();
                Log.e("tipomat",""+seleccion_ventaneria);
                if(seleccion_ventaneria.equals("Madera y/o Fierro")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(1));
                        Log.e("val5",""+valorVentanerias);
                    }
                }
                if(seleccion_ventaneria.equals("Perfil de aluminio natural de 1'' y/o Perfil de fierro estructural 1''")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(2));
                        Log.e("val5",""+valorVentanerias);
                    }
                }
                if(seleccion_ventaneria.equals("Perfil de aluminio natural de 1'' y/o Perfil tubular de pared delgada")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(2));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Perfil de aluminio natural de 2'' y/o Perfil de fierro estructural")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Perfil de aluminio natural de 2'' y/o tubular de pared gruesa")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(4));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Perfil de aluminio anodizado o esmaltado  hasta de " +
                        "3''con canceles de piso a techo y cristal hasta de 6mm")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(5));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Perfil de aluminio anodizado o esmaltado  hasta de" +
                        " 4''con canceles de piso a techo y cristal hasta de 9mm")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(10));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Anteriores y/o cristal templado filtrasol")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Anteriores y/o cristal polarizado")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Anteriores y/o canceleria de pvc de doble cristal termico y acustico")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1&&valorVentanerias<651){
                        valorVentanerias= Double.valueOf(String.valueOf(13));
                        Log.e("val5",""+valorVentanerias);
                    }
                    if(valorVentanerias>651){
                        valorVentanerias= Double.valueOf(String.valueOf(16));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Anteriores y/o laminado inteligente inastillable")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(16));
                        Log.e("val5",""+valorVentanerias);
                    }

                }
                if(seleccion_ventaneria.equals("Anteriores y/o terrazas techadas")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(16));
                        Log.e("val5",""+valorVentanerias);
                    }

                }

                if(seleccion_ventaneria.equals("Anteriores y/o un solarium o mas de perfil de aluminio con cristales o policarbonatos")){
                    seleccion_ventaneria= String.valueOf(1);
                    valor_ventanerias= Double.valueOf(seleccion_ventaneria);
                    valorVentanerias=valor_ventanerias*valorConstruccion;
                    Log.e("val5",""+valorVentanerias);
                    if(valorVentanerias>1){
                        valorVentanerias= Double.valueOf(String.valueOf(16));
                        Log.e("val5",""+valorVentanerias);
                    }

                }


                Log.e("tipo",""+valorVentanerias);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabados_recubrimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_recubrimiento=findViewById(R.id.acabadosRecubrimiento);
                if (tipo_recubrimiento==null)
                {
                    tipo_recubrimiento=(TextView) view.findViewById(R.id.acabadosRecubrimiento);
                }
                else
                {
                    tipo_recubrimiento=(TextView) view.findViewById(R.id.acabadosRecubrimiento);
                }
                seleccion_recubrimiento =tipo_recubrimiento.getText().toString();
                Log.e("tipomat",""+seleccion_recubrimiento);
                if(seleccion_recubrimiento.equals("Sin recubrimientos")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorFachadas);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(0));
                        Log.e("val6",""+valorRecubrimiento);
                    }
                }
                if(seleccion_recubrimiento.equals("Mosaicos de 20x20cm y/o azulejos de 11x11cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(2));
                        Log.e("val6",""+valorRecubrimiento);
                    }
                }
                if(seleccion_recubrimiento.equals("Mosaicos de 20x20cm y/o loseta ceramica de 20x20cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(3));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o loseta ceramica de hasta 30x30cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(3));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o marmol de 10x30cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(3));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o loseta ceramica de mayor 30x30cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1&&valorRecubrimiento<251){
                        valorRecubrimiento= Double.valueOf(String.valueOf(9));
                        Log.e("val6",""+valorRecubrimiento);
                    }
                    if(valorRecubrimiento>251&&valorRecubrimiento<451){
                        valorRecubrimiento= Double.valueOf(String.valueOf(12));
                        Log.e("val6",""+valorRecubrimiento);
                    }
                    if(valorRecubrimiento>451){
                        valorRecubrimiento= Double.valueOf(String.valueOf(16));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o marmol de hasta 30x30cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(9));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o marmol mayor de  30x30cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1&&valorRecubrimiento<451){
                        valorRecubrimiento= Double.valueOf(String.valueOf(12));
                        Log.e("val6",""+valorRecubrimiento);
                    }
                    if(valorRecubrimiento>1&&valorRecubrimiento<451){
                        valorRecubrimiento= Double.valueOf(String.valueOf(16));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o granito en placas mayores  30x30cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(16));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o loseta ceramica mayor de  40x40cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1&&valorRecubrimiento<651){
                        valorRecubrimiento= Double.valueOf(String.valueOf(20));
                        Log.e("val6",""+valorRecubrimiento);
                    }


                }
                if(seleccion_recubrimiento.equals("Anteriores y/o marmol en placas mayores  90x90cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(20));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                if(seleccion_recubrimiento.equals("Anteriores y/o granito en placas  90x90cm")){
                    seleccion_recubrimiento= String.valueOf(1);
                    valor_recubrimiento= Double.valueOf(seleccion_recubrimiento);
                    valorRecubrimiento=valor_recubrimiento*valorConstruccion;
                    Log.e("val6",""+valorRecubrimiento);
                    if(valorRecubrimiento>1){
                        valorRecubrimiento= Double.valueOf(String.valueOf(20));
                        Log.e("val6",""+valorRecubrimiento);
                    }

                }
                Log.e("tipo",""+valor_recubrimiento);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        acabadosbanos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_banos=findViewById(R.id.acabadosBanos);
                if (tipo_banos==null)
                {
                    tipo_banos=(TextView) view.findViewById(R.id.acabadosBanos);
                }
                else
                {
                    tipo_banos=(TextView) view.findViewById(R.id.acabadosBanos);
                }
                seleccion_banos =tipo_banos.getText().toString();
                Log.e("tipomat",""+seleccion_banos);
                if(seleccion_banos.equals("W.C de barro sin conexion de agua corriente " +
                        "y/o letrina sin conexion de agua corriente")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1){
                        valorBanos= Double.valueOf(String.valueOf(5));
                        Log.e("val6",""+valorBanos);
                    }
                }
                if(seleccion_banos.equals("Muebles tipo 'A' economica")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1){
                        valorBanos= Double.valueOf(String.valueOf(8));
                        Log.e("val6",""+valorBanos);
                    }
                }
                if(seleccion_banos.equals("Muebles tipo 'B' mediana calidad")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1){
                        valorBanos= Double.valueOf(String.valueOf(12));
                        Log.e("val6",""+valorBanos);
                    }

                }
                if(seleccion_banos.equals("Muebles tipo 'C' buena calidad")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1){
                        valorBanos= Double.valueOf(String.valueOf(25));
                        Log.e("val6",""+valorBanos);
                    }

                }
                if(seleccion_banos.equals("Muebles tipo 'D' lujo")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1){
                        valorBanos= Double.valueOf(String.valueOf(34));
                        Log.e("val6",""+valorBanos);
                    }

                }
                if(seleccion_banos.equals("Muebles tipo 'E' super lujo")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1&&valorBanos<251){
                        valorBanos= Double.valueOf(String.valueOf(46));
                        Log.e("val6",""+valorBanos);
                    }

                }
                if(seleccion_banos.equals("Muebles tipo 'F' gran lujo")){
                    seleccion_banos= String.valueOf(1);
                    valor_banos= Double.valueOf(seleccion_banos);
                    valorBanos=valor_banos*valorConstruccion;
                    Log.e("val6",""+valorBanos);
                    if(valorBanos>1){
                        valorBanos= Double.valueOf(String.valueOf(56));
                        Log.e("val6",""+valorBanos);
                    }
                }
                Log.e("tipo",""+valor_banos);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sigPag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valor_acabados.setVisibility(View.GONE);
                valor_puntos.setVisibility(View.VISIBLE);
                Log.e("baños valor",""+valorBanos);
                Log.e("cadena total",""+valor_estruc+valorMateriales+valorPisos+valorCubiertas+valorAcabadosM+valorAcabadosP+valorFachadas+valorVentanerias+valorRecubrimiento+valorBanos);
                valor_total_matriz=valor_estruc+valorMateriales+valorPisos+valorCubiertas+valorAcabadosM+valorAcabadosP+valorFachadas+valorVentanerias+valorRecubrimiento+valorBanos;
                Log.e("VALOR MATRIZ",valor_total_matriz+"");

                puntos_matriz.setText(String.valueOf(valor_total_matriz));
                if(valor_total_matriz>0&&valor_total_matriz<38){
                    tipo_clase=Double.valueOf(String.valueOf(1));

                }
                if(valor_total_matriz>39&&valor_total_matriz<60){
                    tipo_clase=Double.valueOf(String.valueOf(2));
                }
                if(valor_total_matriz>61&&valor_total_matriz<85){
                    tipo_clase=Double.valueOf(String.valueOf(3));
                }
                if(valor_total_matriz>86&&valor_total_matriz<115){
                    tipo_clase=Double.valueOf(String.valueOf(4));
                }
                if(valor_total_matriz>116&&valor_total_matriz<145){
                    tipo_clase=Double.valueOf(String.valueOf(5));
                }
                if(valor_total_matriz>146&&valor_total_matriz<180){
                    tipo_clase=Double.valueOf(String.valueOf(6));
                }
                if(valor_total_matriz>181){
                    tipo_clase=Double.valueOf(String.valueOf(7));
                }
                clase.setText(String.valueOf(tipo_clase));
            }
        });
        cambiar_nivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cajaEditNiveles.setVisibility(View.GONE);
                cajaNiveles.setVisibility(View.VISIBLE);
                nivelesFinal=Double.parseDouble(niveles.getText().toString());
                niveles_total.setText(String.valueOf(nivelesFinal));

                valor_unitario_total=111.0;

                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==1){
                    valor_unitario_total=1365.93;
                }
                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==2){
                    valor_unitario_total=2081.46;
                }
                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==3){
                    valor_unitario_total=3370.78;
                }
                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==4){
                    valor_unitario_total=4541.11;
                }
                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==5){
                    valor_unitario_total=7517.45;
                }
                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==6){
                    valor_unitario_total=10673.50;
                }
                if(nivelesFinal>0&&nivelesFinal<3&&tipo_clase==7){
                    valor_unitario_total=12233.78;
                }


                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==1){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==2){
                    valor_unitario_total=2216.75;
                }
                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==3){
                    valor_unitario_total=3732.64;
                }
                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==4){
                    valor_unitario_total=5579.40;
                }
                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==5){
                    valor_unitario_total=7538.63;
                }
                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==6){
                    valor_unitario_total=12261.56;
                }
                if(nivelesFinal>2&&nivelesFinal<5&&tipo_clase==7){
                    valor_unitario_total=14242.54;
                }


                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==1){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==2){
                    valor_unitario_total=2493.85;
                }
                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==3){
                    valor_unitario_total=3724.50;
                }
                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==4){
                    valor_unitario_total=6818.19;
                }
                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==5){
                    valor_unitario_total=8335.69;
                }
                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==6){
                    valor_unitario_total=12989.05;
                }
                if(nivelesFinal>4&&nivelesFinal<10&&tipo_clase==7){
                    valor_unitario_total=14973.79;
                }


                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==1){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==2){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==3){
                    valor_unitario_total=3960.84;
                }
                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==4){
                    valor_unitario_total=7228.94;
                }
                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==5){
                    valor_unitario_total=9771.77;
                }
                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==6){
                    valor_unitario_total=14664.43;
                }
                if(nivelesFinal>9&&nivelesFinal<15&&tipo_clase==7){
                    valor_unitario_total=16435.01;
                }


                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==1){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==2){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==3){
                    valor_unitario_total=4490.58;
                }
                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==4){
                    valor_unitario_total=8200.41;
                }
                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==5){
                    valor_unitario_total=11078.95;
                }
                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==6){
                    valor_unitario_total=16831.01;
                }
                if(nivelesFinal>14&&nivelesFinal<20&&tipo_clase==7){
                    valor_unitario_total=19405.41;
                }


                if(nivelesFinal>20&&tipo_clase==1){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>20&&tipo_clase==2){
                    valor_unitario_total=0.0;
                }
                if(nivelesFinal>20&&tipo_clase==3){
                    valor_unitario_total=4567.20;
                }
                if(nivelesFinal>20&&tipo_clase==4){
                    valor_unitario_total=8337.34;
                }
                if(nivelesFinal>20&&tipo_clase==5){
                    valor_unitario_total=11264.75;
                }
                if(nivelesFinal>20&&tipo_clase==6){
                    valor_unitario_total=19842.08;
                }
                if(nivelesFinal>20&&tipo_clase==7){
                    valor_unitario_total=22475.44;
                }
                Log.e("valor unitario",""+valor_unitario_total);
                valor_unitario.setText("$"+String.valueOf(valor_unitario_total));
            }
        });
        calcularPrecioConstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valor_puntos.setVisibility(View.GONE);
                valor_m2.setVisibility(View.VISIBLE);
                precio_fisico_construccion=valorConstruccion*valor_unitario_total;
                valor_dinero_m2.setText("$"+String.valueOf(precio_fisico_construccion));
                valor_instalaciones=precio_fisico_construccion*0.8;
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        miLatLong();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("sirve","sirve");
                mapaid.setVisibility(View.GONE);
                val_tierra.setVisibility(View.VISIBLE);
                valTierra.setVisibility(View.VISIBLE);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        buscar_precio();
                        Log.e("tarea","y esto igual");
                    }
                });
                return false;
            }
        });
    }
    private void solicitarPermisoLocation() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISO_LOCATION);
        Log.e("va","va");
    }
    public void checarPermisos() {
        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            Intent irPermisos = new Intent(Mapa.this, ActivarPermisos.class);
            startActivity(irPermisos);
        }
    }
    private void miLatLong() {

        /** SE PIDEN PERMISOS DE LOCACIÓN PARA*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /** MANAGER DE LOCACIONES DE ANDROID*/
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 5, locationListener);
    }
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            agregarMarcadorUbicacion(latitud, longitud);
            direccion = darDireccion(this, latitud, longitud);
            LatLng coord = new LatLng(latitud,longitud);
            String[] direccion_fragmentada=direccion.split(",");
            for (int i=0;i<direccion_fragmentada.length;i++){
                Log.e("direccion_fragmentada",direccion_fragmentada[i]);
            }
            calle=direccion_fragmentada[0].toUpperCase();
            nombre_colonia=direccion_fragmentada[1].toUpperCase().trim();
            nombre_alcaldia=direccion_fragmentada[2].toUpperCase().trim();
            cp=direccion_fragmentada[3].toUpperCase();
            ciudad=direccion_fragmentada[4].toUpperCase();
            pais=direccion_fragmentada[5].toUpperCase();
            Log.e("alcaldia",nombre_alcaldia);
            Log.e("colonia",nombre_colonia);
            Log.e("cantidad",direccion_fragmentada.length+"");
            coordenadas =coord;
            puntoPartida.setText(direccion);
            //Toast.makeText(getApplicationContext(),"direccion: "+direccion,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"lat: "+latitud+"long:"+longitud,Toast.LENGTH_LONG).show();
            Context context = this;
            SharedPreferences preferencias = getSharedPreferences("Usuario", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("latitud", String.valueOf(latitud));
            editor.putString("longitud", String.valueOf(longitud));
            editor.putString("direccion", "" + direccion);
            editor.apply();
            if (latitud!=0){
                //segundoPlano = new SegundoPlano();
                //segundoPlano.execute();
            }
        }
    }
    public String darDireccion(Context ctx, double darLat, double darLong) {
        String fullDireccion = null;
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> direcciones = geocoder.getFromLocation(darLat, darLong, 1);
            if (direcciones.size() > 0) {
                Address direccion = direcciones.get(0);
                fullDireccion = direccion.getAddressLine(0);
                String ciudad = direccion.getLocality();
                String pueblo = direccion.getCountryName();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fullDireccion;
    }
    public Bitmap resizeBitmap(String drawableName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
    private void agregarMarcadorUbicacion(double latitud, double longitud) {

        latLong = new LatLng(latitud, longitud);
        if (marker != null) {
                marker.remove();
                marker = mMap.addMarker(new MarkerOptions()
                    .position(latLong)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("fondo", 70, 70))));
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latLong, 18);
            mMap.moveCamera(miUbicacion);
        }
        else{
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLong)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("fondo", 70, 70))));
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latLong, 18);
            mMap.animateCamera(miUbicacion);
        }

        marker.setZIndex(0);
    }
    LocationListener locationListener = new LocationListener() {
        //Cuando la ubicación cambia
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
            Location locationA = new Location("Primera");
            latUpdate = location.getLatitude();
            longUpdate = location.getLongitude();
            locationA.setLatitude(latUpdate);
            locationA.setLongitude(longUpdate);
            //Location locationB = new Location("point B");
            //String latitudX = sharedPreferences.getString("latitud", "no");
            //String longitudX = sharedPreferences.getString("longitud", "no");
            /*if (!latitudX.equals("no") && !longitudX.equals("no")) {
                locationB.setLatitude(Double.parseDouble(latitudX));
                locationB.setLongitude(Double.parseDouble(longitudX));
                float distance = locationA.distanceTo(locationB);
                if (distance >= 3)//era 10 antes
                {
                    mMap.clear();
                    miLatLong();
                }
                //strLat = datosViaje.getString("lat"," ");
                //strLong = datosViaje.getString("lng"," ");
                //strEmpresa = datosViaje.getString("empresa"," ");
                //strEstado = datosViaje.getString("estado"," ");
                if (strEstado.equals("destino")){
                    Location locationC = new Location("point B");
                    locationC.setLatitude(Double.parseDouble(strLat));
                    locationC.setLongitude(Double.parseDouble(strLong));
                    float distanciaDestino = locationC.distanceTo(locationA);
                    Log.e("distancia",""+distanciaDestino);
                    if (distanciaDestino < 70)//distancia en metros a punto de partida
                    {
                        //Log.e("distancia","es menor a 1");
                        //if ()
                        capaDestino.setVisibility(View.GONE);
                        capaLlegada.setVisibility(View.VISIBLE);
                    }
                }
            }*/
        }
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISO_LOCATION : {
                Log.e("confirma","confirma");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Intent reiniciar=new Intent(Mapa.this,ActivarPermisos.class);
                    startActivity(reiniciar);
                } else {
                    Intent valcuacion=new Intent(Mapa.this,Mapa.class);
                    startActivity(valcuacion);

                    Log.e("AQUIMAMO","AQUIMAMO");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void buscar_precio()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"buscar_precio.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta2:",response + "");
                        try {
                            json_datos_catastrales=new JSONArray(response);
                            for (int i=0;i<json_datos_catastrales.length();i++){
                                JSONObject jsonObject = json_datos_catastrales.getJSONObject(i);
                                //Log.e("nombreMovies", String.valueOf(jsonObject));
                                String strNombrecolonia = jsonObject.getString("nombre_colonia");
                                String strNombrealcaldia = jsonObject.getString("nombre_alcaldia");
                                String strNumeroalcaldia=jsonObject.getString("alcaldia");
                                String strColoniacatastral=jsonObject.getString("colonia_catastral");
                                String strRegion=jsonObject.getString("region");
                                String strValor=jsonObject.getString("valor");
                                Double multi=Double.parseDouble(strValor);
                                Log.e("revisa",""+strValor);
                                valorTotalTerreno=strValor;
                                valTierra.setText("$"+strValor);
                                //val_total_tierra.setText("$"+multiplicacion);
                                Log.e("nombre_colonia",strNombrecolonia);
                                Log.e("nombre_alcaldia",strNombrealcaldia);
                                Log.e("alcaldia",strNumeroalcaldia);
                                Log.e("colonia_catastral",strColoniacatastral);
                                Log.e("region",strRegion);
                                Log.e("valor",strValor);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("respuesta1", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("nombre_colonia",nombre_colonia);
                map.put("nombre_alcaldia",nombre_alcaldia);
                return map;
            }
        };
        requestQueue.add(request);
    }
    public void setListaEstructura()
    {
        listaEstructura.clear();
        String coy[] = {"", "Muros de carga","M.D.C y columnas de concreto",
                "M.D.C y columnas de acero", "M.D.C y columnas mixtas"};
        for (int i=0; i<coy.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaEstructura.add(sched);
        }
    }
    public void setListaMateriales()
    {
        listaMateriales.clear();
        String coy1[] = {"Lamina","Madera", "Tabicon", "Block","Tabique","Sillar de adobe"};
        for (int i=0; i<coy1.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy1[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaMateriales.add(sched);
        }
    }
    public void setListaEntrepisos()
    {
        listaEntrepisos.clear();
        String coy2[] = {"", "Sin entrepisos","Con o sin losa de concreto",
                "Con/Sin L.C y/o losa aligerada", "Con/Sin L.C y/o de Madera","Con/Sin L.C y/o losa reticular"};
        for (int i=0; i<coy2.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy2[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaEntrepisos.add(sched);
        }
    }
    public void setListaCubiertas()
    {
        listaCubiertas.clear();
        String coy3[] = {"", "Lamina","Losa de concreto",
                "L.C y/o Aligerada", "L.C y/o Madera","L.C y/o Reticular"};
        for (int i=0; i<coy3.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy3[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaCubiertas.add(sched);
        }
    }
    public void setListaAcabadosMuros()
    {
        listaAcabadosMuros.clear();
        String coy4[] = {"", "No hay o muy escasos","Tabique y/o block aparente",
                "Anteriores y/o aplanado yeso con pintura","Anteriores y/o aplanado de mezcla con pintura",
                "Anteriores y/o pasta texturizada","Anteriores y/o papel tapiz",
                "Anteriores y/o pasta texturizada con color integral",
                "Anteriores y/o papel tapiz plastificado",
                "Anteriores y/o tapiz de tela",
                "Anteriores y/o cenefas de madera",
                "Anteriores y/o  pastas quimicas con mezclas especiales",
                "Anteriores y/o tapiz de tela importado",
                "Anteriores y/o estucos o frescos decorativos"};
        for (int i=0; i<coy4.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy4[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaAcabadosMuros.add(sched);
        }
    }
    public void setListaAcabadosPisos()
    {
        listaAcabadosPisos.clear();
        String coy5[] = {"", "Piso de tierra y/o Firme de mezcla",
                "Firme de concreto simple o pulido",
                "Firme de concreto simple o pulido y/o Loseta vinilica",
                "Firme de concreto simple o pulido y/o Linoleum",
                "Firme de concreto simple o pulido y/o Alfombra tipo A",
                "Anteriores y/o Mosaico de pasta",
                "Anteriores y/o Mosaico terrazo",
                "Anteriores y/o Alfombra tipo B",
                "Anteriores y/o Loseta ceramica de 20x 20cm",
                "Anteriores y/o Duela de madera laminada",
                "Anteriores y/o Marmol de 10x30cm",
                "Anteriores y/o mosaico terrazo en placas o colado en sitio",
                "Anteriores y/o Alfombra tipo C",
                "Anteriores y/o Loseta ceramica  de hasta 30x30cm",
                "Anteriores y/o Duela o parquet de madera",
                "Anteriores y/o marmol de hasta 30x30cm",
                "Anteriores y/o Alfombra tipo D",
                "Anteriores y/o Loseta ceramica  mayor de 30x30cm",
                "Anteriores y/o marmol mayor de 30x30cm",
                "Anteriores y/o cantera laminada",
                "Anteriores y/o Alfombra tipo E",
                "Anteriores y/o Duela o parquet de maderas finas",
                "Anteriores y/o loseta de marmol de 40x40cm",
                "Anteriores y/o Loseta e porcelanato de  hasta 30x30cm o mayores",
                "Anteriores y/o loseta de marmol mayor de de 40x40cm",
                "Anteriores y/o Loseta e porcelanato de  hasta 50x50cm o mayores"};
        for (int i=0; i<coy5.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy5[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaAcabadosPisos.add(sched);
        }
    }

    public void setListaAcabadosFachadas()
    {
        listaAcabadosFachadas.clear();
        String coy6[] = {"", "Sin acabados","Material aperante",
                "Aplanado de mezcla y/o pasta con pintura",
                "Pasta con aplicaciones de cantera y/o marmol",
                "Pasta con aplicaciones de cantera y/o ceramica",
                "Pasta con aplicaciones de cantera y/o losas inclinadas",
                "Aplanado de mezcla y/o pasta",
                "Aplanado de mezcla y/o placa de cantera",
                "Aplanado de mezcla y/o marmoles",
                "Aplanado de mezcla y/o losas inclinadas",
                "Aplanado de mezcla y/o piedrin",
                "Aplanado de mezcla y/o precolados de concreto",
                "Anteriores y/o laminas de aluminio esmaltado",
                "Anteriores y/o fachada integral de cristal",
                "Anteriores y/o balcones",
                "Anteriores y/o terrazas",
                "Anteriores y/o balcones techados",
                "Anteriores y/o terrazas techadas"};
        for (int i=0; i<coy6.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy6[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaAcabadosFachadas.add(sched);
        }
    }
    public void setListaVentaneria()
    {
        listaAcabadosVentaneria.clear();
        String coy7[] = {"", "Madera y/o Fierro",
                "Perfil de aluminio natural de 1'' y/o Perfil de fierro estructural 1''",
                "Perfil de aluminio natural de 1'' y/o Perfil tubular de pared delgada",
                "Perfil de aluminio natural de 2'' y/o Perfil de fierro estructural",
                "Perfil de aluminio natural de 2'' y/o tubular de pared gruesa",
                "Perfil de aluminio anodizado o esmaltado  hasta de 3''con canceles de piso a techo y cristal hasta de 6mm",
                "Perfil de aluminio anodizado o esmaltado  hasta de 4''con canceles de piso a techo y cristal hasta de 9mm",
                "Anteriores y/o cristal templado filtrasol",
                "Anteriores y/o cristal polarizado",
                "Anteriores y/o canceleria de pvc de doble cristal termico y acustico",
                "Anteriores y/o laminado inteligente inastillable",
                "Anteriores y/o terrazas techadas",
                "Anteriores y/o un solarium o mas de perfil de aluminio con cristales o policarbonatos"};
        for (int i=0; i<coy7.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy7[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaAcabadosVentaneria.add(sched);
        }
    }
    public void setListaAcabadosRecubrimiento()
    {
        listaAcabadosRecubrimiento.clear();
        String coy8[] = {"", "Sin recubrimientos",
                "Mosaicos de 20x20cm y/o azulejos de 11x11cm",
                "Mosaicos de 20x20cm y/o loseta ceramica de 20x20cm",
                "Anteriores y/o loseta ceramica de hasta 30x30cm",
                "Anteriores y/o marmol de 10x30cm",
                "Anteriores y/o loseta ceramica de mayor 30x30cm",
                "Anteriores y/o marmol de hasta 30x30cm",
                "Anteriores y/o marmol mayor de  30x30cm",
                "Anteriores y/o granito en placas mayores  30x30cm",
                "Anteriores y/o loseta ceramica mayor de  40x40cm",
                "Anteriores y/o marmol en placas mayores  90x90cm",
                "Anteriores y/o granito en placas  90x90cm",};
        for (int i=0; i<coy8.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy8[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaAcabadosRecubrimiento.add(sched);
        }
    }
    public void setListaAcabadosBanos()
    {
        listaAcabadosBanos.clear();
            String coy9[] = {"", "W.C de barro sin conexion de agua corriente" +
                    " y/o letrina sin conexion de agua corriente",
                "Muebles tipo 'A' economica",
                "Muebles tipo 'B' mediana calidad",
                "Muebles tipo 'C' buena calidad",
                "Muebles tipo 'D' lujo",
                "Muebles tipo 'E' super lujo",
                "Muebles tipo 'F' gran lujo"};
        for (int i=0; i<coy9.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy9[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaAcabadosBanos.add(sched);
        }
    }
}