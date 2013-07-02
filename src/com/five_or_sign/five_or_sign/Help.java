package com.five_or_sign.five_or_sign;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Help extends Activity {
	/** Called when the About activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		final Button button = (Button) findViewById(R.id.buttonBackFromHelp);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		final TextView tw = (TextView) findViewById(R.id.helpTextView);
		tw.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
}