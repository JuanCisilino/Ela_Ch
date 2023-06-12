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
                    typesTextView.text = types?.joinToString(", ")
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

    private fun generateRandomDescription(pokedex: List<String>?): String {
        if (pokedex.isNullOrEmpty()) return ""
        return pokedex.random()
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
        return when {
            type.any{it == context.getString(R.string.grass)} -> context.getColor(R.color.grass)
            type.any{it == context.getString(R.string.fire)} -> context.getColor(R.color.fire)
            type.any{it == context.getString(R.string.poison)} -> context.getColor(R.color.poison)
            type.any{it == context.getString(R.string.psychic)} -> context.getColor(R.color.psychic)
            type.any{it == context.getString(R.string.fight)} -> context.getColor(R.color.fight)
            type.any{it == context.getString(R.string.water)} -> context.getColor(R.color.water)
            type.any{it == context.getString(R.string.normal)} -> context.getColor(R.color.normal)
            type.any{it == context.getString(R.string.dark)} -> context.getColor(R.color.dark)
            type.any{it == context.getString(R.string.flying)} -> context.getColor(R.color.flying)
            type.any{it == context.getString(R.string.electric)} -> context.getColor(R.color.electric)
            type.any{it == context.getString(R.string.steel)} -> context.getColor(R.color.steel)
            type.any{it == context.getString(R.string.ground)} -> context.getColor(R.color.ground)
            type.any{it == context.getString(R.string.rock)} -> context.getColor(R.color.rock)
            type.any{it == context.getString(R.string.ghost)} -> context.getColor(R.color.ghost)
            type.any{it == context.getString(R.string.bug)} -> context.getColor(R.color.bug)
            type.any{it == context.getString(R.string.ice)} -> context.getColor(R.color.ice)
            type.any{it == context.getString(R.string.dragon)} -> context.getColor(R.color.dragon)
            type.any{it == context.getString(R.string.fairy)} -> context.getColor(R.color.fairy)
            else -> context.getColor(R.color.white)
        }
    }

    override fun getItemCount() = pokemonList.size
}