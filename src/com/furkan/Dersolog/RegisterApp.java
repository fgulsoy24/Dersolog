package com.furkan.Dersolog;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
 
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
 
import com.google.android.gms.gcm.GoogleCloudMessaging;

 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
  
  
public class RegisterApp extends AsyncTask<Void, Void, String> {
  
 private static final String TAG = "Koyun Ke�i D�nyas�";
 Context ctx;
 GoogleCloudMessaging gcm;//Google Cloud referans�
 final String PROJECT_ID = "787656692538";//Bu de�er Google Ap� sayfas�nda Owerview men�s�nde(Giri� sayfas�) yukar�da yer al�r. Project Number:987... �eklinde
 String regid = null; 
 private int appVersion;
  
 public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion){ //SplassScreen den gelen de�erleri ald�k
  this.ctx = ctx;
  this.gcm = gcm;
  this.appVersion = appVersion;
 }
   
   
 @Override
 protected void onPreExecute() {
  super.onPreExecute();
 }
  
  
 @Override
 protected String doInBackground(Void... arg0) { //
  String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);//GCM objesi olu�turduk ve gcm referans�na ba�lad�k
            }
            regid = gcm.register(PROJECT_ID);//gcm objesine PROJECT_ID mizi g�ndererek regid de�erimizi ald�k.Bu de�erimizi hem sunucular�m�za g�nderece�iz Hemde Androidde sakl�yaca��z
            msg = "Registration ID=" + regid;
 
             
            sendRegistrationIdToBackend();//Sunuculara regid g�nderme i�lemini yapacak method
             
            storeRegistrationId(ctx, regid);//Androidde regid sakl� tutacak method
             
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            
        }
        return msg;
 }
  
 private void storeRegistrationId(Context ctx, String regid) {//Androidde regid ve appversion sakl� tutacak method
     //Burada SharedPreferences kullanarak kay�t yapmaktad�r
     //SharedPreferences hakk�nda ayr�nt�l� dersi Blo�umuzda bulabilirsiniz.
   final SharedPreferences prefs = ctx.getSharedPreferences(SplashScreen.class.getSimpleName(),
             Context.MODE_PRIVATE);
     Log.i(TAG, "Saving regId on app version " + appVersion);
     SharedPreferences.Editor editor = prefs.edit();
     editor.putString("registration_id", regid);
     editor.putInt("appVersion", appVersion);
     editor.commit();
    
 }
  
  
 private void sendRegistrationIdToBackend() {//Sunucuya regid de�erini g�nderecek method
     //Arkada�lar biz burda get methodu ile g�nderdik .
     //Siz isterseniz post methoduda kullanabilirsiniz
     //HTTP Post ie ilgili dersimiz blog umuzda bulunmaktad�r.
  URI url = null;
  try {
   url = new URI("http://takipciscript.furkangulsoy.com/koyundunyasi/register.php?regId=" + regid);
  } catch (URISyntaxException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } 
  HttpClient httpclient = new DefaultHttpClient();
  HttpGet request = new HttpGet();
  request.setURI(url);
  try {
   httpclient.execute(request);
  } catch (ClientProtocolException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
  
  
 @Override
 protected void onPostExecute(String result) {
     //doInBackground i�lemi bittikten sonra �al���r
  super.onPostExecute(result);
  Log.v(TAG, result);
  Intent i = new Intent(ctx,MainActivity.class);//Anasayfaya Y�nlendir
  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
  i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
  ctx.startActivity(i);
  
 }
}