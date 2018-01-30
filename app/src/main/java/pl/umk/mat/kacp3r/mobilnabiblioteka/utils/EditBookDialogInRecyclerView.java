package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.LibraryActivity;

public class EditBookDialogInRecyclerView
{
    public void showDialog(final Context context,
                           final LibraryActivity libraryActivity,
                           final RealmBaseAdapter<Book> adapter,
                           final Book book,
                           final Realm realm,
                           String msg,
                           final int i)
    {
        final Dialog dialog = new Dialog(libraryActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_book_in_library_custom_alert);

        TextView dialogText = (TextView) dialog.findViewById(R.id.text_dialog);
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

        Button markAsReadButton = (Button) dialog.findViewById(R.id.confirm_button);
        markAsReadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                libraryActivity.makeToast("Książka została zedytowana");
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void fillEditTexts(Dialog dialog, Book book)
    {
        AppCompatEditText titleEditTex = (AppCompatEditText)dialog.findViewById(R.id.title_edit_text);
        titleEditTex.setText(book.getTitle());

        AppCompatEditText thumbnailUrlEditText = (AppCompatEditText)dialog.findViewById(R.id.thumbnail_edit_text);
        thumbnailUrlEditText.setText(book.getThumbnail());

        AppCompatEditText descriptionEditText = (AppCompatEditText)dialog.findViewById(R.id.description_edit_text);
        descriptionEditText.setText(book.getDescription());

        AppCompatEditText publisherEditText = (AppCompatEditText)dialog.findViewById(R.id.publisher_edit_text);
        publisherEditText.setText(book.getPublisher());

        AppCompatEditText publishedDateEditText = (AppCompatEditText)dialog.findViewById(R.id.published_date_edit_text);
        publishedDateEditText.setText(book.getPublishedDate());

        AppCompatEditText pageCountEditText = (AppCompatEditText)dialog.findViewById(R.id.page_count_edit_text);
        pageCountEditText.setText(String.valueOf(book.getPageCount()));

        AppCompatEditText authorsEditText = (AppCompatEditText)dialog.findViewById(R.id.authors_edit_text);
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

        AppCompatEditText categoriesEditText = (AppCompatEditText)dialog.findViewById(R.id.categories_edit_text);
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

        AppCompatEditText isbn10EditText = (AppCompatEditText)dialog.findViewById(R.id.isbn_10_edit_text);
        AppCompatEditText isbn13EditText = (AppCompatEditText)dialog.findViewById(R.id.isbn_13_edit_text);
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
}
