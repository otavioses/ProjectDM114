package br.com.osouza.projectdm114.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {
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