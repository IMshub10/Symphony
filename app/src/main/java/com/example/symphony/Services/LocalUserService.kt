package com.example.symphony.Services

import android.content.Context
import com.example.symphony.Models.PrefernecesModel

object LocalUserService {
    @JvmStatic
    fun getLocalUserFromPreferences(context: Context): PrefernecesModel {
        val pref = context.getSharedPreferences("LocalUser", 0)
        val prefernecesModel = PrefernecesModel()
        prefernecesModel.Phone = pref.getString("Phone", null)
        prefernecesModel.ImageUrl = pref.getString("ImageUrl", null)
        prefernecesModel.Key = pref.getString("Key", null)
        prefernecesModel.Name=pref.getString("Name",null)
        return prefernecesModel
    }

    fun deleteLocalUserFromPreferences(context: Context): Boolean {
        return try {
            val pref = context.getSharedPreferences("LocalUser", 0)
            val editor = pref.edit()
            editor.clear()
            editor.apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }
}