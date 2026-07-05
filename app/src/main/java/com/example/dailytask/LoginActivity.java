package com.example.dailytask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dailytaskplus.R;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.*;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPass; Button btn; TextView tvGo;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPass  = findViewById(R.id.etPassword);
        btn     = findViewById(R.id.btnLogin);
        tvGo    = findViewById(R.id.tvGoRegister);

        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        if (sp.getInt("user_id", 0) > 0) { goMain(); return; }

        tvGo.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        btn.setOnClickListener(v -> login());
    }

    private void login() {
        String e = etEmail.getText().toString().trim();
        String p = etPass.getText().toString().trim();
        if (e.isEmpty()) { etEmail.setError("Email wajib"); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) { etEmail.setError("Format email salah"); return; }
        if (p.length() < 6) { etPass.setError("Password minimal 6"); return; }

        String json = "{\"email\":\""+e+"\",\"password\":\""+p+"\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiClient.get().login(body).enqueue(new Callback<ApiResponse>() {
            @Override public void onResponse(Call<ApiResponse> c, Response<ApiResponse> r) {
                ApiResponse res = r.body();
                if (res != null && "success".equals(res.status)) {
                    SharedPreferences.Editor ed = getSharedPreferences("session", MODE_PRIVATE).edit();
                    ed.putInt("user_id", res.user.id);
                    ed.putString("name", res.user.name);
                    ed.apply();
                    Toast.makeText(LoginActivity.this, "Selamat datang " + res.user.name, Toast.LENGTH_SHORT).show();
                    goMain();
                } else Toast.makeText(LoginActivity.this, res != null ? res.message : "Gagal", Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<ApiResponse> c, Throwable t) {
                String msg = t.getMessage();
                if (msg != null && msg.contains("failed to connect")) {
                    msg = "Gagal terhubung ke server. Pastikan IP " + ApiClient.BASE_URL + " benar dan server sudah jalan.";
                }
                Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void goMain(){ startActivity(new Intent(this, MainActivity.class)); finish(); }
}
