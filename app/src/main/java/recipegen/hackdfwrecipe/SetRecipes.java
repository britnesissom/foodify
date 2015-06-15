package recipegen.hackdfwrecipe;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
* Created by britne on 3/18/15.
*/
public class SetRecipes {

    private JSONObject jsonObject;
    private Recipe recipes;

    public SetRecipes(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Recipe setRecipesToView() {

        //need total # of recipes to check if we need to search again
        try {
            int count = jsonObject.getInt("count");
            Log.d("num recipes: ", "" + count);

            int newCount = determineNumRecipesToShow(count);
            JSONArray recipesList = jsonObject.getJSONArray("recipes");
            recipes = new Recipe(newCount);

            //need "global" recipe array to save individual recipes to
            recipes.setPublisher("no publisher");
            recipes.setSocialRank(0);
            recipes.setSourceUrl("no url");
            recipes.setTitle("all recipes");
            recipes.setImageUrl("no image url");
            recipes.setNumRecipesToShow(newCount);
            recipes.setTotalNumOfRecipes(count);

            JSONObject jsonRecipe;

            //only want to show <= 7 recipes, not all of them
            for (int i = 0; i < newCount; i++) {
                //each individual recipe as a json object
                jsonRecipe = recipesList.getJSONObject(i);

                //create a new recipe with all necessary info
                Recipe r = new Recipe(1);
                r.setPublisher(jsonRecipe.getString("publisher"));
                r.setSocialRank(jsonRecipe.getInt("social_rank"));
                r.setSourceUrl(jsonRecipe.getString("source_url"));
                r.setTitle(jsonRecipe.getString("title"));
                r.setImageUrl(jsonRecipe.getString("image_url"));

                //add to recipe list
                recipes.addRecipe(r);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    //only want to show 5 recipes max
    private int determineNumRecipesToShow(int count) {
        if (count < 7) { return count; }
        else { return 7; }
    }
}
