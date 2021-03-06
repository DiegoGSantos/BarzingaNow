package com.barzinga.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.barzinga.R
import com.barzinga.databinding.ItemProductBinding

import com.barzinga.model.Product
import com.barzinga.viewmodel.ProductViewModel
import com.bumptech.glide.Glide

/**
 * Created by diego.santos on 05/10/17.
 */
class ProductsAdapter(val context: Context, var products: List<Product>?) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>(){

    private var itemClick: ((Product) -> Unit)? = null
    companion object {
        var mProducts: List<Product>? = emptyList()
    }

    init {
        mProducts = products
    }

    override fun getItemCount(): Int = mProducts?.size ?: 0

    override fun onBindViewHolder(holder: ProductViewHolder?, position: Int) {
        val binding = holder?.binding
        val product = mProducts?.get(position)

        var viewModel = product?.let { ProductViewModel(it) }
        binding?.viewModel = viewModel

        holder?.setClickListener(itemClick)

        Glide.with(context).load(binding?.viewModel?.product?.image_url).into(binding?.mProductImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductViewHolder {
        val binding = DataBindingUtil.inflate<ItemProductBinding>(
                LayoutInflater.from(parent?.context),
                R.layout.item_product,
                parent,
                false
        )

        return ProductViewHolder(binding)
    }

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setClickListener(callback: ((Product) -> Unit)?){
            binding.viewModel.clicks().subscribe {
                callback?.invoke(binding.viewModel.product)
            }
        }

        init {
            binding.decreaseQtde.setOnClickListener({
                var i = Integer.parseInt(binding.mQtde.getText().toString())

                if (i > 0) {
                    i--
                    binding.mQtde.setText(i.toString())
                    mProducts?.get(position)?.quantity = i.toString()
                }
            })

            binding.increaseQtde.setOnClickListener({
                var i = Integer.parseInt(binding.mQtde.getText().toString())

                i++
                binding.mQtde.setText(i.toString())
                mProducts?.get(position)?.quantity = i.toString()
            })
        }
    }

    fun setClickListener(itemClick: ((Product) -> Unit)?) {
        this.itemClick = itemClick
    }
}