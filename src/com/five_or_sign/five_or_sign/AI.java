package com.five_or_sign.five_or_sign;

import com.five_or_sign.five_or_sign.Position;

//Artificial Intelligence for the game
public class AI {
	int[][] tableAI;
	boolean[][] AI;
	boolean[][] enemy;
	boolean[][] blocks;
	int[] values;

	int type;

	public void init() {
		tableAI = new int[13][13];
		AI = new boolean[13][13];
		enemy = new boolean[13][13];
		blocks = new boolean[13][13];
		type = 0;

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				tableAI[i][j] = -10000;
				AI[i][j] = false;
				enemy[i][j] = false;
				blocks[i][j] = false;
			}
		}

		// Weights
		values = new int[5];

		values[0] = 3;
		values[1] = 5;
		values[2] = 8;
		values[3] = 12;
		values[4] = 10000;
	}

	// Finds a sign on the game table
	Position findSign(boolean[][] sign, int width, int height,
			int maxEmptyHole, boolean side) {
		// If side == false : enemy player
		// If side == true : AI
		Position pos = new Position(-1, -1);

		boolean[][] emptyTable = new boolean[width][height];

		boolean finished = false;

		for (int i = 0; i < 13 - width && finished == false; i++) {
			for (int j = 0; j < 13 - height && finished == false; j++) {
				int emptyHoles = maxEmptyHole;
				boolean possible = true;

				for (int m = 0; m < width; m++) {
					for (int n = 0; n < height; n++) {
						emptyTable[m][n] = false;
					}
				}

				for (int k = 0; k < width && finished == false
						&& possible == true && emptyHoles > -1; k++) {
					for (int l = 0; l < height && finished == false
							&& possible == true && emptyHoles > -1; l++) {
						if (type == 0) {
							if (side == false) {
								if (sign[k][l] == true) {
									if (enemy[i + k][j + l] == false
											&& AI[i + k][j + l] == false
											&& blocks[i + k][j + l] == false) {
										emptyHoles--;
										emptyTable[k][l] = true;
									} else if (enemy[i + k][j + l] == false) {
										possible = false;
										for (int m = 0; m < width; m++) {
											for (int n = 0; n < height; n++) {
												emptyTable[m][n] = false;
											}
										}
									}
								}
							} else {
								if (sign[k][l] == true) {
									if (AI[i + k][j + l] == false
											&& enemy[i + k][j + l] == false
											&& blocks[i + k][j + l] == false) {
										emptyHoles--;
										emptyTable[k][l] = true;
									} else if (AI[i + k][j + l] == false) {
										possible = false;
										for (int m = 0; m < width; m++) {
											for (int n = 0; n < height; n++) {
												emptyTable[m][n] = false;
											}
										}
									}
								}
							}
						} else if (type == 1) {
							if (side == false) {
								if (sign[k][l] == true) {
									if (AI[i + k][j + l] == false
											&& blocks[i + k][j + l] == false) {
										emptyHoles--;
										emptyTable[k][l] = true;
									} else if (AI[i + k][j + l] == false) {
										possible = false;
										for (int m = 0; m < width; m++) {
											for (int n = 0; n < height; n++) {
												emptyTable[m][n] = false;
											}
										}
									}
								}
							}
						}
					}
				}

				if (possible == true && emptyHoles > -1) {
					finished = true;
					pos.x = i;
					pos.y = j;
				}
			}
		}

		if (pos.x != -1 && pos.y != -1) {
			int x = -1;
			int y = -1;
			int max = -10001;

			if (type == 0) {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						if (emptyTable[i][j] == true) {
							if (tableAI[pos.x + i][pos.y + j] > max) {
								max = tableAI[pos.x + i][pos.y + j];
								x = pos.x + i;
								y = pos.y + j;
							}
						}
					}
				}
			} else if (type == 1) {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						if (emptyTable[i][j] == true) {
							x = pos.x + i;
							y = pos.y + j;
						}
					}
				}
			}

			pos.x = x;
			pos.y = y;
		}

		return pos;
	}

	// Sets a defined value in a circle of given radius
	void setValueInRadius(int value, int radius, int x, int y) {
		if (y - radius > -1) {
			for (int i = x - radius; i < x + radius + 1; i++) {
				if (i > -1 && i < 13) {
					if (tableAI[i][y - radius] != -10000
							|| tableAI[i][y - radius] < 10000) {
						tableAI[i][y - radius] = tableAI[i][y - radius] + value;
					} else if (tableAI[i][y - radius] < 10000) {
						tableAI[i][y - radius] = value;
					}
				}
			}
		}
		if (y + radius < 13) {
			for (int i = x - radius; i < x + radius + 1; i++) {
				if (i > -1 && i < 13) {
					if (tableAI[i][y + radius] != -10000
							|| tableAI[i][y + radius] < 10000) {
						tableAI[i][y + radius] = tableAI[i][y + radius] + value;
					} else if (tableAI[i][y + radius] < 10000) {
						tableAI[i][y + radius] = value;
					}
				}
			}
		}
		if (x - radius > -1) {
			for (int i = y - radius; i < y + radius + 1; i++) {
				if (i > -1 && i < 13) {
					if (tableAI[i][x - radius] != -10000
							|| tableAI[i][x - radius] < 10000) {
						tableAI[i][x - radius] = tableAI[i][x - radius] + value;
					} else if (tableAI[i][x - radius] < 10000) {
						tableAI[i][x - radius] = value;
					}
				}
			}
		}
		if (x + radius < 13) {
			for (int i = y - radius; i < y + radius + 1; i++) {
				if (i > -1 && i < 13) {
					if (tableAI[i][x + radius] != -10000
							|| tableAI[i][x + radius] < 10000) {
						tableAI[i][x + radius] = tableAI[i][x + radius] + value;
					} else if (tableAI[i][x + radius] < 10000) {
						tableAI[i][x + radius] = value;
					}
				}
			}
		}
	}

	// Calculate the AI's next move
	public Position calculateMove(int t) {
		// if t == 0: every table
		// if t == 1: enemy's tables

		Position pos = new Position();
		pos.x = -1;

		// Predefined signs
		boolean[][] table = new boolean[5][5];

		if (t == 0) {
			table[0][0] = true;
			table[0][1] = false;
			table[0][2] = false;
			table[0][3] = false;
			table[0][4] = false;
			table[1][0] = false;
			table[1][1] = true;
			table[1][2] = false;
			table[1][3] = false;
			table[1][4] = false;
			table[2][0] = false;
			table[2][1] = false;
			table[2][2] = true;
			table[2][3] = false;
			table[2][4] = false;
			table[3][0] = false;
			table[3][1] = false;
			table[3][2] = false;
			table[3][3] = true;
			table[3][4] = false;
			table[4][0] = false;
			table[4][1] = false;
			table[4][2] = false;
			table[4][3] = false;
			table[4][4] = true;
			pos = findSign(table, 5, 5, 1, true);

			if (pos.x == -1) {
				table = new boolean[5][5];
				table[0][0] = false;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = false;
				table[0][4] = true;
				table[1][0] = false;
				table[1][1] = false;
				table[1][2] = false;
				table[1][3] = true;
				table[1][4] = false;
				table[2][0] = false;
				table[2][1] = false;
				table[2][2] = true;
				table[2][3] = false;
				table[2][4] = false;
				table[3][0] = false;
				table[3][1] = true;
				table[3][2] = false;
				table[3][3] = false;
				table[3][4] = false;
				table[4][0] = true;
				table[4][1] = false;
				table[4][2] = false;
				table[4][3] = false;
				table[4][4] = false;
				pos = findSign(table, 5, 5, 1, true);
			}

			if (pos.x == -1) {
				table = new boolean[5][1];
				table[0][0] = true;
				table[1][0] = true;
				table[2][0] = true;
				table[3][0] = true;
				table[4][0] = true;
				pos = findSign(table, 5, 1, 1, true);
			}

			if (pos.x == -1) {
				table = new boolean[1][5];
				table[0][0] = true;
				table[0][1] = true;
				table[0][2] = true;
				table[0][3] = true;
				table[0][4] = true;
				pos = findSign(table, 1, 5, 1, true);
			}
		}

		//

		if (t == 0 || t == 1) {
			if (pos.x == -1) {
				table = new boolean[5][5];
				table[0][0] = true;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = false;
				table[0][4] = false;
				table[1][0] = false;
				table[1][1] = true;
				table[1][2] = false;
				table[1][3] = false;
				table[1][4] = false;
				table[2][0] = false;
				table[2][1] = false;
				table[2][2] = true;
				table[2][3] = false;
				table[2][4] = false;
				table[3][0] = false;
				table[3][1] = false;
				table[3][2] = false;
				table[3][3] = true;
				table[3][4] = false;
				table[4][0] = false;
				table[4][1] = false;
				table[4][2] = false;
				table[4][3] = false;
				table[4][4] = true;
				pos = findSign(table, 5, 5, 1, false);
			}

			if (pos.x == -1) {
				table = new boolean[5][5];
				table[0][0] = false;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = false;
				table[0][4] = true;
				table[1][0] = false;
				table[1][1] = false;
				table[1][2] = false;
				table[1][3] = true;
				table[1][4] = false;
				table[2][0] = false;
				table[2][1] = false;
				table[2][2] = true;
				table[2][3] = false;
				table[2][4] = false;
				table[3][0] = false;
				table[3][1] = true;
				table[3][2] = false;
				table[3][3] = false;
				table[3][4] = false;
				table[4][0] = true;
				table[4][1] = false;
				table[4][2] = false;
				table[4][3] = false;
				table[4][4] = false;
				pos = findSign(table, 5, 5, 1, false);
			}

			if (pos.x == -1) {
				table = new boolean[5][1];
				table[0][0] = true;
				table[1][0] = true;
				table[2][0] = true;
				table[3][0] = true;
				table[4][0] = true;
				pos = findSign(table, 5, 1, 1, false);
			}

			if (pos.x == -1) {
				table = new boolean[1][5];
				table[0][0] = true;
				table[0][1] = true;
				table[0][2] = true;
				table[0][3] = true;
				table[0][4] = true;
				pos = findSign(table, 1, 5, 1, false);
			}
		}

		//

		if (t == 0 || t == 1) {
			if (pos.x == -1) {
				table = new boolean[4][4];
				table[0][0] = true;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = false;
				table[1][0] = false;
				table[1][1] = true;
				table[1][2] = false;
				table[1][3] = false;
				table[2][0] = false;
				table[2][1] = false;
				table[2][2] = true;
				table[2][3] = false;
				table[3][0] = false;
				table[3][1] = false;
				table[3][2] = false;
				table[3][3] = true;
				pos = findSign(table, 4, 4, 1, false);
			}

			if (pos.x == -1) {
				table = new boolean[4][4];
				table[0][0] = false;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = true;
				table[1][0] = false;
				table[1][1] = false;
				table[1][2] = true;
				table[1][3] = false;
				table[2][0] = false;
				table[2][1] = true;
				table[2][2] = false;
				table[2][3] = false;
				table[3][0] = true;
				table[3][1] = false;
				table[3][2] = false;
				table[3][3] = false;
				pos = findSign(table, 4, 4, 1, false);
			}

			if (pos.x == -1) {
				table = new boolean[4][1];
				table[0][0] = true;
				table[1][0] = true;
				table[2][0] = true;
				table[3][0] = true;
				pos = findSign(table, 4, 1, 1, false);
			}

			if (pos.x == -1) {
				table = new boolean[1][4];
				table[0][0] = true;
				table[0][1] = true;
				table[0][2] = true;
				table[0][3] = true;
				pos = findSign(table, 1, 4, 1, false);
			}
		}

		//

		if (t == 0) {
			if (pos.x == -1) {
				table = new boolean[4][4];
				table[0][0] = true;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = false;
				table[1][0] = false;
				table[1][1] = true;
				table[1][2] = false;
				table[1][3] = false;
				table[2][0] = false;
				table[2][1] = false;
				table[2][2] = true;
				table[2][3] = false;
				table[3][0] = false;
				table[3][1] = false;
				table[3][2] = false;
				table[3][3] = true;
				pos = findSign(table, 4, 4, 1, true);
			}

			if (pos.x == -1) {
				table = new boolean[4][4];
				table[0][0] = false;
				table[0][1] = false;
				table[0][2] = false;
				table[0][3] = true;
				table[1][0] = false;
				table[1][1] = false;
				table[1][2] = true;
				table[1][3] = false;
				table[2][0] = false;
				table[2][1] = true;
				table[2][2] = false;
				table[2][3] = false;
				table[3][0] = true;
				table[3][1] = false;
				table[3][2] = false;
				table[3][3] = false;
				pos = findSign(table, 4, 4, 1, true);
			}

			if (pos.x == -1) {
				table = new boolean[4][1];
				table[0][0] = true;
				table[1][0] = true;
				table[2][0] = true;
				table[3][0] = true;
				pos = findSign(table, 4, 1, 1, true);
			}

			if (pos.x == -1) {
				table = new boolean[1][4];
				table[0][0] = true;
				table[0][1] = true;
				table[0][2] = true;
				table[0][3] = true;
				pos = findSign(table, 1, 4, 1, true);
			}
		}

		// If predefined signs didn't give result
		if (pos.x == -1) {
			int max = -10000;
			for (int i = 0; i < 13; i++) {
				for (int j = 0; j < 13; j++) {
					if (tableAI[i][j] > max && tableAI[i][j] < 10000
							&& type == 0) {
						max = tableAI[i][j];
						pos.x = i;
						pos.y = j;
					} else if (type == 1) {
						if (enemy[i][j] == true) {
							pos.x = i;
							pos.y = j;
						}
					}
				}
			}
		}

		if (pos.x == -1) {
			pos.x = 6;
			pos.y = 6;
		}

		for (int i = 0; i < 5; i++) {
			setValueInRadius(values[4 - i], i, pos.x, pos.y);
		}

		AI[pos.x][pos.y] = true;

		return pos;
	}

	// Records where did the enemy put their piece
	public void recordEnemyMove(Position pos) {
		for (int i = 0; i < 5; i++) {
			setValueInRadius(values[4 - i], i, pos.x, pos.y);
		}
		tableAI[pos.x][pos.y] = values[4];
		enemy[pos.x][pos.y] = true;
	}

	// Records a new block
	public void recordNewBlock(Position pos) {
		blocks[pos.x][pos.y] = true;
		setValueInRadius(values[4], 0, pos.x, pos.y);
	}

	// Deletes a piece which was placed by the AI
	public void deleteAIPiece(int x, int y) {
		AI[x][y] = false;
		for (int i = 0; i < 5; i++) {
			setValueInRadius(-values[4 - i], i, x, y);
		}
		tableAI[x][y] = -10000;
	}

	// Deletes a piece which was placed by the enemy
	public void deleteEnemyPiece(int x, int y) {
		enemy[x][y] = false;
		for (int i = 0; i < 5; i++) {
			setValueInRadius(-values[4 - i], i, x, y);
		}
		tableAI[x][y] = -10000;
	}

	// Deletes a block
	public void deleteBlock(int x, int y) {
		blocks[x][y] = false;
		tableAI[x][y] = -10000;
	}

	// Puts a piece to the table
	public Position putBlock() {
		return calculateMove(1);
	}

	// The AI replaces an enemy piece with its own
	public Position replaceEnemyPiece() {
		type = 0;
		Position pos = calculateMove(0);
		type = 1;
		return pos;
	}
}