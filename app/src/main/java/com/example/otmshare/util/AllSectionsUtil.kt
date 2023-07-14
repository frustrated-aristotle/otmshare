package com.example.otmshare.util

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat

fun makeTitleFromEpisodeAndSeasonString(episodeAndSeasonString : String) : String
{
    val numberParts = episodeAndSeasonString.split(".")
    val title = "Sezon ${numberParts[0]} - Bölüm ${numberParts[1]}  |  ${numberParts[2]}"
    return title
}

fun openSpotifyWithPodcast(url: String, view : View)
{
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.setPackage("com.spotify.music") // Spotify uygulamasýný belirtmek için
    ContextCompat.startActivity(view.context, intent, null)
}