package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.adapters.RealmBooksAdapter;

public class FinishedFragment extends Fragment
{
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.number_of_elements_text_view) TextView numberOfElementsTextView;
    @BindView(R.id.sort_image_button)
    ImageButton sortImageButton;
    @BindView(R.id.constraint_layout) ConstraintLayout constraintLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_finished_books_list_textView) TextView emptyFinishedBooksListTextView;

    private Realm realm;
    private FinishedRecyclerViewAdapter adapter;
    private boolean sortAsc = true;

    @OnClick(R.id.sort_image_button)
    public void onImageButtonClick()
    {
        if (sortImageButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.sort_icon_asc).getConstantState()))
        {
            sortAsc = true;
            setRealmAdapter(RealmController.with(this.getActivity()).finishedBooks(), sortAsc);
        }
        else
        {
            sortAsc = false;
            setRealmAdapter(RealmController.with(this.getActivity()).finishedBooks(), sortAsc);
        }
        setSortIcon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.finished_fragment, container, false);
        ButterKnife.bind(this, v);
        ButterKnife.setDebug(true);

        this.realm = RealmController.with(this.getActivity()).getRealm();

        adapter = new FinishedRecyclerViewAdapter(this.getActivity(), (LibraryActivity)getActivity(), FinishedFragment.this);

        handleFinishedFragmentRecyclerView();
        setNumberOfElementsTextView();
        setSortIcon();

        return v;
    }

    public void setNumberOfElementsTextView()
    {
        numberOfElementsTextView.setText("Liczba elementów (" + adapter.getItemCount() + ")");
    }

    public void handleFinishedFragmentRecyclerView()
    {
        if (RealmController.with(this.getActivity()).finishedBooks().size() > 0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            emptyFinishedBooksListTextView.setVisibility(View.INVISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
            linearLayoutManager.setReverseLayout(false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVerticalScrollBarEnabled(false);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            RealmController.with(this.getActivity()).refresh();

            setRealmAdapter(RealmController.with(this.getActivity()).finishedBooks(), sortAsc);
        }
        else
        {
            linearLayout.setVisibility(View.INVISIBLE);
            emptyFinishedBooksListTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setSortIcon()
    {
        if (sortAsc)
        {
            sortImageButton.setImageResource(R.drawable.sort_icon_desc);
        }
        else
        {
            sortImageButton.setImageResource(R.drawable.sort_icon_asc);
        }
    }

    public void setRealmAdapter(RealmResults<Book> books, boolean sortAsc)
    {
        if (sortAsc)
        {
            books.sort("title", Sort.ASCENDING);
        }
        else
        {
            books.sort("title", Sort.DESCENDING);
        }

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

