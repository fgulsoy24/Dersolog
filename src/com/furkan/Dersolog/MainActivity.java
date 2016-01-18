package com.furkan.Dersolog;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;




import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.furkan.Dersolog.MyApplication.TrackerName;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sbstrm.appirater.Appirater;



public class MainActivity extends AppCompatActivity {

	WebView myWebView;
	public ProgressDialog progressBar;
	private CustomWebViewClient webViewClient;
	public ProgressDialog mProgressDialog;
	private WakeLock mWakeLock;
	private InterstitialAd interstitial;
	private static final String REKLAM_ID = "ca-app-pub-1985773543143607/9932289373";
	public int reklamkontrol;
	private VideoEnabledWebView webView;
	private VideoEnabledWebChromeClient webChromeClient;
	ActionBar actionBar;
	public int purchaseads;
	private int myInt;
	public int geciskontrol;
	
	private Toolbar mToolbar;
	public int randinterval=0;
	private String ownername;
	private String owneremail;
	private String ownerphone;

	@Override
	   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        setTitle(" Dersolog");

      
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.actionbarlogo);
        mProgressDialog = new ProgressDialog(this);//ProgressDialog objesi olusturuyoruz
        mProgressDialog.setMessage("Yükleniyor...");//ProgressDialog Yukleniyor yazÄ±sÄ±
        webViewClient = new CustomWebViewClient();
        
        
          
        Tracker t = ((MyApplication) getApplication()).getTracker(
                TrackerName.GLOBAL_TRACKER); 
        t.send(new HitBuilders.AppViewBuilder().build());
       
    	OwnerInfo o= new OwnerInfo(this);
    	String phone = o.getPhone();
    	System.out.println(phone +  " null");
        //internet baï¿½lantï¿½sï¿½ olayï¿½
        if (isOnline()){
        }
        else{
        	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	    builder.setPositiveButton("OK", null)
        	           .setTitle("Ýnternet Baðlantý Hatasý")
        	           .setMessage("Uygulama internetsiz Kullanýlamýyor.")
        	           .setCancelable(false)
        	           .setPositiveButton("Kapat",
        	                  new DialogInterface.OnClickListener() {
        	        	   public void onClick(DialogInterface dialog, int id) {
        	                          finish();
        	                  }
        	           })
        	           .show();
        	    return;
        }
        entryControlOwnerInfo();
     
        adView();
		   final WebView myWebView = (WebView) findViewById(R.id.webview);
		   // Initialize the VideoEnabledWebChromeClient and set event handlers
		   fullscreenEnabled();
	      myWebView.setWebChromeClient(webChromeClient);
		  myWebView.getSettings().setBuiltInZoomControls(true); //zoom yapï¿½lmasï¿½na izin verir
		  myWebView.getSettings().setSupportZoom(true);
		 
		   myWebView.getSettings().setJavaScriptEnabled(true);
		   myWebView.setWebViewClient(webViewClient); //oluï¿½turduï¿½umuz webViewClient objesini webViewï¿½mï¿½za set ediyoruz
		   
		
		   myWebView.loadUrl("http://androidlessons.furkangulsoy.com/home.php");
		   
		   //ekran ï¿½ï¿½ï¿½ï¿½ï¿½ ayarlamasï¿½
		        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,"");    
		         mWakeLock.acquire();
		      Appirater.appLaunched(this);

		   
	}
	public static void postNewComment(final String name,final String email,final String phone){
		 String tag_string_req = "req_register";
	    StringRequest sr = new StringRequest(Request.Method.POST,"http://androidlessons.furkangulsoy.com/redirectinfo.php", new Response.Listener<String>() {
	        @Override
	        public void onResponse(String response) {
	           
	        }
	    }, new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        }
	    }){
	        @Override
	        protected Map<String,String> getParams(){
	            Map<String,String> params = new HashMap<String, String>();
	            params.put("email",email);
	            params.put("name",name);
	            params.put("phone",phone);
	            
	            return params;
	        }

	        @Override
	        public Map<String, String> getHeaders() throws AuthFailureError {
	            Map<String,String> params = new HashMap<String, String>();
	            params.put("Content-Type","application/x-www-form-urlencoded");
	            return params;
	        }
	    };
	    MyApplication.getInstance().addToRequestQueue(sr, tag_string_req);
	    
	}

	public interface PostCommentResponseListener {
	    public void requestStarted();
	    public void requestCompleted();
	    public void requestEndedWithError(VolleyError error);
	}


