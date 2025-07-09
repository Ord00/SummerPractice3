package summer.practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import summer.practice.dto.AnalysisResult;
import summer.practice.dto.SqlRequest;
import summer.practice.service.exceptions.SQLScanException;
import summer.practice.service.scanner.Scanner;
import summer.practice.service.scanner.Token;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class AnalysisController {

    private Scanner scanner;

    @Autowired
    public AnalysisController(Scanner scanner) {
        this.scanner = scanner;
    }

    @PostMapping("/analyse")
    public ResponseEntity<?> analyseSql(@RequestBody SqlRequest request) {

        try {
            List<Token> lexicalResult = new ArrayList<>();

            // Лексический анализ
            scanner.tryAnalyse(request.getSqlQuery(), lexicalResult);

            return ResponseEntity.ok(new AnalysisResult(lexicalResult));

        } catch (SQLScanException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
