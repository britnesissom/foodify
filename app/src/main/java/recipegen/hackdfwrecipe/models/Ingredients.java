package recipegen.hackdfwrecipe.models;

/**
 * Created by britne on 3/6/15.
 */
public class Ingredients {

    private String _id;
    private String ingredientName;
    private String ingredientType;

    public Ingredients() {

    }

    public Ingredients(String id, String ingredientName, String ingredientType) {
        this._id = id;
        this.ingredientType = ingredientType;
        this.ingredientName = ingredientName;
    }

    public Ingredients(String ingredientName, String ingredientType) {
        this.ingredientName = ingredientName;
        this.ingredientType = ingredientType;
    }

    public void setID(String id) {
        this._id = id;
    }

    public String getID() {
        return this._id;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public void setIngredientType(String ingredientType) {
        this.ingredientType = ingredientType;
    }

    public String getIngredientType() {
        return this.ingredientType;

    }

}
