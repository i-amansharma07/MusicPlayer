package com.example.musixhops.main.model

// Data class do not have methods like other classes it only have properties.

data class MusicModel(
    val id : String,
    val title : String,
    val album : String,
    val artist : String,
    val duration : String,
    val path:String

)
