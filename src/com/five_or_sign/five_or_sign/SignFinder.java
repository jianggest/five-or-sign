package com.five_or_sign.five_or_sign;

import java.util.ArrayList;

import com.five_or_sign.five_or_sign.Position;

public class SignFinder {
	boolean[][] player0;
	boolean[][] player1;
	boolean[][] blocks;

	// Sign position container
	ArrayList<ArrayList<Position>> signList;

	public SignFinder() {
		blocks = new boolean[13][13];
		player0 = new boolean[13][13];
		player1 = new boolean[13][13];

		signList = new ArrayList<ArrayList<Position>>(0);

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				blocks[i][j] = false;
				player0[i][j] = false;
				player1[i][j] = false;
			}
		}
	}

	// Utilities to save used signs

	void addToSignList(ArrayList<Position> list) {
		signList.add(list);
	}

	int isSignContains(Position pos) {
		for (int i = 0; i < signList.size(); i++) {
			for (int j = 0; j < signList.get(i).size(); j++) {
				Position t = signList.get(i).get(j);
				if (pos.x == t.x && pos.y == t.y) {
					return i;
				}
			}
		}

		return -1;
	}

	boolean isSignContainsList(ArrayList<Position> list) {
		for (int i = 0; i < signList.size(); i++) {
			int k = 0;
			if (signList.get(i).size() == list.size()) {
				for (int j = 0; j < list.size(); j++) {
					Position t1 = signList.get(i).get(j);
					Position t2 = list.get(j);
					if (t1.x == t2.x && t1.y == t2.y) {
						k++;
					}
				}
			}
			if (k == list.size()) {
				return true;
			}
		}

		return false;
	}

	void deleteFromSignList(int index) {
		if (signList.isEmpty() == false) {
			signList.set(index, signList.get(signList.size() - 1));
			signList.remove(signList.size() - 1);
		}
	}

	//

	public Position findSign(int[][] sign, int side, int width, int height) {
		Position pos = new Position(-1, -1);

		boolean[][] emptyTable = new boolean[width][height];

		boolean finished = false;

		for (int i = 0; i < 13 - width && finished == false; i++) {
			for (int j = 0; j < 13 - height && finished == false; j++) {
				boolean possible = true;

				for (int m = 0; m < width; m++) {
					for (int n = 0; n < height; n++) {
						emptyTable[m][n] = false;
					}
				}

				ArrayList<Position> list = new ArrayList<Position>(0);

				for (int k = 0; k < width && finished == false
						&& possible == true; k++) {
					for (int l = 0; l < height && finished == false
							&& possible == true; l++) {
						if (side == 0) {
							if (sign[k][l] == 1
									&& player0[i + k][j + l] == false) {
								possible = false;
							} else if (sign[k][l] == 1
									&& player0[i + k][j + l] == true) {
								Position tpos = new Position(i + k, j + l);
								list.add(tpos);
							}
						} else if (side == 1) {
							if (sign[k][l] == 1
									&& player1[i + k][j + l] == false) {
								possible = false;
							} else if (sign[k][l] == 1
									&& player1[i + k][j + l] == true) {
								Position tpos = new Position(i + k, j + l);
								list.add(tpos);
							}
						}
					}
				}

				if (possible == true) {
					list.trimToSize();
					if (isSignContainsList(list) == false) {
						addToSignList(list);
						pos.x = i;
						pos.y = j;
						return pos;
					}
				}
			}
		}
		return pos;
	}

	public void addBlock(int x, int y) {
		blocks[x][y] = true;
	}

	public void addX(int x, int y) {
		player0[x][y] = true;
	}

	public void addO(int x, int y) {
		player1[x][y] = true;
	}

	public void deleteBlock(int x, int y) {
		blocks[x][y] = false;
	}

	public void deleteX(int x, int y) {
		player0[x][y] = false;
	}

	public void deleteO(int x, int y) {
		player1[x][y] = false;
	}
}