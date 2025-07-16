package summer.practice.dto;

import lombok.Getter;
import summer.practice.scanner.Token;

import java.util.List;

@Getter
public class AnalysisResult {

    private final List<Token> lexicalResult;

    public AnalysisResult(List<Token> lexical) {
        this.lexicalResult = lexical;
    }

}
