package com.project.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.myapplication.R;
import com.project.myapplication.adapter.IssuedBooksAdapter;
import com.project.myapplication.model.IssuedBook;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IssuePageActivity extends AppCompatActivity {

    RecyclerView recyclerViewIssuedBooks;   // একবার ডিক্লেয়ার করলেই হবে
    IssuedBooksAdapter adapter;
    List<IssuedBook> issuedBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_page_activity);

        recyclerViewIssuedBooks = findViewById(R.id.recyclerViewIssuedBooks);
        recyclerViewIssuedBooks.setLayoutManager(new LinearLayoutManager(this));
        issuedBookList = new ArrayList<>();

        adapter = new IssuedBooksAdapter(this, issuedBookList);
        recyclerViewIssuedBooks.setAdapter(adapter);

        loadIssuedBooks();
    }

    private void loadIssuedBooks() {

        String url = "https://farhana42.top/get_issued_books.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            issuedBookList.clear();
                            JSONArray books = response.getJSONArray("data");
                            for (int i = 0; i < books.length(); i++) {
                                JSONObject book = books.getJSONObject(i);
                                String id = book.getString("id");
                                String bookId = book.getString("book_id");
                                String bookName = book.getString("book_name");
                                String memberName = book.getString("member_name");
                              issuedBookList.add(new IssuedBook(id, bookId, bookName, memberName));
                            }
                            adapter.notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}