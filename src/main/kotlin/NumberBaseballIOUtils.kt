package org.example

import java.io.File

class NumberBaseballIOUtils private constructor() {
    private val file = File(Constants.RECORD_FILE_PATH)

    /**
     * 게임 기록을 파일에서 읽어 줄마다 구분해 반환합니다.
     *
     * @return 게임 기록이 담긴 List<String>
     */
    fun getGameRecords(): List<String> {
        if (this.file.readLines().isEmpty()) throw Exception("게임 기록이 없습니다.")

        return this.file.readLines()
    }

    /**
     * NumberBaseballGameRecord에서 난이도와 입력 횟수를 얻어 파일에 기록합니다.
     *
     * @param 한 게임이 끝난 기록이 담긴 NumberBaseballGameRecord
     */
    fun addGameRecord(gameRecord: NumberBaseballGameRecord) {
        this.file.appendText("${gameRecord.difficulty.name}:${gameRecord.inputCount}\n")
    }

    companion object {
        private var instance: NumberBaseballIOUtils? = null

        /**
         * 유틸리티는 Singleton instance로 관리
         *
         */
        fun getInstance(): NumberBaseballIOUtils = this.instance ?: NumberBaseballIOUtils()
    }
}