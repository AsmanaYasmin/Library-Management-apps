package com.project.myapplication.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.myapplication.R;
import com.project.myapplication.model.User;
import com.project.myapplication.adapter.UserAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> userList;
    String URL = "https://farhana42.top/get_users.php"; // ✅ Change to your own

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list_activity);

        recyclerView = findViewById(R.id.recyclerViewMembers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        loadUsersFromServer();
    }

    private void loadUsersFromServer() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray users = response.getJSONArray("users");

                            userList.clear();
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                int id = user.getInt("id");
                                String name = user.getString("name");
                                String phone = user.getString("phone");

                                userList.add(new User(id, name, phone));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MemberListActivity.this, "Network error: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

}