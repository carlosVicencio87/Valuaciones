package com.Valuaciones;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.maps.android.SphericalUtil;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int PERMISO_LOCATION=1;
    private LocationManager locationManager;
    private double latitud,longitud,latUpdate,longUpdate;
    private TextView puntoPartida,valTierra,area_total,val_total_tierra,area_total_construccion,niveles_total,puntos_matriz,clase,
            valor_unitario,valor_dinero_m2,instalaciones,años_construccion_final,valor_condepreciacion,valor_real,ingresar_val_form,borrar_linea,msj_mapa,aceptar_poligono,pts_estructura_tv,pts_materiales_muros_tv,pts_materiales_entrepisos,pts_materiales_cubiertas,pts_acabados_muros_tex,pts_acabados_pisos_tv,pts_acabados_fachadas_tv,pts_acabados_ventaneria_tv,pts_acabados_recubrimientos_tv,pts_acabados_banos_tv,valor_total_inmueble,valor_total_inmueble_again,rango_tex,límite_inferior_del_rango,porcentaje_limite_inferior,cuota_fija,valor_isai_1,valor_isaiIncluido,utilidad_fijada,valor_conUtilidad,valor_isai_recalculado;
    private EditText area,areaConstruccion,niveles,años_construccion,porcenta_utilidad_new;
    private ImageView area_aprobada,cambiar_area,cambiar_area_construccion,cambiar_nivel,cambiar_tiempo,marcar_utilidad,tomar_foto,foto_estructura,foto_mat_muros,foto_entrepisos,foto_cubiertas;
    private String direccion,pagina_actual_str;
    private Button cambiar_pag_1, cambiar_pag_2, cambiar_pag_3,calcularPrecioConstr,calcularIsai,finalizar_val,btn_calear_envio,btn_aceptar_envio;
    private String calle,numeroAlcaldia, nombre_colonia,nombre_alcaldia,colonia_catastral,valor,cp,ciudad,pais,pathTemp,nombreImagen,finalPropietario,propietario2;
    private Double areaConstruccionDbl,valor_muros,valor_materiales, ptsMaterialesMuro,valor_pisos, ptoEntrepisos,
            puntos_estructura,valor_estructura, ptosAcabadosMuros,valor_cubiertas, ptsCubiertas,valor_acabadosM, ptosAcabadosPisos,valor_acabadosP,
            ptosFachadas,valor_fachadas, ptosVentanerias,valor_ventanerias, ptosAcabadosRecubrimientos,valor_recubrimiento, ptosRecuBanos,valor_banos,
            nivelesDbl,valor_total_matriz,tipo_clase,valor_unitario_total,precio_fisico_construccion,valor_instalaciones,valor_años
            ,valor_depreciacion, valor_de_depreciacion,valor_con_depreciacion,valor_total_inmueble_dbl,total_terreno_dbl,limite_inferior_dbl,cuota_fija_dbl,porcentaje_exc_limite_dbl,valor_isai_1_dbl,valor_isaiIncluido_dbl,utilidad_fijada_dbl,valor_conUtilidad_dbl,valor_isai_recalculado_dbl;
    private LatLng coord,coordenadas,latLong;
    private Marker marker;
    private LinearLayout cajaEditararea,cajaAreaCons,cajaEditNiveles,caja_años,div_poligono,abrir_camara_estructura,abrir_camara_mat_muros,abrir_camara_entrepisos,abrir_camara_cubiertas;
    private ConstraintLayout mapaid,div_preview_camara;
    private ScrollView valor_construccion,val_tierra,valor_acabados,valor_puntos,valor_m2,scrolIsai;
    private Fragment map;
    private int check=0;
    private static String SERVIDOR_CONTROLADOR;
    private ExecutorService executorService;
    private JSONObject jsonObject;
    private JSONArray json_datos_catastrales;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String valorTotalTerreno,str_area_total,str_val_total_tierra,seleccion_estructura,seleccion_materiales,seleccion_pisos,
            seleccion_cubiertas,seleccion_acabadosM,seleccion_acabadosP,
            seleccion_fachadas,seleccion_ventaneria,seleccion_recubrimiento,seleccion_banos,rango_str;
    private Mapa activity;
    public ArrayList<SpinnerModel> listaEstructura = new ArrayList<>();
    private Spinner tipo_estructura,materiales_muros,entrepisos,cubiertas,
            acabados_muros,acabados_pisos,acabados_fachadas,acabados_ventaneria
            ,acabados_recubrimiento,acabadosbanos,descripcion_espacios_sp;
    public ArrayList<SpinnerModel> listaMateriales = new ArrayList<>();
    public ArrayList<SpinnerModel> listaEntrepisos = new ArrayList<>();
    public ArrayList<SpinnerModel> listaCubiertas = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosMuros = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosPisos = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosFachadas = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosVentaneria = new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosRecubrimiento= new ArrayList<>();
    public ArrayList<SpinnerModel> listaAcabadosBanos= new ArrayList<>();

    public ArrayList<SpinnerModel> listaDescripcionEspacio= new ArrayList<>();
    private AdapterDescripcionEspacios adapterDescripcionEspacios;
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
    private List<LatLng> polygonPoints = new ArrayList<>();
    private Polygon polygon;
    private CheckBox check_numero_area,check_dibujar_poligono;
    private GoogleMap.OnMapClickListener locationClickListener;
    private GoogleMap.OnMapClickListener drawPolygonClickListener;
    private List<Marker> markers = new ArrayList<>();

    private  ConstraintLayout div_alerta_final;
    double area_poligono;
    private int check_area=0;
    private  int check_area_contrs=0;
    private  int check_nivel=0;
    private  int check_utilidad=0;
    private PreviewView previewCamara;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    private File imagen,carpeta,filesDir,video,imagen2;
    private int CAMERA_PERMISSION_CODE=1000;
    private ImageView snImagen=null;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        startCamera(cameraFacing,snImagen);
                    }
                }
            }
    );

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
        cambiar_area=findViewById(R.id.cambiar_area);
        area=findViewById(R.id.area);
        puntoPartida=findViewById(R.id.puntoPartida);
        valTierra=findViewById(R.id.valTierra);
        val_tierra=findViewById(R.id.val_tierra);
        mapaid=findViewById(R.id.mapaid);
        SERVIDOR_CONTROLADOR = new Servidor().servidor;
        cajaEditararea=findViewById(R.id.cajaEditararea);
        cambiar_pag_1 =findViewById(R.id.sigCalculo);
        valor_construccion=findViewById(R.id.valor_construccion);
        cajaAreaCons=findViewById(R.id.cajaAreaCons);
        cambiar_area_construccion=findViewById(R.id.cambiar_area_construccion);
        area_total_construccion=findViewById(R.id.area_total_construccion);
        valor_acabados=findViewById(R.id.valor_acabados);
        tipo_estructura=findViewById(R.id.tipo_estructura);
        activity = this;
        materiales_muros=findViewById(R.id.materiales_muros);
        entrepisos=findViewById(R.id.entrepisos);
        cubiertas=findViewById(R.id.cubiertas);
        areaConstruccion=findViewById(R.id.areaConstruccion);
        area_total_construccion=findViewById(R.id.area_total_construccion);
        cambiar_pag_2 =findViewById(R.id.sig_Calculo);
        acabados_muros=findViewById(R.id.acabados_muros);
        acabados_pisos=findViewById(R.id.acabados_pisos);
        acabados_fachadas=findViewById(R.id.acabados_fachadas);
        acabados_ventaneria=findViewById(R.id.acabados_ventaneria);
        acabados_recubrimiento=findViewById(R.id.acabados_recubrimiento);
        descripcion_espacios_sp=findViewById(R.id.descripcion_espacios);
        acabadosbanos=findViewById(R.id.acabadosbanos);
        cambiar_pag_3=findViewById(R.id.sigPag);
        valor_puntos=findViewById(R.id.valor_puntos);
        cajaEditNiveles=findViewById(R.id.cajaEditNiveles);

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
        años_construccion=findViewById(R.id.años_construccion);
        caja_años=findViewById(R.id.caja_años);

        años_construccion_final=findViewById(R.id.años_construccion_final);

        cambiar_tiempo=findViewById(R.id.cambiar_tiempo);
        valor_condepreciacion=findViewById(R.id.valor_condepreciacion);
        valor_real=findViewById(R.id.valor_real);
        calcularIsai=findViewById(R.id.calcularIsai);
        ingresar_val_form=findViewById(R.id.ingresar_val_form);
        check_numero_area=findViewById(R.id.check_numero_area);
        check_dibujar_poligono=findViewById(R.id.check_dibujar_poligono);
        div_poligono=findViewById(R.id.div_poligono);
        borrar_linea=findViewById(R.id.borrar_linea);
        msj_mapa=findViewById(R.id.msj_mapa);
        aceptar_poligono=findViewById(R.id.aceptar_poligono);
        areaConstruccionDbl= Double.valueOf(0);
        pts_estructura_tv=findViewById(R.id.pts_estructura_tv);
        pts_materiales_muros_tv=findViewById(R.id.pts_materiales_muros_tv);
        pts_materiales_entrepisos=findViewById(R.id.pts_materiales_entrepisos);
        pts_materiales_cubiertas=findViewById(R.id.pts_materiales_cubiertas);
        pts_acabados_muros_tex=findViewById(R.id.pts_acabados_muros_tex);
        pts_acabados_pisos_tv=findViewById(R.id.pts_acabados_pisos_tv);
        pts_acabados_fachadas_tv=findViewById(R.id.pts_acabados_fachadas_tv);
        pts_acabados_ventaneria_tv=findViewById(R.id.pts_acabados_ventaneria_tv);
        pts_acabados_recubrimientos_tv=findViewById(R.id.pts_acabados_recubrimientos_tv);
        pts_acabados_banos_tv=findViewById(R.id.pts_acabados_banos_tv);
        valor_total_inmueble=findViewById(R.id.valor_total_inmueble);
        valor_total_inmueble_again=findViewById(R.id.valor_total_inmueble_again);
        scrolIsai=findViewById(R.id.scrolIsai);
        rango_tex=findViewById(R.id.rango_tex);
        límite_inferior_del_rango=findViewById(R.id.límite_inferior_del_rango);
        porcentaje_limite_inferior=findViewById(R.id.porcentaje_limite_inferior);
        cuota_fija=findViewById(R.id.cuota_fija);
        valor_isai_1=findViewById(R.id.valor_isai_1);
        valor_isaiIncluido=findViewById(R.id.valor_isaiIncluido);
        porcenta_utilidad_new=findViewById(R.id.porcenta_utilidad_new);
        utilidad_fijada=findViewById(R.id.utilidad_fijada);
        marcar_utilidad=findViewById(R.id.marcar_utilidad);
        valor_conUtilidad=findViewById(R.id.valor_conUtilidad);
        valor_isai_recalculado=findViewById(R.id.valor_isai_recalculado);
        div_alerta_final=findViewById(R.id.div_alerta_final);
        finalizar_val=findViewById(R.id.finalizar_val);
        btn_calear_envio=findViewById(R.id.btn_calear_envio);
        btn_aceptar_envio=findViewById(R.id.btn_aceptar_envio);
        abrir_camara_estructura=findViewById(R.id.abrir_camara_estructura);
        div_preview_camara=findViewById(R.id.div_preview_camara);
        previewCamara=findViewById(R.id.previewCamara);
        tomar_foto=findViewById(R.id.tomar_foto);
        foto_estructura=findViewById(R.id.foto_estructura);
        abrir_camara_mat_muros=findViewById(R.id.abrir_camara_mat_muros);
        foto_mat_muros=findViewById(R.id.foto_mat_muros);
        abrir_camara_entrepisos=findViewById(R.id.abrir_camara_entrepisos);
        foto_entrepisos=findViewById(R.id.foto_entrepisos);
        abrir_camara_cubiertas=findViewById(R.id.abrir_camara_cubiertas);
        foto_cubiertas=findViewById(R.id.foto_cubiertas);
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
        setListaDescripcionEspacios();
        pagina_actual_str="pagina_1";

        final int permisoLocacion = ContextCompat.checkSelfPermission(Mapa.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permisoLocacion!= PackageManager.PERMISSION_GRANTED) {
            solicitarPermisoLocation();

            Log.e("paso","paso");
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

        } else {
            // Los permisos ya se han otorgado, puedes usar la cámara
        }
        sharedPreferences=getSharedPreferences("datos_catastrales",this.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        executorService= Executors.newSingleThreadExecutor();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        ingresar_val_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapaid.setVisibility(View.GONE);
                val_tierra.setVisibility(View.VISIBLE);
                valTierra.setVisibility(View.VISIBLE);
                pagina_actual_str="pagina_2";

                executorService.execute(() -> {
                    buscar_precio();
                    Log.e("tarea", "y esto igual");
                });
            }
        });
        cambiar_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_area ==0){
                    if (!area.getText().toString().equals("")) {
// Formato para números con dos decimales y separación de miles con coma
                        DecimalFormat formatter = new DecimalFormat("#,###.00");
                        area_total.setText(area.getText().toString());
                        Double valor_1=Double.parseDouble(area.getText().toString());
                        valorTotalTerreno = valorTotalTerreno.replace(",", "");
                        // Convertir el String a Double
                        Double valor_2 = Double.parseDouble(valorTotalTerreno);
                         total_terreno_dbl = valor_1*valor_2;
                        Log.e("total_area",total_terreno_dbl+"");
                        String formattedTotal = formatter.format(total_terreno_dbl);
                        val_total_tierra.setText("$"+formattedTotal);
                        area_total.setVisibility(View.VISIBLE);
                        area.setVisibility(View.GONE);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(cambiar_area.getWindowToken(), 0);
                        cambiar_area.requestFocus();
                /*        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm2.showSoftInput(cambiar_area, InputMethodManager.SHOW_IMPLICIT);*/
                        check_area =1;
                        cambiar_area.setImageResource(R.drawable.lock);

                    } else {
                        Toast.makeText(getApplicationContext(), "El area del terreno es neccesario.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    area_total.setVisibility(View.GONE);
                    area.setVisibility(View.VISIBLE);
                    check_area =0;
                    cambiar_area.setImageResource(R.drawable.unlock);
                    area.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(area, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        cambiar_pag_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_area_total=area_total.getText().toString();
                str_val_total_tierra=val_total_tierra.getText().toString();
                pagina_actual_str="pagina_4";
                if(!valTierra.equals("")){
                    if(!str_area_total.equals("")){
                        if(!str_val_total_tierra.equals("")){
                            val_tierra.setVisibility(View.GONE);
                            valor_construccion.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "El area del terreno es neccesario.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "El area del terreno es neccesario.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "El valor de la  tierra es neccesario.", Toast.LENGTH_LONG).show();
                }

            }
        });


        cambiar_area_construccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_area_contrs ==0){

                    if (!areaConstruccion.getText().toString().equals("")) {

                        area_total_construccion.setText(areaConstruccion.getText().toString());
                        area_total_construccion.setVisibility(View.VISIBLE);
                        areaConstruccion.setVisibility(View.GONE);
                        areaConstruccionDbl = Double.parseDouble(areaConstruccion.getText().toString());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(cambiar_area_construccion.getWindowToken(), 0);
                        cambiar_area_construccion.requestFocus();
                        check_area_contrs =1;
                        cambiar_area_construccion.setImageResource(R.drawable.lock);

                    } else {
                        Toast.makeText(getApplicationContext(), "El area del terreno es neccesario.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    area_total_construccion.setVisibility(View.GONE);
                    areaConstruccion.setVisibility(View.VISIBLE);
                    check_area_contrs =0;
                    cambiar_area_construccion.setImageResource(R.drawable.unlock);
                    areaConstruccion.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(areaConstruccion, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        adapterTipoEstructura = new AdapterTipoEstructura(activity,R.layout.tipo_estructura,listaEstructura,getResources());
        tipo_estructura.setAdapter(adapterTipoEstructura);

        adapterDescripcionEspacios = new AdapterDescripcionEspacios(activity,R.layout.descripcion_espacios,listaDescripcionEspacio,getResources());
        descripcion_espacios_sp.setAdapter(adapterDescripcionEspacios);

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

        check_dibujar_poligono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_box_are();
                check_dibujar_poligono.setChecked(true);
                mapaid.setVisibility(View.VISIBLE);
                val_tierra.setVisibility(View.GONE);
                valTierra.setVisibility(View.GONE);
                ingresar_val_form.setVisibility(View.GONE);
                div_poligono.setVisibility(View.VISIBLE);
                pagina_actual_str="pagina_3";
                msj_mapa.setText("El calculo del area del poligno es un aproximado proporcionado por google maps");
                // Cambiar el listener de clic en el mapa para el dibujo del polígono
                mMap.setOnMapClickListener(drawPolygonClickListener);
            }
        });
        aceptar_poligono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valorTotalTerreno = valorTotalTerreno.replace(",", "");
                // Convertir el String a Double
                Double valor_2 = Double.parseDouble(valorTotalTerreno);
                Double total = area_poligono*valor_2;
                total_terreno_dbl=total;
                Log.e("area_poligono",area_poligono+"");
                Log.e("valorTotalTerreno",valorTotalTerreno+"");
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                Log.e("total_area",total_terreno_dbl+"");
                String formattedTotal = formatter.format(total_terreno_dbl);
                val_total_tierra.setText("$"+formattedTotal);
                mapaid.setVisibility(View.GONE);
                val_tierra.setVisibility(View.VISIBLE);
                valTierra.setVisibility(View.VISIBLE);
                ingresar_val_form.setVisibility(View.VISIBLE);
                div_poligono.setVisibility(View.GONE);

                check_area =1;
                area.setVisibility(View.GONE);
                cajaEditararea.setVisibility(View.VISIBLE);
                area_total.setVisibility(View.VISIBLE);
                cambiar_area.setVisibility(View.GONE);
                cambiar_area.setImageResource(R.drawable.lock);

                area_total.setText(area_poligono+ "m2");
            }
        });
        check_numero_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_box_are();
                check_numero_area.setChecked(true);
                area_total.setText("");
                cambiar_area.setVisibility(View.VISIBLE);
                cajaEditararea.setVisibility(View.VISIBLE);
                area_total.setVisibility(View.GONE);
                cambiar_area.setImageResource(R.drawable.unlock);

                area.setVisibility(View.VISIBLE);
                area.setText("");
                check_area =0;


            }
        });
        borrar_linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!polygonPoints.isEmpty()) {
                    // Eliminar el último punto del polígono
                    polygonPoints.remove(polygonPoints.size() - 1);
                    // Eliminar el último marcador del mapa y de la lista de marcadores
                    if (!markers.isEmpty()) {
                        markers.get(markers.size() - 1).remove();
                        markers.remove(markers.size() - 1);
                    }
                    // Redibujar el polígono
                    drawPolygon();
                }
            }
        });
        tipo_estructura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_estruc = findViewById(R.id.texto);
                if (tipo_estruc == null) {
                    tipo_estruc = (TextView) view.findViewById(R.id.texto);
                } else {
                    tipo_estruc = (TextView) view.findViewById(R.id.texto);
                }
                seleccion_estructura = tipo_estruc.getText().toString();

                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        parent.setSelection(0);
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.e("val1", "" + seleccion_estructura);
                    valor_estructura = Double.valueOf(1);
                    puntos_estructura = valor_estructura * areaConstruccionDbl;
                    Log.e("val1", "" + puntos_estructura);

                    if (puntos_estructura < 51) {
                        puntos_estructura = Double.valueOf(7);
                        Log.e("val1", "" + puntos_estructura);
                    } else if (puntos_estructura > 51 && puntos_estructura < 250) {
                        puntos_estructura = Double.valueOf(8);
                        Log.e("val1", "" + puntos_estructura);
                    } else if (puntos_estructura > 251 && puntos_estructura < 650) {
                        puntos_estructura = Double.valueOf(9);
                        Log.e("val1", "" + puntos_estructura);
                    } else if (puntos_estructura > 651) {
                        puntos_estructura = Double.valueOf(11);
                        Log.e("val1", "" + puntos_estructura);
                    }
                    pts_estructura_tv.setVisibility(View.VISIBLE);
                    pts_estructura_tv.setText("Puntos obtenidos por estructura "+puntos_estructura);

                }
                Log.e("tipo", "" + seleccion_estructura);
                Log.e("tipo", "" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        materiales_muros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tipo_materiales = findViewById(R.id.tipoMateriales);
                if (tipo_materiales == null) {
                    tipo_materiales = (TextView) view.findViewById(R.id.tipoMateriales);
                } else {
                    tipo_materiales = (TextView) view.findViewById(R.id.tipoMateriales);
                }
                seleccion_materiales = tipo_materiales.getText().toString();

                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }

                    Log.e("tipomat", "" + seleccion_materiales);
                    valor_materiales = Double.valueOf(1);
                    ptsMaterialesMuro = valor_materiales * areaConstruccionDbl;

                    if (seleccion_materiales.equals("Lamina")) {
                        Log.e("seleccion_materiales", "" + seleccion_materiales);
                        if (ptsMaterialesMuro > 1) {
                            ptsMaterialesMuro = Double.valueOf(8);
                            Log.e("val1", "" + ptsMaterialesMuro);
                        }
                    }
                    if (seleccion_materiales.equals("Madera")) {
                        Log.e("seleccion_materiales", "" + seleccion_materiales);
                        if (ptsMaterialesMuro > 1) {
                            ptsMaterialesMuro = Double.valueOf(8);
                            Log.e("val2", "" + ptsMaterialesMuro);
                        }
                    }
                    if (seleccion_materiales.equals("Tabicon")) {
                        Log.e("seleccion_materiales", "" + seleccion_materiales);
                        if (ptsMaterialesMuro > 1 && ptsMaterialesMuro < 51) {
                            ptsMaterialesMuro = Double.valueOf(8);
                            Log.e("val3", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 51) {
                            ptsMaterialesMuro = Double.valueOf(11);
                            Log.e("val33", "" + ptsMaterialesMuro);
                        }
                    }
                    if (seleccion_materiales.equals("Block")) {
                        Log.e("seleccion_materiales", "" + seleccion_materiales);
                        if (ptsMaterialesMuro > 1 && ptsMaterialesMuro < 86) {
                            ptsMaterialesMuro = Double.valueOf(11);
                            Log.e("val4", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 86 && ptsMaterialesMuro < 151) {
                            ptsMaterialesMuro = Double.valueOf(18);
                            Log.e("val44", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 151 && ptsMaterialesMuro < 251) {
                            ptsMaterialesMuro = Double.valueOf(19);
                            Log.e("val444", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 251 && ptsMaterialesMuro < 451) {
                            ptsMaterialesMuro = Double.valueOf(20);
                            Log.e("val4444", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 451 && ptsMaterialesMuro < 651) {
                            ptsMaterialesMuro = Double.valueOf(21);
                            Log.e("val44444", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 651) {
                            ptsMaterialesMuro = Double.valueOf(22);
                            Log.e("val444444", "" + ptsMaterialesMuro);
                        }
                    }
                    if (seleccion_materiales.equals("Tabique")) {
                        Log.e("seleccion_materiales", "" + seleccion_materiales);
                        if (ptsMaterialesMuro > 1 && ptsMaterialesMuro < 86) {
                            ptsMaterialesMuro = Double.valueOf(11);
                            Log.e("val5", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 86 && ptsMaterialesMuro < 151) {
                            ptsMaterialesMuro = Double.valueOf(18);
                            Log.e("val55", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 151 && ptsMaterialesMuro < 251) {
                            ptsMaterialesMuro = Double.valueOf(19);
                            Log.e("val555", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 251 && ptsMaterialesMuro < 451) {
                            ptsMaterialesMuro = Double.valueOf(20);
                            Log.e("val5555", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 451 && ptsMaterialesMuro < 651) {
                            ptsMaterialesMuro = Double.valueOf(21);
                            Log.e("val55555", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 651) {
                            ptsMaterialesMuro = Double.valueOf(22);
                            Log.e("val555555", "" + ptsMaterialesMuro);
                        }
                    }
                    if (seleccion_materiales.equals("Sillar de adobe")) {
                        Log.e("seleccion_materiales", "" + seleccion_materiales);
                        if (ptsMaterialesMuro > 1 && ptsMaterialesMuro < 86) {
                            ptsMaterialesMuro = Double.valueOf(11);
                            Log.e("val6", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 86 && ptsMaterialesMuro < 151) {
                            ptsMaterialesMuro = Double.valueOf(18);
                            Log.e("val66", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 151 && ptsMaterialesMuro < 251) {
                            ptsMaterialesMuro = Double.valueOf(19);
                            Log.e("val666", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 251 && ptsMaterialesMuro < 451) {
                            ptsMaterialesMuro = Double.valueOf(20);
                            Log.e("val6666", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 451 && ptsMaterialesMuro < 651) {
                            ptsMaterialesMuro = Double.valueOf(21);
                            Log.e("val66666", "" + ptsMaterialesMuro);
                        }
                        if (ptsMaterialesMuro > 651) {
                            ptsMaterialesMuro = Double.valueOf(22);
                            Log.e("val666666", "" + ptsMaterialesMuro);
                        }
                    }
                    pts_materiales_muros_tv.setVisibility(View.VISIBLE);
                    pts_materiales_muros_tv.setText("Puntos obtenidos por materiales en muros "+ptsMaterialesMuro);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
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

                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }
                    valor_pisos = Double.valueOf(1);
                    ptoEntrepisos =valor_pisos* areaConstruccionDbl;
                    Log.e("tipomat",""+seleccion_pisos);
                    if(seleccion_pisos.equals("Sin entrepisos")){

                        Log.e("val3",""+ ptsMaterialesMuro);
                        if(ptoEntrepisos >1){
                            ptoEntrepisos = Double.valueOf(String.valueOf(1));
                            Log.e("val3",""+ ptsMaterialesMuro);
                        }
                    }

                    if(seleccion_pisos.equals("Con o sin losa de concreto")){
                        Log.e("val3",""+ ptoEntrepisos);
                        if(ptoEntrepisos >1&& ptoEntrepisos <86){
                            ptoEntrepisos = Double.valueOf(String.valueOf(4));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >86&& ptoEntrepisos <151){
                            ptoEntrepisos = Double.valueOf(String.valueOf(5));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >151&& ptoEntrepisos <251){
                            ptoEntrepisos = Double.valueOf(String.valueOf(7));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >251&& ptoEntrepisos <451){
                            ptoEntrepisos = Double.valueOf(String.valueOf(8));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >451&& ptoEntrepisos <651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(9));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(10));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                    }
                    if(seleccion_pisos.equals("Con/Sin L.C y/o losa aligerada")){
                        Log.e("val3",""+ ptoEntrepisos);
                        if(ptoEntrepisos >1&& ptoEntrepisos <86){
                            ptoEntrepisos = Double.valueOf(String.valueOf(4));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >86&& ptoEntrepisos <151){
                            ptoEntrepisos = Double.valueOf(String.valueOf(5));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >151&& ptoEntrepisos <251){
                            ptoEntrepisos = Double.valueOf(String.valueOf(7));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >251&& ptoEntrepisos <451){
                            ptoEntrepisos = Double.valueOf(String.valueOf(8));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >451&& ptoEntrepisos <651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(9));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(10));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                    }
                    if(seleccion_pisos.equals("Con/Sin L.C y/o de Madera")){
                        Log.e("val3",""+ ptoEntrepisos);
                        if(ptoEntrepisos >1&& ptoEntrepisos <86){
                            ptoEntrepisos = Double.valueOf(String.valueOf(4));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >86&& ptoEntrepisos <151){
                            ptoEntrepisos = Double.valueOf(String.valueOf(5));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >151&& ptoEntrepisos <251){
                            ptoEntrepisos = Double.valueOf(String.valueOf(7));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >251&& ptoEntrepisos <451){
                            ptoEntrepisos = Double.valueOf(String.valueOf(8));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >451&& ptoEntrepisos <651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(9));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(10));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                    }
                    if(seleccion_pisos.equals("Con/Sin L.C y/o losa reticular")){
                        Log.e("val3",""+ ptsMaterialesMuro);
                        if(ptoEntrepisos >1&& ptoEntrepisos <86){
                            ptoEntrepisos = Double.valueOf(String.valueOf(4));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >86&& ptoEntrepisos <151){
                            ptoEntrepisos = Double.valueOf(String.valueOf(5));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >151&& ptoEntrepisos <251){
                            ptoEntrepisos = Double.valueOf(String.valueOf(7));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >251&& ptoEntrepisos <451){
                            ptoEntrepisos = Double.valueOf(String.valueOf(8));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                        if(ptoEntrepisos >451&& ptoEntrepisos <651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(9));
                            Log.e("val3",""+ ptsMaterialesMuro);
                        }
                        if(ptoEntrepisos >651){
                            ptoEntrepisos = Double.valueOf(String.valueOf(10));
                            Log.e("val3",""+ ptoEntrepisos);
                        }
                    }
                    pts_materiales_entrepisos.setVisibility(View.VISIBLE);
                    pts_materiales_entrepisos.setText("Puntos obtenidos por entrepisos "+ptoEntrepisos);
                }

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

                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }
                    Log.e("tipomat",""+seleccion_cubiertas);
                    valor_cubiertas = Double.valueOf(1);
                    ptsCubiertas =valor_cubiertas* areaConstruccionDbl;
                    if(seleccion_cubiertas.equals("Lamina")){
                        Log.e("val4",""+ ptsCubiertas);
                        if(ptsCubiertas >1){
                            ptsCubiertas = Double.valueOf(String.valueOf(4));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                    }
                    if(seleccion_cubiertas.equals("Losa de concreto")){

                        Log.e("val4",""+ ptsCubiertas);
                        if(ptsCubiertas >1&& ptsCubiertas <86){
                            ptsCubiertas = Double.valueOf(String.valueOf(5));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >86&& ptsCubiertas <151){
                            ptsCubiertas = Double.valueOf(String.valueOf(6));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >151&& ptsCubiertas <251){
                            ptsCubiertas = Double.valueOf(String.valueOf(9));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >251&& ptsCubiertas <451){
                            ptsCubiertas = Double.valueOf(String.valueOf(13));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >451&& ptsCubiertas <651){
                            ptsCubiertas = Double.valueOf(String.valueOf(14));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >651){
                            ptsCubiertas = Double.valueOf(String.valueOf(16));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                    }
                    if(seleccion_cubiertas.equals("L.C y/o Aligerada")){

                        Log.e("val4",""+ ptsCubiertas);
                        if(ptsCubiertas >1&& ptsCubiertas <86){
                            ptsCubiertas = Double.valueOf(String.valueOf(5));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >86&& ptsCubiertas <151){
                            ptsCubiertas = Double.valueOf(String.valueOf(6));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >151&& ptsCubiertas <251){
                            ptsCubiertas = Double.valueOf(String.valueOf(9));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >251&& ptsCubiertas <451){
                            ptsCubiertas = Double.valueOf(String.valueOf(13));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >451&& ptsCubiertas <651){
                            ptsCubiertas = Double.valueOf(String.valueOf(14));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >651){
                            ptsCubiertas = Double.valueOf(String.valueOf(15));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                    }
                    if(seleccion_cubiertas.equals("L.C y/o Madera")){
                        Log.e("val4",""+ ptsCubiertas);
                        if(ptsCubiertas >1&& ptsCubiertas <86){
                            ptsCubiertas = Double.valueOf(String.valueOf(5));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >86&& ptsCubiertas <151){
                            ptsCubiertas = Double.valueOf(String.valueOf(6));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >151&& ptsCubiertas <251){
                            ptsCubiertas = Double.valueOf(String.valueOf(9));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >251&& ptsCubiertas <451){
                            ptsCubiertas = Double.valueOf(String.valueOf(13));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >451&& ptsCubiertas <651){
                            ptsCubiertas = Double.valueOf(String.valueOf(14));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >651){
                            ptsCubiertas = Double.valueOf(String.valueOf(16));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                    }
                    if(seleccion_cubiertas.equals("L.C y/o Reticular")){

                        Log.e("val4",""+ ptsCubiertas);
                        if(ptsCubiertas >1&& ptsCubiertas <86){
                            ptsCubiertas = Double.valueOf(String.valueOf(5));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >86&& ptsCubiertas <151){
                            ptsCubiertas = Double.valueOf(String.valueOf(6));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >151&& ptsCubiertas <251){
                            ptsCubiertas = Double.valueOf(String.valueOf(9));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >251&& ptsCubiertas <451){
                            ptsCubiertas = Double.valueOf(String.valueOf(13));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >451&& ptsCubiertas <651){
                            ptsCubiertas = Double.valueOf(String.valueOf(14));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                        if(ptsCubiertas >651){
                            ptsCubiertas = Double.valueOf(String.valueOf(16));
                            Log.e("val4",""+ ptsCubiertas);
                        }
                    }
                    pts_materiales_cubiertas.setVisibility(View.VISIBLE);
                    pts_materiales_cubiertas.setText("Puntos obtenidos por entrepisos "+ptsCubiertas);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cambiar_pag_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areaConstruccionDbl != 0) {
                    if(!seleccion_estructura.equals("Tipo de estructura")){
                        if(!seleccion_materiales.equals("Materiales de Muros")){
                            if(!seleccion_pisos.equals("Entrepisos")){
                                if(!seleccion_cubiertas.equals("Cubiertas")){
                                    pagina_actual_str="pagina_5";
                                    valor_construccion.setVisibility(View.GONE);
                                    valor_acabados.setVisibility(View.VISIBLE);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Seleccione un tipo de cubierta de la lista.", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Seleccione un tipo de entrepisos de la lista.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Seleccione una tipo de material para muros de la lista.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Seleccione un tipo de estructura de la lista.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese un valor en el area", Toast.LENGTH_LONG).show();
                }


            }
        });
       acabados_muros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
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

                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }
                    valor_acabadosM= Double.valueOf(1);
                    ptosAcabadosMuros =valor_acabadosM* areaConstruccionDbl;
                    Log.e("seleccion_acabadosM",""+seleccion_acabadosM);
                    if(seleccion_acabadosM.equals("No hay o muy escasos")){

                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(0));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Tabique y/o block aparente")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o aplanado yeso con pintura")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }

                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o aplanado de mezcla con pintura")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }

                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o pasta texturizada")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(6));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o papel tapiz")){
                        Log.e("val5",""+ ptosAcabadosMuros);
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(6));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o pasta texturizada con color integral")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(8));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }

                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o papel tapiz plastificado")){
                        Log.e("val5",""+ ptosAcabadosMuros);
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(8));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }

                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o tapiz de tela")){

                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(9));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o cenefas de madera")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(9));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o  pastas quimicas con mezclas especiales")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(12));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }
                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o tapiz de tela importado")){
                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(12));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }

                    }
                    if(seleccion_acabadosM.equals("Anteriores y/o estucos o frescos decorativos")){

                        if(ptosAcabadosMuros >1){
                            ptosAcabadosMuros = Double.valueOf(String.valueOf(12));
                            Log.e("val5",""+ ptosAcabadosMuros);
                        }

                    }

                    pts_acabados_muros_tex.setVisibility(View.VISIBLE);
                    pts_acabados_muros_tex.setText("Puntos obtenidos por acabados de muros "+ptosAcabadosMuros);
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

                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }
                    valor_acabadosP= Double.valueOf(1);
                    ptosAcabadosPisos =valor_acabadosP* areaConstruccionDbl;
                    Log.e("tipomat",""+seleccion_acabadosP);
                    if(seleccion_acabadosP.equals("Piso de tierra y/o Firme de mezcla")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(0));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }

                    if(seleccion_acabadosP.equals("Firme de concreto simple o pulido")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(2));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }

                    }
                    if(seleccion_acabadosP.equals("Firme de concreto simple o pulido y/o Loseta vinilica")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(2));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }

                    }
                    if(seleccion_acabadosP.equals("Firme de concreto simple o pulido y/o Linoleum")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(2));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }

                    }
                    if(seleccion_acabadosP.equals("Firme de concreto simple o pulido y/o Alfombra tipo A")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(2));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Mosaico de pasta")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Mosaico terrazo")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo B")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Loseta ceramica de 20x 20cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Duela de madera laminada")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Marmol de 10x30cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o mosaico terrazo en placas o colado en sitio")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo C")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Loseta ceramica  de hasta 30x30cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Duela o parquet de madera")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o marmol de hasta 30x30cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo D")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(6));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Loseta ceramica  mayor de 30x30cm")){
                        if(ptosAcabadosPisos >1&& ptosAcabadosPisos <450){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(6));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                        if(ptosAcabadosPisos >450){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o marmol mayor de 30x30cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(6));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o cantera laminada")){

                        if(ptosAcabadosPisos >1&& ptosAcabadosPisos <450){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(6));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                        if(ptosAcabadosPisos >450&& ptosAcabadosPisos <650){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                        if(ptosAcabadosPisos >650){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(10));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Alfombra tipo E")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Duela o parquet de maderas finas")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }

                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o loseta de marmol de 40x40cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Loseta e porcelanato de  hasta 30x30cm o mayores")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o loseta de marmol mayor de de 40x40cm")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(10));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }
                    }
                    if(seleccion_acabadosP.equals("Anteriores y/o Loseta e porcelanato de  hasta 50x50cm o mayores")){
                        if(ptosAcabadosPisos >1){
                            ptosAcabadosPisos = Double.valueOf(String.valueOf(10));
                            Log.e("val5",""+ ptosAcabadosPisos);
                        }

                    }
                    pts_acabados_pisos_tv.setVisibility(View.VISIBLE);
                    pts_acabados_pisos_tv.setText("Puntos obtenidos por acabados de pisos "+ptosAcabadosPisos);
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
                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }

                    valor_fachadas= Double.valueOf(1);
                    ptosFachadas =valor_fachadas* areaConstruccionDbl;
                    Log.e("tipomat",""+seleccion_fachadas);
                    if(seleccion_fachadas.equals("Sin acabados")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(0));
                            Log.e("val5",""+ ptosFachadas);
                        }
                    }
                    if(seleccion_fachadas.equals("Material aperante")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(3));
                            Log.e("val5",""+ ptosFachadas);
                        }
                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o pasta con pintura")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Pasta con aplicaciones de cantera y/o marmol")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Pasta con aplicaciones de cantera y/o ceramica")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Pasta con aplicaciones de cantera y/o losas inclinadas")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o pasta")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o placa de cantera")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o marmoles")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o losas inclinadas")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o piedrin")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Aplanado de mezcla y/o precolados de concreto")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(7));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }

                    if(seleccion_fachadas.equals("Anteriores y/o laminas de aluminio esmaltado")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Anteriores y/o fachada integral de cristal")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Anteriores y/o balcones")){

                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Anteriores y/o terrazas")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Anteriores y/o balcones techados")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(23));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    if(seleccion_fachadas.equals("Anteriores y/o terrazas techadas")){
                        if(ptosFachadas >1){
                            ptosFachadas = Double.valueOf(String.valueOf(23));
                            Log.e("val5",""+ ptosFachadas);
                        }

                    }
                    pts_acabados_fachadas_tv.setVisibility(View.VISIBLE);
                    pts_acabados_fachadas_tv.setText("Puntos obtenidos por fachadas "+ptosFachadas);

                }


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
                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }

                    valor_ventanerias= Double.valueOf(1);
                    ptosVentanerias =valor_ventanerias* areaConstruccionDbl;
                    Log.e("tipomat",""+seleccion_ventaneria);
                    if(seleccion_ventaneria.equals("Madera y/o Fierro")){

                        Log.e("val5",""+ ptosVentanerias);
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(1));
                            Log.e("val5",""+ ptosVentanerias);
                        }
                    }
                    if(seleccion_ventaneria.equals("Perfil de aluminio natural de 1'' y/o Perfil de fierro estructural 1''")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(2));
                            Log.e("val5",""+ ptosVentanerias);
                        }
                    }
                    if(seleccion_ventaneria.equals("Perfil de aluminio natural de 1'' y/o Perfil tubular de pared delgada")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(2));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Perfil de aluminio natural de 2'' y/o Perfil de fierro estructural")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Perfil de aluminio natural de 2'' y/o tubular de pared gruesa")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(4));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Perfil de aluminio anodizado o esmaltado  hasta de " +
                            "3''con canceles de piso a techo y cristal hasta de 6mm")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(5));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Perfil de aluminio anodizado o esmaltado  hasta de" +
                            " 4''con canceles de piso a techo y cristal hasta de 9mm")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(10));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Anteriores y/o cristal templado filtrasol")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Anteriores y/o cristal polarizado")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Anteriores y/o canceleria de pvc de doble cristal termico y acustico")){
                        if(ptosVentanerias >1&& ptosVentanerias <651){
                            ptosVentanerias = Double.valueOf(String.valueOf(13));
                            Log.e("val5",""+ ptosVentanerias);
                        }
                        if(ptosVentanerias >651){
                            ptosVentanerias = Double.valueOf(String.valueOf(16));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Anteriores y/o laminado inteligente inastillable")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(16));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    if(seleccion_ventaneria.equals("Anteriores y/o terrazas techadas")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(16));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }

                    if(seleccion_ventaneria.equals("Anteriores y/o un solarium o mas de perfil de aluminio con cristales o policarbonatos")){
                        if(ptosVentanerias >1){
                            ptosVentanerias = Double.valueOf(String.valueOf(16));
                            Log.e("val5",""+ ptosVentanerias);
                        }

                    }
                    pts_acabados_ventaneria_tv.setVisibility(View.VISIBLE);
                    pts_acabados_ventaneria_tv.setText("Puntos obtenidos por ventaneria "+ptosVentanerias);
                }
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
                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }
                    valor_recubrimiento= Double.valueOf(1);
                    ptosAcabadosRecubrimientos =valor_recubrimiento* areaConstruccionDbl;
                    Log.e("tipomat",""+seleccion_ventaneria);
                    if(seleccion_recubrimiento.equals("Sin recubrimientos")){

                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(0));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }
                    }
                    if(seleccion_recubrimiento.equals("Mosaicos de 20x20cm y/o azulejos de 11x11cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(2));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }
                    }
                    if(seleccion_recubrimiento.equals("Mosaicos de 20x20cm y/o loseta ceramica de 20x20cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(3));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o loseta ceramica de hasta 30x30cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(3));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o marmol de 10x30cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(3));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o loseta ceramica de mayor 30x30cm")){
                        if(ptosAcabadosRecubrimientos >1&& ptosAcabadosRecubrimientos <251){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(9));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }
                        if(ptosAcabadosRecubrimientos >251&& ptosAcabadosRecubrimientos <451){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(12));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }
                        if(ptosAcabadosRecubrimientos >451){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(16));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o marmol de hasta 30x30cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(9));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o marmol mayor de  30x30cm")){
                        if(ptosAcabadosRecubrimientos >1&& ptosAcabadosRecubrimientos <451){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(12));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }
                        if(ptosAcabadosRecubrimientos >451){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(16));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o granito en placas mayores  30x30cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(16));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o loseta ceramica mayor de  40x40cm")){
                        if(ptosAcabadosRecubrimientos >1&& ptosAcabadosRecubrimientos <651){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(20));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }


                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o marmol en placas mayores  90x90cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(20));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    if(seleccion_recubrimiento.equals("Anteriores y/o granito en placas  90x90cm")){
                        if(ptosAcabadosRecubrimientos >1){
                            ptosAcabadosRecubrimientos = Double.valueOf(String.valueOf(20));
                            Log.e("val6",""+ ptosAcabadosRecubrimientos);
                        }

                    }
                    pts_acabados_recubrimientos_tv.setVisibility(View.VISIBLE);
                    pts_acabados_recubrimientos_tv.setText("Puntos obtenidos por acabados de recubrimiento "+ptosAcabadosRecubrimientos);
                }

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
                if (position > 0) {
                    if (areaConstruccionDbl == 0) {
                        Toast.makeText(getApplicationContext(), "Es necesario ingresar el área de la construcción", Toast.LENGTH_SHORT).show();
                        // Set spinner position back to 0
                        parent.setSelection(0);
                        return;
                    }

                    valor_banos= Double.valueOf(1);
                    ptosRecuBanos =valor_banos* areaConstruccionDbl;
                    Log.e("tipomat",""+seleccion_banos);
                    if(seleccion_banos.equals("W.C de barro sin conexion de agua corriente " +
                            "y/o letrina sin conexion de agua corriente")){

                        if(ptosRecuBanos >1){
                            ptosRecuBanos = Double.valueOf(String.valueOf(5));
                            Log.e("val6",""+ ptosRecuBanos);
                        }
                    }
                    if(seleccion_banos.equals("Muebles tipo 'A' economica")){
                        if(ptosRecuBanos >1){
                            ptosRecuBanos = Double.valueOf(String.valueOf(8));
                            Log.e("val6",""+ ptosRecuBanos);
                        }
                    }
                    if(seleccion_banos.equals("Muebles tipo 'B' mediana calidad")){
                        if(ptosRecuBanos >1){
                            ptosRecuBanos = Double.valueOf(String.valueOf(12));
                            Log.e("val6",""+ ptosRecuBanos);
                        }

                    }
                    if(seleccion_banos.equals("Muebles tipo 'C' buena calidad")){
                        if(ptosRecuBanos >1){
                            ptosRecuBanos = Double.valueOf(String.valueOf(25));
                            Log.e("val6",""+ ptosRecuBanos);
                        }

                    }
                    if(seleccion_banos.equals("Muebles tipo 'D' lujo")){
                        if(ptosRecuBanos >1){
                            ptosRecuBanos = Double.valueOf(String.valueOf(34));
                            Log.e("val6",""+ ptosRecuBanos);
                        }

                    }
                    if(seleccion_banos.equals("Muebles tipo 'E' super lujo")){
                        if(ptosRecuBanos >1&& ptosRecuBanos <251){
                            ptosRecuBanos = Double.valueOf(String.valueOf(46));
                            Log.e("val6",""+ ptosRecuBanos);
                        }

                    }
                    if(seleccion_banos.equals("Muebles tipo 'F' gran lujo")){
                        if(ptosRecuBanos >1){
                            ptosRecuBanos = Double.valueOf(String.valueOf(56));
                            Log.e("val6",""+ ptosRecuBanos);
                        }
                    }
                    pts_acabados_banos_tv.setVisibility(View.VISIBLE);
                    pts_acabados_banos_tv.setText("Puntos obtenidos por acabados deL baño "+ptosRecuBanos);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cambiar_pag_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!seleccion_acabadosM.equals("Acabados Muros")){
                    if(!seleccion_acabadosP.equals("Acabados Pisos")){
                        if(!seleccion_fachadas.equals("Acabados fachadas")){
                            if(!seleccion_ventaneria.equals("Ventaneria")){
                                if(!seleccion_recubrimiento.equals("Recubrimientos de cocina y baño")){
                                    if(!seleccion_banos.equals("Recubrimientos de cocina y baño")){
                                        pagina_actual_str="pagina_6";
                                        valor_acabados.setVisibility(View.GONE);
                                        valor_puntos.setVisibility(View.VISIBLE);
                                        Log.e("valorFachadas",""+ ptosFachadas);
                                        Log.e("valorVentanerias",""+ ptosVentanerias);
                                        Log.e("valorRecubrimiento",""+ ptosAcabadosRecubrimientos);
                                        Log.e("baños valor",""+ ptosRecuBanos);

                                        valor_total_matriz= puntos_estructura + ptsMaterialesMuro + ptoEntrepisos + ptsCubiertas + ptosAcabadosMuros + ptosAcabadosPisos + ptosFachadas + ptosVentanerias + ptosAcabadosRecubrimientos + ptosRecuBanos;
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
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Selecciona una opcion de recubirmientos  de baño de la lista", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Selecciona una opcion de recubirmientos de la lista", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Selecciona una opcion de acabados de ventaneria", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Selecciona una opcion de acabados de fachadas", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Selecciona una opcion de acabados de pisos", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Selecciona una opcion de acabados de muros", Toast.LENGTH_LONG).show();
                }

            }
        });
        cambiar_nivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_nivel ==0){

                    if (!niveles.getText().toString().equals("")) {

                        niveles_total.setText(niveles.getText().toString());
                        niveles_total.setVisibility(View.VISIBLE);
                        niveles.setVisibility(View.GONE);
                        nivelesDbl =Double.parseDouble(niveles.getText().toString());
                        niveles_total.setText(niveles.getText().toString());
                        valor_unitario_total=111.0;
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==1){
                            valor_unitario_total=1440.78;
                        }
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==2){
                            valor_unitario_total=2195.55 ;
                        }
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==3){
                            valor_unitario_total= 3555.50 ;
                        }
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==4){
                            valor_unitario_total=4789.96 ;
                        }
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==5){
                            valor_unitario_total=7929.40;
                        }
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==6){
                            valor_unitario_total= 11258.41 ;
                        }
                        if(nivelesDbl >0&& nivelesDbl <3&&tipo_clase==7){
                            valor_unitario_total=12904.19 ;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==1){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==2){
                            valor_unitario_total=2338.23;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==3){
                            valor_unitario_total=3937.19 ;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==4){
                            valor_unitario_total=5885.15 ;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==5){
                            valor_unitario_total= 7951.75 ;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==6){
                            valor_unitario_total= 12933.50 ;
                        }
                        if(nivelesDbl >2&& nivelesDbl <6&&tipo_clase==7){
                            valor_unitario_total=15023.03 ;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==1){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==2){
                            valor_unitario_total=2630.51;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==3){
                            valor_unitario_total=  3928.60  ;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==4){
                            valor_unitario_total=7191.82;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==5){
                            valor_unitario_total= 8792.49  ;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==6){
                            valor_unitario_total= 13700.85 ;
                        }
                        if(nivelesDbl >5&& nivelesDbl <11&&tipo_clase==7){
                            valor_unitario_total=15794.35 ;
                        }
                        if(nivelesDbl >10&& nivelesDbl <16&&tipo_clase==1){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >10&& nivelesDbl <16&&tipo_clase==2)   {
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >10&& nivelesDbl <16&&tipo_clase==3){
                            valor_unitario_total= 4177.90 ;
                        }
                        if(nivelesDbl >10&& nivelesDbl <16&&tipo_clase==4){
                            valor_unitario_total=7625.08;
                        }
                        if(nivelesDbl >10&& nivelesDbl <16&&tipo_clase==5){
                            valor_unitario_total=10307.20;
                        }
                        if(nivelesDbl >10&& nivelesDbl <16&&tipo_clase==6){
                            valor_unitario_total= 15468.04 ;
                        }
                        if(nivelesDbl >9&& nivelesDbl <15&&tipo_clase==7){
                            valor_unitario_total= 17335.64 ;
                        }
                        if(nivelesDbl >15&& nivelesDbl <21&&tipo_clase==1){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >15&& nivelesDbl <21&&tipo_clase==2){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >15&& nivelesDbl <21&&tipo_clase==3){
                            valor_unitario_total= 4736.66 ;
                        }
                        if(nivelesDbl >14&& nivelesDbl <20&&tipo_clase==4){
                            valor_unitario_total=8649.79;
                        }
                        if(nivelesDbl >14&& nivelesDbl <20&&tipo_clase==5){
                            valor_unitario_total=11686.08;
                        }
                        if(nivelesDbl >14&& nivelesDbl <20&&tipo_clase==6){
                            valor_unitario_total= 17753.35 ;
                        }
                        if(nivelesDbl >14&& nivelesDbl <20&&tipo_clase==7){
                            valor_unitario_total=20468.82 ;
                        }
                        if(nivelesDbl >20&&tipo_clase==1){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >20&&tipo_clase==2){
                            valor_unitario_total=0.0;
                        }
                        if(nivelesDbl >20&&tipo_clase==3){
                            valor_unitario_total= 4817.48 ;
                        }
                        if(nivelesDbl >20&&tipo_clase==4){
                            valor_unitario_total=8794.22;
                        }
                        if(nivelesDbl >20&&tipo_clase==5){
                            valor_unitario_total=11882.06;
                        }
                        if(nivelesDbl >20&&tipo_clase==6){
                            valor_unitario_total= 20929.43 ;
                        }
                        if(nivelesDbl >20&&tipo_clase==7){
                            valor_unitario_total=23707.10 ;
                        }
                        Log.e("valor unitario",""+valor_unitario_total);
                        valor_unitario.setText("$"+valor_unitario_total+"/m3");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(cambiar_nivel.getWindowToken(), 0);
                        cambiar_nivel.requestFocus();
                        check_nivel =1;
                        cambiar_nivel.setImageResource(R.drawable.lock);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ingrese el numero de niveles.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    niveles_total.setVisibility(View.GONE);
                    niveles.setVisibility(View.VISIBLE);
                    check_nivel =0;
                    cambiar_nivel.setImageResource(R.drawable.unlock);
                    cambiar_nivel.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(cambiar_nivel, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        calcularPrecioConstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!niveles.getText().toString().equals("")) {
                    pagina_actual_str="pagina_7";
                    DecimalFormat formatter = new DecimalFormat("#,###.00");

                    valor_puntos.setVisibility(View.GONE);
                    valor_m2.setVisibility(View.VISIBLE);
                    Log.e("areaConstruccionDbl",""+areaConstruccionDbl);
                    Log.e("valor_unitario_total",""+valor_unitario_total);
                    precio_fisico_construccion= areaConstruccionDbl *valor_unitario_total;

                    Log.e("precio_fisico_construccion",""+precio_fisico_construccion);
                    Log.e("total_terreno_dbl",""+total_terreno_dbl);

                    valor_total_inmueble_dbl=total_terreno_dbl+precio_fisico_construccion;
                    String formattedTotalInmueble = formatter.format(valor_total_inmueble_dbl);
                    String formattedTotalPrecioFisico = formatter.format(precio_fisico_construccion);

                    valor_total_inmueble.setText("$"+formattedTotalInmueble+" M.N.");
                    Log.e("valFisico",""+precio_fisico_construccion);
                    valor_dinero_m2.setText("$"+formattedTotalPrecioFisico+" M.N.");
                    valor_instalaciones=precio_fisico_construccion*0.8;
                    String formattedTotalValorInstalacion = formatter.format(valor_instalaciones);
                    instalaciones.setText("$"+formattedTotalValorInstalacion+" M.N.");
                }else {
                    Toast.makeText(getApplicationContext(), "Ingrese el numero de niveles.", Toast.LENGTH_LONG).show();
                }

            }
        });
        cambiar_tiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_area ==0){

                    if (!años_construccion.getText().toString().equals("")) {
                        DecimalFormat formatter = new DecimalFormat("#,###.00");

                        años_construccion.setVisibility(View.GONE);
                        años_construccion_final.setVisibility(View.VISIBLE);
                        valor_años=Double.parseDouble(años_construccion.getText().toString());
                        años_construccion_final.setText(String.valueOf(valor_años));
                        valor_depreciacion=valor_años*0.008;
                        Log.e("porcentaje depreciacion",""+valor_depreciacion);
                        valor_de_depreciacion =precio_fisico_construccion*valor_depreciacion;
                        Log.e("$construcion",""+ valor_de_depreciacion);
                        String formattedDepreciacion = formatter.format(valor_de_depreciacion);

                        valor_condepreciacion.setText("$"+formattedDepreciacion+" M.N.");
                        valor_con_depreciacion=precio_fisico_construccion-valor_de_depreciacion;
                        String formattedValorConDepreciacion = formatter.format(valor_con_depreciacion);

                        valor_real.setText("$"+formattedValorConDepreciacion+" M.N.");

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(cambiar_tiempo.getWindowToken(), 0);
                        cambiar_tiempo.requestFocus();
                /*        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm2.showSoftInput(cambiar_area, InputMethodManager.SHOW_IMPLICIT);*/
                        check_area =1;
                        cambiar_tiempo.setImageResource(R.drawable.lock);

                    } else {
                        Toast.makeText(getApplicationContext(), "El area del terreno es neccesario.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    años_construccion_final.setVisibility(View.GONE);
                    años_construccion.setVisibility(View.VISIBLE);
                    check_area =0;
                    cambiar_tiempo.setImageResource(R.drawable.unlock);
                    años_construccion.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(años_construccion, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        calcularIsai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual_str="pagina_8";
                valor_total_inmueble_again.setText("$"+valor_total_inmueble_dbl);
                if(valor_total_inmueble_dbl>0.11&&valor_total_inmueble_dbl<171659.02){
                    rango_str="A";
                    limite_inferior_dbl=0.11;
                    cuota_fija_dbl=178.92;
                    porcentaje_exc_limite_dbl= 0.01693;

                }
                if(valor_total_inmueble_dbl> 171659.03&&valor_total_inmueble_dbl< 343317.53 ){
                    rango_str="B";
                    limite_inferior_dbl= 171659.03;
                    cuota_fija_dbl= 207.98 ;
                    porcentaje_exc_limite_dbl=0.03228;
                }
                if(valor_total_inmueble_dbl>  343317.54 &&valor_total_inmueble_dbl<  686636.36){
                    rango_str="C";
                    limite_inferior_dbl= 343317.54;
                    cuota_fija_dbl=  263.39;
                    porcentaje_exc_limite_dbl= 0.10089;
                }
                if(valor_total_inmueble_dbl> 686636.37 &&valor_total_inmueble_dbl<1029953.87 ){
                    rango_str="D";
                    limite_inferior_dbl= 686636.37;
                    cuota_fija_dbl=   609.76;
                    porcentaje_exc_limite_dbl= 0.12380;
                }
                if(valor_total_inmueble_dbl>1029953.88  &&valor_total_inmueble_dbl< 1373272.71 ){
                    rango_str="E";
                    limite_inferior_dbl= 1029953.88;
                    cuota_fija_dbl=    1034.79;
                    porcentaje_exc_limite_dbl=  0.12697;
                }
                if(valor_total_inmueble_dbl>1373272.72  &&valor_total_inmueble_dbl<1716590.23 ){
                    rango_str="F";
                    limite_inferior_dbl= 1373272.72;
                    cuota_fija_dbl=     1470.70 ;
                    porcentaje_exc_limite_dbl=  0.14757 ;
                }
                if(valor_total_inmueble_dbl>1716590.24  &&valor_total_inmueble_dbl<2059907.73 ){
                    rango_str="G";
                    limite_inferior_dbl= 1716590.24;
                    cuota_fija_dbl= 1977.34;
                    porcentaje_exc_limite_dbl= 0.15251;
                }
                if(valor_total_inmueble_dbl>2059907.74  &&valor_total_inmueble_dbl<2403226.59 ){
                    rango_str="H";
                    limite_inferior_dbl= 2059907.74;
                    cuota_fija_dbl=  2500.93 ;
                    porcentaje_exc_limite_dbl=0.16663;
                }
                if(valor_total_inmueble_dbl>2403226.60  &&valor_total_inmueble_dbl<2746544.10 ){
                    rango_str="I";
                    limite_inferior_dbl= 2403226.60;
                    cuota_fija_dbl=   3073.00  ;
                    porcentaje_exc_limite_dbl=0.17427;
                }
                if(valor_total_inmueble_dbl>2746544.11  &&valor_total_inmueble_dbl<3089862.96 ){
                    rango_str="J";
                    limite_inferior_dbl= 2746544.11;
                    cuota_fija_dbl= 3671.30;
                    porcentaje_exc_limite_dbl=0.17934;
                }
                if(valor_total_inmueble_dbl>3089862.97  &&valor_total_inmueble_dbl<3433180.45 ){
                    rango_str="K";
                    limite_inferior_dbl= 3089862.97;
                    cuota_fija_dbl=  4287.01 ;
                    porcentaje_exc_limite_dbl=0.18486;
                }
                if(valor_total_inmueble_dbl>3433180.46&&valor_total_inmueble_dbl<3776497.98 ){
                    rango_str="L";
                    limite_inferior_dbl= 3433180.46;
                    cuota_fija_dbl=   4921.67 ;
                    porcentaje_exc_limite_dbl=0.18988;
                }
                if(valor_total_inmueble_dbl>3776497.99&&valor_total_inmueble_dbl<4120143.77 ){
                    rango_str="M";
                    limite_inferior_dbl= 3776497.99;
                    cuota_fija_dbl=   5573.56 ;
                    porcentaje_exc_limite_dbl=0.20059;
                }
                if(valor_total_inmueble_dbl>4120143.78&&valor_total_inmueble_dbl<12360429.98){
                    rango_str="N";
                    limite_inferior_dbl= 4120143.78;
                    cuota_fija_dbl=   6262.88 ;
                    porcentaje_exc_limite_dbl=0.21660;
                }
                if(valor_total_inmueble_dbl>12360429.99&&valor_total_inmueble_dbl<26015421.90){
                    rango_str="O";
                    limite_inferior_dbl= 12360429.99;
                    cuota_fija_dbl=   24111.34 ;
                    porcentaje_exc_limite_dbl=0.21671;
                }
                if(valor_total_inmueble_dbl>26015421.91){
                    rango_str="P";
                    limite_inferior_dbl= 26015421.91;
                    cuota_fija_dbl=   53703.07 ;
                    porcentaje_exc_limite_dbl=0.22529;
                }
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                valor_isai_1_dbl=valor_total_inmueble_dbl-limite_inferior_dbl;
                Log.e("valor_isai_1_dbl", String.valueOf(valor_isai_1_dbl));
                valor_isai_1_dbl=valor_isai_1_dbl*porcentaje_exc_limite_dbl;
                Log.e("valor_isai_1_dbl", String.valueOf(valor_isai_1_dbl));
                valor_isai_1_dbl=valor_isai_1_dbl+cuota_fija_dbl;
                Log.e("valor_isai_1_dbl", String.valueOf(valor_isai_1_dbl));
                String formattedValorIsai = formatter.format(valor_isai_1_dbl);
                valor_isai_1.setText("$"+formattedValorIsai);
                valor_isaiIncluido_dbl=valor_isai_1_dbl+valor_total_inmueble_dbl;
                String formattedValorIsaiIncluido = formatter.format(valor_isaiIncluido_dbl);
                valor_isaiIncluido.setText("$"+formattedValorIsaiIncluido+" M.N.");
                rango_tex.setText(rango_str);
                String formattedValorLimiteInf = formatter.format(limite_inferior_dbl);
                límite_inferior_del_rango.setText("$"+formattedValorLimiteInf);
                porcentaje_limite_inferior.setText(""+porcentaje_exc_limite_dbl);
                cuota_fija.setText("$"+cuota_fija_dbl);
                valor_m2.setVisibility(View.GONE);
                scrolIsai.setVisibility(View.VISIBLE);
            }
        });
        marcar_utilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_utilidad ==0){

                    if (!porcenta_utilidad_new.getText().toString().equals("")) {
                        utilidad_fijada.setText(porcenta_utilidad_new.getText().toString());
                        utilidad_fijada.setVisibility(View.VISIBLE);
                        porcenta_utilidad_new.setVisibility(View.GONE);
                        utilidad_fijada_dbl= Double.valueOf(porcenta_utilidad_new.getText().toString());
                        DecimalFormat formatter = new DecimalFormat("#,###.00");

                        Log.e("utilidad_fijada_dbl", String.valueOf(utilidad_fijada_dbl));
                        utilidad_fijada_dbl=utilidad_fijada_dbl/100;
                        Log.e("utilidad_fijada_dbl", String.valueOf(utilidad_fijada_dbl));
                        utilidad_fijada_dbl=utilidad_fijada_dbl+1;
                        Log.e("utilidad_fijada_dbl", String.valueOf(utilidad_fijada_dbl));
                        valor_conUtilidad_dbl=utilidad_fijada_dbl*valor_isaiIncluido_dbl;
                        Log.e("utilidad_fijada_dbl", String.valueOf(utilidad_fijada_dbl));
                        String formattedValorConUtilidad = formatter.format(valor_conUtilidad_dbl);

                        valor_conUtilidad.setText("$"+formattedValorConUtilidad+" M.N.");
                        if(valor_conUtilidad_dbl>0.11&&valor_conUtilidad_dbl<171659.02){

                            limite_inferior_dbl=0.11;
                            cuota_fija_dbl=178.92;
                            porcentaje_exc_limite_dbl= 0.01693;

                        }
                        if(valor_conUtilidad_dbl> 171659.03&&valor_conUtilidad_dbl< 343317.53 ){
                            limite_inferior_dbl= 171659.03;
                            cuota_fija_dbl= 207.98 ;
                            porcentaje_exc_limite_dbl=0.03228;
                        }
                        if(valor_conUtilidad_dbl>  343317.54 &&valor_conUtilidad_dbl<  686636.36){
                            limite_inferior_dbl= 343317.54;
                            cuota_fija_dbl=  263.39;
                            porcentaje_exc_limite_dbl= 0.10089;
                        }
                        if(valor_conUtilidad_dbl> 686636.37 &&valor_conUtilidad_dbl<1029953.87 ){
                            limite_inferior_dbl= 686636.37;
                            cuota_fija_dbl=   609.76;
                            porcentaje_exc_limite_dbl= 0.12380;
                        }
                        if(valor_conUtilidad_dbl>1029953.88  &&valor_conUtilidad_dbl< 1373272.71 ){
                            limite_inferior_dbl= 1029953.88;
                            cuota_fija_dbl=    1034.79;
                            porcentaje_exc_limite_dbl=  0.12697;
                        }
                        if(valor_conUtilidad_dbl>1373272.72  &&valor_conUtilidad_dbl<1716590.23 ){
                            limite_inferior_dbl= 1373272.72;
                            cuota_fija_dbl=     1470.70 ;
                            porcentaje_exc_limite_dbl=  0.14757 ;
                        }
                        if(valor_conUtilidad_dbl>1716590.24  &&valor_conUtilidad_dbl<2059907.73 ){
                            limite_inferior_dbl= 1716590.24;
                            cuota_fija_dbl= 1977.34;
                            porcentaje_exc_limite_dbl= 0.15251;
                        }
                        if(valor_conUtilidad_dbl>2059907.74  &&valor_conUtilidad_dbl<2403226.59 ){
                            limite_inferior_dbl= 2059907.74;
                            cuota_fija_dbl=  2500.93 ;
                            porcentaje_exc_limite_dbl=0.16663;
                        }
                        if(valor_conUtilidad_dbl>2403226.60  &&valor_conUtilidad_dbl<2746544.10 ){
                            limite_inferior_dbl= 2403226.60;
                            cuota_fija_dbl=   3073.00  ;
                            porcentaje_exc_limite_dbl=0.17427;
                        }
                        if(valor_conUtilidad_dbl>2746544.11  &&valor_conUtilidad_dbl<3089862.96 ){
                            limite_inferior_dbl= 2746544.11;
                            cuota_fija_dbl= 3671.30;
                            porcentaje_exc_limite_dbl=0.17934;
                        }
                        if(valor_conUtilidad_dbl>3089862.97  &&valor_conUtilidad_dbl<3433180.45 ){
                            limite_inferior_dbl= 3089862.97;
                            cuota_fija_dbl=  4287.01 ;
                            porcentaje_exc_limite_dbl=0.18486;
                        }
                        if(valor_conUtilidad_dbl>3433180.46&&valor_conUtilidad_dbl<3776497.98 ){
                            limite_inferior_dbl= 3433180.46;
                            cuota_fija_dbl=   4921.67 ;
                            porcentaje_exc_limite_dbl=0.18988;
                        }
                        if(valor_conUtilidad_dbl>3776497.99&&valor_conUtilidad_dbl<4120143.77 ){

                            limite_inferior_dbl= 3776497.99;
                            cuota_fija_dbl=   5573.56 ;
                            porcentaje_exc_limite_dbl=0.20059;
                        }
                        if(valor_conUtilidad_dbl>4120143.78&&valor_conUtilidad_dbl<12360429.98){
                            limite_inferior_dbl= 4120143.78;
                            cuota_fija_dbl=   6262.88 ;
                            porcentaje_exc_limite_dbl=0.21660;
                        }
                        if(valor_conUtilidad_dbl>12360429.99&&valor_conUtilidad_dbl<26015421.90){
                            limite_inferior_dbl= 12360429.99;
                            cuota_fija_dbl=   24111.34 ;
                            porcentaje_exc_limite_dbl=0.21671;
                        }
                        if(valor_conUtilidad_dbl>26015421.91){
                            limite_inferior_dbl= 26015421.91;
                            cuota_fija_dbl=   53703.07 ;
                            porcentaje_exc_limite_dbl=0.22529;
                        }
                        valor_isai_recalculado_dbl=valor_conUtilidad_dbl-limite_inferior_dbl;
                        Log.e("valor_isai_1_dbl", String.valueOf(valor_isai_recalculado_dbl));
                        valor_isai_recalculado_dbl=valor_isai_recalculado_dbl*porcentaje_exc_limite_dbl;
                        Log.e("valor_isai_1_dbl", String.valueOf(valor_isai_recalculado_dbl));
                        valor_isai_recalculado_dbl=valor_isai_recalculado_dbl+cuota_fija_dbl;
                        Log.e("valor_isai_1_dbl", String.valueOf(valor_isai_recalculado_dbl));
                        String formattedValorIsaiRecal = formatter.format(valor_isai_recalculado_dbl);

                        valor_isai_recalculado.setText("$"+formattedValorIsaiRecal+" M.N.");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(marcar_utilidad.getWindowToken(), 0);
                        marcar_utilidad.requestFocus();
                        check_utilidad =1;
                        marcar_utilidad.setImageResource(R.drawable.lock);

                    } else {
                        Toast.makeText(getApplicationContext(), "El porcentaje de utilidad es neccesario.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    utilidad_fijada.setVisibility(View.GONE);
                    porcenta_utilidad_new.setVisibility(View.VISIBLE);
                    check_utilidad =0;
                    marcar_utilidad.setImageResource(R.drawable.unlock);
                    porcenta_utilidad_new.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(porcenta_utilidad_new, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        finalizar_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual_str="pagina_9";
                scrolIsai.setVisibility(View.GONE);
                div_alerta_final.setVisibility(View.VISIBLE);
            }
        });
        btn_calear_envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual_str="pagina_8";
                scrolIsai.setVisibility(View.VISIBLE);
                div_alerta_final.setVisibility(View.GONE);
            }
        });
        abrir_camara_estructura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    pagina_actual_str="pagina_camara";
                    div_preview_camara.setVisibility(View.VISIBLE);
                    nombreImagen = finalPropietario+".jpeg";
                    nombreImagen = nombreImagen.replace(" ", "_");
                    try {
                        imagen = crearArchivo();
                        pathTemp = imagen.getPath();
                        Log.e("pupudrete","gordok");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("mensaje_errpr",e.getMessage());
                    }
                    startCamera(cameraFacing,foto_estructura);
            }
        });
        abrir_camara_mat_muros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual_str="pagina_camara";
                div_preview_camara.setVisibility(View.VISIBLE);
                nombreImagen = finalPropietario+".jpeg";
                nombreImagen = nombreImagen.replace(" ", "_");
                try {
                    imagen = crearArchivo();
                    pathTemp = imagen.getPath();
                    Log.e("pupudrete","gordok");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("mensaje_errpr",e.getMessage());
                }
                startCamera(cameraFacing,foto_mat_muros);
            }
        });
        abrir_camara_entrepisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual_str="pagina_camara";
                div_preview_camara.setVisibility(View.VISIBLE);

                nombreImagen = finalPropietario+".jpeg";
                nombreImagen = nombreImagen.replace(" ", "_");
                try {
                    imagen = crearArchivo();
                    pathTemp = imagen.getPath();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("mensaje_errpr",e.getMessage());
                }
                startCamera(cameraFacing,foto_entrepisos);
            }
        });
        abrir_camara_cubiertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagina_actual_str="pagina_camara";
                div_preview_camara.setVisibility(View.VISIBLE);
                nombreImagen = finalPropietario+".jpeg";
                nombreImagen = nombreImagen.replace(" ", "_");
                try {
                    imagen = crearArchivo();
                    pathTemp = imagen.getPath();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("mensaje_errpr",e.getMessage());
                }
                startCamera(cameraFacing,foto_cubiertas);
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
    File crearArchivo() throws IOException {
        filesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pathTemp = null;

        carpeta = new File(filesDir, "Valuaciones");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        propietario();
        if (nombreImagen == null) {
            nombreImagen = finalPropietario+".jpeg";
            nombreImagen = nombreImagen.replace(" ","-");
        }

        nombreImagen = finalPropietario+".jpeg";

        File imagen3 = new File(carpeta, nombreImagen);
        pathTemp = imagen3.getAbsolutePath();
        Path filePath = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            filePath = imagen3.toPath();
            String fileType = null;
            fileType = Files.probeContentType(filePath);
            if (fileType != null) {
                System.out.println("El tipo de archivo es: " + fileType);
            } else {
                System.out.println("No se pudo determinar el tipo de archivo.");
            }
        }
        return imagen3;
    }
    private void propietario() {
        Date date = new Date();
        DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        propietario2 = fechaHora.format(date);
        int valorDado = (int) Math.floor(Math.random()*6+1);
        String convertido = String.valueOf(valorDado);
        finalPropietario = propietario2 + convertido;
        finalPropietario = finalPropietario.replace(" ","_");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

            }
        }
    }
    public Bitmap rotarImagen(Bitmap bitmap, int grados, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(grados);

        if (rotation != 0) {
            int centerX = bitmap.getWidth() / 2;
            int centerY = bitmap.getHeight() / 2;
            matrix.postRotate(rotation, centerX, centerY);
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar la ubicación inicial
        miLatLong();

        // Inicializar el listener de clic para la ubicación
        locationClickListener = latLng -> {
            // Actualizar ubicación y marcador
            actualizarUbicacion(new Location("") {{
                setLatitude(latLng.latitude);
                setLongitude(latLng.longitude);
            }});
        };

        // Inicializar el listener de clic para el dibujo del polígono
        drawPolygonClickListener = latLng -> {
            // Añadir el punto a la lista de puntos del polígono
            polygonPoints.add(latLng);
            drawPolygon();

            // Calcular y mostrar el área del polígono
            if (polygonPoints.size() > 2) {
                area_poligono = calculateArea(polygon);
                Log.e("Área del polígono: " , String.valueOf(area));
            }
        };

        // Configurar el listener inicial para la ubicación
        mMap.setOnMapClickListener(locationClickListener);
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
       // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 5, locationListener);
    }
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            agregarMarcadorUbicacion(latitud, longitud);
            direccion = darDireccion(this, latitud, longitud);
            LatLng coord = new LatLng(latitud, longitud);
            String[] direccion_fragmentada = direccion.split(",");
            for (int i = 0; i < direccion_fragmentada.length; i++) {
                Log.e("direccion_fragmentada", direccion_fragmentada[i]);
            }
            nombre_colonia = direccion_fragmentada[1].toUpperCase().trim();
            nombre_alcaldia = direccion_fragmentada[2].toUpperCase().trim();


            coordenadas = coord;
            puntoPartida.setText(direccion);

            // Guardar ubicación en preferencias
            Context context = this;
            SharedPreferences preferencias = getSharedPreferences("Usuario", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("latitud", String.valueOf(latitud));
            editor.putString("longitud", String.valueOf(longitud));
            editor.putString("direccion", "" + direccion);
            editor.apply();
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
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLong)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("fondo", 70, 70))));
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latLong, 18);
        mMap.animateCamera(miUbicacion);
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
   /* public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
    }*/
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
    public void setListaDescripcionEspacios()
    {
        listaDescripcionEspacio.clear();
        String coy[] = {"", "Cuarto para preparar\n" +
                "alimentos, comer y dormir con\n" +
                "baño","Sala-comedor, cocina,\n" +
                "recámara(s) y baño(s",
                "Sala y comedor, cocina,\n" +
                        "recámara(s), baño(s), cuarto de\n" +
                        "servicio o lavado y planchado,\n" +
                        "estudio y lugares de\n" +
                        "estacionamient,Sala y comedor, cocina,\n" +
                        "desayunador, recámara(s),\n" +
                        "baño(s), cuarto de servicio con\n" +
                        "baño, estudio, cuarto de lavado\n" +
                        "y lugares de estacionamiento\n" +
                        "cubiertos,Sala y comedor, cocina,\n" +
                        "desayunador, estudio, sala de\n" +
                        "televisión, recámara(s),\n" +
                        "baño(s), cuarto de servicio con\n" +
                        "baño, cuarto de lavado,\n" +
                        "lugares de estacionamiento\n" +
                        "cubiertos y espacios\n" +
                        "adicionales tales como\n" +
                        "gimnasio y salón de juegos,Antesala(s), sala(s) comedor,\n" +
                        "cocina, desayunador,\n" +
                        "estudio(s), sala de televisión,\n" +
                        "recámara(s), baño(s), alguno\n" +
                        "con instalaciones para vapor\n" +
                        "y/o sauna, cuarto(s) de servicio\n" +
                        "con baño(s), cuarto de lavado y\n" +
                        "planchado, lugares de\n" +
                        "estacionamiento cubiertos y\n" +
                        "espacios adicionales tales\n" +
                        "como gimnasio, salón de\n" +
                        "juegos, salas de proyección,\n" +
                        "bar y salón de fiesta"};
        for (int i=0; i<coy.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaDescripcionEspacio.add(sched);
        }
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
        String coy1[] = {"","Lamina","Madera", "Tabicon", "Block","Tabique","Sillar de adobe"};
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
    private void drawPolygon() {
        if (polygon != null) {
            polygon.remove();
        }

        if (polygonPoints.size() > 0) {
            PolygonOptions polygonOptions = new PolygonOptions().addAll(polygonPoints).clickable(true)
                    .strokeColor(0xFF0000FF)
                    .fillColor(0x7F00FF00);
            polygon = mMap.addPolygon(polygonOptions);

            // Eliminar todos los marcadores antes de añadir nuevos
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();

            // Añadir marcadores en los puntos del polígono para hacerlos más visibles
            for (LatLng point : polygonPoints) {
                markers.add(mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
            }
        }
    }
    // Método para calcular el área de un polígono
    private double calculateArea(Polygon polygon) {
        List<LatLng> points = polygon.getPoints();
        return SphericalUtil.computeArea(points);
    }
    private void  check_box_are(){
        check_dibujar_poligono.setChecked(false);
        check_numero_area.setChecked(false);
    }
    private void handleBackPressed() {
        switch (pagina_actual_str) {
            case "pagina_1":

                break;
            case "pagina_2":
                mapaid.setVisibility(View.VISIBLE);
                val_tierra.setVisibility(View.GONE);
                valTierra.setVisibility(View.GONE);
                pagina_actual_str = "pagina_1";
                break;
            case "pagina_3":

                mapaid.setVisibility(View.GONE);
                val_tierra.setVisibility(View.VISIBLE);
                valTierra.setVisibility(View.VISIBLE);
                ingresar_val_form.setVisibility(View.VISIBLE);
                div_poligono.setVisibility(View.GONE);
                pagina_actual_str="pagina_2";
                msj_mapa.setText("Puedes cambiar tu ubicacion dando click en el mapa");
                // Cambiar el listener de clic en el mapa para el dibujo del polígono
                mMap.setOnMapClickListener(locationClickListener);
                break;
                case "pagina_4":
                    pagina_actual_str="pagina_2";
                    val_tierra.setVisibility(View.VISIBLE);
                    valor_construccion.setVisibility(View.GONE);
                    break;
            case "pagina_5":
                pagina_actual_str="pagina_4";
                valor_construccion.setVisibility(View.VISIBLE);
                valor_acabados.setVisibility(View.GONE);
                break;
            case "pagina_6":
                pagina_actual_str="pagina_5";
                valor_acabados.setVisibility(View.VISIBLE);
                valor_puntos.setVisibility(View.GONE);
                break;
            case "pagina_7":
                pagina_actual_str="pagina_6";

                valor_puntos.setVisibility(View.VISIBLE);
                valor_m2.setVisibility(View.GONE);
                break;
            case "pagina_8":
                pagina_actual_str="pagina_7";
                valor_m2.setVisibility(View.VISIBLE);
                scrolIsai.setVisibility(View.GONE);
                break;
           case "pagina_9":
               pagina_actual_str="pagina_8";
               scrolIsai.setVisibility(View.VISIBLE);
               div_alerta_final.setVisibility(View.GONE);
                break;
            /*case "dictamenes_view":
                caja_info_pesas.setVisibility(View.VISIBLE);
                caja_dictamen_final.setVisibility(View.GONE);
                pagina_actual_str = "caja_equipo_basculas";
                break;
            case "caja_firma_cliente":
                div_formulario_principal.setVisibility(View.VISIBLE);
                div_firma_cliente.setVisibility(View.GONE);
                pagina_actual_str = "formulario_principal";
                break;
            case "pagina_camara":
                div_preview_camara.setVisibility(View.GONE);
                caja_dictamen_final.setVisibility(View.VISIBLE);
                pagina_actual_str = "dictamenes_view";
                break;*/
        }
    }
    public void startCamera(int cameraFacing,ImageView imageViewToUpdate) {
        int aspectRatio = aspectRatio(previewCamara.getWidth(), previewCamara.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                // OnClickListener para el botón de tomar foto
                tomar_foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Llamar a takePicture con el ImageView objetivo
                        takePicture(imageCapture, imageViewToUpdate);
                    }
                });

                preview.setSurfaceProvider(previewCamara.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    // Modificación de takePicture para aceptar el ImageView objetivo
    public void takePicture(ImageCapture imageCapture, ImageView imageViewToUpdate) {
        final File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri uriFoto = Uri.fromFile(file);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Mapa.this, "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();
                        Bitmap imgBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        try {
                            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                            int rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                            int grados = 0;
                            switch (rotation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    grados = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    grados = 90;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    grados = 360;
                                    break;
                            }

                            Bitmap imgRotada;

                            if(grados!=0){
                                imgRotada = rotarImagen(imgBitmap, -90, grados);
                            }
                            else{
                                imgRotada=imgBitmap;
                            }
                            imageViewToUpdate.setImageBitmap(imgRotada);
                            imageViewToUpdate.setVisibility(View.VISIBLE);
                            // Ocultar vistas o realizar otra lógica después de tomar la foto
                            div_preview_camara.setVisibility(View.GONE);

                        } catch (IOException e) {
                            e.printStackTrace();

                        }


                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Mapa.this, "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                startCamera(cameraFacing, imageViewToUpdate); // Volver a iniciar la cámara si hay un error
            }
        });
    }
    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}