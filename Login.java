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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.myapplication.helper.PreferenceManager;
import com.project.myapplication.R;
import com.project.myapplication.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /////////////////////////////////
        preferenceManager = new PreferenceManager(this);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////


        binding.textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });


        ////////////////////////////////////////////////////////////////////////////////

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.editTextUsernameOrPhone.getText().toString();
                String password = binding.editTextLoginPassword.getText().toString();


                loginUser(phone, password);
            }
        });

    }

    ////////////////////////////////////////////////////////////////
    private void loginUser(String phone, String password) {

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "ফোন নম্বর ও পাসওয়ার্ড দিন", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://farhana42.top/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.getString("status");

                        if (status.equals("VALID LOGIN")) {

                            int userId = json.getInt("id");
                            String name = json.getString("name");
                            String phoneNumber = json.getString("phone");


                            preferenceManager.putInt("user_id", userId);
                            preferenceManager.putString("name", name);
                            preferenceManager.putString("phone", phoneNumber);

                            Toast.makeText(this, "লগইন সফল", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "ভুল ফোন নম্বর অথবা পাসওয়ার্ড", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "ইন্টারনেট সমস্যা! পরে চেষ্টা করুন", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("phone", phone.trim());
                map.put("password", password.trim());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


}