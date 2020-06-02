package com.example.android34;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class replayGame extends AppCompatActivity {

    ArrayList<String> temp;
    int index = 0;
    Game game;
    TableLayout board;
    Game[] gameStates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String gameName = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_scene);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gameName = extras.getString("gameName");
        }
        //Find the directory for the SD Card using the API
        String path = System.getProperty("user.dir") + "data/data/com.example.android34/files/";

        //Get the text file
        File file = new File(path,gameName);

        //Read text from file
        temp = new ArrayList<String>();


        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                temp.add(line);
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            e.printStackTrace();
        }

        //System.out.println("Here is the name of the selected game: "+ gameName);
        //System.out.println("Here is the size of the temp: "+temp.size());
        game = new Game();
        game.setBoard(setUpBoard(game.getBoard()));
        board = findViewById(R.id.replay_board);
        //Create an array of Games to hold all the game states in
        gameStates = new Game[temp.size()];
    }


    public void backButton(View view) {

        if(index == 0){
            Toast.makeText(this, "Cannot go back further than the begging", Toast.LENGTH_SHORT).show();
        }else{
            --index;
            game.setBoard(gameStates[index].getBoard());
            game.setWhitePlayer(gameStates[index].getWhitePlayer());
            game.setBlackPlayer(gameStates[index].getBlackPlayer());
            int row_count = board.getChildCount();
            for (int i = 0; i < row_count; i++) {
                //Get the ith row from the board
                View v_temp = board.getChildAt(i);
                //Make sure it is an instance of table row
                if (v_temp instanceof TableRow) {
                    TableRow row = (TableRow) v_temp;
                    //Get all children of the row, ie all cells within that row
                    int column_Count = row.getChildCount();
                    for (int j = 0; j < column_Count; j++) {
                        ImageView tile = (ImageView) row.getChildAt(j);
                        if (game.board[i][j] != null && game.board[i][j].getColor().equals("w")){
                            if (game.board[i][j].getInitial().equals("p")){
                                tile.setImageResource(R.drawable.white_pawn);
                            }else if(game.board[i][j].getInitial().equals("R")){
                                tile.setImageResource(R.drawable.white_rook);
                            }else if(game.board[i][j].getInitial().equals("N")){
                                tile.setImageResource(R.drawable.white_knight);
                            }else if(game.board[i][j].getInitial().equals("B")){
                                tile.setImageResource(R.drawable.white_bishop);
                            }else if(game.board[i][j].getInitial().equals("Q")){
                                tile.setImageResource(R.drawable.white_queen);
                            }else if(game.board[i][j].getInitial().equals("K")){
                                tile.setImageResource(R.drawable.white_king);
                            }
                        }else if (game.board[i][j] != null && game.board[i][j].getColor().equals("b")) {
                            if (game.board[i][j].getInitial().equals("p")){
                                tile.setImageResource(R.drawable.black_pawn);
                            }else if(game.board[i][j].getInitial().equals("R")){
                                tile.setImageResource(R.drawable.black_rook);
                            }else if(game.board[i][j].getInitial().equals("N")){
                                tile.setImageResource(R.drawable.black_knight);
                            }else if(game.board[i][j].getInitial().equals("B")){
                                tile.setImageResource(R.drawable.black_bishop);
                            }else if(game.board[i][j].getInitial().equals("Q")){
                                tile.setImageResource(R.drawable.black_queen);
                            }else if(game.board[i][j].getInitial().equals("K")){
                                tile.setImageResource(R.drawable.black_king);
                            }
                        }else{
                            tile.setImageDrawable(null);
                        }
                    }
                }
            }
        }
    }

    public void forwardButton(View view){

        if(index == temp.size()){
            Toast.makeText(this, "No more moves left", Toast.LENGTH_SHORT).show();
        }else{
            String nextMove = temp.get(index);
            int[] coordinates = new int[4];
            String[] tempArray = nextMove.split(",", 4);
            System.out.println("1: "+tempArray[0]);
            System.out.println("2: "+tempArray[1]);
            System.out.println("3: "+tempArray[2]);
            System.out.println("4: "+tempArray[3]);
            coordinates[0] = Integer.parseInt(tempArray[0]);
            coordinates[1] = Integer.parseInt(tempArray[1]);
            coordinates[2] = Integer.parseInt(tempArray[2]);
            coordinates[3] = Integer.parseInt(tempArray[3]);

            //Save the current game state
            gameStates[index] = new Game();
            gameStates[index].setBoard(game.getBoard());
            ++index;
            makeMove(coordinates);
        }
    }


    public void makeMove(int[] coordinates){

        game.board = ChessRules.checkRules(game.board, coordinates, null, game);
        //Need to copy the results somehow and show them to the user
        int row_count = board.getChildCount();
        for (int i = 0; i < row_count; i++) {
            //Get the ith row from the board
            View v_temp = board.getChildAt(i);
            //Make sure it is an instance of table row
            if (v_temp instanceof TableRow) {
                TableRow row = (TableRow) v_temp;
                //Get all children of the row, ie all cells within that row
                int column_Count = row.getChildCount();
                for (int j = 0; j < column_Count; j++) {
                    ImageView tile = (ImageView) row.getChildAt(j);
                    if (game.board[i][j] != null && game.board[i][j].getColor().equals("w")){
                        if (game.board[i][j].getInitial().equals("p")){
                            tile.setImageResource(R.drawable.white_pawn);
                        }else if(game.board[i][j].getInitial().equals("R")){
                            tile.setImageResource(R.drawable.white_rook);
                        }else if(game.board[i][j].getInitial().equals("N")){
                            tile.setImageResource(R.drawable.white_knight);
                        }else if(game.board[i][j].getInitial().equals("B")){
                            tile.setImageResource(R.drawable.white_bishop);
                        }else if(game.board[i][j].getInitial().equals("Q")){
                            tile.setImageResource(R.drawable.white_queen);
                        }else if(game.board[i][j].getInitial().equals("K")){
                            tile.setImageResource(R.drawable.white_king);
                        }
                    }else if (game.board[i][j] != null && game.board[i][j].getColor().equals("b")) {
                        if (game.board[i][j].getInitial().equals("p")){
                            tile.setImageResource(R.drawable.black_pawn);
                        }else if(game.board[i][j].getInitial().equals("R")){
                            tile.setImageResource(R.drawable.black_rook);
                        }else if(game.board[i][j].getInitial().equals("N")){
                            tile.setImageResource(R.drawable.black_knight);
                        }else if(game.board[i][j].getInitial().equals("B")){
                            tile.setImageResource(R.drawable.black_bishop);
                        }else if(game.board[i][j].getInitial().equals("Q")){
                            tile.setImageResource(R.drawable.black_queen);
                        }else if(game.board[i][j].getInitial().equals("K")){
                            tile.setImageResource(R.drawable.black_king);
                        }
                    }else{
                        tile.setImageDrawable(null);
                    }
                }
            }
        }
    }

    public static ChessPiece[][] setUpBoard(ChessPiece[][] board) {
        int i,j;
        //Set all values in the board to null
        for(i = 0; i < 8; ++i) {
            for(j = 0; j < 8; ++j) {
                board[i][j] = null;
            }
        }

        //Set up Black pieces
        board[0][0] = new Rook();
        board[0][7] = new Rook();
        board[0][1] = new Knight();
        board[0][6] = new Knight();
        board[0][2] = new Bishop();
        board[0][5] = new Bishop();
        board[0][3] = new Queen();
        board[0][4] = new King();
        for(i = 0; i < 8; ++i) {
            board[1][i] = new Pawn();
        }
        //Set the color field for the black pieces
        for(i = 0; i < 2; ++i) {
            for(j = 0; j < 8; ++j) {
                board[i][j].setColor("b");
            }
        }
        //Set up White pieces
        board[7][0] = new Rook();
        board[7][7] = new Rook();
        board[7][1] = new Knight();
        board[7][6] = new Knight();
        board[7][2] = new Bishop();
        board[7][5] = new Bishop();
        board[7][3] = new Queen();
        board[7][4] = new King();
        for(i = 0; i < 8; ++i) {
            board[6][i] = new Pawn();
        }
        //Set the color field for the white pieces
        for(i = 7; i > 5; --i) {
            for(j = 0; j < 8; ++j) {
                board[i][j].setColor("w");
            }
        }
        return board;
    }
}
