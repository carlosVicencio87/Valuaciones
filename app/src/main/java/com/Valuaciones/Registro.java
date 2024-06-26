package com.Valuaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity{


    private ExecutorService executorService;
    private TextView fecha, registrar,mensaje;
    private EditText nombre,apellidos,telefono,pin,confirmarPin,email;
    private String valnom,valapellidos,valtel,valpin,valconP,valemail, nombreFinal,apellidosFinal,telefonoFinal,conPinFinal,pinFinal,emailfinal,nombreTmp,apellidostemp,telefonotemp,pintemp,conPintemp;
    private boolean tel10,pin4,nombreExitoso,apellidoExitoso,telefonoExitoso,confirmartelExitoso,telefonoExistente,pinExitoso,comPinExitoso,emailExitoso;
    private static String SERVIDOR_CONTROLADOR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registro);

        SERVIDOR_CONTROLADOR = new Servidor().servidor;

        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        pin = findViewById(R.id.pin);
        confirmarPin = findViewById(R.id.confirmarPin);
        email =findViewById(R.id.email);
        registrar = findViewById(R.id.registrar);
        mensaje = findViewById(R.id.mensaje);
        executorService= Executors.newSingleThreadExecutor();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cuenta = telefono.getText().toString().trim().length();
                if (cuenta == 10) {
                    tel10 = true;
                }
                else {
                    tel10 = false;
                }
                int cpin = pin.getText().toString().trim().length();
                if (cpin == 4) {

                    pin4 = true;
                } else {

                    pin4 = false;
                }
                /*executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        buscar_tel();
                    }
                });*/
                valnom = nombre.getText().toString();
                valapellidos = apellidos.getText().toString();
                valtel = telefono.getText().toString();
                valpin = pin.getText().toString();
                valconP = confirmarPin.getText().toString();
                valemail = email.getText().toString();
                if(!valnom.trim().equals("")){
                    if (!valapellidos.trim().equals("")){
                        if(!valtel.trim().equals("")){
                                if(!valpin.trim().equals("")){
                                    if(!valconP.trim().equals("")){
                                        if(!valemail.trim().equals("")){
                                            if(nombreExitoso=true){
                                                if(apellidoExitoso=true){
                                                    if(telefonoExitoso=true){
                                                        if(pinExitoso=true){
                                                            if(emailExitoso=true){

                                                                    mensaje.setText("Registrado");
                                                                    mensaje.setVisibility(View.VISIBLE);
                                                                    registrar.setVisibility(View.GONE);
                                                                    executorService.execute(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            hacerPeticion();
                                                                        }
                                                                    });


                                                            }
                                                            else{Toast.makeText(getApplicationContext(),"Ingrese una direccion de correo valida",Toast.LENGTH_LONG).show();}
                                                        }
                                                        else{Toast.makeText(getApplicationContext(),"El pinn solo pede tener numeros",Toast.LENGTH_LONG).show();}
                                                    }
                                                    else{Toast.makeText(getApplicationContext(),"El telefono solo pede tener numeros",Toast.LENGTH_LONG).show();}
                                                }
                                                else{Toast.makeText(getApplicationContext(),"El apellido solo pede tener letras",Toast.LENGTH_LONG).show();}
                                            }
                                            else{Toast.makeText(getApplicationContext(),"El nombre solo pede tener letras",Toast.LENGTH_LONG).show();}
                                        }
                                        else{Toast.makeText(getApplicationContext(),"Ingresar un correo es necesario",Toast.LENGTH_LONG).show(); }
                                    }
                                    else{Toast.makeText(getApplicationContext(),"Confirmar tu contrasena es necesario",Toast.LENGTH_LONG).show(); }
                                }
                                else{Toast.makeText(getApplicationContext(),"Una contrasena es necesaria",Toast.LENGTH_LONG).show(); }
                            }

                        else{ Toast.makeText(getApplicationContext(),"El telefono es necesario",Toast.LENGTH_LONG).show();}
                    }
                    else{Toast.makeText(getApplicationContext(),"El apellido es necesario",Toast.LENGTH_LONG).show(); }
                }
                else{Toast.makeText(getApplicationContext(),"El nombre es necesario",Toast.LENGTH_LONG).show(); }
            }
        });

        nombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    //Toast.makeText(getApplicationContext(), "NOMBRE PERDIO FOCO", Toast.LENGTH_LONG).show();
                    nombreFinal=nombre.getText().toString().trim().toLowerCase();
                    if (!nombreFinal.equals("")&&nombreFinal!=null)
                    {
                        String regexUsuario = "[a-z]+";
                        nombreTmp=nombreFinal;
                        String verificarReg= nombreTmp.trim().replaceAll(regexUsuario,"");
                        if (verificarReg.equals(""))
                        {
                            nombreExitoso=true;
                            mensaje.setVisibility(View.GONE);
                        }

                        else
                        {

                            mensaje.setText("El nombre solo puede contener letras");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "NOMBRE obtuvo FOCO", Toast.LENGTH_LONG).show();
                }
            }
        });
        apellidos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    //Toast.makeText(getApplicationContext(), "NOMBRE PERDIO FOCO", Toast.LENGTH_LONG).show();
                    apellidosFinal=apellidos.getText().toString().trim().toLowerCase();
                    if (!apellidosFinal.equals("")&&apellidosFinal!=null)
                    {
                        String regexUsuario = "[a-z]+";
                        apellidostemp=apellidosFinal;
                        String verificarReg= apellidostemp.trim().replaceAll(regexUsuario,"");
                        if (verificarReg.equals(""))
                        {
                            apellidoExitoso=true;
                            mensaje.setVisibility(View.GONE);
                        }

                        else
                        {

                            mensaje.setText("Los apellidos solo pueden tener   letras");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "NOMBRE obtuvo FOCO", Toast.LENGTH_LONG).show();
                }
            }
        });
        telefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    //Toast.makeText(getApplicationContext(), "NOMBRE PERDIO FOCO", Toast.LENGTH_LONG).show();
                    telefonoFinal=telefono.getText().toString().trim().toLowerCase();
                    if (!telefonoFinal.equals("")&&telefonoFinal!=null)
                    {
                        String regexUsuario = "[0-9]+";
                        telefonotemp=telefonoFinal;
                        String verificarReg= telefonotemp.trim().replaceAll(regexUsuario,"");
                        if (verificarReg.equals(""))
                        {
                            telefonoExitoso=true;
                            mensaje.setVisibility(View.GONE);
                        }

                        else
                        {

                            mensaje.setText("El telefono solo pueden tener numeros");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{

                }
            }
        });
        pin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    //Toast.makeText(getApplicationContext(), "NOMBRE PERDIO FOCO", Toast.LENGTH_LONG).show();
                    pinFinal=pin.getText().toString().trim().toLowerCase();
                    if (!pinFinal.equals("")&&pinFinal!=null)
                    {
                        String regexUsuario = "[0-9]+";
                        pintemp=pinFinal;
                        String verificarReg= pintemp.trim().replaceAll(regexUsuario,"");
                        if (verificarReg.equals(""))
                        {
                            pinExitoso=true;
                            mensaje.setVisibility(View.GONE);
                        }

                        else
                        {

                            mensaje.setText("El telefono solo pueden tener numeros");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "NOMBRE obtuvo FOCO", Toast.LENGTH_LONG).show();
                }
            }
        });
        confirmarPin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    //Toast.makeText(getApplicationContext(), "NOMBRE PERDIO FOCO", Toast.LENGTH_LONG).show();
                    conPinFinal=confirmarPin.getText().toString().trim().toLowerCase();
                    if (!conPinFinal.equals("")&&conPinFinal!=null)
                    {
                        String regexUsuario = "[0-9]+";
                        conPintemp=conPinFinal;
                        String verificarReg= conPintemp.trim().replaceAll(regexUsuario,"");
                        if (verificarReg.equals(""))
                        {
                            comPinExitoso=true;

                            valpin = pin.getText().toString().trim();
                            valconP = confirmarPin.getText().toString().trim();

                            Log.e("valores",valpin+"   "+valconP);
                            if(valpin.equals(valconP)){
                                mensaje.setVisibility(View.GONE);
                                Log.e("paso","paso");
                            }
                            else{
                                mensaje.setText("Los PIN no coinciden");
                                mensaje.setVisibility(View.VISIBLE);
                            }
                        }

                        else
                        {

                            mensaje.setText("El PIN solo pueden tener numeros");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "NOMBRE obtuvo FOCO", Toast.LENGTH_LONG).show();
                }
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    emailfinal=email.getText().toString().trim().toLowerCase();
                    if (!emailfinal.equals("")&&emailfinal!=null)
                    {
                        // String regex = "^(.+)@(.+)$";

                        String regexUsuario = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                        Pattern pattern = Pattern.compile(regexUsuario);
                        Matcher matcher = pattern.matcher(emailfinal);
                        if(matcher.matches()==true){

                            emailExitoso=true;
                            mensaje.setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    mensaje.setText("Ingrese una direccion de correo valido");
                    mensaje.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void hacerPeticion()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"registro.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta4:",response + "sal");
                        if(response.equals("success")){
                            mensaje.setText("El registro del usuario a sido exitoso");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("respuesta4Error:",error + "error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                    map.put("nombre",valnom);
                map.put("apellido",valapellidos);
                map.put("telefono",valtel);
                map.put("email",valemail);
                map.put("password",valpin);


                return map;
            }
        };
        requestQueue.add(request);
    }


    public void buscar_tel()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"verificar_telefono.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta5:",response + "EL TELEFONO YA EXISTE");
                        if(response.equals("existe")){
                            telefonoExistente=true;
                            mensaje.setVisibility(View.VISIBLE);
                            mensaje.setText("El telefono ya existe");


                        }
                        else{

                            telefonoExistente=false;
                            mensaje.setVisibility(View.GONE);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("respuesta5Error:",error + "error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();


                map.put("telefono",valtel);
                return map;
            }
        };
        requestQueue.add(request);
    }
}