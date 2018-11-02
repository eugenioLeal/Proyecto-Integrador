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
        dispatchTakePictureIntent();
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
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
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

        if (codigoDeSolicitud == SOLICITAR_REQUEST && codigoDeResultado == RESULT_OK && datos != null && datos.getData() != null) {
            Uri filePath = datos.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void subirImagen(View v) {
        // Mostramos una barra de progreso
        final ProgressDialog cargadno = ProgressDialog.show(this, "Subiendo imagen...", "Subiendo...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MICROSERVICIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        // Cerramos la barra de progreso
                        cargadno.dismiss();
                        // Mostramos un mensaje como respuesta
                        Toast.makeText(Menu.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Cerramos el dialogo de la barra de progreso
                        cargadno.dismiss();

                        // Monstramos el mensaje de error
                        Toast.makeText(Menu.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Convertimos un Bitmap a un string de datos
                String imagen = obtieneNombreImagen(bitmap);

                // Obtenemos el nombre de la imagen
                String nombre = editarNombre.getText().toString().trim();

                // Creamos un mapa para los parametros
                Map<String, String> params = new Hashtable<String, String>();

                // Le enexamos los parametros
                params.put(LLAVE_IMAGEN, imagen);
                params.put(LLAVE_NOMBRE, nombre);

                // regresamos los parametros
                return params;
            }
        };

        // Usamos Volley para crear la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Anexamos el request a la cola
        requestQueue.add(stringRequest);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
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
