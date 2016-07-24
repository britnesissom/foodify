package recipegen.hackdfwrecipe.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by britne on 3/14/15.
 */

/*
implement Parcelable to be able to pass from activity to activity
 */
public class Recipes implements Parcelable {

    private String href;
    private String title;
    private String thumbnail;
    private boolean favorited = false;

    public void setHref(String href) {
        this.href = href;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public String getTitle() {
        return title;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }



    /* parcelable stuff here */

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(href);
        out.writeString(title);
        out.writeString(thumbnail);
        out.writeByte((byte) (favorited ? 1 : 0));
    }

    public static final Parcelable.Creator<Recipes> CREATOR
            = new Parcelable.Creator<Recipes>() {
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    private Recipes(Parcel in) {
        href = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        favorited = in.readByte() != 0;
    }
}
