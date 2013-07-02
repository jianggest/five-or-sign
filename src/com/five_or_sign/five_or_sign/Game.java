package com.five_or_sign.five_or_sign;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.content.res.Resources;

import com.five_or_sign.five_or_sign.Piece;
import com.five_or_sign.five_or_sign.AI;
import com.five_or_sign.five_or_sign.Position;
import com.five_or_sign.five_or_sign.SignFinder;

public class Game extends Activity {
	private boolean player;
	private boolean gameEnded;
	private boolean undone;
	private boolean humanX;
	private boolean countdown;

	private int type;
	private Piece[][] pieces;
	private int lastX;
	private int lastY;
	private int lastAIX;
	private int lastAIY;
	private int lastPlayerX;
	private int lastPlayerY;
	private int clickType;
	private int countdownCounter;
	private int countdownRate;
	private CharSequence map;

	//Direction types
	enum direction {
		ERROR, VERTICAL, HORIZONTAL, LEFT_TRANS, RIGHT_TRANS
	};

	AI ai;
	SignFinder signFinder;

	//Return class of the isGameEnded() function
	class center {
		//Center of the line
		int x;
		int y;
		direction d;
	}

	private void setCountdownRate(int i) {
		countdownCounter = countdownCounter / countdownRate * i;
		countdownRate = i;
	}

	// Activating/Deactivating the countdown
	private void activateCountdown(boolean i) {
		if (i == true) {
			countdown = true;
		} else {
			countdown = false;
		}
	}

	// Placing concentrical frames (only 1/turn) from blocks
	private void newBlockFrame() {
		if (countdown == true) {
			if (countdownCounter % countdownRate == 0) {
				for (int i = 0; i <= countdownCounter / countdownRate; i++) {
					for (int j = 0; j < 13; j++) {
						// Removing overwrote pieces from ai and signFinder
						if (pieces[i][j].type == Piece.pieceType.PLAYER0) {
							signFinder.deleteX(i, j);
							if (type == 1) {
								if (humanX == false) {
									ai.deleteEnemyPiece(i, j);
								} else {
									ai.deleteAIPiece(i, j);
								}
							}
						} else if (pieces[i][j].type == Piece.pieceType.PLAYER1) {
							signFinder.deleteO(i, j);
							if (type == 1) {
								if (humanX == true) {
									ai.deleteEnemyPiece(i, j);
								} else {
									ai.deleteAIPiece(i, j);
								}
							}
						}
						if (pieces[j][j].type == Piece.pieceType.PLAYER0) {
							signFinder.deleteX(j, i);
							if (type == 1) {
								if (humanX == false) {
									ai.deleteEnemyPiece(j, i);
								} else {
									ai.deleteAIPiece(j, i);
								}
							}
						} else if (pieces[j][i].type == Piece.pieceType.PLAYER1) {
							signFinder.deleteO(j, i);
							if (type == 1) {
								if (humanX == true) {
									ai.deleteEnemyPiece(j, i);
								} else {
									ai.deleteAIPiece(j, i);
								}
							}
						}

						if (pieces[i][j].type != Piece.pieceType.BLOCKED) {
							ai.recordNewBlock(new Position(i, j));
						}
						if (pieces[j][i].type != Piece.pieceType.BLOCKED) {
							ai.recordNewBlock(new Position(j, i));
						}
						
						pieces[i][j].setMarked(Piece.pieceType.BLOCKED);
						signFinder.addBlock(i, j);
						pieces[j][i].setMarked(Piece.pieceType.BLOCKED);
						signFinder.addBlock(j, i);
					}
				}
				for (int i = 12; i >= 12 - countdownCounter / countdownRate; i--) {
					for (int j = 0; j < 13; j++) {
						// Removing overwrote pieces from ai and signFinder
						if (pieces[i][j].type == Piece.pieceType.PLAYER0) {
							signFinder.deleteX(i, j);
							if (type == 1) {
								if (humanX == false) {
									ai.deleteEnemyPiece(i, j);
								} else {
									ai.deleteAIPiece(i, j);
								}
							}
						} else if (pieces[i][j].type == Piece.pieceType.PLAYER1) {
							signFinder.deleteO(i, j);
							if (type == 1) {
								if (humanX == true) {
									ai.deleteEnemyPiece(i, j);
								} else {
									ai.deleteAIPiece(i, j);
								}
							}
						}
						if (pieces[j][j].type == Piece.pieceType.PLAYER0) {
							signFinder.deleteX(j, i);
							if (type == 1) {
								if (humanX == false) {
									ai.deleteEnemyPiece(j, i);
								} else {
									ai.deleteAIPiece(j, i);
								}
							}
						} else if (pieces[j][i].type == Piece.pieceType.PLAYER1) {
							signFinder.deleteO(j, i);
							if (type == 1) {
								if (humanX == true) {
									ai.deleteEnemyPiece(j, i);
								} else {
									ai.deleteAIPiece(j, i);
								}
							}
						}

						if (pieces[i][j].type != Piece.pieceType.BLOCKED) {
							ai.recordNewBlock(new Position(i, j));
						}
						if (pieces[j][i].type != Piece.pieceType.BLOCKED) {
							ai.recordNewBlock(new Position(j, i));
						}
						
						pieces[i][j].setMarked(Piece.pieceType.BLOCKED);
						signFinder.addBlock(i, j);
						pieces[j][i].setMarked(Piece.pieceType.BLOCKED);
						signFinder.addBlock(j, i);
					}
				}
			}
			countdownCounter++;
		}
	}

