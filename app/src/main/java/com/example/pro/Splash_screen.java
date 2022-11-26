package com.example.pro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_screen extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView text1, text2;
    private static int SPLASH_SCREEN = 5000;
    SharedPreferences preferences,sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = sh.edit();
//        editor.putBoolean("isUserLogin", false);
//        editor.apply();
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.topanim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottomanim);

        image = findViewById(R.id.profile_image);
        text1 = findViewById(R.id.textView3);

        //aimation seting
        image.setAnimation(topAnim);
        text1.setAnimation(bottomAnim);
        //  text2.setAnimation(bottomAnim);
        if (preferences.contains("isUserLogin")) {
            Intent intent = new Intent(Splash_screen.this, Driver_Home.class);
            startActivity(intent);
        }else if (preferences.contains("isStudentLogin")){
            Intent intent = new Intent(Splash_screen.this, Home_Student.class);
            startActivity(intent);
        }

        else if (preferences.contains("isAdminLogin")){
            Intent intent = new Intent(Splash_screen.this, Admin_Home.class);
            startActivity(intent);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent intent = new Intent(Splash_screen.this, LoginOrSignUp.class);
                    startActivity(intent);


//                Intent intent=new Intent(Splash_screen.this, LoginOrSignUp.class);
//                startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN);
        }
    }
}