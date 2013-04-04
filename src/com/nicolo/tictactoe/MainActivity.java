package com.nicolo.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String ENABLE_AI = "com.nicolo.tictactoe.AI";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	 public boolean onOptionsItemSelected(MenuItem mi){
		 Intent intent = new Intent(this,SettingsActivity.class);
		 startActivity(intent);
		 return true;
	 }
	 
	// "One Player" Button listener
	public void singleGame(View view){
		Intent intent = new Intent(this,GameActivity.class);
		intent.putExtra(ENABLE_AI, true);
		startActivity(intent);
	}
	
	// "Two Players" Button listener
	public void multiGame(View view){
		Intent intent = new Intent(this,GameActivity.class);
		intent.putExtra(ENABLE_AI, false);
		startActivity(intent);
	}

	 private void getDimens(){
	    	//Determine screen size
	        if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {     
	            Toast.makeText(this, "Large screen",Toast.LENGTH_LONG).show();

	        }
	        else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {     
	            Toast.makeText(this, "Normal sized screen" , Toast.LENGTH_LONG).show();

	        } 
	        else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
	            Toast.makeText(this, "Small sized screen" , Toast.LENGTH_LONG).show();
	        }
	        else {
	            Toast.makeText(this, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
	        }




	        //Determine density
	        DisplayMetrics metrics = new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(metrics);
	            int density = metrics.densityDpi;

	            if (density==DisplayMetrics.DENSITY_HIGH) {
	                Toast.makeText(this, "DENSITY_HIGH... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
	            }
	            else if (density==DisplayMetrics.DENSITY_MEDIUM) {
	                Toast.makeText(this, "DENSITY_MEDIUM... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
	            }
	            else if (density==DisplayMetrics.DENSITY_LOW) {
	                Toast.makeText(this, "DENSITY_LOW... Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
	            }
	            else {
	                Toast.makeText(this, "Density is neither HIGH, MEDIUM OR LOW.  Density is " + String.valueOf(density),  Toast.LENGTH_LONG).show();
	            }
	    }
}
