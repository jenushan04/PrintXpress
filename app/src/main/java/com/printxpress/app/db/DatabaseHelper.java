package com.printxpress.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.printxpress.app.model.Order;
import com.printxpress.app.model.Product;
import com.printxpress.app.model.Promotion;
import com.printxpress.app.model.User;
import com.printxpress.app.util.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Central SQLite helper. Owns the schema, seeds initial data on first run,
 * and exposes typed CRUD methods used by activities.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "printxpress.db";
    private static final int DB_VERSION = 1;

    // ----- Tables -----
    public static final String T_USERS = "users";
    public static final String T_PRODUCTS = "products";
    public static final String T_ORDERS = "orders";
    public static final String T_PROMOTIONS = "promotions";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_USERS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "email TEXT UNIQUE NOT NULL, " +
            "phone TEXT NOT NULL, " +
            "password_hash TEXT NOT NULL, " +
            "address TEXT, " +
            "role TEXT NOT NULL DEFAULT 'CUSTOMER', " +
            "created_at INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + T_PRODUCTS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "category TEXT NOT NULL, " +
            "description TEXT, " +
            "material TEXT, " +
            "size_option TEXT, " +
            "price REAL NOT NULL, " +
            "active INTEGER NOT NULL DEFAULT 1)");

        db.execSQL("CREATE TABLE " + T_ORDERS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "product_id INTEGER NOT NULL, " +
            "quantity INTEGER NOT NULL, " +
            "specifications TEXT, " +
            "custom_text TEXT, " +
            "design_file_uri TEXT, " +
            "delivery_type TEXT NOT NULL, " +
            "delivery_address TEXT, " +
            "total_amount REAL NOT NULL, " +
            "status TEXT NOT NULL DEFAULT 'PENDING', " +
            "created_at INTEGER NOT NULL, " +
            "updated_at INTEGER NOT NULL, " +
            "FOREIGN KEY(user_id) REFERENCES users(id), " +
            "FOREIGN KEY(product_id) REFERENCES products(id))");

        db.execSQL("CREATE TABLE " + T_PROMOTIONS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT NOT NULL, " +
            "description TEXT, " +
            "discount_percent INTEGER NOT NULL DEFAULT 0, " +
            "valid_until TEXT, " +
            "active INTEGER NOT NULL DEFAULT 1)");

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + T_PROMOTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + T_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void seedData(SQLiteDatabase db) {
        long now = System.currentTimeMillis();

        // Default admin account
        ContentValues admin = new ContentValues();
        admin.put("name", "Administrator");
        admin.put("email", "admin@printxpress.lk");
        admin.put("phone", "+94770000000");
        admin.put("password_hash", PasswordUtil.hash("admin123"));
        admin.put("address", "PrintXpress HQ, Colombo");
        admin.put("role", User.ROLE_ADMIN);
        admin.put("created_at", now);
        db.insert(T_USERS, null, admin);

        // Sample customer account
        ContentValues cust = new ContentValues();
        cust.put("name", "Sample Customer");
        cust.put("email", "customer@printxpress.lk");
        cust.put("phone", "+94771234567");
        cust.put("password_hash", PasswordUtil.hash("customer123"));
        cust.put("address", "No. 25, Galle Road, Colombo 03");
        cust.put("role", User.ROLE_CUSTOMER);
        cust.put("created_at", now);
        db.insert(T_USERS, null, cust);

        // Seed products
        insertProduct(db, "Business Cards (Standard)", "Business Cards",
            "Premium quality 300gsm business cards with matte finish.",
            "300gsm Matte Card", "85 x 55 mm (100 cards)", 1500.00);
        insertProduct(db, "Business Cards (Glossy)", "Business Cards",
            "High-gloss laminated business cards for a polished look.",
            "350gsm Glossy", "85 x 55 mm (100 cards)", 1800.00);

        insertProduct(db, "A3 Poster", "Posters",
            "Full-colour A3 posters on 170gsm satin paper. Great for events.",
            "170gsm Satin", "A3 (297 x 420 mm)", 450.00);
        insertProduct(db, "A2 Poster (Large)", "Posters",
            "Eye-catching A2 posters with vivid colour reproduction.",
            "170gsm Satin", "A2 (420 x 594 mm)", 850.00);

        insertProduct(db, "Vinyl Banner", "Banners",
            "Durable outdoor vinyl banner with eyelets.",
            "440gsm PVC Vinyl", "Per square foot", 250.00);

        insertProduct(db, "A5 Flyers", "Flyers",
            "A5 single-sided full-colour flyers for promotions.",
            "130gsm Gloss", "A5 (148 x 210 mm) - 100 pcs", 1200.00);
        insertProduct(db, "A4 Flyers", "Flyers",
            "A4 double-sided full-colour flyers, ideal for menus.",
            "150gsm Gloss", "A4 (210 x 297 mm) - 100 pcs", 2200.00);

        insertProduct(db, "Vinyl Stickers (Pack of 50)", "Stickers",
            "Waterproof die-cut vinyl stickers, custom shapes supported.",
            "Vinyl with adhesive backing", "Up to 75 x 75 mm", 1750.00);

        insertProduct(db, "Custom T-Shirt", "Custom Merchandise",
            "Cotton t-shirt with custom DTG print on front.",
            "180gsm Cotton", "S / M / L / XL / XXL", 1850.00);

        insertProduct(db, "Custom Mug", "Custom Merchandise",
            "11oz ceramic mug with full-wrap sublimation print.",
            "Ceramic", "11oz / 325ml", 950.00);

        // Seed promotions
        insertPromo(db, "Festive Season 15% Off",
            "Get 15% off on all custom merchandise this festive season.",
            15, "2026-12-31");
        insertPromo(db, "Bulk Order Special",
            "Order 200+ business cards and save 20% on the total amount.",
            20, "2026-06-30");
        insertPromo(db, "Welcome Offer",
            "10% off your first order with us. Welcome to PrintXpress!",
            10, "2026-12-31");
    }

    private void insertProduct(SQLiteDatabase db, String name, String cat, String desc,
                               String material, String size, double price) {
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("category", cat);
        v.put("description", desc);
        v.put("material", material);
        v.put("size_option", size);
        v.put("price", price);
        v.put("active", 1);
        db.insert(T_PRODUCTS, null, v);
    }

    private void insertPromo(SQLiteDatabase db, String title, String desc,
                             int discount, String validUntil) {
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("description", desc);
        v.put("discount_percent", discount);
        v.put("valid_until", validUntil);
        v.put("active", 1);
        db.insert(T_PROMOTIONS, null, v);
    }

    // ============================================================
    //                        USER OPERATIONS
    // ============================================================

    /** Returns row id, or -1 if email already exists. */
    public long registerUser(User user, String plainPassword) {
        if (findUserByEmail(user.getEmail()) != null) return -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", user.getName());
        v.put("email", user.getEmail());
        v.put("phone", user.getPhone());
        v.put("password_hash", PasswordUtil.hash(plainPassword));
        v.put("address", user.getAddress());
        v.put("role", user.getRole() == null ? User.ROLE_CUSTOMER : user.getRole());
        v.put("created_at", System.currentTimeMillis());
        return db.insert(T_USERS, null, v);
    }

    public User authenticate(String email, String plainPassword) {
        User u = findUserByEmail(email);
        if (u == null) return null;
        return PasswordUtil.verify(plainPassword, u.getPasswordHash()) ? u : null;
    }

    public User findUserByEmail(String email) {
        if (email == null) return null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_USERS, null, "email=?",
            new String[]{email.trim().toLowerCase()}, null, null, null);
        User user = null;
        if (c.moveToFirst()) user = readUser(c);
        c.close();
        return user;
    }

    public User findUserById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_USERS, null, "id=?",
            new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (c.moveToFirst()) user = readUser(c);
        c.close();
        return user;
    }

    public boolean updateUserProfile(long id, String name, String phone, String address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("phone", phone);
        v.put("address", address);
        return db.update(T_USERS, v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean changePassword(long id, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("password_hash", PasswordUtil.hash(newPassword));
        return db.update(T_USERS, v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<User> getAllCustomers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_USERS, null, "role=?",
            new String[]{User.ROLE_CUSTOMER}, null, null, "created_at DESC");
        while (c.moveToNext()) list.add(readUser(c));
        c.close();
        return list;
    }

    private User readUser(Cursor c) {
        User u = new User();
        u.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        u.setName(c.getString(c.getColumnIndexOrThrow("name")));
        u.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
        u.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
        u.setPasswordHash(c.getString(c.getColumnIndexOrThrow("password_hash")));
        u.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
        u.setRole(c.getString(c.getColumnIndexOrThrow("role")));
        u.setCreatedAt(c.getLong(c.getColumnIndexOrThrow("created_at")));
        return u;
    }

    // ============================================================
    //                       PRODUCT OPERATIONS
    // ============================================================

    public long addProduct(Product p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = productValues(p);
        return db.insert(T_PRODUCTS, null, v);
    }

    public boolean updateProduct(Product p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = productValues(p);
        return db.update(T_PRODUCTS, v, "id=?", new String[]{String.valueOf(p.getId())}) > 0;
    }

    public boolean deleteProduct(long id) {
        SQLiteDatabase db = getWritableDatabase();
        // Soft delete: just mark inactive so historical orders still resolve
        ContentValues v = new ContentValues();
        v.put("active", 0);
        return db.update(T_PRODUCTS, v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    private ContentValues productValues(Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("category", p.getCategory());
        v.put("description", p.getDescription());
        v.put("material", p.getMaterial());
        v.put("size_option", p.getSizeOption());
        v.put("price", p.getPrice());
        v.put("active", p.isActive() ? 1 : 0);
        return v;
    }

    public Product getProduct(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PRODUCTS, null, "id=?",
            new String[]{String.valueOf(id)}, null, null, null);
        Product p = null;
        if (c.moveToFirst()) p = readProduct(c);
        c.close();
        return p;
    }

    /** Active products only (customer-facing). Optional category filter and search term. */
    public List<Product> getActiveProducts(String category, String search) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder where = new StringBuilder("active=1");
        List<String> args = new ArrayList<>();
        if (category != null && !"All".equalsIgnoreCase(category)) {
            where.append(" AND category=?");
            args.add(category);
        }
        if (search != null && !search.trim().isEmpty()) {
            where.append(" AND (name LIKE ? OR description LIKE ?)");
            String like = "%" + search.trim() + "%";
            args.add(like);
            args.add(like);
        }
        Cursor c = db.query(T_PRODUCTS, null, where.toString(),
            args.toArray(new String[0]), null, null, "category, name");
        while (c.moveToNext()) list.add(readProduct(c));
        c.close();
        return list;
    }

    /** Admin view sees inactive products too. */
    public List<Product> getAllProductsForAdmin() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PRODUCTS, null, null, null, null, null, "active DESC, category, name");
        while (c.moveToNext()) list.add(readProduct(c));
        c.close();
        return list;
    }

    public List<String> getDistinctCategories() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT category FROM " + T_PRODUCTS +
            " WHERE active=1 ORDER BY category", null);
        while (c.moveToNext()) list.add(c.getString(0));
        c.close();
        return list;
    }

    private Product readProduct(Cursor c) {
        Product p = new Product();
        p.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        p.setName(c.getString(c.getColumnIndexOrThrow("name")));
        p.setCategory(c.getString(c.getColumnIndexOrThrow("category")));
        p.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        p.setMaterial(c.getString(c.getColumnIndexOrThrow("material")));
        p.setSizeOption(c.getString(c.getColumnIndexOrThrow("size_option")));
        p.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
        p.setActive(c.getInt(c.getColumnIndexOrThrow("active")) == 1);
        return p;
    }

    // ============================================================
    //                         ORDER OPERATIONS
    // ============================================================

    public long placeOrder(Order o) {
        SQLiteDatabase db = getWritableDatabase();
        long now = System.currentTimeMillis();
        ContentValues v = new ContentValues();
        v.put("user_id", o.getUserId());
        v.put("product_id", o.getProductId());
        v.put("quantity", o.getQuantity());
        v.put("specifications", o.getSpecifications());
        v.put("custom_text", o.getCustomText());
        v.put("design_file_uri", o.getDesignFileUri());
        v.put("delivery_type", o.getDeliveryType());
        v.put("delivery_address", o.getDeliveryAddress());
        v.put("total_amount", o.getTotalAmount());
        v.put("status", Order.STATUS_PENDING);
        v.put("created_at", now);
        v.put("updated_at", now);
        return db.insert(T_ORDERS, null, v);
    }

    public boolean updateOrderStatus(long orderId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", status);
        v.put("updated_at", System.currentTimeMillis());
        return db.update(T_ORDERS, v, "id=?", new String[]{String.valueOf(orderId)}) > 0;
    }

    public List<Order> getOrdersForUser(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT o.*, p.name AS product_name, u.name AS customer_name " +
            "FROM " + T_ORDERS + " o " +
            "LEFT JOIN " + T_PRODUCTS + " p ON o.product_id = p.id " +
            "LEFT JOIN " + T_USERS + " u ON o.user_id = u.id " +
            "WHERE o.user_id = ? ORDER BY o.created_at DESC";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        List<Order> list = new ArrayList<>();
        while (c.moveToNext()) list.add(readOrderJoined(c));
        c.close();
        return list;
    }

    public List<Order> getAllOrders(String statusFilter) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder(
            "SELECT o.*, p.name AS product_name, u.name AS customer_name " +
            "FROM " + T_ORDERS + " o " +
            "LEFT JOIN " + T_PRODUCTS + " p ON o.product_id = p.id " +
            "LEFT JOIN " + T_USERS + " u ON o.user_id = u.id ");
        List<String> args = new ArrayList<>();
        if (statusFilter != null && !"All".equalsIgnoreCase(statusFilter)) {
            sql.append("WHERE o.status = ? ");
            args.add(statusFilter);
        }
        sql.append("ORDER BY o.created_at DESC");
        Cursor c = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        List<Order> list = new ArrayList<>();
        while (c.moveToNext()) list.add(readOrderJoined(c));
        c.close();
        return list;
    }

    public Order getOrder(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT o.*, p.name AS product_name, u.name AS customer_name " +
            "FROM " + T_ORDERS + " o " +
            "LEFT JOIN " + T_PRODUCTS + " p ON o.product_id = p.id " +
            "LEFT JOIN " + T_USERS + " u ON o.user_id = u.id " +
            "WHERE o.id = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(id)});
        Order o = null;
        if (c.moveToFirst()) o = readOrderJoined(c);
        c.close();
        return o;
    }

    private Order readOrderJoined(Cursor c) {
        Order o = new Order();
        o.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        o.setUserId(c.getLong(c.getColumnIndexOrThrow("user_id")));
        o.setProductId(c.getLong(c.getColumnIndexOrThrow("product_id")));
        int idx = c.getColumnIndex("product_name");
        if (idx >= 0) o.setProductName(c.getString(idx));
        idx = c.getColumnIndex("customer_name");
        if (idx >= 0) o.setCustomerName(c.getString(idx));
        o.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
        o.setSpecifications(c.getString(c.getColumnIndexOrThrow("specifications")));
        o.setCustomText(c.getString(c.getColumnIndexOrThrow("custom_text")));
        o.setDesignFileUri(c.getString(c.getColumnIndexOrThrow("design_file_uri")));
        o.setDeliveryType(c.getString(c.getColumnIndexOrThrow("delivery_type")));
        o.setDeliveryAddress(c.getString(c.getColumnIndexOrThrow("delivery_address")));
        o.setTotalAmount(c.getDouble(c.getColumnIndexOrThrow("total_amount")));
        o.setStatus(c.getString(c.getColumnIndexOrThrow("status")));
        o.setCreatedAt(c.getLong(c.getColumnIndexOrThrow("created_at")));
        o.setUpdatedAt(c.getLong(c.getColumnIndexOrThrow("updated_at")));
        return o;
    }

    public int countOrdersByStatus(String status) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        if (status == null) {
            c = db.rawQuery("SELECT COUNT(*) FROM " + T_ORDERS, null);
        } else {
            c = db.rawQuery("SELECT COUNT(*) FROM " + T_ORDERS + " WHERE status=?", new String[]{status});
        }
        int n = 0;
        if (c.moveToFirst()) n = c.getInt(0);
        c.close();
        return n;
    }

    // ============================================================
    //                       PROMOTION OPERATIONS
    // ============================================================

    public long addPromotion(Promotion p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = promotionValues(p);
        return db.insert(T_PROMOTIONS, null, v);
    }

    public boolean updatePromotion(Promotion p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = promotionValues(p);
        return db.update(T_PROMOTIONS, v, "id=?", new String[]{String.valueOf(p.getId())}) > 0;
    }

    public boolean deletePromotion(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_PROMOTIONS, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    private ContentValues promotionValues(Promotion p) {
        ContentValues v = new ContentValues();
        v.put("title", p.getTitle());
        v.put("description", p.getDescription());
        v.put("discount_percent", p.getDiscountPercent());
        v.put("valid_until", p.getValidUntil());
        v.put("active", p.isActive() ? 1 : 0);
        return v;
    }

    public Promotion getPromotion(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PROMOTIONS, null, "id=?",
            new String[]{String.valueOf(id)}, null, null, null);
        Promotion p = null;
        if (c.moveToFirst()) p = readPromotion(c);
        c.close();
        return p;
    }

    public List<Promotion> getActivePromotions() {
        List<Promotion> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PROMOTIONS, null, "active=1", null, null, null, "id DESC");
        while (c.moveToNext()) list.add(readPromotion(c));
        c.close();
        return list;
    }

    public List<Promotion> getAllPromotionsForAdmin() {
        List<Promotion> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PROMOTIONS, null, null, null, null, null, "active DESC, id DESC");
        while (c.moveToNext()) list.add(readPromotion(c));
        c.close();
        return list;
    }

    private Promotion readPromotion(Cursor c) {
        Promotion p = new Promotion();
        p.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        p.setTitle(c.getString(c.getColumnIndexOrThrow("title")));
        p.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        p.setDiscountPercent(c.getInt(c.getColumnIndexOrThrow("discount_percent")));
        p.setValidUntil(c.getString(c.getColumnIndexOrThrow("valid_until")));
        p.setActive(c.getInt(c.getColumnIndexOrThrow("active")) == 1);
        return p;
    }
}
