package com.dataart.vyakunin.coubplayer.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.dataart.vyakunin.coubplayer.R;
import com.dataart.vyakunin.coubplayer.commands.GetCategoriesCommand;
import com.dataart.vyakunin.coubplayer.datamodel.CoubStore;
import com.dataart.vyakunin.coubplayer.datamodel.CoubsContentProvider;
import com.dataart.vyakunin.coubplayer.service.Command;
import com.dataart.vyakunin.coubplayer.service.messaging.ResultReceiverManager;
import com.dataart.vyakunin.coubplayer.ui.adapters.CategoryAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    public static Fragment newInstance() {
        return HomeFragment_.builder().build();
    }

    private CategoryAdapter adapter;

    @ViewById(R.id.list_view)
    ListView listView;

    @AfterViews
    void init() {
        adapter = new CategoryAdapter(getActivity(), null);
        new GetCategoriesCommand().start(getActivity());
        showProgressOverlay(true);
    }

    private ResultReceiverManager.ResultListener forecastResultListener = new ResultReceiverManager.ResultListener(Command.getCode(GetCategoriesCommand.class)) {
        @Override
        protected void onSuccess(Bundle resultData) {
            showProgressOverlay(false);
            getLoaderManager().restartLoader(10, null, contactsLoader);
        }

        @Override
        protected void onError(Bundle resultData) {
            showProgressOverlay(false);
            Toast.makeText(getActivity(), "Sync error", Toast.LENGTH_LONG).show();
        }
    };


    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(getActivity(), CoubsContentProvider.contentUri(CoubStore.CategoriesTable.CONTENT_URI), null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            adapter.changeCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            adapter.changeCursor(null);
        }
    };


    @Override
    protected List<ResultReceiverManager.ResultListener> getResultListeners() {
        return Arrays.asList(forecastResultListener);
    }
}
