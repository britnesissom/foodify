package recipegen.hackdfwrecipe.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import recipegen.hackdfwrecipe.R;
import recipegen.hackdfwrecipe.RestAdapterClient;
import recipegen.hackdfwrecipe.models.Food2ForkResponse;
import recipegen.hackdfwrecipe.models.Recipes;
import recipegen.hackdfwrecipe.SharedPrefsUtility;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DisplayRecipesActivity extends CommonDisplayBehaviorActivity {

    private SharedPrefsUtility prefs;
    private ArrayList<Recipes> recipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipes);

        Log.d("DisplayRecipes", "display activity opened");
        recipesList = new ArrayList<>();
        prefs = new SharedPrefsUtility();
        super.onCreateCommon();
    }

    protected void setTitle() {
        setTitle("Top Recipes");
    }

    protected ArrayList<Recipes> getRecipes() {
        String key = this.getResources().getString(R.string.food2fork);

        RestAdapterClient.getRestClient().getRecipes(key, getIntent().getStringExtra("ingredients"),
                new Callback<Food2ForkResponse>() {
                    @Override
                    public void success(Food2ForkResponse food2ForkResponse, Response response) {
                        Log.d("DisplayRecipes", "recipes retrieved!");
                        Log.d("DisplayRecipes", food2ForkResponse.getRecipes().get(0).getTitle());
                        recipesList.clear();
                        recipesList.addAll(food2ForkResponse.getRecipes());
                        getListAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("DisplayRecipes", "error retrieving recipes: " + error.getMessage());
                    }
                });
        return recipesList;
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
                Recipes recipes = (Recipes) getListAdapter().getItem(info.position);

                //adds recipe to favorites using sharedpreferences
                prefs.addFavorite(this, recipes);
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
