package com.example.android34;

public class Bishop extends ChessPiece {

	private String initial = "B";
	private String color;
	private String identifier;
	boolean test = false;
	@Override
	public boolean moveTo(ChessPiece[][] board, int[] coordinates, Game game) {

		int xOffset = coordinates[2] - coordinates[0];
		int yOffset = coordinates[3] - coordinates[1];

		if(coordinates[0] == coordinates[2] || coordinates[1] == coordinates[3])
			return false;
		else if(Math.abs(coordinates[0] - coordinates[2]) != Math.abs(coordinates[1] - coordinates[3]))
			return false;

		//If the move is to the same spot as to where it's currently located, return false
		if(xOffset == 0 && yOffset == 0){
			return false;
		}
		
		//This is to get the smallest difference of two sets of coordinates
		int coordSetOne = coordinates[0] - coordinates[2];
		int coordSetTwo = coordinates[1] - coordinates[3];
		int difference = Math.min(coordSetOne, coordSetTwo);
		//System.out.println("Coord Numbers: " + coordSetOne + " " + coordSetTwo);
		
		if(this.color.equals("w")) {
			/* Move Northeast
			 * coordSetOne is positive
			 * coordSetTwo is negative
			 */
			if ((Integer.signum(coordSetOne) == 1) && (Integer.signum(coordSetTwo) == -1)) {
				//System.out.println("White Bishop Test1");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for (int i = 1; i < absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if (board[coordinates[2] + i][coordinates[3] - i] != null)
						return false;
				}
				for (int i = 1; i < absNum + 1; i++) {
					if (board[coordinates[0] - i][coordinates[1] + i] == null)
						continue;
					else if (board[coordinates[0] - i][coordinates[1] + i].getColor().equals("b") && board[coordinates[0] - i][coordinates[1] + i] != null)
						break;
					else
						return false;
				}
				/*
				 * Move Northwest
				 * Both coordinate Sets are positive numbers
				 * */
			} else if ((Integer.signum(coordSetOne) == 1) && (Integer.signum(coordSetTwo) == 1)) {
				//System.out.println("White Bishop Test2");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for (int i = 1; i < absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if (board[coordinates[2] + i][coordinates[3] + i] != null)
						return false;
				}
				for (int i = 1; i < absNum + 1; i++) {
					if (board[coordinates[0] - i][coordinates[1] - i] == null)
						continue;
					else if (board[coordinates[0] - i][coordinates[1] - i].getColor().equals("b") && board[coordinates[0] - i][coordinates[1] - i] != null)
						break;
					else
						return false;
				}
				/*
				 * Move SouthEast
				 * Both Coordinate Sets are negative
				 * */
			} else if ((Integer.signum(coordSetOne) == -1) && (Integer.signum(coordSetTwo) == -1)) {
				//System.out.println("White Bishop Test3");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for (int i = 1; i < absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if (board[coordinates[2] - i][coordinates[3] - i] != null)
						return false;
				}
				for (int i = 1; i < absNum + 1; i++) {
					if (board[coordinates[0] + i][coordinates[1] + i] == null)
						continue;
					else if (board[coordinates[0] + i][coordinates[1] + i].getColor().equals("b") && board[coordinates[0] + i][coordinates[1] + i] != null)
						break;
					else
						return false;
				}
			}
			/*
			 * Move Southwest
			 * Coordinate Set one negative
			 * Coordinate Set two positive
			 * */
			else if ((Integer.signum(coordSetOne) == -1) && (Integer.signum(coordSetTwo) == 1)) {
				//System.out.println("White Bishop Test4");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for (int i = 1; i < absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if (board[coordinates[2] - i][coordinates[3] + i] != null)
						return false;
				}
				for (int i = 1; i < absNum + 1; i++) {
					if (board[coordinates[0] + i][coordinates[1] - i] == null)
						continue;
					else if (board[coordinates[0] + i][coordinates[1] - i].getColor().equals("b") && board[coordinates[0] + i][coordinates[1] - i] != null)
						break;
					else
						return false;
				}
			}
			//Means piece being moved is black
		}else {
			/* Move piece SouthEast
			 * Both Coordinates are negative
			 * */
			if((Integer.signum(coordSetOne) == -1) && (Integer.signum(coordSetTwo) == -1)) {
				//System.out.println("Black Bishop Test1");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for(int i = 1; i <absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if(board[coordinates[2] - i][coordinates[3] - i] != null)
						return false;
				}
				for(int i = 1; i < absNum + 1; i++) {
					if(board[coordinates[0] + i][coordinates[1] + i] == null)
						continue;
					else if(board[coordinates[0] + i][coordinates[1] + i].getColor().equals("w") && board[coordinates[0] + i][coordinates[1] + i] != null)
						break;
					else
						return false;
				}
			/* Move piece Southwest
			 * Coordinate One: negative
			 * Coordinate Two: positive
			 * */
			}else if((Integer.signum(coordSetOne) == -1) && (Integer.signum(coordSetTwo) == 1)){
				//System.out.println("Black Bishop Test2");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for(int i = 1; i <absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if(board[coordinates[2] - i][coordinates[3] + i] != null)
						return false;
				}
				for(int i = 1; i < absNum + 1; i++) {
					if(board[coordinates[0] + i][coordinates[1] - i] == null)
						continue;
					else if(board[coordinates[0] + i][coordinates[1] - i].getColor().equals("w") && board[coordinates[0] + i][coordinates[1] - i] != null)
						break;
					else 
						return false;
				}
			/* Move piece Northeast
			 * Coordinate One: positive
			 * Coordinate Two: negative
			 * */
			}else if((Integer.signum(coordSetOne) == 1) && (Integer.signum(coordSetTwo) == -1)){
				//System.out.println("Black Bishop Test3");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for(int i = 1; i <absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if(board[coordinates[2] + i][coordinates[3] - i] != null)
						return false;
				}
				for(int i = 1; i < absNum + 1; i++) {
					if(board[coordinates[0] - i][coordinates[1] + i] == null)
						continue;
					else if(board[coordinates[0] - i][coordinates[1] + i].getColor().equals("w") && board[coordinates[0] - i][coordinates[1] + i] != null)
						break;
					else
						return false;
				}
			/* Move piece Northwest
			 * Coordinate One: positive
			 * Coordinate Two: negative
			 * */
			}else if((Integer.signum(coordSetOne) == 1) && (Integer.signum(coordSetTwo) == 1)){
				//System.out.println("Black Bishop Test4");
				int absNum = Math.abs(difference);
				//Check if a piece is in the way
				for(int i = 1; i <absNum; i++) {
					//System.out.println("Checking for piece in the way");
					if(board[coordinates[2] + i][coordinates[3] + i] != null)
						return false;
				}
				for(int i = 1; i < absNum + 1; i++) {
					if(board[coordinates[0] - i][coordinates[1] - i] == null)
						continue;
					else if(board[coordinates[0] - i][coordinates[1] - i].getColor().equals("w") && board[coordinates[0] - i][coordinates[1] - i] != null)
						break;
					else
						return false;
				}
			}			
		}

		test = check(board,coordinates,game);
		if(test == true){
			return false;
		}else {
			return true;
		}
	}
	
	public String getColor() {
		return color;
	}
	
	public String getidentifier() {
		return identifier;
	}
	
	public String getInitial() {
		return initial;
	}
	
	public void setColor(String color) {
		this.color = color;
		this.identifier = color+initial;
	}

}
