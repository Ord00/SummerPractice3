package summer.practice.mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import summer.practice.controller.AnalysisController;
import summer.practice.dto.AnalysisResult;
import summer.practice.dto.SqlRequest;
import summer.practice.enums.Category;
import summer.practice.exceptions.SQLScanException;
import summer.practice.scanner.Scanner;
import summer.practice.scanner.Token;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalysisControllerMockitoTest {
    @Mock
    private Scanner scanner;

    @InjectMocks
    private AnalysisController analysisController;

    private SqlRequest emptyRequest;

    @BeforeEach
    public void setUp() {
        emptyRequest = new SqlRequest();
        emptyRequest.setSqlQuery("");
    }

    @Test
    public void testOfAnalysisOfValidQuery() throws SQLScanException {
        String testSql = "SELECT id FROM users";
        List<Token> expectedTokens = List.of(
                new Token("SELECT", Category.DML),
                new Token("id", Category.IDENTIFIER),
                new Token("FROM", Category.KEYWORD),
                new Token("users", Category.IDENTIFIER)
        );

        List<Token> actualTokensPassedToScanner = new ArrayList<>();
        doAnswer(invocation -> {
            List<Token> tokens = invocation.getArgument(1);
            tokens.addAll(expectedTokens);
            actualTokensPassedToScanner.addAll(tokens);
            return null;
        }).when(scanner).tryAnalyse(eq(testSql), any(List.class));

        SqlRequest request = new SqlRequest();
        request.setSqlQuery(testSql);

        ResponseEntity<?> response = analysisController.analyseSql(request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertInstanceOf(AnalysisResult.class, response.getBody());

        AnalysisResult result = (AnalysisResult) response.getBody();

        Assertions.assertEquals(expectedTokens, result.getLexicalResult());
        verify(scanner).tryAnalyse(eq(testSql), any(List.class));
        Assertions.assertEquals(expectedTokens.size(), actualTokensPassedToScanner.size());
    }

    @Test
    public void testAnalysisOfBadQuery() throws SQLScanException {
        doThrow(new SQLScanException("SQL query can't be empty"))
                .when(scanner)
                .tryAnalyse(eq(""), any(List.class));

        ResponseEntity<?> response = analysisController.analyseSql(emptyRequest);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("SQL query can't be empty", response.getBody());
        verify(scanner, atLeastOnce()).tryAnalyse(emptyRequest.getSqlQuery(), new ArrayList<>());
    }
}
