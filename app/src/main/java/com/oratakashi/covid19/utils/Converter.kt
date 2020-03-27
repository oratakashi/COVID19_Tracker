package com.oratakashi.covid19.utils

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

            format = SimpleDateFormat("dd-MMMM-yyyy")

            return format.format(newDate)
        }

        fun decimalFormat(number : Int): String{
            val numberFormat = DecimalFormat("00")
            return numberFormat.format(number.toLong())
        }
    }
}