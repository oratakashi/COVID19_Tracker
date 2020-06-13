package com.oratakashi.covid19.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.oratakashi.covid19.BuildConfig

class Database(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        const val DB_NAME = "db."+ BuildConfig.APPLICATION_ID
        const val DB_VERSION = BuildConfig.VERSION_CODE

        const val TABLE_CONFIRM = "confirmed"
        const val TABLE_RECOVERED = "recovered"
        const val TABLE_DEATH = "death"
        const val TABLE_PROVINCE = "province"
        const val TABLE_TIMELINE = "timeline"
        const val TABLE_HOTLINE = "hotline"
        const val TABLE_HOSPITAL = "hospital"

        /**
         * Attribute Table
         */
        const val id : String = "id"
        const val provinceState : String = "provinceState"
        const val countryRegion : String = "countryRegion"
        const val lat : String = "lat"
        const val long : String = "long"
        const val confirmed : String = "confirmed"
        const val recovered : String = "recovered"
        const val deaths : String = "deaths"
        const val case : String = "cases"
        const val date : String = "date"
        const val phone : String = "phone"
        const val province : String = "province"
        const val name : String = "name"
        const val address : String = "address"
        const val region : String = "region"
        const val type : String = "type"
    }

    private val createTableConfirm = "CREATE TABLE $TABLE_CONFIRM (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$provinceState TEXT," +
            "$countryRegion TEXT," +
            "$lat TEXT," +
            "$long TEXT," +
            "$confirmed INTEGER," +
            "$recovered INTEGER," +
            "$deaths INTEGER" +
            ")"
    private val createTableRecovered = "CREATE TABLE $TABLE_RECOVERED (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$provinceState TEXT," +
            "$countryRegion TEXT," +
            "$lat TEXT," +
            "$long TEXT," +
            "$confirmed INTEGER," +
            "$recovered INTEGER," +
            "$deaths INTEGER" +
            ")"
    private val createTableDeath = "CREATE TABLE $TABLE_DEATH (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$provinceState TEXT," +
            "$countryRegion TEXT," +
            "$lat TEXT," +
            "$long TEXT," +
            "$confirmed INTEGER," +
            "$recovered INTEGER," +
            "$deaths INTEGER" +
            ")"
    private val createTableProvince = "CREATE TABLE $TABLE_PROVINCE (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$provinceState TEXT," +
            "$lat TEXT," +
            "$long TEXT," +
            "$confirmed INTEGER," +
            "$recovered INTEGER," +
            "$deaths INTEGER" +
            ")"

    private val createTableTimeline = "CREATE TABLE $TABLE_TIMELINE (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$date DATE," +
            "$case INTEGER," +
            "$confirmed INTEGER," +
            "$recovered INTEGER," +
            "$deaths INTEGER" +
            ")"
    private val createTableHotline = "CREATE TABLE $TABLE_HOTLINE (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$province TEXT," +
            "$phone TEXT" +
            ")"
    private val createTableHospital = "CREATE TABLE $TABLE_HOSPITAL (" +
            "$id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$name TEXT," +
            "$address TEXT," +
            "$phone TEXT," +
            "$region TEXT," +
            "$type TEXT," +
            "$lat TEXT," +
            "$long TEXT" +
            ")"

    var context: Context? = null
    var db: SQLiteDatabase? = null

    init {
        this.context = context
        db = this.writableDatabase
    }

    fun getCursor(query : String) : Cursor {
        return db!!.rawQuery(query, null)
    }

    fun insert(content : ContentValues, table : String) : Long{
        return db!!.insert(table, null, content)
    }

    fun delete(table: String){
        db!!.delete(table, null, null)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(createTableConfirm)
        db.execSQL(createTableRecovered)
        db.execSQL(createTableDeath)
        db.execSQL(createTableProvince)
        db.execSQL(createTableTimeline)
        db.execSQL(createTableHotline)
        db.execSQL(createTableHospital)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONFIRM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECOVERED")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DEATH")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROVINCE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TIMELINE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HOTLINE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HOSPITAL")

        db.execSQL(createTableConfirm)
        db.execSQL(createTableRecovered)
        db.execSQL(createTableDeath)
        db.execSQL(createTableProvince)
        db.execSQL(createTableTimeline)
        db.execSQL(createTableHotline)
        db.execSQL(createTableHospital)
    }
}