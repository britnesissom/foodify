package recipegen.hackdfwrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;


/*
Implement a "which ingredients do you already have?" activity
Implement a "save recipe" function -> toast to say recipe is saved?
New activity to view saved recipes
 */
public class IngredientChooserActivity extends AppCompatActivity {

    private boolean initializedView = false;
    private TextView ingredient1;
    private TextView ingredient2;
    private TextView ingredient3;
    private DBHelper dbHelper;
    private Spinner ingredientTypeSpinner;
    private String ingredientType;
    private Recipe recipesList;

    /*
    create the TextViews and button to retrieve/set info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_chooser);

        dbHelper = new DBHelper(this);
        ingredient1 = (TextView) findViewById(R.id.slot1);
        ingredient2 = (TextView) findViewById(R.id.slot2);
        ingredient3 = (TextView) findViewById(R.id.slot3);

        addNothingSelectedSpinner();
        addListenerOnSpinnerSelection();
    }

    private void addNothingSelectedSpinner() {
        ingredientTypeSpinner = (Spinner) findViewById(R.id.ingredient_type_spinner);

        //add a fancy view to spinner
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.ingredient_types,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ingredientTypeSpinner.setPrompt("Select an ingredient type");

        ingredientTypeSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        this));

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
                if (initializedView == false) {
                    initializedView = true;
                    ingredientType = "Sweet";
                } else {
                    if (ingredientTypeSpinner.getSelectedItem().toString().equals("Sweet")) {
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
    public void randomPicker(View view) {

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

        //retrieve recipes using food2fork api
        RetrieveRecipes rr = new RetrieveRecipes(ingred1.getIngredientName(),
                ingred2.getIngredientName(), ingred3.getIngredientName());
        rr.execute();
    }

    //flashes ingredients on screen one after another to make it prettier
    //kind of like a slot machine except words change in the same spot
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
                    runOnUiThread(new Runnable() {
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

    private void checkForNoRecipesFound() {
        //if there are no recipes containing those ingredients, search using other random ingredients
        if (recipesList.getTotalNumOfRecipes() == 0) {
            //alert dialog pops up after words stop rotating
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    new AlertDialog.Builder(IngredientChooserActivity.this)
                            .setTitle("No Recipes Found!")
                            .setMessage(R.string.no_recipes_found)
                            .setPositiveButton(R.string.search_again, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //go back to ingredients screen
                                }
                            })
                            .show();
                }
            }, 2500);
        } else {

            //wait 2.7s before going to display recipes so you know what the ingredients are
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent intent =
                            new Intent(IngredientChooserActivity.this, DisplayRecipesActivity.class);
                    intent.putExtra("recipes", recipesList);
                    intent.putExtra("ingredients", ingredient1.getText().toString()
                            + ", " + ingredient2.getText().toString() + ", and "
                            + ingredient3.getText().toString());
                    startActivity(intent);
                }
            }, 2700);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredient_chooser, menu);
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


    private class RetrieveRecipes extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;
        private String ingredient1;
        private String ingredient2;
        private String ingredient3;

        //retrieves ingredients from IngredientChooserActivity
        public RetrieveRecipes(String ingredient1, String ingredient2, String ingredient3) {
            this.ingredient1 = ingredient1;
            this.ingredient2 = ingredient2;
            this.ingredient3 = ingredient3;

            convertWhitespace();
        }

        //converts all whitespace to + symbol for url readability
        private void convertWhitespace() {
            ingredient1 = ingredient1.replaceAll("\\s", "+");
            ingredient2 = ingredient2.replaceAll("\\s", "+");
            ingredient3 = ingredient3.replaceAll("\\s", "+");
        }

        @Override
        protected JSONObject doInBackground(Void... param) {

            //search for recipes using found ingredients from IngredientChooserActivity
            //must use api key then &q with list of ingredients for query
            //%2C is a comma
            String key = getResources().getString(R.string.food2fork);
            String uri = "http://food2fork.com/api/search" + "?key=" + key + "&q=" +
                    ingredient1 + "%2C" + ingredient2 + "%2C" + ingredient3;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(uri);
                URLConnection urlConnection = url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                int b;
                //creates json as string
                while ((b = in.read()) != -1) {
                    stringBuilder.append((char) b);
                }

                in.close();
            }
            catch(IOException e) {
                Log.e("IngredientChooser", e.getMessage());
            }

            //creates actual JSON object with braces
            try {
                jsonObject = new JSONObject(stringBuilder.toString());

            } catch (JSONException e) {
                Log.e("json parser", "error parsing " + e.toString());
            }
            return jsonObject;
        }

        //create Recipe objects
        //put them in a recipe array to display contents later
        //in another activity
        @Override
        protected void onPostExecute(JSONObject result) {
            SetRecipes recipes = new SetRecipes(jsonObject);
            recipesList = recipes.setRecipesToView();
            checkForNoRecipesFound();
        }
    }
}
