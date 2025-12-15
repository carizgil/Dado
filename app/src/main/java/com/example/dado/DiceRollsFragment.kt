package com.example.dado

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import org.w3c.dom.Text

class DiceRollsFragment : Fragment() {
    private lateinit var diceRollsAdapter: DiceRollsAdapter
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().invalidateOptionsMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        diceRollsAdapter = DiceRollsAdapter(MainFragment.diceRolls)

        return inflater.inflate(R.layout.fragment_dice_rolls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val rvDiceRolls: RecyclerView = view.findViewById(R.id.recyclerView)
        rvDiceRolls.adapter = diceRollsAdapter
        rvDiceRolls.layoutManager = LinearLayoutManager(context)

        val btnBackToGame: Button = view.findViewById(R.id.btnBackToGame)
        btnBackToGame.setOnClickListener {
            findNavController().navigate(R.id.action_diceRollsFragment_to_mainFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_resultados, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                MainFragment.diceRolls.clear()
                diceRollsAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}