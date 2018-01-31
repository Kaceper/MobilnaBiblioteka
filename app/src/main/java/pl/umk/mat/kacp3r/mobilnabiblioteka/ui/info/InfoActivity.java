package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.info;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.BottomNavigationViewHelper;

public class InfoActivity extends AppCompatActivity
{
    private static final int ACTIVITY_NUM = 2;
    private static final String TAG = "InfoActivity";

    @BindView(R.id.bottom_navigation_view) BottomNavigationViewEx bottomNavigationViewEx;

    private void setUpBottomNavigationViewEx()
    {
        BottomNavigationViewHelper.setupNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        Log.d(TAG, "setupBottomNavigationViewEx");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        setUpBottomNavigationViewEx();

        LibsSupportFragment fragment = new LibsBuilder()
                .withAboutIconShown(true)
                    .withVersionShown(true)
                .withLicenseShown(true)
                .withAboutAppName("Mobilna Biblioteka")
                .withAboutDescription("Aplikacja stworzona na rzecz pracy inżynierskiej" +
                        "<br><br>Ikony w aplikacji zostały pobrane ze strony " + Html.fromHtml("<![CDATA[<a href=" + "\"" + "https://icons8.com/icon/new-icons/all" +  "\"" + ">icons8</a>]]>") +
                        "<br>Autorem design'u loga aplikacji jest " + Html.fromHtml("<![CDATA[<a href=" + "\"" + "https://www.freepik.com/free-vector/great-book-logos_1035853.htm" +  "\"" + ">Freepik</a>]]>"))

                .withShowLoadingProgress(false)
                .supportFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
