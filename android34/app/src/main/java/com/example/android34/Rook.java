package com.example.android34;

public class Rook extends ChessPiece {

	//Really hasCastled checks if piece has moved
	private boolean hasCastled= false;
	private String initial = "R";
	private String color;
	private String identifier;
	boolean test = false;
	private boolean hasMoved = false;
	@Override
	public boolean moveTo(ChessPiece[][] board, int[] coordinates,Game game) {

		int xOffset = coordinates[2] - coordinates[0];
		int yOffset = coordinates[3] - coordinates[1];
		//If the move is to the same spot as to where it's currently located, return false
		if(xOffset == 0 && yOffset == 0){
			return false;
		}
		//System.out.println("Rook Checkpoint One");
		//Input validation to ensure coordinates are correct
		if(coordinates[0] != coordinates[2] && coordinates[1] != coordinates[3]) {
			//System.out.println("Coordinates for Row : " + coordinates[0] + " "  + coordinates[2]);
			//System.out.println("Coordinates for Column : " + coordinates[1] + " "  + coordinates[3]);
			return false;
		}
		
		int difference = 0; //Figure out how many spaces to move
		//System.out.println("Rook Checkpoint Two");
		if(this.color.equals("w")) {
			//Pieces are in the same row move by column
			if(coordinates[0] == coordinates[2]) {
				//System.out.println("White Test1");
				difference = coordinates[1] - coordinates[3];
				//If positive move to the left
				if(Integer.signum(difference) > 0) {
					int absNum = Math.abs(difference);
					//System.out.println("absNum: " + absNum);
					//Go backwards to make sure there is not a piece in the way
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2]][coordinates[3] + i] != null)
							return false;
					}
					//Otherwise just keep going
					for(int i = 1; i < absNum + 1; i++) {
						//First check is to make sure that the piece has nothing in it's path
						if(board[coordinates[0]][coordinates[1] - i] == null) {
							//System.out.println("Fails in this line1: " + coordinates[0] + " | " + coordinates[1] + i);
							continue;
						}
						//Else if check if the piece is black and stop here and break from the loop
						else if(board[coordinates[0]][coordinates[1] - i].getColor().equals("b") && board[coordinates[0]][coordinates[1] - i] != null) 
							break;
						else
							return false;
					}
				//Move to the right
				}else {
					//System.out.println("White Test2");
					int absNum = Math.abs(difference);
					//Go backwards and check if no piece is in the path
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2]][coordinates[3] - i] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++) {
						//First check is to make sure that the piece has nothing in it's path
						if(board[coordinates[0]][coordinates[1] + i] == null)
							continue;				
						else if(board[coordinates[0]][coordinates[1] + i].getColor().equals("b") && board[coordinates[0]][coordinates[1] + i] != null)
							break;
						else
							return false;
					}
				}
			}else {
			//Chess pieces are in the same column and move by row
				difference = coordinates[0] - coordinates[2];
				//System.out.println("Difference: " + difference);
				//If positive then increment upwards
				if(Integer.signum(difference) > 0) {
					//System.out.println("White Test3");
					int absNum = Math.abs(difference);
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2] + i][coordinates[3]] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++) {
						//Check if pieces are not white
						if(board[coordinates[0] - i][coordinates[1]] == null)
							continue;
						//This breaks after one iteration when trying to get color
						else if(board[coordinates[0] - i][coordinates[1]].getColor().equals("b") && board[coordinates[0] - i][coordinates[1]] != null)
							break;
						else
							return false;
					}
				}
				
				else {
					//System.out.println("White Test4");
					//System.out.println("Difference: " + difference);
					int absNum = Math.abs(difference);
					//System.out.println("absNum: " + absNum);
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2] - i][coordinates[3]] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++)
						if(board[coordinates[0] + i][coordinates[1]] == null)
							continue;
						else if(board[coordinates[0] + i][coordinates[1]].getColor().equals("b") && board[coordinates[0] + i][coordinates[1]] != null)
							break;
						else 
							return false;
				}
			}
		//Mean the piece is black
		}else { 
			//If the rows are the same move by the columns
			if(coordinates[0] == coordinates[2]) {
				difference = coordinates[1] - coordinates[3];
				//System.out.println("Number of spaces needed to move: " + difference);
				//If positive then increment upwards
				if(Integer.signum(difference) > 0) {
					//System.out.println("Black Test One");
					int absNum = Math.abs(difference);
					//System.out.println("absNum: " + absNum);
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2]][coordinates[3] + i] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++)
						if(board[coordinates[0]][coordinates[1] - i] == null) {
							continue;
						}
						else if(board[coordinates[0]][coordinates[1] - i].getColor().equals("w") && board[coordinates[0]][coordinates[1] - i] != null)
							break;
						else 
							return false;
				}
				
				else {
					//System.out.println("Black Test2");
					int absNum = Math.abs(difference);
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2]][coordinates[3] - i] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++)
						if(board[coordinates[0]][coordinates[1] + i] == null)
							continue;
						else if(board[coordinates[0]][coordinates[1] + i].getColor().equals("w") && board[coordinates[0]][coordinates[1] + i] != null)
							break;
						else
							return false;
				}
			}else{
				//Else you are moving the chess piece by rows
				difference = coordinates[0] - coordinates[2];
				//If positive then increment upwards
				//System.out.println("Difference before check: " + difference);
				//System.out.println("Positive/Negative: " + Integer.signum(difference));
				if(Integer.signum(difference) > 0) {
					//System.out.println("Black Test3");
					int absNum = Math.abs(difference);
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2] + i][coordinates[3]] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++)
						if(board[coordinates[0] - i][coordinates[1]] == null)
							continue;
						else if(board[coordinates[0] - i][coordinates[1]].getColor().equals("w") && board[coordinates[0] - i][coordinates[1]] != null)
							break;
						else
							return false;
				}

				else {
					//System.out.println("Black Test4");
					//System.out.println("Difference: " + difference);
					int absNum = Math.abs(difference);
					for(int i = 1; i < absNum; i++) {
						//System.out.println("Checking if a piece is in the way");
						if(board[coordinates[2] - i][coordinates[3]] != null)
							return false;
					}
					for(int i = 1; i < absNum + 1; i++)
						if(board[coordinates[0] + i][coordinates[1]] == null)
							continue;
						else if(board[coordinates[0] + i][coordinates[1]].getColor().equals("w") && board[coordinates[0] + i][coordinates[1]] != null)
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
			this.setHasCastled(true);
			this.setHasMoved(true);
			return true;
		}
	}

	public boolean getHasMoved(){
		return hasMoved;
	}

	public void setHasMoved(boolean x){
		this.hasMoved = x;
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
