package com.example.dailytask;
import android.os.Bundle; import android.util.Patterns; import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailytaskplus.R;
import okhttp3.MediaType; import okhttp3.RequestBody; import retrofit2.*;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPass; Button btn; TextView tvGo;
    @Override protected void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_register);
        etName=findViewById(R.id.etName); etEmail=findViewById(R.id.etEmail);
        etPass=findViewById(R.id.etPassword); btn=findViewById(R.id.btnRegister);
        tvGo=findViewById(R.id.tvGoLogin);
        btn.setOnClickListener(v->register());
        tvGo.setOnClickListener(v->finish());
    }
    private void register(){
        String n=etName.getText().toString().trim(), e=etEmail.getText().toString().trim(), p=etPass.getText().toString().trim();
        if(n.isEmpty()){etName.setError("Wajib");return;}
        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){etEmail.setError("Email invalid");return;}
        if(p.length()<6){etPass.setError("Min 6 karakter");return;}
        String json = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",n,e,p);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiClient.get().register(body).enqueue(new Callback<ApiResponse>(){
            @Override public void onResponse(Call<ApiResponse> c, Response<ApiResponse> r){
                Toast.makeText(RegisterActivity.this, r.body().message, Toast.LENGTH_SHORT).show();
                if("success".equals(r.body().status)) finish();
            }
            @Override public void onFailure(Call<ApiResponse> c, Throwable t){
                String msg = t.getMessage();
                if (msg != null && msg.contains("failed to connect")) {
                    msg = "Gagal terhubung ke server. Pastikan IP " + ApiClient.BASE_URL + " benar dan server sudah jalan.";
                }
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
