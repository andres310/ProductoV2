package com.example.productov2.data.dto

import com.example.productov2.data.model.Product
import kotlinx.serialization.Serializable

@Serializable
class ProductDTO(
    var name: String = "",
    var price: Float = 0F,
    var imageUrl: String = ""
) {
    fun fillUpDto(productModel: Product) {
        name = productModel.name
        price = productModel.price
        imageUrl = productModel.imageUrl
    }
}