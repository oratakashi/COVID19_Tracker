package com.oratakashi.covid19.ui.faq_menus.faq

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.model.faq.DataFaq
import com.oratakashi.covid19.utils.AssetsManager
import org.json.JSONException

class FaqViewModel : ViewModel(){
    val dataFaq = MutableLiveData<List<DataFaq>>()

    @Throws(JSONException::class)
    fun getFaq(context: Context){
        val data : MutableList<DataFaq> = ArrayList()

        for(i in 0 until AssetsManager.getJson(context, "faq").length()){
            val json = AssetsManager.getJson(context, "faq").getJSONObject(i)
             data.add(
                 DataFaq(
                     json.getString("title"),
                     json.getString("message")
                 )
             )
        }

        dataFaq.value = data
    }
}