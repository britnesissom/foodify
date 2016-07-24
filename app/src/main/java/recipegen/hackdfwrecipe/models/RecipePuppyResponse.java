package recipegen.hackdfwrecipe.models;

import java.util.List;

/**
 * Created by britne on 7/24/15.
 */
public class RecipePuppyResponse {

    private List<Recipes> results;

    public List<Recipes> getRecipes() {
        return results;
    }

    public void setRecipes(List<Recipes> recipes) {
        this.results = recipes;
    }
}
