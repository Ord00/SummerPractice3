package summer.practice.unit;

import org.junit.jupiter.api.Test;
import summer.practice.dto.AnalysisResult;
import summer.practice.enums.Category;
import summer.practice.scanner.Token;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalysisResultUnitTest {
    @Test
    public void testAnalysisResultCreation() {
        List<Token> testTokens = List.of(
                new Token("SELECT", Category.DML),
                new Token("id", Category.IDENTIFIER),
                new Token("FROM", Category.KEYWORD),
                new Token("users", Category.IDENTIFIER)
        );

        AnalysisResult result = new AnalysisResult(testTokens);

        assertNotNull(result);
        assertEquals(4, result.getLexicalResult().size());
        assertEquals("SELECT", result.getLexicalResult().getFirst().lexeme);
        assertEquals(Category.DML, result.getLexicalResult().getFirst().category);
    }

    @Test
    public void testAnalysisResultWithEmptyList() {
        AnalysisResult result = new AnalysisResult(List.of());

        assertNotNull(result);
        assertTrue(result.getLexicalResult().isEmpty());
    }
}
