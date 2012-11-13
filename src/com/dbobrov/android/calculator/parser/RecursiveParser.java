package com.dbobrov.android.calculator.parser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 11.11.12
 * Time: 19:51
 */

public class RecursiveParser {
    private final String expressionString;

    private static class ParseHolder {
        int pos;
        double value;

        public ParseHolder(int pos, double value) {
            this.pos = pos;
            this.value = value;
        }
    }

    public RecursiveParser(String expression) {
        this.expressionString = expression.replaceAll(" ", "");
    }

    public String getResult() throws ParseException {
        try {
            ParseHolder holder = parseExpr(0);
            if (holder.pos != expressionString.length()) {
                throw new ParseException("Invalid expression");
            }
            return round(holder.value);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid expression");
        }
    }

    /*
    expr ::= term expr_rest
    expr_rest ::= [('+'|'-') term expr_rest]
    term ::= factor term_rest
    term_rest ::= [('*'|'/') factor term_rest]
    factor ::= (NUMBER | '(' expr ')' | '-' factor)
     */

    private ParseHolder parseExpr(int pos) throws ParseException {
        ParseHolder p1 = parseTerm(pos);
        if (p1.pos < expressionString.length()) {
            ParseHolder p2 = parseExprRest(p1.pos);
            p1.pos = p2.pos;
            p1.value = p1.value + p2.value;
        }
        if (Math.abs(p1.value) > 1e11) {
            throw new ParseException("Overflow");
        }
        return p1;
    }

    private ParseHolder parseExprRest(int pos) throws ParseException {
        double m;
        switch (expressionString.charAt(pos)) {
            case '+':
                m = 1.0;
                break;
            case '-':
                m = -1.0;
                break;
            default:
                return new ParseHolder(pos, 0.0);
        }
        ParseHolder p1 = parseTerm(pos + 1);
        p1.value *= m;
        if (p1.pos < expressionString.length()) {
            ParseHolder p2 = parseExprRest(p1.pos);
            p1.pos = p2.pos;
            p1.value = (p1.value + p2.value);
        }
        return p1;
    }

    private ParseHolder parseTerm(int pos) throws ParseException {
        ParseHolder p1 = parseFactor(pos);
        if (p1.pos < expressionString.length()) {
            ParseHolder p2 = parseTermRest(p1.pos);
            p1.pos = p2.pos;
            p1.value = p1.value * p2.value;
        }
        return p1;
    }

    private ParseHolder parseTermRest(int pos) throws ParseException {
        boolean isDivision;
        switch (expressionString.charAt(pos)) {
            case '*':
                isDivision = false;
                break;
            case '/':
                isDivision = true;
                break;
            default:
                return new ParseHolder(pos, 1.0);
        }
        ParseHolder p1 = parseFactor(pos + 1);
        if (isDivision) {
            if (Math.abs(p1.value) < 1e-10) {
                throw new ParseException("Division by zero");
            }
            p1.value = 1.0 / p1.value;
        }
        if (p1.pos < expressionString.length()) {
            ParseHolder p2 = parseTermRest(p1.pos);
            p1.pos = p2.pos;
            p1.value = p1.value * p2.value;
        }
        if (Math.abs(p1.value) > 1e11) {
            throw new ParseException("Overflow");
        }
        return p1;
    }

    private ParseHolder parseFactor(int pos) throws ParseException {
        switch (expressionString.charAt(pos)) {
            case '(':
                ParseHolder p1 = parseExpr(pos + 1);
                if (expressionString.charAt(p1.pos) == ')') {
                    p1.pos++;
                    return p1;
                }
                throw new ParseException("Invalid expression");
            case '-':
                ParseHolder p2 = parseFactor(pos + 1);
                p2.value *= -1.0;
                return p2;
            default:
                char c = expressionString.charAt(pos);
                if (!Character.isDigit(c)) {
                    throw new ParseException("Invalid expression");
                }
                return tryParseNumber(pos);
        }
    }

    private ParseHolder tryParseNumber(int pos) throws ParseException {
        boolean dotPassed = false;
        int i;
        for (i = pos; i < expressionString.length(); ++i) {
            char c = expressionString.charAt(i);
            if (Character.isDigit(c)) continue;
            if (c == '.') {
                if (dotPassed) {
                    throw new ParseException("Invalid expression");
                }
                dotPassed = true;
                continue;
            }
            break;
        }
        double val = Double.parseDouble(expressionString.substring(pos, i));
        if (Math.abs(val) < 1e-10) {
            if (val != 0.0) {
                throw new ParseException("Underflow");
            }
            val = 0.0;
        } else if (Math.abs(val) > 1e10) {
            throw new ParseException("Overflow");
        }
        return new ParseHolder(i, val);
    }

    private static final DecimalFormat FORMAT = new DecimalFormat("#.#####", new DecimalFormatSymbols(Locale.US));

    private static String round(double d) {
        return FORMAT.format(d);
    }
}
