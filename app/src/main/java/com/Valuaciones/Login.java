package com.Valuaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {


    private EditText correo,contrasena;
    private TextView ingresar,recuperarContra,mensaje,registrar;
    private String valCorreo,valContra,correo_final;
    private static String SERVIDOR_CONTROLADOR;
    private int check=0;
    private SharedPreferences datosUsuario;
    private SharedPreferences.Editor editor;
    private boolean correo_exitoso,contrasena_exitoso;
    private JSONObject json_datos_usuario;
    private  String strInicio,strUsuario;
    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        SERVIDOR_CONTROLADOR = new Servidor().local;
        datosUsuario = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosUsuario.edit();


        correo=findViewById(R.id.correo);
        contrasena =findViewById(R.id.contrasena);
        ingresar= findViewById(R.id.ingresar);
        recuperarContra =findViewById(R.id.recuperarContra);
        registrar =findViewById(R.id.registrar);
        mensaje =findViewById(R.id.mensaje);
        datosUsuario = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosUsuario.edit();
        executorService= Executors.newSingleThreadExecutor();
        checkSesion();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valCorreo=correo.getText().toString();
                valContra=contrasena.getText().toString();
                Log.e("datocorreo",valCorreo );
                Log.e("datocontra",valContra );
                if(!valCorreo.trim().equals("")){
                    if(!valContra.trim().equals("")){
                        if(correo_exitoso==true){


                            recuperarContra.setVisibility(View.GONE);
                            ingresar.setVisibility(View.GONE);
                            registrar.setVisibility(View.GONE);
                            mensaje.setText("Iniciando sesión ...");
                            mensaje.setVisibility(View.VISIBLE);
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    hacerPeticion();
                                    Log.e("tarea","y esto igual");
                                    Intent intent = new Intent(Login.this,Mapa.class);
                                    startActivity(intent);
                                }
                            });


                        }
                        else{
                            correo_exitoso = false;
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "La contrasena es necesario.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "El correo es necesario.", Toast.LENGTH_LONG).show();
                }

            }

        });
        recuperarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intento2= new Intent( Login.this,Recuperar_Contra.class);
                //startActivity(intento2);
            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento3= new Intent(Login.this, Registro.class);
                startActivity(intento3);
            }
        });

        correo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    correo_final=correo.getText().toString().trim().toLowerCase();
                    if (!correo_final.equals("")&&correo_final!=null)
                    {
                        // String regex = "^(.+)@(.+)$";

                        String regexUsuario = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                        Pattern pattern = Pattern.compile(regexUsuario);
                        Matcher matcher = pattern.matcher(correo_final);
                        if(matcher.matches()==true){

                            correo_exitoso=true;

                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Ingrese correo valido.",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    public void hacerPeticion()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta:",response);
                        if (response.equals("no_existe")) {
                            recuperarContra.setVisibility(View.VISIBLE);
                            ingresar.setVisibility(View.VISIBLE);
                            mensaje.setText("El teléfono o correo es incorrecto.");
                        }
                        else
                        {
                            try {

                                //json_datos_usuario=new JSONArray(response);
                                json_datos_usuario=new JSONObject(response);
                                String strId = json_datos_usuario.getString("id");
                                String strNombre= json_datos_usuario.getString("nombre");
                                String strApellido = json_datos_usuario.getString("apellido");
                                String strTelefono = json_datos_usuario.getString("telefono");
                                String strCorreo = json_datos_usuario.getString("email");
                                String strContra=json_datos_usuario.getString("password");
                                String strActivo=json_datos_usuario.getString("activo");
                                String strFirecode=json_datos_usuario.getString("fireCode");
                                String strLatitud=json_datos_usuario.getString("lat");
                                String strLongitud=json_datos_usuario.getString("lng");
                                String stridSesion=json_datos_usuario.getString("id_sesion");
                                String strFechaRegistro=json_datos_usuario.getString("fecha_registro");
                                Log.e("id_sesion",""+stridSesion);
                                Log.e("lala",""+json_datos_usuario);
                                editor.putString("id",strId);
                                editor.putString("nombre",strNombre);
                                editor.putString("apellido",strApellido);
                                editor.putString("telefono",strTelefono);
                                editor.putString("email",strCorreo);
                                editor.putString("password",strContra);
                                editor.putString("activo",strActivo);
                                editor.putString("fireCode",strFirecode);
                                editor.putString("lat",strLatitud);
                                editor.putString("lng",strLongitud);
                                editor.putString("id_sesion",stridSesion);
                                editor.putString("fecha_registro",strFechaRegistro);
                                editor.apply();
                                datosUsuario.getString("usuario","no hay");
                                String prefedatosus=datosUsuario.getString("idSesion","no hay");
                                Log.e("comprobacion",""+prefedatosus);

                                // Log.e("3",strActivo);
                                Log.e("4",strFirecode);
                                Log.e("5",stridSesion);

                            }
                            catch (JSONException e) {
                                Log.e("errorRespuesta", String.valueOf(e));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e( "error", "error: " +error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",valCorreo);
                map.put("password",valContra);
                return map;
            }
        };
        requestQueue.add(request);
    }
    private void checkSesion() {
        strInicio = datosUsuario.getString("id_sesion", "no");

        Log.e("inicio",""+strInicio);
        if (!strInicio.equals("no"))
        {

            Log.e("idsesion_main",strInicio);
            Intent agenda= new Intent(Login.this, Mapa.class);
            startActivity(agenda);
        }
    }
}