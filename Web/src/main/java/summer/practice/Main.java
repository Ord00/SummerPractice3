package summer.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Симонов Иван Алексеевич 3 курс 91 группа ПММ
// Приложение получает на вход строку текста, написанного на языке SQL.
// После этого выполняет лексический анализ введённого запроса,
// т.е. разбивает предложение на слова, каждому из которых ставит в соответствие категорию.
// Если слово распознать не удаётся, то выводится информация об ошибке.
// Распознаватель построен на основе конечных автоматов (FSM).
// В результате работы получается поток токенов - пар лексема-категория
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}