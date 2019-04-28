package com.example.dell.booksearch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;

import static com.example.dell.booksearch.R.id.Bookname;

public class MainActivity extends AppCompatActivity {

    EditText et_book;
    TextView tv;
    StringBuilder builder;
    String Bookname;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_book=findViewById(R.id.Bookname);
        tv=findViewById(R.id.res);
    }
     public  void search(View view)
     {
          Bookname=et_book.getText().toString();
         String url="https://www.googleapis.com/books/v1/volumes?q="+Bookname;
         Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
         //Toast.makeText(this, ""+builder.toString(), Toast.LENGTH_SHORT).show();
         MyTask task=new MyTask();
         task.execute();
     }

     class MyTask extends AsyncTask<String,Void,String>{
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             ProgressDialog pd= new ProgressDialog(MainActivity.this);
             pd.setMessage("please wait...");
             pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             pd.show();
         }

         @Override
         protected String doInBackground(String... strings) {
             String url = "https://www.googleapis.com/books/v1/volumes?q=" + Bookname;
             try {
                 URL u = new URL(url);
                 HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
                 //Input stream reader is used for getting data from server
                 InputStream is = connection.getInputStream();
                 // as the srever data is not able to read we use buffered reader
                 BufferedReader br = new BufferedReader(new InputStreamReader(is));
                 String line = "";
                 //to get line by line we use stringbuilder or stringbuffer
                 StringBuilder builder = new StringBuilder();
                 while ((line = br.readLine())!= null) {
                     builder.append(line + "\n");
                 }
                 return builder.toString();
             } catch (Exception e) {
                 e.printStackTrace();
             }

             return null;
         }

         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);
             Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
             pd.dismiss();
             try {
                 JSONObject jsonObject= null;
                 JSONArray itemsArray=jsonObject.getJSONArray("items");
                 JSONObject firstitemObject=itemsArray.getJSONObject(0);
                 JSONObject volumeInfoObject=firstitemObject.getJSONObject("volumeInfo");
                 String title=volumeInfoObject.getString("Head First Java");
                 tv.setText(title);
             }catch (JSONException e){
                 e.printStackTrace();
             }
              //String string=  tv.setText(s);
         }// return  st
     }
}
