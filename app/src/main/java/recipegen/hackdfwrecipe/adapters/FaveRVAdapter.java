package recipegen.hackdfwrecipe.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * Created by britne on 5/18/16.
 */
public class FaveRVAdapter extends RecyclerView.Adapter<FaveRVAdapter.ViewHolder> {

    private static final String TAG = "RecipeRecycler";
    private List<Recipes> recipesList;
    private OnRemoveRecipeListener listener;
    private Context context;

    public interface OnRemoveRecipeListener {
        void onRemoveRecipe(Recipes recipe);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView recipeTitle;
        ImageView image;

        public ViewHolder(View v) {
            super(v);
            view = v;
            image = (ImageView) v.findViewById(R.id.food_image);
            recipeTitle = (TextView) v.findViewById(R.id.recipe_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FaveRVAdapter(List<Recipes> recipes, Context context, OnRemoveRecipeListener
            listener) {
        recipesList = recipes;
        this.listener = listener;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FaveRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fave_recipe_entry,
                parent, false);
        return new FaveRVAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final int clickPos = holder.getAdapterPosition();

        Picasso.with(context).load(recipesList.get(clickPos).getThumbnail())
                .into(holder.image);
        holder.recipeTitle.setText(recipesList.get(clickPos).getTitle());
        holder.image.setContentDescription(WordUtils.capitalize(StringEscapeUtils.unescapeHtml4
                (recipesList.get(clickPos).getTitle())));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(recipesList.get(clickPos).getHref()));
                context.startActivity(browserIntent);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onRemoveRecipe(recipesList.get(clickPos));
                return false;
            }
        });
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


