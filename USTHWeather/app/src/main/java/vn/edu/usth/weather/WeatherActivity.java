package vn.edu.usth.weather;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("CreateApplication", "onCreate() is being executed!");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mediaPlayer = MediaPlayer.create(this,R.raw.weatherforecast);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });

        mediaPlayer.start();
        handler = new Handler(Looper.getMainLooper());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        TabLayout tabLayout =(TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(pager);
    }

    private void NetworkSimulator(){
        new Thread( ()-> {
            try {
                Thread.sleep(3000);
            } catch (Exception e){
                Log.i("Thread Error", "There is Thread error in NetworkSimulator");
            }
            handler.post(() ->
                    Toast.makeText(WeatherActivity.this, "Refresh Completed", Toast.LENGTH_SHORT).show()
            );
        }

        ).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.refresh){
            Log.i("Refresh", "Refresh Homepage Button clicked");
            Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
            NetworkSimulator();
            return true;
        }
        if (item.getItemId() == R.id.setting){
            Intent refIntent = new Intent(this, PrefActivity.class);
            startActivity(refIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        Log.i("StartApplication", "onStart() is being executed!");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("ResumeApplication", "onResume() is being executed!");
        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.i("PauseApplication", "onPause() is being executed!");
        super.onPause();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    @Override
    protected void onStop() {
        Log.i("StopApplication", "onStop() is being executed!");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("DestroyApplication", "onDestroy() is being executed!");
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
    }
}