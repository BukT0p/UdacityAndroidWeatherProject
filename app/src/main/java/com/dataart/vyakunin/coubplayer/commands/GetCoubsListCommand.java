package com.dataart.vyakunin.coubplayer.commands;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.dataart.vyakunin.coubplayer.datamodel.DBHelper;
import com.dataart.vyakunin.coubplayer.datamodel.JSONHelper;
import com.dataart.vyakunin.coubplayer.datamodel.models.CoubCategory;
import com.dataart.vyakunin.coubplayer.datamodel.models.CoubItem;
import com.dataart.vyakunin.coubplayer.service.RetrofittedCommand;

import org.json.JSONException;

import java.util.ArrayList;


public class GetCoubsListCommand extends RetrofittedCommand {
    private final String permalink;
    private final int page;
    private final int perPage;

    public GetCoubsListCommand(String permalink, int page, int perPage) {
        this.permalink = permalink;
        this.page = page;
        this.perPage = perPage;
    }

    public static final Creator<GetCoubsListCommand> CREATOR = new Creator<GetCoubsListCommand>() {
        @Override
        public GetCoubsListCommand createFromParcel(Parcel source) {
            return new GetCoubsListCommand(source.readString(), source.readInt(), source.readInt());
        }

        @Override
        public GetCoubsListCommand[] newArray(int size) {
            return new GetCoubsListCommand[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(permalink);
        dest.writeInt(page);
        dest.writeInt(perPage);
    }

    @Override
    public void run() {
        try {
            ArrayList<CoubItem> coubs = JSONHelper.getCoubs(getApi().getCoubs(permalink, page, perPage));
            DBHelper.insertCoubs(context, coubs);
        } catch (JSONException e) {
            Log.e(TAG, "Error getting coubs for " + permalink, e);
        }
        sendOk(Bundle.EMPTY);
    }
}
