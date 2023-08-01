package com.codersguidebook.notes

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.codersguidebook.notes.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val FILEPATH = "notes.json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            NewNote().show(supportFragmentManager, "")
        }

        adapter.noteList = retrieveNotes()
        adapter.notifyItemRangeInserted(0, adapter.noteList.size)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun createNewNote(note: Note) {
        adapter.noteList.add(note)
        adapter.notifyItemInserted(adapter.noteList.size -1)
        saveNotes()
    }

    fun deleteNote(index: Int) {
        adapter.noteList.removeAt(index)
        adapter.notifyItemRemoved(index)
        saveNotes()
    }

    fun showNote(index: Int) {
        val dialog = ShowNote(adapter.noteList[index], index)
        dialog.show(supportFragmentManager, null)
    }

    private fun saveNotes() {
        val notes = adapter.noteList
        val gson = GsonBuilder().create()
        val jsonNotes = gson.toJson(notes)

        val outputStream = openFileOutput(FILEPATH, Context.MODE_PRIVATE)
        OutputStreamWriter(outputStream).use { writer ->
            writer.write(jsonNotes)
        }
    }

    private fun retrieveNotes(): MutableList<Note> {
        val noteList = mutableListOf<Note>()
        if (getFileStreamPath(FILEPATH).isFile) {
            val fileInput = openFileInput(FILEPATH)
            BufferedReader(InputStreamReader(fileInput)).use { reader ->
                val stringBuilder = StringBuilder()
                for (line in reader.readLine()) stringBuilder.append(line)

                if (stringBuilder.isNotEmpty()){
                    val listType = object : TypeToken<List<Note>>() {}.type
                    noteList.addAll(Gson().fromJson(stringBuilder.toString(), listType))
                }
            }
        }
        return noteList
    }
}