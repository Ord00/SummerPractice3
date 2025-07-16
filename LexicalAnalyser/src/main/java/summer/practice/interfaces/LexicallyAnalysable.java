package summer.practice.interfaces;

import summer.practice.exceptions.SQLScanException;
import summer.practice.scanner.Token;

import java.util.List;

public interface LexicallyAnalysable {
    void tryAnalyse(String codeToScan, List<Token> tokens) throws SQLScanException;
}
