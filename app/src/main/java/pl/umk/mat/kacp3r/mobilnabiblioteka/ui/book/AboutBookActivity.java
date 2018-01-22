package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import pl.umk.mat.kacp3r.mobilnabiblioteka.MobilnaBiblioteka;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.RestApi;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.VolumeResponse;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Authors;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AboutBookActivity extends AppCompatActivity
{
    private static final String TAG = "AboutBookActivity";

    @Inject Retrofit retrofit;
    private Realm realm;

    @BindView(R.id.maturity_rating_image_view) ImageView maturityRatingImageView;
    @BindView(R.id.back_top_image_button) ImageButton backTopImageButton;
    @BindView(R.id.title_text_view) TextView titleTextView;
    @BindView(R.id.thumbnail) ImageView thumbnailImageView;
    @BindView(R.id.authors_text_view) TextView authorsTextView;
    @BindView(R.id.rating) RatingBar ratingBar;
    @BindView(R.id.ratings_count_text_view) TextView ratingsCountTextView;
    @BindView(R.id.publish_info_text_view) TextView publishInfoTextView;
    @BindView(R.id.description_webview) WebView descriptionWebView;
    @BindView(R.id.publisher_text_view) TextView publisherTextView;
    @BindView(R.id.isbn_text_view) TextView isbnTextView;
    @BindView(R.id.page_count_text_view) TextView pageCountTextView;

    @OnClick(R.id.back_top_image_button)
    public void onBackTopImageButtonClick()
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    private void makeToast(String toastMessage)
    {
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }

    private void bookGoogleIdRequestWithRetrofit(final String id)
    {
        ((MobilnaBiblioteka) getApplication()).getNetComponent().inject(this);
        RestApi service = retrofit.create(RestApi.class);
        Call<VolumeResponse> call = service.getBookInfo(id);

        call.enqueue(new Callback<VolumeResponse>()
        {
            @Override
            public void onResponse(Call<VolumeResponse> call, Response<VolumeResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        if (response.body().getVolumeInfo() != null)
                        {
                            String maturityRating;
                            String title;
                            List<String> authors = null;
                            String thumbnailUrl = null;
                            String rate;
                            int ratingsCount;
                            String publisher;
                            String publishedDate;
                            String description;
                            List<String> isbnList;
                            int pageCount;

                            if (response.body().getVolumeInfo().getMaturityRating() != null)
                            {
                                maturityRating = response.body().getVolumeInfo().getMaturityRating();
                            }
                            else
                            {
                                maturityRating = "NO_INFO";
                            }

                            if (response.body().getVolumeInfo().getTitle() != null)
                            {
                                title = response.body().getVolumeInfo().getTitle();
                            }
                            else
                            {
                                title = "Brak info o tytule";
                            }

                            if (response.body().getVolumeInfo().getAuthors() != null)
                            {
                                authors = response.body().getVolumeInfo().getAuthors();
                            }
                            else
                            {
                                authors.add(0, "Brak info o autorach");
                            }

                            if (response.body().getVolumeInfo().getImageLinks() != null)
                            {
                                if (response.body().getVolumeInfo().getImageLinks().getThumbnail() != null)
                                {
                                    thumbnailUrl = response.body().getVolumeInfo().getImageLinks().getThumbnail();
                                }
                            }

                            if (response.body().getVolumeInfo().getAverageRating() != null)
                            {
                                rate = response.body().getVolumeInfo().getAverageRating().toString();
                            }
                            else
                            {
                                rate = "0.0";
                            }

                            if (response.body().getVolumeInfo().getRatingsCount() != null)
                            {
                                ratingsCount = response.body().getVolumeInfo().getRatingsCount();
                            }
                            else
                            {
                                ratingsCount = 0;
                            }

                            if (response.body().getVolumeInfo().getPublisher() != null)
                            {
                                publisher = response.body().getVolumeInfo().getPublisher();
                            }
                            else
                            {
                                publisher = "Brak info o wydawcy";
                            }

                            if (response.body().getVolumeInfo().getPublishedDate() != null)
                            {
                                publishedDate = response.body().getVolumeInfo().getPublishedDate();
                            }
                            else
                            {
                                publishedDate = "";
                            }

                            if (response.body().getVolumeInfo().getDescription() != null)
                            {
                                description = response.body().getVolumeInfo().getDescription();
                            }
                            else
                            {
                                description = "Brak opisu";
                            }

                            if (response.body().getVolumeInfo().getIndustryIdentifiers() != null)
                            {
                                isbnList = new ArrayList<>();

                                for (int i = 0; i < response.body().getVolumeInfo().getIndustryIdentifiers().size(); i++)
                                {
                                    isbnList.add(i, response.body().getVolumeInfo().getIndustryIdentifiers().get(i).getIdentifier());
                                }
                            }
                            else
                            {
                                isbnList = new ArrayList<>();
                                isbnList.add(0, "-");
                            }

                            if (response.body().getVolumeInfo().getPageCount() != null)
                            {
                                pageCount = response.body().getVolumeInfo().getPageCount();
                            }
                            else
                            {
                                pageCount = 0;
                            }

                            setBookMainInformations(maturityRating,
                                    title,
                                    thumbnailUrl,
                                    authors,
                                    rate,
                                    ratingsCount,
                                    publisher,
                                    publishedDate);

                            setBookDescription(description);

                            setBibliographyInfo(publisher,
                                    isbnList,
                                    pageCount);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VolumeResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBookInfoFromDatabase(String id)
    {
        final Book book = RealmController.with(this).getBook(id);

        String maturityRating;
        String title;
        List<String> authors = null;
        String thumbnailUrl = null;
        String rate;
        int ratingsCount;
        String publisher;
        String publishedDate;
        String description;
        List<String> isbnList;
        int pageCount;

        maturityRating = book.getMaturityRating();

        title = book.getTitle();

        authors = new ArrayList<>();
        for (int i = 0; i < book.getAuthors().size(); i++)
        {
            authors.add(book.getAuthors().get(i).getAuthor());
        }

        thumbnailUrl = book.getThumbnail();

        rate = book.getAverageRating().toString();

        ratingsCount = book.getRatingsCount();

        publisher = book.getPublisher();

        publishedDate = book.getPublishedDate();

        description = book.getDescription();

        isbnList = new ArrayList<>();
        for (int i = 0; i < book.getIndustryIdentifiers().size(); i++)
        {
            isbnList.add(i, book.getIndustryIdentifiers().get(i).getIdentifier());
        }

        pageCount = book.getPageCount();

        setBookMainInformations(maturityRating,
                title,
                thumbnailUrl,
                authors,
                rate,
                ratingsCount,
                publisher,
                publishedDate);

        setBookDescription(description);

        setBibliographyInfo(publisher,
                isbnList,
                pageCount);
    }

    private void setBookMainInformations(String maturityRating,
                                         String title,
                                         String thumbnail,
                                         List<String> authorsList,
                                         String rate,
                                         int ratingsCount,
                                         String publisher,
                                         String publishedDate)
    {
        if (maturityRating.equals("MATURE"))
        {
            maturityRatingImageView.setImageResource(R.drawable.above_18);
        }
        else if (maturityRating.equals("NOT_MATURE"))
        {
            maturityRatingImageView.setImageResource(R.drawable.under_18);
        }
        else
        {
            maturityRatingImageView.setImageResource(0);
        }

        titleTextView.setText(title);

        if (thumbnail != null)
        {
            Picasso.with(this)
                    .load(thumbnail)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .placeholder(R.drawable.books_placeholder)
                    .error(R.drawable.books_placeholder)
                    .into(thumbnailImageView);
        }
        else
        {
            Picasso.with(this)
                    .load(R.drawable.books_placeholder)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .placeholder(R.drawable.books_placeholder)
                    .error(R.drawable.books_placeholder)
                    .into(thumbnailImageView);
        }

        if (!publishedDate.equals("NO_DATE"))
        {
            publishInfoTextView.setText(publisher + ", " + publishedDate);
        }
        else
        {
            publishInfoTextView.setText(publisher);
        }

        if (authorsList != null)
        {
            String prefix = "";

            StringBuilder builder = new StringBuilder();
            for (String authors : authorsList)
            {
                if (authorsList.size() > 1)
                {
                    builder.append(prefix);
                    prefix = ", ";
                    builder.append(authors);
                }
                else
                {
                    builder.append(authors + "");
                }
            }
            authorsTextView.setText(builder.toString());
        }

        ratingBar.setEnabled(true);
        ratingBar.setMax(5);
        ratingBar.setStepSize(0.01f);
        ratingBar.setRating(Float.parseFloat(rate));
        ratingBar.invalidate();

        ratingsCountTextView.setText(String.valueOf(ratingsCount));
    }

    private void setBookDescription(String description)
    {
        String myData = String.valueOf(Html.fromHtml("<![CDATA[<body style=\"text-align:justify;color:#747474;margin: 0; padding: 0 \">"
                + description
                + "</body>]]>"));

        descriptionWebView.getSettings();
        descriptionWebView.setBackgroundColor(Color.TRANSPARENT);
        descriptionWebView.loadData(myData, "text/html", "UTF-8");
    }

    private void setBibliographyInfo(String publisher,
                                     List<String> isbnList,
                                     int pageCount)
    {
        publisherTextView.setText(publisher);

        String prefix = "";

        StringBuilder builder = new StringBuilder();
        for (String isbn : isbnList)
        {
            if (isbnList.toArray().length > 1)
            {
                builder.append(prefix);
                prefix = ", ";
                builder.append(isbn);
            }
            else
            {
                builder.append(isbn + "");
            }
        }
        isbnTextView.setText(builder.toString());

        if (pageCount == 0)
        {
            pageCountTextView.setText("-");
        }
        else
        {
            pageCountTextView.setText(String.valueOf(pageCount));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_book);

        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        // refresh the realm instance
        RealmController.with(this).refresh();

        Boolean isInLibrary = getIntent().getExtras().getBoolean("isInLibrary");
        String id = getIntent().getExtras().getString("id");

        if (isInLibrary)
        {
            getBookInfoFromDatabase(id);
        }
        else
        {
            bookGoogleIdRequestWithRetrofit(id);
        }
    }
}
