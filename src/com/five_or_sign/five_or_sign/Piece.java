package com.five_or_sign.five_or_sign;

import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

public class Piece {
	enum pieceType {
		PLAYER0, PLAYER1, EMPTY, BLOCKED
	};

	pieceType type;
	boolean player;
	Button button;
	LayoutParams params;

	// If the current piece is not marked this will set it marked
	public void setMarked(pieceType t) {
		this.type = t;

		if (this.type == pieceType.PLAYER0) {
			button.setBackgroundResource(R.drawable.x);
		} else if (this.type == pieceType.PLAYER1) {
			button.setBackgroundResource(R.drawable.o);
		} else if (this.type == pieceType.EMPTY) {
			button.setBackgroundResource(R.drawable.tablepiece);
		} else if (this.type == pieceType.BLOCKED) {
			button.setBackgroundResource(R.drawable.blockedpiece);
		}
	}
}
