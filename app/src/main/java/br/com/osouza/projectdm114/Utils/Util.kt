package br.com.osouza.projectdm114.Utils

import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun getDateFormated(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("pt", "BR"))
        val formatedDate = formatter.format(date)
        return formatedDate
    }

    fun getCurrentDate(): Date {
        val date = Calendar.getInstance().time
        return date
    }

}