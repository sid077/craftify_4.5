package com.siddhant.craftifywallpapers.viewmodel;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DownloadImage extends AsyncTask<String,Integer,Bitmap> {
   private Bitmap bitmap = null;
    private ProgressBar progressBar;
    private Context context;

    public DownloadImage(ProgressBar progressBar, Context context) {
        this.progressBar = progressBar;

        this.context = context;
    }

    public DownloadImage() {
        progressBar = null;
        context = null;
    }


    @Override
    protected void onPreExecute(){
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);


    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            connection.disconnect();




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e){
            e.printStackTrace();

        }




        return bitmap;
    }


}
