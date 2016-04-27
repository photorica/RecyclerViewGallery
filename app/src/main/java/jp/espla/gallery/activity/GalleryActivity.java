package jp.espla.gallery.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

import jp.espla.gallery.R;
import jp.espla.gallery.adapter.GalleryAdapter;
import jp.espla.gallery.listener.OnGalleryItemClickListener;
import jp.espla.gallery.model.ImageModel;
import jp.espla.gallery.util.DateUtils;

public class GalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = GalleryActivity.class.getSimpleName();

    /** 画像日付フォーマット */
    private final static SimpleDateFormat IMAGE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.JAPAN);

    /** グリッドのカラム数 */
    private static final int GRID_COLUMN_NUM = 3;

    /** カメラアプリ起動コード */
    private static final int REQUEST_CODE_CAMERA = 0;

    /** リストView */
    private RecyclerView mRecyclerView;

    /** アダプター */
    private GalleryAdapter mGalleryAdapter;

    /** 画像URI */
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_gallery);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        getSupportLoaderManager().initLoader(0, null, this);
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
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        releaseView();

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_ADDED);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished");
        mGalleryAdapter.swapCursorData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode != RESULT_OK) {
                // カメラ撮影に失敗した場合はMediaStoreから削除する
                if (mImageUri != null) {
                    getContentResolver().delete(mImageUri, null, null);
                    mImageUri = null;
                }
                return;
            }

            getSupportLoaderManager().initLoader(0, null, this);
            mImageUri = null;
        }
    }

    /**
     * Viewの初期化
     */
    private void initView() {
        // ヘッダー
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // カメラボタン
        View addButton = findViewById(R.id.activity_gallery_camera_button);
        if (addButton != null) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchCameraApp();
                }
            });
        }

        // リスト
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_gallery_recycler_view);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), GRID_COLUMN_NUM);
            mRecyclerView.setLayoutManager(layoutManager);
            mGalleryAdapter = new GalleryAdapter(getContentResolver(), new OnGalleryItemClickListener() {
                @Override
                public void onItemClick(ImageModel imageModel) {
                    moveToDetailView(imageModel);
                }
            });
            mRecyclerView.setAdapter(mGalleryAdapter);
        }
    }

    /**
     * Viewを解放する
     */
    private void releaseView() {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(null);
            mRecyclerView = null;
        }
        mGalleryAdapter = null;
    }

    /**
     * カメラアプリを起動する
     */
    private void launchCameraApp() {
        // 画像の事前準備
        String dateString = DateUtils.convertMillsIntoDateString(System.currentTimeMillis(), IMAGE_DATE_FORMAT);
        String fileName = String.format("IMG_%s.jpg", dateString);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 詳細画面へ遷移する
     * @param imageModel 画像データ
     */
    private void moveToDetailView(ImageModel imageModel) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageActivity.INTENT_KEY_IMAGE_MODEL, imageModel);
        startActivity(intent);
    }
}
