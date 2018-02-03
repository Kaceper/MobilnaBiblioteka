package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Authors;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Categories;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.LibraryActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ToReadRecyclerViewAdapter;

public class EditBookDialogInRecyclerView
{
    private TextView dialogText;
    private AppCompatEditText titleEditText,
            thumbnailUrlEditText,
            descriptionEditText,
            publisherEditText,
            publishedDateEditText,
            pageCountEditText,
            authorsEditText,
            categoriesEditText,
            isbn10EditText,
            isbn13EditText;

    public void showDialog(final String googleBookId,
                           final Activity activity,
                           final RecyclerView.Adapter adapter,
                           final Book book,
                           final Realm realm,
                           String msg)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_book_in_library_custom_alert);

        dialogText = (TextView) dialog.findViewById(R.id.text_dialog);
        titleEditText = (AppCompatEditText)dialog.findViewById(R.id.title_edit_text);
        thumbnailUrlEditText = (AppCompatEditText)dialog.findViewById(R.id.thumbnail_edit_text);
        descriptionEditText = (AppCompatEditText)dialog.findViewById(R.id.description_edit_text);
        publisherEditText = (AppCompatEditText)dialog.findViewById(R.id.publisher_edit_text);
        publishedDateEditText = (AppCompatEditText)dialog.findViewById(R.id.published_date_edit_text);
        pageCountEditText = (AppCompatEditText)dialog.findViewById(R.id.page_count_edit_text);
        authorsEditText = (AppCompatEditText)dialog.findViewById(R.id.authors_edit_text);
        categoriesEditText = (AppCompatEditText)dialog.findViewById(R.id.categories_edit_text);
        isbn10EditText = (AppCompatEditText)dialog.findViewById(R.id.isbn_10_edit_text);
        isbn13EditText = (AppCompatEditText)dialog.findViewById(R.id.isbn_13_edit_text);

        dialogText.setText(msg);

        fillEditTexts(dialog, book);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        Button confirmButton = (Button) dialog.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RealmResults<Book> results = realm.where(Book.class).equalTo("googleBookId", googleBookId).findAll();

                realm.beginTransaction();
                results.get(0).setTitle(titleEditText.getText().toString());
                results.get(0).setSmallThumbnail(thumbnailUrlEditText.getText().toString());
                results.get(0).setThumbnail(thumbnailUrlEditText.getText().toString());
                results.get(0).setDescription(descriptionEditText.getText().toString());
                results.get(0).setPublisher(publisherEditText.getText().toString());
                results.get(0).setPublishedDate(publishedDateEditText.getText().toString());

                if (results.get(0).getPageCount() <= Integer.parseInt(pageCountEditText.getText().toString()))
                {
                    if (results.get(0).getShelf() == 3)
                    {
                        results.get(0).setPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                        results.get(0).setReadedPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                    }
                    else if (results.get(0).getShelf() == 2)
                    {
                        results.get(0).setPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                    }
                    else
                    {
                        results.get(0).setPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                    }
                }
                else
                {
                    if (results.get(0).getShelf() == 3)
                    {
                        results.get(0).setPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                        results.get(0).setReadedPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                    }
                    else if (results.get(0).getShelf() == 2)
                    {
                        results.get(0).setPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                        results.get(0).setReadedPageCount(Integer.parseInt(pageCountEditText.getText().toString()) - 1);
                    }
                    else
                    {
                        results.get(0).setPageCount(Integer.parseInt(pageCountEditText.getText().toString()));
                    }
                }

                String[] authorsWithoutCommas = (authorsEditText.getText().toString().split("\\s*, \\s*"));
                Authors authors = new Authors();
                results.get(0).getAuthors().clear();
                for (int j = 0; j < authorsWithoutCommas.length; j++)
                {
                    authors.setAuthor(authorsWithoutCommas[j]);
                    results.get(0).getAuthors().add(authors);
                }

                String[] categoriesWithoutCommas = (categoriesEditText.getText().toString().split("\\s*, \\s*"));
                Categories categories = new Categories();
                results.get(0).getCategories().clear();
                for (int k = 0; k < categoriesWithoutCommas.length; k++)
                {
                    categories.setCategory(categoriesWithoutCommas[k]);
                    results.get(0).getCategories().add(categories);
                }

                if (results.get(0).getIndustryIdentifiers() != null)
                {
                    for (int j = 0; j < results.get(0).getIndustryIdentifiers().size(); j++)
                    {
                        if (results.get(0).getIndustryIdentifiers().get(j).getType().equals("ISBN_10"))
                        {
                            results.get(0).getIndustryIdentifiers().get(j).setIdentifier(isbn10EditText.getText().toString());
                        }
                        else if (results.get(0).getIndustryIdentifiers().get(j).getType().equals("ISBN_13"))
                        {
                            results.get(0).getIndustryIdentifiers().get(j).setIdentifier(isbn13EditText.getText().toString());
                        }
                    }
                }

                realm.commitTransaction();

                adapter.notifyDataSetChanged();
                handleActivity(activity);
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void fillEditTexts(Dialog dialog, Book book)
    {
        titleEditText.setText(book.getTitle());

        thumbnailUrlEditText.setText(book.getThumbnail());

        descriptionEditText.setText(book.getDescription());

        publisherEditText.setText(book.getPublisher());

        publishedDateEditText.setText(book.getPublishedDate());

        pageCountEditText.setText(String.valueOf(book.getPageCount()));

        String prefix = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < book.getAuthors().size(); i++)
        {

            if (book.getAuthors().size() > 1)
            {
                builder.append(prefix);
                prefix = ", ";
                builder.append(book.getAuthors().get(i).getAuthor());
            }
            else
            {
                builder.append(book.getAuthors().get(i).getAuthor() + "");
            }
        }
        authorsEditText.setText(builder.toString());

        prefix = "";
        builder = new StringBuilder();
        for (int i = 0; i < book.getCategories().size(); i++)
        {
            if (book.getCategories().size() > 1)
            {
                builder.append(prefix);
                prefix = ", ";
                builder.append(book.getCategories().get(i).getCategory());
            }
            else
            {
                builder.append(book.getCategories().get(i).getCategory() + "");
            }
        }
        categoriesEditText.setText(builder.toString());

        if (book.getIndustryIdentifiers() != null)
        {
            for (int j = 0; j < book.getIndustryIdentifiers().size(); j++)
            {
                if (book.getIndustryIdentifiers().get(j).getType().equals("ISBN_10"))
                {
                    isbn10EditText.setText(book.getIndustryIdentifiers().get(j).getIdentifier());
                }
                else if (book.getIndustryIdentifiers().get(j).getType().equals("ISBN_13"))
                {
                    isbn13EditText.setText(book.getIndustryIdentifiers().get(j).getIdentifier());
                }
            }
        }
    }

    private void handleActivity(Activity activity)
    {
        if (activity instanceof  LibraryActivity)
        {
            ((LibraryActivity) activity).setPageCountTextView();
        }
    }
}
