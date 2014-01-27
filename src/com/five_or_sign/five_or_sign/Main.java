package com.five_or_sign.five_or_sign;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class Main extends Activity {
	/** Called when the Main activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button button = (Button) findViewById(R.id.buttonAbout);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Starting About activity
				Intent myintent = new Intent(v.getContext(), About.class); 
				startActivity(myintent);
			}
		});

		final Button button2 = (Button) findViewById(R.id.buttonExit);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Closing Main activity
				finish();
			}
		});
		
		final Button button3 = (Button) findViewById(R.id.buttonHelp);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Starting Help activity
				Intent myintent = new Intent(v.getContext(), Help.class); 
				startActivity(myintent);
			}
		});

		final Button button4 = (Button) findViewById(R.id.buttonNewGame);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Starting NewGame activity
				Intent myintent = new Intent(v.getContext(), NewGame.class); 
				startActivity(myintent);
			}
		});
	}
}