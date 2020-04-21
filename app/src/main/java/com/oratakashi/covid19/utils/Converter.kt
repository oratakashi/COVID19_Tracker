package com.oratakashi.covid19.utils

import android.util.Log
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Converter {
    companion object{
        @Throws(ParseException::class)
        fun dateFormat(date: String) : String{
            var format = SimpleDateFormat("dd-MM-yyyy")
            var newDate : Date? = null

            newDate = format.parse(date)

            format = SimpleDateFormat("yyyy-MM-dd")

            return format.format(newDate)
        }

        @Throws(ParseException::class)
        fun dateFormatIndo(date: String) : String{
            var format = SimpleDateFormat("yyyy-MM-dd")
            var newDate : Date? = null

            newDate = format.parse(date)

            format = SimpleDateFormat("dd MMMM yyyy")

            return format.format(newDate)
        }

        fun decimalFormat(number : Int): String{
            val numberFormat = DecimalFormat("00")
            return numberFormat.format(number.toLong())
        }

        fun numberConvert(number: Number): String? {
            Log.e("numberConvert", "number : $number")
            val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
            val numValue: Long = number.toLong()
            val value = Math.floor(Math.log10(numValue.toDouble())).toInt()
            val base = value / 3
            return if (value >= 3 && base < suffix.size) {
                DecimalFormat("#0.0").format(
                    numValue / Math.pow(
                        10.0,
                        base * 3.toDouble()
                    )
                ) + suffix[base]
            } else {
                DecimalFormat("#,##0").format(numValue)
            }
        }

        fun persentase(count : Float, total : Float) : String{
            val value = ((count / total)*100)

            return DecimalFormat("#.##").format(value)+"%"
        }

        fun numberFormat(number : Int) : String{
            return DecimalFormat("###,###,###,###").format(number)
        }
    }
}