package com.example.otmshare.util

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat

fun makeTitleFromEpisodeAndSeasonString(episodeAndSeasonString : String) : String
{
    val numberParts = episodeAndSeasonString.split(".")
    val title = "Sezon ${numberParts[0]} - B�l�m ${numberParts[1]}  |  ${numberParts[2]}"
    return title
}

fun openSpotifyWithPodcast(url: String, view : View)
{
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.setPackage("com.spotify.music") // Spotify uygulamas�n� belirtmek i�in
    ContextCompat.startActivity(view.context, intent, null)
}