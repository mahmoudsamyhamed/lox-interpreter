public enum TokenType {
    // Single-character tokens;
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // Comparison and Equality Tokens (1 or 2 characters tokens)
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    AND, OR, IF, ELSE, FOR, WHILE, TRUE, FALSE,
    NIL, PRINT, CLASS, THIS, FUN, SUPER, RETURN,
    VAR,

    EOF
}
