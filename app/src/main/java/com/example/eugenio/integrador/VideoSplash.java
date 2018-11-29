package com.example.eugenio.integrador;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VideoSplash extends AppCompatActivity {
    private long tiempoDeEspera = 5000; // milisegundos
    String imageUrl;
    String[] images;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_splash);
        images = new String[]{"blue.jpg","poly.jpg","polyarch.jpg","mount.jpg"};
        Random randomGenerator = new Random();
        int ran = randomGenerator.nextInt(images.length);
        //
        imageUrl = "http://ubiquitous.csf.itesm.mx/~pddm-1022983/content/images/";
        imageUrl = imageUrl + images[ran];


        // Image link from internet
        new DownloadImageFromInternet((ImageView) findViewById(R.id.back))
                .execute(imageUrl);


        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent().setClass(VideoSplash.this, MainActivity.class);
                startActivity(intent);
            }
        };
        Timer timer = new Timer();
        timer.schedule(tarea,tiempoDeEspera);
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
