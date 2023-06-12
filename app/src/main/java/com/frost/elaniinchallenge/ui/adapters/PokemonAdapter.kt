package com.frost.elaniinchallenge.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.elaniinchallenge.R
import com.frost.elaniinchallenge.databinding.PokemonItemBinding
import com.frost.elaniinchallenge.models.Pokemon
import java.util.*

class PokemonAdapter(private val context: Context, private val teamAdapter: Boolean ?= false) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private var pokemonList = listOf<Pokemon>()
    private var partialList = arrayListOf<Pokemon>()
    var onPokemonAddedClickCallback : ((pokemon: Pokemon) -> Unit)? = null
    var onPokemonRemovedClickCallback : ((pokemon: Pokemon) -> Unit)? = null

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(pokemonList[position]) {
                val pokemon = this
                with(binding) {
                    if (!image.isNullOrEmpty()) glideIt(image!!, pokeImageView)
                    nameTextView.text = name?.uppercase(Locale.ROOT)
                    idTextView.text = "ID: $id"
                    typesTextView.text = generateTypeString(types?: listOf())
                    descriptionTextView.text = generateRandomDescription(pokedex)
                    pokemonLayout.setBackgroundColor(getBackgroundType(types))
                    if (teamAdapter == true) {
                        teamImageView.visibility = View.GONE
                    } else {
                        pokemonLayout.setOnClickListener {
                            if (isSelected == false){
                                if (partialList.size < 6) {
                                    teamImageView.setImageResource(R.drawable.baseline_radio_button_checked_24)
                                    pokemon.isSelected = true
                                    partialList.add(pokemon)
                                    onPokemonAddedClickCallback?.invoke(pokemon)
                                }
                            } else {
                                teamImageView.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
                                partialList.remove(pokemon)
                                pokemon.isSelected = false
                                onPokemonRemovedClickCallback?.invoke(pokemon)
                            }
                        }
                    }

                }
            }
        }
    }

    private fun generateTypeString(types: List<String>): String{
        val newList = arrayListOf<String>()
        types.forEach {
            when(it) {
                context.getString(R.string.type_grass) -> newList.add("Hierba")
                context.getString(R.string.type_fire) -> newList.add("Fuego")
                context.getString(R.string.type_poison) -> newList.add("Veneno")
                context.getString(R.string.type_psychic) -> newList.add("Psiquico")
                context.getString(R.string.type_fighting) -> newList.add("Peleador")
                context.getString(R.string.type_water) -> newList.add("Agua")
                context.getString(R.string.type_normal) -> newList.add("Normal")
                context.getString(R.string.type_dark) -> newList.add("Oscuro")
                context.getString(R.string.type_flying) -> newList.add("Volador")
                context.getString(R.string.type_electric) -> newList.add("Electrico")
            }
        }
        return "Tipo: ${newList.joinToString(", ")}"
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