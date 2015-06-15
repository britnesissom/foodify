package recipegen.hackdfwrecipe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by britne on 4/5/15.
 */
public class DBHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "IngredientTable.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    //picks random ingredient based on type from database
    public Ingredients randomPicker(String ingredientType) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Ingredients ingredient = new Ingredients();

        String[] sqlSelect = {"Ingredient", "Type"};
        String sqlTables = "Ingredients";
        String typeToSelect = sqlSelect[1] + "=\'" + ingredientType + "\'";
        qb.setTables(sqlTables);

        //pick only one ingredient from a random row
        Cursor cursor = qb.query(db, sqlSelect, typeToSelect, null, null, null, "RANDOM()", "1");

        //cursor to ingredient
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            ingredient.setIngredientName(cursor.getString(0));
            ingredient.setIngredientType(cursor.getString(1));
            cursor.close();
        } else {
            ingredient = null;
        }
        db.close();

        return ingredient;
    }
}
