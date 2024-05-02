import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.example.NumberBaseballGame
import org.example.model.NumberBaseballGameStatus

class NumberBaseballTest : BehaviorSpec({

    Given("정답이 384, 입력이 318 일때") {
        val game = NumberBaseballGame("384")
        val input = "318"

        When("입력하고 볼 카운트를 체크하면") {
            var strike = 0
            var ball = 0
            val status = game.checkInput(input)
            game.getCurrentGameBallCount {
                strike = it.strike
                ball = it.ball
            }

            Then("1스트라이크 1볼이어야 한다") {
                status shouldBe NumberBaseballGameStatus.Progress
                strike shouldBe 1
                ball shouldBe 1
            }
        }
    }

    Given("정답이 384, 입력이 271 일때") {
        val game = NumberBaseballGame("384")
        val input = "271"

        When("입력하고 결게임 상태는") {
            val status = game.checkInput(input)

            Then("Nothing 이어야 한다") {
                status shouldBe NumberBaseballGameStatus.Nothing
            }
        }
    }

    Given("정답이 384, 입력이 384 일때") {
        val game = NumberBaseballGame("384")
        val input = "384"

        When("입력하고 게임 상태는") {
            val status = game.checkInput(input)

            Then("Correct 이어야 한다") {
                status shouldBe NumberBaseballGameStatus.Correct
            }
        }
    }

    Given("정답이 384, 입력이 30 일때") {
        val game = NumberBaseballGame("384")
        val input = "30"

        When("입력하면") {
            val exception = shouldThrow<Exception> { game.checkInput(input) }

            Then("입력값이 올바르지 않아야 한다") {
                exception.message shouldBe "입력값이 올바르지 않습니다."
            }
        }
    }

    Given("정답이 384, 입력이 031 일때") {
        val game = NumberBaseballGame("384")
        val input = "031"

        When("입력하면") {
            val exception = shouldThrow<Exception> { game.checkInput(input) }

            Then("첫번째 숫자를 체크해야 한다") {
                exception.message shouldBe "첫 번째 숫자는 0이 될 수 없습니다."
            }
        }
    }

    Given("정답이 384, 입력이 311 일때") {
        val game = NumberBaseballGame("384")
        val input = "311"

        When("입력하면") {
            val exception = shouldThrow<Exception> { game.checkInput(input) }

            Then("중복값을 체크해야 한다") {
                exception.message shouldBe "중복 값이 있습니다."
            }
        }
    }
})