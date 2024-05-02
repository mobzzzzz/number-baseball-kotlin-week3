package org.example

import org.example.model.NumberBaseballDifficulty
import org.example.model.NumberBaseballGameStatus
import org.example.utils.GameRecordIOUtils

private val baseballIOUtils = GameRecordIOUtils.getInstance()

fun main() {
    showMenu()
}

/**
 * 게임 기본 메뉴를 출력한다.
 *
 */
fun showMenu() {
    println("< 게임을 시작합니다 >")

    do {
        println("1. 게임 시작하기 2. 게임 기록 보기 3. 종료하기")
        val menuInput = readln()

        when (menuInput) {
            "1" -> startGame()
            "2" -> showGameRecordMenu()
            "3" -> println("게임을 종료합니다.")
            else -> println("잘못 입력하셨습니다.")
        }
    } while (menuInput != "3")
}

/**
 * 게임 기록 조회 메뉴를 출력한다.
 *
 */
fun showGameRecordMenu() {
    val gameRecords: List<String>
    try {
        gameRecords = baseballIOUtils.getGameRecords()
    } catch (e: Exception) {
        println(e.message)
        return
    }

    do {
        println("1. 전체 기록 보기 2. 랭킹으로 보기 3. 돌아가기")
        val menuInput = readln()

        when (menuInput) {
            "1" -> showGameRecordAll(gameRecords)
            "2" -> showGameRecordRankingMenu(gameRecords)
            "3" -> println("이전 메뉴로 돌아갑니다.")
            else -> println("잘못 입력하셨습니다.")
        }
    } while (menuInput != "3")
}

/**
 * 난이도별로 랭킹 조회를 할 수 있게 선택 메뉴를 출력한다.
 *
 * @param 게임 기록 raw가 담긴 list
 */
fun showGameRecordRankingMenu(records: List<String>) {
    val entries = NumberBaseballDifficulty.entries
    val size = entries.size
    do {
        entries.forEachIndexed { index, difficulty ->
            print("${index + 1}. ${difficulty.name} ")
        }

        println("${size + 1}. 돌아가기")

        val menuInput = readln().toInt()

        try {
            when (menuInput) {
                in 1..size -> showRecordByRanking(records, entries[menuInput - 1])
                size + 1 -> println("이전 메뉴로 돌아갑니다.")
                else -> println("잘못 입력하셨습니다.")
            }
        } catch (e: Exception) {
            println(e.message)
        }
    } while (menuInput != size + 1)
}

/**
 * 모든 게임 기록을 기록된 순서로 출력한다.
 *
 * @param 게임 기록 raw가 담긴 list
 */
fun showGameRecordAll(records: List<String>) {
    splitRecords(records).forEach { println("${it.first} 난이도를 ${it.second}회만에 풀었습니다.") }
}

/**
 * 게임 기록을 선택한 랭킹 순으로 정렬해서 출력한다.
 *
 * @param 게임 기록 raw가 담긴 list
 * @param 게임 난이도
 */
fun showRecordByRanking(records: List<String>, difficulty: NumberBaseballDifficulty) {
    val filteredRecords = splitRecords(records).filter { it.first == difficulty.name }
    if (filteredRecords.isEmpty()) throw Exception("게임 기록이 없습니다.")

    filteredRecords.sortedBy { it.second }.forEachIndexed { index, data ->
            println("${index + 1}등: ${data.second}회")
        }
}

/**
 * 읽어온 게임 기록을 난이도, 횟수로 묶어 반환한다.
 *
 * @param 게임 기록 raw가 담긴 list
 * @return 난이도, 횟수를 묶은 Pair
 */
fun splitRecords(records: List<String>): List<Pair<String, Int>> {
    return records.map { it.split(":").let { data -> Pair(data[0], data[1].toInt()) } }
}

/**
 * 현재 진행중인 게임의 난이도 설정을 바꾼다.
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
 * 게임 인스턴스를 만들고 게임을 진행한다.
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
            } catch (e: Exception) {
                println(e.message)
                return@with
            }

            when (status) {
                NumberBaseballGameStatus.Correct -> {
                    try {
                        baseballIOUtils.addGameRecord(game.getGameRecord())
                    } catch (e: Exception) {
                        println("게임 기록에 실패했습니다. :" + e.message)
                    }

                    println("정답입니다!")
                    println("정답 리셋중...")
                    resetGame()
                }

                NumberBaseballGameStatus.Progress -> {
                    getCurrentGameBallCount {
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