package summer.practice.dto;

import lombok.Getter;
import summer.practice.service.scanner.Token;

import java.util.List;

@Getter
public class AnalysisResult {

    private List<Token> lexicalResult;

    public AnalysisResult(List<Token> lexical) {
        this.lexicalResult = lexical;
    }

}
