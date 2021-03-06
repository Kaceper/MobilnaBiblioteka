package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import pl.umk.mat.kacp3r.mobilnabiblioteka.MobilnaBiblioteka;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.RestApi;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.VolumeResponse;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.Item;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.SearchResponse;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Authors;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Categories;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.IndustryIdentifier;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.AddBookDialogInActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.MarkAsReadDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AboutBookActivity extends AppCompatActivity
{
    private static final String TAG = "AboutBookActivity";

    private String googleBookId;
    private Boolean isInLibrary;
    private AuthorsBooksRecyclerViewAdapter authorsBooksRecyclerViewAdapter;
    private List<Item> bookList;

    @Inject Retrofit retrofit;
    private Realm realm;

    @BindView(R.id.constraint_layout) ConstraintLayout constraintLayout;
    @BindView(R.id.left_image_button) ImageButton leftImageButton;
    @BindView(R.id.seek_bar_value) TextView seekBarValueTextView;
    @BindView(R.id.seek_bar) DiscreteSeekBar seekBar;
    @BindView(R.id.right_image_button) ImageButton rightImageButton;
    @BindView(R.id.maturity_rating_image_view) ImageView maturityRatingImageView;
    @BindView(R.id.back_top_image_button) ImageButton backTopImageButton;
    @BindView(R.id.add_book_to_library_image_button) ImageButton addBookToLibraryImageButton;
    @BindView(R.id.title_text_view) TextView titleTextView;
    @BindView(R.id.author_icon) ImageView authorImageView;
    @BindView(R.id.author_books_title_text_view) TextView authorBooksTitleTextView;
    @BindView(R.id.thumbnail) ImageView thumbnailImageView;
    @BindView(R.id.progress_bar) MaterialProgressBar progressBar;
    @BindView(R.id.mark_as_read_image_button) ImageButton markAsReadImageButton;
    @BindView(R.id.authors_text_view) TextView authorsTextView;
    @BindView(R.id.rating) RatingBar ratingBar;
    @BindView(R.id.ratings_count_text_view) TextView ratingsCountTextView;
    @BindView(R.id.publish_info_text_view) TextView publishInfoTextView;
    @BindView(R.id.description_webview) WebView descriptionWebView;
    @BindView(R.id.recycler_view_author_books) RecyclerView recyclerViewAuthorBooks;
    @BindView(R.id.categories_text_view) TextView categoriesTextView;
    @BindView(R.id.publisher_text_view) TextView publisherTextView;
    @BindView(R.id.isbn_text_view) TextView isbnTextView;
    @BindView(R.id.page_count_text_view) TextView pageCountTextView;

    @OnClick(R.id.back_top_image_button)
    public void onBackTopImageButtonClick()
    {
        onBackPressed();
    }

    @OnClick(R.id.add_book_to_library_image_button)
    public void setAddBookToLibraryImageButtonClick()
    {
        AddBookDialogInActivity addBookDialogInActivity = new AddBookDialogInActivity();
        addBookDialogInActivity.showDialog(AboutBookActivity.this, "Dodaj książkę do biblioteki", googleBookId);
    }

    @OnClick(R.id.mark_as_read_image_button)
    public void markAsReadImageButtonClick()
    {
         MarkAsReadDialog markAsReadDialog = new MarkAsReadDialog();
         markAsReadDialog.showDialog(AboutBookActivity.this,"Oznaczyć książkę jako przeczytaną?", googleBookId);
    }

    @OnClick(R.id.left_image_button)
    public void onLeftImageButtonClick()
    {
        final int[] readedPageCount = new int[1];
        final int[] pageCount = new int[1];

        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute (Realm realm)
            {
                Book book = realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst();
                if(book != null)
                {
                    if (book.getReadedPageCount() < 1)
                    {
                        book.setReadedPageCount(0);
                        readedPageCount[0] = 0;
                        pageCount[0] = seekBar.getMax();
                    }
                    else
                    {
                        book.setReadedPageCount(book.getReadedPageCount() - 1);
                        readedPageCount[0] = book.getReadedPageCount();
                        pageCount[0] = seekBar.getMax();
                    }
                }
            }
        });
        setSeekBarValueTextView(readedPageCount[0], pageCount[0]);
        setProgressBarProgress(readedPageCount[0], pageCount[0]);
        seekBar.setProgress(readedPageCount[0]);
        checkForSeekBarValue(readedPageCount[0], pageCount[0]);
    }

    @OnClick(R.id.right_image_button)
    public void onRightImageButtonClick()
    {
        final int[] readedPageCount = new int[1];
        final int[] pageCount = new int[1];

        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute (Realm realm)
            {
                Book book = realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst();
                if(book != null)
                {
                    if (book.getReadedPageCount() == (book.getPageCount() - 1))
                    {
                        book.setReadedPageCount(book.getPageCount());
                    }
                    else
                    {
                        book.setReadedPageCount(book.getReadedPageCount() + 1);
                        readedPageCount[0] = book.getReadedPageCount();
                        pageCount[0] = seekBar.getMax();
                    }
                }
            }
        });
        setSeekBarValueTextView(readedPageCount[0], pageCount[0]);
        setProgressBarProgress(readedPageCount[0], pageCount[0]);
        seekBar.setProgress(readedPageCount[0]);
        checkForSeekBarValue(readedPageCount[0], pageCount[0]);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void makeToast(String toastMessage)
    {
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }

    public void getAuthorOtherBooksWithetrofit(final String author, final String title)
    {
        ((MobilnaBiblioteka) getApplication()).getNetComponent().inject(this);
        RestApi service = retrofit.create(RestApi.class);
        Call<SearchResponse> call = service.getAuthorBooks("https://www.googleapis.com/books/v1/volumes?q=-"+ "\"" + title  + "\"" + "+" + "inauthor:" + author + "&maxResults=30");

        call.enqueue(new Callback<SearchResponse>()
        {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response)
            {
                bookList = new ArrayList<>();

                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        if (response.body().getItems() != null)
                        {
                            authorImageView.setVisibility(View.VISIBLE);
                            authorBooksTitleTextView.setVisibility(View.VISIBLE);
                            recyclerViewAuthorBooks.setVisibility(View.VISIBLE);

                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(constraintLayout);
                            constraintSet.connect(R.id.bibliography_info_text_view, ConstraintSet.TOP, R.id.recycler_view_author_books, ConstraintSet.BOTTOM, 16);
                            constraintSet.applyTo(constraintLayout);

                            if (response.body().getItems().size() > 0)
                            {
                                bookList = response.body().getItems();

                                authorsBooksRecyclerViewAdapter = new AuthorsBooksRecyclerViewAdapter(getApplicationContext(), AboutBookActivity.this,  bookList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                layoutManager.setReverseLayout(false);
                                recyclerViewAuthorBooks.setLayoutManager(layoutManager);
                                recyclerViewAuthorBooks.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewAuthorBooks.setHorizontalScrollBarEnabled(false);
                                recyclerViewAuthorBooks.setHasFixedSize(true);
                                recyclerViewAuthorBooks.setAdapter(authorsBooksRecyclerViewAdapter);
                            }
                        }
                        else
                        {
                            authorImageView.setVisibility(View.INVISIBLE);
                            authorBooksTitleTextView.setVisibility(View.INVISIBLE);
                            recyclerViewAuthorBooks.setVisibility(View.INVISIBLE);

                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(constraintLayout);
                            constraintSet.connect(R.id.bibliography_info_text_view, ConstraintSet.TOP, R.id.description_webview, ConstraintSet.BOTTOM, 16);
                            constraintSet.applyTo(constraintLayout);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t)
            {

            }
        });
    }

    public void bookGoogleIdRequestWithRetrofit(final String id, final Boolean isInLibrary, final int shelf)
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
                            List<String> authors;
                            List<String> categories;
                            String thumbnailUrl = null;
                            String rate;
                            int ratingsCount;
                            String publisher;
                            String publishedDate;
                            String description;
                            List<String> isbnList;
                            int pageCount;
                            int readedPageCount;
                            pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.ImageLinks imageLinks = null;
                            List<pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.IndustryIdentifier> industryIdentifiers = null;

                            if (response.body().getVolumeInfo().getMaturityRating() != null)
                            {
                                maturityRating = response.body().getVolumeInfo().getMaturityRating();
                            }
                            else
                            {
                                maturityRating = "NO_INFO";
                            }

                            String title;
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
                                authors = new ArrayList<>();
                                authors.add(0, "Brak info o autorach");
                            }

                            if (response.body().getVolumeInfo().getCategories() != null)
                            {
                                categories = response.body().getVolumeInfo().getCategories();
                            }
                            else
                            {
                                categories = new ArrayList<>();
                                categories.add(0, "Brak kategorii");
                            }

                            if (response.body().getVolumeInfo().getImageLinks() != null)
                            {
                                imageLinks = response.body().getVolumeInfo().getImageLinks();

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
                                industryIdentifiers = response.body().getVolumeInfo().getIndustryIdentifiers();

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

                            if (shelf == 3)
                            {
                                readedPageCount = pageCount;
                            }
                            else
                            {
                                readedPageCount = 0;
                            }

                            if (isInLibrary && shelf != 0)
                            {
                                addBookToDatabase(realm,
                                        shelf,
                                        title,
                                        description,
                                        readedPageCount,
                                        pageCount,
                                        rate,
                                        ratingsCount,
                                        maturityRating,
                                        publisher,
                                        publishedDate,
                                        authors,
                                        categories,
                                        imageLinks,
                                        industryIdentifiers,
                                        isInLibrary);
                            }
                            else
                            {
                                setBookMainInformations(maturityRating,
                                        title,
                                        thumbnailUrl,
                                        readedPageCount,
                                        pageCount,
                                        authors,
                                        rate,
                                        ratingsCount,
                                        publisher,
                                        publishedDate,
                                        isInLibrary);

                                setBookDescription(description);

                                getAuthorOtherBooksWithetrofit(authors.get(0), title);

                                setBibliographyInfo(categories,
                                        publisher,
                                        isbnList,
                                        pageCount);

                            }
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

    public void addBookToDatabase(final Realm realm,
                                  int shelf,
                                  String title,
                                  String desciption,
                                  int readedPageCount,
                                  final int pageCount,
                                  String averageRating,
                                  int ratingsCount,
                                  String maturityRating,
                                  String publisher,
                                  String publishedDate,
                                  List<String> authorsList,
                                  List<String> categoriesList,
                                  pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.ImageLinks imageLinks,
                                  List<pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.IndustryIdentifier> industryIdentifierList,
                                  boolean isInLibrary)
    {
        Book book = new Book();
        Authors authors;
        RealmList<Authors> authorsRealmList = new RealmList<>();
        Categories categories;
        RealmList<Categories> categoriesRealmList = new RealmList<>();
        IndustryIdentifier industryIdentifier;
        RealmList<IndustryIdentifier> industryIdentifiersRealmList = new RealmList<>();

        book.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());

        book.setGoogleBookId(googleBookId);

        if (title != null)
        {
            book.setTitle(title);
        }
        else
        {
            book.setTitle("Brak info o tytule");
        }

        if (desciption != null)
        {
            book.setDescription(desciption);
        }
        else
        {
            book.setDescription("Brak opisu");
        }

        book.setPageCount(pageCount);

        book.setReadedPageCount(readedPageCount);

        if (averageRating != null)
        {
            book.setAverageRating(Double.valueOf(averageRating));
        }
        else
        {
            book.setAverageRating(0.0);
        }

        if (ratingsCount >= 0)
        {
            book.setRatingsCount(ratingsCount);
        }
        else
        {
            book.setRatingsCount(0);
        }

        if (maturityRating != null)
        {
            book.setMaturityRating(maturityRating);
        }
        else
        {
            book.setMaturityRating("NO_INFO");
        }

        if (publisher != null)
        {
            book.setPublisher(publisher);
        }
        else
        {
            book.setPublisher("Brak info o wydawcy");
        }

        if (publishedDate != null)
        {
            book.setPublishedDate(publishedDate);
        }
        else
        {
            book.setPublishedDate("NO_DATE");
        }

        if (authorsList != null)
        {
            for (int j = 0; j < authorsList.size(); j++)
            {
                authors = new Authors();
                authors.setAuthor(authorsList.get(j));
                authorsRealmList.add(j, authors);
            }
            book.setAuthors(authorsRealmList);
        }
        else
        {
            authors = new Authors();
            authors.setAuthor("Brak info o autorach");
            authorsRealmList.add(0, authors);

            book.setAuthors(authorsRealmList);
        }

        if (categoriesList != null)
        {
            for (int k = 0; k < categoriesList.size(); k++)
            {
                categories = new Categories();
                categories.setCategory(categoriesList.get(k));
                categoriesRealmList.add(k, categories);
            }
            book.setCategories(categoriesRealmList);
        }
        else
        {
            categories = new Categories();
            categories.setCategory("Brak kategorii");
            categoriesRealmList.add(0, categories);

            book.setCategories(categoriesRealmList);
        }

        if (imageLinks != null)
        {
            if (imageLinks.getSmallThumbnail() != null)
            {
                book.setSmallThumbnail(imageLinks.getSmallThumbnail());
            }

            if (imageLinks.getThumbnail() != null)
            {
                book.setThumbnail(imageLinks.getThumbnail());
            }
        }

        if (industryIdentifierList != null)
        {
            for (int l = 0; l < industryIdentifierList.size(); l++)
            {
                industryIdentifier = new IndustryIdentifier();
                industryIdentifier.setType(industryIdentifierList.get(l).getType());
                industryIdentifier.setIdentifier(industryIdentifierList.get(l).getIdentifier());
                industryIdentifiersRealmList.add(l, industryIdentifier);
            }
            book.setIndustryIdentifiers(industryIdentifiersRealmList);
        }
        else
        {
            industryIdentifier = new IndustryIdentifier();
            industryIdentifier.setType("-");
            industryIdentifier.setIdentifier("-");
            industryIdentifiersRealmList.add(0, industryIdentifier);

            book.setIndustryIdentifiers(industryIdentifiersRealmList);
        }

        book.setShelf(shelf);

        realm.beginTransaction();
        realm.copyToRealm(book);
        realm.commitTransaction();

        addBookToLibraryImageButton.setVisibility(View.INVISIBLE);

        setProgressBarAndSeekbarVisiblility(true, pageCount, readedPageCount);
    }

    private void getBookInfoFromDatabase(String id, boolean isInLibrary)
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
        List<String> categoriesList;
        List<String> isbnList;
        int readedPageCount;
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

        categoriesList = new ArrayList<>();
        for (int i = 0; i < book.getCategories().size(); i++)
        {
            categoriesList.add(i, book.getCategories().get(i).getCategory());
        }

        publisher = book.getPublisher();

        publishedDate = book.getPublishedDate();

        description = book.getDescription();

        isbnList = new ArrayList<>();
        for (int i = 0; i < book.getIndustryIdentifiers().size(); i++)
        {
            isbnList.add(i, book.getIndustryIdentifiers().get(i).getIdentifier());
        }

        readedPageCount = book.getReadedPageCount();
        pageCount = book.getPageCount();

        handleDescriptionMargin();

        setBookMainInformations(maturityRating,
                title,
                thumbnailUrl,
                readedPageCount,
                pageCount,
                authors,
                rate,
                ratingsCount,
                publisher,
                publishedDate,
                isInLibrary);

        setBookDescription(description);

        getAuthorOtherBooksWithetrofit(authors.get(0), title);

        setBibliographyInfo(categoriesList,
                publisher,
                isbnList,
                pageCount);
    }

    private void setBookMainInformations(String maturityRating,
                                         String title,
                                         String thumbnail,
                                         final int readedPageCount,
                                         final int pageCount,
                                         List<String> authorsList,
                                         String rate,
                                         int ratingsCount,
                                         String publisher,
                                         String publishedDate,
                                         boolean isInLibrary)
    {
        setProgressBarAndSeekbarVisiblility(isInLibrary, pageCount, readedPageCount);

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

    private void setBibliographyInfo(List<String> categories,
                                     String publisher,
                                     List<String> isbnList,
                                     int pageCount)
    {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (String category : categories)
        {
            if (categories.toArray().length > 1)
            {
                builder.append(prefix);
                prefix = ", ";
                builder.append(category);
            }
            else
            {
                builder.append(category + "");
            }
        }
        categoriesTextView.setText(builder.toString());

        publisherTextView.setText(publisher);

        prefix = "";
        builder = new StringBuilder();
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

    private void setProgressBarAndSeekbarVisiblility(boolean isInLibrary,
                                                     final int pageCount,
                                                     int readedPageCount)
    {
        if (!isInLibrary)
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
        else
        {
            handleDescriptionMargin();

            progressBar.setVisibility(View.VISIBLE);

            progressBar.setMax(pageCount);
            setProgressBarProgress(readedPageCount, pageCount);

            setSeekBarValueTextView(readedPageCount, pageCount);

            seekBar.setMin(0);
            seekBar.setMax(pageCount);
            seekBar.setProgress(readedPageCount);
            seekBar.setIndicatorPopupEnabled(true);

            seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
            {
                @Override
                public void onProgressChanged(DiscreteSeekBar seekBar, final int value, boolean fromUser)
                {
                    realm.executeTransaction(new Realm.Transaction()
                    {
                        @Override
                        public void execute (Realm realm)
                        {
                            Book book = realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst();
                            if(book != null)
                            {
                                book.setReadedPageCount(value);
                            }
                        }
                    });
                    setSeekBarValueTextView(value, pageCount);
                    setProgressBarProgress(value, pageCount);
                    checkForSeekBarValue(value, pageCount);
                }

                @Override
                public void onStartTrackingTouch(DiscreteSeekBar seekBar)
                {

                }

                @Override
                public void onStopTrackingTouch(DiscreteSeekBar seekBar)
                {

                }
            });
        }
    }

    private void setSeekBarValueTextView(int readedPageCount, int pageCount)
    {
        seekBarValueTextView.setText(readedPageCount + "/" + pageCount);
    }

    private void setProgressBarProgress(int pageReaded, int pageCount)
    {
        if (pageReaded == pageCount)
        {
            progressBar.setProgress(pageReaded);

            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
        }
        else
        {
            progressBar.setProgress(pageReaded);

            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFD700")));
        }
    }

    private void checkForSeekBarValue(int pageReaded, int pageCount)
    {
        if (pageReaded == pageCount)
        {
            MarkAsReadDialog markAsReadDialog = new MarkAsReadDialog();
            markAsReadDialog.showDialog(AboutBookActivity.this, "Oznacz książkę jako przeczytaną", googleBookId);
        }
        else if (pageReaded > 0)
        {
            realm.executeTransaction(new Realm.Transaction()
            {
                @Override
                public void execute (Realm realm)
                {
                    Book book = realm.where(Book.class).equalTo("googleBookId", googleBookId).equalTo("shelf", 1).findFirst();
                    if(book != null)
                    {
                        book.setShelf(2);
                    }
                }
            });
        }
    }

    public void moveBookToFinishedShelf(final String googleBookId)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute (Realm realm)
            {
                Book book = realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst();
                if(book != null)
                {
                    book.setShelf(3);
                    book.setReadedPageCount(book.getPageCount());
                    setProgressBarProgress(book.getReadedPageCount(), book.getPageCount());
                }
            }
        });
        handleDescriptionMargin();
    }

    public void moveBackSeekBar()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute (Realm realm)
            {
                Book book = realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst();
                if(book != null)
                {
                    book.setReadedPageCount(seekBar.getMax() - 1);
                }
            }
        });

        seekBar.setProgress(seekBar.getMax() - 1);
        setProgressBarProgress(seekBar.getMax() - 1, seekBar.getMax());
        checkForSeekBarValue(seekBar.getMax() - 1, seekBar.getMax());
    }

    private void handleDescriptionMargin()
    {
        if (realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst() != null)
        {
            if (realm.where(Book.class).equalTo("googleBookId", googleBookId).equalTo("shelf", 3).findFirst() != null)
            {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.description_title_text_view, ConstraintSet.TOP, R.id.thumbnail, ConstraintSet.BOTTOM, 32);
                constraintSet.applyTo(constraintLayout);

                leftImageButton.setVisibility(View.INVISIBLE);
                seekBarValueTextView.setVisibility(View.INVISIBLE);
                seekBar.setVisibility(View.INVISIBLE);
                rightImageButton.setVisibility(View.INVISIBLE);
                markAsReadImageButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.description_title_text_view, ConstraintSet.TOP, R.id.seek_bar, ConstraintSet.BOTTOM, 32);
                constraintSet.applyTo(constraintLayout);

                leftImageButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                seekBarValueTextView.setVisibility(View.VISIBLE);
                rightImageButton.setVisibility(View.VISIBLE);
                markAsReadImageButton.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.description_title_text_view, ConstraintSet.TOP, R.id.thumbnail, ConstraintSet.BOTTOM, 32);
            constraintSet.applyTo(constraintLayout);

            leftImageButton.setVisibility(View.INVISIBLE);
            seekBarValueTextView.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
            rightImageButton.setVisibility(View.INVISIBLE);
            markAsReadImageButton.setVisibility(View.INVISIBLE);
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

        isInLibrary = getIntent().getExtras().getBoolean("isInLibrary");
        googleBookId = getIntent().getExtras().getString("id");

        if (isInLibrary)
        {
            getBookInfoFromDatabase(googleBookId, isInLibrary);
            addBookToLibraryImageButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            bookGoogleIdRequestWithRetrofit(googleBookId, false, 0);
            addBookToLibraryImageButton.setVisibility(View.VISIBLE);
        }
        handleDescriptionMargin();
    }
}
