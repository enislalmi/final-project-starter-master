package filters;

/**
 * Parse a string in the filter language and return the filter.
 * Throws a SyntaxError exception on failure.
 *
 * This is a top-down recursive descent parser (a.k.a., LL(1))
 *
 * The really short explanation is that an LL(1) grammar can be parsed by a collection
 * of mutually recursive methods, each of which is able to recognize a single grammar symbol.
 *
 * The grammar (EBNF) for our filter language is:
 *
 * goal    ::= expr
 * expr    ::= orexpr
 * orexpr  ::= andexpr ( "or" andexpr )*
 * andexpr ::= notexpr ( "and" notexpr )*
 * notexpr ::= prim | "not" notexpr
 * prim    ::= word | "(" expr ")"
 *
 * The reason for writing it this way is that it respects the "natural" precedence of boolean
 * expressions, where the precedence order (decreasing) is:
 *      parens
 *      not
 *      and
 *      or
 * This allows an expression like:
 *      blue or green and not red or yellow and purple
 * To be parsed like:
 *      blue or (green and (not red)) or (yellow and purple)
 */
public class Parser {
    private final Scanner scanner;
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String OR_OPERATOR = "or";
    private static final String AND_OPERATOR = "and";
    private static final String NOT_OPERATOR = "not";

    public Parser(String input) {
        scanner = new Scanner(input);
    }

    public Filter parse() throws SyntaxError {
        Filter answer = or_expression();
        if (scanner.peek() != null) {
            throw new SyntaxError("Extra stuff at end of input");
        }
        return answer;
    }

    private Filter or_expression() throws SyntaxError {
        Filter sub = and_expression();
        String token = scanner.peek();
        while (token != null && token.equals(OR_OPERATOR)) {
            scanner.advance();
            Filter right = and_expression();
            sub = new OrFilter(sub, right);
            token = scanner.peek();
        }
        return sub;
    }

    private Filter and_expression() throws SyntaxError {
        Filter sub = not_expression();
        String token = scanner.peek();
        while (token != null && token.equals(AND_OPERATOR)) {
            scanner.advance();
            Filter right = not_expression();
            sub = new AndFilter(sub, right);
            token = scanner.peek();
        }
        return sub;
    }

    private Filter not_expression() throws SyntaxError {
        String token = scanner.peek();
        if (token.equals(NOT_OPERATOR)) {
            scanner.advance();
            Filter sub = not_expression();
            return new NotFilter(sub);
        } //deleted the whole else as it returned the same thing as prim
        return prim();

    }

    private Filter prim() throws  SyntaxError {
        String token = scanner.peek();
        if (token.equals(LEFT_PARENTHESIS)) {
            scanner.advance();
            Filter sub = or_expression();
            if (!scanner.peek().equals(RIGHT_PARENTHESIS)) {
                throw new SyntaxError("Expected ')'");
            }
            scanner.advance();
            return sub;
        }

        Filter sub = new BasicFilter(token);
        scanner.advance();
        return sub;

    }
}