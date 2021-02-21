package me.kokokotlin.songlist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException
import java.lang.IllegalStateException


const val FILE_CHOOSEN_ID = 1

class SongChooseDialog : DialogFragment() {

    private var songUri: Uri? = null

    internal lateinit var listener: SongChooseDialogListener

    interface SongChooseDialogListener {
        fun onDialogPositiveClick(dialog: SongChooseDialog)
    }

    var result: Pair<String, Uri> = Pair("", Uri.EMPTY)
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        activity?.let {
            var builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val alertDialogView = inflater.inflate(R.layout.file_dialog, null)

            val songName = alertDialogView.findViewById<EditText>(R.id.txtDialogSongName)

            builder.setView(alertDialogView)
                .setPositiveButton(R.string.fileDialogSaveBtn
                ) { dialog, _ ->
                    if(songUri == null || songName.text.toString().isEmpty()) {
                        Toast.makeText(context,
                            getString(R.string.errorFileDialog),
                            Toast.LENGTH_LONG
                        ).show()
                        dialog?.cancel()
                    } else {
                        result = Pair(songName.text.toString(), songUri!!)
                        listener.onDialogPositiveClick(this)
                    }
                }
                .setNegativeButton(R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }

            // choose file btn
            alertDialogView
                .findViewById<Button>(R.id.fileDialogBtnAdd)
                .setOnClickListener {
                    // choose file
                    val chooseIntent = Intent(Intent.ACTION_GET_CONTENT)
                    chooseIntent.type = "*/*"
                    chooseIntent.addCategory(Intent.CATEGORY_OPENABLE)

                    startActivityForResult(
                        Intent.createChooser(chooseIntent, getString(R.string.midiChooserTitle)),
                        FILE_CHOOSEN_ID
                    )
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            FILE_CHOOSEN_ID -> {
                songUri = data?.data ?: return
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as SongChooseDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement SongChooseDialogListener")
        }
    }
}