	private void undo() {
		if (!undone) {
			undone = true;
			final Button undoButton = (Button) findViewById(R.id.undoButton);
			undoButton.setVisibility(Button.INVISIBLE);
			if (type != 1) {
				player = !player;
				pieces[lastX][lastY].setMarked(Piece.pieceType.EMPTY);
			} else {
				pieces[lastAIX][lastAIY].setMarked(Piece.pieceType.EMPTY);
				ai.deleteAIPiece(lastAIX, lastAIY);
				pieces[lastPlayerX][lastPlayerY]
						.setMarked(Piece.pieceType.EMPTY);
				ai.deleteEnemyPiece(lastPlayerY, lastPlayerY);
			}
		}
	}

	private void endGame(center c) {
		// Draw the line
		if (c.d == direction.HORIZONTAL) {
			for (int i = c.x - 2; i < c.x + 3; i++) {
				if (player == true) {
					pieces[i][c.y].button.setBackgroundResource(R.drawable.oh);
				} else {
					pieces[i][c.y].button.setBackgroundResource(R.drawable.xh);
				}
			}
		} else if (c.d == direction.VERTICAL) {
			for (int i = c.y - 2; i < c.y + 3; i++) {
				if (player == true) {
					pieces[c.x][i].button.setBackgroundResource(R.drawable.ov);
				} else {
					pieces[c.x][i].button.setBackgroundResource(R.drawable.xv);
				}
			}
		} else if (c.d == direction.RIGHT_TRANS) {
			for (int i = -2; i < 3; i++) {
				if (player == true) {
					pieces[c.x + i][c.y + i].button
							.setBackgroundResource(R.drawable.ort);
				} else {
					pieces[c.x + i][c.y + i].button
							.setBackgroundResource(R.drawable.xrt);
				}
			}
		} else if (c.d == direction.LEFT_TRANS) {
			for (int i = -2; i < 3; i++) {
				if (player == true) {
					pieces[c.x - i][c.y + i].button
							.setBackgroundResource(R.drawable.olt);
				} else {
					pieces[c.x - i][c.y + i].button
							.setBackgroundResource(R.drawable.xlt);
				}
			}
		}
		Intent myintent = new Intent(this, Winner.class);
		myintent.putExtra("winner", player);
		startActivity(myintent);
	}

