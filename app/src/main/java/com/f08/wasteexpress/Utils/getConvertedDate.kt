package com.f08.wasteexpress.Utils

import android.content.Context

object getConvertedDate {
    private const val MILLIS_PAR_SECONDE = 1000
    private const val MILLIS_PAR_MINUTE = 60 * MILLIS_PAR_SECONDE
    private const val MILLIS_PAR_HEURE = 60 * MILLIS_PAR_MINUTE
    private const val MILLIS_PAR_JOUR = 24 * MILLIS_PAR_HEURE

    @JvmStatic
    fun obtenirTempsEcoule(temps: Long, ctx: Context): String? {
        var tempsMillis = temps
        if (tempsMillis < 1000000000000L) {
            // Si le timestamp est donné en secondes, le convertir en millisecondes
            tempsMillis *= 1000
        }

        val maintenant = System.currentTimeMillis()
        if (tempsMillis > maintenant || tempsMillis <= 0) {
            return null
        }

        val diff = maintenant - tempsMillis
        return when {
            diff < MILLIS_PAR_MINUTE -> {
                "À l'instant"
            }
            diff < 2 * MILLIS_PAR_MINUTE -> {
                "Il y a une minute"
            }
            diff < 50 * MILLIS_PAR_MINUTE -> {
                "Il y a ${diff / MILLIS_PAR_MINUTE} minutes"
            }
            diff < 90 * MILLIS_PAR_MINUTE -> {
                "Il y a une heure"
            }
            diff < 24 * MILLIS_PAR_HEURE -> {
                "Il y a ${diff / MILLIS_PAR_HEURE} heures"
            }
            diff < 48 * MILLIS_PAR_HEURE -> {
                "Hier"
            }
            else -> {
                "Il y a ${diff / MILLIS_PAR_JOUR} jours"
            }
        }
    }
}
