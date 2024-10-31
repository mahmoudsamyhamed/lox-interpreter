import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Scanner {
    private final String src;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("nil", TokenType.NIL);
        keywords.put("print", TokenType.PRINT);
        keywords.put("class", TokenType.CLASS);
        keywords.put("this", TokenType.THIS);
        keywords.put("fun", TokenType.FUN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("return", TokenType.RETURN);
        keywords.put("var", TokenType.VAR);
    }

    Scanner(String src) {
        this.src = src;
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private  void scanToken() {
        char c = advance();

        switch (c) {
            case '(' : addToken(TokenType.LEFT_BRACE) ; break;
            case ')' : addToken(TokenType.RIGHT_BRACE); break;
            case '{' : addToken(TokenType.LEFT_PAREN); break;
            case '}' : addToken(TokenType.RIGHT_PAREN); break;
            case ',' : addToken(TokenType.COMMA); break;
            case '.' : addToken(TokenType.DOT); break;
            case '+' : addToken(TokenType.PLUS); break;
            case '-' : addToken(TokenType.MINUS); break;
            case '*' : addToken(TokenType.STAR); break;
            case ':' : addToken(TokenType.SEMICOLON); break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '/':
                if(match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                while(peek() != '"' && !isAtEnd()) {
                    if(peek() == '\n') line++;
                    advance();
                }
                if(isAtEnd()) {
                   Main.error(line, "Unterminated string.");
                   return;
                }

                advance();

                String value = src.substring(start + 1, current - 1);
                addToken(TokenType.STRING, value);
                break;
            default:
                if(isDigit(c)) {
                   number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Main.error(line, "Unexpected Character.");
                }
                break;
        }
    }

    private boolean match(char expected) {
        if(isAtEnd()) return false;

        if(src.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private void number() {
        while (isDigit(peek())) advance();

        if(peek() == '.' && isDigit(peekNext())) {

            do advance();
            while (isDigit(peek()));
        }

        addToken(TokenType.NUMBER, Double.parseDouble(src.substring(start, current)));
    }

    private void identifier() {
        while(isAlphaNumeric(peek())) advance();

        String text = src.substring(start, current);
        TokenType type = keywords.get(text);

        if(type == null) type = TokenType.IDENTIFIER;

        addToken(type);
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private boolean isAlpha(char c) {
        return 'a' <= c && c <= 'z'  ||
                'A' <= c && c <= 'Z' ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private char peekNext() {
        if(current + 1 >= src.length()) return '\0';

        return src.charAt(current + 1);
    }

    private char peek() {
        if(isAtEnd()) return '\0';

        return src.charAt(current);
    }

    private char advance() {
        return src.charAt(current++);
    }


    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = src.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    boolean isAtEnd() {
        return current >= src.length();
    }
}
