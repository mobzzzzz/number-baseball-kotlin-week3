package org.example

fun main() {
    val numberBaseballGame = NumberBaseballGame()
    println("< 게임을 시작합니다 >")

    while (true) {
        println("숫자를 입력하세요")
        var input = readln()
        if (input == "3") break

        with(numberBaseballGame) {
            val status: NumberBaseballGameStatus

            try {
                status = checkInput(input)

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
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}