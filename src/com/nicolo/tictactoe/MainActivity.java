package com.nicolo.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

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

	// "One Player" Button listener
	public void singleGame(View view){
		Toast.makeText(getApplicationContext(), getResources().getString(R.string.notReady), Toast.LENGTH_LONG).show();
	}
	
	// "Two Players" Button listener
	public void multiGame(View view){
		Intent intent = new Intent(this,GameActivity.class);
		startActivity(intent);
	}

}
