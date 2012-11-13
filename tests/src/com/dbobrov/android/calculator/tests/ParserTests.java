package com.dbobrov.android.calculator.tests;

import android.test.AndroidTestCase;
import com.dbobrov.android.calculator.parser.ParseException;
import com.dbobrov.android.calculator.parser.RecursiveDescent;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 11.11.12
 * Time: 22:58
 * To change this template use File | Settings | File Templates.
 */
public class ParserTests extends AndroidTestCase {
    public void testSum() {
        RecursiveDescent parser = new RecursiveDescent("1+1");
        try {
            assertEquals(parser.parse(), 2.0, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testSub() {
        RecursiveDescent parser = new RecursiveDescent("0.001-1.0001");
        try {
            assertEquals(parser.parse(), 0.001 - 1.0001, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testSubLeftAssoc() {
        RecursiveDescent parser = new RecursiveDescent("1-1-1");
        try {
            assertEquals(parser.parse(), -1.0, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testMul() {
        RecursiveDescent parser = new RecursiveDescent("-1.5*-100500");
        try {
            assertEquals(parser.parse(), -1.5 * -100500.0, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testDiv() {
        RecursiveDescent parser = new RecursiveDescent("15/5/3");
        try {
            assertEquals(parser.parse(), 1.0, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testDivZero() {
        RecursiveDescent parser = new RecursiveDescent("1/0.0000001");
        try {
            double val = parser.parse();
            assert false;
        } catch (ParseException e) {
            assert false;
        } catch (ArithmeticException e) {
            assertEquals(e.getMessage(), "Division by zero");
        }
    }

    public void testBrackets() {
        RecursiveDescent parser = new RecursiveDescent("1-(1-1)");
        try {
            assertEquals(parser.parse(), 1.0, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testMultiple() {
        RecursiveDescent parser = new RecursiveDescent("10+10-10*0/0.05+0.004/2-(20-10/2)*2");
        try {
            assertEquals(parser.parse(), -9.998, 1e-6);
        } catch (ParseException e) {
            assert false;
        }
    }

    public void testInvalidExpression() {
        RecursiveDescent parser = new RecursiveDescent("()");
        try {
            parser.parse();
            assert false;
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testInvalidBracketSequences() {
        RecursiveDescent parser = new RecursiveDescent("1-(1-1");
        try {
            parser.parse();
            assert false;
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testInvalidNumbers() {
        RecursiveDescent parser = new RecursiveDescent("10000000*100000000000");
        try {
            parser.parse();
            assert false;
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Overflow");
        }
    }

    public void testInvalidExpression1() {
        RecursiveDescent parser = new RecursiveDescent("12-3+");
        try {
            parser.parse();
            assert false;
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testDoubleDots() {
        RecursiveDescent parser = new RecursiveDescent("1.2.3");
        try {
            parser.parse();
            assert false;
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testEmpty() {
        RecursiveDescent parser = new RecursiveDescent("");
        try {
            parser.parse();
            assert false;
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }
}