	// Checking game end situation
	private center isGameEnded() {
		boolean value = false;
		int j = 0;
		center c = new center();
		int tmpX = 0;
		int tmpY = 0;
		for (int i = 0; i < 13; i++) {
			if ((pieces[i][lastY].type == Piece.pieceType.PLAYER0 && player == false)
					|| (pieces[i][lastY].type == Piece.pieceType.PLAYER1 && player == true)) {
				j++;
			} else {
				j = 0;
			}
			if (j == 3) {
				tmpX = i;
				tmpY = lastY;
			}
			if (j == 5) {
				value = true;
				c.d = direction.HORIZONTAL;
				c.x = tmpX;
				c.y = tmpY;
			}
		}

		j = 0;
		for (int i = 0; i < 13; i++) {
			if ((pieces[lastX][i].type == Piece.pieceType.PLAYER0 && player == false)
					|| (pieces[lastX][i].type == Piece.pieceType.PLAYER1 && player == true)) {
				j++;
			} else {
				j = 0;
			}
			if (j == 3) {
				tmpX = lastX;
				tmpY = i;
			}
			if (j == 5) {
				value = true;
				c.d = direction.VERTICAL;
				c.x = tmpX;
				c.y = tmpY;
			}
		}

		for (int i = 0; i < 13; i++) {
			for (int k = 0; k < 13; k++) {
				int step = i;
				j = 0;
				for (int l = k; l < 13; l++) {
					if (step < 13) {
						if ((pieces[l][step].type == Piece.pieceType.PLAYER0 && player == false)
								|| (pieces[l][step].type == Piece.pieceType.PLAYER1 && player == true)) {
							j++;
						} else {
							j = 0;
						}
						if (j == 3) {
							tmpX = l;
							tmpY = step;
						}
					}
					if (j == 5) {
						value = true;
						c.d = direction.RIGHT_TRANS;
						c.x = tmpX;
						c.y = tmpY;
					}
					step++;
				}
			}
		}
		for (int i = 0; i < 13; i++) {
			for (int k = 12; k >= 0; k--) {
				int step = i;
				j = 0;
				for (int l = k; l >= 0; l--) {
					if (step < 13) {
						if ((pieces[l][step].type == Piece.pieceType.PLAYER0 && player == false)
								|| (pieces[l][step].type == Piece.pieceType.PLAYER1 && player == true)) {
							j++;
						} else {
							j = 0;
						}
						if (j == 3) {
							tmpX = l;
							tmpY = step;
						}
					}
					if (j == 5) {
						value = true;
						c.d = direction.LEFT_TRANS;
						c.x = tmpX;
						c.y = tmpY;
					}
					step++;
				}
			}
		}
		if (value == false) {
			c.d = direction.ERROR;
		}
		return c;
	}

	// Set mark on a piece
	private void normalClick(int x, int y) {
		if (pieces[x][y].type == Piece.pieceType.EMPTY && gameEnded == false) {
			lastX = x;
			lastY = y;
			if (player) {
				pieces[x][y].setMarked(Piece.pieceType.PLAYER1);
				signFinder.addO(x, y);
			} else {
				pieces[x][y].setMarked(Piece.pieceType.PLAYER0);
				signFinder.addX(x, y);
			}

			if (type == 0) {
				final Button undoButton = (Button) findViewById(R.id.undoButton);
				undoButton.setVisibility(Button.VISIBLE);
				undone = false;
			} else if ((type == 1 && player == false && humanX == false)
					|| (type == 1 && player == true && humanX == true)) {
				Position pos = new Position(x, y);
				lastPlayerX = x;
				lastPlayerY = y;
				ai.recordEnemyMove(pos);

				final Button undoButton = (Button) findViewById(R.id.undoButton);
				undoButton.setVisibility(Button.VISIBLE);
				undone = false;
			}

			center tmp = isGameEnded();
			if (tmp.d != direction.ERROR) {
				final Button undoButton = (Button) findViewById(R.id.undoButton);
				undoButton.setVisibility(Button.INVISIBLE);
				gameEnded = true;
				endGame(tmp);
			}

			if (gameEnded == false) {
				if (player == true) {
					player = false;
				} else {
					player = true;
				}
			}
		}

		newBlockFrame();
	}

	private void giveControlToAI() {
		Position pos = ai.calculateMove(0);
		lastAIX = pos.x;
		lastAIY = pos.y;
		userClick(pos.x, pos.y);
	}

