package com.codersguidebook.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val mainActivity: MainActivity):
    RecyclerView.Adapter<NoteAdapter.ViewHolderNote>() {

    var noteList = mutableListOf<Note>()

    inner class ViewHolderNote(view: View) :
        RecyclerView.ViewHolder(view) {

        internal var title = view.findViewById<View>(R.id.viewTitle) as TextView
        internal var contents = view.findViewById<View>(R.id.viewContents) as TextView

        init {
            view.isClickable = true
            view.setOnClickListener {
                mainActivity.showNote(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNote {
        return ViewHolderNote(LayoutInflater.from(parent.context).inflate(R.layout.note_preview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderNote, position: Int) {
        val note = noteList[position]

        holder.title.text = note.title
        holder.contents.text = if (note.contents.length < 15) note.contents
        else note.contents.substring(0, 15) + "..."
    }

    override fun getItemCount(): Int = noteList.size
}
