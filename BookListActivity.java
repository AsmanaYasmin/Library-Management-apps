package com.project.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.myapplication.R;
import com.project.myapplication.adapter.BookListAdapter;
import com.project.myapplication.databinding.BookListActivityBinding;
import com.project.myapplication.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private BookListActivityBinding binding;
    private List<Book> bookList = new ArrayList<>();
    private BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = BookListActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // System bars padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView setup
        adapter = new BookListAdapter(this, bookList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Add book button click
        binding.addButton.setOnClickListener(v -> {
            startActivity(new Intent(BookListActivity.this, AddBookActivity.class));
        });

        // Load data
        loadPdf();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Optional: শুধুমাত্র AddBookActivity থেকে ফিরে আসলে reload করতে চাইলে এখানে কল করুন
      //  loadPdf();
    }

    private void loadPdf() {
        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        // Clear old data to avoid double
        bookList.clear();
        adapter.notifyDataSetChanged();

        String url = "https://farhana42.top/get_booklist.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    binding.progressBar.setVisibility(android.view.View.GONE);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            JSONArray array = json.getJSONArray("books");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                String status = obj.getString("status");
                                if ("return".equalsIgnoreCase(status)) {  // শুধু return স্ট্যাটাসের বই নেবে
                                    Book book = new Book(
                                            obj.getInt("id"),
                                            obj.getString("title"),
                                            obj.getString("authorname"),
                                            obj.getString("file_url"),
                                            status
                                    );
                                    bookList.add(book);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "কোনো বই পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    binding.progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(this, "ইন্টারনেট সংযোগ ব্যর্থ", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
