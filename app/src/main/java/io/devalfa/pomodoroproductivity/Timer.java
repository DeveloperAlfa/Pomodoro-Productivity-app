package io.devalfa.pomodoroproductivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.devalfa.pomodoroproductivity.R;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Timer extends AppCompatActivity {
    String task;
    long time = 1500000;
    NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;
    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        task = getIntent().getStringExtra("Session");
        if(getIntent().hasExtra("time")) time = Long.parseLong(getIntent().getStringExtra("time"));


        createNotificationChannel();


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_NO_CREATE);
         builder = new NotificationCompat.Builder(this, "one")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Pomodoro Session")
                .setContentText("Completing "+task)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());

        CountDownTimer myTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long tot_sec = millisUntilFinished/1000;
                long mint = tot_sec / 60;
                long sec = tot_sec % 60;
                String mints = mint+"", secs = sec+"";
                if(mints.length()==1) mints = "0"+mints;
                if(secs.length()==1) secs = "0"+secs;
                TextView timer = findViewById(R.id.timer);
                timer.setText(mints+":"+secs);
                //intent.putExtra("time", millisUntilFinished+"");
            }

            @Override
            public void onFinish() {
                notificationManager.cancelAll();
                PendingIntent pendingIntent2 = PendingIntent.getActivity(Timer.this, 0, new Intent(Timer.this, MainActivity.class), PendingIntent.FLAG_NO_CREATE);
                builder = new NotificationCompat.Builder(Timer.this, "one")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Pomodoro Completed")
                        .setContentText("Time to celebrate")
                        .setContentIntent(pendingIntent2)
                        .setOngoing(false)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(1, builder.build());
            }
        };
        myTimer.start();
        RecyclerView tasks = findViewById(R.id.tasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tasks.setLayoutManager(linearLayoutManager);
        final ArrayList<String> strings = new ArrayList<>();
        final MyAdapter myAdapter = new MyAdapter(strings);
        tasks.setAdapter(myAdapter);
        ImageButton imageButton = findViewById(R.id.AddTask);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imageButtonRef = findViewById(R.id.AddTask);
                imageButtonRef.setVisibility(View.GONE);
                CardView cardView = findViewById(R.id.task_cv);
                cardView.setVisibility(View.VISIBLE);
            }
        });
        add = findViewById(R.id.addtaskbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText e = findViewById(R.id.newtask_et);
                if(e.getText().toString().length()==0)
                {
                    Toast.makeText(Timer.this, "Enter a task", Toast.LENGTH_SHORT).show();
                    return;
                }
                CardView cardView = findViewById(R.id.task_cv);
                cardView.setVisibility(View.GONE);
                strings.add(e.getText().toString());
                e.setText("");
                myAdapter.notifyDataSetChanged();
                ImageButton imageButtonRef = findViewById(R.id.AddTask);
                imageButtonRef.setVisibility(View.VISIBLE);
            }
        });
        EditText e = findViewById(R.id.newtask_et);
        e.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    add.performClick();
                    return true;
                }
                return false;
            }
        });
        ImageButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Timer.this, MainActivity.class));
                Toast.makeText(Timer.this, "I hope you'll start a new one soon!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        builder.setOngoing(false);
        notificationManager.cancelAll();

    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView taskName = findViewById(R.id.taskHeading);
        taskName.setText(task);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "one";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("one", name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}

class MVH extends RecyclerView.ViewHolder
{


    private TextView t;
    public MVH(View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.item_text);
    }
    public void setT(String t) {
        this.t.setText(t);
    }
}
class MyAdapter extends RecyclerView.Adapter<MVH>
{

    public MyAdapter(@NonNull ArrayList<String> stringList) {
        this.stringList = stringList;
    }

    ArrayList<String> stringList;



    public MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View t = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MVH(t);
    }

    @Override
    public void onBindViewHolder(@NonNull MVH holder, int position) {
        String val = stringList.get(position);
        holder.setT(val);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
