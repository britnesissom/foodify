package recipegen.hackdfwrecipe.models;

import java.util.List;

/**
 * Created by britne on 7/24/15.
 */
public class Food2ForkResponse {

    private int count;
    private List<Recipes> recipes;

    public int getCount() {
        return count;
    }

    public List<Recipes> getRecipes() {
        return recipes;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
    }
}
