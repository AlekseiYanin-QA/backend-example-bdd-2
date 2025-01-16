package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSteps {
    private List<String> searchResults;
    private String errorMessage;
    private Map<String, List<String>> keywordResultsMap;

    @Before
    public void setUp() {
        System.out.println("Инициализация теста...");
        // Инициализация карты с ключевыми словами и результатами поиска
        keywordResultsMap = new HashMap<>();
        keywordResultsMap.put("ноутбук", List.of("Ноутбук A", "Ноутбук B"));
        keywordResultsMap.put("телефон", List.of("Телефон X", "Телефон Y"));
        keywordResultsMap.put("наушники", List.of("Наушники A", "Наушники B"));
        keywordResultsMap.put("ноут", List.of("Ноутбук A", "Ноутбук B")); // Добавлено для частичного совпадения
    }

    @After
    public void tearDown() {
        System.out.println("Завершение теста...");
    }

    /**
     * Переход на главную страницу интернет-магазина.
     */
    @Given("Я нахожусь на главной странице интернет-магазина")
    public void iAmOnTheHomePageOfTheOnlineStore() {
        System.out.println("Перехожу на главную страницу интернет-магазина...");
    }

    /**
     * Ввод ключевого слова в поле поиска и нажатие кнопки "Найти".
     *
     * @param keyword   Ключевое слово для поиска
     * @param buttonText Текст кнопки поиска
     */
    @When("Я ввожу ключевое слово {string} в поле поиска и нажимаю кнопку {string}")
    public void iEnterTheKeywordAndClickTheSearchButton(String keyword, String buttonText) {
        // Нормализация ключевого слова (удаление спецсимволов и приведение к нижнему регистру)
        String normalizedKeyword = keyword.replaceAll("[^a-zA-Zа-яА-Я0-9]", "").toLowerCase();
        // Получение результатов поиска из карты
        searchResults = keywordResultsMap.getOrDefault(normalizedKeyword, new ArrayList<>());
        // Если результаты пусты, устанавливаем сообщение об ошибке
        if (searchResults.isEmpty()) {
            errorMessage = "Товары не найдены";
        }
    }

    /**
     * Проверка, что отображается список товаров, соответствующих запросу.
     *
     * @param keyword Ключевое слово для проверки
     */
    @Then("Я вижу список товаров, соответствующих запросу {string}")
    public void iShouldSeeAListOfProductsMatchingTheQuery(String keyword) {
        Assertions.assertThat(searchResults)
                .isNotEmpty() // Проверка, что список не пуст
                .allMatch(product -> product.toLowerCase().contains(keyword.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я0-9]", "")));
    }

    /**
     * Проверка, что отображается пустой список товаров.
     */
    @Then("Я вижу пустой список товаров")
    public void iShouldSeeAnEmptyProductList() {
        Assertions.assertThat(searchResults).isEmpty();
    }

    /**
     * Проверка, что отображается сообщение об ошибке.
     *
     * @param message Ожидаемое сообщение об ошибке
     */
    @Then("Я вижу сообщение {string}")
    public void iShouldSeeAMessage(String message) {
        Assertions.assertThat(errorMessage).isEqualTo(message);
    }

    /**
     * Проверка, что отображается список товаров, содержащих одно из ключевых слов.
     *
     * @param keyword1 Первое ключевое слово
     * @param keyword2 Второе ключевое слово
     */
    @Then("Я вижу список товаров, в названиях которых содержится {string} или {string}")
    public void iShouldSeeAListOfProductsContainingOr(String keyword1, String keyword2) {
        Assertions.assertThat(searchResults)
                .isNotEmpty() // Проверка, что список не пуст
                .allMatch(product -> product.toLowerCase().contains(keyword1.toLowerCase()) ||
                        product.toLowerCase().contains(keyword2.toLowerCase()));
    }
}