package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import io.realm.Realm;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.Item;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book.AboutBookActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search.SearchActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search.SearchRecyclerViewAdapter;

public class AddBookDialogInRecyclerView
{
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;

    public void showDialog(final List<Item> bookList,
                           final Context context,
                           final SearchActivity searchActivity,
                           final Realm realm,
                           String msg,
                           final int i)
    {
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(bookList, context,searchActivity);

        final Dialog dialog = new Dialog(searchActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_book_to_library_custom_alert);

        TextView dialogText = (TextView) dialog.findViewById(R.id.text_dialog);
        dialogText.setText(msg);

        Button toReadAddButton = (Button) dialog.findViewById(R.id.to_read_add_button);
        toReadAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchRecyclerViewAdapter.addBookToDatabase(realm, 1, i);

                dialog.dismiss();
            }
        });

        Button progressAddButton = (Button) dialog.findViewById(R.id.progress_add_button);
        progressAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchRecyclerViewAdapter.addBookToDatabase(realm, 2, i);

                dialog.dismiss();
            }
        });

        Button finishAddButton = (Button) dialog.findViewById(R.id.finish_add_button);
        finishAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchRecyclerViewAdapter.addBookToDatabase(realm, 3, i);

                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}