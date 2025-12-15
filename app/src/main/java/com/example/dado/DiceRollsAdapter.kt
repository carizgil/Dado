package com.example.dado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiceRollsAdapter(private val diceRolls: List<DiceRoll>) : RecyclerView.Adapter<DiceRollsAdapter.DiceRollViewHolder>() {

    class DiceRollViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rollResult: TextView = itemView.findViewById(R.id.rollResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceRollViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dice_roll_item, parent, false)
        return DiceRollViewHolder(view)
    }


    override fun onBindViewHolder(holder: DiceRollViewHolder, position: Int) {
        val diceRoll = diceRolls[position]
        holder.rollResult.text = "Resultado: ${diceRoll.result}"
    }

    override fun getItemCount() = diceRolls.size
}