package com.example.molegame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout background;
    TextView time, count, tv_level;
    Button start, method, btn_continue;
    ImageView weapon;
    ImageView[] img_array = new ImageView[14];
    int[] imageID = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6,
            R.id.imageView7, R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12, R.id.imageView13, R.id.imageView14};
    String TAG_ON = "on";
    String TAG_OFF = "off";
    int score = 0;
    int level = 1;
    int[] speedX = new int[14];
    int[] speedY = new int[14];

    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.tv_time);
        count = findViewById(R.id.tv_count);
        tv_level = findViewById(R.id.tv_level);
        start = findViewById(R.id.btn_start);
        method = findViewById(R.id.btn_method);
        btn_continue = findViewById(R.id.btn_continue);
        background = findViewById(R.id.background);
        weapon = findViewById(R.id.weapon);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;

        level = getIntent().getIntExtra("level", 1);
        tv_level.setText("Level : " + level);

        for (int i = 0; i < 14; i++) {
            img_array[i] = findViewById(imageID[i]);
            img_array[i].setImageResource(R.drawable.ic_favorite);
            img_array[i].setVisibility(View.INVISIBLE);
            img_array[i].setTag(TAG_OFF);

            speedX[i] = new Random().nextInt(5 + (level - 1) * 10) + 1;
            speedY[i] = new Random().nextInt(5 + (level - 1) * 10) + 1;

            img_array[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ImageView) v).getTag().toString().equals(TAG_ON)) {
                        count.setText(String.valueOf(++score) + "점");
                        v.setBackgroundResource(R.drawable.cockdie);
                        ((ImageView) v).setVisibility(View.INVISIBLE);
                        v.setTag(TAG_OFF);
                    } else {
                        count.setText(String.valueOf(--score) + "점");
                    }
                }
            });
        }

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                weapon.setX(event.getRawX() - 150);
                weapon.setY(event.getRawY() - 150);
                weapon.setVisibility(View.VISIBLE);
                new Thread(new weaponThread()).start();

                System.out.println(event.getX() + " " + event.getY());
                return false;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                method.setVisibility(View.GONE);
                new Thread(new timeCheck()).start();
                for (int i = 0; i < 14; i++) {
                    new Thread(new moveThread(i)).start();
                    new Thread(new DThread(i)).start();
                }
            }
        });

        method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MethodActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    Handler onHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            img_array[msg.arg1].setVisibility(View.VISIBLE);
            if (msg.arg1 < 9) {
                img_array[msg.arg1].setImageResource(R.drawable.cockcock);
                img_array[msg.arg1].setTag(TAG_ON);
            } else {
                img_array[msg.arg1].setImageResource(R.drawable.spider);
                img_array[msg.arg1].setTag(TAG_OFF);
            }
        }
    };

    Handler offHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            img_array[msg.arg1].setVisibility(View.INVISIBLE);
            img_array[msg.arg1].setTag(TAG_OFF);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            time.setText(msg.arg1 + "초");
        }
    };

    Handler moveHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            img_array[msg.arg1].setX(img_array[msg.arg1].getX() + speedX[msg.arg1]);
            img_array[msg.arg1].setY(img_array[msg.arg1].getY() + speedY[msg.arg1]);
            if (img_array[msg.arg1].getX() < 0 || img_array[msg.arg1].getX() > width - 420) {
                speedX[msg.arg1] = -speedX[msg.arg1];
            }
            if (img_array[msg.arg1].getY() < 0 || img_array[msg.arg1].getY() > height - 700) {
                speedY[msg.arg1] = -speedY[msg.arg1];
            }
        }
    };

    Handler weaponHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            weapon.setVisibility(View.GONE);
        }
    };

    public class weaponThread implements Runnable {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                Thread.sleep(500);
                msg.arg1 = 1;
                weaponHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class moveThread implements Runnable {
        int index = 0;

        public moveThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message msg = new Message();
                    Thread.sleep(10);
                    msg.arg1 = index;
                    moveHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public class DThread implements Runnable {
        int index = 0;

        DThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message msg1 = new Message();
                    int offtime = new Random().nextInt(1000) + 500;
                    Thread.sleep(offtime);
                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    Message msg2 = new Message();
                    int ontime = new Random().nextInt(1000) + 500;
                    Thread.sleep(ontime);
                    msg2.arg1 = index;
                    offHandler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    int times = 10;
    Boolean pause = false;

    public class timeCheck implements Runnable {

        @Override
        public void run() {
            while (times >= 0 && pause == false) {
                try {
                    Message msg = new Message();
                    msg.arg1 = times--;
                    handler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (times == -1) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("level", level);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btn_continue.setVisibility(View.VISIBLE);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                pause = false;
                new Thread(new timeCheck()).start();
            }
        });
    }
}
