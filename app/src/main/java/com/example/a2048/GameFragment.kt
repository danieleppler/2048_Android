package com.example.a2048

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class GameFragment: Fragment() {


    companion object {
        private const val GRID_SIZE = 4
        const val SCORE_DEST = 2048
    }



    private var uiTiles = Array(GRID_SIZE) { Array<TextView?>(GRID_SIZE) { null } }
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var scoreText: TextView
    private lateinit var gameGridLayout: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
                    if (gameViewModel.checkWin()) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("2048")
                            .setMessage("You won")
                            .setPositiveButton("OK") { dialog, _ ->
                                gameViewModel.resetGame()
                                dialog.dismiss()
                            }
                            .show()

                    } else if (gameViewModel.checkLose()) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("2048")
                            .setMessage("You Lost")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                gameViewModel.resetGame()
                            }
                            .show()
                    }
                }
            }
        }
    }


    private fun updateUi(gameBoard: Array<IntArray>) {
        updateGameBoard(gameBoard)

//        // Update score displays
//        updateScoreDisplay(gameState)
//
//        // Update control buttons state
//        updateControlsState(gameState)
//
//        // Handle game end states
//        handleGameEndStates(gameState)
    }

    private fun updateGameBoard(board: Array<IntArray>) {
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                val tile = uiTiles[i][j]
                val value = board[i][j]

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

//
//    private fun getTileBackground(value: Int): Int {
//        return when (value) {
//            2 -> R.drawable.tile_2
//            4 -> R.drawable.tile_4
//            8 -> R.drawable.tile_8
//            16 -> R.drawable.tile_16
//            32 -> R.drawable.tile_32
//            64 -> R.drawable.tile_64
//            128 -> R.drawable.tile_128
//            256 -> R.drawable.tile_256
//            512 -> R.drawable.tile_512
//            1024 -> R.drawable.tile_1024
//            2048 -> R.drawable.tile_2048
//            else -> R.drawable.tile_background
//        }
//    }

    private fun getTileTextColor(value: Int): Int {
        return when (value) {
            2, 4 -> 0xFF776e65.toInt() // Dark text for light tiles
            else -> 0xFFf9f6f2.toInt() // Light text for dark tiles
        }
    }

    private fun getTileTextSize(value: Int): Float {
        return when {
            value < 100 -> 24f
            value < 1000 -> 20f
            value < 10000 -> 18f
            else -> 16f
        }
    }


    private fun initViews(view: View) {
        gameGridLayout = view.findViewById(R.id.game_board)
        scoreText = view.findViewById(R.id.score_text)

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