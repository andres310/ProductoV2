package com.example.productov2

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.productov2.data.dto.ProductDTO
import com.example.productov2.data.model.Product
import com.example.productov2.databinding.FragmentSecondBinding
import com.example.productov2.view.FormViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val productList: MutableList<ProductDTO> = ArrayList()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        permissionHandler()
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    // Viewmodel
    private val formViewModel: FormViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Name
        binding.nameTextInputLayout.editText?.doAfterTextChanged { textInput ->
            formViewModel.product.name = textInput.toString()
        }
        binding.nameTextInputLayout.editText?.setText(formViewModel.product.name)

        // Price
        binding.priceTextInputLayout.editText?.doAfterTextChanged { textInput ->
            if (!textInput.toString().isEmpty())
                formViewModel.product.price = textInput.toString().toFloat()
            else
                formViewModel.product.price = 0F
        }
        binding.priceTextInputLayout.editText?.setText(formViewModel.product.price.toString())

        binding.buttonList.setOnClickListener {
            // Hace el JSON del array de productos y lo manda al ProductFragment
            var productListJSON = Json.encodeToString(productList)

            println(productListJSON)

            // Va hacia el siguiente fragmento de manera segura
            val destination = SecondFragmentDirections.actionSecondFragmentToProductFragment(productListJSON)
            findNavController().navigate(destination)
        }

        // Añade nuevo producto a la lista
        binding.buttonAdd.setOnClickListener {
            var productModel = formViewModel.product
            var productDto = ProductDTO()
            productDto.fillUpDto(productModel)
            productList.add(productDto)

            Snackbar.make(it, "${productDto.name} $${productDto.price} Added!", Snackbar.LENGTH_SHORT).show()
        }

        // Tap sobre la imagen para añadir
        binding.imageButton.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    fun permissionHandler(){
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d(TAG,"READ_EXTERNAL_STORAGE permission is granted. showDialog to select new Image")
                    showSelectImageProduct()



                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    //README: Layout show message to the user.
                    showMessagePermissionRequeriedForProductImage()
                    Log.d(TAG,"READ_EXTERNAL_STORAGE permission is not granted. Show current layout for explanation .")
                }
            }

    }

    fun checkAndRequestPermission(){
        if ( ActivityCompat.checkSelfPermission(
                this@SecondFragment.requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG,"Check for internet permission.")
            requestPermissionLauncher.launch(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return
        } else{
            Log.d(TAG,"READ_EXTERNAL_STORAGE permission is already granted.")

            showSelectImageProduct()
        }
    }

    private fun showSelectImageProduct() {
        // https://developer.android.com/training/basics/intents/result#launch
        // Pass in the mime type you want to let the user select
        // as the input
        getContent.launch("image/*")
    }

    //Method
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { ituri->
            Log.d(TAG,"path= ${ituri.path}")
            binding.imageButton.load(ituri)
        }
        // Handle the returned Uri
        Log.d(TAG,"List of images")
    }

    private fun showMessagePermissionRequeriedForProductImage() {
        TODO("Not yet implemented")
    }

    companion object{
        private const val PERMISSION_REQUEST_NOTIFICATION = 10
        private const val TAG="SecondFragment"
    }
}