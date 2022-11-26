package com.example.pro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pro.bubblenavigation.BubbleNavigationLinearView;
import com.example.pro.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.example.pro.fragmentadmin.AdminMapView;
import com.example.pro.fragmentadmin.Bus_view;
import com.example.pro.fragmentadmin.DriverFragment;
import com.example.pro.fragmentadmin.FeedbackFragment;
import com.example.pro.fragmentadmin.StudentFragment;

public class Admin_Home extends AppCompatActivity {
    Fragment selectedFragment = null;
    SharedPreferences preferences;
    String myString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
      //  String myStrValue = preferences.getString("username", "defaultStringIfNothingFound");
        // username = getIntent().getStringExtra("userName");
       // myString=myStrValue;
        BubbleNavigationLinearView bubbleNavigation = findViewById(R.id.bubbleNavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new StudentFragment()).addToBackStack(null).commit();

        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 0:
                        selectedFragment = new StudentFragment();
                        break;
                    case 1:
                        selectedFragment = new Bus_view();
                        break;
                    case 2:
                        selectedFragment = new AdminMapView();
                        break;
                    case 3:
                        selectedFragment = new FeedbackFragment();
                        break;
                    case 4:
                        selectedFragment = new DriverFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).addToBackStack(null).commit();
            }
        });

    }
    public void onBackPressed() {

        selectImageOption();
    }


    private void selectImageOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Home.this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("isAdminLogin");
                editor.commit();

                finish();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        }).setNegativeButton("No",null);
        builder.show();
    }
}