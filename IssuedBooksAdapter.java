package com.project.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.myapplication.R;
import com.project.myapplication.activity.BookListActivity;
import com.project.myapplication.databinding.ItemIssuedBookBinding;
import com.project.myapplication.model.IssuedBook;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssuedBooksAdapter extends RecyclerView.Adapter<IssuedBooksAdapter.ViewHolder> {

    private List<IssuedBook> issuedBooksList;
    private Context context;

    public IssuedBooksAdapter(Context context, List<IssuedBook> issuedBooksList) {
        this.context = context;
        this.issuedBooksList = issuedBooksList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIssuedBookBinding binding = ItemIssuedBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IssuedBook issuedBook = issuedBooksList.get(position);

        holder.binding.textBookName.setText(issuedBook.getBookName());
        holder.binding.textMemberName.setText(issuedBook.getMemberName());

        // ✅ এখানে return button listener বসাতে হবে
        holder.binding.btnReturnBook.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Confirm Return")
                    .setMessage("Are you sure you want to return this book?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // ✅ এখানে call হচ্ছে
                        returnBookToServer(issuedBook.getBookId(), issuedBook.getId(), position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return issuedBooksList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemIssuedBookBinding binding;

        public ViewHolder(ItemIssuedBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void returnBookToServer(String bookId, String issueId, int position) {
        String url = "https://farhana42.top/return_book.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            Toast.makeText(context, "Book returned!", Toast.LENGTH_SHORT).show();
                            issuedBooksList.remove(position);
                            notifyItemRangeChanged(position, issuedBooksList.size());
                            context.startActivity(new Intent(context, BookListActivity.class));
                            notifyItemRemoved(position);
                        } else {
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("book_id", bookId);
                params.put("issue_id", issueId);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

}
