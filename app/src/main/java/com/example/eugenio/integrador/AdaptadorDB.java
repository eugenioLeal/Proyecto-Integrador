package com.example.eugenio.integrador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//

public class AdaptadorDB {

    static final String  LLAVE_EMAIL = "email";
    static final String  LLAVE_USUARIO = "usuario";
    static final String  LLAVE_PASSWORD = "password";
    static final String ETIQUETA = "AdaptadorDB";
    static final String NOMBRE_BD = "usuarios";
    static final String BD_TABLA = "usuarios";
    static final int VERSION_DB = 2;

    static final String CREAR_BD =
            "create table usuarios ("
                    + "email text primary key not null,  usuario text not null, password text not null);";

    final Context contexto;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public AdaptadorDB(Context ctx)
    {
        this.contexto = ctx;
        DBHelper = new DatabaseHelper(contexto);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, NOMBRE_BD, null, VERSION_DB);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(CREAR_BD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(ETIQUETA, "Actualizando la version de la Base de Datos de " + oldVersion + " a "
                    + newVersion + ", este proceso eliminarÃ¡ los registros de la versiÃ³n anterior");
            db.execSQL("DROP TABLE IF EXISTS usuarios");
            onCreate(db);
        }
    }
    public void dropTable() {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
    }
    //--- Abrimos la BD ---
    public AdaptadorDB open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //--- Cerramos la BD ---
    public void close()
    {
        DBHelper.close();
    }

    //--- Insertamos registros a la tabla usuarios ---
    public long insertaUsuario( String email, String username, String password)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(LLAVE_PASSWORD, password);
        initialValues.put(LLAVE_EMAIL, email);
        initialValues.put(LLAVE_USUARIO, username);
        return db.insert(BD_TABLA, null, initialValues);
    }

    //--- Borra un Usuario en particular ---
    public boolean borraUsuario(String email)
    {
        return db.delete(BD_TABLA, LLAVE_EMAIL + "=" + email, null) > 0;
    }

    //--- Recuperamos todos los registros de la tabla ---
    public Cursor obtenTodosLosUsuarios()
    {
        return db.query(BD_TABLA, new String[] {
                LLAVE_EMAIL}, null, null, null, null, null);
    }
    public boolean login(String email,String password) {
        String sql = "SELECT EXISTS (SELECT * FROM usuarios WHERE ("+LLAVE_EMAIL+"='"+email+"' or "+ LLAVE_USUARIO + " = '"+email+"')"+
                " and "+LLAVE_PASSWORD+"='"+password+"' LIMIT 1)";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        // cursor.getInt(0) is 1 if column with value exists
        if (cursor.getInt(0) == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    //--- Recuperamos un registro de cliente en particular ---
    public Cursor obtieneUnUsuario(String email) throws SQLException
    {
        Cursor mCursor =
                db.query(true, BD_TABLA, new String[] {
                                LLAVE_EMAIL,LLAVE_PASSWORD}, LLAVE_EMAIL + "=" + email, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //--- Actualizamos un registro  ---
    public boolean actualizaCliente(String email,String password )
    {
        ContentValues args = new ContentValues();
        args.put(LLAVE_PASSWORD, password);
        return db.update(BD_TABLA, args, LLAVE_EMAIL + "=" + email, null) > 0;
    }

}