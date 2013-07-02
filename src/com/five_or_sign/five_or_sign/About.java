package com.five_or_sign.five_or_sign;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity {
	/** Called when the about activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		final Button button = (Button) findViewById(R.id.buttonBack);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Closing about activity
				finish();
			}
		});

		final TextView tw = (TextView) findViewById(R.id.textViewAboutText);
		tw.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
}
