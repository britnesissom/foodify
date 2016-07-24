package recipegen.hackdfwrecipe.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import recipegen.hackdfwrecipe.R;
import recipegen.hackdfwrecipe.models.Recipes;

/**
 * Created by britne on 8/29/15.
 */
public class RecipeRVAdapter extends RecyclerView.Adapter<RecipeRVAdapter.ViewHolder> {

    private static final String TAG = "RecipeRecycler";
    private List<Recipes> recipesList;
    private OnFaveRecipeListener listener;
    private Context context;

    public interface OnFaveRecipeListener {
        void onFaveRecipe(Recipes recipe);
        void onRemoveRecipe(Recipes recipes);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView recipeTitle;
        ImageView image;
        ImageView faveStar;

        public ViewHolder(View v) {
            super(v);

            image = (ImageView) v.findViewById(R.id.food_image);
            faveStar = (ImageView) v.findViewById(R.id.fave_star_btn);
            recipeTitle = (TextView) v.findViewById(R.id.recipe_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecipeRVAdapter(List<Recipes> recipes, Context context, OnFaveRecipeListener listener) {
        recipesList = recipes;
        this.listener = listener;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_entry,
                parent, false);
        return new RecipeRVAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int pos = holder.getAdapterPosition();

        Picasso.with(context).load(recipesList.get(position).getThumbnail())
                .into(holder.image);
        holder.image.setContentDescription(WordUtils.capitalize(StringEscapeUtils.unescapeHtml4(recipesList
                .get(position).getTitle())));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(recipesList.get(pos).getHref()));
                context.startActivity(browserIntent);
            }
        });

        // make sure you can tell which recipes are faved already
        if (recipesList.get(pos).isFavorited()) {
            holder.faveStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable
                    .ic_star));
        } else {
            holder.faveStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable
                    .ic_star_border));
        }

        holder.faveStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFave(holder, pos);
            }
        });

        holder.recipeTitle.setText(WordUtils.capitalize(StringEscapeUtils.unescapeHtml4
                (recipesList.get(pos).getTitle())));
    }

    private void checkFave(ViewHolder holder, int pos) {
        if (recipesList.get(pos).isFavorited()) {
            recipesList.get(pos).setFavorited(false);
            holder.faveStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable
                    .ic_star_border));
            listener.onRemoveRecipe(recipesList.get(pos));
        }
        else {
            recipesList.get(pos).setFavorited(true);
            holder.faveStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable
                    .ic_star));
            listener.onFaveRecipe(recipesList.get(pos));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (recipesList == null) {
            recipesList = new ArrayList<>();
        }
        return recipesList.size();
    }
}
