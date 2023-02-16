package com.example.musixhops.main.adapter




import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.musixhops.R
import com.example.musixhops.main.PlayerActivity
import com.example.musixhops.main.model.MusicModel


internal class CustomAdapter(private val context: Activity, private val data: ArrayList<MusicModel>) :
    ArrayAdapter<MusicModel?>(context,R.layout.list_row,
        data as List<MusicModel?>
    ), View.OnClickListener {

    // View lookup cache
    private class ViewHolder {
        var txtTitle: TextView? = null
    }

    private var lastPosition = -1


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var convertView = convertView
        val dataModel = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        val viewHolder: ViewHolder // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.list_row, parent, false)
            viewHolder.txtTitle = convertView.findViewById<View>(R.id.title) as TextView?
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }


        lastPosition = position
        if (dataModel != null) {
            val title = dataModel.title
            if(title.length>15){
                val tempStr = dataModel.title.subSequence(0,15)
                viewHolder.txtTitle?.text = StringBuilder().append(tempStr).append("...").toString()
            }else{
                viewHolder.txtTitle?.text = dataModel.title
            }
        }

        convertView!!.setOnClickListener {
//            Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(context,PlayerActivity::class.java)
            intent.putExtra("position",position)
            context.startActivity(intent)
        }

        // Return the completed view to render on screen
        return convertView!!
    }


    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}