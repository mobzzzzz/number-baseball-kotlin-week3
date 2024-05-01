package org.example

fun main() {
    println("< 게임을 시작합니다 >")
    showMenu()
}

/**
 * 게임 기본 메뉴 출력
 *
 */
fun showMenu() {
    do {
        println("1. 게임 시작하기 2. 게임 기록 보기 3. 종료하기")
        val menuInput = readln()

        when (menuInput) {
            "1" -> startGame()
            "2" -> showGameRecord()
            "3" -> println("게임을 종료합니다.")
            else -> println("잘못 입력하셨습니다.")
        }
    } while (menuInput != "3")
}

/**
 * 게임 기록 출력 (파일입출력 이용해 저장, 불러오기)
 *
 */
fun showGameRecord() {

}

/**
 * 현재 진행중인 게임의 난이도 설정 바꾸기
 *
 * @param game: 현재 진행중인 게임 인스턴스
 */
fun setGameDifficulty(game: NumberBaseballGame) {
    while (true) {
        println("게임 난이도를 선택해주세요.")
        println("1. 보통(3자리) 2. 어려움(4자리) 3. 매우 어려움(5자리)")
        val difficultyInput = readln()

        game.difficulty = when (difficultyInput) {
            "1" -> NumberBaseballDifficulty.Normal
            "2" -> NumberBaseballDifficulty.Hard
            "3" -> NumberBaseballDifficulty.VeryHard
            else -> {
                println("입력값이 올바르지 않습니다.")
                continue
            }
        }

        break
    }
}

/**
 * 게임 인스턴스를 만들고 진행
 *
 */
fun startGame() {
    val game = NumberBaseballGame()

    setGameDifficulty(game)

    while (true) {
        println("숫자를 입력하세요. Q를 입력하면 중단합니다. 현재 난이도: ${game.difficulty.length}자리")
        val input = readln()

        if (input == "Q") {
            println("게임 중단")
            return
        }

        with(game) {
            val status: NumberBaseballGameStatus

            try {
                status = checkInput(input)

                when (status) {
                    NumberBaseballGameStatus.Correct -> {
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