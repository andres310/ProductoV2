package com.example.productov2

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.productov2.data.dto.ProductDTO
import com.example.productov2.databinding.FragmentItemListBinding
import com.example.productov2.databinding.FragmentSecondBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * A fragment representing a list of Items.
 */

private val ARG_ARRAY_LIST = "param1"

class ProductFragment : Fragment() {

    val args: ProductFragmentArgs by navArgs<ProductFragmentArgs>()

    var myProductList: MutableList<ProductDTO>? = null

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        args.productList.let {
            myProductList = Json.decodeFromString<MutableList<ProductDTO>>(it)
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = myProductList?.let { MyProductRecyclerViewAdapter(it) }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val binding: FragmentItemListBinding = FragmentItemListBinding.bind(view)
        //val binding: FragmentSecondBinding? = FragmentSecondBinding.bind(view)



        // Este no se para que sirve xd
        /*myProductList?.let {

        }*/
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}