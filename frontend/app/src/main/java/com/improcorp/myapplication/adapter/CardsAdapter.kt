package com.improcorp.myapplication.adapter

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.improcorp.myapplication.R
import com.improcorp.myapplication.model.Card
import java.util.*
import kotlin.collections.ArrayList

class CardsAdapter(
    private val mContext: Context,
    private val editionPosition: Int,
    var mCardList: ArrayList<Card>,
    private var mListener: CardsAdapterListener
    ) : RecyclerView.Adapter<CardsAdapter.ItemHolder>(), Filterable {

    private val mCardReferenceList: ArrayList<Card> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(mContext).inflate(R.layout.cardview_card, parent, false)

        return ItemHolder(itemHolder, editionPosition, mListener)
    }

    override fun getItemCount(): Int = mCardList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val card: Card = mCardList[position]

        Glide.with(mContext)
            .load(card.mImageUrl)
            .into(holder.illustration)

        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(if(card.mUserQuantity == 0) 0f else 1f)

        val filter = ColorMatrixColorFilter(colorMatrix)
        holder.illustration.colorFilter = filter

        val quantity = if(card.mUserQuantity > card.mQuantityInEdition) card.mQuantityInEdition else card.mUserQuantity

        holder.quantity.text = StringBuilder().append(quantity).append("/").append(card.mQuantityInEdition)
        holder.code.text = StringBuilder().append(card.mCode)
        holder.rarity.text = card.mRarity
        holder.name.text = card.mName
    }

    fun updateCardsListFromEdition(cardsList: ArrayList<Card>){
        mCardList.addAll(cardsList)
        mCardReferenceList.addAll(cardsList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val filterResults = FilterResults()
                val charString = charSequence.toString()
                mCardList = if (charString.isEmpty()) {
                    mCardReferenceList
                } else {
                    val filteredList = ArrayList<Card>()
                    for (row in mCardReferenceList) {
                        if (row.mName.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT)) || row.mCode
                                .contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                filterResults.values = mCardList
                filterResults.count = mCardList.size
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                mCardList = filterResults.values as ArrayList<Card>

                notifyDataSetChanged()
            }
        }
    }

    interface CardsAdapterListener {
        fun onCardSelected(editionPosition: Int, cardPosition: Int)
    }

    class ItemHolder(itemView: View,
                     private val editionPosition: Int,
                     onCardsListener: CardsAdapterListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var quantity: TextView = itemView.findViewById(R.id.cd_card_quantity_txt)
        var illustration: ImageView = itemView.findViewById(R.id.cd_card_image_view)
        var code: TextView = itemView.findViewById(R.id.cd_card_code_txt)
        var rarity: TextView = itemView.findViewById(R.id.cd_card_rarity_txt)
        var name: TextView = itemView.findViewById(R.id.cd_card_name_txt)

        private var onCardListener: CardsAdapterListener

        init {
            this.itemView.setOnClickListener(this)
            this.onCardListener = onCardsListener
        }

        override fun onClick(v: View?) {
            this.onCardListener.onCardSelected(editionPosition, bindingAdapterPosition)
        }
    }
}