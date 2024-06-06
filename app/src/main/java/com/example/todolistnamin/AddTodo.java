package com.example.todolistnamin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddTodo extends AppCompatActivity {

    // TODO: 6/6/2024 UPDATE - TITLE 
    
    TodoModel todo;
    ListDatabaseHelper db;
    EditText tbTitle, tbDateLabel, tbTimeLabel;
    Button btnAddTodo;
    ImageButton btnSelectDate, btnSelectTime;

    EditText todoEntry;
    LinearLayout todoListContainer;

    String todayDate, intentTitle, intentDate;
    long titleID;
    Calendar today;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        i = getIntent();

        tbTitle = findViewById(R.id.tbAddTitle);
        tbDateLabel = findViewById(R.id.tbDateLabel);
        tbTimeLabel = findViewById(R.id.tbTimeLabel);


        if (i != null) {
            String extraTitle = i.getStringExtra("title");
            String extraDate = i.getStringExtra("date");
            titleID = i.getLongExtra("titleId", 1);

            if (extraDate != null && extraTitle != null) {
                tbDateLabel.setText(extraDate);
                tbTitle.setText(extraTitle);
            }
        }

        today = Calendar.getInstance();
        int yearNow = today.get(Calendar.YEAR);
        int monthNow = today.get(Calendar.MONTH);
        int dayNow = today.get(Calendar.DAY_OF_MONTH);
        todayDate = monthNow + "/" + dayNow + "/" + yearNow;

        todo = new TodoModel();
        db = new ListDatabaseHelper(this);

        todoListContainer = findViewById(R.id.todo_list_container);
        todoEntry = findViewById(R.id.todo_input);
        btnAddTodo = findViewById(R.id.btnAddToDo);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSelectDate = findViewById(R.id.btnSelectDate);

        btnAddTodo.setOnClickListener(v -> {
//            todo.setTitle(tbTitle.getText().toString());
//            todo.setDate(tbDateLabel.getText().toString());
//            todo.setTime(tbTimeLabel.getText().toString());
//
//            db.insertTitleAndTodo(todo);
        });

        btnSelectDate.setOnClickListener(v -> {
            DatePickerDialog dateDialog = new DatePickerDialog(AddTodo.this, (view, year, month, dayOfMonth) ->
                    tbDateLabel.setText(month+ 1+ "/" +dayOfMonth+ "/" +year), yearNow, monthNow, dayNow);

            dateDialog.show();
        });

        btnSelectTime.setOnClickListener(v ->{
            Log.d("clicked clock", "hello");
            TimePickerDialog timeDialog = new TimePickerDialog(AddTodo.this, (view, hourOfDay, minute) ->
                    tbTimeLabel.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute)), 15, 00, false);

            timeDialog.show();
        });

        todoEntry.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String todoText = todoEntry.getText().toString().trim();
                String todoTitle = tbTitle.getText().toString().trim();
                String todoDate = tbDateLabel.getText().toString().trim();
                if (!todoText.isEmpty() && !todoTitle.isEmpty() && !todoDate.isEmpty()) {
                    if (!tbTimeLabel.getText().toString().isEmpty()) {
                        createCheckboxForTodo();
                        db.insertEntryAndTime(titleID, todoText, tbTimeLabel.getText().toString());
                        tbTimeLabel.setText(null);
                        return true;
                    } else {
                        Toast.makeText(this, "Add Time!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Missing Fields!", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });
    }

    private void createCheckboxForTodo() {
        String todoText = todoEntry.getText().toString().trim();
        if (!todoText.isEmpty()) {
            LinearLayout todoItem = new LinearLayout(this);
            todoItem.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            todoItem.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvTimeSelected = new TextView(this);
            tvTimeSelected.setGravity(Gravity.END);
            tvTimeSelected.setText(tbTimeLabel.getText().toString());

            CheckBox checkbox = new CheckBox(this);
            checkbox.setText(todoText);

            todoItem.addView(checkbox);
            todoItem.addView(tvTimeSelected, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f));

            todoListContainer.addView(todoItem);
            todoEntry.getText().clear();
        }
    }
}

//    String todoText = todoEntry.getText().toString().trim();
//        if (!todoText.isEmpty()) {
//                CheckBox checkbox = new CheckBox(this);
//                checkbox.setText(todoText);
//                todoListContainer.addView(checkbox);
//                todoEntry.getText().clear();
//                }