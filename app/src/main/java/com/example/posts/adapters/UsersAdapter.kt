package com.example.posts.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.posts.activities.MainActivity.Companion.commentsList
import com.example.posts.databinding.UserRowBinding
import com.example.posts.models.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class UsersAdapter(private var list: MutableList<User>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    lateinit var context: Context
    inner class ViewHolder(val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       context=parent.context
       val binding = UserRowBinding
           .inflate(LayoutInflater.from(parent.context), parent, false)

       return ViewHolder(binding)
   }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(list[position]){
                val user = list.get(position)
                var commentsVal=setComments(user.id)

                binding.tvCommentdesc.text=commentsVal
                binding.txtUserUserid.text= "User Id :"+ user.userId.toString()
                binding.txtUserInfo1.text= user.title.toString()
                binding.txtUserInfo2.text= user.body.toString()

                binding.expandedView.visibility = if (this.expand) View.VISIBLE else View.GONE

                binding.cardLayout.setOnClickListener {
                    this.expand = !this.expand
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun setComments(id: String?): String? {
        var commentsInfo: String? = ""

        try {
            val jsonArray = JSONTokener(commentsList).nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {

                val obj = JSONObject(jsonArray[i].toString())
                val postId = obj.getString("postId")

                if(id== postId)  //checking postId from comments == id from posts
                {
                    val email = obj.getString("email")
                    val name = obj.getString("name")
                    val body = obj.getString("body")
                    commentsInfo = commentsInfo+ name+"\n"+email+"\n\n"+body+"\n\n***\n"

                }
            }

        }catch (e: JSONException){
            Log.d("Exception--", e.toString())
        }
        return commentsInfo
        }
}




