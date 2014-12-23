package com.dataart.vyakunin.coubplayer.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dataart.vyakunin.coubplayer.R;
import com.dataart.vyakunin.coubplayer.datamodel.CoubStore;

public class CategoryAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private int titleColId;
    private int permalinkColId;
    private int imageColId;


    public CategoryAdapter(Context context, Cursor c) {
        super(context, c, false);
        inflater = LayoutInflater.from(context);
    }

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2014-12-23 19:12:06 by Android Layout Inspector
     */
    private static class ViewHolder {
        public final ImageView categoryImage;
        public final TextView categoryTitle;
        public final TextView categoryPermalink;

        private ViewHolder(ImageView categoryImage, TextView categoryTitle, TextView categoryPermalink) {
            this.categoryImage = categoryImage;
            this.categoryTitle = categoryTitle;
            this.categoryPermalink = categoryPermalink;
        }

        public static ViewHolder create(LinearLayout rootView) {
            ImageView categoryImage = (ImageView) rootView.findViewById(R.id.category_image);
            TextView categoryTitle = (TextView) rootView.findViewById(R.id.category_title);
            TextView categoryPermalink = (TextView) rootView.findViewById(R.id.category_permalink);
            return new ViewHolder(categoryImage, categoryTitle, categoryPermalink);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();
        vh.categoryTitle.setText(cursor.getString(titleColId));
        vh.categoryPermalink.setText(cursor.getString(permalinkColId));
        // TODO Load image using picasa
    }

    void populateColumns(Cursor cursor) {
        if (cursor == null)
            return;
        permalinkColId = cursor.getColumnIndex(CoubStore.CategoriesTable.PERMALINK);
        titleColId = cursor.getColumnIndex(CoubStore.CategoriesTable.TITLE);
        imageColId = cursor.getColumnIndex(CoubStore.CategoriesTable.IMAGE);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        populateColumns(cursor);
        super.changeCursor(cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.category_list_item, parent, false);
        view.setTag(ViewHolder.create((LinearLayout) view));
        return view;
    }

}
