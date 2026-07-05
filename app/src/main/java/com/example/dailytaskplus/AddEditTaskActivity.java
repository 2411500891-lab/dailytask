package com.example.dailytaskplus;

import android.app.*; import android.content.*; import android.os.Bundle; import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dailytask.ApiClient;
import com.example.dailytask.ApiResponse;
import java.util.*;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditTaskActivity extends AppCompatActivity {
    EditText etTitle, etDesc; Spinner spCat, spPri;
    Button btnDate, btnTime, btnSave; TextView tvTitle;
    String date="", time=""; int editId=0, isDone=0; int userId;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_add_edit);
        userId = getSharedPreferences("session",MODE_PRIVATE).getInt("user_id",0);

        etTitle=findViewById(R.id.etTitle); etDesc=findViewById(R.id.etDesc);
        spCat=findViewById(R.id.spCategory); spPri=findViewById(R.id.spPriority);
        btnDate=findViewById(R.id.btnDate); btnTime=findViewById(R.id.btnTime);
        btnSave=findViewById(R.id.btnSave); tvTitle=findViewById(R.id.tvTitle);

        spCat.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Kuliah","Tugas","Praktikum","Organisasi","Pribadi","Umum"}));
        spPri.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Low","Medium","High"}));

        Intent i = getIntent();
        if(i.hasExtra("id")){
            editId = i.getIntExtra("id",0); tvTitle.setText("Edit Tugas ✏️");
            etTitle.setText(i.getStringExtra("title"));
            etDesc.setText(i.getStringExtra("desc"));
            date = i.getStringExtra("date")==null?"":i.getStringExtra("date");
            time = i.getStringExtra("time")==null?"":i.getStringExtra("time");
            btnDate.setText(date.isEmpty()?"📅 Pilih Tanggal":"📅 "+date);
            btnTime.setText(time.isEmpty()?"⏰ Pilih Jam":"⏰ "+time);
            isDone = i.getIntExtra("done",0);
        }

        Calendar c = Calendar.getInstance();
        btnDate.setOnClickListener(v -> new DatePickerDialog(this,(dp,y,m,d)->{
            date = String.format(Locale.US,"%04d-%02d-%02d",y,m+1,d);
            btnDate.setText("📅 "+date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show());
        btnTime.setOnClickListener(v -> new TimePickerDialog(this,(tp,h,mi)->{
            time = String.format(Locale.US,"%02d:%02d:00",h,mi);
            btnTime.setText("⏰ "+time);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show());

        btnSave.setOnClickListener(v -> save());
    }

    private void save(){
        String t = etTitle.getText().toString().trim();
        String d = etDesc.getText().toString().trim();
        if(t.isEmpty()){etTitle.setError("Judul wajib");return;}
        if(date.isEmpty()){Toast.makeText(this,"Pilih tanggal",Toast.LENGTH_SHORT).show();return;}

        String cat = spCat.getSelectedItem().toString();
        String pri = spPri.getSelectedItem().toString();

        String json;
        Call<ApiResponse> call;
        if(editId>0){
            json = String.format(Locale.US,
                    "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\",\"category\":\"%s\",\"priority\":\"%s\",\"due_date\":\"%s\",\"due_time\":\"%s\",\"is_done\":%d}",
                    editId,esc(t),esc(d),cat,pri,date,time,isDone);
            call = ApiClient.get().updateTask(RequestBody.create(MediaType.parse("application/json"),json));
        } else {
            json = String.format(Locale.US,
                    "{\"user_id\":%d,\"title\":\"%s\",\"description\":\"%s\",\"category\":\"%s\",\"priority\":\"%s\",\"due_date\":\"%s\",\"due_time\":\"%s\"}",
                    userId,esc(t),esc(d),cat,pri,date,time);
            call = ApiClient.get().addTask(RequestBody.create(MediaType.parse("application/json"),json));
        }
        call.enqueue(new Callback<ApiResponse>() {
            @Override public void onResponse(Call<ApiResponse> c, Response<ApiResponse> r) {
                Toast.makeText(AddEditTaskActivity.this, r.body().message, Toast.LENGTH_SHORT).show();
                if("success".equals(r.body().status)) finish();
            }
            @Override public void onFailure(Call<ApiResponse> c, Throwable t) {
                Toast.makeText(AddEditTaskActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private String esc(String s){ return s==null?"":s.replace("\"","\\\""); }
}
