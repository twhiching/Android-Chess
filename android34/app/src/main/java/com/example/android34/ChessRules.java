package com.example.android34;

public class ChessRules {

    //Handles all the actual moving of piece's and checks for special moves, ie enPassant, castling, pawn promotion, check and check mate
    public static ChessPiece[][] checkRules(ChessPiece[][] board, int[] coordinates, String promotion, Game game) {

        String opponentColor;
        int xStart = coordinates[0];
        int yStart = coordinates[1];
        int xEnd = coordinates[2];
        int yEnd = coordinates[3];
        int xOffset = xEnd - xStart;
        int yOffset = yEnd - yStart;
        int kingX = -1;
        int threatX = -1;
        int threatY = -1;
        int kingY = -1;
        boolean test = false;
        boolean result = false;
        boolean threat = false;
        int count = 0;

        //Find out the color of the opponent
        if(game.board[xStart][yStart].getColor().equals("b")){
            opponentColor = "w";
        }else{
            opponentColor = "b";
        }

        //Check for castling
        if(board[xStart][yStart] != null && board[xStart][yStart].getInitial().equals("K")  && (yOffset == 2 || yOffset == -2 )){
            if(yOffset == 2){
                board[xEnd][yEnd] = board[xStart][yStart];
                board[xEnd][yEnd-1] = new Rook();
                board[xStart][yStart] = null;
                board[xStart][7] = null;
                if(opponentColor.equals("w")){
                    board[xEnd][yEnd-1].setColor("b");
                }else{
                    board[xEnd][yEnd-1].setColor("w");
                }

            }else if(yOffset == -2){
                board[xEnd][yEnd] = board[xStart][yStart];
                board[xEnd][yEnd+1] = new Rook();
                board[xStart][yStart] = null;
                board[xStart][0] = null;
                if(opponentColor.equals("w")){
                    board[xEnd][yEnd+1].setColor("b");
                }else{
                    board[xEnd][yEnd+1].setColor("w");
                }
            }
        }else{
            //Else just make the move
            board[coordinates[2]][coordinates[3]] = board[coordinates[0]][coordinates[1]];
            board[coordinates[0]][coordinates[1]] = null;
        }

        //Set values of player status to 0
        game.setWhitePlayer(0);
        game.setBlackPlayer(0);

        //Check enPassant
        //Check if pawn
        if(board[xEnd][yEnd].getInitial().equals("p")){
            //If pawn check what color it is
            if(board[xEnd][yEnd].getColor().equals("w")){
                //If white check +1 row
                if(board[xEnd+1][yEnd] != null && board[xEnd+1][yEnd].getidentifier().equals("bp") && board[xEnd+1][yEnd].getEnPassant()){
                    board[xEnd+1][yEnd] = null;
                }
            }else{
                //If black check -1 row
                if(board[xEnd-1][yEnd] != null && board[xEnd-1][yEnd].getidentifier().equals("wp") && board[xEnd-1][yEnd].getEnPassant()){
                    board[xEnd-1][yEnd] = null;
                }
            }
        }
        //remove all enPassant values for oppent color
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (board[i][j] != null && board[i][j].getColor().equals(opponentColor) && board[i][j].getInitial().equals("p")) {
                    board[i][j].setEnPassant(false);
                }
            }
        }

        //CHeck for pawn promotion
        if(promotion == null){
            promotion = "Q";
        }
        if(board[xEnd][yEnd].getInitial().equals("p")){
            if(board[xEnd][yEnd].getColor().equals("w")){
                if(xEnd == 0){
                    switch (promotion){
                        case "R":
                            board[xEnd][yEnd] = new Rook();
                            board[xEnd][yEnd].setColor("w");
                            break;

                        case "N":
                            board[xEnd][yEnd] = new Knight();
                            board[xEnd][yEnd].setColor("w");
                            break;

                        case "B":
                            board[xEnd][yEnd] = new Bishop();
                            board[xEnd][yEnd].setColor("w");
                            break;

                        case "Q":
                            board[xEnd][yEnd] = new Queen();
                            board[xEnd][yEnd].setColor("w");
                            break;

                        default:
                            board[xEnd][yEnd] = new Queen();
                            board[xEnd][yEnd].setColor("w");
                            break;


                    }

                }
            }else if (xEnd == 7){
                switch (promotion){
                    case "R":
                        board[xEnd][yEnd] = new Rook();
                        board[xEnd][yEnd].setColor("b");
                        break;

                    case "N":
                        board[xEnd][yEnd] = new Knight();
                        board[xEnd][yEnd].setColor("b");
                        break;

                    case "B":
                        board[xEnd][yEnd] = new Bishop();
                        board[xEnd][yEnd].setColor("b");
                        break;

                    case "Q":
                        board[xEnd][yEnd] = new Queen();
                        board[xEnd][yEnd].setColor("b");
                        break;

                    default:
                        board[xEnd][yEnd] = new Queen();
                        board[xEnd][yEnd].setColor("b");
                        break;

                }

            }
        }

        System.out.println("1");
        //check if move played put opponent in check
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 8; ++j){
                if(board[i][j] != null && !(board[i][j].getColor().equals(board[xEnd][yEnd].getColor())) && board[i][j].getInitial().equals("K")){
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        System.out.println("2");
        //Now run a loop on all your pieces to see if they can reach opponent's king
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 8; ++j){
                if(board[i][j] != null && !(board[i][j].getColor().equals(opponentColor))){
                    coordinates[0] = i;
                    coordinates[1] = j;
                    coordinates[2] = kingX;
                    coordinates[3] = kingY;
                    test = board[i][j].moveTo(board,coordinates,game);
                    //System.out.println("Checking check on Oppenet...Moving "+ board[i][j].getidentifier() +  " at position" + "("+i+","+j+") to " + kingX +","+ kingY +" is:" +test);
                    if(test == true){
                        result = true;
                        System.out.println("Check");
                        if(opponentColor.equals("w")){
                            game.setWhitePlayer(4);
                        }else{
                            game.setBlackPlayer(4);
                        }
                        threatX = i;
                        threatY = j;
                        break;
                    }

                }
            }
            if(result){
                break;
            }
        }

        System.out.println("3");
        //If in check, see if there is any possible moves left
        if(result){

            //See if any piece can take out the threatening piece
            for(int i = 0; i < 8; ++i){
                for (int j = 0; j < 8; ++j){
                    if(board[i][j] != null && (board[i][j].getColor().equals(opponentColor))){
                        coordinates[0] = i;
                        coordinates[1] = j;
                        coordinates[2] = threatX;
                        coordinates[3] = threatY;
                        test = board[i][j].moveTo(board,coordinates,game);
                        //System.out.println("Checking to see if any piece can capture threating piece.. Moving "+ board[i][j].getidentifier() +  " at position" + "("+i+","+j+") to " + threatX +","+ threatY +" is:" +test);
                        if(test == true){
                            threat = true;
                            break;
                        }
                    }

                }
                if(threat){
                    break;
                }
            }

            System.out.println("4");
            //Check to see if any piece can block the path of the threatening piece
            //There are 8 potential spots, see if any piece can move beside the threatening piece and then check for check mate
            //System.out.println("Openents color is: "+ opponentColor);
            for(int x = -1; x < 2; ++x){
                for(int y = -1; y < 2; ++y){
                    int tempX = threatX + x;
                    int tempY = threatY + y;
                    if(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0){
                        continue;
                    }
                    for(int i = 0; i < 8; ++i){
                        for(int j = 0; j < 8; ++j){

                            /*System.out.println("is ("+i+","+j+") null: "+ board[i][j].equals(null));
                            if(!board[i][j].equals(null)){
                                System.out.println("Color of that piece at" + "("+i+","+j+") is:" + board[i][j].getColor());
                            }*/
                            if(board[i][j] != null && (board[i][j].getColor().equals(opponentColor))){
                                coordinates[0] = i;
                                coordinates[1] = j;
                                coordinates[2] = tempX;
                                coordinates[3] = tempY;
                                test = board[i][j].moveTo(board,coordinates,game);
                                //System.out.println("Checking to see if any piece can block the threatening piece.. Moving "+ board[i][j].getidentifier() +  " at position" + "("+i+","+j+") to " + tempX +","+ tempY +" is:" +test);
                                if(test == true){
                                    test = ChessPiece.check(board,coordinates,game);
                                    if(test == false){
                                        threat = true;
                                    }
                                }
                            }

                        }
                    }

                }
            }

            System.out.println("5");
            //Other wise nobody can take out piece, see if king can move away or take the piece out it's self without putting it's self into check
            if(threat == false){
                for(int x = -1; x < 2; ++x){
                    for(int y = -1; y < 2; ++y){
                        int tempX = kingX + x;
                        int tempY = kingY + y;
                        if(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0){
                            ++count;
                            continue;
                        }
                        if(x == 0 & y == 0){
                            continue;
                        }
                        coordinates[0] = kingX;
                        coordinates[1] = kingY;
                        coordinates[2] = tempX;
                        coordinates[3] = tempY;
                        test = board[kingX][kingY].moveTo(board,coordinates,game);
                        //System.out.println("Checking check mate Oppenet...Moving "+ board[kingX][kingY].getidentifier() +  " at position" + "("+kingX+","+kingY+") to " + tempX +","+ tempY +" is:" +test);
                        if(test == false){
                            //System.out.println("Mpve to ("+tempX+","+tempY+") failed test");
                            ++count;
                        }
                    }
                }
            }
        }

        System.out.println("6");
        //If all moves are false, then the opponent is in check mate
        if(count == 8){

            if(opponentColor.equals("w")){
                game.setWhitePlayer(5);
            }else{
                game.setBlackPlayer(5);
            }
        }
        return board;
    }

}