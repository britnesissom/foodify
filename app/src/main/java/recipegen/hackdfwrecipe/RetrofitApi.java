package recipegen.hackdfwrecipe;

import recipegen.hackdfwrecipe.models.RecipePuppyResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by britne on 7/24/15.
 */
public interface RetrofitApi {

    @GET("/")
    void getRecipes(@Query("i") String ingredients, @Query("p") int page,
                    Callback<RecipePuppyResponse> callback);
}
