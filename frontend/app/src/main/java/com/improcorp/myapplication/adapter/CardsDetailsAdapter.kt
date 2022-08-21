package com.improcorp.myapplication.adapter

import android.annotation.SuppressLint
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
import com.improcorp.myapplication.model.*
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.tool.MyRequest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CardsDetailsAdapter(
    private var mContext: Context,
    var mEdition: Edition,
    private val repo: MyRepository
    ): RecyclerView.Adapter<CardsDetailsAdapter.ItemHolder>() {

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    private var mQuantityList = ArrayList<Int>()

    init{
        for (element in mEdition.mCardList) {
            mQuantityList.add(element.mUserQuantity)
        }
    }

    class ItemHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        var contribution: TextView = itemView.findViewById(R.id.card_details_contribution_txt2)

        var editionName: TextView = itemView.findViewById(R.id.card_details_e_name_txt)
        var cardName: TextView = itemView.findViewById(R.id.card_details_name)
        var illustration: ImageView = itemView.findViewById(R.id.card_details_illustration)
        var code1: TextView = itemView.findViewById(R.id.card_details_code_txt1)
        var code2: TextView = itemView.findViewById(R.id.card_details_code_txt2)
        var rarity1: TextView = itemView.findViewById(R.id.card_details_rarity_txt1)
        var rarity2: TextView = itemView.findViewById(R.id.card_details_rarity_txt2)
        var level1: TextView = itemView.findViewById(R.id.card_details_level_txt1)
        var level2: TextView = itemView.findViewById(R.id.card_details_level_txt2)
        var attribute1: TextView = itemView.findViewById(R.id.card_details_attribute_txt1)
        var attribute2: TextView = itemView.findViewById(R.id.card_details_attribute_txt2)
        var type1: TextView = itemView.findViewById(R.id.card_details_type_txt1)
        var type2: TextView = itemView.findViewById(R.id.card_details_type_txt2)
        var attack1: TextView = itemView.findViewById(R.id.card_details_attack_txt1)
        var attack2: TextView = itemView.findViewById(R.id.card_details_attack_txt2)
        var defense1: TextView = itemView.findViewById(R.id.card_details_defense_txt1)
        var defense2: TextView = itemView.findViewById(R.id.card_details_defense_txt2)
        var linkClassification1: TextView = itemView.findViewById(R.id.card_details_link_classification_txt1)
        var linkClassification2: TextView = itemView.findViewById(R.id.card_details_link_classification_txt2)
        var description1: TextView = itemView.findViewById(R.id.card_details_description_txt1)
        var description2: TextView = itemView.findViewById(R.id.card_details_description_txt2)

        var dateInsert1: TextView = itemView.findViewById(R.id.card_details_date_insert_txt1)
        var dateInsert2: TextView = itemView.findViewById(R.id.card_details_date_insert_txt2)
        var dateUpdate1: TextView = itemView.findViewById(R.id.card_details_date_update_txt1)
        var dateUpdate2: TextView = itemView.findViewById(R.id.card_details_date_update_txt2)

        var minus: Button = itemView.findViewById(R.id.card_details_minus_button)
        var add: Button = itemView.findViewById(R.id.card_details_add_button)
        var plus: Button = itemView.findViewById(R.id.card_details_plus_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(mContext).inflate(R.layout.activity_card_details, parent, false)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        itemHolder.layoutParams = params

        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int = mEdition.mCardList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val card: Card = mEdition.mCardList[position]
        val quantity = mQuantityList[position]

        println("position " + position)

        Glide.with(mContext)
            .load(card.mImageUrl)
            .into(holder.illustration)

        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(if(card.mUserQuantity == 0) 0f else 1f)

        val filter = ColorMatrixColorFilter(colorMatrix)
        holder.illustration.colorFilter = filter

        holder.setIsRecyclable(false)

        holder.editionName.text = card.mEdition
        holder.cardName.text = card.mName
        holder.code2.text = card.mCode
        holder.rarity2.text = card.mRarity
        holder.level2.text = card.mLevel
        holder.attribute2.text = card.mAttribute
        holder.type2.text = card.mType
        holder.attack2.text = card.mAttack
        holder.defense2.text = card.mDefense
        holder.linkClassification2.text = card.mLinkClassification

        holder.add.text = quantity.toString()

        if(card.mInsertDate == null){
            holder.dateInsert1.visibility = View.GONE
            holder.dateInsert2.visibility = View.GONE

            holder.dateUpdate1.visibility = View.GONE
            holder.dateUpdate2.visibility = View.GONE
        } else {
            holder.dateInsert1.visibility = View.VISIBLE
            holder.dateInsert2.visibility = View.VISIBLE

            holder.dateUpdate1.visibility = View.VISIBLE
            holder.dateUpdate2.visibility = View.VISIBLE

            holder.dateInsert2.text = card.mInsertDate
            holder.dateUpdate2.text = card.mUpdateDate
        }

        if(card.mText.isEmpty()){
            holder.description1.visibility = View.GONE
            holder.description2.visibility = View.GONE

        } else{
            holder.description2.text = card.mText
        }

        holder.minus.isEnabled = mQuantityList[position] > 0
        holder.plus.isEnabled = mQuantityList[position] < 100

        holder.add.isEnabled = card.mUserQuantity != mQuantityList[position]

        println("template_id" + card.mTemplateId)
        when(card.mTemplateId){
            1, 2, 7 -> { //magic/trap/token
                holder.level1.visibility = View.GONE
                holder.level2.visibility = View.GONE
                holder.attribute1.visibility = View.GONE
                holder.attribute2.visibility = View.GONE
                holder.type1.visibility = View.GONE
                holder.type2.visibility = View.GONE
                holder.attack1.visibility = View.GONE
                holder.attack2.visibility = View.GONE
                holder.defense1.visibility = View.GONE
                holder.defense2.visibility = View.GONE
                holder.linkClassification1.visibility = View.GONE
                holder.linkClassification2.visibility = View.GONE
            }
            3 -> { // link_monster
                holder.level1.visibility = View.GONE
                holder.level2.visibility = View.GONE
                holder.defense1.visibility = View.GONE
                holder.defense2.visibility = View.GONE
            }
            4 -> { //duelist card name
                holder.level1.visibility = View.GONE
                holder.level2.visibility = View.GONE
                holder.attribute1.visibility = View.GONE
                holder.attribute2.visibility = View.GONE
                holder.type1.visibility = View.GONE
                holder.type2.visibility = View.GONE
                holder.attack1.visibility = View.GONE
                holder.attack2.visibility = View.GONE
                holder.defense1.visibility = View.GONE
                holder.defense2.visibility = View.GONE
                holder.rarity1.visibility = View.GONE
                holder.rarity2.visibility = View.GONE
                holder.linkClassification1.visibility = View.GONE
                holder.linkClassification2.visibility = View.GONE
                holder.description1.visibility = View.GONE
                holder.description2.visibility = View.GONE
            }
            5 -> { //monster
                holder.linkClassification1.visibility = View.GONE
                holder.linkClassification2.visibility = View.GONE
            }
            6 -> { //token
                holder.level1.visibility = View.GONE
                holder.level2.visibility = View.GONE
                holder.attribute1.visibility = View.GONE
                holder.attribute2.visibility = View.GONE
                holder.type1.visibility = View.GONE
                holder.type2.visibility = View.GONE
                holder.attack1.visibility = View.GONE
                holder.attack2.visibility = View.GONE
                holder.defense1.visibility = View.GONE
                holder.defense2.visibility = View.GONE
                holder.linkClassification1.visibility = View.GONE
                holder.linkClassification2.visibility = View.GONE
                holder.description1.visibility = View.GONE
                holder.description2.visibility = View.GONE
            }
        }

        holder.plus.setOnClickListener {
            mQuantityList[position] = if(mQuantityList[position] < 100) mQuantityList[position] + 1 else 100
            notifyDataSetChanged()
        }

        holder.minus.setOnClickListener {
            mQuantityList[position] = if(mQuantityList[position] > 0) mQuantityList[position] - 1 else 0
            notifyDataSetChanged()
        }

        holder.add.setOnClickListener {
            val userCollection = UserCollection(User.mId!!, card.mId, mQuantityList[position], sdf.format(Date()), sdf.format(Date()))
            if(card.mUserQuantity == 0 && mQuantityList[position] > 0){
                repo.addCardUser(userCollection, object: MyRequest.AddCardUserCallback{
                    override fun onSuccess(message: String) {
                        val quantityEdition = if (mQuantityList[position] > card.mQuantityInEdition) card.mQuantityInEdition else mQuantityList[position]
                        mEdition.mUserNumberOfCards += quantityEdition
                        mEdition.calculateCompletion()

                        mEdition.mCardList[position].mUserQuantity = userCollection.quantity
                        mEdition.mCardList[position].mInsertDate = userCollection.dateInsert
                        mEdition.mCardList[position].mUpdateDate = userCollection.dateUpdate

                        notifyDataSetChanged()
                    }

                    override fun onError(message: String) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun inputErrors(errorHashMap: HashMap<String, String>) {
                        for(item in errorHashMap){
                            Toast.makeText(mContext, item.value, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else if(card.mUserQuantity != 0 && mQuantityList[position] > 0){
                repo.updateCardUser(userCollection, object: MyRequest.UpdateCardUserCallback{
                    override fun onSuccess(message: String) {
                        val quantityEdition = if (card.mUserQuantity > card.mQuantityInEdition) card.mQuantityInEdition else card.mUserQuantity
                        val newQuantityEdition = if (mQuantityList[position] > card.mQuantityInEdition) card.mQuantityInEdition else mQuantityList[position]

                        mEdition.mUserNumberOfCards += (newQuantityEdition - quantityEdition)

                        mEdition.calculateCompletion()

                        card.mUserQuantity = userCollection.quantity
                        card.mUpdateDate = userCollection.dateUpdate

                        notifyDataSetChanged()
                    }

                    override fun onError(message: String) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun inputErrors(errorHashMap: HashMap<String, String>) {
                        for(item in errorHashMap){
                            Toast.makeText(mContext, item.value, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else if(card.mUserQuantity != 0 && mQuantityList[position] == 0){
                repo.deleteCardUser(userCollection,  object: MyRequest.DeleteCardUserCallback{
                    override fun onSuccess(message: String) {
                        val quantityEdition = if (card.mUserQuantity > card.mQuantityInEdition) card.mQuantityInEdition else card.mUserQuantity

                        mEdition.mUserNumberOfCards -= quantityEdition
                        mEdition.calculateCompletion()

                        mEdition.mCardList[position].mUserQuantity = mQuantityList[position]
                        mEdition.mCardList[position].mInsertDate = null
                        mEdition.mCardList[position].mUpdateDate = null

                        notifyDataSetChanged()
                    }

                    override fun onError(message: String) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun inputErrors(errorHashMap: HashMap<String, String>) {
                        for(item in errorHashMap){
                            Toast.makeText(mContext, item.value, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
}