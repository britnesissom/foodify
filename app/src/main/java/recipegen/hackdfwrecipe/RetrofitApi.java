package recipegen.hackdfwrecipe;

import recipegen.hackdfwrecipe.models.Food2ForkResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by britne on 7/24/15.
 */
public interface RetrofitApi {

    @GET("/search")
    void getRecipes(@Query("key") String apiKey, @Query("q") String ingredients,
                    Callback<Food2ForkResponse> callback);
}
