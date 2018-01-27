package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.BottomNavigationViewHelper;
import retrofit2.Retrofit;

public class LibraryActivity extends AppCompatActivity
{
    private static final String TAG = "LibraryActivity";
    @Inject Retrofit retrofit;

    @BindView(R.id.constraint_layout) ConstraintLayout constraintLayout;
    @BindView(R.id.bottom_navigation_view) BottomNavigationViewEx bottomNavigationViewEx;
    @BindView(R.id.library_radio_group) RadioGroup libraryRadioGroup;
    @BindView(R.id.to_read_radio) RadioButton toReadRadioButton;
    @BindView(R.id.progress_radio) RadioButton progressRadioButton;
    @BindView(R.id.finished_radio) RadioButton finishedRadioButton;
    @BindView(R.id.page_count_text_view) TextView pageCountTextView;

    private static final int ACTIVITY_NUM = 1;
    private FinishedFragment finishedFragment;
    private ProgressFragment progressFragment;
    private ToReadFragment toReadFragment;
    private TextView textView;

    private void setUpBottomNavigationViewEx()
    {
        BottomNavigationViewHelper.setupNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        Log.d(TAG, "setupBottomNavigationViewEx");
    }

    @OnClick({ R.id.to_read_radio, R.id.progress_radio, R.id.finished_radio })
    public void onRadioButtonClicked(RadioButton radioButton)
    {
        boolean checked = radioButton.isChecked();

        switch (radioButton.getId())
        {
            case R.id.to_read_radio:
                if (checked)
                {
                    changeFragment(toReadRadioButton);
                }
                break;

            case R.id.progress_radio:
                if (checked)
                {
                    changeFragment(progressRadioButton);
                }
                break;

            case R.id.finished_radio:
                if (checked)
                {
                    changeFragment(finishedRadioButton);
                }
                break;
        }
    }

    private void changeFragment(View view)
    {
        if (view == toReadRadioButton)
        {
            toReadRadioButton.setChecked(true);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ToReadFragment());
            fragmentTransaction.commit();
        }

        if (view == progressRadioButton)
        {
            progressRadioButton.setChecked(true);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ProgressFragment(), "PROGRESS_FRAGMENT");
            fragmentTransaction.commit();
        }

        if (view == finishedRadioButton)
        {
            finishedRadioButton.setChecked(true);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FinishedFragment());
            fragmentTransaction.commit();
        }
    }

    private void changeFragmentAtStart()
    {
        // TODO
        changeFragment(progressRadioButton);
    }

    public void makeToast(String toastMessage)
    {
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }

    public void setPageCountTextView()
    {
        if (RealmController.with(this).readedPagesCount() > 0)
        {
            pageCountTextView.setText(RealmController.with(this).readedPagesCount()
                    + " przeczytanych stron");

            /*
            if (finishedRadioButton.isChecked())
            {
                finishedFragment.handleFinishedFragmentRecyclerView();
            }
            else if (progressRadioButton.isChecked())
            {
                progressFragment.handleProgressFragmentRecyclerView();
            }
            else
            {
                toReadFragment.handleToReadFragmentRecyclerView();
            }
            */
        }
        else
        {
            pageCountTextView.setVisibility(View.INVISIBLE);
            handleFragmentsContainerMargin();

            /*
            if (finishedRadioButton.isChecked())
            {
                finishedFragment.handleFinishedFragmentRecyclerView();
            }
            else if (progressRadioButton.isChecked())
            {
                progressFragment.handleProgressFragmentRecyclerView();
            }
            else
            {
                toReadFragment.handleToReadFragmentRecyclerView();
            }
            */
        }
    }

    private void handleFragmentsContainerMargin()
    {
        if (RealmController.with(this).readedPagesCount() > 0)
        {
            pageCountTextView.setVisibility(View.VISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.fragment_container, ConstraintSet.BOTTOM, R.id.page_count_text_view, ConstraintSet.TOP, 0);
            constraintSet.applyTo(constraintLayout);

            //emptyFinishedBooksListTextView.setVisibility(View.INVISIBLE);
        }
        else
        {
            pageCountTextView.setVisibility(View.INVISIBLE);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.fragment_container, ConstraintSet.BOTTOM, R.id.bottom_navigation_view, ConstraintSet.TOP, 0);
            constraintSet.applyTo(constraintLayout);

            //emptyFinishedBooksListTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

        if (libraryRadioGroup.getCheckedRadioButtonId() == R.id.to_read_radio)
        {
            changeFragment(toReadRadioButton);
        }
        else if (libraryRadioGroup.getCheckedRadioButtonId() == R.id.progress_radio)
        {
            changeFragment(progressRadioButton);
        }
        else
        {
            changeFragment(finishedRadioButton);
        }

        handleFragmentsContainerMargin();
        setPageCountTextView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        setUpBottomNavigationViewEx();
        changeFragmentAtStart();

        finishedFragment = new FinishedFragment();
        progressFragment = new ProgressFragment();
        toReadFragment = new ToReadFragment();

        handleFragmentsContainerMargin();
        setPageCountTextView();
    }
}
