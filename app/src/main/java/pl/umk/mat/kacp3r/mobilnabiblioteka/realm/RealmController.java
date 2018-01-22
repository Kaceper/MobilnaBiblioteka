package pl.umk.mat.kacp3r.mobilnabiblioteka.realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;

public class RealmController
{
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application)
    {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment)
    {
        if (instance == null)
        {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity)
    {
        if (instance == null)
        {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application)
    {
        if (instance == null)
        {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance()
    {
        return instance;
    }

    public Realm getRealm()
    {
        return realm;
    }

    // Refresh the realm istance
    public void refresh()
    {
        realm.refresh();
    }

    // Clear all objects frpm Book.class
    public void clearAll()
    {
        realm.beginTransaction();
        realm.clear(Book.class);
        realm.commitTransaction();
    }

    // Find all objects in the Book.class
    public RealmResults<Book> getBooks()
    {
        return realm.where(Book.class).findAll();
    }

    // Query a single item with the given id
    public Book getBook(String googleBookId)
    {
        return realm.where(Book.class).equalTo("googleBookId", googleBookId).findFirst();
    }

    // Check if Book.class is empty
    public boolean hasBooks()
    {
        return !realm.allObjects(Book.class).isEmpty();
    }

    public boolean hasBookWithGoogleId(String googleBookId)
    {
        Book results = realm.where(Book.class)
                .equalTo("googleBookId", googleBookId)
                .findFirst();

        return results != null;
    }

    // Query example
    public RealmResults<Book> booksToRead()
    {
        return realm.where(Book.class)
                .equalTo("shelf", 1)
                //.contains("isbn", "1111")
                //.or()
                //.contains("title", "Harry Potter")
                .findAll();

    }

    public RealmResults<Book> progressBooks()
    {
        return realm.where(Book.class)
                .equalTo("shelf", 2)
                .findAll();
    }

    public int numberOfFinishedBooks()
    {
        RealmResults<Book> results =  realm.where(Book.class)
                .equalTo("shelf", 3)
                .findAll();

        return results.sum("id").intValue();
    }

    public RealmResults<Book> finishedBooks()
    {
        return realm.where(Book.class)
                .equalTo("shelf", 3)
                .findAll();
    }

    public int readedPagesCount()
    {
        RealmResults<Book> results = realm.where(Book.class).findAll();

        int sum = results.sum("readedPageCount").intValue();

        return sum;
    }
}
