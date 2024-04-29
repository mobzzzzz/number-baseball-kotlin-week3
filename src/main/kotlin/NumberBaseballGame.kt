package org.example

class NumberBaseballGame {
    private var answer = ""
    private var strike: Int = 0
    private var ball: Int = 0

    init {
        this.resetGame()
    }

    fun resetGame() {
        this.resetAnswer()
        this.resetGameCount()
    }

    private fun resetAnswer() {
        this.answer = "345"
    }

    private fun resetGameCount() {
        this.strike = 0
        this.ball = 0
    }

    private fun validateAnswer(): NumberBaseballGameStatus {
        return when {
            this.strike == this.answer.length -> NumberBaseballGameStatus.Complete
            this.strike > 0 || this.ball > 0 -> NumberBaseballGameStatus.Progress
            else -> NumberBaseballGameStatus.Nothing
        }
    }

    fun checkInput(input: String): NumberBaseballGameStatus {
        this.resetGameCount()

        (0 until this.answer.length).forEach { index ->
            if (this.answer[index] == input[index]) { this.strike++ }
            else if (this.answer.contains(input[index])) { this.ball++ }
        }

        return this.validateAnswer()
    }

    fun getCurrentGameCount(completion: (NumberBaseballCount) -> Unit) {
        completion(NumberBaseballCount(this.strike, this.ball))
    }
}