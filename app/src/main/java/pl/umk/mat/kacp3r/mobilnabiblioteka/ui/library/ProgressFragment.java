package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.adapters.RealmBooksAdapter;

public class ProgressFragment extends Fragment
{
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.number_of_elements_text_view) TextView numberOfElementsTextView;
    @BindView(R.id.sort_image_button)
    ImageButton sortImageButton;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_progress_books_list_textView) TextView emptyProgressBooksListTextView;

    private Realm realm;
    private ProgressRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.progress_fragment, container, false);
        ButterKnife.bind(this, v);
        ButterKnife.setDebug(true);

        this.realm = RealmController.with(this.getActivity()).getRealm();

        adapter = new ProgressRecyclerViewAdapter(this.getActivity(), (LibraryActivity)getActivity(),ProgressFragment.this);

        handleProgressFragmentRecyclerView();
        setNumberOfElementsTextView();

        return v;
    }

    public void setNumberOfElementsTextView()
    {
        numberOfElementsTextView.setText("Liczba elementów (" + adapter.getItemCount() + ")");
    }

    public void handleProgressFragmentRecyclerView()
    {
        if (RealmController.with(this.getActivity()).progressBooks().size() > 0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            emptyProgressBooksListTextView.setVisibility(View.INVISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
            linearLayoutManager.setReverseLayout(false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVerticalScrollBarEnabled(false);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            RealmController.with(this.getActivity()).refresh();
            setRealmAdapter(RealmController.with(this.getActivity()).progressBooks());
        }
        else
        {
            linearLayout.setVisibility(View.INVISIBLE);
            emptyProgressBooksListTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setRealmAdapter(RealmResults<Book> books)
    {
        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getActivity(), books, true);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    /**
     * Metoda onDestroyView().
     */
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
}
