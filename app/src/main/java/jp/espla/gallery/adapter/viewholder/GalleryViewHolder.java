package jp.espla.gallery.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jp.espla.gallery.R;

/**
 * @author Yuta Kaga
 */
public class GalleryViewHolder extends RecyclerView.ViewHolder {

    public final View mItemView;
    public final ImageView mThumbnailView;
    public final TextView mNameView;

    public GalleryViewHolder(View itemView) {
        super(itemView);

        mItemView = itemView;
        mThumbnailView = (ImageView) itemView.findViewById(R.id.item_thumbnail_view);
        mNameView = (TextView) itemView.findViewById(R.id.item_name_view);
    }
}
