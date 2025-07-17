package summer.practice.unit;

import org.junit.jupiter.api.Test;
import summer.practice.scanner.builders.IdentifierFSMBuilder;
import summer.practice.scanner.builders.LiteralFSMBuilder;
import summer.practice.scanner.builders.LogicalOperatorFSMBuilder;
import summer.practice.scanner.builders.NumberFSMBuilder;
import summer.practice.scanner.builders.PunctuationFSMBuilder;
import summer.practice.scanner.finite.automata.FSM;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FSMBuilderUnitTest {

    @Test
    public void testIdentifierFSMBuilder() {
        IdentifierFSMBuilder builder = new IdentifierFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("a"));
        assertTrue(fsm.simulate("A"));
        assertTrue(fsm.simulate("_"));
        assertTrue(fsm.simulate("a1"));
        assertTrue(fsm.simulate("var_name"));

        assertFalse(fsm.simulate("1"));
        assertFalse(fsm.simulate("1var"));
        assertFalse(fsm.simulate("@"));
    }

    @Test
    public void testLiteralFSMBuilder() {
        LiteralFSMBuilder builder = new LiteralFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("''"));
        assertTrue(fsm.simulate("'a'"));
        assertTrue(fsm.simulate("'abc'"));

        assertFalse(fsm.simulate("'"));
        assertFalse(fsm.simulate("abc"));
        assertFalse(fsm.simulate("a'b'"));
    }

    @Test
    public void testLogicalOperatorFSMBuilder() {
        LogicalOperatorFSMBuilder builder = new LogicalOperatorFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("="));
        assertTrue(fsm.simulate("<"));
        assertTrue(fsm.simulate(">"));

        assertFalse(fsm.simulate("=="));
        assertFalse(fsm.simulate("<="));
        assertFalse(fsm.simulate("!"));
    }

    @Test
    public void testNumberFSMBuilder() {
        NumberFSMBuilder builder = new NumberFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("0"));
        assertTrue(fsm.simulate("123"));
        assertTrue(fsm.simulate("-1"));
        assertTrue(fsm.simulate("-123"));

        assertFalse(fsm.simulate("-"));
        assertFalse(fsm.simulate("1a"));
        assertFalse(fsm.simulate("1.0"));
    }

    @Test
    public void testPunctuationFSMBuilder() {
        PunctuationFSMBuilder builder = new PunctuationFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("."));
        assertTrue(fsm.simulate(","));
        assertTrue(fsm.simulate("("));
        assertTrue(fsm.simulate(")"));

        assertFalse(fsm.simulate(".."));
        assertFalse(fsm.simulate(";"));
        assertFalse(fsm.simulate("a"));
    }
}
