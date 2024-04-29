package org.example

fun main() {
    val numberBaseballGame = NumberBaseballGame()
    println("< 게임을 시작합니다 >")
    var input = ""

    while (input != "3") {
        println("숫자를 입력하세요")
        input = readln()
        with(numberBaseballGame) {
            val status = checkInput(input)

            when (status) {
                NumberBaseballGameStatus.Complete -> {
                    println("정답입니다!")
                    println("정답 리셋중...")
                    resetGame()
                }
                NumberBaseballGameStatus.Progress -> {
                    getCurrentGameCount {
                        if (it.strike != 0) print("${it.strike}스트라이크 ")
                        if (it.ball != 0) print("${it.ball}볼 ")
                        println()
                    }
                }
                NumberBaseballGameStatus.Nothing -> {
                    println("Nothing")
                }
            }
        }
    }
}