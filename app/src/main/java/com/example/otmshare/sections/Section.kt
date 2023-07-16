package com.example.otmshare.sections

class Section (
    val seasonAndEpisode : String,
    val content : String,
    val url : String,
    val saveCount : Int = 0,
    val likeCount : Int = 0,
    val isActive : Boolean = true,
    val id : Long = 0,
    var isLikeClicked : Boolean = false,
    var isSaveClicked : Boolean = false
    ){
}