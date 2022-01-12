package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.onLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTasks.removeAt(position)

                // Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                // Remove from saved list
                saveItems()
            }
        }

        loadItems()

        // Look up recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach the adapter to recyclerView to populate data
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field so the user can enter a task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        // and then set n onClickListener
        findViewById<Button>(R.id.button).setOnClickListener{
            // Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // Add the String to list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask);

            // Notify the data adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Reset text field
            inputTextField.setText("")

            // save the items when user adds new items
            saveItems()
        }
    }

    // Save the data that the user has inputted

    // Get the data file we need
    fun getDataFile(): File {
        // Each line in the file represents a specific task
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save the items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}