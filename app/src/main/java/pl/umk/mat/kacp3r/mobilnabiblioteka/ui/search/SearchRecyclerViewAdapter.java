package pl.umk.mat.kacp3r.mobilnabiblioteka.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import pl.umk.mat.kacp3r.mobilnabiblioteka.R;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.Item;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.VolumeInfo;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Authors;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Book;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.Categories;
import pl.umk.mat.kacp3r.mobilnabiblioteka.model.IndustryIdentifier;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.RealmController;
import pl.umk.mat.kacp3r.mobilnabiblioteka.realm.adapters.RealmRecyclerViewAdapter;
import pl.umk.mat.kacp3r.mobilnabiblioteka.ui.book.AboutBookActivity;
import pl.umk.mat.kacp3r.mobilnabiblioteka.utils.AddBookDialogInRecyclerView;

public class SearchRecyclerViewAdapter extends RealmRecyclerViewAdapter<Book>
{
    private List<Item> bookList;
    private List<String> authorsList;
    private Context context;
    private Realm realm;
    private SearchActivity searchActivity;

    public SearchRecyclerViewAdapter(List<Item> bookList, Context context, SearchActivity searchActivity)
    {
        this.bookList = bookList;
        authorsList = new ArrayList<>();
        this.context = context;
        this.searchActivity = searchActivity;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searched_books_card_view_row, viewGroup, false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i)
    {
        realm = RealmController.getInstance().getRealm();
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        if (bookList.get(i).getVolumeInfo() != null)
        {
            VolumeInfo bookInfo = bookList.get(i).getVolumeInfo();
            authorsList = bookInfo.getAuthors();

            holder.title.setText(bookInfo.getTitle());

            if (bookList.get(i).getVolumeInfo().getAverageRating() != null)
            {
                holder.rate.setText(bookInfo.getAverageRating() + "/5");
            }
            else
            {
                holder.rate.setText("0/5");
            }

            if (authorsList != null)
            {
                String prefix = "";

                StringBuilder builder = new StringBuilder();
                for (String authors : authorsList)
                {
                    if (authorsList.toArray().length > 1)
                    {
                        builder.append(prefix);
                        prefix = ", ";
                        builder.append(authors);
                    }
                    else
                    {
                        builder.append(authors + "");
                    }
                }
                holder.authors.setText(builder.toString());
            }

            if (bookList.get(i).getVolumeInfo().getImageLinks() != null)
            {
                if (bookList.get(i).getVolumeInfo().getImageLinks().getThumbnail() != null
                        || !bookList.get(i).getVolumeInfo().getImageLinks().getThumbnail().isEmpty())
                {
                    Picasso.with(context.getApplicationContext())
                            .load(bookInfo.getImageLinks().getThumbnail())
                            .fit()
                            .noFade()
                            .centerCrop()
                            .placeholder(R.drawable.books_placeholder)
                            .error(R.drawable.books_placeholder)
                            .into(holder.cover);
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
                            .into(holder.cover);
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
                        .into(holder.cover);
            }
        }

        holder.add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Book book = realm.where(Book.class).contains("googleBookId",
                        bookList.get(i).getId()).findFirst();

                if (book == null)
                {
                    AddBookDialogInRecyclerView addBookDialogInRecyclerView = new AddBookDialogInRecyclerView();
                    addBookDialogInRecyclerView.showDialog(bookList, context, searchActivity, realm, "Dodaj książkę do biblioteki", i);
                }
                else
                {
                    Toast.makeText(view.getContext(), "Książka jest już w bazie danych", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent library = new Intent(context, AboutBookActivity.class);
                library.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                library.putExtra("isInLibrary", RealmController.with(searchActivity).hasBookWithGoogleId(bookList.get(i).getId()));
                library.putExtra("id", bookList.get(i).getId());
                context.startActivity(library);
            }
        });
    }

    public void addBookToDatabase(Realm realm, int shelf, int position)
    {
        Book book = new Book();
        Authors authors;
        RealmList<Authors> authorsList = new RealmList<>();
        Categories categories;
        RealmList<Categories> categoriesList = new RealmList<>();
        IndustryIdentifier industryIdentifier;
        RealmList<IndustryIdentifier> industryIdentifiersList = new RealmList<>();

        book.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());

        book.setGoogleBookId(bookList.get(position).getId());

        if (bookList.get(position).getVolumeInfo().getTitle() != null)
        {
            book.setTitle(bookList.get(position).getVolumeInfo().getTitle());
        }
        else
        {
            book.setTitle("Brak info o tytule");
        }

        if (bookList.get(position).getVolumeInfo().getDescription() != null)
        {
            book.setDescription(bookList.get(position).getVolumeInfo().getDescription());
        }
        else
        {
            book.setDescription("Brak opisu");
        }

