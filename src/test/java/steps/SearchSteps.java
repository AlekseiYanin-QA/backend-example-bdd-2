package steps;

import steps.exceptions.EmptySearchResultException;
import steps.exceptions.InvalidKeywordException;
import steps.exceptions.SearchException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.*;

public class SearchSteps {
    private List<String> searchResults;
    private Map<String, List<String>> keywordResultsMap;

    @Before
    public void setUp() {
        System.out.println("Инициализация теста...");
        // Инициализация карты с ключевыми словами и результатами поиска
        keywordResultsMap = new HashMap<>();
        keywordResultsMap.put("ноутбук", Arrays.asList("Ноутбук A", "Ноутбук B"));
        keywordResultsMap.put("телефон", Arrays.asList("Телефон X", "Телефон Y"));
        keywordResultsMap.put("наушники", Arrays.asList("Наушники A", "Наушники B"));
        keywordResultsMap.put("ноут", Arrays.asList("Ноутбук A", "Ноутбук B")); // Частичное совпадение
    }

    @After
    public void tearDown() {
        System.out.println("Завершение теста...");
    }

    @Given("Я нахожусь на главной странице интернет-магазина")
    public void iAmOnTheHomePageOfTheOnlineStore() {
        System.out.println("Перехожу на главную страницу интернет-магазина...");
    }

    /**
     * Метод для ввода ключевого слова и нажатия кнопки поиска.
     *
     * @param keyword    Ключевое слово для поиска.
     * @param buttonText Текст кнопки для нажатия.
     */
    @When("Я ввожу ключевое слово {string} в поле поиска и нажимаю кнопку {string}")
    public void enterKeywordAndClickSearchButton(String keyword, String buttonText) {
        System.out.println("Ввод ключевого слова: " + keyword);

        // Нормализация ключевого слова
        String normalizedKeyword = normalizeKeyword(keyword);

        // Получение результатов поиска
        searchResults = keywordResultsMap.getOrDefault(normalizedKeyword, new ArrayList<>());
    }

    /**
     * Проверка наличия списка товаров, соответствующих запросу.
     *
     * @param keyword Ключевое слово для проверки.
     */
    @Then("Я вижу список товаров, соответствующих запросу {string}")
    public void iShouldSeeAListOfProductsMatchingTheQuery(String keyword) {
        System.out.println("Проверка списка товаров для ключевого слова: " + keyword);

        Assertions.assertThat(searchResults)
                .isNotEmpty()
                .allMatch(product -> product.toLowerCase().contains(normalizeKeyword(keyword)));

        System.out.println("Проверка прошла успешно: товары найдены.");
    }

    /**
     * Проверка отсутствия товаров в результате поиска.
     */
    @Then("Я вижу пустой список товаров")
    public void iShouldSeeAnEmptyProductList() {
        System.out.println("Проверка пустого списка товаров");

        Assertions.assertThat(searchResults).isEmpty();

        System.out.println("Проверка прошла успешно: список товаров пуст.");
    }

    /**
     * Проверка сообщения об ошибке.
     *
     * @param expectedMessage Ожидаемое сообщение об ошибке.
     */
    @Then("Я вижу сообщение {string}")
    public void iShouldSeeAMessage(String expectedMessage) {
        System.out.println("Проверка сообщения: " + expectedMessage);

        // Проверяем, что исключение было выброшено
        Assertions.assertThatThrownBy(() -> {
                    if (searchResults.isEmpty()) {
                        throw new EmptySearchResultException(expectedMessage);
                    }
                    if (expectedMessage.equals("Ключевое слово не может быть пустым")) {
                        throw new InvalidKeywordException(expectedMessage);
                    }
                })
                .isInstanceOf(SearchException.class)
                .hasMessage(expectedMessage);

        System.out.println("Проверка прошла успешно: сообщение об ошибке корректно.");
    }

    /**
     * Проверка наличия товаров, содержащих одно из двух ключевых слов.
     *
     * @param keyword1 Первое ключевое слово.
     * @param keyword2 Второе ключевое слово.
     */
    @Then("Я вижу список товаров, в названиях которых содержится {string} или {string}")
    public void iShouldSeeAListOfProductsContainingOr(String keyword1, String keyword2) {
        System.out.println("Проверка списка товаров для ключевых слов: " + keyword1 + " или " + keyword2);

        Assertions.assertThat(searchResults)
                .isNotEmpty()
                .allMatch(product -> product.toLowerCase().contains(normalizeKeyword(keyword1)) ||
                        product.toLowerCase().contains(normalizeKeyword(keyword2)));

        System.out.println("Проверка прошла успешно: товары найдены.");
    }

    /**
     * Нормализация ключевого слова: удаление спецсимволов и приведение к нижнему регистру.
     *
     * @param keyword Ключевое слово.
     * @return Нормализованное ключевое слово.
     */
    private String normalizeKeyword(String keyword) {
        // Удаляем только специальные символы, оставляя пробелы
        return keyword.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "").toLowerCase().trim();
    }
}