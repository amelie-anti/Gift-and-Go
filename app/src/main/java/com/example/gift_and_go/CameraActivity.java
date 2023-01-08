package com.example.gift_and_go;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();

// Ouvrez la caméra arrière par défaut
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Définissez la prévisualisation de la caméra sur la vue de surface
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }
        });
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int displayRotation = display.getRotation();
        switch (displayRotation) {
            case Surface.ROTATION_0:
                camera.setDisplayOrientation(90);
                break;
            case Surface.ROTATION_90:
                camera.setDisplayOrientation(0);
                break;
            case Surface.ROTATION_180:
                camera.setDisplayOrientation(270);
                break;
            case Surface.ROTATION_270:
                camera.setDisplayOrientation(180);
                break;
        }


        ImageView imageView = findViewById(R.id.image_view);

        Point size = new Point();
        display.getSize(size);
        int duration = 300;
        int distanceX = 10;
        int distanceY = 10;
        // déplacer l'image vers la droite
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "translationX", distanceX);
        animator1.setDuration(duration);

        // déplacer l'image vers le bas
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "translationY", distanceY);
        animator2.setDuration(duration);

        // déplacer l'image vers la gauche
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, "translationX", -distanceX);
        animator3.setDuration(duration);

        // déplacer l'image vers le haut
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(imageView, "translationY", -distanceY);
        animator4.setDuration(duration);

        // créer une animation en boucle qui répète l'animation de manière infinie
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator1, animator2, animator3, animator4);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });

        animatorSet.start();


        String base64Image = getIntent().getStringExtra("image"); // chaîne de caractères contenant l'image en Base64

        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


        Glide.with(this)
                .load(bitmap)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}