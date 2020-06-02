package com.example.android34;

public abstract class ChessPiece {

    public int turnCount;
    public abstract boolean moveTo(ChessPiece[][] board, int[] coordinates,Game game);
    public abstract String getColor();
    public abstract String getidentifier();
    public abstract String getInitial();
    public abstract void setColor(String color);
    public boolean getEnPassant(){return false;}
    public void setEnPassant(boolean x){}
    public boolean getHasCastled(){return false; }
    public void setHasCastled(boolean castled){ }
    public void setHasMoved(boolean statement){}
    public boolean getHasMoved(){return false;}
    //Checks to see if the move suggested by the player is a valid move, ie checks to see if it puts them into check
    public static boolean check(ChessPiece[][] board, int[] coordinates, Game game){

        System.out.println("Here are the coordinates passed into ChessPiece: ("+coordinates[0]+","+coordinates[1]+"),("+coordinates[2]+","+coordinates[3]+")");
        System.out.println("Here is the selected piece and its color: "+board[coordinates[0]][coordinates[1]].getidentifier());
        String opponenetColor;
        String restoredPiece = null;
        String restoredPieceColor = null;
        boolean kingOrPawnSpecialState = false;
        int kingX = 0;
        int kingY = 0;
        int opponentKingX = 0;
        int opponentKingY = 0;
        boolean flag = false;
        int[] oppnentCoordinates = {0,0,0,0};
        boolean test = false;
        boolean result = false;
        boolean secondTest = false;

        //Make the move onto the board object
        //Run a loop finding the king of the color of the object
        //Then run a loop and check all pieces of opposite color and run there moveTo methods
        //See if they can reach you, if so return back true, else return false

        //System.out.println("Checking to make sure that player didn't put themselves in check");
        //System.out.println("Coordinates passed into check methid is:" + coordinates[0] +"," + coordinates[1] + ","+coordinates[2]+","+coordinates[3]);

        if(board[coordinates[0]][coordinates[1]].getColor().equals("w")){
            opponenetColor = "b";
        }else{
            opponenetColor = "w";
        }

        //If end destination is occupied, remember piece to restore state of the board
        if(board[coordinates[2]][coordinates[3]] != null){
            switch (board[coordinates[2]][coordinates[3]].getInitial()){
                case "R":
                    restoredPiece = "R";
                    restoredPieceColor = board[coordinates[2]][coordinates[3]].getColor();
                    break;

                case "N":
                    restoredPiece = "N";
                    restoredPieceColor = board[coordinates[2]][coordinates[3]].getColor();
                    break;

                case "B":
                    restoredPiece = "B";
                    restoredPieceColor = board[coordinates[2]][coordinates[3]].getColor();
                    break;

                case "Q":
                    restoredPiece = "Q";
                    restoredPieceColor = board[coordinates[2]][coordinates[3]].getColor();
                    break;

                case "K":
                    restoredPiece = "K";
                    restoredPieceColor = board[coordinates[2]][coordinates[3]].getColor();
                    kingOrPawnSpecialState = board[coordinates[2]][coordinates[3]].getHasCastled();
                    break;

                case "p":
                    restoredPiece = "p";
                    restoredPieceColor = board[coordinates[2]][coordinates[3]].getColor();
                    kingOrPawnSpecialState = board[coordinates[2]][coordinates[3]].getEnPassant();
                    break;

                default:

                    break;


            }

        }
        //Make the actual move
        board[coordinates[2]][coordinates[3]] = board[coordinates[0]][coordinates[1]];
        board[coordinates[0]][coordinates[1]] = null;


        if(opponenetColor.equals("w")){
            if(game.getBlackPlayer() == 4){

                //Find out what color the player is and then find out where there king is located at
                if(board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals("b")){
                    for(int i = 0; i < 8; ++i){
                        for(int j = 0; j < 8; ++j){
                            if(board[i][j] != null && board[i][j].getidentifier().equals("bK")){
                                kingX = i;
                                kingY = j;
                                break;
                            }
                        }
                    }
                }
                //Now run a loop on all opponents pieces to see if they can reach your king
                for(int i = 0; i < 8; ++i){
                    for(int j = 0; j < 8; ++j){
                        if(board[i][j] != null && board[i][j].getColor().equals(opponenetColor)){
                            oppnentCoordinates[0] = i;
                            oppnentCoordinates[1] = j;
                            oppnentCoordinates[2] = kingX;
                            oppnentCoordinates[3] = kingY;
                            test = board[i][j].moveTo(board,oppnentCoordinates,game);
                            System.out.println("First: Moving "+ board[i][j].getidentifier() +  " at position" + "("+i+","+j+") to " + kingX +","+ kingY +" is:" +test);
                            if(test){
                                secondTest = true;
                            }
                        }
                    }
                }


            }
        }else if(opponenetColor.equals("b")){
            if(game.getWhitePlayer() == 4){
                //Find out what color the player is and then find out where there king is located at
                if(board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals("w")){
                    for(int i = 0; i < 8; ++i){
                        for(int j = 0; j < 8; ++j){
                            if(board[i][j] != null && board[i][j].getidentifier().equals("wK")){
                                kingX = i;
                                kingY = j;
                                break;
                            }
                        }
                    }
                }
                //Now run a loop on all opponents pieces to see if they can reach your king
                for(int i = 0; i < 8; ++i){
                    for(int j = 0; j < 8; ++j){
                        if(board[i][j] != null && board[i][j].getColor().equals(opponenetColor)){
                            oppnentCoordinates[0] = i;
                            oppnentCoordinates[1] = j;
                            oppnentCoordinates[2] = kingX;
                            oppnentCoordinates[3] = kingY;
                            test = board[i][j].moveTo(board,oppnentCoordinates,game);
                            System.out.println("First: Moving "+ board[i][j].getidentifier() +  " at position" + "("+i+","+j+") to " + kingX +","+ kingY +" is:" +test);
                            if(test){
                                secondTest = true;
                            }
                        }
                    }
                }
            }
        }else{
            //Find out what color the player is and then find out where there king is located at
            if(board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals("w")){
                opponenetColor = "b";
                for(int i = 0; i < 8; ++i){
                    for(int j = 0; j < 8; ++j){
                        if(board[i][j] != null && board[i][j].getidentifier().equals("wK")){
                            kingX = i;
                            kingY = j;
                            break;
                        }
                    }
                }
            }else if(board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals("b")){
                opponenetColor = "w";
                for(int i = 0; i < 8; ++i){
                    for(int j = 0; j < 8; ++j){
                        if(board[i][j] != null && board[i][j].getidentifier().equals("bK")){
                            kingX = i;
                            kingY = j;
                            break;
                        }
                    }
                }

            }

            //Now run a loop on all opponents pieces to see if they can reach your king
            for(int i = 0; i < 8; ++i){
                for(int j = 0; j < 8; ++j){
                    if(board[i][j] != null && board[i][j].getColor().equals(opponenetColor)){
                        oppnentCoordinates[0] = i;
                        oppnentCoordinates[1] = j;
                        oppnentCoordinates[2] = kingX;
                        oppnentCoordinates[3] = kingY;
                        test = board[i][j].moveTo(board,oppnentCoordinates,game);
                        System.out.println("First: Moving "+ board[i][j].getidentifier() +  " at position" + "("+i+","+j+") to " + kingX +","+ kingY +" is:" +test);
                        if(test){
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        //Restore the board back to it's orginal state
        if(restoredPiece != null){
            switch (restoredPiece){
                case "R":
                    board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
                    board[coordinates[2]][coordinates[3]] = new Rook();
                    board[coordinates[2]][coordinates[3]].setColor(restoredPieceColor);
                    break;

                case "N":
                    board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
                    board[coordinates[2]][coordinates[3]] = new Knight();
                    board[coordinates[2]][coordinates[3]].setColor(restoredPieceColor);
                    break;

                case "B":
                    board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
                    board[coordinates[2]][coordinates[3]] = new Bishop();
                    board[coordinates[2]][coordinates[3]].setColor(restoredPieceColor);
                    break;

                case "Q":
                    board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
                    board[coordinates[2]][coordinates[3]] = new Queen();
                    board[coordinates[2]][coordinates[3]].setColor(restoredPieceColor);
                    break;

                case "K":
                    board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
                    board[coordinates[2]][coordinates[3]] = new King();
                    board[coordinates[2]][coordinates[3]].setColor(restoredPieceColor);
                    board[coordinates[2]][coordinates[3]].setHasCastled(kingOrPawnSpecialState);
                    break;

                case "p":
                    board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
                    board[coordinates[2]][coordinates[3]] = new Pawn();
                    board[coordinates[2]][coordinates[3]].setColor(restoredPieceColor);
                    board[coordinates[2]][coordinates[3]].setEnPassant(kingOrPawnSpecialState);
                    break;
                default:
                    break;
            }
        }else{
            board[coordinates[0]][coordinates[1]] = board[coordinates[2]][coordinates[3]];
            board[coordinates[2]][coordinates[3]] = null;
        }
        if(result){
            //Check to see if your currently in checkmate
           return true;
        }if(secondTest){
            return true;
        }
        return false;
    }
}
