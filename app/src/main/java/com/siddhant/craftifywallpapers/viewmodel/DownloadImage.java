package com.siddhant.craftifywallpapers.viewmodel;

import android.app.ProgressDialog;
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
    ProgressDialog mProgressDialog;

    public DownloadImage(ProgressBar progressBar, Context context) {
        this.progressBar = progressBar;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please wait while the wallpaper is being downloaded!");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMax(100);
        this.context = context;
    }

    public DownloadImage() {
        progressBar = null;
        context = null;
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressDialog.setProgress(values[0]);



    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mProgressDialog.dismiss();


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
