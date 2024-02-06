package com.example.schedulenow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.schedulenow.databinding.FragmentToDoBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ToDoFragment extends Fragment {

    private FragmentToDoBinding binding;
    private ArrayList<ToDo> toDos;
    private ToDoAdapter adapter;

    // you need a way to store and retrieve data, even if the app is closed and reopened
    // to do this, you can store the data as a JSON string in SharedPreferences
    private static final String PREFS_NAME = "ToDoPrefs";
    private static final String KEY_TODO_LIST = "todoList";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        binding = FragmentToDoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize classes and adapter member variables
        toDos = new ArrayList<>();
        adapter = new ToDoAdapter(requireContext(), toDos, unused -> saveToDoPreferences(), this::showEditToDoDialog);

        // Find the correct RecyclerView layout
        RecyclerView recyclerView = root.findViewById(R.id.rvTodo);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadToDoesPreferences();

        adapter.notifyDataSetChanged();

        // Find the floating action button from the xml file
        com.google.android.material.floatingactionbutton.FloatingActionButton floatingButton =
                root.findViewById(R.id.floatingAddToDo);

        floatingButton.setOnClickListener(view -> showDialogAddToDo());

        Button sortButton = root.findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> showSortOptionsDialog());

        return root;
    }

    // Populate dialog box for user to add classes
    @SuppressLint("NotifyDataSetChanged")
    private void showDialogAddToDo() {
        // Create AlertDialog to pop up
        AlertDialog.Builder todoBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_todo_dialog, null);
        todoBuilder.setView(dialogView);
        AlertDialog dialog = todoBuilder.create();

        // Find the views from the dialog class layout
        EditText editToDoName = dialogView.findViewById(R.id.editToDoName);
        EditText editToDoDueDate = dialogView.findViewById(R.id.editToDoDueDate);
        Button addToDoButton = dialogView.findViewById(R.id.addToDoButton);
        Button cancelToDoButton = dialogView.findViewById(R.id.cancelToDoButton);

        addToDoButton.setOnClickListener(view -> {
            String todoName = editToDoName.getText().toString();
            String todoTime = editToDoDueDate.getText().toString();

            // check if the fields are not empty before creating new class
            if (!todoName.isEmpty() && !todoTime.isEmpty()) {
                toDos.add(new ToDo(todoName, todoTime));
                saveToDoPreferences();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // set up click listener for cancel button to dismiss dialog
        cancelToDoButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showEditToDoDialog(ToDo toDoToEdit) {
        // Create AlertDialog to pop up
        AlertDialog.Builder todoBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.edit_todo_dialog, null);
        todoBuilder.setView(dialogView);
        AlertDialog dialog = todoBuilder.create();

        // Find the views from the dialog class layout
        EditText editToDoName = dialogView.findViewById(R.id.editToDoName);
        EditText editToDoDueDate = dialogView.findViewById(R.id.editToDoDueDate);
        Button saveToDoButton = dialogView.findViewById(R.id.saveToDoButton);
        Button cancelAssignmentButton = dialogView.findViewById(R.id.cancelToDoButton);

        // Set the initial values
        editToDoName.setText(toDoToEdit.getName());
        editToDoDueDate.setText(toDoToEdit.getTime());

        saveToDoButton.setOnClickListener(view -> {
            // Retrieve edited values
            String updatedTitle = editToDoName.getText().toString();
            String updatedDueDate = editToDoDueDate.getText().toString();

            // Update the todos in the list
            toDoToEdit.setTitle(updatedTitle);
            toDoToEdit.setDueDate(updatedDueDate);

            // Save the updated todos list
            saveToDoPreferences();

            // Notify the adapter of the change
            adapter.notifyDataSetChanged();

            // Dismiss the dialog
            dialog.dismiss();
        });

        // set up click listener for cancel button to dismiss dialog
        cancelAssignmentButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void loadToDoesPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String todosJSON = preferences.getString(KEY_TODO_LIST, null);

        if (todosJSON != null) {
            Type listType = new TypeToken<List<ToDo>>() {}.getType();
            List<ToDo> loadedToDoes = new Gson().fromJson(todosJSON, listType);

            toDos.clear();
            toDos.addAll(loadedToDoes);
        }
    }

    private void saveToDoPreferences(){
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String todosJSON = new Gson().toJson(toDos);

        editor.putString(KEY_TODO_LIST, todosJSON);
        editor.apply();
    }

    private void showSortOptionsDialog() {
        // Using AlertDialog for a single sorting option
        AlertDialog.Builder sortBuilder = new AlertDialog.Builder(requireContext());
        sortBuilder.setTitle("Sort Options");

        // Add sorting option to the list
        sortBuilder.setItems(new CharSequence[]{"Sort by Due Date"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the selected sorting option
                if (which == 0) {
                    sortToDosByDueDate();
                }
            }
        });

        // Display the dialog
        sortBuilder.create().show();
    }
    private void sortToDosByDueDate() {
        selectionSorter(toDos, Comparator.comparing(ToDo::getTime));
        adapter.notifyDataSetChanged();
    }

    private void selectionSorter(ArrayList<ToDo> toDos, Comparator<ToDo> comparator) {
        for (int i = 0; i < toDos.size() - 1; ++i) {
            int index = i;
            for (int j = i + 1; j < toDos.size(); ++j) {
                if (comparator.compare(toDos.get(j), toDos.get(index)) < 0) {
                    index = j;
                }
            }
            ToDo temp = toDos.get(i);
            toDos.set(i, toDos.get(index));
            toDos.set(index, temp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}