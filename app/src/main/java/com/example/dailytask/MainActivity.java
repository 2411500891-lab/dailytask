package com.example.dailytask;

import android.content.*; import android.os.Bundle; import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.*; import androidx.recyclerview.widget.*;

import com.example.dailytaskplus.AddEditTaskActivity;
import com.example.dailytaskplus.R;
import com.example.dailytaskplus.model.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import java.util.*;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TaskAdapter.Listener {
    RecyclerView rv; TaskAdapter adapter; List<Task> list = new ArrayList<>();
    int userId; TextView tvHello, tvTaskCount; ExtendedFloatingActionButton fab; Button btnLogout;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        userId = sp.getInt("user_id",0);
        tvHello = findViewById(R.id.tvHello);
        tvTaskCount = findViewById(R.id.tvTaskCount);
        tvHello.setText(sp.getString("name","User")+" 👋");

        rv = findViewById(R.id.rvTasks);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this,list,this); rv.setAdapter(adapter);

        fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditTaskActivity.class)));

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            sp.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class)); finish();
        });

        // Interactive scroll: hide FAB text on scroll
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 10) fab.shrink();
                else if (dy < -10) fab.extend();
            }
        });
    }

    @Override protected void onResume(){ super.onResume(); loadTasks(); }

    private void loadTasks(){
        ApiClient.get().getTasks(userId).enqueue(new Callback<com.example.dailytask.ApiResponse>() {
            @Override public void onResponse(Call<com.example.dailytask.ApiResponse> c, Response<com.example.dailytask.ApiResponse> r) {
                if (r.body()!=null && r.body().data!=null) {
                    list.clear(); list.addAll(r.body().data); 
                    adapter.notifyDataSetChanged();
                    updateCount();
                }
            }
            @Override public void onFailure(Call<com.example.dailytask.ApiResponse> c, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal muat tugas: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateCount() {
        int active = 0;
        for (Task t : list) if (t.is_done == 0) active++;
        tvTaskCount.setText("Kamu punya " + active + " tugas belum selesai");
    }

    @Override public void onToggle(Task t){
        String json = new Gson().toJson(t);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiClient.get().updateTask(body).enqueue(new Callback<ApiResponse>() {
            @Override public void onResponse(Call<ApiResponse> c, Response<ApiResponse> r) { 
                if (r.body() != null) {
                    if ("success".equals(r.body().status)) {
                        loadTasks(); 
                    } else {
                        Toast.makeText(MainActivity.this, "Server: " + r.body().message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Gagal: Server Error " + r.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<ApiResponse> c, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override public void onDelete(Task t){
        new AlertDialog.Builder(this).setTitle("Hapus tugas?").setMessage(t.title)
                .setPositiveButton("Hapus",(d,w)->{
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"),"{\"id\":"+t.id+"}");
                    ApiClient.get().deleteTask(body).enqueue(new Callback<ApiResponse>() {
                        @Override public void onResponse(Call<ApiResponse> c, Response<ApiResponse> r) {
                            Toast.makeText(MainActivity.this,"Terhapus",Toast.LENGTH_SHORT).show(); loadTasks();
                        }
                        @Override public void onFailure(Call<ApiResponse> c, Throwable t) {}
                    });
                }).setNegativeButton("Batal",null).show();
    }
    private String esc(String s){ return s==null?"":s.replace("\"","\\\""); }
}