package com.dataart.vyakunin.coubplayer.datamodel;

import android.content.ContentValues;
import android.content.Context;

import com.dataart.vyakunin.coubplayer.datamodel.models.CoubCategory;
import com.dataart.vyakunin.coubplayer.datamodel.models.CoubItem;

import java.util.ArrayList;

public class DBHelper {
    //CRUD methods
    public static void insertCategories(Context context, ArrayList<CoubCategory> categories) {
        ContentValues[] cvs = new ContentValues[categories.size()];
        int i = 0;
        for (CoubCategory category : categories) {
            ContentValues cv = new ContentValues();
            cv.put(CoubStore.CategoriesTable.TITLE, category.getTitle());
            cv.put(CoubStore.CategoriesTable.IMAGE, category.getImageUrl());
            cv.put(CoubStore.CategoriesTable.PERMALINK, category.getPermalink());
            cvs[i++] = cv;
        }
        context.getContentResolver().bulkInsert(CoubsContentProvider.contentUriBulkInsert(CoubStore.CategoriesTable.CONTENT_URI, CoubsContentProvider.BulkInsertConflictMode.INSERT), cvs);
    }

    public static void insertCoubs(Context context, ArrayList<CoubItem> coubs) {

    }
}
