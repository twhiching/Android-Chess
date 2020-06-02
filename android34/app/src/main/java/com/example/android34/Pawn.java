package com.example.android34;
public class Pawn extends ChessPiece {

    private boolean hasMoved = false;
    private boolean enPassant = false;
    private String initial = "p";
    private String color;
    private String identifier;
    @Override
    public boolean moveTo(ChessPiece[][] board, int[] coordinates, Game game) {

        boolean test = false;
        int xOffset = coordinates[2] - coordinates[0];
        int yOffset = coordinates[3] - coordinates[1];
        ChessPiece[][] test_board = board;

        //System.out.println("Coordinates passed in to pawn are: ("+coordinates[0]+","+coordinates[1]+"),("+coordinates[2]+","+coordinates[3]+")");
        //check for 4 possible outcomes, moves forward two spaces, moves up one space, moves right diagonal one space, moves left diagonal one space

        //If the move is to the same spot as to where it's currently located, return false
        if(xOffset == 0 && yOffset == 0){
            return false;
        }

        //White pawns go from bottom to top, black pawns go from top to bottom
        if(this.color.equals("w")){

            //Check if coordinates for white are always going forward
            if(!(coordinates[0] >= coordinates[2])){
                return false;
            }

            //System.out.println("piece at position: (" + coordinates[2] +"," + coordinates[3] +") is:" b]]]]]]]]]]]\ );
            //check for forward move two spaces first
            if(coordinates[0] == 6 && board[coordinates[0]-1][coordinates[3]] == null &&Math.abs(coordinates[1] - coordinates[3]) == 0 && Math.abs(coordinates[0] - coordinates[2]) == 2 && board[coordinates[2]][coordinates[3]] == null){
                hasMoved = true;
                enPassant = true;
                //Check for check
                test = check(test_board,coordinates,game);
                if(test == true){
                    return  false;
                }else
                    return true;
            }

            //check for forward move one space
            else if(Math.abs(coordinates[1] - coordinates[3]) == 0 && Math.abs(coordinates[0] - coordinates[2]) == 1 && board[coordinates[2]][coordinates[3]] == null){
                hasMoved = true;
                //Check for check
                test = check(test_board,coordinates,game);
                if(test == true){
                    return  false;
                }else
                    return true;
            }

            //check for enPassant
            else if(Math.abs(coordinates[1] - coordinates[3]) == 1 && Math.abs(coordinates[0] - coordinates[2]) == 1 && checkEnPassant(board,coordinates)){
                hasMoved = true;
                //Check for check
                test = check(test_board,coordinates,game);
                if(test == true){
                    return  false;
                }else
                    return true;
            }

            //check for right or left diagonal move
            else{
                if(Math.abs(coordinates[1] - coordinates[3]) == 1 && Math.abs(coordinates[0] - coordinates[2]) == 1 && (board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals("b"))){
                    hasMoved = true;
                    //Check for check
                    test = check(test_board,coordinates,game);
                    if(test == true){
                        return  false;
                    }else
                        return true;
                }
            }
        }

        else{
            //Check if coordinates for black are always backward forward
            if(!(coordinates[0] <= coordinates[2])){
                return false;
            }

            //check for forward move two spaces first
            if(coordinates[0] == 1 && board[coordinates[0]+1][coordinates[3]] == null && Math.abs(coordinates[1] - coordinates[3]) == 0 && Math.abs(coordinates[0] - coordinates[2]) == 2 && board[coordinates[2]][coordinates[3]] == null){
               enPassant = true;
               hasMoved = true;
               //Check for check
               test = check(test_board,coordinates,game);
               if(test == true){
                   return  false;
               }else
                   return true;
            }

            //check for forward move one space
            else if(Math.abs(coordinates[1] - coordinates[3]) == 0 && Math.abs(coordinates[0] - coordinates[2]) == 1 && board[coordinates[2]][coordinates[3]] == null){
                hasMoved = true;
                //Check for check
                test = check(test_board,coordinates,game);
                if(test == true){
                    return  false;
                }else
                    return true;
            }

            else if(Math.abs(coordinates[1] - coordinates[3]) == 1 && Math.abs(coordinates[0] - coordinates[2]) == 1 && checkEnPassant(board,coordinates)){
                hasMoved = true;
                //Check for check
                test = check(test_board,coordinates,game);
                if(test == true){
                    return  false;
                }else
                    return true;
            }

            //check for right or left diagonal move
            else{
                if(Math.abs(coordinates[1] - coordinates[3]) == 1 && Math.abs(coordinates[0] - coordinates[2]) == 1 && (board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals("w"))){
                    hasMoved = true;
                    //Check for check
                    test = check(test_board,coordinates,game);
                    if(test == true){
                        return  false;
                    }else
                        return true;
                }
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

    public static boolean checkEnPassant(ChessPiece[][] board, int[] coordinates) {

        String openentColor = "blank";
        int xStart = coordinates[0];
        int yStart = coordinates[1];
        int xEnd = coordinates[2];
        int yEnd = coordinates[3];
        String opponentColor;
        int rowOffset = Math.abs(xEnd - xStart);
        int colOffset = yEnd - yStart;
        boolean result = false;

        //CHeck what color you currenlty are
        if(board[xStart][yStart].getColor().equals("w")){
            opponentColor = "b";
            if(board[xEnd+rowOffset][yEnd] != null  && board[xEnd+rowOffset][yEnd].getidentifier().equals("bp")){
                if(board[xEnd+rowOffset][yEnd].getEnPassant()){
                    result  = true;
                }

            }
        }else {
            opponentColor = "w";
            if (board[xEnd - rowOffset][yEnd] != null && board[xEnd - rowOffset][yEnd].getidentifier().equals("wp")) {
                if (board[xEnd - rowOffset][yEnd].getEnPassant()) {
                    result = true;
                }

            }

        }
        return result;
    }

    public boolean getEnPassant(){
        return enPassant;
    }

    public void setEnPassant(boolean x){
        this.enPassant = x;
    }

}
