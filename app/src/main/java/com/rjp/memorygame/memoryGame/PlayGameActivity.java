package com.rjp.memorygame.memoryGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rjp.memorygame.R;

public class PlayGameActivity extends Activity {

    private Context mContext;

    public static void start(Context mContext, int count){
        Intent intent = new Intent(mContext, PlayGameActivity.class);
        intent.putExtra("count", count);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        mContext = this;

        MemoryGameView gameView = findViewById(R.id.memory_game);
        gameView.setOnMemoryGameListener(new OnMemoryGameListener() {
            @Override
            public void gameOver() {
                ResultActivity.start(mContext, "error");
                finish();
            }

            @Override
            public void gameSuccess(String level) {
                ResultActivity.start(mContext, level);
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("count")){
            int count = intent.getIntExtra("count", 5);
            gameView.start(count);
        }
    }
}
