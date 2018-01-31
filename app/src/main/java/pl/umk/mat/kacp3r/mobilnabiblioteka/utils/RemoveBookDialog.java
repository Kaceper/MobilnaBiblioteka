package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
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

    public void showDialog(final String googleBookId,
                           final LibraryActivity libraryActivity,
                           final RecyclerView.Adapter adapter,
                           final Book book,
                           final Realm realm,
                           String msg,
                           final int position)
    {
        books = RealmController.with(libraryActivity).booksToRead();

        final Dialog dialog = new Dialog(libraryActivity);
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
                RealmResults<Book> results = realm.where(Book.class).equalTo("googleBookId", googleBookId).findAll();

                realm.beginTransaction();
                results.remove(0);
                realm.commitTransaction();

                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                libraryActivity.makeToast("Książka została usunięta z bazy danych ");

                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
