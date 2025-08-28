package com.example.a2048

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class GameViewModel : ViewModel() {



    private val _board = MutableStateFlow(
        arrayOf(
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0)
        )
    )
    val board: StateFlow<Array<IntArray>> = _board.asStateFlow()

    var gameForfieted: Boolean = false
    var currMove: String? = null

    init{
        assignRandom2();
        assignRandom2();
    }



    fun moveDirection(direction :String?) {
        when (direction) {
            "up", "UP" -> moveUp()
            "down", "DOWN" -> moveDown()
            "right", "RIGHT" -> moveRight()
            "left", "LEFT" -> moveLeft()
        }
        assignRandom2()
        _board.value = Array(_board.value.size) { i ->
            _board.value[i].clone()
        }
    }

    private fun moveUp(){
        var addingAvailable : Boolean
        var loopCount : Int
        for(i in 0..3){
            loopCount = 3
            if ((_board.value[0][i] != 0 || _board.value[1][i] !=0 || _board.value[2][i] != 0 || _board.value[3][i]!=0) && _board.value[0][i] == _board.value[1][i] && _board.value[2][i] == _board.value[3][i])
            {

                _board.value[0][i] = _board.value[0][i] * 2;
                _board.value[1][i] = 0;
                _board.value[2][i] = _board.value[2][i] * 2;
                _board.value[3][i] = 0;
                addingAvailable = false;
            }
            else addingAvailable = true;
            while(loopCount > 0)
            {
                for (j in 0 .. 2) //rows
                {
                    if (_board.value[j][i] != 0 && _board.value[j][i] == _board.value[j + 1][i] && addingAvailable)
                    {
                        _board.value[j][i] = _board.value[j][i] * 2;
                        _board.value[j + 1][i] = 0;
                        addingAvailable = false;
                    }
                    else if (_board.value[j][i] == 0)
                    {
                        _board.value[j][i] = _board.value[j + 1][i];
                        _board.value[j + 1][i] = 0;
                    }
                }
                loopCount--;
            }
        }
    }

    private fun moveDown(){
        var addingAvailable : Boolean
        var loopCount : Int
        for(i in 0..3){
            loopCount = 3
            if ((_board.value[0][i] != 0 || _board.value[1][i] !=0 || _board.value[2][i] != 0 || _board.value[3][i]!=0) && _board.value[0][i] == _board.value[1][i] && _board.value[2][i] == _board.value[3][i])
            {
                _board.value[1][i] = _board.value[1][i] * 2;
                _board.value[0][i] = 0;
                _board.value[3][i] = _board.value[3][i] * 2;
                _board.value[0][i] = 0;
                addingAvailable = false;
            }
            else addingAvailable = true;
            while(loopCount > 0)
            {
                for (j in 3 downTo  1) //rows
                {
                    if (_board.value[j][i] != 0 && _board.value[j][i] == _board.value[j - 1][i] && addingAvailable)
                    {
                        _board.value[j][i] = _board.value[j][i] * 2;
                        _board.value[j - 1][i] = 0;
                        addingAvailable = false;
                    }
                    else if (_board.value[j][i] == 0)
                    {
                        _board.value[j][i] = _board.value[j - 1][i];
                        _board.value[j - 1][i] = 0;
                    }
                }
                loopCount--;
            }
        }
    }

    private fun moveRight(){
        var addingAvailable : Boolean
        var loopCount : Int
        for(i in 0..3){
            loopCount = 3
            if ((_board.value[i][0] != 0 || _board.value[i][1] !=0 || _board.value[i][2] != 0 || _board.value[i][3]!=0) && _board.value[i][0] == _board.value[i][1] && _board.value[i][2] == _board.value[i][3])
            {
                _board.value[i][1] = _board.value[i][1] * 2;
                _board.value[i][0] = 0;
                _board.value[i][3] = _board.value[i][3] * 2;
                _board.value[i][2] = 0;
                addingAvailable = false;
            }
            else addingAvailable = true;
            while(loopCount > 0)
            {
                for (j in 3 downTo  1) //rows
                {
                    if (_board.value[i][j] != 0 && _board.value[i][j] == _board.value[i][j-1] && addingAvailable)
                    {
                        _board.value[i][j] = _board.value[i][j - 1] * 2;
                        _board.value[i][j - 1] = 0;
                        addingAvailable = false;
                    }
                    else if (_board.value[i][j] == 0)
                    {
                        _board.value[i][j] = _board.value[i][j - 1];
                        _board.value[i][j - 1] = 0;
                    }
                }
                loopCount--;
            }
        }
    }

    private fun moveLeft(){
        var addingAvailable : Boolean
        var loopCount : Int
        for(i in 0..3){
            loopCount = 3
            if ((_board.value[i][0] != 0 || _board.value[i][1] !=0 || _board.value[i][2] != 0 || _board.value[i][3]!=0) && _board.value[i][0] == _board.value[i][1] && _board.value[i][2] == _board.value[i][3])
            {
                _board.value[i][1] = _board.value[i][1] * 2;
                _board.value[i][0] = 0;
                _board.value[i][3] = _board.value[i][3] * 2;
                _board.value[i][2] = 0;
                addingAvailable = false;
            }
            else addingAvailable = true;
            while(loopCount > 0)
            {
                for (j in 0 ..  2) //rows
                {
                    if (_board.value[i][j] != 0 && _board.value[i][j] == _board.value[i][j + 1] && addingAvailable)
                    {
                        _board.value[i][j] = _board.value[i][j + 1] * 2;
                        _board.value[i][j + 1] = 0;
                        addingAvailable = false;
                    }
                    else if (_board.value[i][j] == 0)
                    {
                        _board.value[i][j] = _board.value[i][j + 1];
                        _board.value[i][j + 1] = 0;
                    }
                }
                loopCount--;
            }
        }
    }


    fun resetGame() {
        _board.value = createInitialBoard()
        // Reset any other game state
        assignRandom2() // Add initial tiles
        assignRandom2()
        // Create a new array to trigger StateFlow update
        val currentBoard = _board.value
        _board.value = Array(currentBoard.size) { i ->
            currentBoard[i].clone()
        }

    }

    private fun createInitialBoard(): Array<IntArray> {
        return Array(4) { IntArray(4) { 0 } } // 4x4 board with all zeros
    }

    fun getCurrentBoard(): Array<IntArray> {
        return _board.value
    }

    private fun assignRandom2() {
        var tilePutted :Boolean = false;
        var randomPlace :Int;
        while (!tilePutted)
        {
            randomPlace = Random.nextInt(0, 16);
            if(_board.value[randomPlace / 4][randomPlace % 4] == 0)
            {
                _board.value[randomPlace / 4][randomPlace % 4] = 2;
                tilePutted = true;
            }
        }
    }

     fun checkLose() : Boolean{
        _board.value.forEach { it.forEach { cell ->
            if(cell == 0)
                return false
        }
        }
        return  true
    }

    fun checkWin() : Boolean{
        _board.value.forEach { it.forEach { cell ->
            if(cell == GameFragment.SCORE_DEST)
                return true
        }
        }
        return  false
    }


}