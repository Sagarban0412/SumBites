//package com.example.foodie.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.foodie.MenuAdapter;
//import com.example.foodie.MenuItem;
//import com.example.foodie.R;
//import com.example.foodie.database.MyDatabase;
//
//import java.util.List;
//
//public class SmokeFragment extends Fragment {
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_menu, container, false);
//
//        MyDatabase db = new MyDatabase(requireContext());
//        List<MenuItem> drinkMenu = db.getItemsByCategory("smoke");
//
//        RecyclerView recyclerView = view.findViewById(R.id.recycler_menu);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recyclerView.setAdapter(new MenuAdapter(drinkMenu, requireContext()));
//
//        return view;
//    }
//}
package com.example.foodie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.MenuAdapter;
import com.example.foodie.MenuItem;
import com.example.foodie.R;
import com.example.foodie.database.MyDatabase;

import java.util.List;

public class SmokeFragment extends Fragment {

    private int tableNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        if (getArguments() != null) {
            tableNumber = getArguments().getInt("table_number", -1);
        }

        MyDatabase db = new MyDatabase(requireContext());
        List<MenuItem> smokeMenu = db.getItemsByCategory("smoke");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new MenuAdapter(smokeMenu, requireContext(), tableNumber));

        return view;
    }
}
