package recipegen.hackdfwrecipe.models;

/**
 * Created by britne on 3/14/15.
 */

/*
implement Parcelable to be able to pass from activity to activity
 */
public class Recipes {

    private String source_url;
    private String title;
    private String image_url;
    private boolean favorited = false;

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getTitle() {
        return title;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
