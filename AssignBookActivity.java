package com.project.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.myapplication.databinding.AssignBookActivityBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssignBookActivity extends AppCompatActivity {

    AssignBookActivityBinding binding;
    String bookTitle;
    int bookId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AssignBookActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookId = getIntent().getIntExtra("bookId",0);
        bookTitle = getIntent().getStringExtra("bookTitle");
        Toast.makeText(this, ""+bookId, Toast.LENGTH_SHORT).show();

        binding.bookName.setText(bookTitle);

        loadMembers();

        binding.assignBtn.setOnClickListener(v -> {
            String selectedMember = binding.memberSpinner.getSelectedItem().toString();

            if (selectedMember.isEmpty()) {
                Toast.makeText(this, "Please select a member", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.assignBtn.setEnabled(false);

            assignBookToMember( bookId , bookTitle, selectedMember); // API কল
        });
    }
    private void assignBookToMember(int bookId, String bookName, String memberName) {
        String url = "https://farhana42.top/assign_book.php";

        try {
            JSONObject postData = new JSONObject();
            postData.put("book_id", bookId);
            postData.put("book_name", bookName);
            postData.put("member_name", memberName);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                    response -> {
                        try {
                            // Enable button again after response
                            binding.assignBtn.setEnabled(true);

                            if (response.getBoolean("success")) {
                                Toast.makeText(this, "Book assigned successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, IssuePageActivity.class);
                                intent.putExtra("bookName", bookName);
                                intent.putExtra("memberName", memberName);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Failed: " + response.optString("error"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Response parsing error", Toast.LENGTH_SHORT).show();
                            binding.assignBtn.setEnabled(true); // Enable button on exception
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show();
                        binding.assignBtn.setEnabled(true); // Enable button on error
                    }
            );

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON error", Toast.LENGTH_SHORT).show();
            binding.assignBtn.setEnabled(true); // Enable button on exception
        }
    }

    private void loadMembers() {
        String url = "https://farhana42.top/get_users.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray users = response.getJSONArray("users");
                            List<String> memberNames = new ArrayList<>();

                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                String name = user.getString("name");
                                memberNames.add(name);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                    android.R.layout.simple_spinner_dropdown_item, memberNames);
                            binding.memberSpinner.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, "Failed to load members", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }


}
