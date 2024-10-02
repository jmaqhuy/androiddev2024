package vn.edu.usth.weather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;


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
        AsyncTask<String, Integer, Bitmap> task = new AsyncTask<String, Integer, Bitmap>() {

            @Override
            protected void onPreExecute() {
                // You can prepare the UI here (e.g., showing a progress bar)
                Log.i("USTHWeather", "Starting download...");
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection =
                            (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    int response = connection.getResponseCode();
                    Log.i("USTHWeather", "The response is: " + response);
                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e){
                    bitmap = null;
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ImageView logo = (ImageView) findViewById(R.id.logo);
                    logo.setImageBitmap(bitmap);
                    Toast.makeText(WeatherActivity.this, "Image set successfully", Toast.LENGTH_SHORT).show();
                    Log.i("USTHWeather", "Image set successfully.");
                } else {
                    Log.e("USTHWeather", "Failed to download the image.");
                }
            }
        };
        task.execute("https://cdn.haitrieu.com/wp-content/uploads/2022/11/Logo-Truong-Dai-hoc-Khoa-hoc-va-Cong-nghe-Ha-Noi-VN-France-University.png");
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