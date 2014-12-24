package com.dataart.vyakunin.coubplayer.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dataart.vyakunin.coubplayer.R;
import com.dataart.vyakunin.coubplayer.datamodel.CoubStore;

public class CategoryAdapter extends AbsCursorAdapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private int titleColId;
    private int permalinkId;

    public CategoryAdapter(Context context, Cursor cursor) {
        super(cursor);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor != null)
            populateCursorColumns(newCursor);
        return super.swapCursor(newCursor);
    }

    private void populateCursorColumns(Cursor cursor) {
        titleColId = cursor.getColumnIndex(CoubStore.CategoriesTable.TITLE);
        permalinkId = cursor.getColumnIndex(CoubStore.CategoriesTable.PERMALINK);
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
        viewHolder.titleTextView.setText(cursor.getString(titleColId));
        viewHolder.titleTextView.append("\n"+cursor.getString(permalinkId));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(inflater.inflate(R.layout.category_list_item, viewGroup, false));
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.category_title);
        }
    }
}
