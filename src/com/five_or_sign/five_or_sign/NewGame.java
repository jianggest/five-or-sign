package com.five_or_sign.five_or_sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewGame extends Activity {
	/** Called when the NewGame activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgame);

		final Button button = (Button) findViewById(R.id.buttonBackFromNewGame);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Closing NewGame activity
				finish();
			}
		});

		final Button button1 = (Button) findViewById(R.id.buttonMultiplayer);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Starting multiplayer game
				Intent myintent = new Intent(v.getContext(), Game.class);
				myintent.putExtra("type", false);
				myintent.putExtra("level", "empty");
				startActivity(myintent);
			}
		});

		final Button button2 = (Button) findViewById(R.id.buttonCustomGame);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Starting custom game
				Intent myintent = new Intent(v.getContext(), Game.class);
				myintent.putExtra("type", true);
				myintent.putExtra("level", "empty");
				startActivity(myintent);
			}
		});

	}
}