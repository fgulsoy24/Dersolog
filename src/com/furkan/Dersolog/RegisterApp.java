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
  
 private static final String TAG = "Koyun Keçi Dünyasý";
 Context ctx;
 GoogleCloudMessaging gcm;//Google Cloud referansý
 final String PROJECT_ID = "787656692538";//Bu deðer Google Apý sayfasýnda Owerview menüsünde(Giriþ sayfasý) yukarýda yer alýr. Project Number:987... þeklinde
 String regid = null; 
 private int appVersion;
  
 public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion){ //SplassScreen den gelen deðerleri aldýk
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
                gcm = GoogleCloudMessaging.getInstance(ctx);//GCM objesi oluþturduk ve gcm referansýna baðladýk
            }
            regid = gcm.register(PROJECT_ID);//gcm objesine PROJECT_ID mizi göndererek regid deðerimizi aldýk.Bu deðerimizi hem sunucularýmýza göndereceðiz Hemde Androidde saklýyacaðýz
            msg = "Registration ID=" + regid;
 
             
            sendRegistrationIdToBackend();//Sunuculara regid gönderme iþlemini yapacak method
             
            storeRegistrationId(ctx, regid);//Androidde regid saklý tutacak method
             
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            
        }
        return msg;
 }
  
 private void storeRegistrationId(Context ctx, String regid) {//Androidde regid ve appversion saklý tutacak method
     //Burada SharedPreferences kullanarak kayýt yapmaktadýr
     //SharedPreferences hakkýnda ayrýntýlý dersi Bloðumuzda bulabilirsiniz.
   final SharedPreferences prefs = ctx.getSharedPreferences(SplashScreen.class.getSimpleName(),
             Context.MODE_PRIVATE);
     Log.i(TAG, "Saving regId on app version " + appVersion);
     SharedPreferences.Editor editor = prefs.edit();
     editor.putString("registration_id", regid);
     editor.putInt("appVersion", appVersion);
     editor.commit();
    
 }
  
  
 private void sendRegistrationIdToBackend() {//Sunucuya regid deðerini gönderecek method
     //Arkadaþlar biz burda get methodu ile gönderdik .
     //Siz isterseniz post methoduda kullanabilirsiniz
     //HTTP Post ie ilgili dersimiz blog umuzda bulunmaktadýr.
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
     //doInBackground iþlemi bittikten sonra çalýþýr
  super.onPostExecute(result);
  Log.v(TAG, result);
  Intent i = new Intent(ctx,MainActivity.class);//Anasayfaya Yönlendir
  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
  i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
  ctx.startActivity(i);
  
 }
}