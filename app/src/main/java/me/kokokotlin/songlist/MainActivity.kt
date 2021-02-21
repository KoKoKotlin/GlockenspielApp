package me.kokokotlin.songlist

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment

class MainActivity : AppCompatActivity(), SongChooseDialog.SongChooseDialogListener {

    private val itemsWithUri = HashMap<String, Uri>()
    private val items = ArrayList<String>()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val mainContentList = findViewById<ListView>(R.id.mainContentList)

        adapter = ArrayAdapter (
            applicationContext,
            android.R.layout.simple_list_item_1,
            items
        )

        mainContentList.adapter = adapter

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            // add a new song to the list
            val dialog = SongChooseDialog()
            dialog.show(supportFragmentManager, "file_dialog")
        }
    }

    override fun onDialogPositiveClick(dialog: SongChooseDialog) {
        // insert into list
        items.add(dialog.result.first)
        itemsWithUri[dialog.result.first] = dialog.result.second
        adapter.notifyDataSetChanged()
    }
}