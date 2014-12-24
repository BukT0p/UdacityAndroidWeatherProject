package com.dataart.vyakunin.coubplayer.datamodel;

import com.dataart.vyakunin.coubplayer.datamodel.models.CoubCategory;
import com.dataart.vyakunin.coubplayer.datamodel.models.CoubItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONHelper {

    public static ArrayList<CoubCategory> getCategoriesList(JSONArray resultArray) throws JSONException {
        if (resultArray == null)
            return null;
        int arraySize = resultArray.length();
        ArrayList<CoubCategory> categoriesList = new ArrayList<>(arraySize);
        for (int i = 0; i < arraySize; i++) {
            JSONObject catJSON = resultArray.getJSONObject(i);
            CoubCategory coubCategory = new CoubCategory();
            coubCategory.setTitle(catJSON.getString(JsonValues.CATEGORY_TITLE));
            coubCategory.setImageUrl(catJSON.getString(JsonValues.CATEGORY_IMAGE));
            coubCategory.setPermalink(catJSON.getString(JsonValues.CATEGORY_PERMALINK));
            categoriesList.add(coubCategory);
        }
        return categoriesList;
    }

    public static ArrayList<CoubItem> getCoubs(JSONObject coubs) throws JSONException {
        return null;
    }

    public static class JsonValues {
        //Category
        public static final String CATEGORY_TITLE = "title";
        public static final String CATEGORY_IMAGE = "image";
        public static final String CATEGORY_PERMALINK = "permalink";
        //Coub


    }
}
