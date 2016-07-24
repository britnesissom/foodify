package recipegen.hackdfwrecipe.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import recipegen.hackdfwrecipe.R;

// TODO: check if user doesn't type anything
public class UsersIngredientsDFrag extends DialogFragment {

    private static final String TAG = "AddCollarDialog";

    public static UsersIngredientsDFrag newInstance() {
        return new UsersIngredientsDFrag();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_dialog, null);

        final EditText ingredients = (EditText) view.findViewById(R.id.enter_ingredients);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.enter_ingredients)
                .setPositiveButton(R.string.search, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                .create();

        // create an alert dialog
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (!ingredients.getText().toString().isEmpty()) {
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                    .beginTransaction();
                            transaction.replace(R.id.content_frag, DisplayRecipesFragment.newInstance
                                    (ingredients.getText().toString()));
                            transaction.commit();

                            alertDialog.dismiss();
                        }
                        else {
                            ingredients.setError("No ingredients entered");
                        }
                    }
                });
            }
        });

        return alertDialog;
    }
}
