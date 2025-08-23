package com.project.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.myapplication.R;
import com.project.myapplication.adapter.BookListAdapter;
import com.project.myapplication.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity {

    FloatingActionButton addButton;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<Book> bookList = new ArrayList<>();
    BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_list_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        addButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // RecyclerView setup
        adapter = new BookListAdapter(this, bookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load data
        loadPdf();

        // Add book button click
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(BookList.this, AddBook.class);
            startActivity(intent);
        });
    }

    private void loadPdf() {
        progressBar.setVisibility(View.VISIBLE);
        bookList.clear(); // Clear old data

        String url = "https://farhana42.top/get_booklist.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "ইন্টারনেট সংযোগ ব্যর্থ", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
