package com.example.foodie; // Or your actual package name

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.foodie.database.MyDatabase;

public class JsonExporter {
    public static String getOrderItemsForTableAsJson(Context context, int tableNumber) {
        MyDatabase dbHelper = new MyDatabase(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + MyDatabase.ORDER_TABLE + " WHERE " + MyDatabase.COLUMN_TABLE_NUMBER + " = ?",
                new String[]{String.valueOf(tableNumber)}
        );

        JSONArray jsonArray = new JSONArray();

        try {
            while (cursor.moveToNext()) {
                JSONObject obj = new JSONObject();
                obj.put("id", cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_ID)));
                obj.put("name", cursor.getString(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_NAME)));
                obj.put("quantity", cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_QUANTITY)));
                obj.put("price", cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_PRICE)));
                obj.put("table_number", cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_TABLE_NUMBER)));
                jsonArray.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        return jsonArray.toString();
    }

}
