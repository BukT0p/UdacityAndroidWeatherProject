package com.dataart.vyakunin.coubplayer.commands;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.dataart.vyakunin.coubplayer.datamodel.DBHelper;
import com.dataart.vyakunin.coubplayer.datamodel.JSONHelper;
import com.dataart.vyakunin.coubplayer.datamodel.models.CoubCategory;
import com.dataart.vyakunin.coubplayer.service.RetrofittedCommand;

import org.json.JSONException;

import java.util.ArrayList;

public class GetCategoriesCommand extends RetrofittedCommand {

    public static final Creator<GetCategoriesCommand> CREATOR = new Creator<GetCategoriesCommand>() {
        @Override
        public GetCategoriesCommand createFromParcel(Parcel source) {
            return new GetCategoriesCommand();
        }

        @Override
        public GetCategoriesCommand[] newArray(int size) {
            return new GetCategoriesCommand[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public void run() {
        try {
            ArrayList<CoubCategory> categories = JSONHelper.getCategoriesList(getApi().getCategories());
            DBHelper.insertCategories(context, categories);
        } catch (JSONException e) {
            Log.e(TAG, "Error getting categories", e);
        }
        sendOk(Bundle.EMPTY);
    }
}