	// User click handling
	private void userClick(int x, int y) {
		if (clickType == 0) {
			normalClick(x, y);
		}

		if (clickType == 1) {
			putBlockSign(x, y);
		}

		if (clickType == 2) {
			replaceEnemyPiece(x, y);
		}

		if (gameEnded == false) {
			int[][] sign;
			Position p1 = new Position();
			p1.x = 0;
			while (p1.x != -1) {
				sign = new int[3][3];

				sign[0][0] = 0;
				sign[0][1] = 1;
				sign[0][2] = 0;
				sign[1][0] = 1;
				sign[1][1] = 0;
				sign[1][2] = 1;
				sign[2][0] = 0;
				sign[2][1] = 1;
				sign[2][2] = 0;
				p1 = signFinder.findSign(sign, 0, sign.length, sign[0].length);
				if (p1.x != -1) {
					clickType = 1;

					if (player == true) {
						player = false;
					} else {
						player = true;
					}
				}

				if (p1.x == -1) {
					p1 = signFinder.findSign(sign, 1, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						clickType = 1;

						if (player == true) {
							player = false;
						} else {
							player = true;
						}
					}
				}

				//

				if (p1.x == -1) {
					sign = new int[3][3];

					sign[0][0] = 0;
					sign[0][1] = 1;
					sign[0][2] = 0;
					sign[1][0] = 1;
					sign[1][1] = 0;
					sign[1][2] = 1;
					sign[2][0] = 1;
					sign[2][1] = 0;
					sign[2][2] = 1;

					p1 = signFinder.findSign(sign, 0, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						clearTheTableSign();
					}
				}

				if (p1.x == -1) {
					sign = new int[3][3];

					sign[0][0] = 0;
					sign[0][1] = 1;
					sign[0][2] = 0;
					sign[1][0] = 1;
					sign[1][1] = 0;
					sign[1][2] = 1;
					sign[2][0] = 1;
					sign[2][1] = 0;
					sign[2][2] = 1;

					p1 = signFinder.findSign(sign, 1, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						clearTheTableSign();
					}
				}

				//

				if (p1.x == -1) {
					sign = new int[2][4];

					sign[0][0] = 1;
					sign[0][1] = 0;
					sign[0][2] = 1;
					sign[0][3] = 0;
					sign[1][0] = 0;
					sign[1][1] = 1;
					sign[1][2] = 0;
					sign[1][3] = 1;

					p1 = signFinder.findSign(sign, 0, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						if (player == true) {
							player = false;
						} else {
							player = true;
						}
					}
				}

				if (p1.x == -1) {
					sign = new int[2][4];

					sign[0][0] = 1;
					sign[0][1] = 0;
					sign[0][2] = 1;
					sign[0][3] = 0;
					sign[1][0] = 0;
					sign[1][1] = 1;
					sign[1][2] = 0;
					sign[1][3] = 1;

					p1 = signFinder.findSign(sign, 1, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						if (player == true) {
							player = false;
						} else {
							player = true;
						}
					}
				}

				//

				if (p1.x == -1) {
					sign = new int[4][2];

					sign[0][0] = 1;
					sign[0][1] = 0;
					sign[1][0] = 0;
					sign[1][1] = 1;
					sign[2][0] = 1;
					sign[2][1] = 0;
					sign[3][0] = 0;
					sign[3][1] = 1;

					p1 = signFinder.findSign(sign, 0, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						if (player == true) {
							player = false;
						} else {
							player = true;
						}

						boolean suc = false;
						while (suc != true) {
							Random random = new Random(
									System.currentTimeMillis());
							int rX = random.nextInt(13);
							int rY = random.nextInt(13);
							if (pieces[rX][rY].type == Piece.pieceType.EMPTY) {
								suc = true;
								normalClick(rX, rY);
							}
						}
					}
				}

				if (p1.x == -1) {
					sign = new int[4][2];

					sign[0][0] = 1;
					sign[0][1] = 0;
					sign[1][0] = 0;
					sign[1][1] = 1;
					sign[2][0] = 1;
					sign[2][1] = 0;
					sign[3][0] = 0;
					sign[3][1] = 1;

					p1 = signFinder.findSign(sign, 1, sign.length,
							sign[0].length);
					if (p1.x != -1) {
						if (player == true) {
							player = false;
						} else {
							player = true;
						}

						boolean suc = false;
						while (suc != true) {
							Random random = new Random(
									System.currentTimeMillis());
							int rX = random.nextInt(13);
							int rY = random.nextInt(13);
							if (pieces[rX][rY].type == Piece.pieceType.EMPTY) {
								suc = true;
								normalClick(rX, rY);
							}
						}
					}
				}

				//

				if (p1.x == -1) {
					sign = new int[3][2];

					sign[0][0] = 1;
					sign[0][1] = 1;
					sign[1][0] = 1;
					sign[1][1] = 1;
					sign[2][0] = 1;
					sign[2][1] = 0;

					p1 = signFinder.findSign(sign, 0, sign.length,
							sign[0].length);

					if (p1.x != -1) {
						boolean tempSide = player;
						boolean suc = false;
						while (suc != true) {
							Random random = new Random(
									System.currentTimeMillis());
							int rX = random.nextInt(13);
							int rY = random.nextInt(13);

							player = random.nextBoolean();
							if (pieces[rX][rY].type == Piece.pieceType.EMPTY) {
								suc = true;
								normalClick(rX, rY);
								player = tempSide;
							}
						}
					}
				}

				if (p1.x == -1) {
					sign = new int[3][2];

					sign[0][0] = 1;
					sign[0][1] = 1;
					sign[1][0] = 1;
					sign[1][1] = 1;
					sign[2][0] = 1;
					sign[2][1] = 0;

					p1 = signFinder.findSign(sign, 1, sign.length,
							sign[0].length);

					if (p1.x != -1) {
						boolean tempSide = player;
						boolean suc = false;
						while (suc != true) {
							Random random = new Random(
									System.currentTimeMillis());
							int rX = random.nextInt(13);
							int rY = random.nextInt(13);

							player = random.nextBoolean();
							if (pieces[rX][rY].type == Piece.pieceType.EMPTY) {
								suc = true;
								normalClick(rX, rY);
								player = tempSide;
							}
						}
					}
				}

				//

				if (p1.x == -1) {
					sign = new int[3][3];

					sign[0][0] = 1;
					sign[0][1] = 1;
					sign[0][2] = 1;
					sign[1][0] = 0;
					sign[1][1] = 0;
					sign[1][2] = 0;
					sign[2][0] = 0;
					sign[2][1] = 1;
					sign[2][2] = 0;

					p1 = signFinder.findSign(sign, 0, sign.length,
							sign[0].length);

					if (p1.x != -1) {
						if (player == true) {
							player = false;
						} else {
							player = true;
						}

						clickType = 2;
					}
				}

				if (p1.x == -1) {
					sign = new int[3][3];

					sign[0][0] = 1;
					sign[0][1] = 1;
					sign[0][2] = 1;
					sign[1][0] = 0;
					sign[1][1] = 0;
					sign[1][2] = 0;
					sign[2][0] = 0;
					sign[2][1] = 1;
					sign[2][2] = 0;

					p1 = signFinder.findSign(sign, 1, sign.length,
							sign[0].length);

					if (p1.x != -1) {
						if (player == true) {
							player = false;
						} else {
							player = true;
						}

						clickType = 2;
					}
				}
			}
		}

		if (gameEnded == false && clickType == 0) {
			if ((player == true && type == 1 && humanX == false)
					|| (player == false && type == 1 && humanX == true)) {
				giveControlToAI();
			}
		}

		if (gameEnded == false && clickType == 1) {
			if ((player == true && type == 1 && humanX == false)
					|| (player == false && type == 1 && humanX == true)) {
				giveControlToAIToPutBlock();
			}
		}

		if (gameEnded == false && clickType == 2) {
			if ((player == true && type == 1 && humanX == false)
					|| (player == false && type == 1 && humanX == true)) {
				Position p2 = ai.replaceEnemyPiece();
				replaceEnemyPiece(p2.x, p2.y);
			}
		}
	}

