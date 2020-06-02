package com.example.android34;

//TODO castling doesn;t work as expected, king can still castle if rook moves and returns back to origin
public class King extends ChessPiece{

    private boolean hasMoved = false;
    private boolean hasCastling = false;
    private String initial = "K";
    private String color;
    private String identifier;
    @Override
    public boolean moveTo(ChessPiece[][] board, int[] coordinates, Game game) {

        int x,y;
        int xOffset = coordinates[2] - coordinates[0];
        int yOffset = coordinates[3] - coordinates[1];
        int kingX = 0;
        int kingY = 0;
        int opponentkingX = 0;
        int opponentkingY = 0;
        int bounds_test_col = coordinates[3] - coordinates[1];
        int bounds_test_row = coordinates[2] - coordinates[0];
        boolean test = false;
        boolean flag = false;
        String opponenetColor = null;

        //Figure out the color of the opponent
        if(this.color.equals("w")){
            opponenetColor = "b";
        }else{
            opponenetColor = "w";
        }
        //If the move is to the same spot as to where it's currently located, return false
        if(xOffset == 0 && yOffset == 0){
            return false;
        }

        //Check to see if the proposed move is that of a castling move
        if((yOffset == 2 || yOffset == -2) && xOffset == 0 && board[coordinates[2]][coordinates[3]] == null && !hasMoved){
            int[] coord = {-1, -1, -1, -1};
            //Now see what side to castle on, left or right side
            //First go to the right
            if(yOffset == 2){
                if(board[coordinates[0]][7] != null && board[coordinates[0]][7].getInitial().equals("R") && !board[coordinates[0]][7].getHasCastled() && !board[coordinates[0]][7].getHasMoved() && !hasMoved){
                    //check if king is currently in check
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            if (board[i][j] != null && board[i][j].getColor().equals(opponenetColor)) {
                                coord[0] = i;
                                coord[1] = j;
                                coord[2] = coordinates[0];
                                coord[3] = coordinates[1];
                                test = board[i][j].moveTo(board, coord, game);
                                //System.out.println("Checking to see if " + board[coord[2]][coord[3]].getColor() + "king is currently in check for the castling move...Moving " + board[i][j].getidentifier() + " at position" + "(" + i + "," + j + ") to " + coord[2] + "," + coord[3] + " is:" + test);
                                if (test == true) {
                                    return false;
                                }

                            }
                        }
                    }
                }

                //Check to see now if path where the king has to travel in is under attack by any  opposing piece
                for(int k = 1; k < 3; ++k){
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            if (board[i][j] != null && board[i][j].getColor().equals(opponenetColor)) {
                                coord[0] = i;
                                coord[1] = j;
                                coord[2] = coordinates[0];
                                coord[3] = coordinates[1]+k;
                                test = board[i][j].moveTo(board, coord,game);
                                //System.out.println("Checking to see if spaces along the way is currently under attack ...Moving " + board[i][j].getidentifier() + " at position" + "(" + i + "," + j + ") to " + coord[2] + "," + coord[3] + " is:" + test);
                                if (test == true) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //See if there are any pieces in between the king and rook
                board[coordinates[0]][coordinates[1]] = new Rook();
                if(opponenetColor.equals("w")){
                    board[coordinates[0]][coordinates[1]].setColor("w");
                }else{
                    board[coordinates[0]][coordinates[1]].setColor("b");
                }

                test = board[coordinates[0]][coordinates[1]].moveTo(board,coordinates,game);
                //System.out.println("Checking to see if move is possible"+ board[coordinates[0]][coordinates[1]].getidentifier() +  " at position" + "("+coordinates[0]+","+coordinates[1]+") to " + coordinates[2] +","+ coordinates[3] +" is:" +test);
                if(test){
                    board[coordinates[0]][coordinates[1]] = new King();
                    board[coordinates[0]][coordinates[1]].setColor(this.color);
                    board[coordinates[0]][coordinates[1]].setHasCastled(true);
                    //Check to see if this puts you into check
                    test = check(board,coordinates,game);
                    if(test == true){
                        return  false;
                    }
                    else{
                        setHasMoved(true);
                        return  true;
                    }
                }else{
                    board[coordinates[0]][coordinates[1]] = new King();
                    board[coordinates[0]][coordinates[1]].setColor(this.color);
                    return false;
                }
            }else if(yOffset == -2){
                //Castling move is now going to the left
                if(board[coordinates[0]][0] != null && board[coordinates[0]][0].getInitial().equals("R") && !board[coordinates[0]][0].getHasCastled() && !board[coordinates[0]][0].getHasMoved() && !hasMoved){
                    //Then to the left
                    //check if king is currently in check
                    for (int i = 0; i < 8; ++i) {
                        for (int j = 0; j < 8; ++j) {
                            if (board[i][j] != null && board[i][j].getColor().equals(opponenetColor)) {
                                coord[0] = i;
                                coord[1] = j;
                                coord[2] = coordinates[0];
                                coord[3] = coordinates[1];
                                test = board[i][j].moveTo(board, coord, game);
                                //System.out.println("Checking to see if spaces along the way is currently under attack ...Moving " + board[i][j].getidentifier() + " at position" + "(" + i + "," + j + ") to " + coord[2] + "," + coord[3] + " is:" + test);
                                if (test == true) {
                                    return false;
                                }
                            }
                        }
                    }

                    //Check to see now if the two spaces are under attack by any piece
                    for(int k = -1; k > -3; --k){
                        for (int i = 0; i < 8; ++i) {
                            for (int j = 0; j < 8; ++j) {
                                if (board[i][j] != null && board[i][j].getColor().equals(opponenetColor)) {
                                    coord[0] = i;
                                    coord[1] = j;
                                    coord[2] = coordinates[0];
                                    coord[3] = coordinates[1]+k;
                                    test = board[i][j].moveTo(board, coord, game);
                                    //System.out.println("checking to see if spaces along the way is currently under attack ...Moving " + board[i][j].getidentifier() + " at position" + "(" + i + "," + j + ") to " + coord[2] + "," + coord[3] + " is:" + test);
                                    if (test == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }

                    //See if there are any pieces in between the king and rook
                    board[coordinates[0]][coordinates[1]] = new Rook();
                    if(opponenetColor.equals("w")){
                        board[coordinates[0]][coordinates[1]].setColor("w");
                    }else{
                        board[coordinates[0]][coordinates[1]].setColor("b");
                    }

                    test = board[coordinates[0]][coordinates[1]].moveTo(board,coordinates,game);
                    //System.out.println("Checking to see if if move is possible"+ board[coordinates[0]][coordinates[1]].getidentifier() +  " at position" + "("+coordinates[0]+","+coordinates[1]+") to " + coordinates[2] +","+ coordinates[3] +" is:" +test);
                    if(test){
                        board[coordinates[0]][coordinates[1]] = new King();
                        board[coordinates[0]][coordinates[1]].setColor(this.color);
                        board[coordinates[0]][coordinates[1]].setHasCastled(true);
                        //Check to see if this puts you into check
                        test = check(board,coordinates,game);
                        if(test == true){
                            return  false;
                        }
                        else{
                            setHasMoved(true);
                            return  true;
                        }
                    }else{
                        board[coordinates[0]][coordinates[1]] = new King();
                        board[coordinates[0]][coordinates[1]].setColor(this.color);
                        return false;
                    }
                }
            }

        }
        //King can only move one space at a time in any direction(not including the castling move)
        if(bounds_test_col > 1 || bounds_test_col < -1 || bounds_test_row > 1 || bounds_test_row < -1){
            return false;
        }
        //Check to make sure that the test doesn't give you out of bounds coordinates
        if(coordinates[0] + bounds_test_row > 7 || coordinates[1] + bounds_test_col > 7 || coordinates[0] + bounds_test_row < 0 || coordinates[1] + bounds_test_col < 0)
            return false;

        if(board[coordinates[2]][coordinates[3]] != null && board[coordinates[2]][coordinates[3]].getColor().equals(this.color))
            return false;
        //Get your coordinates and the opponents king coordinates

        kingX = coordinates[2];
        kingY = coordinates[3];
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 8; ++j){
                if(board[i][j] != null && board[i][j].getColor().equals(opponenetColor) && board[i][j].getInitial().equals("K")){
                    opponentkingX = i;
                    opponentkingY = j;
                }
            }
        }

        System.out.println("This is where the King wants to go: " + kingX+","+kingY);
        System.out.println("This opponent King's coordinates are: " +opponentkingX+","+opponentkingY);
        if((Math.abs(kingX - opponentkingX) == 1  && Math.abs(kingY - opponentkingY) == 0) || (Math.abs(kingY - opponentkingY) == 1) && Math.abs(kingX - opponentkingX) == 0 || (Math.abs(kingX - opponentkingX) == 1  && Math.abs(kingY - opponentkingY) == 1)){
            return false;
        }

        //Check to see if move puts you into check
        test = check(board,coordinates,game);
        if(test == true){
            return  false;
        }
        else{
            setHasMoved(true);
            return  true;
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

    public boolean getHasCastled(){
        return hasCastling;
    }

    public boolean getHasMoved(){
        return hasMoved;
    }

    public void setHasCastled(boolean castled){
        hasCastling = castled;
    }

    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
