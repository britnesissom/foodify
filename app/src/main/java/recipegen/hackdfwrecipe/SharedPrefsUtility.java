package recipegen.hackdfwrecipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import recipegen.hackdfwrecipe.models.Recipes;

/**
 * Created by britne on 6/13/15.
 */
public class SharedPrefsUtility {
    public static final String TAG = "SharedPrefsUtility";
    public static final String PREFS_NAME = "FOODIFY_APP";
    public static final String FAVORITES = "Foodify_Favorites";

    public SharedPrefsUtility() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Recipes> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public void addFavorite(Context context, Recipes recipes) {
        List<Recipes> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(recipes);
        saveFavorites(context, favorites);
        Log.d(TAG, "recipe added to favorites");
    }

    public void removeFavorite(Context context, Recipes recipes) {
        List<Recipes> favorites = getFavorites(context);

        if (favorites != null) {
            Log.d(TAG, "size before removal: " + favorites.size());

            //recipe is different object than r in favorites
            //so iterating through list is necessary to show that recipe exists
            //it is just a different object
            for (Iterator<Recipes> iterator = favorites.iterator(); iterator.hasNext();) {
                Recipes r = iterator.next();
                if (r.getTitle().equals(recipes.getTitle())) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();
                }
            }

            Log.d(TAG, "size after removal: " + favorites.size());
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Recipes> getFavorites(Context context) {
        SharedPreferences settings;
        List<Recipes> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Recipes[] favoriteItems = gson.fromJson(jsonFavorites,
                    Recipes[].class);

            favorites = Arrays.asList(favoriteItems);

            return new ArrayList<>(favorites);

        } else
            return new ArrayList<>();

    }

}
