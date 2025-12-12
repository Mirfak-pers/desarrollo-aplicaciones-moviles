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
        private const val DATABASE_NAME = "pasteleriamilsabores.db"
        private const val DATABASE_VERSION = 2

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
        private const val COLUMN_IS_PRELOADED = "is_preloaded"

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
                $COLUMN_IS_PRELOADED INTEGER DEFAULT 0,
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

        insertPreloadedProducts(db)
    }

    private fun insertPreloadedProducts(db: SQLiteDatabase) {
        val preloadedProducts = listOf(
            Triple("Torta de Chocolate", 25000.0, "Deliciosa torta de chocolate con cobertura de ganache" to "torta_chocolate"),
            Triple("Torta de Vainilla", 24000.0, "Suave torta de vainilla con crema de mantequilla" to "torta_vainilla"),
            Triple("Torta de Frutas", 28000.0, "Torta decorada con frutas frescas de temporada" to "torta_frutas"),
            Triple("Torta de Manjar", 26000.0, "Torta rellena con delicioso manjar casero" to "torta_manjar"),
            Triple("Torta de Naranja", 23000.0, "Refrescante torta de naranja con glaseado" to "torta_naranja"),
            Triple("Torta de Cumpleaños", 30000.0, "Torta personalizada para celebraciones especiales" to "torta_cumpleanos"),
            Triple("Torta de Boda", 150000.0, "Elegante torta de varios pisos para bodas" to "torta_boda"),
            Triple("Torta Vegana", 32000.0, "Torta 100% vegana sin ingredientes de origen animal" to "torta_vegana"),
            Triple("Brownie Sin Gluten", 5000.0, "Delicioso brownie apto para celíacos" to "brownie_sin_gluten"),
            Triple("Cheesecake Sin Azúcar", 22000.0, "Cheesecake endulzado naturalmente" to "cheesecake_sin_azucar"),
            Triple("Empanada de Manzana", 3500.0, "Crujiente empanada rellena de manzana y canela" to "empanada_manzana"),
            Triple("Galletas Veganas", 8000.0, "Pack de galletas veganas variadas (12 unidades)" to "galletas_veganas"),
            Triple("Mousse de Chocolate", 6500.0, "Suave mousse de chocolate belga" to "mousse_chocolate"),
            Triple("Pan Sin Gluten", 4500.0, "Pan artesanal libre de gluten" to "pan_sin_gluten"),
            Triple("Tarta de Santiago", 18000.0, "Tradicional tarta de almendras española" to "tarta_santiago"),
            Triple("Tiramisú", 7500.0, "Clásico postre italiano con café y mascarpone" to "tiramisu")
        )

        preloadedProducts.forEach { (title, price, descriptionAndImage) ->
            val (description, imageName) = descriptionAndImage
            val values = ContentValues().apply {
                put(COLUMN_TITLE, title)
                put(COLUMN_PRICE, price)
                put(COLUMN_DESCRIPTION, description)
                put(COLUMN_IMAGE, "drawable:$imageName") // Marcamos como recurso drawable
                put(COLUMN_USER_ID, 0)
                put(COLUMN_IS_PRELOADED, 1)
            }
            db.insert(TABLE_PRODUCTS, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_PRODUCTS ADD COLUMN $COLUMN_IS_PRELOADED INTEGER DEFAULT 0")
        }
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
            put(COLUMN_IS_PRELOADED, 0)
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
                    image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    isPreloaded = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PRELOADED)) == 1
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