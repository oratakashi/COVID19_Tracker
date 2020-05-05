package com.oratakashi.covid19.utils

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream

class AssetsManager {
    companion object{
        @Throws(IOException::class)
        fun getJson(context: Context, name : String) : JSONArray{
            val inputStream : InputStream = context.assets.open("$name.json")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer)

            return JSONArray(json)
        }
    }
}