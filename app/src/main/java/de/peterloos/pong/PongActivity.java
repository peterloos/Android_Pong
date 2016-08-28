package de.peterloos.pong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PongActivity extends AppCompatActivity implements View.OnClickListener {

    private PongView pongView;
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pong);

        this.pongView = (PongView) this.findViewById(R.id.pong_view);
        this.buttonStart = (Button) this.findViewById(R.id.button_start);
        this.buttonStop = (Button) this.findViewById(R.id.button_stop);
        TextView textviewScoreboard = (TextView) this.findViewById(R.id.textview_gamescore);
        this.pongView.setScoreboardView(textviewScoreboard);

        this.buttonStart.setOnClickListener(this);
        this.buttonStop.setOnClickListener(this);

        // retrieve game mode
        Intent intent = this.getIntent();
        String mode = intent.getStringExtra(Globals.GameType);
        if (mode.equals("Single"))
            this.pongView.setGameType(GameType.SinglePlayerGame);
        else if (mode.equals("Multi"))
            this.pongView.setGameType(GameType.MultiPlayerGame);
        else if (mode.equals("Demo"))
            this.pongView.setGameType(GameType.DemoPlayerGame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_highscore) {
            Toast.makeText(this, "Pressed Highscore", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == this.buttonStart) {
            this.pongView.Start();

        } else if (view == this.buttonStop) {
            this.pongView.Stop();
        }
    }
}
