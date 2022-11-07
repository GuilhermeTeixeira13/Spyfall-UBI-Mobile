package pt.ubi.di.pmd.spyfall_ubi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class GameONActivity extends AppCompatActivity {
    ArrayList<Player> players;
    Place place;
    int playerStarting;
    int playerPlaying;
    TextView txtViewPlayer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameon);

        Intent intent = getIntent();
        String checkFlag= intent.getStringExtra("flag");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtViewTimer = (TextView) findViewById(R.id.textViewTimer);
        txtViewPlayer = (TextView) findViewById(R.id.textViewWhosPlaying);
        setSupportActionBar(toolbar);

        if(checkFlag.equals("FROM_READY")){
            players = (ArrayList<Player>) getIntent().getSerializableExtra("PLAYERS");
            place = (Place) getIntent().getSerializableExtra("PLACE");
            playerStarting = (int) getIntent().getSerializableExtra("PLAYER_STARTING");
            playerPlaying = playerStarting;
            txtViewPlayer.setText("Playing: "+ players.get(playerPlaying).getName());
        }

        new CountDownTimer(480000, 1000) {
            String min, sec;
            public void onTick(long millisUntilFinished) {
                min = String.valueOf(millisUntilFinished / (60 * 1000) % 60);
                sec = String.valueOf(millisUntilFinished / 1000 % 60);

                if (Long.parseLong(sec) < 10)
                    sec = "0"+String.valueOf(millisUntilFinished / 1000 % 60);

                txtViewTimer.setText(min+":"+sec);
            }

            public void onFinish() {
                txtViewTimer.setText("done!");
            }

        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_rest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareButton:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Link to Playstore";
                String shareSubject = "Spyfall @ UBI!";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share using:"));
                break;
            case R.id.homeButton:
                // Ask if we want to the lobby and lose all the current page settings

                new AlertDialog.Builder(GameONActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you going to the main page?")
                        .setMessage("Do you want to lose the current game state and go back to the main page?")
                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goToMainActivity();
                            }
                        })
                        .setNegativeButton("No!", null)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToMainActivity () {
        Intent goToMainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(goToMainActivityIntent);
    }

    public void revealSpy(View v){

    }

    public void revealLocation(View v){

    }

    public void playerBack(View v){
        playerPlaying -= 1;

        if(playerPlaying < 0)
            playerPlaying = players.size() - 1;

        txtViewPlayer.setText("Playing: "+ players.get(playerPlaying).getName());
    }

    public void playerNext(View v){
        playerPlaying += 1;

        if(playerPlaying >= players.size())
            playerPlaying = 0;

        txtViewPlayer.setText("Playing: "+ players.get(playerPlaying).getName());
    }
}