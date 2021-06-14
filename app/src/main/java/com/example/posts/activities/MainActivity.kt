package com.example.posts.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts.adapters.UsersAdapter
import com.example.posts.databinding.ActivityMainBinding
import com.example.posts.models.User
import com.example.posts.viewmodels.UserViewModel
import com.example.posts.viewmodelsfactories.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_row.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import com.example.posts.api.ApiClient.COMMENTS_URL

class MainActivity : AppCompatActivity() {

    // view binding for the activity
    private var baseBinding: ActivityMainBinding? = null
    private val binding get() = baseBinding!!
    private lateinit var listUsers: MutableList<User>
    private lateinit var adapter: UsersAdapter
    lateinit var filteredList: MutableList<User>
    var commentsArr : JSONArray? =null

    companion object {
        var commentsList = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getComments(COMMENTS_URL)  // getting comments -- background task

        baseBinding!!.recyclerMain.layoutManager=LinearLayoutManager(this)
        listUsers = mutableListOf<User>()
        adapter = UsersAdapter(listUsers)
        baseBinding!!.recyclerMain.adapter=adapter

        user_search.setFocusable(false);  // serach
        user_search.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                user_search.setFocusableInTouchMode(true)
                return false
            }
        })

        //Search option
        user_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filteredList  = arrayListOf()
                if (p0.toString() != "") {
                    for (item in listUsers)
                    {
                        if(item.title.toString().lowercase().contains(p0.toString().lowercase()))
                        {
                            filteredList.add(item)
                        }
                    }
                    adapter = UsersAdapter(filteredList)
                    baseBinding!!.recyclerMain.adapter=adapter
                } else {
                    adapter = UsersAdapter(listUsers)
                    baseBinding!!.recyclerMain.adapter=adapter
                }
            }
        })

       val userViewModel = ViewModelProviders.of(this, UserViewModelFactory(this)).get(UserViewModel::class.java)
        userViewModel.getData().observe(this,object: Observer<ArrayList<User>> {
            override fun onChanged(t: ArrayList<User>?)
            {
                listUsers.clear()
                t?.let { listUsers.addAll(it) }
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun getComments(url: String) {

        val url:URL? = try {
            URL(url)
        }catch (e: MalformedURLException){
            Log.d("Exception", e.toString())
            null
        }
        // io dispatcher for networking operation
        lifecycleScope.launch(Dispatchers.IO){
            url?.getString()?.apply {
                // default dispatcher for json parsing
                withContext(Dispatchers.Default){

                    val jsonArray = JSONTokener(this@apply).nextValue() as JSONArray
                    commentsArr=jsonArray
                    commentsList= commentsArr.toString()

                    withContext(Dispatchers.Main){
                        commentsList?.forEach {
                        }
                    }
                }
            }
        }
    }

    // extension function to get string data from url
    fun URL.getString(): String? {
        val stream = openStream()
        return try {
            val r = BufferedReader(InputStreamReader(stream))
            val result = StringBuilder()
            var line: String?
            while (r.readLine().also { line = it } != null) {
                result.append(line).appendln()
            }
            result.toString()
        }catch (e: IOException){
            e.toString()
        }
    }
}


