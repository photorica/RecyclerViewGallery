package jp.espla.gallery.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.espla.gallery.R;
import jp.espla.gallery.adapter.viewholder.GalleryViewHolder;
import jp.espla.gallery.listener.OnGalleryItemClickListener;
import jp.espla.gallery.model.ImageModel;
import jp.espla.gallery.util.DateUtils;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();

    /** 日付フォーマット */
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN);

    /** データ・セット */
    private final ArrayList<ImageModel> mDataSet = new ArrayList<>();

    /** コンテンツリゾルバー */
    private final ContentResolver mContentResolver;

    /** クリックリスナー */
    private final OnGalleryItemClickListener mListener;

    public GalleryAdapter(ContentResolver contentResolver, OnGalleryItemClickListener listener) {
        mContentResolver = contentResolver;
        mListener = listener;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder viewHolder, int position) {
        final ImageModel imageModel = mDataSet.get(position);

        // アイテム
        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(imageModel);
            }
        });

        // サムネイル
        Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, imageModel.id, MediaStore.Images.Thumbnails.MINI_KIND, null);
        viewHolder.mThumbnailView.setImageBitmap(thumbnail);

        // 表示名
        viewHolder.mNameView.setText(imageModel.name);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * カーソルからデータを入れ替える
     */
    public void swapCursorData(Cursor cursor) {
        mDataSet.clear();

        cursor.moveToFirst();
        do {
            ImageModel imageModel = new ImageModel();
            imageModel.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            imageModel.name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            imageModel.description = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            imageModel.mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            imageModel.width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            imageModel.height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            imageModel.size = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            imageModel.latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
            imageModel.longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
            imageModel.orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
            imageModel.path = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageModel.id).toString();
            imageModel.dateAdded = DateUtils.convertSecIntoDateString(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)), DATE_FORMAT);
            imageModel.dateModified = DateUtils.convertSecIntoDateString(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)), DATE_FORMAT);

//            Log.d(TAG, imageModel.toString());
            mDataSet.add(imageModel);
        } while (cursor.moveToNext());

        notifyDataSetChanged();
    }

}
