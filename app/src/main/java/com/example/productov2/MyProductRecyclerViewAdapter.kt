package com.example.productov2

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import coil.load
import com.example.productov2.data.dto.ProductDTO

import com.example.productov2.databinding.FragmentItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyProductRecyclerViewAdapter(
    private val values: MutableList<ProductDTO>
) : RecyclerView.Adapter<MyProductRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = values[position]
        holder.nameView.text = product.name
        holder.priceView.text = product.price.toString()
        holder.imageView.load(product.imageUrl)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.productName
        val priceView: TextView = binding.productPrice
        val imageView: ImageView = binding.imageView

        override fun toString(): String {
            return super.toString() + " '" + priceView.text + "'"
        }
    }

}