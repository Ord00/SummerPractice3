package summer.practice.mockito;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import summer.practice.enums.Category;
import summer.practice.exceptions.SQLScanException;
import summer.practice.scanner.Scanner;
import summer.practice.scanner.Token;
import summer.practice.scanner.finite.automata.FSM;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScannerMockitoTest {

    @Mock
    private FSM dmlFsm;
    @Mock
    private FSM keywordFsm;
    @Mock
    private FSM identifierFsm;
    @Mock
    private FSM numberFsm;
    @Mock
    private FSM literalFsm;
    @Mock
    private FSM punctuationFsm;
    @Mock
    private FSM logicalOperatorFsm;

    @InjectMocks
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        Map<Category, FSM> fsms = new LinkedHashMap<>();
        fsms.put(Category.DML, dmlFsm);
        fsms.put(Category.KEYWORD, keywordFsm);
        fsms.put(Category.IDENTIFIER, identifierFsm);
        fsms.put(Category.NUMBER, numberFsm);
        fsms.put(Category.LITERAL, literalFsm);
        fsms.put(Category.PUNCTUATION, punctuationFsm);
        fsms.put(Category.LOGICAL_OPERATOR, logicalOperatorFsm);

        try {
            Field field = Scanner.class.getDeclaredField("fsms");
            field.setAccessible(true);
            field.set(scanner, fsms);
        } catch (Exception e) {
            Fail.fail("Failed to set up test environment");
        }
    }

    @Test
    public void testAnalysisOfKeywordsAndDsm() throws SQLScanException {
        when(dmlFsm.simulate("select")).thenReturn(true);
        when(keywordFsm.simulate("from")).thenReturn(true);
        List<Token> tokens = new ArrayList<>();

        scanner.tryAnalyse("select from", tokens);

        Assertions.assertEquals(2, tokens.size());
        Assertions.assertEquals("SELECT", tokens.get(0).lexeme);
        Assertions.assertEquals("FROM", tokens.get(1).lexeme);
    }

    @Test
    public void testAnalysisOfComplexQuery() throws SQLScanException {
        List<Token> expectedTokens = new ArrayList<>(List.of(
                new Token("SELECT", Category.DML),
                new Token("name", Category.IDENTIFIER),
                new Token(",", Category.PUNCTUATION),
                new Token("age", Category.IDENTIFIER),
                new Token("FROM", Category.KEYWORD),
                new Token("users", Category.IDENTIFIER),
                new Token("WHERE", Category.KEYWORD),
                new Token("age", Category.IDENTIFIER),
                new Token(">", Category.LOGICAL_OPERATOR),
                new Token("18", Category.NUMBER)
        ));

        configureFlexibleStubs();

        when(dmlFsm.simulate("SELECT")).thenReturn(true);
        when(logicalOperatorFsm.simulate(">")).thenReturn(true);

        List<Token> tokens = new ArrayList<>();
        scanner.tryAnalyse("SELECT name, age FROM users WHERE age > 18", tokens);

        Assertions.assertEquals(expectedTokens, tokens);
    }

    private void configureFlexibleStubs() {
        lenient().when(numberFsm.simulate(anyString()))
                .thenAnswer(inv -> inv.getArgument(0).equals("18"));

        lenient().when(punctuationFsm.simulate(anyString()))
                .thenAnswer(inv -> inv.getArgument(0).equals(","));

        lenient().when(keywordFsm.simulate(anyString()))
                .thenAnswer(inv -> {
                    String arg = inv.getArgument(0);
                    return arg.equalsIgnoreCase("FROM") ||
                            arg.equalsIgnoreCase("WHERE");
                });

        lenient().when(identifierFsm.simulate(anyString()))
                .thenAnswer(inv -> {
                    String arg = inv.getArgument(0);
                    return arg.equals("name") ||
                            arg.equals("age") ||
                            arg.equals("users");
                });
    }
}
