package pl.umk.mat.kacp3r.mobilnabiblioteka.realm.adapters;

import android.content.Context;
import io.realm.RealmResults;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;

public class RealmBooksAdapter extends RealmModelAdapter<Book>
{
    public RealmBooksAdapter(Context context, RealmResults<Book> realmResults, boolean automaticUpdate)
    {
        super(context, realmResults, automaticUpdate);
    }
}
