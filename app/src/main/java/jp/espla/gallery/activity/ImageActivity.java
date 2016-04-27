package jp.espla.gallery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

import jp.espla.gallery.R;
import jp.espla.gallery.model.ImageModel;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = GalleryActivity.class.getSimpleName();

    public static final String INTENT_KEY_IMAGE_MODEL = "image_model";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        releaseView();
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
    }

    /**
     * Viewの初期化
     */
    private void initView() {
        // ヘッダー
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ImageModel imageModel = (ImageModel) intent.getSerializableExtra(INTENT_KEY_IMAGE_MODEL);
        if (imageModel != null) {
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageModel.path));
            } catch (IOException e) {
                Log.w(TAG, String.format("Failed to load thumbnail: %s", e.getMessage()));
            }
            ImageView imageView = (ImageView) findViewById(R.id.activity_image_view);
            if (imageView != null) {
                imageView.setImageBitmap(image);
            }
        }
    }

    /**
     * Viewを解放する
     */
    private void releaseView() {
    }

}