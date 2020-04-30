package com.e.ekanektask.Utils

import android.content.Context
import android.content.SharedPreferences

const val SAVE_LIST = "SaveList "
const val PREFERENCES_KEY = "Save_History "
const val SAVE_STRING = "Save_String "
class Prefrences {
    companion object {
        var preferences: SharedPreferences? = null
        var preferencesEditor: SharedPreferences.Editor? = null
        var prefrences: Prefrences? = null
        var context: Context? = null
        fun getInstance(context: Context): Prefrences? {
            if (preferences == null) {
                prefrences =
                    Prefrences()
                preferences = context.getSharedPreferences(
                    PREFERENCES_KEY, Context.MODE_PRIVATE)
                preferencesEditor =
                    context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE).edit()
                Companion.context = context
                return prefrences
            } else return prefrences
        }
    }

    fun saveData(hashSet: HashSet<String>) {
        preferencesEditor?.putStringSet(
            SAVE_LIST, hashSet)?.commit()
    }
    fun getData(): HashSet<String>{
        return preferences?.getStringSet(
            SAVE_LIST, mutableSetOf()) as HashSet<String>
    }

    fun saveStringData(searchKey : String , string: String){
        preferencesEditor?.putString(searchKey,string)?.commit()
    }
    fun getStringData(searchKey: String): String{
       return preferences?.getString(searchKey,"{}").toString()?:"{}"
    }
}