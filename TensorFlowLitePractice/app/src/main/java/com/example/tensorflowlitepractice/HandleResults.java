package com.example.tensorflowlitepractice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class HandleResults extends Activity {

    public RelativeLayout recyclableLayout;
    public RelativeLayout compostableLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_layout);

        recyclableLayout = findViewById(R.id.recycablesLayout);
        compostableLayout = findViewById(R.id.compostablesLayout);

        populateRecyclables();
        populateCompostables();
    }



    public void populateRecyclables(){
        try{
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,20,20,20);
            int previousID = -1;
            if(TemporaryUtility.currentRecyclables.size()==0) {
                params.addRule(RelativeLayout.BELOW, R.id.recycablesText);
                TextView text = new TextView(this);
                text.setText("No Recyclable Objects Present");
                text.setLayoutParams(params);
                recyclableLayout.addView(text);
            }

            for(String recyclable : TemporaryUtility.currentRecyclables){
                if(previousID==-1){
                    params.addRule(RelativeLayout.BELOW, R.id.recycablesText);
                }
                else{
                    params.addRule(RelativeLayout.BELOW, previousID);
                }

                int viewID = View.generateViewId();

                Button btn = new Button(this);
                btn.setText(recyclable);
                btn.setId(viewID);
                btn.setLayoutParams(params);
                btn.setOnClickListener(this::onClickResult);
                recyclableLayout.addView(btn);

                previousID = viewID;
            }
        }
        catch(Exception e){
            Log.e("ERROR", e.toString());
        }

    }

    public void populateCompostables(){
        try{
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,20,20,20);
            int previousID = -1;
            if(TemporaryUtility.currentCompostables.size()==0) {
                params.addRule(RelativeLayout.BELOW, R.id.compostablesText);
                TextView text = new TextView(this);
                text.setText("No Compostable Objects Present");
                text.setLayoutParams(params);
                compostableLayout.addView(text);
            }

            for(String compostable : TemporaryUtility.currentCompostables){
                if(previousID==-1){
                    params.addRule(RelativeLayout.BELOW, R.id.recycablesText);
                }
                else{
                    params.addRule(RelativeLayout.BELOW, previousID);
                }

                int viewID = View.generateViewId();

                Button btn = new Button(this);
                btn.setText(compostable);
                btn.setId(viewID);
                btn.setLayoutParams(params);
                btn.setOnClickListener(this::onClickResult);
                compostableLayout.addView(btn);

                previousID = viewID;
            }
        }
        catch(Exception e){
            Log.e("ERROR", e.toString());
        }
    }

    public void onClickResult(View view){

    }

    public void returnHome(View view) {
        finish();
    }
}
