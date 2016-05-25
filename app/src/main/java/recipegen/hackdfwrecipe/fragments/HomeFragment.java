package recipegen.hackdfwrecipe.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import recipegen.hackdfwrecipe.R;

// TODO: use signin maybe and retrieve user's faves from server
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        Button random = (Button) view.findViewById(R.id.use_random);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.content_frag, IngredientChooserFragment.newInstance());
                transaction.commit();
            }
        });

        Button users = (Button) view.findViewById(R.id.use_users_ingredients);
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUsers();
            }
        });
        return view;
    }

    public void goToUsers() {
        DialogFragment ingredientsDialogFrag = UsersIngredientsDFrag.newInstance();
        ingredientsDialogFrag.show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ingredient_chooser, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.favorited_items) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.content_frag, FavoritedRecipesFragment.newInstance());
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
