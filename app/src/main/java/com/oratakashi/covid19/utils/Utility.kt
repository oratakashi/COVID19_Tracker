package com.oratakashi.covid19.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

class Utility {
    companion object{
        fun dismisKeyboard(activity : Activity){
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        fun showKeyboard(activity: Activity){
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
}