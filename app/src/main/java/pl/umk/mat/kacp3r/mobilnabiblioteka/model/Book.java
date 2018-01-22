package pl.umk.mat.kacp3r.mobilnabiblioteka.model;

import android.support.annotation.Nullable;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Book extends RealmObject
{
    @PrimaryKey
    private long id;

    private String googleBookId;

    private String isbn;

    private String title;

    @Nullable
    private String description;

    private String publisher;

    private String publishedDate;

    private int readedPageCount;

    private int pageCount;

    private Double averageRating;

    private int ratingsCount;

    private String maturityRating;

    private RealmList<Authors> authors;

    private RealmList<Categories> categories;

    private RealmList<IndustryIdentifier> industryIdentifiers;

    @Nullable
    private String smallThumbnail;

    @Nullable
    private String thumbnail;

    private int shelf;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getGoogleBookId()
    {
        return googleBookId;
    }

    public void setGoogleBookId(String googleBookId)
    {
        this.googleBookId = googleBookId;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getReadedPageCount()
    {
        return readedPageCount;
    }

    public void setReadedPageCount(int readedPageCount)
    {
        this.readedPageCount = readedPageCount;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public Double getAverageRating()
    {
        return  averageRating;
    }

    public void setAverageRating(Double averageRating)
    {
        this.averageRating = averageRating;
    }

    public int getRatingsCount()
    {
        return  ratingsCount;
    }

    public void setRatingsCount(int ratingsCount)
    {
        this.ratingsCount = ratingsCount;
    }

    public String getMaturityRating() {
        return maturityRating;
    }

    public void setMaturityRating(String maturityRating) {
        this.maturityRating = maturityRating;
    }

    public RealmList<Authors> getAuthors()
    {
        return authors;
    }

    public void setAuthors(RealmList<Authors> authors)
    {
        this.authors = authors;
    }

    public RealmList<Categories> getCategories()
    {
        return categories;
    }

    public void setCategories(RealmList<Categories> categories)
    {
        this.categories = categories;
    }

    public RealmList<IndustryIdentifier> getIndustryIdentifiers()
    {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(RealmList<IndustryIdentifier> industryIdentifiers)
    {
        this.industryIdentifiers = industryIdentifiers;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getShelf()
    {
        return shelf;
    }

    public void setShelf(int shelf)
    {
        this.shelf = shelf;
    }
}

