package com.example.ownserver.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ownserver.R;
import com.example.ownserver.databinding.DummyFragmentBinding;
import com.example.ownserver.model.HomeViewModel;
import com.example.ownserver.model.UserModel;

import java.util.ArrayList;

public class DummyFragment extends Fragment {
    private Context context;
    private TextView id, name, auth;
    private HomeViewModel viewModel;
    private ArrayList<String> fragmentValue = new ArrayList<>();
    private DummyFragmentBinding binding;

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
        Log.d("CreateView", "dummy");
        binding = DummyFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        fragmentValue = viewModel.getViewModelList().getValue();

        setData(fragmentValue, true);

        return view;
    }

    public void setData(ArrayList<String> data, Boolean isCallInFragment){
        if(isCallInFragment.equals(true)){
            if(!(fragmentValue == null)){
                UserModel user = new UserModel(fragmentValue.get(0), fragmentValue.get(1), fragmentValue.get(2));
                binding.setUser(user);
            }else{
                Log.d("DUMMY VALUE", "NULL");
                Toast.makeText(context, "로딩된 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(!(data == null)){
                UserModel user = new UserModel(data.get(0), data.get(1), data.get(2));
                binding.setUser(user);
            }else{
                Toast.makeText(context, "로딩된 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
