package com.example.android34;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

public class chess extends AppCompatActivity implements View.OnClickListener {


    TableLayout board;
    ImageView startImg;
    ImageView endImg;
    TextView player_turn_text;
    private int startImgId;
    private int endImgId;
    private int copy_startImgId;
    private int copy_endImgId;
    private int copy_rookStartImgId = -1;
    private int copy_rookEndImgId = -1;
    private int copy_enPassantImgId = -1;
    private Drawable startPosDrawable;
    private Drawable endPosDrawable;
    private Drawable rookStartPosDrawable = null;
    private Drawable rookEndPosDrawable = null;
    private String enPassantColor = null;
    private boolean undoOnce = false;
    private String startPos;
    private String endPos;
    String thirdResponse = null;
    int[] tempIntArray = new int[4];
    int[] posHighLighted = new int[4];
    Game game;
    Game tempGame;
    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_board);
        board = findViewById(R.id.chess_board);
        result = findViewById(R.id.editTextDialogUserInput);
        //Get all rows associated with the board
        int row_count = board.getChildCount();
        String tag = "";
        //Loop through all rows in the table layout
        for (int i = 0; i < row_count; i++) {
            //Get the ith row from the board
            View v = board.getChildAt(i);
            //Make sure it is an instance of table row
            if (v instanceof TableRow) {
                TableRow row = (TableRow) v;
                //Get all children of the row, ie all cells within that row
                int column_Count = row.getChildCount();
                for (int j = 0; j < column_Count; j++) {
                    //give each cell a tag to uniquely identify them
                    tag = i + "," + j;
                    //Set the tag for each particular cell
                    row.getChildAt(j).setTag(tag);
                    //Set an onclick listener to wait for user input
                    row.getChildAt(j).setOnClickListener(this);
                }
            }
        }
        //Initialize game instance
        game = new Game();
        tempGame = new Game();
        tempGame.setBoard(game.board);
        /*
         * Add function to clear txt file when upon start-up of a new game
         * */
        FileOutputStream fos = null;
        try{
            fos = openFileOutput("temp_game.txt", MODE_PRIVATE);
            System.out.println("Made file");
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(fos != null){
                try{
                    fos.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        //Initialize the chess board and Add the appropriate objects to the chess board
        game.setBoard(setUpBoard(game.getBoard()));
        player_turn_text = findViewById(R.id.current_player_text);
        player_turn_text.setText(R.string.white_player);
    }

    @Override
    public void onClick(View v) {
        drawBoard(game.board);
        String tempPos = v.getTag().toString();
        String[] tempArrayNewStartPos = tempPos.split(",", 2);
        tempIntArray[2] = Integer.parseInt(tempArrayNewStartPos[0]);
        tempIntArray[3] = Integer.parseInt(tempArrayNewStartPos[1]);
        System.out.print("value of temp int array is: ");
        for (int i: tempIntArray ){
            System.out.print(i+",");
        }
        System.out.println();
        if (startPos == null) { //First pick
            System.out.println("1");
            startPos = v.getTag().toString();
            String[] tempArray = startPos.split(",", 2);
            int[] checkArray = new int[2];
            checkArray[0] = Integer.parseInt(tempArray[0]);
            checkArray[1] = Integer.parseInt(tempArray[1]);
            if(game.board[checkArray[0]][checkArray[1]] != null && game.board[checkArray[0]][checkArray[1]].getColor().equals(game.getCurrentPlayerTurn())){
                tempIntArray[0] = Integer.parseInt(tempArray[0]);
                tempIntArray[1] = Integer.parseInt(tempArray[1]);
                startImgId = v.getId();
                allValidMoves(tempIntArray);
            }else{
                playSound(1);
                startPos = null;
            }
        }else if(startPos != null && game.board[tempIntArray[2]][tempIntArray[3]] != null && game.board[tempIntArray[2]][tempIntArray[3]].getColor().equals(game.getCurrentPlayerTurn())) {
            System.out.println("2");
            clearBoardBackground();
            startPos = v.getTag().toString();
            startImgId = v.getId();
            String[] tempArray = startPos.split(",", 2);
            tempIntArray[0] = Integer.parseInt(tempArray[0]);
            tempIntArray[1] = Integer.parseInt(tempArray[1]);
            allValidMoves(tempIntArray);
        }else if(startPos!= null){
            System.out.println("3");
            System.out.print("value of temp int array inside of number 3 is: ");
            for (int i: tempIntArray ){
                System.out.print(i+",");
            }
            System.out.println();
            System.out.println("Here is the current board: ");
            if(game.board[tempIntArray[0]][tempIntArray[1]].moveTo(game.board,tempIntArray,game)){
                endPos = v.getTag().toString();
                endImgId = v.getId();
                Log.d("my tag", "gonna pass the coordinates: " + startPos + " to: " + endPos + " to the game function");
                String[] tempArray = new String[4];
                //Combine the strings together to make full user input
                String userInput = startPos + "," + endPos;
                //Split the string up in to individual units
                tempArray = userInput.split(",", 4);
                final int[] cord = new int[4];
                for (int i = 0; i < 4; ++i) {
                    //Convert them into ints
                    cord[i] = Integer.parseInt(tempArray[i]);
                }

                System.out.print("Here are your coord after conversion:");
                for(int i : cord){
                    System.out.print(i+",");
                }
                System.out.println();
                makeMove(cord,tempArray);
            }else{
                //Do an animation here
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                findViewById(startImgId).startAnimation(shake);
                playSound(1);
            }
        }
    }

    public void makeMove(final int[] coord,String[] tempArray){
        boolean test = game.board[coord[0]][coord[1]].moveTo(game.board, coord,game);
        if (!test) {
            endPos = null;
        }else {
            //Try/Catch block to write coordinates to TXT file
            try{
                tempGame.setBoard(game.board);
                copy_startImgId = startImgId;
                copy_endImgId = endImgId;
                String formatedCoord = coord[0] + "," + coord[1] + "," + coord[2] + "," + coord[3];
                String phoneData = System.getProperty("user.dir") + "data/data/com.example.android34/files/";
                System.out.println("Phone Data path: " + phoneData);
                BufferedReader reader = new BufferedReader(new FileReader(phoneData + "temp_game.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter( phoneData + "temp_game.txt", true));
                //If file is empty then just do a simple insert
                if(reader.readLine() == null){
                    System.out.println("Writing coordinates: " + formatedCoord);
                    writer.write(formatedCoord);
                    writer.close();
                    reader.close();
                }else{
                    //File has an existing entry in it add new line to it
                    System.out.println("Appending coordinates: " + formatedCoord);
                    writer.write("\n" + formatedCoord);
                    writer.close();
                    reader.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }

            //Else send it over to checkRules to check for things like, castling,pawn Promotion, enPassant, check, checkmate and to carry out the actual move
            //Coord seems to change values after checkRules is called wtf!!!!!!
            if (game.board[coord[0]][coord[1]].getInitial().equals("p")) {
                if (coord[2] == 0) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.alert_dialog_title)
                            .setSingleChoiceItems(R.array.chess_pieces, 0, null)
                            .setPositiveButton(R.string.Accept, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    if (selectedPosition == 0) {
                                        endImg.setImageResource(R.drawable.white_queen);
                                    } else if (selectedPosition == 1) {
                                        endImg.setImageResource(R.drawable.white_bishop);
                                        game.board[coord[2]][coord[3]] = new Bishop();
                                        game.board[coord[2]][coord[3]].setColor("w");
                                    } else if (selectedPosition == 2) {
                                        endImg.setImageResource(R.drawable.white_knight);
                                        game.board[coord[2]][coord[3]] = new Knight();
                                        game.board[coord[2]][coord[3]].setColor("w");
                                    } else if (selectedPosition == 3) {
                                        endImg.setImageResource(R.drawable.white_rook);
                                        game.board[coord[2]][coord[3]] = new Rook();
                                        game.board[coord[2]][coord[3]].setColor("w");
                                    }
                                }
                            })
                            .show().setCanceledOnTouchOutside(false);
                } else if (coord[2] == 7) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.alert_dialog_title)
                            .setSingleChoiceItems(R.array.chess_pieces, 0, null)
                            .setPositiveButton(R.string.Accept, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    if (selectedPosition == 0) {
                                        endImg.setImageResource(R.drawable.black_queen);
                                    } else if (selectedPosition == 1) {
                                        endImg.setImageResource(R.drawable.black_bishop);
                                        game.board[coord[2]][coord[3]] = new Bishop();
                                        game.board[coord[2]][coord[3]].setColor("b");
                                    } else if (selectedPosition == 2) {
                                        endImg.setImageResource(R.drawable.black_knight);
                                        game.board[coord[2]][coord[3]] = new Knight();
                                        game.board[coord[2]][coord[3]].setColor("b");
                                    } else if (selectedPosition == 3) {
                                        endImg.setImageResource(R.drawable.black_rook);
                                        game.board[coord[2]][coord[3]] = new Rook();
                                        game.board[coord[2]][coord[3]].setColor("b");
                                    }
                                }
                            }).show().setCanceledOnTouchOutside(false);
                }
            }
            game.board = ChessRules.checkRules(game.board, coord, thirdResponse, game);
            for (int i = 0; i < 4; ++i) {
                //Copy the values again because of some stupid magic that changes values in the background!!!!
                coord[i] = Integer.parseInt(tempArray[i]);
                posHighLighted[i] = Integer.parseInt(tempArray[i]);
            }

            if (game.board[coord[2]][coord[3]].getColor().equals("w")) {
                player_turn_text.setText(R.string.black_player);
            } else {
                player_turn_text.setText(R.string.white_player);
            }
            //Check to see if castling happened
            if (game.board[coord[2]][coord[3]].getInitial().equals("K") && Math.abs(coord[1] - coord[3]) == 2) {
                //Figure out if it was to the left or right
                int offset = coord[1] - coord[3];
                startImg = findViewById(startImgId);
                endImg = findViewById(endImgId);
                startPosDrawable = startImg.getDrawable();
                endPosDrawable = endImg.getDrawable();
                endImg.setImageDrawable(startImg.getDrawable());
                startImg.setImageDrawable(null);
                if (offset < 0) {//moved to the right
                    if (game.board[coord[2]][coord[3]].getColor().equals("w")) {
                        ImageView rookNewPos = findViewById(R.id.row0col5);
                        ImageView rookOldPos = findViewById(R.id.row0col7);
                        copy_rookStartImgId = rookOldPos.getId();
                        copy_rookEndImgId = rookNewPos.getId();
                        rookStartPosDrawable = rookOldPos.getDrawable();
                        rookEndPosDrawable = rookNewPos.getDrawable();
                        rookNewPos.setImageResource(R.drawable.white_rook);
                        rookOldPos.setImageDrawable(null);
                    } else {
                        ImageView rookNewPos = findViewById(R.id.row7col5);
                        ImageView rookOldPos = findViewById(R.id.row7col7);
                        copy_rookStartImgId = rookOldPos.getId();
                        copy_rookEndImgId = rookNewPos.getId();
                        rookStartPosDrawable = rookOldPos.getDrawable();
                        rookEndPosDrawable = rookNewPos.getDrawable();
                        rookNewPos.setImageResource(R.drawable.black_rook);
                        rookOldPos.setImageDrawable(null);
                    }
                } else {//moved to the left
                    if (game.board[coord[2]][coord[3]].getColor().equals("w")) {
                        ImageView rookNewPos = findViewById(R.id.row0col3);
                        ImageView rookOldPos = findViewById(R.id.row0col0);
                        copy_rookStartImgId = rookOldPos.getId();
                        copy_rookEndImgId = rookNewPos.getId();
                        rookStartPosDrawable = rookOldPos.getDrawable();
                        rookEndPosDrawable = rookNewPos.getDrawable();
                        rookNewPos.setImageResource(R.drawable.white_rook);
                        rookOldPos.setImageDrawable(null);
                    } else {
                        ImageView rookNewPos = findViewById(R.id.row7col3);
                        ImageView rookOldPos = findViewById(R.id.row7col0);
                        copy_rookStartImgId = rookOldPos.getId();
                        copy_rookEndImgId = rookNewPos.getId();
                        rookStartPosDrawable = rookOldPos.getDrawable();
                        rookEndPosDrawable = rookNewPos.getDrawable();
                        rookNewPos.setImageResource(R.drawable.black_rook);
                        rookOldPos.setImageDrawable(null);
                    }
                }
                playSound(0);
                game.increaseTurnCount();
                startPos = null;
                endPos = null;
                undoOnce = false;
                copy_enPassantImgId = -1;
                enPassantColor = null;
                clearBoardBackground();
                startImg.setBackgroundResource(R.drawable.previous_square);
                endImg.setBackgroundResource(R.drawable.current_square);
                drawBoard(game.getBoard());
                //CHeck to see if enpansant or pawn promotion happened
            } else if (game.board[coord[2]][coord[3]].getInitial().equals("p") && Math.abs(coord[0]-coord[2]) == 1 && Math.abs(coord[1] - coord[3]) == 1) {
                View rowSlice;
                TableRow row;
                ImageView image;
                startImg = findViewById(startImgId);
                endImg = findViewById(endImgId);
                startPosDrawable = startImg.getDrawable();
                endPosDrawable = endImg.getDrawable();
                endImg.setImageDrawable(startImg.getDrawable());
                startImg.setImageDrawable(null);
                if (game.board[coord[2]][coord[3]].getColor().equals("w")) {
                    rowSlice = board.getChildAt(coord[2] + 1);
                    //Make sure it is an instance of table row
                    if (rowSlice instanceof TableRow) {
                        row = (TableRow) rowSlice;
                        image = (ImageView) row.getChildAt(coord[3]);
                        if (game.board[coord[2] + 1][coord[3]] == null) {
                            copy_enPassantImgId = image.getId();
                            enPassantColor = "b";
                            image.setImageDrawable(null);
                        }
                    }
                } else {
                    rowSlice = board.getChildAt(coord[2] - 1);
                    if (rowSlice instanceof TableRow) {
                        row = (TableRow) rowSlice;
                        image = (ImageView) row.getChildAt(coord[3]);
                        if (game.board[coord[2] - 1][coord[3]] == null) {
                            copy_enPassantImgId = image.getId();
                            enPassantColor = "w";
                            image.setImageDrawable(null);
                        }
                    }
                }
                playSound(0);
                game.increaseTurnCount();
                startPos = null;
                endPos = null;
                copy_rookStartImgId = -1;
                copy_rookEndImgId = -1;
                rookStartPosDrawable = null;
                rookEndPosDrawable = null;
                undoOnce = false;
                clearBoardBackground();
                startImg.setBackgroundResource(R.drawable.previous_square);
                endImg.setBackgroundResource(R.drawable.current_square);
                drawBoard(game.getBoard());
            } else {
                playSound(0);
                startImg = findViewById(startImgId);
                endImg = findViewById(endImgId);
                startPosDrawable = startImg.getDrawable();
                endPosDrawable = endImg.getDrawable();
                endImg.setImageDrawable(startImg.getDrawable());
                startImg.setImageDrawable(null);
                game.increaseTurnCount();
                startPos = null;
                endPos = null;
                //Make the rook and pawn stuff all -1 and null
                copy_rookStartImgId = -1;
                copy_rookEndImgId = -1;
                rookStartPosDrawable = null;
                rookEndPosDrawable = null;
                copy_enPassantImgId = -1;
                enPassantColor = null;
                undoOnce = false;
                clearBoardBackground();
                startImg.setBackgroundResource(R.drawable.previous_square);
                endImg.setBackgroundResource(R.drawable.current_square);
                drawBoard(game.getBoard());
            }
        }
        System.out.println("current player turn is: "+ game.getCurrentPlayerTurn());
        System.out.println("here is white status: "+game.getWhitePlayer());
        System.out.println("here is black status: "+game.getBlackPlayer());
        //Check if player is in check or check mate!
        if(game.getCurrentPlayerTurn().equals("w")){
            if(game.getWhitePlayer() == 4){//In check
                Toast toast=Toast.makeText(getApplicationContext(),"White is in check",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM , 0, 0);
                toast.show();
                //This doesn't work
                //highLightKing("w");
            }else if(game.getWhitePlayer() == 5){//In check mate
                drawWinScreen("b");
            }
        }else{
            if(game.getBlackPlayer() == 4){//In check
                Toast toast=Toast.makeText(getApplicationContext(),"Black is in check",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM , 0, 0);
                toast.show();
                //this doesn't work
                //highLightKing("b");
            }else if(game.getBlackPlayer() == 5){//In check mate
                drawWinScreen("w");
            }
        }
    }

    public void highLightKing(String color){
        //System.out.println("In highlightking with color: "+ color);
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
                    System.out.println(i+","+j);
                    ImageView tile = (ImageView) row.getChildAt(j);
                    if(game.board[i][j] != null && game.board[i][j].getidentifier().equals("K") && game.board[i][j].getColor().equals(color)){
                        //System.out.println("i'm here!");
                        tile.setBackgroundResource(R.drawable.red_square);
                    }
                }
            }
        }
    }

    public void playSound(int x){
        MediaPlayer mp_xmPlayer2 = new MediaPlayer();
        //0 for move sound, 1 for error sound
        if(x == 0){
            mp_xmPlayer2 = MediaPlayer.create(this, R.raw.move_piece);
        }else if(x == 1){
            mp_xmPlayer2 = MediaPlayer.create(this, R.raw.error);
        }
        mp_xmPlayer2.start();
        mp_xmPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public void drawWinScreen(String player){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        if(player.equals("w")){
            alertDialog.setTitle(R.string.white_wins);
        }else if(player.equals("b")){
            alertDialog.setTitle(R.string.black_wins);
        }else if(player.equals("d")){
            alertDialog.setTitle("Draw");
        }

        if(!player.equals("d")){
            alertDialog.setMessage("Congratulations, You win!");
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Main Menu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //go to main menu from here
                Intent intent = new Intent(chess.this, MainActivity.class);
                startActivity(intent);
            } });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Play Again?", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                //reset game values
                game.setBlackPlayer(0);
                game.setWhitePlayer(0);
                game.setTurncount(1);
                player_turn_text.setText(R.string.white_player);

                //reset the main board
                setUpBoard(game.board);
                //Reset the board that the user interacts with
                clearBoardBackground();
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
                            tile.setImageDrawable(null);
                        }
                    }
                }
                //Black pieces set up first
                ImageView image = findViewById(R.id.row7col0);
                image.setImageResource(R.drawable.black_rook);
                image = findViewById(R.id.row7col1);
                image.setImageResource(R.drawable.black_knight);
                image = findViewById(R.id.row7col2);
                image.setImageResource(R.drawable.black_bishop);
                image = findViewById(R.id.row7col3);
                image.setImageResource(R.drawable.black_queen);
                image = findViewById(R.id.row7col4);
                image.setImageResource(R.drawable.black_king);
                image = findViewById(R.id.row7col5);
                image.setImageResource(R.drawable.black_bishop);
                image = findViewById(R.id.row7col6);
                image.setImageResource(R.drawable.black_knight);
                image = findViewById(R.id.row7col7);
                image.setImageResource(R.drawable.black_rook);
                //Black pawns
                image = findViewById(R.id.row6col0);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col1);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col2);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col3);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col4);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col5);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col6);
                image.setImageResource(R.drawable.black_pawn);
                image = findViewById(R.id.row6col7);
                image.setImageResource(R.drawable.black_pawn);

                //White pieces set up
                image = findViewById(R.id.row0col0);
                image.setImageResource(R.drawable.white_rook);
                image = findViewById(R.id.row0col1);
                image.setImageResource(R.drawable.white_knight);
                image = findViewById(R.id.row0col2);
                image.setImageResource(R.drawable.white_bishop);
                image = findViewById(R.id.row0col3);
                image.setImageResource(R.drawable.white_queen);
                image = findViewById(R.id.row0col4);
                image.setImageResource(R.drawable.white_king);
                image = findViewById(R.id.row0col5);
                image.setImageResource(R.drawable.white_bishop);
                image = findViewById(R.id.row0col6);
                image.setImageResource(R.drawable.white_knight);
                image = findViewById(R.id.row0col7);
                image.setImageResource(R.drawable.white_rook);
                //Black pawns
                image = findViewById(R.id.row1col0);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col1);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col2);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col3);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col4);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col5);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col6);
                image.setImageResource(R.drawable.white_pawn);
                image = findViewById(R.id.row1col7);
                image.setImageResource(R.drawable.white_pawn);

                //reset game file
                FileOutputStream fos = null;
                try{
                    fos = openFileOutput("temp_game.txt", MODE_PRIVATE);
                    //System.out.println("Reseting file");
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    if(fos != null){
                        try{
                            fos.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }});
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Save Game", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(getApplicationContext(), "This is the save button!", Toast.LENGTH_SHORT).show();
                LayoutInflater inflate = LayoutInflater.from(chess.this);
                View promptsView = inflate.inflate(R.layout.save_game, null);

                AlertDialog.Builder saveGame = new AlertDialog.Builder(chess.this);
                saveGame.setView(promptsView);
                final EditText save_input = promptsView.findViewById(R.id.editTextDialogUserInput);

                saveGame.setCancelable(false).setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String gameName = String.valueOf(save_input.getText());
                                Toast.makeText(getApplicationContext(), save_input.getText(), Toast.LENGTH_SHORT).show();
                                try {
                                    FileOutputStream out = openFileOutput(gameName + ".txt", MODE_PRIVATE);;
                                    String phoneData = System.getProperty("user.dir") + "data/data/com.example.android34/files/";
                                    InputStream in = new FileInputStream(phoneData + "temp_game.txt");
                                    OutputStream output = new FileOutputStream(phoneData + gameName + ".txt");
                                    byte[] buffer = new byte[1024];
                                    int length;
                                    while( (length = in.read(buffer)) > 0){
                                        out.write(buffer,0,length);
                                    }

                                    out.close();
                                    in.close();
                                    output.close();
                                }catch(IOException e){
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(chess.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = saveGame.create();
                alert.show();

            }});
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    public static void drawBoard(ChessPiece[][] board) {

        int i,j;
        //Display the board
        for (i = 0; i < 8; ++i) {
            for(j = 0; j < 8; ++j) {
                if(board[i][j] != null) {
                    System.out.print(board[i][j].getidentifier()+ " ");
                }else {
                    if((i + j) % 2 == 0)
                        System.out.print("   ");
                    else
                        System.out.print("## ");
                }
                if(j == 7)
                    System.out.println(8-i);
            }
            if(i == 7)
                System.out.println(" a  b  c  d  e  f  g  h");
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

    public void clearBoardBackground(){
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
                    if((i == posHighLighted[0] && j == posHighLighted[1])){
                        tile.setBackgroundResource(R.drawable.previous_square);
                    }else if((i == posHighLighted[2] && j == posHighLighted[3])){
                        tile.setBackgroundResource(R.drawable.current_square);
                    }else{
                        if((i + j) % 2 == 0) {
                            tile.setBackgroundResource(R.drawable.white_square);
                        } else {
                            tile.setBackgroundResource(R.drawable.dark_square);
                        }
                    }
                }
            }
        }
    }

    public void allValidMoves(int[] tempCord){
        boolean flag = false;
        for (int i = 0; i < 8; i++) {
            //Get the ith row from the board
            int [] ex = new int[4];
            boolean tempCastle = false;
            boolean tempMoved = false;
            ex[0] = tempCord[0];
            ex[1] = tempCord[1];
            View v_temp = board.getChildAt(i);
            //Make sure it is an instance of table row
            if (v_temp instanceof TableRow) {
                TableRow row = (TableRow) v_temp;
                //Get all children of the row, ie all cells within that row
                for (int j = 0; j < 8; j++) {
                    //Split the string up in to individual units
                    ex[2] = i;
                    ex[3] = j;
                    if(game.board[ex[0]][ex[1]].getInitial().equals("K")){
                        tempCastle = game.board[ex[0]][ex[1]].getHasCastled();
                        tempMoved = game.board[ex[0]][ex[1]].getHasMoved();
                    }
                    if(game.board[ex[0]][ex[1]].getInitial().equals("R")){
                        tempCastle = game.board[ex[0]][ex[1]].getHasCastled();
                        tempMoved = game.board[ex[0]][ex[1]].getHasMoved();
                    }
                    if(game.board[ex[0]][ex[1]].moveTo(game.board,ex,game)){
                        flag = true;
                        if(game.board[ex[0]][ex[1]].getInitial().equals("p")){
                            game.board[ex[0]][ex[1]].setEnPassant(false);
                            game.board[ex[0]][ex[1]].setHasMoved(false);
                        }else if(game.board[ex[0]][ex[1]].getInitial().equals("K")){
                            if(game.board[ex[0]][ex[1]].getHasCastled()){
                                game.board[ex[0]][ex[1]].setHasCastled(tempCastle);
                            }
                            game.board[ex[0]][ex[1]].setHasMoved(tempMoved);
                        }else if(game.board[ex[0]][ex[1]].getInitial().equals("R")){
                            if(game.board[ex[0]][ex[1]].getHasCastled()){
                                game.board[ex[0]][ex[1]].setHasCastled(tempCastle);
                            }
                            game.board[ex[0]][ex[1]].setHasMoved(tempMoved);
                        }
                        ImageView tile = (ImageView) row.getChildAt(j);
                        if((i == posHighLighted[0] && j == posHighLighted[1])){
                            tile.setBackgroundResource(R.drawable.previous_square_valid_move);
                        }else if((i == posHighLighted[2] && j == posHighLighted[3])){
                            tile.setBackgroundResource(R.drawable.current_square_valid_move);
                        }else{
                            if((i + j) % 2 == 0) {
                                tile.setBackgroundResource(R.drawable.white_square_valid_move);
                            } else {
                                tile.setBackgroundResource(R.drawable.dark_square_valid_move);
                            }
                        }
                    }
                }
            }
        }
        if(!flag){
            //Do an animation here
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(startImgId).startAnimation(shake);
            playSound(1);
        }
    }
    //Buttons below

    public void drawButton(View view) {
        Toast.makeText(getApplicationContext(), "Draw button tapped", Toast.LENGTH_SHORT).show();
        String player = game.getCurrentPlayerTurn();
        AlertDialog.Builder acceptResignation = new AlertDialog.Builder(this);
        acceptResignation.setTitle("Call Draw");
        if(player.equals("w")){
            acceptResignation.setMessage("Black do you accept draw with White?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                /*
                 * By default just cancel until I figure out what else to do
                 * */
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    drawWinScreen("d");
                }
            });
            acceptResignation.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }else{
            acceptResignation.setMessage("White do you accept draw with Black?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            drawWinScreen("d");
                        }
                    });
            acceptResignation.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        AlertDialog drawDialog = acceptResignation.create();
        drawDialog.show();
    }

    public void undoButton(View view) {
        if(!undoOnce){
            ImageView tempStartImg;
            ImageView tempEndImg;
            ImageView tempRookStartImg;
            ImageView tempRookEndImg;
            ImageView tempPassantImg;
            Toast.makeText(getApplicationContext(), "Undoing Move", Toast.LENGTH_SHORT).show();
            RandomAccessFile randomFile = null;
            game.setBoard(tempGame.getBoard());
            tempStartImg = findViewById(copy_startImgId);
            tempEndImg = findViewById(copy_endImgId);
            tempStartImg.setImageDrawable(startPosDrawable);
            tempEndImg.setImageDrawable(endPosDrawable);
            if(copy_rookStartImgId != -1 && copy_rookEndImgId != 1){
                tempRookStartImg = findViewById(copy_rookStartImgId);
                tempRookEndImg = findViewById(copy_rookEndImgId);
                tempRookStartImg.setImageDrawable(rookStartPosDrawable);
                tempRookEndImg.setImageDrawable(rookEndPosDrawable);
                copy_rookStartImgId = -1;
                copy_rookEndImgId = -1;
                rookStartPosDrawable = null;
                rookEndPosDrawable = null;
            }else if(copy_enPassantImgId != -1){
                tempPassantImg = findViewById(copy_enPassantImgId);
                //System.out.println("Here is the tag of the enPassant" + tempPassantImg.getTag());
                if (enPassantColor.equals("w")) {
                    tempPassantImg.setImageResource(R.drawable.white_pawn);
                }else{
                    tempPassantImg.setImageResource(R.drawable.black_pawn);
                }
                copy_enPassantImgId = -1;
                enPassantColor = null;
            }
            game.decreaseTurnCount();
            if(game.getCurrentPlayerTurn().equals("w")){
                player_turn_text.setText(R.string.white_player);
            }else{
                player_turn_text.setText(R.string.black_player);
            }
            try{
                String phoneData = System.getProperty("user.dir") + "data/data/com.example.android34/files/";
                randomFile = new RandomAccessFile(new File(phoneData + "temp_game.txt"), "rw");
                byte bit;
                long length = randomFile.length();
                //System.out.println("File Length: " + length);
                if(length != 0){
                    //Only one line
                    System.out.println("Deleting last line");
                    if(length == 7){
                        randomFile.setLength(0);
                    }else
                        randomFile.setLength(length - 8);
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                if(randomFile != null){
                    try{
                        randomFile.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
            undoOnce = true;

        }else{
            Toast.makeText(getApplicationContext(), "Already tapped undo", Toast.LENGTH_SHORT).show();
        }
    }

    public void AIButton(View view) {
        //System.out.println("This is the AI button");
        //figure out who's turn it is, figure out how many moves they currently have available and randomly select one from that list
        int available_moves = 0;
        int move_selected = 0;
        View v_temp;
        int[] temp_Coord = new int[4];
        String[] temp_String_Array = new String[4];
        boolean test = false;
        String current_player = game.getCurrentPlayerTurn();
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 8; ++j){
                if(game.board[i][j] != null && game.board[i][j].getColor().equals(current_player)){
                   //Now do a loop with the piece selected on all position's on the board to see how many moves that piece can make
                    for(int m = 0; m < 8; ++m){
                        for(int n = 0; n < 8; ++n){
                            temp_Coord[0] = i;
                            temp_Coord[1] = j;
                            temp_Coord[2] = m;
                            temp_Coord[3] = n;
                            test = game.board[i][j].moveTo(game.board,temp_Coord,game);
                            if(test){
                                ++available_moves;
                            }
                        }
                    }
                }
            }
        }
        //Toast.makeText(getApplicationContext(),"Number of Available moves are " +available_moves,Toast.LENGTH_SHORT).show();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(available_moves) + 1;
        for(int i = 0; i < 8; ++i){
            v_temp = board.getChildAt(i);
            //Make sure it is an instance of table row
            if (v_temp instanceof TableRow) {
                TableRow row = (TableRow) v_temp;
                for(int j = 0; j < 8; ++j){
                    if(game.board[i][j] != null && game.board[i][j].getColor().equals(current_player)){
                        //Now do a loop with the piece selected on all position's on the board to see how many moves that piece can make
                        for(int m = 0; m < 8; ++m){
                            for(int n = 0; n < 8; ++n){
                                temp_Coord[0] = i;
                                temp_Coord[1] = j;
                                temp_Coord[2] = m;
                                temp_Coord[3] = n;
                                test = game.board[i][j].moveTo(game.board,temp_Coord,game);
                                if(test){
                                    ++move_selected;
                                    if(move_selected == randomInt){
                                        //Also have to get values for startPos, startImgId, endPos and endImgId
                                        ImageView tile = (ImageView) row.getChildAt(j);
                                        startImgId = tile.getId();
                                        v_temp = board.getChildAt(m);
                                        TableRow new_row = (TableRow) v_temp;
                                        tile = (ImageView)new_row.getChildAt(n);
                                        endImgId = tile.getId();
                                        temp_String_Array[0] = Integer.toString(temp_Coord[0]);
                                        temp_String_Array[1] = Integer.toString(temp_Coord[1]);
                                        temp_String_Array[2] = Integer.toString(temp_Coord[2]);
                                        temp_String_Array[3] = Integer.toString(temp_Coord[3]);
                                        makeMove(temp_Coord,temp_String_Array);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void resignButton(View view) {
        String player = game.getCurrentPlayerTurn();
        //If player is white then black wins
        if(player.equals("w")){
            drawWinScreen("b");
        }else{
            drawWinScreen("w");
        }
    }
}