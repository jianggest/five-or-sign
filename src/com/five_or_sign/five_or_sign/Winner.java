package com.five_or_sign.five_or_sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Winner extends Activity {
	/** Called when the Winner activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winner);
		Intent sender = getIntent();
		boolean winner = sender.getBooleanExtra("winner", true);
		final ImageView view = (ImageView) findViewById(R.id.winnerImageView);
		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.winnerActivity);
		if (winner == true) {
			view.setImageResource(R.drawable.obig);
			layout.setBackgroundResource(R.drawable.bgwo);
		} else {
			view.setImageResource(R.drawable.xbig);
			layout.setBackgroundResource(R.drawable.bgwx);
		}

		final Button button = (Button) findViewById(R.id.viewTableButton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Closing Winner activity
				finish();
			}
		});
	}
}