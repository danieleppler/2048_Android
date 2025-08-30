package com.example.a2048

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt


class GameFragment: Fragment() {


    companion object {
        private const val GRID_SIZE = 4
        const val SCORE_DEST = 2048
    }


    private var uiTiles = Array(GRID_SIZE) { Array<TextView?>(GRID_SIZE) { null } }
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var gameGridLayout: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupControls(view)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                gameViewModel.board.collect { gameBoard ->
                    updateUi(gameBoard)
                    var isWon = false;
                    var isLost = false;
                    if (gameViewModel.checkWin()) {
                        isWon = true
                    }
                    else if(gameViewModel.checkLose()){
                        isLost =true
                    }
                    if(isWon || isLost){
                        AlertDialog.Builder(requireContext())
                            .setTitle("2048")
                            .setMessage( if (isLost) "You Lost" else "You Won")
                            .setPositiveButton("OK") { dialog, _ ->
                                gameViewModel.resetGame()
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun updateUi(gameBoard: Array<IntArray>) {
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                val tile = uiTiles[i][j]
                val value = gameBoard[i][j]

                tile?.let { tileView ->
                    if (value == 0) {
                        // Empty tile
                        tileView.text = ""
                        tileView.setBackgroundResource(R.drawable.tile_background)
                        tileView.alpha = 0.5f
                    } else {
                        // Tile with value
                        tileView.text = value.toString()
                        tileView.setTextColor(getTileTextColor(value))
                        tileView.alpha = 1.0f

                        // Adjust text size based on value
                        tileView.textSize = getTileTextSize(value)
                    }
                }
            }
        }

    }

    private fun getTileTextColor(value: Int): Int {
        return when (value) {
            2 -> ContextCompat.getColor(requireContext(), R.color.tile_text_dark_brown)      // Dark brown
            4 -> ContextCompat.getColor(requireContext(), R.color.tile_text_deep_blue)       // Deep blue
            8 -> ContextCompat.getColor(requireContext(), R.color.tile_text_forest_green)    // Forest green
            16 -> ContextCompat.getColor(requireContext(), R.color.tile_text_burgundy)       // Burgundy
            32 -> ContextCompat.getColor(requireContext(), R.color.tile_text_navy)           // Navy blue
            64 -> ContextCompat.getColor(requireContext(), R.color.tile_text_dark_purple)    // Dark purple
            128 -> ContextCompat.getColor(requireContext(), R.color.tile_text_crimson)       // Crimson red
            256 -> ContextCompat.getColor(requireContext(), R.color.tile_text_teal)          // Dark teal
            512 -> ContextCompat.getColor(requireContext(), R.color.tile_text_orange)        // Orange
            1024 -> ContextCompat.getColor(requireContext(), R.color.tile_text_golden)       // Golden yellow
            2048 -> ContextCompat.getColor(requireContext(), R.color.tile_text_victory_gold) // Victory gold
            else -> ContextCompat.getColor(requireContext(), R.color.tile_text_dark_brown)   // Default dark brown
        }
    }

    private fun getTileTextSize(value: Int): Float {
        return when {
            value < 100 -> 16f
            value < 1000 -> 18f
            else -> 20f
        }
    }

    private fun initViews(view: View) {
        gameGridLayout = view.findViewById(R.id.game_board)

        // Create tile TextViews and add to GridLayout
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                val tile = TextView(context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(j, 1f)
                        rowSpec = GridLayout.spec(i, 1f)
                        setMargins(4, 4, 4, 4)
                    }

                    setBackgroundResource(R.drawable.tile_background)
                    gravity = android.view.Gravity.CENTER
                    textSize = 24f
                    setTextColor(0xFF776e65.toInt())
                }
                uiTiles[i][j] = tile
                gameGridLayout.addView(tile)
            }
        }
    }

    private fun setupControls(view: View) {
        view.findViewById<ImageButton>(R.id.btn_up).setOnClickListener {
            gameViewModel.moveDirection("UP")
        }
        view.findViewById<ImageButton>(R.id.btn_down).setOnClickListener {
            gameViewModel.moveDirection("DOWN")
        }
        view.findViewById<ImageButton>(R.id.btn_left).setOnClickListener {
            gameViewModel.moveDirection("LEFT")
        }
        view.findViewById<ImageButton>(R.id.btn_right).setOnClickListener {
            gameViewModel.moveDirection("RIGHT")
        }
    }
}