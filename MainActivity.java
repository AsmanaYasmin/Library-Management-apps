package com.project.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.myapplication.helper.PreferenceManager;
import com.project.myapplication.R;
import com.project.myapplication.databinding.ActivityMainBinding;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    PreferenceManager preferenceManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferenceManager = new PreferenceManager(MainActivity.this);

        String title = preferenceManager.getString("name", "User");
        binding.titlebar.setText(title);



        loadTotalMembers();
        loadTotalBooks();

        binding.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookList.class);
                startActivity(intent);
            }
        });

        ///////////////////////////////////////////////////////////////////////////////

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceManager.clear();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////


        binding.member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemberList.class);
                startActivity(intent);
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////




        binding.issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IssuePageActivity.class);
                startActivity(intent);
            }
        });


    }

    ///////////////////////////////////////member show/////////////////////////////////////////////////////////////////
    private void loadTotalMembers() {
        String URL = "https://farhana42.top/get_users.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray users = response.getJSONArray("users");
                            int total = users.length();
                            binding.totalMember.setText("Total Members: " + total);
                        } else {
                            binding.totalMember.setText("Total Members: 0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing user data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }



    private void loadTotalBooks() {
        String totalBooks = "https://farhana42.top/get_booklist.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, totalBooks, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray users = response.getJSONArray("books");
                            int total = users.length();
                            binding.totalBooks.setText("Books: " + total);
                        } else {
                            binding.totalBooks.setText("Total Books: 0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing books data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }


}