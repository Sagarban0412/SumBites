package com.example.foodie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.foodie.MenuItem;
import com.example.foodie.TableModel;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SumBytes";
    private static final int DATABASE_VERSION = 11;

    // order_item table
    public static final String ORDER_TABLE = "order_item";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TABLE_NUMBER = "table_number";

    // waiters table
    public static final String WAITER_TABLE = "waiters";
    public static final String ID = "id";
    public static final String USERNAME = "name";
    public static final String PASSWORD = "password";

    // menu_item table
    public static final String MENU_TABLE = "menu_items";
    public static final String MENU_ID = "id";
    public static final String MENU_NAME = "name";
    public static final String MENU_PRICE = "price";
    public static final String MENU_CATEGORY = "category";

//    Status Table

    public static  final String STATUS_TABLE = "table_status";
    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create order_item table
        String createOrderTable = "CREATE TABLE " + ORDER_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_TABLE_NUMBER + " INTEGER)";
        db.execSQL(createOrderTable);

        // Create waiters table
        String createWaiterTable = "CREATE TABLE " + WAITER_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT)";
        db.execSQL(createWaiterTable);

        // Create menu_item table
        String createMenuTable = "CREATE TABLE " + MENU_TABLE + " (" +
                MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MENU_NAME + " TEXT, " +
                MENU_PRICE + " REAL, " +
                MENU_CATEGORY + " TEXT)";
        db.execSQL(createMenuTable);

        String createTableStatus = "CREATE TABLE IF NOT EXISTS "+STATUS_TABLE+"("+
                "table_number INTEGER PRIMARY KEY, " +
                "order_status INTEGER DEFAULT 0)";
        db.execSQL(createTableStatus);

        //Creating Table for Tables
        String createTablesTable = "CREATE TABLE IF NOT EXISTS tables (" +
                "table_number INTEGER PRIMARY KEY, " +
                "table_name TEXT)";
        db.execSQL(createTablesTable);



        // Insert default waiter
        ContentValues cv = new ContentValues();
        cv.put(USERNAME, "employee");
        cv.put(PASSWORD, "1234");
        db.insert(WAITER_TABLE, null, cv);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WAITER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MENU_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STATUS_TABLE);
        db.execSQL("Drop Table if Exists tables");
        onCreate(db);
    }

    // Add an item to order_item
    public void insertItem(String name, int quantity, double price, int tableNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_QUANTITY, quantity);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_TABLE_NUMBER, tableNumber);
        db.insert(ORDER_TABLE, null, cv);
    }

    // Get all order items
    public static Cursor selectData(Context context) {
        MyDatabase dbHelper = new MyDatabase(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM " + ORDER_TABLE;
        return db.rawQuery(query, null);
    }

    // Waiter login validation
    public boolean validateWaiter(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + WAITER_TABLE + " WHERE " + USERNAME + "=? AND " + PASSWORD + "=?",
                new String[]{name, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Get menu items by category (compatible with your MenuItem class)
    public List<MenuItem> getItemsByCategory(String category) {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MENU_TABLE + " WHERE " + MENU_CATEGORY + "=?", new String[]{category});
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MENU_PRICE));
                list.add(new MenuItem(name, price));  // using your MenuItem class
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    // In MyDatabase.java

    // Set order status (0 = not placed, 1 = placed)
    public void setOrderStatus(int tableNumber, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_status", status);
        db.update("table_status", values, "table_number=?", new String[]{String.valueOf(tableNumber)});
        Log.d("DB_STATUS", "Order status set to " + status + " for table " + tableNumber);
        db.close();
    }

    // Get order status
    public int getOrderStatus(int tableNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT order_status FROM table_status WHERE table_number=?",
                new String[]{String.valueOf(tableNumber)});
        int status = 0;
        if (cursor.moveToFirst()) {
            status = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return status;
    }

    // Initialize table_status row if not present (call this during app init)
    public void initializeTableStatus(int tableNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM table_status WHERE table_number=?", new String[]{String.valueOf(tableNumber)});
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("table_number", tableNumber);
            values.put("order_status", 0);
            db.insert("table_status", null, values);
        }
        cursor.close();
        db.close();
    }

    //method for tables
    public void insertDefaultTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] names = {"Table 1", "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Table 7", "VIP"};
        for (int i = 0; i < names.length; i++) {
            ContentValues values = new ContentValues();
            values.put("table_number", i + 1);
            values.put("table_name", names[i]);
            db.insertWithOnConflict("tables", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }
    public void insertTableIfNotExists(int tableNumber, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + STATUS_TABLE + " WHERE table_number=?",
                new String[]{String.valueOf(tableNumber)});
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("table_number", tableNumber);
            values.put("table_name", tableName);
            values.put("order_status", 0);
            db.insert(STATUS_TABLE, null, values);
        }
        cursor.close();
        db.close();
    }


    //fetch all tables
    public List<TableModel> getAllTables() {
        List<TableModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT table_number, table_name FROM tables", null);
        while (cursor.moveToNext()) {
            int num = cursor.getInt(0);
            String name = cursor.getString(1);
            list.add(new TableModel(num, name));
        }
        cursor.close();
        return list;
    }


}
