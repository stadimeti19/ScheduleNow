package com.example.schedulenow;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class ClassesAdapter extends
    RecyclerView.Adapter<ClassesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView timeTextView;
        public TextView instructorTextView;
        public Button deleteClassButton;
        public Button editClassButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.class_name);
            timeTextView = (TextView) itemView.findViewById(R.id.class_time);
            instructorTextView = (TextView) itemView.findViewById(R.id.class_instructor);
            deleteClassButton = (Button) itemView.findViewById(R.id.deleteClassButton);
            deleteClassButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeClass(position);
                }
            });
            editClassButton = (Button) itemView.findViewById(R.id.editClassButton);
            editClassButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Class aClass = cClasses.get(position);
                    onEditClassCallback.accept(aClass);
                }
            });
        }
    }

    // more member variables for classes
    private List<Class> cClasses;
    private Context context;
    private Consumer<Void> onClassRemovedCallback; // Callback to be invoked on class removal
    private Consumer<Class> onEditClassCallback;

    private static final String PREFS_NAME = "ClassPrefs";
    private static final String KEY_CLASSES_LIST = "classesList";

    public ClassesAdapter(Context context, List<Class> classes, Consumer<Void> onClassRemovedCallback, Consumer<Class> onEditClassCallback) {
        this.context = context;
        cClasses = classes;
        this.onClassRemovedCallback = onClassRemovedCallback;
        this.onEditClassCallback = onEditClassCallback;
    }

    @NonNull
    @Override
    public ClassesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View classView = inflater.inflate(R.layout.item_class, parent, false);

        // return a new instance of the holder
        return new ClassesAdapter.ViewHolder(classView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesAdapter.ViewHolder holder, int position) {
        Class c = cClasses.get(position);
        if (c != null) {
            TextView textView1 = holder.nameTextView;
            textView1.setText(c.getName());
            TextView textView2 = holder.timeTextView;
            textView2.setText(c.getTime());
            TextView textView3 = holder.instructorTextView;
            textView3.setText(c.getInstructor());
        }
    }

    // method to properly remove the class if user wants to remove
    private void removeClass(int position) {
        if (position < cClasses.size() && position >= 0) {
            cClasses.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cClasses.size());
            onClassRemovedCallback.accept(null); // Notify the fragment
        }
    }

    // return total count of class items in the list
    @Override
    public int getItemCount() {
        return cClasses.size();
    }

}
