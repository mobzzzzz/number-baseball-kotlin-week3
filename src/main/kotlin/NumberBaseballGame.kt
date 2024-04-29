package org.example

class NumberBaseballGame {
    private var answer = ""
    private var strike: Int = 0
    private var ball: Int = 0

    init {
        this.resetGame()
    }

    /**
     * 현재 게임 상태를 초기화합니다.
     *
     */
    fun resetGame() {
        this.resetAnswer()
        this.resetGameCount()
    }

    /**
     * 현재 게임 정답을 초기화합니다.
     *
     */
    private fun resetAnswer() {
        this.answer = "345"
    }

    /**
     * 숫자 야구 게임에 사용중인 볼 카운트를 초기화합니다.
     */
    private fun resetGameCount() {
        this.strike = 0
        this.ball = 0
    }

    /**
     * 현재 게임 상태에 맞는 NumberBaseballGameStatus를 반환합니다.
     *
     * @return NumberBaseballGameStatus
     */
    private fun validateAnswer(): NumberBaseballGameStatus {
        return when {
            this.strike == this.answer.length -> NumberBaseballGameStatus.Correct
            this.strike > 0 || this.ball > 0 -> NumberBaseballGameStatus.Progress
            else -> NumberBaseballGameStatus.Nothing
        }
    }

    /**
     * 입력받은 값을 정답과 비교해 NumberBaseballGameStatus로 정답 여부를 반환합니다
     *
     * @param input: 입력받은 값
     * @return NumberBaseballGameStatus: Nothing, Progress, Correct 세 가지 상태중 하나입니다.
     */
    fun checkInput(input: String): NumberBaseballGameStatus {
        val inputCharList = Regex("""\d""").findAll(input).map { it.value.single() }.toList()
        val answerLength = this.answer.length

        if (inputCharList.count() != answerLength) { throw Exception("입력값이 올바르지 않습니다.") }
        if (inputCharList.first() == '0') { throw Exception("첫 번째 숫자는 0이 될 수 없습니다.") }
        if (inputCharList.distinct().count() < answerLength) { throw Exception("중복 값이 있습니다.") }

        this.resetGameCount()

        (0 until answerLength).forEach { index ->
            if (this.answer[index] == inputCharList[index]) { this.strike++ }
            else if (this.answer.contains(inputCharList[index])) { this.ball++ }
        }

        return this.validateAnswer()
    }

    /**
     * 현재 스트라이크, 볼 카운트를 NumberBaseballCount로 묶어 completion으로 전달합니다.
     *
     * @param completion: Strike, Ball 정보를 가진 NumberBaseballCount를 전달합니다
     */
    fun getCurrentGameCount(completion: (NumberBaseballCount) -> Unit) {
        completion(NumberBaseballCount(this.strike, this.ball))
    }
}