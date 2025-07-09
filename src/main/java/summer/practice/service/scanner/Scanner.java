package summer.practice.service.scanner;

import org.springframework.stereotype.Component;
import summer.practice.service.exceptions.SQLScanException;
import summer.practice.service.scanner.builders.FSMBuilder;
import summer.practice.service.scanner.builders.IdentifierFSMBuilder;
import summer.practice.service.scanner.builders.LiteralFSMBuilder;
import summer.practice.service.scanner.builders.NumberFSMBuilder;
import summer.practice.service.scanner.builders.PunctuationFSMBuilder;
import summer.practice.service.scanner.builders.special.words.DmlFSMBuilder;
import summer.practice.service.scanner.builders.special.words.KeywordFSMBuilder;
import summer.practice.service.scanner.finite.automata.FSM;
import summer.practice.service.enums.Category;
import summer.practice.service.interfaces.LexicallyAnalysable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Scanner implements LexicallyAnalysable {
    private final Map<Category, FSM> fsms;

    public Scanner() {
        fsms = new LinkedHashMap<>();

        Map<Category, FSMBuilder> builders = new LinkedHashMap<>() {{
            put(Category.DML, new DmlFSMBuilder());
            put(Category.KEYWORD, new KeywordFSMBuilder());
            put(Category.IDENTIFIER, new IdentifierFSMBuilder());
            put(Category.NUMBER, new NumberFSMBuilder());
            put(Category.LITERAL, new LiteralFSMBuilder());
            put(Category.PUNCTUATION, new PunctuationFSMBuilder());
        }};

        Category[] categories = Category.values();
        int len = categories.length;

        for (int i = 0; i < len - 2; ++i) {
            FSMBuilder fsmBuilder = builders.get(categories[i]);
            fsms.put(categories[i], fsmBuilder.build());
        }
    }

    public void tryAnalyse(String codeToScan,
                              List<Token> tokens) throws SQLScanException {

        List<String> partsSql = splitIntoParts(codeToScan.trim());

        for (String part : partsSql) {

            boolean isFound = false;

            for (Category category : fsms.keySet()) {

                FSM fsm = fsms.get(category);

                if (fsm.simulate(part)) {

                    tokens.add(new Token(List.of(Category.DML,
                            Category.KEYWORD).contains(category) ? part.toUpperCase() : part, category));
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                throw new SQLScanException(String.format("The lexeme %s is not recognised by the language!",
                        part));
            }
        }
    }

    private List<String> splitIntoParts(String codeToSplit) {

        List<String> specials = new ArrayList<>(List.of(
                " ",
                ",",
                "\\.",
                "\\(",
                "\\)",
                "<",
                ">",
                "=",
                "\n",
                "\t"
        ));

        List<String> parts = new ArrayList<>();
        parts.add(codeToSplit);

        for (String special : specials) {

            String[] curParts = parts.toArray(new String[0]);
            parts.clear();

            for (String part : curParts) {

                String[] splits = part.trim().split(String.format("%s(?=(?:[^']|'[^']*')*[^']*$)",
                        special),
                        -1);

                if (splits.length == 1) {
                    parts.add(splits[0]);
                } else {

                    for (String split : splits) {
                        parts.add(split);
                        parts.add(special.replaceAll("\\\\", ""));
                    }
                    parts.removeLast();

                }

                parts = new ArrayList<>(parts.stream()
                        .map(String::trim)
                        .filter(i -> !i.isBlank())
                        .toList());
            }
        }

        return parts;
    }
}
