package com.frost.elaniinchallenge.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.databinding.PokemonItemBinding
import com.frost.elaniinchallenge.models.Pokemon
import java.util.*
import kotlin.collections.ArrayList

class PokemonAdapter(private val context: Context) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private var pokemonList = listOf<Pokemon>()

    var onPokemonClickCallback : ((pokemonId: String) -> Unit)? = null

    fun updateItems(newItems: List<Pokemon>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = pokemonList.size

            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                pokemonList[oldItemPosition].id == newItems[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                pokemonList[oldItemPosition] == newItems[newItemPosition]

        })
        pokemonList = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(pokemonList[position]) {
                with(binding) {
                    if (!image.isNullOrEmpty()) glideIt(image!!, pokeImageView)
                    nameTextView.text = name?.uppercase(Locale.ROOT)
                    idTextView.text = "ID: $id"
                    typesTextView.text = generateTypeString(types?: listOf())
                    descriptionTextView.text = generateRandomDescription(pokedex)
                    pokemonLayout.setBackgroundColor(getBackgroundType(types))
                }
            }
        }
    }

    private fun generateTypeString(types: List<String>): String{
        val string = "Tipo: ${types.joinToString(", ")}"
        types.forEach {
            when(it) {
                context.getString(R.string.type_grass) -> string.plus("Hierba ")
                context.getString(R.string.type_fire) -> string.plus("Fuego ")
                context.getString(R.string.type_poison) -> string.plus("Veneno ")
                context.getString(R.string.type_psychic) -> string.plus("Psiquico ")
                context.getString(R.string.type_fighting) -> string.plus("Peleador ")
                context.getString(R.string.type_water) -> string.plus("Agua ")
                context.getString(R.string.type_normal) -> string.plus("Normal ")
                context.getString(R.string.type_dark) -> string.plus("Oscuro ")
                context.getString(R.string.type_flying) -> string.plus("Volador ")
                context.getString(R.string.type_electric) -> string.plus("Electrico ")
            }
        }
        return string
    }

    private fun generateRandomDescription(pokedex: List<String>?): String{
        if (pokedex.isNullOrEmpty()) return ""
        //return pokedex.random()
        return pokedex[0]
    }

    private fun glideIt(url: String, image: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
    }

    private fun getBackgroundType(type: List<String>?): Int {
        type?:run { return context.getColor(R.color.white) }
        if (type.any { it == context.getString(R.string.type_grass) }) return context.getColor(R.color.hoenn)
        if (type.any { it == context.getString(R.string.type_fire) }) return context.getColor(R.color.kalos)
        return if (type.any { it == context.getString(R.string.type_poison) }) context.getColor(R.color.kanto)
        else context.getColor(R.color.white)
    }

    override fun getItemCount() = pokemonList.size
}