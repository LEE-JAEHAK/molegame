package com.example.molegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    Button button, nextlevel;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        nextlevel = findViewById(R.id.btn_nextlevel);

        int score = getIntent().getIntExtra("score", 1);
        textView.setText(String.valueOf(score) + "점 입니다.");

        int level = getIntent().getIntExtra("level", 1);
        if (score > level) {
            level++;
        }else{
            Toast.makeText(this,"바퀴벌레를 덜 잡아서 다음단계로 넘어갈수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        final int tmp = level;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nextlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.putExtra("level", tmp);
                startActivity(intent);
                finish();
            }
        });
    }
}
