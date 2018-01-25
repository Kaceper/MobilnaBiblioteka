package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.Item;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;

public class AuthorsBooksRecyclerViewAdapter extends RecyclerView.Adapter<AuthorsBooksRecyclerViewAdapter.ViewHolder>
{
    private List<Item> authorBooks;
    private static AuthorsBooksRecyclerViewAdapter.MyClickListener myClickListener;
    private Context context;
    private AboutBookActivity aboutBookActivity;

    public AuthorsBooksRecyclerViewAdapter(Context context, AboutBookActivity aboutBookActivity, List<Item> authorBooks)
    {
        this.context = context;
        this.aboutBookActivity = aboutBookActivity;
        this.authorBooks = authorBooks;
    }

    @Override
    public AuthorsBooksRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.author_book_card_view_row, viewGroup, false);
        return new AuthorsBooksRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthorsBooksRecyclerViewAdapter.ViewHolder viewHolder, final int i)
    {
        if (authorBooks.get(i).getVolumeInfo().getImageLinks() != null)
        {
            if (authorBooks.get(i).getVolumeInfo().getImageLinks().getThumbnail() != null
                    || !authorBooks.get(i).getVolumeInfo().getImageLinks().getThumbnail().isEmpty())
            {
                Picasso.with(context.getApplicationContext())
                        .load(authorBooks.get(i).getVolumeInfo().getImageLinks().getThumbnail())
                        .fit()
                        .noFade()
                        .centerCrop()
                        .placeholder(R.drawable.books_placeholder)
                        .error(R.drawable.books_placeholder)
                        .into(viewHolder.bookCover);
            }
            else
            {
                Picasso.with(context)
                        .load(R.drawable.books_placeholder)
                        .fit()
                        .noFade()
                        .centerCrop()
                        .placeholder(R.drawable.books_placeholder)
                        .error(R.drawable.books_placeholder)
                        .into(viewHolder.bookCover);
            }
        }
        else
        {
            Picasso.with(context)
                    .load(R.drawable.books_placeholder)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .placeholder(R.drawable.books_placeholder)
                    .error(R.drawable.books_placeholder)
                    .into(viewHolder.bookCover);
        }

        viewHolder.bookTitle.setText(authorBooks.get(i).getVolumeInfo().getTitle());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent library = new Intent(context, AboutBookActivity.class);
                library.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                library.putExtra("isInLibrary", RealmController.with(aboutBookActivity).hasBookWithGoogleId(authorBooks.get(i).getId()));
                library.putExtra("id", authorBooks.get(i).getId());
                context.startActivity(library);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return authorBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CardView cardView;
        private ImageView bookCover;
        private TextView bookTitle;

        public ViewHolder(View itemView)
        {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            bookCover = (ImageView) itemView.findViewById(R.id.book_cover);
            bookTitle = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(AuthorsBooksRecyclerViewAdapter.MyClickListener myClickListener)
    {
        this.myClickListener = myClickListener;
    }


    public interface MyClickListener
    {
        void onItemClick(int position, View v);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}