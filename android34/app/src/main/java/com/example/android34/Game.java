package com.example.android34;

public class Game {
    // 0 is default state, 2 resigned, 3 asked for draw, 4 check, 5 checkmate
    int whitePlayer;
    int blackPlayer;
    int turncount = 1;
    ChessPiece board[][] = new ChessPiece[8][8];

    public Game(){
        whitePlayer = 0;
        blackPlayer = 0;
    }
   public int getWhitePlayer(){
        return whitePlayer;
   }

    public int getBlackPlayer(){
        return blackPlayer;
    }

    public void setWhitePlayer(int x){
        whitePlayer = x;
    }

    public void setBlackPlayer(int x){
        blackPlayer = x;
    }

    public void increaseTurnCount(){
        ++turncount;
    }

    public void setTurncount(int x){
        turncount = x;
    }

    public void decreaseTurnCount(){
        --turncount;
    }

    public int getCurrentTurnCount() {
        return turncount/2;
    }

    public String getCurrentPlayerTurn(){
        String currentPlayer;
        if(turncount % 2 == 0){
            //BlackPLayers turn
            currentPlayer = "b";
        }else{
            //White player turn
            currentPlayer = "w";
        }
        return currentPlayer;
    }

    public ChessPiece[][] getBoard(){
        return  board;
    }

    public void setBoard (ChessPiece[][] board){
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 8; ++j){
                this.board[i][j] = board[i][j];
            }
        }
    }
}