package recipegen.hackdfwrecipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by britne on 3/17/15.
 */
public class RecipeListViewAdapter extends BaseAdapter {

    private List<Recipe> recipeEntries;
    private Context context;
    private int layoutResourceId;

    static class ViewHolder {
        TextView recipeTitle;
        ImageView image;
    }

    public RecipeListViewAdapter(Context context, List<Recipe> recipeEntries, int layoutResourceId) {
        this.context = context;
        this.recipeEntries = recipeEntries;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return recipeEntries.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v;

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            // well set up the ViewHolder
            v = new ViewHolder();
            v.image = (ImageView) convertView.findViewById(R.id.food_image);
            v.recipeTitle = (TextView) convertView.findViewById(R.id.recipe_name);

            // store the holder with the view.
            convertView.setTag(v);
        }
        else {
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder
            v = (ViewHolder) convertView.getTag();
        }

        Recipe recipe = (Recipe) getItem(position);

        // assign values if the object is not null
        if(recipe != null) {
            // tag (item ID) values
            v.recipeTitle.setTag(recipe.getTitle());

            //set image url as picture
            DownloadImageTask dit = new DownloadImageTask(v.image, recipeEntries.get(position).getImageUrl(),
                    position);
            dit.execute();

            v.image.setContentDescription("Image for " + recipeEntries.get(position).getTitle());
            v.recipeTitle.setText(StringEscapeUtils.unescapeHtml4(recipeEntries.get(position).getTitle()));
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return recipeEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView image;
        private int position;

        public DownloadImageTask(ImageView image, String url, int position) {
            this.url = url;
            this.image = image;
            this.position = position;
            image.setTag(position);
            image.setImageBitmap(null);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmapImage = null;
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmapImage = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmapImage;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            //checks to see if image is in same position
            if(result != null && ((Integer) image.getTag()) == this.position) {
                image.setImageBitmap(result);
            }
        }
    }

    public void remove(int position) {
        recipeEntries.remove(recipeEntries.get(position));
    }
}
