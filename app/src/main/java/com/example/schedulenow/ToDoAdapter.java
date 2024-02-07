package com.example.schedulenow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class ToDoAdapter extends
    RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView detailsTextView;
        public TextView dueDateTextView;
        public Button completeToDoButton;
        public Button editToDoButton;

        public ViewHolder(View itemView) {
            super(itemView);

            detailsTextView = (TextView) itemView.findViewById(R.id.todo_details);
            dueDateTextView = (TextView) itemView.findViewById(R.id.todo_due_date);
            completeToDoButton = (Button) itemView.findViewById(R.id.completeToDoButton);
            completeToDoButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeToDo(position);
                }
            });
            editToDoButton = (Button) itemView.findViewById(R.id.editToDoButton);
            editToDoButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ToDo todo = toDos.get(position);
                    onEditToDoCallback.accept(todo);
                }
            });
        }
    }

    // member variable for the todos
    private List<ToDo> toDos;
    private Context context;
    private Consumer<Void> onToDoRemovedCallback; // Callback to be invoked on todo removal
    private Consumer<ToDo> onEditToDoCallback;

    public ToDoAdapter(Context context, List<ToDo> toDos, Consumer<Void> onToDoRemovedCallback, Consumer<ToDo> onEditToDoCallback) {
        this.context = context;
        this.toDos = toDos;
        this.onToDoRemovedCallback = onToDoRemovedCallback;
        this.onEditToDoCallback = onEditToDoCallback;
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View ToDoView = inflater.inflate(R.layout.item_todo, parent, false);

        // return a new instance of the holder
        return new ToDoAdapter.ViewHolder(ToDoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        ToDo todo = toDos.get(position);
        if (todo != null) {
            TextView textView1 = holder.detailsTextView;
            textView1.setText(todo.getName());
            TextView textView2 = holder.dueDateTextView;
            textView2.setText(todo.getTime());
        }
    }

    private void removeToDo(int position) {
        if (position < toDos.size() && position >= 0) {
            toDos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, toDos.size());
            onToDoRemovedCallback.accept(null); // Notify the fragment
        }
    }

    // return total count of todo items in the list
    @Override
    public int getItemCount () {
        return toDos.size();
    }
}
