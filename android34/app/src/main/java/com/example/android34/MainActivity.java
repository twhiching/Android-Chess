package com.example.android34;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String gameSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chess_button = findViewById(R.id.chess_button);
        chess_button.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, chess.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void replayButton(View view) {
        //Toast.makeText(this, "In replay button", Toast.LENGTH_SHORT).show();

        String path = System.getProperty("user.dir") + "data/data/com.example.android34/files/";
        List<String> listOfgames = null;
        try {
            listOfgames = new ArrayList<>();
            File folder = new File(path);
            for (File fileEntry : folder.listFiles()) {
                String fileText = fileEntry.getCanonicalPath();
                if(fileText.endsWith(".txt") && !fileEntry.getName().equals("temp_game.txt")) {
                    System.out.println("GAME NAME: " + fileEntry.getName());
                    listOfgames.add(fileEntry.getName());
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        if(listOfgames.size() == 0){
            AlertDialog.Builder noSavedgames = new AlertDialog.Builder(this);
            noSavedgames.setTitle("No Games Exists");
            noSavedgames.setMessage("Play and save games to use this");
            noSavedgames.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        final AlertDialog.Builder replayGame = new AlertDialog.Builder(this);
        replayGame.setTitle("Previously Played Games");

        int checkItem = 1;
        final String[] gamesArray = listOfgames.toArray(new String[0]);
        replayGame.setSingleChoiceItems(gamesArray, checkItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                String selectedGame = gamesArray[index];
                gameSelected = selectedGame;
            }
        });

        replayGame.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*
                 * Bring game information when game is selected and scene is changed
                 * */
                Intent intent = new Intent(MainActivity.this, replayGame.class);
                intent.putExtra("gameName",gameSelected);
                startActivity(intent);
            }
        });

        replayGame.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = replayGame.create();
        dialog.show();
    }
}
