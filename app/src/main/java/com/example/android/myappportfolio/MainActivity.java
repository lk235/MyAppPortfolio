package com.example.android.myappportfolio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapp_main);
    }

    /**
     * This method show Toast message when then button is clicked..
     */
    public void launchApp(View view){
        if(view.getId() == R.id.hot_movie_button){
            Intent intnet = new Intent(this , com.example.android.myappportfolio.topMovies.MainActivity.class);
            startActivity(intnet);
        }
        Toast.makeText(this , "这个按钮将会启动应用\"" + ((Button)view).getText() +"\"", Toast.LENGTH_SHORT).show();;

        }
    }

