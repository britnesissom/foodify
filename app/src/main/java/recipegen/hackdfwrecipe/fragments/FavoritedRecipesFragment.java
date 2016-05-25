package recipegen.hackdfwrecipe.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import recipegen.hackdfwrecipe.R;
import recipegen.hackdfwrecipe.SharedPrefsUtility;
import recipegen.hackdfwrecipe.adapters.FaveRVAdapter;
import recipegen.hackdfwrecipe.models.Recipes;


public class FavoritedRecipesFragment extends Fragment implements FaveRVAdapter.OnRemoveRecipeListener {

    private SharedPrefsUtility prefs;
    private ArrayList<Recipes> faves;
    private FaveRVAdapter adapter;
    private Recipes recipeToRemove;
    private ActionMode mActionMode;

    // TODO: Rename and change types and number of parameters
    public static FavoritedRecipesFragment newInstance() {
        return new FavoritedRecipesFragment();
    }

    public FavoritedRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPrefsUtility();
        faves = prefs.getFavorites(getContext());

        if (faves == null) {
            faves = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorited_recipes, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Favorites");

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new FaveRVAdapter(faves, getContext(), this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recipe_rv);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        //adds floating context menu to each item in list
        // TODO: change this
        registerForContextMenu(recyclerView);
        return view;
    }

    @Override
    public void onRemoveRecipe(Recipes recipe) {

        recipeToRemove = recipe;

        if (mActionMode != null) {
            return;
        }

        // Start the CAB using the ActionMode.Callback defined above
        mActionMode = getActivity().startActionMode(mActionModeCallback);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.remove_fave_context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remove:
                    removeFavorite();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    private void removeFavorite() {
        //removes recipe from favorites using sharedpreferences
        prefs.removeFavorite(getContext(), recipeToRemove);
        ArrayList<Recipes> list = new ArrayList<>(faves);
        list.remove(recipeToRemove);
        faves.clear();
        faves.addAll(list);

        //update listview to not include removed recipe
        adapter.notifyDataSetChanged();
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d("FavoritedRecipes", "onCreateContextMenu called");
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.favorite_recipes_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:
                //gets recipe data to remove from favorites
                //Recipes recipes = (Recipes) getListAdapter().getItem(info.position);

                //removes recipe from favorites using sharedpreferences
                //prefs.removeFavorite(getContext(), recipes);
                //adapter.remove(info.position);

                //update listview to not include removed recipe
                adapter.notifyDataSetChanged();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/
}
