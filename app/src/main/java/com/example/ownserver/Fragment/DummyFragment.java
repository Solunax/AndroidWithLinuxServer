package com.example.ownserver.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ownserver.R;
import com.example.ownserver.model.HomeViewModel;

import java.util.ArrayList;

public class DummyFragment extends Fragment {
    private Context context;
    private TextView id, name, auth;
    private HomeViewModel viewModel;
    private ArrayList<String> fragmentValue = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context cont) {
        super.onAttach(cont);
        context = getContext();
        Log.d("ATTACH", "DUMMY");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        Log.d("DETACH", "DUMMY");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        id = (TextView)view.findViewById(R.id.fragment_id);
        name = (TextView)view.findViewById(R.id.fragment_name);
        auth = (TextView)view.findViewById(R.id.fragment_auth);

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        fragmentValue = viewModel.getViewModelList().getValue();

        if(!(fragmentValue == null)){
            id.setText(fragmentValue.get(0));
            name.setText(fragmentValue.get(1));
            auth.setText(fragmentValue.get(2));
        }
        return view;
    }
}
