import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class TestMode {
    SelenideElement form = $x(".//form");
    SelenideElement error = $x(".//div[@data-test-id='error-notification']");

    @BeforeEach
    public void setup() {open("http://localhost:9999/");}
    @Test
    public void TestUserActive() {
        UserData userActive = UserGeneration.generateUser("active");
        UserRegistration.regisration(userActive);
        form.$x(".//span[@data-test-id='login']//input").val(userActive.getLogin());
        form.$x(".//span[@data-test-id='password']//input").val(userActive.getPassword());
        form.$x(".//button").click();
        $x(".//h2").should(text("Личный кабинет"));
    }

    @Test
    void TestUserBlocked() {
        UserData userBlocked = UserGeneration.generateUser("blocked");
        UserRegistration.regisration(userBlocked);
        form.$x(".//span[@data-test-id='login']//input").val(userBlocked.getLogin());
        form.$x(".//span[@data-test-id='password']//input").val(userBlocked.getPassword());
        form.$x(".//button").click();
        error.should(visible);
        error.$x(".//div[@class='notification__content']").should(text("Пользователь заблокирован"));
        error.$x(".//button").click();
        error.should(hidden);
    }
}