package com.example.tensorflowlitepractice;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String modelFile = "template_model.tflite";
    private ObjectDetector.ObjectDetectorOptions options;

    private Button cameraButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        // Initialization

        options = ObjectDetector.ObjectDetectorOptions.builder()
                .setBaseOptions(BaseOptions.builder().build())
                .setMaxResults(6)
                .build();
        cameraButton = findViewById(R.id.cameraButton);
        imageView = findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    public Bitmap convertToBitmap(){
        ImageView imgView = findViewById(R.id.imageView);
        imgView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable)imgView.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getBitmap());
        return bitmap;
    }

    public TensorImage convertToTensorImage(){
        TensorImage tensorImage = TensorImage.fromBitmap(convertToBitmap());
        return tensorImage;
    }


    public void showResults(View view) {
        Intent intent = new Intent(view.getContext(), HandleResults.class);
        startActivity(intent);
    }

    public void filterResults(List<Detection> results){
        int recycablesNumber = 0;
        int compostablesNumber = 0;

        for(int i=0;i<results.size();i++){
            Detection currentDetection = results.get(i);
            String category = currentDetection.getCategories().get(0).getLabel().toUpperCase();
            if(recycablesNumber>=TemporaryUtility.compostableCount && compostablesNumber >= TemporaryUtility.recycableCount){
                return;
            }
            if(TemporaryUtility.recycables.contains(category)){
                if(recycablesNumber<TemporaryUtility.recycableCount){
                    TemporaryUtility.currentRecyclables.add(category);
                    recycablesNumber++;
                    Log.w("CATEGORY", category);
                }
            }
            if(TemporaryUtility.compostables.contains(category)){
                if(compostablesNumber<TemporaryUtility.compostableCount){
                    TemporaryUtility.currentCompostables.add(category);
                    compostablesNumber++;
                    Log.w("CATEGORY", category);
                }
            }
            Log.i("CATEGORY", category);

        }
    }

    public void captureEnvironment(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
        detectObjects();
    }

    public void detectObjects(){
        try {
            ObjectDetector objectDetector = ObjectDetector.createFromFileAndOptions(this, modelFile, options);
            List<Detection> results = objectDetector.detect(convertToTensorImage());
            filterResults(results);
        }
        catch(Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_LONG).show();
            Log.e("ERROR", e.toString());
        }
    }
}