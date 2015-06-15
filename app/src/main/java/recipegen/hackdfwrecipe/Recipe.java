package recipegen.hackdfwrecipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by britne on 3/14/15.
 */

/*
implement Parcelable to be able to pass from activity to activity
 */
public class Recipe implements Parcelable {

    private ArrayList<Recipe> recipes;
    private int numRecipesToShow;
    private int totalNumOfRecipes;
    private int socialRank;
    private String publisher;
    private String sourceUrl;
    private String title;
    private String imageUrl;

    public Recipe(int count) {
        recipes = new ArrayList<>();
        setTotalNumOfRecipes(count);
    }

    public void setTotalNumOfRecipes(int count) {
        this.totalNumOfRecipes = count;
    }

    public int getTotalNumOfRecipes() {
        return totalNumOfRecipes;
    }

    public void setNumRecipesToShow(int numRecipes) {
        this.numRecipesToShow = numRecipes;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    public void setSocialRank(int socialRank) {
        this.socialRank = socialRank;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumRecipesToShow() {
        return numRecipesToShow;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public int getSocialRank() {
        return socialRank;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /*
    necessary methods to implement parcelable and be able to send Recipe in an intent to
    another activity
     */

    protected Recipe(Parcel in) {
        if (in.readByte() == 0x01) {
            recipes = new ArrayList<>();
            in.readList(recipes, Recipe.class.getClassLoader());
        } else {
            recipes = null;
        }
        numRecipesToShow = in.readInt();
        totalNumOfRecipes = in.readInt();
        socialRank = in.readInt();
        publisher = in.readString();
        sourceUrl = in.readString();
        title = in.readString();
        imageUrl = in.readString();
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
        dest.writeInt(numRecipesToShow);
        dest.writeInt(totalNumOfRecipes);
        dest.writeInt(socialRank);
        dest.writeString(publisher);
        dest.writeString(sourceUrl);
        dest.writeString(title);
        dest.writeString(imageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
