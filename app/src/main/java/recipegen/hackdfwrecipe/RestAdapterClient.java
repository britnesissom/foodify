package recipegen.hackdfwrecipe;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by britne on 7/24/15.
 */
public class RestAdapterClient {

    private static RetrofitApi REST_CLIENT;
    private static String ROOT = "http://www.recipepuppy.com/api";

    static {
        setupRestClient();
    }

    private RestAdapterClient() {}

    public static RetrofitApi getRestClient() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(RetrofitApi.class);
    }
}