//webView classï¿½
	private class CustomWebViewClient extends WebViewClient {
       
		//Alttaki methodlarï¿½n hepsini kullanmak zorunda deilsiniz 
        //Hangisi iï¿½inize yarï¿½yorsa onu kullanabilirsiniz.
	
        @Override 
        public void onPageStarted(WebView view, String url, Bitmap favicon) { //Sayfa yï¿½klenirken ï¿½alï¿½ï¿½ï¿½r
            super.onPageStarted(view, url, favicon);
            if(!mProgressDialog.isShowing())//mProgressDialog aï¿½ï¿½k mï¿½ kontrol ediliyor
          	  try
    	    {
          		mProgressDialog.show();           	      }
    	      catch(Exception e) {		

    	    } 
            if (reklamkontrol == 6+randinterval){
            	gecisreklami();
            	rand();
            	reklamkontrol = 0;
            	
            	}
            else {
            	reklamkontrol++;
            }
        }  
public boolean shuldOverrideKeyEvent (WebView view, KeyEvent event) {
         // Do something with the event here
         return true;
    }

    public boolean shouldOverrideUrlLoading (WebView view, String url) {
        if (Uri.parse(url).getHost().equals("www.youtube.com")) {
       
    			
             // This is my web site, so do not override; let my WebView load the page
             return true;
        }

        // reject anything other
        return false;
    }
        @Override 
        public void onPageFinished(WebView view, String url) {//sayfamï¿½z yï¿½klendiï¿½inde ï¿½alï¿½ï¿½ï¿½yor.
            super.onPageFinished(view, url);
       
            if(mProgressDialog.isShowing()){
            	
            	      try
            	    {
            	    	  mProgressDialog.dismiss();            	      }
            	      catch(Exception e) {// nothing }

            	    }//mProgressDialog aï¿½ï¿½k mï¿½ kontrol
               //mProgressDialog aï¿½ï¿½ksa kapatï¿½lï¿½yor
            } 
        }  
         
        
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        	try {
        		webView.stopLoading();
        	} catch (Exception e) {
        	}
        	try {
        		webView.clearView();
        	} catch (Exception e) {
        	}
        	if (webView.canGoBack()) {
        		webView.goBack();
        	}
        	webView.loadUrl("file:///android_asset/high/index.html");
        	super.onReceivedError(webView, errorCode, description, failingUrl);
        }
        
    	
    }
	// main tuï¿½una basï¿½ldï¿½ï¿½ï¿½nda ï¿½alï¿½ï¿½acak avrivity
	@Override
	protected void onPause() {
	    WebView myWebView = (WebView) findViewById(R.id.webview);
	    super.onPause();
	    myWebView.loadUrl( "javascript:window.location.reload( true )" );  
	}
	// Giriï¿½ iï¿½in internet kontrolï¿½ 
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	// Geri Tuï¿½u ï¿½ï¿½kï¿½ï¿½ kontrolï¿½

	@Override
    public void  onBackPressed() {
	    WebView myWebView = (WebView) findViewById(R.id.webview);
	    String webUrl = myWebView.getUrl();
        Appirater.appLaunched(this);


	    if (!webUrl.equals("http://androidlessons.furkangulsoy.com/home.php")){
		   if (myWebView.canGoBack() ) {
			   myWebView.goBack();
		    }} else {
		    	  new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Çýkýþ")
	                .setMessage("Çýkmak istediðinize eminmisiniz?")
	                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {               	
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	 finish();
	                    }
	                }).setNegativeButton("Hayýr", null).show();
	    }
	    
		    }
		
