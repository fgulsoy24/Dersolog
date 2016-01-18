package com.furkan.Dersolog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;

public class RemoveAdsActivity extends AppCompatActivity {
	private static final String TAG = "com.furkan.Dersolog.inappbilling";
	static final String ITEM_SKU = "com.furkan.donate";
	static final String ITEM_DONATE = "com.furkan.removeads";
	private Preference buyclick = null;
	private int purchaseads;
	IabHelper mHelper;
	IInAppBillingService mService;
	Context mContext;
	private Toolbar mToolbar;

	@Override
	   protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     
     setContentView(R.layout.activity_removeads);
     setTitle("Reklam Ayarları");
     mToolbar = (Toolbar) findViewById(R.id.toolbar);
     mToolbar.setTitleTextColor(Color.WHITE);
     setSupportActionBar(mToolbar);
    
     getSupportActionBar().setDisplayShowHomeEnabled(true);
     getSupportActionBar().setLogo(R.drawable.actionbarlogo);
     getSupportActionBar().setDisplayUseLogoEnabled(true);
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
     TextView thanks = (TextView) findViewById(R.id.thankstextview);
     TextView summary = (TextView)findViewById(R.id.summary);
      TextView title = (TextView)findViewById(R.id.title);

		Button button1 = (Button)findViewById(R.id.button1);
		if(1 == prefs.getInt("purchaseads",-1))
		{
			
			button1.setVisibility(View.GONE);
			summary.setVisibility(View.GONE);
			title.setVisibility(View.GONE);
	    	thanks.setVisibility(View.VISIBLE);
	    	}
		else
		{
			button1.setVisibility(View.VISIBLE);
	    	thanks.setVisibility(View.GONE);
	    	summary.setVisibility(View.VISIBLE);
			title.setVisibility(View.VISIBLE);
	    	
		}
		
  	 String base64EncodedPublicKey = 
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmMwMhuMDzks8GGCMaj+9ePYzXNL05xC9X55VnVqH1spAOsfg5Z277BRWn5U8q5dSB71iu+wNVUJJ2xlNbt5usP5p5E3vik9spp8WzMUxZ6TLRekFXtXcja0hgSJVq7HbMqFPJrIRYqR5VSuSwd+I6tV/cSVOTGTnWhNZFMm14UgO0MyHiepKE/5Fb1zk+u5hFFfS/Xo34+vQc82E4AKDQcSE6VsNP2I5BO3hHYaPpnrv/W7EGR0uasQzI0phLmrQDJAyQW5tYydVOKOXfYNL6puLTKiDkwD3VdBmJZQHKk3oZAr5OvNMwmTAO24gRYlXsC/giC/Kt2OSSVe9/WRqUQIDAQAB";

mHelper = new IabHelper(this, base64EncodedPublicKey);


mHelper.startSetup(new 
IabHelper.OnIabSetupFinishedListener() {
	
public void onIabSetupFinished(IabResult result) 
{
	
if (!result.isSuccess()) {
Log.d(TAG, "In-app Billing setup failed: " + 
result);
} else {

 Log.d(TAG, "In-app Billing is set up OK");
}

}
});
}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
	= new IabHelper.OnIabPurchaseFinishedListener() {
		
	public void onIabPurchaseFinished(IabResult result, 
                    Purchase purchase) 
	{
	   if (result.isFailure()) {
	    	  alert("" + result);

	      return;
	 }      
	 else if (purchase.getSku().equals(ITEM_SKU)) {
		 purchased(1);
		 consumeItem();
	
	}
	      
   }
};
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("Tamam", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
            case android.R.id.home:
            			onRestart(); 
                      	onBackPressed();
            }
            return true;
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, 
	     Intent data) 
	{
	      if (!mHelper.handleActivityResult(requestCode, 
	              resultCode, data)) {     
	    	super.onActivityResult(requestCode, resultCode, data);
	      }
	}
	public void consumeItem() {
		mHelper.queryInventoryAsync(mReceivedInventoryListener);
		
	}
		
	IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener 
	   = new IabHelper.QueryInventoryFinishedListener() {
		   public void onQueryInventoryFinished(IabResult result,
		      Inventory inventory) {  
		      if (result.isFailure()) {
		    	   	  alert(""+result);
		      } else {
	                 mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), 
				mConsumeFinishedListener);
		      }
	    }
	};
	 
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
			  new IabHelper.OnConsumeFinishedListener() {
			   

			public void onConsumeFinished(Purchase purchase, 
		             IabResult result) {

			 if (result.isSuccess()) {		
				    alert("Satın Aldığınız için Teşekkür Ederiz." );
			        System.out.println("purchaseads");
			 } else {
				    alert("Başarısız." ); // handle error
			 }
		  }
		};
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}
	
	public void buyClick(View view) {
		
     	        	mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,   
	     			   mPurchaseFinishedListener, "mypurchasetoken");
		
      		     
}
	public void donateClick(View view) {
     	mHelper.launchPurchaseFlow(this, ITEM_DONATE, 10001,   
			   mPurchaseFinishedListener, "mypurchasetoken");
	     
}
	
	public void purchased(int a){
		 SharedPreferences removeads =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());	
		    SharedPreferences.Editor editor=removeads.edit();
     	editor.putInt("purchaseads", a);
	        editor.commit();
	}
}
