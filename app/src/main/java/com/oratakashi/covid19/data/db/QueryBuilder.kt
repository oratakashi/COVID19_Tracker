package com.oratakashi.covid19.data.db

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.hospital.DataHospital
import com.oratakashi.covid19.data.model.hotline.DataHotline
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.timeline.DataTimeline
import com.oratakashi.covid19.root.App
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Query Builder Powered By Oratakashi
 */

abstract class QueryBuilder {

    /**
     * Core Queries
     */
    var table_name = ""
    var select = ""
    var where = ""
    var groupBy = ""
    var orderBy = ""
    var limit = ""
    var cursor : Cursor?= null

    /**
     * Join Condition
     */
    var join_table : MutableList<String> = ArrayList()
    var where_table : MutableList<String> = ArrayList()
    var index = 0

    /**
     * Init Framework
     * Operator2 Untuk Select, Untuk versi ini Where hanya bisa satu, tidak bisa dua atau lebih
     * Jika Ingin menggunakan lebih dari satu where bisa menggunakan fungsi rawQuery
     *
     * Contoh :
     * SELECT * FROM Content
     * Cukup memakai from("content") lalu get() sudah mendapatkan hasil Query
     * untuk medapatkan resultnya panggil row_content() atau array_content()
     */

    fun select(select : String){
        when(this.select.isEmpty()){
            true -> {
                this.select = select
            }
            false -> {
                this.select += ", $select"
            }
        }
    }

    fun from(table : String){
        table_name = table
    }

    fun join(table: String, where : String){
        join_table.add(index, table)
        where_table.add(index, where)
        index += 1
    }

    fun where(column: String, where : String, operator : String?= null){
        if(this.where.isEmpty()){
            if(operator == null){
                this.where = "$column = '$where'"
            }else if(operator == "like"){
                this.where = "$column ${operator.toUpperCase()} '%$where%'"
            }
        }else{
            if(operator == null){
                this.where += " and $column = '$where'"
            }else if(operator == "like"){
                this.where += " and $column ${operator.toUpperCase()} '%$where%'"
            }
        }
    }

    fun groupBy(group : String?){
        if(group != null){
            this.groupBy = group
        }else{
            this.groupBy = ""
        }

    }

    fun orderBy(order : String, column: String){
        when(order.isEmpty() || column.isEmpty()){
            true -> {
                this.orderBy = ""
            }
            false -> {
                this.orderBy = " ORDER BY $column $order"
            }
        }
    }

    fun limit(limit : Int, offset : Int = -1){
        when(offset == -1){
            true -> {
                this.limit = "$limit"
            }
            false -> {
                this.limit = "$limit OFFSET $offset"
            }
        }
    }

    fun close(){
        index = 0
        table_name = ""
        where = ""
        join_table.clear()
        where_table.clear()
        groupBy = ""
        select = ""
        limit = ""
        orderBy = ""
    }

    fun get(){
        var join = ""
        var where_join = ""
        when(join_table.isEmpty()){
            true -> {
                join = ""
                where_join = ""
            }
            false -> {
                this.join_table.forEach {
                    join += ", $it"
                }
                this.where_table.forEach {
                    where_join += "and $it"
                }
            }
        }

        var query = "SELECT ${when(select.isEmpty()){
            true -> {
                "*"
            }
            false -> {
                select
            }
        }
        } from $table_name"

        if(this.where.isEmpty() && join_table.isEmpty()){
            query += ""
        }else if(this.where.isNotEmpty() && join_table.isEmpty()){
            query += " WHERE ${this.where}"
        }else if(this.where.isNotEmpty() && join_table.isNotEmpty()){
            query += "$join WHERE ${this.where} $where_join"
        }

        if(this.groupBy.isEmpty()){
            query += ""
        }else{
            query += " GROUP BY ${this.groupBy}"
        }

        if(this.orderBy.isEmpty()){
            query += ""
        }else{
            query += orderBy
        }

        cursor = App.db!!.getCursor( query )
    }

    fun cursor() : Cursor {
        return cursor!!
    }

    fun rawQuery(query : String){
        cursor = App.db!!.getCursor(query)
    }

    /**
     * Query Insert menggunakan Overloading method
     */
    fun insert(data : DataConfirm){
        val values = ContentValues()
        values.put(Database.provinceState, data.provinceState)
        values.put(Database.countryRegion, data.countryRegion)
        values.put(Database.lat, data.lat)
        values.put(Database.long, data.long)
        values.put(Database.confirmed, data.confirmed)
        values.put(Database.recovered, data.recovered)
        values.put(Database.deaths, data.deaths)

        App.db!!.insert(values, Database.TABLE_CONFIRM)
    }

    fun insert(data : DataRecovered){
        val values = ContentValues()
        values.put(Database.provinceState, data.provinceState)
        values.put(Database.countryRegion, data.countryRegion)
        values.put(Database.lat, data.lat)
        values.put(Database.long, data.long)
        values.put(Database.confirmed, data.confirmed)
        values.put(Database.recovered, data.recovered)
        values.put(Database.deaths, data.deaths)

        App.db!!.insert(values, Database.TABLE_RECOVERED)
    }

    fun insert(data : DataDeath){
        val values = ContentValues()
        values.put(Database.provinceState, data.provinceState)
        values.put(Database.countryRegion, data.countryRegion)
        values.put(Database.lat, data.lat)
        values.put(Database.long, data.long)
        values.put(Database.confirmed, data.confirmed)
        values.put(Database.recovered, data.recovered)
        values.put(Database.deaths, data.deaths)

        App.db!!.insert(values, Database.TABLE_DEATH)
    }

    fun insert(data : DataProvince){
        val values = ContentValues()
        values.put(Database.provinceState, data.attributes.provinsi)
        values.put(Database.lat, data.geometry.lang)
        values.put(Database.long, data.geometry.lat)
        values.put(Database.confirmed, data.attributes.confirm)
        values.put(Database.recovered, data.attributes.recovered)
        values.put(Database.deaths, data.attributes.death)

        App.db!!.insert(values, Database.TABLE_PROVINCE)
    }

    fun insert(data : DataTimeline){
        val values = ContentValues()
        val date =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(data.attributes.tanggal!!))
        val array = date.split(" ")
        values.put(Database.date, array[0])
        values.put(Database.case, data.attributes.case)
        values.put(Database.confirmed, data.attributes.confirm)
        values.put(Database.recovered, data.attributes.recovered)
        values.put(Database.deaths, data.attributes.death)

        App.db!!.insert(values, Database.TABLE_TIMELINE)
    }

    fun insert(data : DataHotline){
        val values = ContentValues()

        values.put(Database.province, data.province)
        values.put(Database.phone, data.phone)

        App.db!!.insert(values, Database.TABLE_HOTLINE)
    }

    fun insert(data : DataHospital){
        val values = ContentValues()

        values.put(Database.name, data.attributes.nama)
        values.put(Database.address, data.attributes.alamat)
        values.put(Database.phone, data.attributes.telepon)
        values.put(Database.region, data.attributes.wilayah)
        values.put(Database.type, data.attributes.tipe)
        values.put(Database.lat, data.geometry.lat)
        values.put(Database.long, data.geometry.lang)

        App.db!!.insert(values, Database.TABLE_HOSPITAL)
    }

    fun delete(table : String){
        App.db!!.delete(table)
    }
}