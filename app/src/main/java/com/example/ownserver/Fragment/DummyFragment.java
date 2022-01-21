package com.example.ownserver.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ownserver.R;

public class DummyFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context cont) {
        super.onAttach(cont);
        context = getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);
        return view;
    }
}
