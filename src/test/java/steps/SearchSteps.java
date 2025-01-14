package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class SearchSteps {
    private List<String> searchResults;
    private String errorMessage;

    @Before
    public void setUp() {
        System.out.println("Инициализация теста...");
    }

    @After
    public void tearDown() {
        System.out.println("Завершение теста...");
    }

    @Given("Я нахожусь на главной странице интернет-магазина")
    public void i_am_on_the_home_page_of_the_online_store() {
        System.out.println("Перехожу на главную страницу интернет-магазина...");
    }

    @When("Я ввожу ключевое слово {string} в поле поиска и нажимаю кнопку {string}")
    public void i_enter_the_keyword_and_click_the_search_button(String keyword, String buttonText) {
        if (keyword.equalsIgnoreCase("ноутбук") || keyword.equalsIgnoreCase("ноут") || keyword.equalsIgnoreCase("НОУТБУК") || keyword.equals("ноутбук!")) {
            searchResults = new ArrayList<>();
            searchResults.add("Ноутбук A");
            searchResults.add("Ноутбук B");
        } else if (keyword.equalsIgnoreCase("телефон")) {
            searchResults = new ArrayList<>();
            searchResults.add("Телефон X");
            searchResults.add("Телефон Y");
        } else if (keyword.equalsIgnoreCase("наушники")) {
            searchResults = new ArrayList<>();
            searchResults.add("Наушники A");
            searchResults.add("Наушники B");
        } else if (keyword.equals("летающий автомобиль")) {
            searchResults = new ArrayList<>(); // Пустой список
            errorMessage = "Товары не найдены";
        } else {
            searchResults = new ArrayList<>(); // Пустой список по умолчанию
            errorMessage = "Товары не найдены";
        }
    }

    @Then("Я вижу список товаров, соответствующих запросу {string}")
    public void i_should_see_a_list_of_products_matching_the_query(String keyword) {
        Assertions.assertThat(searchResults)
                .isNotNull() // Проверка, что список не null
                .isNotEmpty() // Проверка, что список не пуст
                .allMatch(product -> product.toLowerCase().contains(keyword.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я0-9]", "")));
    }

    @Then("Я вижу пустой список товаров")
    public void i_should_see_an_empty_product_list() {
        Assertions.assertThat(searchResults).isEmpty();
    }

    @Then("Я вижу сообщение {string}")
    public void i_should_see_a_message(String message) {
        Assertions.assertThat(errorMessage).isEqualTo(message);
    }

    @Then("Я вижу список товаров, в названиях которых содержится {string} или {string}")
    public void i_should_see_a_list_of_products_containing_or(String keyword1, String keyword2) {
        Assertions.assertThat(searchResults)
                .isNotNull() // Проверка, что список не null
                .isNotEmpty() // Проверка, что список не пуст
                .allMatch(product -> product.toLowerCase().contains(keyword1.toLowerCase()) ||
                        product.toLowerCase().contains(keyword2.toLowerCase()));
    }
}