package pl.umk.mat.kacp3r.mobilnabiblioteka;

import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about.VolumeResponse;
import pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.search.SearchResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi
{
    @GET("volumes")
    Call<SearchResponse> getBooks(@Query("q") String id,
                                  @Query("startIndex") int startIndex,
                                  @Query("maxResults") int maxResults,
                                  @Query("printType") String printType,
                                  @Query("key") String apiKey);

    @GET("volumes/{id}")
    Call<VolumeResponse> getBookInfo(@Path("id") String id);
}