	private void replaceEnemyPiece(int x, int y) {
		if (player == false && pieces[x][y].type == Piece.pieceType.PLAYER1) {
			signFinder.deleteO(x, y);
			if (type == 1) {
				if (humanX == false) {
					ai.deleteAIPiece(x, y);
				} else {
					ai.deleteEnemyPiece(x, y);
				}
			}
			clickType = 0;
			pieces[x][y].setMarked(Piece.pieceType.EMPTY);
			userClick(x, y);
		}
	}

	private void clearTheTableSign() {
		if (type == 1) {
			ai = new AI();
			ai.init();
		}
		signFinder = new SignFinder();

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				pieces[i][j].setMarked(Piece.pieceType.EMPTY);
			}
		}
		countdownCounter = 0;
	}

	private void putBlockSign(int x, int y) {
		if (pieces[x][y].type == Piece.pieceType.EMPTY && gameEnded == false) {
			Position pos = new Position(x, y);
			if (type == 1) {
				ai.recordNewBlock(pos);
			}
			signFinder.addBlock(x, y);
			ai.recordNewBlock(new Position(x, y));
			pieces[x][y].setMarked(Piece.pieceType.BLOCKED);
			clickType = 0;

			if (player == true) {
				player = false;
			} else {
				player = true;
			}
		}
	}

	private void giveControlToAIToPutBlock() {
		Position pos = ai.putBlock();
		pieces[pos.x][pos.y].setMarked(Piece.pieceType.BLOCKED);
		ai.recordNewBlock(pos);
		signFinder.addBlock(pos.x, pos.y);

		clickType = 0;
		if (player == true) {
			player = false;
		} else {
			player = true;
		}
	}

	/** Called when the Game activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		AlertDialog picker = null;

		// Get game type
		Intent sender = getIntent();
		if (sender.getBooleanExtra("type", true) == false) {
			type = 0;
		} else if (sender.getBooleanExtra("type", true) == true) {
			type = 1;
		}

		clickType = 0;

		player = false;
		gameEnded = false;
		undone = false;
		countdown = false;
		countdownCounter = 0;
		countdownRate = 2;

		signFinder = new SignFinder();

		//activateCountdown(true);

		if (type == 1) {
			ai = new AI();
			ai.init();

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Which player are you?")
					.setCancelable(false)
					.setPositiveButton("X",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									humanX = false;
								}
							})
					.setNegativeButton("O",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									humanX = true;
									Position pos = ai.calculateMove(0);
									userClick(pos.x, pos.y);
								}
							});
			picker = builder.create();
		}

		// Get screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int size = 0;
		int size1 = 0;
		int size2 = 0;
		// Checking screen size orientation
		if (metrics.widthPixels < metrics.heightPixels) {
			size = metrics.widthPixels / 13; // Calculate size of a piece
			size1 = 0;
			size2 = (metrics.heightPixels - 13 * size) / 2;
		} else {
			size = metrics.heightPixels / 13;
			size1 = (metrics.widthPixels - 13 * size) / 2;
			size2 = 0;
		}

		Resources res = getResources();
		String[] gameMap;
		if (sender.getStringExtra("level") == "empty") {
			gameMap = res.getStringArray(R.array.empty_level);
		} else {
			gameMap = res.getStringArray(R.array.empty_level);
		}

		// Creating UI
		RelativeLayout l = (RelativeLayout) findViewById(R.id.gameLayout);
		pieces = new Piece[13][13];
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				// Init buttons and set their properties
				pieces[i][j] = new Piece();

				pieces[i][j].player = false;
				pieces[i][j].button = new Button(this);
				pieces[i][j].params = new LayoutParams(size, size);
				pieces[i][j].params.setMargins(i * size + size1, j * size
						+ size2, 0, 0);

				if (gameMap[i].charAt(j) == 'E') {
					pieces[i][j].setMarked(Piece.pieceType.EMPTY);
				} else if (gameMap[i].charAt(j) == '0') {
					pieces[i][j].setMarked(Piece.pieceType.PLAYER0);
				} else if (gameMap[i].charAt(j) == '1') {
					pieces[i][j].setMarked(Piece.pieceType.PLAYER1);
				} else if (gameMap[i].charAt(j) == 'B') {
					pieces[i][j].setMarked(Piece.pieceType.BLOCKED);
				}

				l.addView(pieces[i][j].button, pieces[i][j].params);

				// Handling clicks
				final int x = i;
				final int y = j;

				pieces[i][j].button
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								userClick(x, y);
							}
						});
			}
			final Button undoButton = (Button) findViewById(R.id.undoButton);
			undoButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					undo();
				}
			});
			undoButton.setVisibility(Button.INVISIBLE);

			final Button button = (Button) findViewById(R.id.cancelButton);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// Closing the activity
					finish();
				}
			});

			if (type == 1) {
				picker.show();
			}
		}
	}
}