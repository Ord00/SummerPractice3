package summer.practice.dto;

import summer.practice.scanner.Token;

import java.util.List;

public class AnalysisResult {

    private final List<Token> lexicalResult;

    public AnalysisResult(List<Token> lexical) {
        this.lexicalResult = lexical;
    }

    public List<Token> getLexicalResult() {
        return lexicalResult;
    }
}
