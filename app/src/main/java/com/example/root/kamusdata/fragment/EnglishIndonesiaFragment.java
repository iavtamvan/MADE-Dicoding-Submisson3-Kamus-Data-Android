package com.example.root.kamusdata.fragment;


import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.kamusdata.R;
import com.example.root.kamusdata.adapter.KamusAdapter;
import com.example.root.kamusdata.helper.database.KamusHelper;
import com.example.root.kamusdata.model.KamusDataModel;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnglishIndonesiaFragment extends Fragment {


    private MaterialSearchBar search;
    private RecyclerView rv;

    private KamusHelper kamusHelper;
    private KamusAdapter kamusAdapter;

    private ArrayList<KamusDataModel> kamusDataModels;

    private boolean English = true;

    public EnglishIndonesiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        initView(view);
        kamusDataModels = new ArrayList<>();
        kamusHelper = new KamusHelper(getActivity());
        kamusAdapter = new KamusAdapter(kamusDataModels, getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(kamusAdapter);

        search.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                kamusAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loadData("");

        return view;
    }


    private void loadData(String search) {
        try {
            kamusHelper.open();
            kamusDataModels = kamusHelper.getDataALl(English);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamusHelper.close();
        }
        kamusAdapter.replaceAll(kamusDataModels);
    }

    private void initView(View view) {
        search = view.findViewById(R.id.search);
        rv =  view.findViewById(R.id.rv);
    }
}
