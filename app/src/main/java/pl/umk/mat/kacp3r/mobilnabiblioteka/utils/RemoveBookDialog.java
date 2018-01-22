package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import io.realm.RealmResults;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.FinishedRecyclerViewAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.LibraryActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ProgressRecyclerViewAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ToReadFragment;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ToReadRecyclerViewAdapter;

public class RemoveBookDialog
{
    private ToReadRecyclerViewAdapter toReadRecyclerViewAdapter;
    private ProgressRecyclerViewAdapter progressRecyclerViewAdapter;
    private FinishedRecyclerViewAdapter finishedRecyclerViewAdapter;
    private RealmResults<Book> books;

    public void showDialog(final Context context,
                           final LibraryActivity libraryActivity,
                           String msg,
                           final int shelf,
                           final String googleBookId,
                           final int i,
                           final int itemCount,
                           final RecyclerView recyclerView)
    {
        books = RealmController.with(libraryActivity).booksToRead();

        if (shelf == 1)
        {
            toReadRecyclerViewAdapter = new ToReadRecyclerViewAdapter(context, libraryActivity);
        }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.remove_book_from_library_custom_alert);

        TextView dialogText = (TextView) dialog.findViewById(R.id.text_dialog);
        dialogText.setText(msg);

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        Button removeButton = (Button) dialog.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (shelf == 1)
                {
                    toReadRecyclerViewAdapter = new ToReadRecyclerViewAdapter(context, libraryActivity);

                    /*
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(libraryActivity);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setVerticalScrollBarEnabled(false);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(toReadRecyclerViewAdapter);

                    RealmController.with(libraryActivity).refresh();
                    */
                    toReadRecyclerViewAdapter.removeBookFromDatabase(i);

                    /*
                    RealmBooksAdapter realmAdapter = new RealmBooksAdapter(libraryActivity, books, true);
                    toReadRecyclerViewAdapter.setRealmAdapter(realmAdapter);
                    toReadRecyclerViewAdapter.notifyDataSetChanged();
                    */
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
