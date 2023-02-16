package com.example.musixhops.main


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.musixhops.databinding.ActivityMainBinding
import com.example.musixhops.main.adapter.CustomAdapter
import com.example.musixhops.main.model.MusicModel
import java.io.File


/*
    for requesting a permission in android we need to perform three steps :
    1. mention the request in manifest file
    2. ask for the permission
    3 handle the permission
 */


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val EXTERNAL_STORAGE = 100

    companion object{
        var musicList  = ArrayList<MusicModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)
        binding = view

        requestRuntimePermissions()

        musicList = getAllMusic()

        val adapter = CustomAdapter(this, musicList)
        binding.listView.adapter = adapter


    }


    // cursor helps us to access data inside the local storage
    // any kind of file
    @SuppressLint("Range")
    private fun getAllMusic():ArrayList<MusicModel>{
        val tempList = ArrayList<MusicModel>()
        // to tell cursor which type of file we want
        val selection  = MediaStore.Audio.Media.IS_MUSIC+ " !=0"
        // to tell cursor what we want from that file
        val projection =  arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE
                ,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST
                ,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA)

        val cursor = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection
            ,selection,null,MediaStore.Audio.Media.DATE_ADDED+ " DESC"
            ,null)


        if(cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    val music = MusicModel(id,title,album,artist,duration,path)
                    val file = File(music.path)
                    if(file.exists()){
                        tempList.add(music)
                    }

                }while (cursor.moveToNext())
                cursor.close()
            }
        }

        return tempList
    }




    private fun requestRuntimePermissions(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==EXTERNAL_STORAGE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show()
            }else{
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),EXTERNAL_STORAGE)
            }
        }
    }


}




