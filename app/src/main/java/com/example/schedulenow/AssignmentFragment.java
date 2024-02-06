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

import com.example.schedulenow.databinding.FragmentAssignmentBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AssignmentFragment extends Fragment {
    private FragmentAssignmentBinding binding;
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
        binding = FragmentAssignmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize assignments and adapter member variables
        assignments = new ArrayList<>();
        adapter = new AssignmentsAdapter(requireContext(), assignments, unused -> saveAssignmentPreferences(), this::showEditAssignmentDialog);
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

        Button sortButton = root.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> showSortOptionsDialog());

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
        EditText editAssignmentProgress = dialogView.findViewById(R.id.editAssignmentProgress); // New EditText for progress
        Button addAssignmentButton = dialogView.findViewById(R.id.addAssignmentButton);
        Button cancelAssignmentButton = dialogView.findViewById(R.id.cancelAssignmentButton);

        addAssignmentButton.setOnClickListener(view -> {
            String assignmentTitle = editAssignmentTitle.getText().toString();
            String assignmentDueDate = editAssignmentDueDate.getText().toString();
            String assignmentClass = editAssignmentClass.getText().toString();
            int assignmentProgress = Integer.parseInt(editAssignmentProgress.getText().toString()); // Parse progress from EditText

            // check if the fields are not empty before creating new assignment
            if (!assignmentTitle.isEmpty() && !assignmentClass.isEmpty() && !assignmentDueDate.isEmpty()) {
                assignments.add(new Assignment(assignmentTitle, assignmentDueDate, assignmentClass, assignmentProgress));
                saveAssignmentPreferences();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // set up click listener for cancel button to dismiss dialog
        cancelAssignmentButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showEditAssignmentDialog(Assignment assignmentToEdit) {
        // Create AlertDialog to pop up
        AlertDialog.Builder assignmentBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.edit_assignment_dialog, null);
        assignmentBuilder.setView(dialogView);
        AlertDialog dialog = assignmentBuilder.create();

        // Find the views from the dialog class layout
        EditText editAssignmentTitle = dialogView.findViewById(R.id.editAssignmentTitle);
        EditText editAssignmentDueDate = dialogView.findViewById(R.id.editAssignmentDueDate);
        EditText editAssignmentClass = dialogView.findViewById(R.id.editAssignmentClass);
        EditText editAssignmentProgress = dialogView.findViewById(R.id.editAssignmentProgress); // New EditText for progress
        Button saveAssignmentButton = dialogView.findViewById(R.id.saveAssignmentButton);
        Button cancelAssignmentButton = dialogView.findViewById(R.id.cancelAssignmentButton);

        // Set the initial values
        editAssignmentTitle.setText(assignmentToEdit.getTitle());
        editAssignmentDueDate.setText(assignmentToEdit.getDueDate());
        editAssignmentClass.setText(assignmentToEdit.getAClass());
        editAssignmentProgress.setText(String.valueOf(assignmentToEdit.getAssignmentProgress())); // Set progress value

        saveAssignmentButton.setOnClickListener(view -> {
            // Retrieve edited values
            String updatedTitle = editAssignmentTitle.getText().toString();
            String updatedDueDate = editAssignmentDueDate.getText().toString();
            String updatedClass = editAssignmentClass.getText().toString();
            int updatedProgress = Integer.parseInt(editAssignmentProgress.getText().toString()); // Parse progress from EditText


            // Update the assignment in the list
            assignmentToEdit.setTitle(updatedTitle);
            assignmentToEdit.setDueDate(updatedDueDate);
            assignmentToEdit.setAClass(updatedClass);
            assignmentToEdit.setAssignmentProgress(updatedProgress); // Set updated progress

            // Save the updated assignments list
            saveAssignmentPreferences();

            // Notify the adapter of the change
            adapter.notifyDataSetChanged();

            // Dismiss the dialog
            dialog.dismiss();
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

    private void showSortOptionsDialog() {
        // Logic to show a dialog or dropdown with sorting options
        // Using AlertDialog for a list of sorting options
        AlertDialog.Builder sortBuilder = new AlertDialog.Builder(requireContext());
        sortBuilder.setTitle("Sort Options");

        // Add sorting options to the list
        String[] options = {"Sort by Due Date", "Sort by Course (Alphabetical)"};
        sortBuilder.setItems(options, (dialog, selection) -> {
            // Handle the selected sorting option
            switch (selection) {
                case 0:
                    sortAssignmentsByDueDate();
                    break;
                case 1:
                    sortAssignmentsByClass();
                    break;
            }
        });
        sortBuilder.create().show();
    }

    private void sortAssignmentsByClass() {
        selectionSorter(assignments, Comparator.comparing(Assignment::getAClass));
        adapter.notifyDataSetChanged();
    }

    private void sortAssignmentsByDueDate() {
        selectionSorter(assignments, Comparator.comparing(Assignment::getDueDate));
        adapter.notifyDataSetChanged();
    }

    private void selectionSorter(ArrayList<Assignment> assignments, Comparator<Assignment> comparator) {
        for (int i = 0; i < assignments.size() - 1; ++i) {
            int index = i;
            for (int j = i + 1; j < assignments.size(); ++j) {
                if (comparator.compare(assignments.get(j), assignments.get(index)) < 0) {
                    index = j;
                }
            }
            Assignment temp = assignments.get(i);
            assignments.set(i, assignments.get(index));
            assignments.set(index, temp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
