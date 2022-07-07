import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class ChangeDataTest {
    SelenideElement form = $("form");
    SelenideElement success = $("div[data-test-id='success-notification']");
    SelenideElement replan = $("div[data-test-id='replan-notification']");

    @BeforeEach
    public void setup() {open("http://localhost:9999/");}
    @Test
    void shouldSubmitRequest() {
        UserData user = UserGenerator.generateUser(7);
        form.$x(".//span[@data-test-id='city']//input").val(user.getCity());
        form.$x(".//span[@data-test-id='date']//input[@class='input__control']").doubleClick().sendKeys(Keys.BACK_SPACE);
        form.$x(".//span[@data-test-id='date']//input[@class='input__control']").val(UserGenerator.generateData(17));
        form.$x(".//span[@data-test-id='name']//input").val(user.getName());
        form.$x(".//span[@data-test-id='phone']//input").val(user.getPhone());
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//button//ancestor::span[contains(text(), 'Запланировать')]").click();

        success.should(visible, ofSeconds(15));
        success.$x(".//div[@class='notification__content']").should(text("Встреча успешно запланирована на " +
                UserGenerator.generateData(17)));
        success.$x(".//button").click();
        success.should(hidden);

        form.$x(".//input[@placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.BACK_SPACE);
        form.$x(".//input[@placeholder=\"Дата встречи\"]").val(UserGenerator.generateData(21));
        form.$x(".//button//ancestor::span[contains(text(), 'Запланировать')]").click();

        replan.should(visible, ofSeconds(15));
        replan.$x(".//span[contains(text(), 'Перепланировать')]//ancestor::button").click();
        replan.should(hidden);

        success.should(visible);
        success.$x(".//div[@class='notification__content']").should(text("Встреча успешно запланирована на " +
                UserGenerator.generateData(21)));
        success.$x(".//button").click();
        success.should(hidden);
    }
}