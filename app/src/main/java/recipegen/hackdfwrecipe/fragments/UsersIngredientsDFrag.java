package recipegen.hackdfwrecipe.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(R.string.enter_ingredients);

        // setup a dialog window
        //user types the ingredients they have into text box
        //converts string to array of ingredients
        alertDialogBuilder
                .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.content_frag, DisplayRecipesFragment.newInstance
                                (ingredients.getText().toString()));
                        transaction.commit();
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create an alert dialog
        return alertDialogBuilder.create();
    }
}
