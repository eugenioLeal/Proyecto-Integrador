package com.example.eugenio.integrador;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.ProgressDialog;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
public class Menu extends AppCompatActivity {

    private String MICROSERVICIO = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/services/Subir/subir.php";
    static final int REQUEST_TAKE_PHOTO = 1;
    private Bitmap bitmap;
    private EditText editarNombre;
    private String LLAVE_IMAGEN = "imagen";
    private String LLAVE_NOMBRE = "nombre";
    private String LLAVE_ID_EXPERIMENTO = "experimento_id";
    String nombre;

    String mCurrentPhotoPath;
    int READ_REQUEST_CODE = 42;
    private int SOLICITAR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        editarNombre = (EditText) findViewById(R.id.editText);

    }
    public void onClickCapture(View v) {
        takePictureIntent();
    }
    // Simple version without saving image
    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            Log.d("foto","1");
            File photoFile = null;
            try {
                Log.d("foto","2");
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("foto","3");
                // Error occurred while creating the File
                ex.printStackTrace();
                Toast.makeText(Menu.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("foto","4");
               Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    protected void onActivityResult(int codigoDeSolicitud, int codigoDeResultado, Intent datos) {
        super.onActivityResult(codigoDeSolicitud, codigoDeResultado, datos);
        Log.d("foto","5");
                Log.d("foto",String.valueOf(codigoDeSolicitud)+datos.toString()+datos.getData().toString());
        if (codigoDeSolicitud == REQUEST_TAKE_PHOTO && datos != null && datos.getData() != null) {
            Uri filePath = datos.getData();
            try {
                Log.d("foto","6");
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                Log.d("foto","7");
                e.printStackTrace();
                Toast.makeText(Menu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void subirImagen(View v) {
        // Obtenemos el nombre de la imagen
        nombre = editarNombre.getText().toString().trim();
        // Mostramos una barra de progreso
        if(!nombre.equals("")) {
            final ProgressDialog cargadno = ProgressDialog.show(this, "Subiendo imagen...", "Subiendo...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MICROSERVICIO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            // Cerramos la barra de progreso
                            cargadno.dismiss();
                            // Mostramos un mensaje como respuesta
                            Toast.makeText(Menu.this, s, Toast.LENGTH_SHORT).show();
                            String token = "";
                            int imgId = -1;
                            Boolean answer = true;
                            try {
                                JSONObject obj = new JSONObject(s);
                                if (!obj.getBoolean("answer"))
                                    answer = false;
                                else
                                    imgId = obj.getInt("data");
                            } catch (Exception e) {
                                answer = false;
                            }
                            if (answer) {
                                Toast.makeText(Menu.this, "imagen mandada", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Menu.this, ImageData.class);
                                intent.putExtra("id", String.valueOf(imgId));
                                startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // Cerramos el dialogo de la barra de progreso
                            cargadno.dismiss();

                            // Monstramos el mensaje de error
                            Toast.makeText(Menu.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Convertimos un Bitmap a un string de datos
                    String imagen = obtieneNombreImagen(bitmap);


                    // Creamos un mapa para los parametros
                    Map<String, String> params = new Hashtable<String, String>();

                    // Le enexamos los parametros
                    params.put(LLAVE_IMAGEN, imagen);
                    params.put(LLAVE_NOMBRE, nombre);
                    params.put(LLAVE_ID_EXPERIMENTO, getIntent().getExtras().getString("id"));

                    // regresamos los parametros
                    return params;
                }
            };

            // Usamos Volley para crear la cola de peticiones
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            // Anexamos el request a la cola
            requestQueue.add(stringRequest);
        }
        else {
            Toast.makeText(Menu.this, "Nombre es obligatorio", Toast.LENGTH_LONG).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = new File(Environment.getExternalStorageDirectory(),
                imageFileName+".jpg"
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public String obtieneNombreImagen(Bitmap mapadebits) {
        ByteArrayOutputStream imagen = new ByteArrayOutputStream();
        mapadebits.compress(Bitmap.CompressFormat.JPEG, 100, imagen);
        byte[] imageBytes = imagen.toByteArray();
        String imagenEncodeada = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imagenEncodeada;
    }
    public void picker(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione la imagen"), SOLICITAR_REQUEST);
    }
    public void onClickResults(View v) {
        Intent intent = new Intent(Menu.this,Results.class);
        startActivity(intent);
    }
}
