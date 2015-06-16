package recipegen.hackdfwrecipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void goToRandom(View v) {
        Intent intent = new Intent(this, IngredientChooserActivity.class);
        startActivity(intent);
    }

    public void goToUsers(View v) {
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.user_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.enter_ingredients);
        // setup a dialog window
        alertDialogBuilder.setMessage(R.string.enter_ingredients)
                .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String[] list = editText.getText().toString().split(",[ ]*");
                        RetrieveRecipesAsyncTask rrat = new RetrieveRecipesAsyncTask(getApplicationContext(),
                                list, new OnRetrieveRecipesFinishedListener() {
                            @Override
                            public void recipesRetrieved(Recipe recipe) {
                                Intent intent = new Intent(getApplicationContext(), DisplayRecipesActivity.class);
                                intent.putExtra("recipes", recipe);
                                startActivity(intent);
                            }
                        });
                        rrat.execute();
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }
}
