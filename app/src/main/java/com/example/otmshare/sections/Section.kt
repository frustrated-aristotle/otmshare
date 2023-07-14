package com.example.otmshare.sections

class Section (
    val seasonAndEpisode : String,
    val content : String,
    val url : String,
    val favAmount : Int = 0,
    val likeAmount : Int = 0,
    val isActive : Boolean = true,
    val id : Long = 0,
    var isLikeClicked : Boolean = false,
    var isSaveClicked : Boolean = false
    ){
}