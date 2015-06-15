package recipegen.hackdfwrecipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by britne on 6/13/15.
 */
public abstract class CommonDisplayBehaviorActivity extends AppCompatActivity {

    private RecipeListViewAdapter adapter;

    protected void onCreateCommon() {
        ListView recipeEntries = (ListView) findViewById(R.id.recipe_list);
        final ArrayList<Recipe> recipes = getRecipes();
        setTitle();

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);    //will return user to home screen
        }

        adapter = new RecipeListViewAdapter(this, recipes, R.layout.recipe_entry);
        recipeEntries.setAdapter(adapter);

        // implement event when an item on list view is selected
        recipeEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int pos, long id) {

                //open recipe website in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(recipes.get(pos).getSourceUrl()));
                startActivity(browserIntent);

            }
        });

        //adds floating context menu to each item in list
        registerForContextMenu(recipeEntries);
        Log.d("CommonDisplay", "finished onCreateCommon");
    }

    public RecipeListViewAdapter getListAdapter() {
        return adapter;
    }

    protected abstract void setTitle();

    protected abstract ArrayList<Recipe> getRecipes();
}
