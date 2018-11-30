package com.example.eugenio.integrador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eugenio.integrador.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.zip.Inflater;

public class CustomAdapter extends ArrayAdapter<Image> {
    private LayoutInflater inflater;
    public CustomAdapter(Context context, Image[] books) {
        super(context, 0, books);
        inflater = ( LayoutInflater.from(context) );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if( row == null ){
            //We must create a View:
            row = inflater.inflate(R.layout.listview_activity, parent, false);
        }
        final Image thisBook = getItem(position);
        final ImageView photo = row.findViewById(R.id.listview_image);
        final TextView nombre = row.findViewById(R.id.listview_item_nombre);
        final TextView fecha = row.findViewById(R.id.listview_item_fecha);
        final TextView hora = row.findViewById(R.id.listview_item_hora);
        nombre.setText(thisBook.name);
        fecha.setText(thisBook.fecha);
        hora.setText(thisBook.hora);
        photo.setImageBitmap(getResizedBitmap(getBitmapFromURL(thisBook.image_url),50,50));

        return row;
    }
        public Bitmap getBitmapFromURL(String src) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                    matrix, false);

            return resizedBitmap;
        }
}