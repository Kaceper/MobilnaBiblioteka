package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.info.InfoActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.LibraryActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search.SearchActivity;

public class BottomNavigationViewHelper
{
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupNavigationView(BottomNavigationViewEx bottomNavigationViewEx)
    {
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
        bottomNavigationViewEx.setIconVisibility(true);

        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx bottomNavigationViewEx)
    {
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.search_icon:
                        Intent search = new Intent(context, SearchActivity.class); //ACTIVITY_NUM = 0
                        search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(search);
                        break;

                        /*
                    case R.id.discover_icon:
                        Intent search1 = new Intent(context, SearchActivity.class); //ACTIVITY_NUM = 1
                        search1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(search1);
                        break;
                        */

                    case R.id.library_icon:
                        Intent library = new Intent(context, LibraryActivity.class); //ACTIVITY_NUM = 1
                        library.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(library);
                        break;

                    case R.id.info_icon:
                        Intent info = new Intent(context, InfoActivity.class); //ACTIVITY_NUM = 2
                        info.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(info);
                        break;
                }
                return false;
            }
        });
    }
}