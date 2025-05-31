package com.example.todolistapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private EditText taskInput, dateInput;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ThreeTenABP here:
        AndroidThreeTen.init(this);

        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.task_input);
        dateInput = findViewById(R.id.date_input);
        Button addButton = findViewById(R.id.add_button);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, task -> taskAdapter.removeTask(task));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        dateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
                // month +1 because Calendar months are 0-based
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                dateInput.setText(selectedDate.toString());
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        addButton.setOnClickListener(view -> {
            String title = taskInput.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDate == null) {
                Toast.makeText(this, "Please select a due date", Toast.LENGTH_SHORT).show();
                return;
            }

            Task newTask = new Task(title, selectedDate);
            taskAdapter.addTask(newTask);

            // Reset inputs
            taskInput.setText("");
            dateInput.setText("");
            selectedDate = null;
        });
    }
}
