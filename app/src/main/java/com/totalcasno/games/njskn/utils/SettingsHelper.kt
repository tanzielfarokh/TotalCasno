package com.totalcasno.games.njskn.utils

import android.content.Context
import android.content.SharedPreferences

object SettingsHelper {
    private const val PREFS_NAME = "game_settings"
    private const val KEY_SLOT_COUNT = "slot_count"
    private const val KEY_THEME = "theme"
    
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveSlotCount(context: Context, count: Int) {
        getPreferences(context).edit().putInt(KEY_SLOT_COUNT, count).apply()
    }
    
    fun getSlotCount(context: Context): Int {
        return getPreferences(context).getInt(KEY_SLOT_COUNT, 3)
    }
    
    fun saveTheme(context: Context, theme: String) {
        getPreferences(context).edit().putString(KEY_THEME, theme).apply()
    }
    
    fun getTheme(context: Context): String {
        return getPreferences(context).getString(KEY_THEME, "classic") ?: "classic"
    }
}

enum class SlotTheme(val displayName: String, val symbols: List<String>) {
    CLASSIC("Classic", listOf("🎰", "💎", "🍀", "⭐", "🔔", "🎲", "💰", "🎁")),
    CARDS("Cards", listOf("♠️", "♥️", "♦️", "♣️", "🃏", "🎴", "🀄", "🂠")),
    MONEY("Money", listOf("💵", "💰", "💳", "💎", "🏆", "🪙", "💸", "🤑")),
    ANIMALS("Animals", listOf("🐶", "🐱", "🐻", "🦁", "🐼", "🦊", "🐯", "🐨")),
    GEMS("Gems", listOf("💎", "💍", "👑", "🔮", "✨", "🌟", "⭐", "💫"));
    
    companion object {
        fun fromString(name: String): SlotTheme {
            return values().find { it.name.equals(name, ignoreCase = true) } ?: CLASSIC
        }
    }
}

