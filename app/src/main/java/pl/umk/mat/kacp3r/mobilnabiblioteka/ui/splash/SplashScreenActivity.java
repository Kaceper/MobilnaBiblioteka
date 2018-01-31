package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search.SearchActivity;

public class SplashScreenActivity extends AppCompatActivity
{
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent searchScreen = new Intent(SplashScreenActivity.this, SearchActivity.class);
                SplashScreenActivity.this.startActivity(searchScreen);
                overridePendingTransition(0, 0);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
