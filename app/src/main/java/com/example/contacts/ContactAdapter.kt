package com.example.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val contactName: TextView = root.name
    val contactPhoneNumber: TextView = root.phoneNumber
}

class UserAdapter(
    private val users: List<Contact>,
    val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
        holder.root.setOnClickListener {
            onClick(users[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.contactName.text =
            users[position].name
        holder.contactPhoneNumber.text =
            users[position].phoneNumber
    }

}

