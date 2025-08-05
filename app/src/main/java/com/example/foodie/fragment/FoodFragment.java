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
//public class FoodFragment extends Fragment {
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_menu, container, false);
//
//        // Fetch food items from SQLite database
//        MyDatabase db = new MyDatabase(requireContext()); // use requireContext for safety
//        List<MenuItem> foodMenu = db.getItemsByCategory("food");
//
//        // Setup RecyclerView
//        RecyclerView recyclerView = view.findViewById(R.id.recycler_menu);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recyclerView.setAdapter(new MenuAdapter(foodMenu, requireContext()));
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

public class FoodFragment extends Fragment {

    private int tableNumber; // 👈 Table number passed from MenuActivity

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Step 1: Get table number from arguments
        if (getArguments() != null) {
            tableNumber = getArguments().getInt("table_number", -1);
        }

        // Step 2: Fetch menu items (food) from DB
        MyDatabase db = new MyDatabase(requireContext());
        List<MenuItem> foodMenu = db.getItemsByCategory("food");

        // OPTIONAL: If you want to hide already-ordered items, you can add filtering here
        // foodMenu = db.getAvailableMenuItems("food", tableNumber); // Implement this if needed

        // Step 3: Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new MenuAdapter(foodMenu, requireContext(), tableNumber)); // 👈 pass tableNumber

        return view;
    }
}
