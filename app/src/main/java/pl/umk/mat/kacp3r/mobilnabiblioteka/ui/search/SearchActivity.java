package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.umk.mat.kacp3r.mobilnabiblioteka.MobilnaBiblioteka;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.RestApi;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.Item;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.SearchResponse;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.BottomNavigationViewHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity
{
    private static final String TAG = "SearchActivity";
    @Inject Retrofit retrofit;

    @BindView(R.id.constraint_layout) ConstraintLayout constraintLayout;
    @BindView(R.id.bottom_navigation_view) BottomNavigationViewEx bottomNavigationViewEx;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.search_editText) EditText searchEditText;
    @BindView(R.id.hint_linear_layout) LinearLayout hintLinearLayout;
    //@BindView(R.id.search_button) Button searchButton;
    //@BindView(R.id.search_book_image_button) ImageButton searchBookImageButton;
    @BindView(R.id.barcode_scan_image_button) ImageButton barcodeScanImageButton;
    @BindView(R.id.empty_search_result_textView) TextView emptySearchResultTextView;
    @BindView(R.id.page_number_textView) TextView pageNumberTextView;
    @BindView(R.id.back_button) ImageButton backButton;
    @BindView(R.id.forward_button) ImageButton forwardButton;

    private List<Item> bookList;
    private SearchRecyclerViewAdapter adapter;
    private static final int ACTIVITY_NUM = 0;
    private int resultsCount;
    private int actualPage;
    private Realm realm;

    @OnClick(R.id.back_button)
    public void onBackButtonClick()
    {
        if (actualPage > 1)
        {
            actualPage = actualPage - 1;
        }
        else
        {
            actualPage = 1;
        }

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        searchRequestWithRetrofit(searchEditText.getText().toString(), actualPage);
        handlePagination();
    }

    @OnClick(R.id.forward_button)
    public void onForwardButtonClick()
    {
        actualPage = actualPage + 1;

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        searchRequestWithRetrofit(searchEditText.getText().toString(), actualPage);
        handlePagination();
    }

    @OnEditorAction(R.id.search_editText)
    public boolean onSearchBookImageButtonClick(TextView v, int actionId, KeyEvent event)
    {
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
        {
            actualPage = 1;

            searchRequestWithRetrofit(searchEditText.getText().toString(), 0);

            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            recyclerView.requestFocus();
            return true;
        }
        return false;
    }

    @OnClick(R.id.barcode_scan_image_button)
    public void onBarcodeScanImageButtonClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                searchRequestWithRetrofit(result.getContents(), 0);
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpBottomNavigationViewEx()
    {
        BottomNavigationViewHelper.setupNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        Log.d(TAG, "setupBottomNavigationViewEx");
    }

    private void searchRequestWithRetrofit(final String bookName,
                                           int startIndex)
    {
        if (startIndex > 0)
        {
            startIndex = (startIndex - 1)* 10;
        }

        ((MobilnaBiblioteka) getApplication()).getNetComponent().inject(this);
        RestApi service = retrofit.create(RestApi.class);
        Call<SearchResponse> call = service.getBooks(bookName, startIndex, 10, "books", getString(R.string.api_key));

        call.enqueue(new Callback<SearchResponse>()
        {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response)
            {
                if (response.isSuccessful())
                {
                    hintLinearLayout.setVisibility(View.INVISIBLE);
                    bookList = new ArrayList<>();

                    if (response.body().getTotalItems() != 0 || response.body().getItems() != null)
                    {
                        emptySearchResultTextView.setVisibility(View.INVISIBLE);

                        resultsCount = response.body().getTotalItems();

                        handleRecyclerViewMargin();
                        handlePagination();

                        bookList = response.body().getItems();

                        if (bookList.size() < 10)
                        {
                            forwardButton.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            forwardButton.setVisibility(View.VISIBLE);
                        }

                        adapter = new SearchRecyclerViewAdapter(bookList, getApplicationContext(), SearchActivity.this);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        linearLayoutManager.setReverseLayout(false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setVerticalScrollBarEnabled(false);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(adapter);

                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    else
                    {
                        actualPage = 0;
                        resultsCount = 0;
                        handlePagination();
                        recyclerView.setAdapter(null);

                        hintLinearLayout.setVisibility(View.INVISIBLE);
                        emptySearchResultTextView.setVisibility(View.VISIBLE);

                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    //makeToast(bookList.get(0).getVolumeInfo().getDescription());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();

                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public void makeToast(String toastMessage)
    {
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }

    private void handleRecyclerViewMargin()
    {
        if (resultsCount < 11)
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.recycler_view, ConstraintSet.BOTTOM, R.id.bottom_navigation_view, ConstraintSet.TOP, 0);
            constraintSet.applyTo(constraintLayout);
        }
        else
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.recycler_view, ConstraintSet.BOTTOM, R.id.back_button, ConstraintSet.TOP, 16);
            constraintSet.applyTo(constraintLayout);
        }
    }

    private void handlePagination()
    {
        if (resultsCount > 10)
        {
            if (actualPage == 1)
            {
                backButton.setVisibility(View.INVISIBLE);
                pageNumberTextView.setVisibility(View.VISIBLE);
                pageNumberTextView.setText(actualPage + " strona");
                forwardButton.setVisibility(View.VISIBLE);
            }
            else
            {
                backButton.setVisibility(View.VISIBLE);
                pageNumberTextView.setVisibility(View.VISIBLE);
                pageNumberTextView.setText(actualPage + " strona");
                forwardButton.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            backButton.setVisibility(View.INVISIBLE);
            pageNumberTextView.setVisibility(View.INVISIBLE);
            forwardButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        // refresh the realm instance
        RealmController.with(this).refresh();

        handlePagination();
        emptySearchResultTextView.setVisibility(View.INVISIBLE);
        setUpBottomNavigationViewEx();

        //RealmController.with(this).clearAll();
        Toast.makeText(this, "ilość książek w bazie = " + RealmController.getInstance().getBooks().size(), Toast.LENGTH_LONG).show();
    }
}
