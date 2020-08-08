package com.apps.avichay.myeasyapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;


public class PrefsFragment extends Fragment {
    ListView listView;
    MyDBFavorites myDBFavorites;
    DataPassListener dataPassListener;
    AlertDialog.Builder alert;
    ListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prefs_fragment, container, false);
        myDBFavorites = new MyDBFavorites(getActivity());
        dataPassListener = (DataPassListener) getActivity();
        listView = view.findViewById(R.id.listViewFavorites);
        adapter = new ListAdapter(getActivity(), myDBFavorites.getData());
        alert = new AlertDialog.Builder(getActivity());
        listView.setAdapter(adapter);

        return view;
    }
}