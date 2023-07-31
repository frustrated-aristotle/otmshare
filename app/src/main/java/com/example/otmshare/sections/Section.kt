package com.example.otmshare.sections

class Section (
    val seasonAndEpisode : String,
    val content : String,
    val url : String,
    var saveCount : Int = 0,
    var likeCount : Int = 0,
    val isActive : Boolean = true,
    val id : Long = 0,
    var isLikeClicked : Boolean = false,
    var isSaveClicked : Boolean = false
    ){
}