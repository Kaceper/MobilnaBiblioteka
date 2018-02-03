package pl.umk.mat.kacp3r.mobilnabiblioteka.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
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
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.FinishedFragment;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.FinishedRecyclerViewAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.LibraryActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ProgressFragment;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ProgressRecyclerViewAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ToReadFragment;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library.ToReadRecyclerViewAdapter;

public class RemoveBookDialog
{
    public void showDialog(final String googleBookId,
                           final Activity activity,
                           final Fragment fragment,
                           final RecyclerView.Adapter adapter,
                           final Realm realm,
                           String msg,
                           final int position)
    {
        final Dialog dialog = new Dialog(activity);
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
                results.clear();
                realm.commitTransaction();

                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                updateFragment(fragment);
                updateActivity(activity);

                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void updateActivity(Activity activity)
    {
        if (activity instanceof LibraryActivity)
        {
            ((LibraryActivity) activity).setPageCountTextView();
            // ((LibraryActivity) activity).makeToast("Książka została usunięta z bazy danych ");
        }
    }

    private void updateFragment(Fragment fragment)
    {
        if (fragment instanceof  ToReadFragment)
        {
            ((ToReadFragment) fragment).setNumberOfElementsTextView();
            ((ToReadFragment) fragment).handleToReadFragmentRecyclerView();
        }

        if (fragment instanceof ProgressFragment)
        {
            ((ProgressFragment) fragment).setNumberOfElementsTextView();
            ((ProgressFragment) fragment).handleProgressFragmentRecyclerView();
        }

        if (fragment instanceof FinishedFragment)
        {
            ((FinishedFragment) fragment).setNumberOfElementsTextView();
            ((FinishedFragment) fragment).handleFinishedFragmentRecyclerView();
        }
    }
}
