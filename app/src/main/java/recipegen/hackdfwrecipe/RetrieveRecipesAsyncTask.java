package recipegen.hackdfwrecipe;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by britne on 6/16/15.
 */
public class RetrieveRecipesAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    private JSONObject jsonObject;
    private Context context;
    private ArrayList<String> ingredients;
    private OnRetrieveRecipesFinishedListener listener;

    //retrieves ingredients from IngredientChooserActivity
    public RetrieveRecipesAsyncTask(Context context, String[] list,
                                    OnRetrieveRecipesFinishedListener listener) {
        this.context = context;
        this.listener = listener;
        ingredients = new ArrayList<>();

        convertWhitespace(list);
    }

    //converts all whitespace to + symbol for url readability
    private void convertWhitespace(String[] list) {
        for (String ingredient : list) {
            String ingred = ingredient.replaceAll("\\s","+");
            ingredients.add(ingred);
        }
    }

    private String getIngredientString() {
        String s = "";

        for (int i = 0; i < ingredients.size() - 1; i++) {
            s += ingredients.get(0) + "%2C";
        }

        s += ingredients.get(ingredients.size()-1);
        return s;
    }

    @Override
    protected JSONObject doInBackground(Void... param) {

        //search for recipes using found ingredients from IngredientChooserActivity
        //must use api key then &q with list of ingredients for query
        //%2C is a comma
        String key = context.getResources().getString(R.string.food2fork);
        String ingredients = getIngredientString();
        String uri = "http://food2fork.com/api/search" + "?key=" + key + "&q=" + ingredients;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(uri);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            int b;
            //creates json as string
            while ((b = in.read()) != -1) {
                stringBuilder.append((char) b);
            }

            in.close();
        }
        catch(IOException e) {
            Log.e("IngredientChooser", e.getMessage());
        }

        //creates actual JSON object with braces
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

        } catch (JSONException e) {
            Log.e("json parser", "error parsing " + e.toString());
        }
        return jsonObject;
    }

    //create Recipe objects
    //put them in a recipe array to display contents later
    //in another activity
    @Override
    protected void onPostExecute(JSONObject result) {
        SetRecipes recipes = new SetRecipes(jsonObject);
        Recipe recipesList = recipes.setRecipesToView();
        listener.recipesRetrieved(recipesList);
    }
}
