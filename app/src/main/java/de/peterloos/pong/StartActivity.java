package de.peterloos.pong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start);

        Button buttonSinglePlayer = (Button) this.findViewById(R.id.button_single_player);
        Button buttonMultiPlayer = (Button) this.findViewById(R.id.button_multi_player);
        Button buttonDemoPlayer = (Button) this.findViewById(R.id.button_demo_player);

        if (buttonSinglePlayer != null) {
            buttonSinglePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, PongActivity.class);
                    intent.putExtra(Globals.GameType, "Single");
                    startActivity(intent);
                }
            });
        }

        if (buttonMultiPlayer != null) {
            buttonMultiPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, PongActivity.class);
                    intent.putExtra(Globals.GameType, "Multi");
                    startActivity(intent);
                }
            });
        }

        if (buttonDemoPlayer != null) {
            buttonDemoPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, PongActivity.class);
                    intent.putExtra(Globals.GameType, "Demo");
                    startActivity(intent);
                }
            });
        }
    }
}
