package com.example.pasteleriamilsabores.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pasteleriamilsabores.data.model.CartItem
import com.example.pasteleriamilsabores.data.model.Product
import com.example.pasteleriamilsabores.data.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ecommerce.db"
        private const val DATABASE_VERSION = 1

        // Tables
        private const val TABLE_USERS = "users"
        private const val TABLE_PRODUCTS = "products"
        private const val TABLE_CART = "cart"

        // Common columns
        private const val COLUMN_ID = "id"

        // User columns
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"

        // Product columns
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_USER_ID = "user_id"

        // Cart columns
        private const val COLUMN_PRODUCT_ID = "product_id"
        private const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PHONE TEXT
            )
        """.trimIndent()

        val createProductsTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IMAGE TEXT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID)
            )
        """.trimIndent()

        val createCartTable = """
            CREATE TABLE $TABLE_CART (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_PRODUCT_ID INTEGER NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID),
                FOREIGN KEY($COLUMN_PRODUCT_ID) REFERENCES $TABLE_PRODUCTS($COLUMN_ID)
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createProductsTable)
        db.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // User operations
    fun insertUser(email: String, password: String, name: String, phone: String?): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NAME, name)
            put(COLUMN_PHONE, phone)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun getUser(email: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            ).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    fun updateUser(userId: Long, name: String, phone: String?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PHONE, phone)
        }
        return db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userId.toString()))
    }

    // Product operations
    fun insertProduct(title: String, price: Double, description: String?, image: String?, userId: Long): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_IMAGE, image)
            put(COLUMN_USER_ID, userId)
        }
        return db.insert(TABLE_PRODUCTS, null, values)
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            products.add(
                Product(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                )
            )
        }
        cursor.close()
        return products
    }

    fun updateProduct(id: Long, title: String, price: Double, description: String?, image: String?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_IMAGE, image)
        }
        return db.update(TABLE_PRODUCTS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteProduct(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_PRODUCTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Cart operations
    fun addToCart(userId: Long, productId: Long, quantity: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_PRODUCT_ID, productId)
            put(COLUMN_QUANTITY, quantity)
        }
        return db.insert(TABLE_CART, null, values)
    }

    fun getCartItems(userId: Long): List<CartItem> {
        val items = mutableListOf<CartItem>()
        val db = readableDatabase
        val query = """
            SELECT c.$COLUMN_ID, c.$COLUMN_QUANTITY, 
                   p.$COLUMN_ID as pid, p.$COLUMN_TITLE, p.$COLUMN_PRICE, p.$COLUMN_IMAGE
            FROM $TABLE_CART c
            INNER JOIN $TABLE_PRODUCTS p ON c.$COLUMN_PRODUCT_ID = p.$COLUMN_ID
            WHERE c.$COLUMN_USER_ID = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        while (cursor.moveToNext()) {
            items.add(
                CartItem(
                    id = cursor.getLong(0),
                    product = Product(
                        id = cursor.getLong(2),
                        title = cursor.getString(3),
                        price = cursor.getDouble(4),
                        image = cursor.getString(5)
                    ),
                    quantity = cursor.getInt(1)
                )
            )
        }
        cursor.close()
        return items
    }

    fun removeFromCart(cartItemId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_CART, "$COLUMN_ID = ?", arrayOf(cartItemId.toString()))
    }
}