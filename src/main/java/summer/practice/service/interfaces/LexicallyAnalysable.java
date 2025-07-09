package summer.practice.service.interfaces;

import summer.practice.service.exceptions.SQLScanException;
import summer.practice.service.scanner.Token;

import java.util.List;

public interface LexicallyAnalysable {
    void tryAnalyse(String codeToScan, List<Token> tokens) throws SQLScanException;
}
