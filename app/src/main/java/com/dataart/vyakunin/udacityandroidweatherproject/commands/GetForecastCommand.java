package com.dataart.vyakunin.udacityandroidweatherproject.commands;

import android.os.Bundle;
import android.os.Parcel;

import com.dataart.vyakunin.udacityandroidweatherproject.datamodel.models.WeatherForDay;
import com.dataart.vyakunin.udacityandroidweatherproject.service.RetrofittedCommand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vyakunin on 10/31/2014.
 */
public class GetForecastCommand extends RetrofittedCommand {
    //    http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&cnt=7&mode=json&units=metric
    public static final String UNITS_METRIC = "metric";
    public static final String UNITS_IMPERIAL = "imperial";
    private static final String WEATHER_FOR_DAYS = "WEATHER_FOR_DAYS";

    private String locationOrZip;
    private int days;
    private String units;

    public GetForecastCommand(String units, int days, String locationOrZip) {
        this.units = units;
        this.days = days;
        this.locationOrZip = locationOrZip;
    }

    public static final Creator<GetForecastCommand> CREATOR = new Creator<GetForecastCommand>() {
        @Override
        public GetForecastCommand createFromParcel(Parcel source) {
            return new GetForecastCommand(source.readString(), source.readInt(), source.readString());
        }

        @Override
        public GetForecastCommand[] newArray(int size) {
            return new GetForecastCommand[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.units);
        dest.writeInt(this.days);
        dest.writeString(this.locationOrZip);
    }

    @Override
    public void run() {
        JSONObject json = getApi().getWeatherForecast(units, days, locationOrZip);
        Bundle bundle = new Bundle();
        bundle.putString(WEATHER_FOR_DAYS, json.toString());
        sendOk(bundle);
    }

    public static ArrayList<WeatherForDay> getWeatherForDayList(Bundle resultData) {
        ArrayList<WeatherForDay> weatherForDayList = new ArrayList<>();
        String result = resultData.getString(WEATHER_FOR_DAYS);
        if (result == null) {
            throw new RuntimeException("No data in result!");
        }
        try {
            JSONObject json = new JSONObject(result);
            JSONArray forecastArray = json.getJSONArray("list");
            for (int i = 0; i < forecastArray.length(); i++) {
                WeatherForDay newDay = new WeatherForDay();
                newDay.setDt(forecastArray.getJSONObject(i).getLong("dt"));
                newDay.setMax(forecastArray.getJSONObject(i).getJSONObject("temp").getDouble("max"));
                newDay.setMin(forecastArray.getJSONObject(i).getJSONObject("temp").getDouble("min"));
                newDay.setMain(forecastArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                weatherForDayList.add(newDay);
            }
        } catch (JSONException ignore) {
            ignore.printStackTrace();
        }
        return weatherForDayList;
    }
}
