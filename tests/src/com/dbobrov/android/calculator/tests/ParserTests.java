package com.dbobrov.android.calculator.tests;

import android.test.AndroidTestCase;
import com.dbobrov.android.calculator.parser.ParseException;
import com.dbobrov.android.calculator.parser.RecursiveParser;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 11.11.12
 * Time: 22:58
 */
public class ParserTests extends AndroidTestCase {
    public void testSum() {
        RecursiveParser parser = new RecursiveParser("1+1");
        try {
            assertEquals(parser.getResult(), "2");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testSub() {
        RecursiveParser parser = new RecursiveParser("0.001-1.0001");
        try {
            assertEquals(parser.getResult(), "-0.9991");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testSubLeftAssoc() {
        RecursiveParser parser = new RecursiveParser("1-1-1");
        try {
            assertEquals(parser.getResult(), "-1");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testMul() {
        RecursiveParser parser = new RecursiveParser("-1.5*(-100500)");
        try {
            assertEquals(parser.getResult(), "150750");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testDiv() {
        RecursiveParser parser = new RecursiveParser("15/5/3");
        try {
            assertEquals(parser.getResult(), "1");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testDivZero() {
        RecursiveParser parser = new RecursiveParser("1/0");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Division by zero");
        }
    }

    public void testBrackets() {
        RecursiveParser parser = new RecursiveParser("1-(1-1)");
        try {
            assertEquals(parser.getResult(), "1");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testMultiple() {
        RecursiveParser parser = new RecursiveParser("10+10-10*0/0.05+0.004/2-(20-10/2)*2");
        try {
            assertEquals(parser.getResult(), "-9.998");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testInvalidExpression() {
        RecursiveParser parser = new RecursiveParser("()");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testInvalidBracketSequences() {
        RecursiveParser parser = new RecursiveParser("1-(1-1");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testInvalidNumbers() {
        RecursiveParser parser = new RecursiveParser("10000000*100000000000");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Overflow");
        }
    }

    public void testInvalidExpression1() {
        RecursiveParser parser = new RecursiveParser("12-3+");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testDoubleDots() {
        RecursiveParser parser = new RecursiveParser("1.2.3");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }

    public void testEmpty() {
        RecursiveParser parser = new RecursiveParser("");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Invalid expression");
        }
    }


    public void testDiv1() {
        RecursiveParser parser = new RecursiveParser("1/(10 - 1000000000/999999999)");
        try {
            assertEquals(parser.getResult(), "0.11111");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testDiv2() {
        RecursiveParser parser = new RecursiveParser("1/(10 - 10000000000/999999999)");
        try {
            assertEquals(parser.getResult(), "-99999991.72596");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testUnderflow() {
        RecursiveParser parser = new RecursiveParser("0.00000000001");
        try {
            parser.getResult();
            assertTrue(false);
        } catch (ParseException e) {
            assertEquals(e.getMessage(), "Underflow");
        }
    }

    public void testNumberWithEndingDot() {
        RecursiveParser parser = new RecursiveParser("1.");
        try {
            assertEquals(parser.getResult(), "1");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }


    public void testUnaryMinusWithoutBrackets() {
        RecursiveParser parser = new RecursiveParser("-1 - -1");
        try {
            assertEquals(parser.getResult(), "0");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    public void testIgnoreSpaces() {
        RecursiveParser parser = new RecursiveParser("1 000 000");
        try {
            assertEquals(parser.getResult(), "1000000");
        } catch (ParseException e) {
            assertTrue(false);
        }
    }
}
