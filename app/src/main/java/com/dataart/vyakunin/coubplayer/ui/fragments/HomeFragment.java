package com.dataart.vyakunin.coubplayer.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;

import com.dataart.vyakunin.coubplayer.R;
import com.dataart.vyakunin.coubplayer.commands.GetCategoriesCommand;
import com.dataart.vyakunin.coubplayer.datamodel.CoubStore;
import com.dataart.vyakunin.coubplayer.datamodel.CoubsContentProvider;
import com.dataart.vyakunin.coubplayer.service.Command;
import com.dataart.vyakunin.coubplayer.service.messaging.ResultReceiverManager;
import com.dataart.vyakunin.coubplayer.ui.adapters.CategoryAdapter;
import com.dataart.vyakunin.coubplayer.ui.adapters.DividerItemDecoration;

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

    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    @AfterViews
    void init() {
        adapter = new CategoryAdapter(getActivity(), null);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetCategoriesCommand().start(getActivity());
        showProgressOverlay(true);
        getLoaderManager().restartLoader(10, null, contactsLoader);
    }

    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(getActivity(), CoubsContentProvider.contentUri(CoubStore.CategoriesTable.CONTENT_URI), null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            adapter.changeCursor(cursor);
            if (cursor.getCount() > 0)
                showProgressOverlay(false);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            adapter.changeCursor(null);
        }
    };

    private ResultReceiverManager.ResultListener categoriesListener = new ResultReceiverManager.ResultListener(Command.getCode(GetCategoriesCommand.class)) {
        @Override
        protected void onSuccess(Bundle resultData) {
            getLoaderManager().restartLoader(10, null, contactsLoader);
        }

        @Override
        protected void onError(Bundle resultData) {
            showProgressOverlay(false);
            Toast.makeText(getActivity(), "Sync error", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected List<ResultReceiverManager.ResultListener> getResultListeners() {
        return Arrays.asList(categoriesListener);
    }
}
