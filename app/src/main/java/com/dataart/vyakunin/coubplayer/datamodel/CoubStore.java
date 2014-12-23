package com.dataart.vyakunin.coubplayer.datamodel;


import android.provider.BaseColumns;

import com.annotatedsql.annotation.provider.Provider;
import com.annotatedsql.annotation.provider.URI;
import com.annotatedsql.annotation.sql.Autoincrement;
import com.annotatedsql.annotation.sql.Column;
import com.annotatedsql.annotation.sql.NotNull;
import com.annotatedsql.annotation.sql.PrimaryKey;
import com.annotatedsql.annotation.sql.Schema;
import com.annotatedsql.annotation.sql.Table;
import com.annotatedsql.annotation.sql.Unique;

@Schema(className = "CoubSchema", dbName = "coubs.db", dbVersion = 1)
@Provider(authority = "com.dataart.vyakunin.coubplayer.datamodel", name = "CoubsContentProvider", schemaClass = "CoubSchema")
public interface CoubStore {
    @Table(CategoriesTable.TABLE_NAME)
    public static interface CategoriesTable {
        String TABLE_NAME = "Categories";
        @URI
        String CONTENT_URI = "category";

        @PrimaryKey
        @Autoincrement
        @Column(type = Column.Type.INTEGER)
        String ID = BaseColumns._ID;

        @NotNull
        @Unique
        @Column(type = Column.Type.TEXT)
        String TITLE = "title";

        @Column(type = Column.Type.TEXT)
        String IMAGE = "image";

        @Column(type = Column.Type.TEXT)
        String PERMALINK = "permalink";
    }
}