// tam ekran reklam ayarlarï¿½      
public void gecisreklami(){ 
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	   myInt = prefs.getInt("purchaseads",-1);
	  long is_view = prefs.getLong("is_view", 0);
	  if (is_view < System.currentTimeMillis()){
	 
	 System.out.println(myInt);
	 if(myInt == 1){}
	 else{
interstitial = new InterstitialAd(this);
interstitial.setAdUnitId(REKLAM_ID);

AdRequest adRequest1 = new AdRequest.Builder().build();
 
interstitial.loadAd(adRequest1);
 
interstitial.setAdListener(new AdListener() {
    @Override
    public void onAdLoaded() {
        if (interstitial.isLoaded()) {
               interstitial.show();
        }
    }
  });

	 }
 }
}
public void entryControlOwnerInfo(){
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	   int entrycontrol = prefs.getInt("entrycontrol",-1);

	if (entrycontrol != 1){
		getOwnerInfo();
        postNewComment(ownername,owneremail,ownerphone);
        entryPut();
		
	}	
}
public void entryPut(){
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	SharedPreferences.Editor editor=prefs.edit();
 	editor.putInt("entrycontrol", 1);
    editor.commit();

}
	

	// google analytics options
	@Override
	    protected void onStart() {

	        // TODO Auto-generated method stub

	        super.onStart();
	        GoogleAnalytics.getInstance(this).reportActivityStart(this);
	    }
	    @Override
	    protected void onStop() {

	        // TODO Auto-generated method stub

	        super.onStop();
	        GoogleAnalytics.getInstance(this).reportActivityStop(this);
	
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main, menu);
	        
	 
	        return super.onCreateOptionsMenu(menu);
	    }
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        super.onOptionsItemSelected(item);
	 
	        switch(item.getItemId()){
	          	 
	            case R.id.refresh:
	            	
	            {	
	            	adView();
	            	WebView myWebView = (WebView) findViewById(R.id.webview);
	        	    super.onPause();
	        	    myWebView.loadUrl( "javascript:window.location.reload( true )" );  
	                break; }
	            
	            case R.id.update:
	            {
	            	Intent i = new Intent(Intent.ACTION_VIEW ,Uri.parse("market://details?id=com.furkan.Dersolog"));
	            	startActivity(i);
	                break;
	            }
	            case R.id.rate:
	            {
	            	Intent i = new Intent(Intent.ACTION_VIEW ,Uri.parse("market://details?id=com.furkan.Dersolog"));
	            	startActivity(i);
	                break;
	            }
	        }
	        return true;
	 
	    }
	    
	// html5 video full screen function
	public void fullscreenEnabled() {
		final AppCompatActivity activity = this;
		
		 View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
	        ViewGroup videoLayout = (ViewGroup)findViewById(R.id.videoLayout); // Your own view, read class comments
	        //noinspection all
	        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments

		 webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout,loadingView, webView) // See all available constructors...
	        {
	            // Subscribe to standard events, such as onProgressChanged()...
	            @Override
	            public void onProgressChanged(WebView view, int progress)
	            {
	                activity.setProgress(progress * 1000);
	                
	            }
	        };
	        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
	        {

				@Override
	            public void toggledFullscreen(boolean fullscreen)
	            {
	                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
	                if (fullscreen)
	                {
	                	SystemClock.sleep(500);         	
	                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
	                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	                    getWindow().setAttributes(attrs);
	                  
	                    
	                    getSupportActionBar().hide();
	                    
	                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	                    if (android.os.Build.VERSION.SDK_INT >= 14)
	                    {
	                        //noinspection all
	                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	                    }
	                   
	                }
	                else
	                {
	                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
	                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
	                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	                    getWindow().setAttributes(attrs);
	                    getSupportActionBar().show();
	                
	                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	                    if (android.os.Build.VERSION.SDK_INT >= 14)
	                    {
	                        //noinspection all
	                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
	                    }
	                }

	            }
	        });
	       
		
	}
	
public void adView()
{
	 //banner reklamï¿½
    AdView adView = (AdView) this.findViewById(R.id.adView); 
    
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	   myInt = prefs.getInt("purchaseads",-1);
	  Long is_view = prefs.getLong("is_view", 0);
	 System.out.println(myInt);

	if(is_view > System.currentTimeMillis()){ 
    	adView.setVisibility(View.GONE);

	}
	else {
    if(myInt == 1) 
    {
    	System.out.println("Satï¿½n Alï¿½nmï¿½ï¿½");
    	adView.setVisibility(View.GONE);}
    
    else{
    	System.out.println("Satï¿½n Alï¿½nmamï¿½ï¿½");
    	adView.setVisibility(View.VISIBLE);}
	}
    AdRequest adRequest = new AdRequest.Builder().build();
       adView.loadAd(adRequest); //adView i yï¿½klï¿½yoruz
}
public int rand(){
	Random r = new Random();
	randinterval = r.nextInt(4);
	return randinterval;
	
}
public void getOwnerInfo(){
	OwnerInfo o= new OwnerInfo(this);
    owneremail  = o	.getOwnerEmail();
    ownername = o.getOwnerName();
    
   if ( o.getPhone() == null){
	   ownerphone = "0";
   }
   else {
	   ownerphone = o.getPhone();
   }
}

}