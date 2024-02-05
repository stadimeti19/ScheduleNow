package com.example.schedulenow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulenow.databinding.FragmentAssignmentsBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssignmentFragment extends Fragment {
    private FragmentAssignmentsBinding binding;
    private ArrayList<Assignment> assignments;
    private AssignmentsAdapter adapter;

    // you need a way to store and retrieve data, even if the app is closed and reopened
    // to do this, you can store the data as a JSON string in SharedPreferences
    private static final String PREFS_NAME = "AssignmentPrefs";
    private static final String KEY_ASSIGNMENTS_LIST = "assignmentsList";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        binding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize assignments and adapter member variables
        assignments = new ArrayList<>();
        adapter = new AssignmentsAdapter(requireContext(), assignments, unused -> saveAssignmentPreferences());

        // Find the correct RecyclerView layout
        RecyclerView recyclerView = root.findViewById(R.id.rvAssignments);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadAssignmentsPreferences();

        adapter.notifyDataSetChanged();

        // Find the floating action button from the xml file
        com.google.android.material.floatingactionbutton.FloatingActionButton floatingButton =
                root.findViewById(R.id.floatingAddAssignments);

        floatingButton.setOnClickListener(view -> showDialogAddAssignment());

        return root;
    }

    // Populate dialog box for user to add assignments
    @SuppressLint("NotifyDataSetChanged")
    private void showDialogAddAssignment() {
        // Create AlertDialog to pop up
        AlertDialog.Builder assignmentBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_assignment_dialog, null);
        assignmentBuilder.setView(dialogView);
        AlertDialog dialog = assignmentBuilder.create();

        // Find the views from the dialog class layout
        EditText editAssignmentTitle = dialogView.findViewById(R.id.editAssignmentTitle);
        EditText editAssignmentDueDate = dialogView.findViewById(R.id.editAssignmentDueDate);
        EditText editAssignmentClass = dialogView.findViewById(R.id.editAssignmentClass);
        Button addAssignmentButton = dialogView.findViewById(R.id.addAssignmentButton);
        Button cancelAssignmentButton = dialogView.findViewById(R.id.cancelAssignmentButton);

        addAssignmentButton.setOnClickListener(view -> {
            String assignmentTitle = editAssignmentTitle.getText().toString();
            String assignmentDueDate = editAssignmentDueDate.getText().toString();
            String assignmentClass = editAssignmentClass.getText().toString();

            // check if the fields are not empty before creating new assignment
            if (!assignmentTitle.isEmpty() && !assignmentClass.isEmpty() && !assignmentDueDate.isEmpty()) {
                assignments.add(new Assignment(assignmentTitle, assignmentDueDate, assignmentClass));
                saveAssignmentPreferences();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // set up click listener for cancel button to dismiss dialog
        cancelAssignmentButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void loadAssignmentsPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String assignmentsJSON = preferences.getString(KEY_ASSIGNMENTS_LIST, null);

        if (assignmentsJSON != null) {
            Type listType = new TypeToken<List<Assignment>>() {}.getType();
            List<Assignment> loadedAssignments = new Gson().fromJson(assignmentsJSON, listType);

            assignments.clear();
            assignments.addAll(loadedAssignments);
        }
    }

    private void saveAssignmentPreferences(){
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String assignmentsJSON = new Gson().toJson(assignments);

        editor.putString(KEY_ASSIGNMENTS_LIST, assignmentsJSON);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
