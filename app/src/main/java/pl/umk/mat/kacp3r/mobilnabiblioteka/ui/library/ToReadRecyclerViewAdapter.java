package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.library;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Authors;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.adapters.RealmRecyclerViewAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book.AboutBookActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.EditBookDialogInRecyclerView;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.RemoveBookDialog;

public class ToReadRecyclerViewAdapter extends RealmRecyclerViewAdapter<Book>
{
    final Context context;
    private Realm realm;
    private LibraryActivity libraryActivity;
    ToReadFragment toReadFragment;

    public ToReadRecyclerViewAdapter(Context context, LibraryActivity libraryActivity)
    {
        this.context = context;
        this.libraryActivity = libraryActivity;
        toReadFragment = new ToReadFragment();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.library_item_card_view_row, viewGroup, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
    {
        realm = RealmController.getInstance().getRealm();

        final Book book = getItem(i);

        final CardViewHolder holder = (CardViewHolder) viewHolder;

        RealmList<Authors> authorsList;
        authorsList = book.getAuthors();

        holder.title.setText(book.getTitle());

        String prefix = "";
        StringBuilder builder = new StringBuilder();
        for (Authors authors : authorsList)
        {
            if (authorsList.toArray().length > 1)
            {
                builder.append(prefix);
                prefix = ", ";
                builder.append(authors.getAuthor());
            }
            else
            {
                builder.append(authors.getAuthor());
            }
        }
        holder.authors.setText(builder.toString());

        Picasso.with(context)
                .load(book.getSmallThumbnail())
                .fit()
                .noFade()
                .centerCrop()
                .into(holder.cover);

        holder.card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent library = new Intent(context, AboutBookActivity.class);
                library.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                library.putExtra("isInLibrary", RealmController.with(libraryActivity).hasBookWithGoogleId(book.getGoogleBookId()));
                library.putExtra("id", book.getGoogleBookId());
                context.startActivity(library);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RemoveBookDialog removeBookDialog = new RemoveBookDialog();
                removeBookDialog.showDialog(book.getGoogleBookId(), libraryActivity, ToReadRecyclerViewAdapter.this, book, realm, "Czy na pewno chcesz usunąć książkę?", i);

                libraryActivity.setPageCountTextView();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditBookDialogInRecyclerView editBookDialogInRecyclerView = new EditBookDialogInRecyclerView();
                editBookDialogInRecyclerView.showDialog(book.getGoogleBookId(), libraryActivity, ToReadRecyclerViewAdapter.this, book, realm,"Edycja książki");
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (getRealmAdapter() != null)
        {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder
    {
        public CardView card;
        public ImageView cover;
        public TextView title;
        public TextView authors;
        public ImageButton remove;
        public Button edit;
        //public ImageButton toReadAdd;
        //public ImageButton progressAdd;
        //public ImageButton finishAdd;

        public CardViewHolder(View itemView)
        {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_view);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
            authors = (TextView) itemView.findViewById(R.id.authors);
            remove = (ImageButton) itemView.findViewById(R.id.remove_image_button);
            edit = (Button) itemView.findViewById(R.id.edit_button);
            //toReadAdd = (ImageButton) itemView.findViewById(R.id.to_read_add);
            //progressAdd = (ImageButton) itemView.findViewById(R.id.progress_add);
            //finishAdd = (ImageButton) itemView.findViewById(R.id.finish_add);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
