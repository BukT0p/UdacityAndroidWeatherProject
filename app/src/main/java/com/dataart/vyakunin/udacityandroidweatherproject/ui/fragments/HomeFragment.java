package com.dataart.vyakunin.udacityandroidweatherproject.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dataart.vyakunin.udacityandroidweatherproject.R;
import com.dataart.vyakunin.udacityandroidweatherproject.commands.GetForecastCommand;
import com.dataart.vyakunin.udacityandroidweatherproject.datamodel.models.WeatherForDay;
import com.dataart.vyakunin.udacityandroidweatherproject.service.Command;
import com.dataart.vyakunin.udacityandroidweatherproject.service.messaging.ResultReceiverManager.ResultListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    public static Fragment newInstance() {
        return HomeFragment_.builder().build();
    }

    @ViewById(R.id.list_view)
    ListView listView;

    @AfterViews
    void init() {
        new GetForecastCommand(GetForecastCommand.UNITS_METRIC, 7, "94043").start(getActivity());
        showProgressOverlay(true);
    }

    private ResultListener forecastResultListener = new ResultListener(Command.getCode(GetForecastCommand.class)) {
        @Override
        protected void onSuccess(Bundle resultData) {
            showProgressOverlay(false);
            showForecastFor(GetForecastCommand.getWeatherForDayList(resultData));
        }

        @Override
        protected void onError(Bundle resultData) {
            showProgressOverlay(false);
            Toast.makeText(getActivity(), "Sync error", Toast.LENGTH_LONG).show();
        }
    };

    private void showForecastFor(ArrayList<WeatherForDay> weatherForDayList) {
        ArrayAdapter<WeatherForDay> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, weatherForDayList);
        listView.setAdapter(adapter);
    }

    @Override
    protected List<ResultListener> getResultListeners() {
        return Arrays.asList(forecastResultListener);
    }
}
