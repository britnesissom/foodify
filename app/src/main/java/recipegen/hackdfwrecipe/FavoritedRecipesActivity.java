package recipegen.hackdfwrecipe;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;


public class FavoritedRecipesActivity extends CommonDisplayBehaviorActivity {

    private SharedPrefsUtility prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorited_recipes);

        prefs = new SharedPrefsUtility();
        super.onCreateCommon();

    }

    protected void setTitle() {
        setTitle("Favorite Recipes");
    }

    protected ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> faves = prefs.getFavorites(this);
        return faves;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d("FavoritedRecipes", "onCreateContextMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_recipes_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:
                //gets recipe data to remove from favorites
                Recipe recipe = (Recipe) getListAdapter().getItem(info.position);

                //removes recipe from favorites using sharedpreferences
                prefs.removeFavorite(this, recipe);
                getListAdapter().remove(info.position);

                //update listview to not include removed recipe
                getListAdapter().notifyDataSetChanged();
                Toast.makeText(this, "Recipe Removed from Favorites", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
