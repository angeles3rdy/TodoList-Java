package com.example.todolistnamin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistnamin.ListDatabaseHelper;
import com.example.todolistnamin.R;
import com.example.todolistnamin.TodoModel;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<TodoModel> todo;
    private ListDatabaseHelper db;

    TodoAdapter(List<TodoModel> todo, Context context) {
        this.todo = todo;
        db = new ListDatabaseHelper(context);
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        ImageView btnDelete, btnView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitleCard);
            btnDelete = itemView.findViewById(R.id.btnDeleteCard);
            btnView = itemView.findViewById(R.id.btnViewCard);

        }
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_todos, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoModel todoModel = todo.get(position);

        holder.txtTitle.setText(todoModel.getTitle());

        holder.btnDelete.setOnClickListener(v -> {

            db.deleteTodoAndEntriesById(todoModel.getId());

            refresh(db.fetchTodoTitles());

        });

        holder.btnView.setOnClickListener(v -> {

            Log.d("btnView", "clicked" + todoModel.getId());
            Intent editTodo = new Intent(holder.itemView.getContext(), EditTodo.class);
            editTodo.putExtra("selectedID", todoModel.getId());
            holder.itemView.getContext().startActivity(editTodo);
        });
    }

    @Override
    public int getItemCount() {
        return todo.size();
    }

    public void refresh(List<TodoModel> newTodoList) {
        todo = newTodoList;
        notifyDataSetChanged();
    }
}
