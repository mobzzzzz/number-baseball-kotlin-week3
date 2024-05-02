package org.example

class NumberBaseballGame {
    // 직접적인 set 방지
    private var answer = ""
    private var numberBaseballCount = NumberBaseballCount(0, 0)
    private var inputCount: Int = 0

    var difficulty: NumberBaseballDifficulty = NumberBaseballDifficulty.Normal
        set(value) {
            field = value
            this.resetGame()
        }

    /**
     * 현재 게임 상태를 초기화합니다.
     *
     */
    fun resetGame() {
        this.resetAnswer()
        this.resetGameCount(true)
    }

    /**
     * 현재 게임 정답을 초기화합니다.
     *
     */
    private fun resetAnswer() {
        this.answer = ""

        do {
            this.answer = (0..9).toList().shuffled().slice(0 until this.difficulty.length).joinToString("")
        } while (this.answer.first() == '0')
    }

    /**
     * 숫자 야구 게임에 사용중인 카운팅 변수를 초기화합니다.
     *
     */
    private fun resetGameCount(withInput: Boolean = false) {
        this.numberBaseballCount.strike = 0
        this.numberBaseballCount.ball = 0
        if (withInput) this.inputCount = 0
    }

    /**
     * 정답과 비교해 현재 게임 상태에 맞는 NumberBaseballGameStatus를 반환합니다.
     *
     * @return NumberBaseballGameStatus
     */
    private fun validateAnswer(): NumberBaseballGameStatus {
        return when {
            this.numberBaseballCount.strike == this.answer.length -> NumberBaseballGameStatus.Correct
            this.numberBaseballCount.strike > 0 || this.numberBaseballCount.ball > 0 -> NumberBaseballGameStatus.Progress
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
        this.increaseInputCount()

        (0 until answerLength).forEach { index ->
            if (this.answer[index] == inputCharList[index]) { this.numberBaseballCount.strike++ }
            else if (this.answer.contains(inputCharList[index])) { this.numberBaseballCount.ball++ }
        }

        return this.validateAnswer()
    }

    /**
     * Input count를 1만큼 증가시킵니다.
     *
     */
    private fun increaseInputCount() {
        this.inputCount += 1
    }

    /**
     * 현재 스트라이크, 볼 카운트를 담고 있는 NumberBaseballCount 객체를 completion으로 전달합니다.
     *
     * @param completion: Strike, Ball 정보를 가진 NumberBaseballCount를 전달합니다
     */
    fun getCurrentGameBallCount(completion: (NumberBaseballCount) -> Unit) {
        completion(this.numberBaseballCount)
    }

    /**
     * 현재 게임 기록을 반환합니다.
     *
     */
    fun getGameRecord() = NumberBaseballGameRecord(this.difficulty, this.inputCount)
}