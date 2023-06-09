package com.frost.elaniinchallenge.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.Region
import com.frost.elaniinchallenge.databinding.RegionItemBinding

class RegionAdapter(private val regionList: List<Region>, private val context: Context) : RecyclerView.Adapter<RegionAdapter.ViewHolder>() {

    var onRegionClickCallback : ((region: String) -> Unit)? = null

    inner class ViewHolder(val binding: RegionItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RegionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(regionList[position]) {
                with(binding) {
                    regionName.text = name
                    regionLayout.setBackgroundColor(getBackground(name))
                    regionLayout.setOnClickListener { onRegionClickCallback?.invoke(name) }
                }
            }
        }
    }

    private fun getBackground(name: String): Int {
        return when(name){
            context.getString(R.string.kanto) -> context.getColor(R.color.kanto)
            context.getString(R.string.johto) -> context.getColor(R.color.jhoto)
            context.getString(R.string.hoenn) -> context.getColor(R.color.hoenn)
            context.getString(R.string.sinnoh) -> context.getColor(R.color.sinnoh)
            context.getString(R.string.unova) -> context.getColor(R.color.unova)
            context.getString(R.string.kalos) -> context.getColor(R.color.kalos)
            context.getString(R.string.alola) -> context.getColor(R.color.alola)
            context.getString(R.string.galar) -> context.getColor(R.color.galar)
            context.getString(R.string.paldea) -> context.getColor(R.color.paldea)
            else -> context.getColor(R.color.white)
        }
    }

    override fun getItemCount() = regionList.size
}