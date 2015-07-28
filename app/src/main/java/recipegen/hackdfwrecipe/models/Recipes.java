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

    /*
    necessary methods to implement parcelable and be able to send Recipe in an intent to
    another activity
     */

    /*protected Recipes(Parcel in) {
        if (in.readByte() == 0x01) {
            recipes = new ArrayList<>();
            in.readList(recipes, Recipes.class.getClassLoader());
        } else {
            recipes = null;
        }
        source_url = in.readString();
        title = in.readString();
        image_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (recipes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipes);
        }
        dest.writeString(source_url);
        dest.writeString(title);
        dest.writeString(image_url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipes> CREATOR = new Parcelable.Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };*/
}
