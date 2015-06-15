package recipegen.hackdfwrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;


public class DisplayRecipesActivity extends CommonDisplayBehaviorActivity {

    private SharedPrefsUtility prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipes);

        prefs = new SharedPrefsUtility();
        super.onCreateCommon();
    }

    protected void setTitle() {
        setTitle("Top Recipes");
    }

    protected ArrayList<Recipe> getRecipes() {
        Recipe recipesList = getIntent().getParcelableExtra("recipes");
        return recipesList.getRecipes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d("DisplayRecipes", "onCreateContextMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_recipes_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.favorite:
                //gets recipe data to add to favorites
                Recipe recipe = (Recipe) getListAdapter().getItem(info.position);

                //adds recipe to favorites using sharedpreferences
                prefs.addFavorite(this, recipe);
                Toast.makeText(this, "Recipe Added to Favorites", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favorited_items) {
            Intent intent = new Intent(this, FavoritedRecipesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