        if (bookList.get(position).getVolumeInfo().getPageCount() != null)
        {
            if (shelf == 3)
            {
                book.setReadedPageCount(bookList.get(position).getVolumeInfo().getPageCount());
                book.setPageCount(bookList.get(position).getVolumeInfo().getPageCount());
            }
            else
            {
                book.setReadedPageCount(0);
                book.setPageCount(bookList.get(position).getVolumeInfo().getPageCount());
            }
        }
        else
        {
            book.setPageCount(0);
        }

        if (bookList.get(position).getVolumeInfo().getAverageRating() != null)
        {
            book.setAverageRating(bookList.get(position).getVolumeInfo().getAverageRating());
        }
        else
        {
            book.setAverageRating(0.0);
        }

        if (bookList.get(position).getVolumeInfo().getRatingsCount() != null)
        {
            book.setRatingsCount(bookList.get(position).getVolumeInfo().getRatingsCount());
        }
        else
        {
            book.setRatingsCount(0);
        }

        if (bookList.get(position).getVolumeInfo().getMaturityRating() != null)
        {
            book.setMaturityRating(bookList.get(position).getVolumeInfo().getMaturityRating());
        }
        else
        {
            book.setMaturityRating("NO_INFO");
        }

        if (bookList.get(position).getVolumeInfo().getPublisher() != null)
        {
            book.setPublisher(bookList.get(position).getVolumeInfo().getPublisher());
        }
        else
        {
            book.setPublisher("Brak info o wydawcy");
        }

        if (bookList.get(position).getVolumeInfo().getPublishedDate() != null)
        {
            book.setPublishedDate(bookList.get(position).getVolumeInfo().getPublishedDate());
        }
        else
        {
            book.setPublishedDate("NO_DATE");
        }

        if (bookList.get(position).getVolumeInfo().getAuthors() != null)
        {
            for (int j = 0; j < bookList.get(position).getVolumeInfo().getAuthors().size(); j++)
            {
                authors = new Authors();
                authors.setAuthor(bookList.get(position).getVolumeInfo().getAuthors().get(j));
                authorsList.add(j, authors);
            }
            book.setAuthors(authorsList);
        }
        else
        {
            authors = new Authors();
            authors.setAuthor("Brak info o autorach");
            authorsList.add(0, authors);

            book.setAuthors(authorsList);
        }

        if (bookList.get(position).getVolumeInfo().getCategories() != null)
        {
            for (int k = 0; k < bookList.get(position).getVolumeInfo().getCategories().size(); k++)
            {
                categories = new Categories();
                categories.setCategory(bookList.get(position).getVolumeInfo().getCategories().get(k));
                categoriesList.add(k, categories);
            }
            book.setCategories(categoriesList);
        }
        else
        {
            categories = new Categories();
            categories.setCategory("Brak kategorii");
            categoriesList.add(0, categories);

            book.setCategories(categoriesList);
        }

        if (bookList.get(position).getVolumeInfo().getImageLinks() != null)
        {
            if (bookList.get(position).getVolumeInfo().getImageLinks().getSmallThumbnail() != null)
            {
                book.setSmallThumbnail(bookList.get(position).getVolumeInfo().getImageLinks().getSmallThumbnail());
            }

            if (bookList.get(position).getVolumeInfo().getImageLinks().getThumbnail() != null)
            {
                book.setThumbnail(bookList.get(position).getVolumeInfo().getImageLinks().getThumbnail());
            }
        }

        if (bookList.get(position).getVolumeInfo().getIndustryIdentifiers() != null)
        {
            for (int l = 0; l < bookList.get(position).getVolumeInfo().getIndustryIdentifiers().size(); l++)
            {
                industryIdentifier = new IndustryIdentifier();
                industryIdentifier.setType(bookList.get(position).getVolumeInfo().getIndustryIdentifiers().get(l).getType());
                industryIdentifier.setIdentifier(bookList.get(position).getVolumeInfo().getIndustryIdentifiers().get(l).getIdentifier());
                industryIdentifiersList.add(l, industryIdentifier);
            }
            book.setIndustryIdentifiers(industryIdentifiersList);
        }
        else
        {
            industryIdentifier = new IndustryIdentifier();
            industryIdentifier.setType("-");
            industryIdentifier.setIdentifier("-");
            industryIdentifiersList.add(0, industryIdentifier);

            book.setIndustryIdentifiers(industryIdentifiersList);
        }

        book.setShelf(shelf);

        realm.beginTransaction();
        realm.copyToRealm(book);
        realm.commitTransaction();
    }

    @Override
    public int getItemCount()
    {
        return bookList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        public CardView card;
        public ImageView cover;
        public TextView title;
        public TextView authors;
        public TextView rate;
        public Button add;
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
            rate = (TextView) itemView.findViewById(R.id.rate_text_view);
            add = (Button) itemView.findViewById(R.id.add_button);
        }
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
