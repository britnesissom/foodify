package recipegen.hackdfwrecipe.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

import recipegen.hackdfwrecipe.DBHelper;
import recipegen.hackdfwrecipe.R;
import recipegen.hackdfwrecipe.RestAdapterClient;
import recipegen.hackdfwrecipe.adapters.NothingSelectedSpinnerAdapter;
import recipegen.hackdfwrecipe.models.Food2ForkResponse;
import recipegen.hackdfwrecipe.models.Ingredients;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class IngredientChooserFragment extends Fragment {

    private static final String TAG = "IngredientChooser";

    private boolean initializedView = false;
    private TextView ingredient1;
    private TextView ingredient2;
    private TextView ingredient3;
    private DBHelper dbHelper;
    private Spinner ingredientTypeSpinner;
    private String ingredientType;

    public IngredientChooserFragment() {
        // Required empty public constructor
    }

    public static IngredientChooserFragment newInstance() {
        return new IngredientChooserFragment();
    }

    /*
    create the TextViews and button to retrieve/set info
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_ingredient_chooser, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText(R.string.app_name);

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ingredient1 = (TextView) view.findViewById(R.id.slot1);
        ingredient2 = (TextView) view.findViewById(R.id.slot2);
        ingredient3 = (TextView) view.findViewById(R.id.slot3);

        addNothingSelectedSpinner(view);
        addListenerOnSpinnerSelection();

        Button foodChooserBtn = (Button) view.findViewById(R.id.food_chooser);
        foodChooserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomPicker();
            }
        });

        return view;
    }

    private void addNothingSelectedSpinner(View view) {
        ingredientTypeSpinner = (Spinner) view.findViewById(R.id.ingredient_type_spinner);

        //add a fancy view to spinner
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.ingredient_types,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ingredientTypeSpinner.setPrompt("Select an ingredient type");

        ingredientTypeSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,
                        getContext()));

    }

    /*
    adds listener to spinner to retrieve the selection and use it to
    pick the correct types of ingredients

    the default type is breakfast ingredients
     */
    public void addListenerOnSpinnerSelection() {
        ingredientTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //if spinner hasn't initialized yet then don't try to get selected item
                //because it will be null
                if (!initializedView) {
                    initializedView = true;
                    ingredientType = "Sweet";
                } else {
                    if (ingredientTypeSpinner.getSelectedItem() == null || ingredientTypeSpinner
                            .getSelectedItem().toString().equals("Sweet")) {
                        ingredientType = "Sweet";
                    } else {
                        ingredientType = "Savory";
                    }
                }
            }

            //option if user has just opened app or never actually chooses a type
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ingredientType = "Sweet";
            }
        });
    }

    /*
    randomly picks ingredients from database using MyDBHandler once button is clicked
     */
    public void randomPicker() {

        flashIngredientsOnScreen();

        Ingredients ingred1 = dbHelper.randomPicker(ingredientType);
        Ingredients ingred2 = dbHelper.randomPicker(ingredientType);
        Ingredients ingred3 = dbHelper.randomPicker(ingredientType);

        //if any 2 of the 3 ingredients are equal, get new ingredients to use
        while (checkIfEqual(ingred1, ingred2, ingred3)) {
            ingred1 = dbHelper.randomPicker(ingredientType);
            ingred2 = dbHelper.randomPicker(ingredientType);
            ingred3 = dbHelper.randomPicker(ingredientType);
        }
        dbHelper.close();   //closes db from any edits

        //set TextView as random ingredients from DBHandler
        setTextView(ingred1, ingredient1);
        setTextView(ingred2, ingredient2);
        setTextView(ingred3, ingredient3);

        String[] list = {ingred1.getIngredientName(), ingred2.getIngredientName(), ingred3.getIngredientName()};

        getRecipes(list);
    }

    private void getRecipes(final String[] list) {
        String key = this.getResources().getString(R.string.food2fork);

        RestAdapterClient.getRestClient().getRecipes(key, list[0] + "," + list[1] + "," + list[2],
                new Callback<Food2ForkResponse>() {
                    @Override
                    public void success(Food2ForkResponse food2ForkResponse, Response response) {
                        Log.d(TAG, "recipes retrieved!");

                        if (food2ForkResponse.getCount() == 0) {
                            Log.d(TAG, "count is 0");
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Snackbar.make(getActivity().findViewById(R.id.coord_layout),
                                                    "No recipes found!", Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("OK", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {}
                                                    }).show();
                                        }
                                    });
                                }
                            }, 1500);
                        } else {
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    String ingredients = list[0] + "," + list[1] + "," + list[2];
                                    FragmentTransaction transaction =
                                            getActivity().getSupportFragmentManager()
                                            .beginTransaction();
                                    transaction.replace(R.id.content_frag, DisplayRecipesFragment
                                            .newInstance(ingredients));
                                    Log.d(TAG, "starting displayrecipes activity");
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }, 2700);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error retrieving recipes: " + error.getMessage());
                    }
                });
    }

    //flashes ingredients on screen one after another to make it prettier
    //words cycle through basically
    private void flashIngredientsOnScreen() {
        new Thread(new Runnable() {
            public void run() {
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    long time = System.currentTimeMillis();
                    public void run()   {
                        //stop rotating words after 2.2 seconds
                        if (System.currentTimeMillis() - time > 2200) {
                            cancel();
                        }

                        // This will update your text instance without the need of a Handler
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Ingredients ingredient = dbHelper.randomPicker(ingredientType);
                                ingredient1.setText(ingredient.getIngredientName());
                                ingredient = dbHelper.randomPicker(ingredientType);
                                ingredient2.setText(ingredient.getIngredientName());
                                ingredient = dbHelper.randomPicker(ingredientType);
                                ingredient3.setText(ingredient.getIngredientName());

                                dbHelper.close();
                            }
                        });
                    }
                }, 0, 100);
            }
        }).start();
    }

    //sets the ingredients textview in this activity so user knows what is chosen
    private void setTextView(Ingredients randomIngredient, TextView ingredient) {
        if (randomIngredient != null) {
            ingredient.setText(randomIngredient.getIngredientName());
        }
    }

    //if any ingredients are equal, return true so new ingredients are found
    private boolean checkIfEqual(Ingredients in1, Ingredients in2, Ingredients in3) {
        if (in1.getIngredientName().equals(in2.getIngredientName())
                || in1.getIngredientName().equals(in3.getIngredientName())
                || in2.getIngredientName().equals(in3.getIngredientName())) {
            Log.i("ingredients equal", "true");
            return true;
        }
        Log.i("ingredients equal", "false");
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
