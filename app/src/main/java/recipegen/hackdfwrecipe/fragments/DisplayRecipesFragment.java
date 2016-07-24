package recipegen.hackdfwrecipe.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import recipegen.hackdfwrecipe.R;
import recipegen.hackdfwrecipe.RestAdapterClient;
import recipegen.hackdfwrecipe.SharedPrefsUtility;
import recipegen.hackdfwrecipe.adapters.RecipeRVAdapter;
import recipegen.hackdfwrecipe.models.RecipePuppyResponse;
import recipegen.hackdfwrecipe.models.Recipes;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

// TODO: add share function for fb, tumblr, twitter?
public class DisplayRecipesFragment extends Fragment implements RecipeRVAdapter.OnFaveRecipeListener {

    private static final String RECIPES = "recipes";

    private ArrayList<Recipes> recipesList;
    private RecipeRVAdapter adapter;
    private SharedPrefsUtility prefs;

    public static DisplayRecipesFragment newInstance(ArrayList<Recipes> recipes) {
        DisplayRecipesFragment fragment = new DisplayRecipesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(RECIPES, recipes);
        fragment.setArguments(args);
        return fragment;
    }

    public DisplayRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPrefsUtility();
        recipesList = new ArrayList<>();

        if (getArguments() != null) {
            recipesList = getArguments().getParcelableArrayList(RECIPES);
        }

        adapter = new RecipeRVAdapter(recipesList, getContext(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_display_recipes, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setDisplayShowTitleEnabled(false);
        }

        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        String top = "Recipes";
        title.setText(top);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recipe_rv);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onFaveRecipe(Recipes recipe) {
        //adds recipe to favorites using sharedpreferences
        prefs.addFavorite(getContext(), recipe);
        Snackbar.make(getActivity().findViewById(R.id.coord_layout), "Added to " +
                "favorites", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        }).show();
    }

    @Override
    public void onRemoveRecipe(Recipes recipe) {
        // removes recipe from faves using sharedpreferences
        prefs.removeFavorite(getContext(), recipe);
        Snackbar.make(getActivity().findViewById(R.id.coord_layout), "Removed from favorites",
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
        }).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favorited_items) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.content_frag, FavoritedRecipesFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
