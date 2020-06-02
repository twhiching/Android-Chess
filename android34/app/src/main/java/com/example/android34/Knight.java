package com.example.android34;

public class Knight extends ChessPiece{

	private String initial = "N";
	private String color;
	private String identifier;
	@Override
	public boolean moveTo(ChessPiece board[][], int[] coordinates, Game game) {

		int xStart = coordinates[0];
		int yStart = coordinates[1];
		int xEnd = coordinates[2];
		int yEnd = coordinates[3];
		int xOffset = coordinates[2] - coordinates[0];
		int yOffset = coordinates[3] - coordinates[1];
		int colBounds = xEnd - xStart;
		int rowBounds = yEnd - yStart;
		boolean test = false;

		//If the move is to the same spot as to where it's currently located, return false
		if(xOffset == 0 && yOffset == 0){
			return false;
		}

		//Make sure that the moved propsed by the player is a move that the knight can do, ie) knoght can only travel in the x or y direction up two over 1 or vice versa
		//Traveling in the x direction +2 and y direction +1
		if(Math.abs(colBounds) == 2 && Math.abs(rowBounds) == 1){
			if(board[xEnd][yEnd] != null && board[xEnd][yEnd].getColor().equals(this.color) ){
				return false;
			}
			//Test for putting yourself in check
			test = check(board,coordinates,game);
			if(test == true){
				return  false;
			}else{
				return true;
			}

		}
		//Traveling in the x direction +1 and y direction +2
		else if (Math.abs(colBounds) == 1 && Math.abs(rowBounds) == 2){
			if(board[xEnd][yEnd] != null && board[xEnd][yEnd].getColor().equals(this.color) ){
				return false;
			}
			//Test for putting yourself in check
			test = check(board,coordinates,game);
			if(test == true){
				return  false;
			}else{
				return true;
			}
		}
		return false;
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
