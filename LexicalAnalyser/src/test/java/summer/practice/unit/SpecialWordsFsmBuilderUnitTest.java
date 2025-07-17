package summer.practice.unit;

import org.junit.jupiter.api.Test;
import summer.practice.scanner.builders.special.words.DmlFSMBuilder;
import summer.practice.scanner.builders.special.words.KeywordFSMBuilder;
import summer.practice.scanner.finite.automata.FSM;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpecialWordsFsmBuilderUnitTest {
    @Test
    void testDmlFSMBuilder() {
        DmlFSMBuilder builder = new DmlFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("INSERT"));
        assertTrue(fsm.simulate("DELETE"));
        assertTrue(fsm.simulate("UPDATE"));
        assertTrue(fsm.simulate("SELECT"));

        assertTrue(fsm.simulate("insert"));
        assertTrue(fsm.simulate("Select"));

        assertFalse(fsm.simulate("FROM"));
        assertFalse(fsm.simulate("WHERE"));
        assertFalse(fsm.simulate(""));
        assertFalse(fsm.simulate("INSERTX"));
    }

    @Test
    void testKeywordFSMBuilder() {
        KeywordFSMBuilder builder = new KeywordFSMBuilder();
        FSM fsm = builder.build();

        assertTrue(fsm.simulate("FROM"));
        assertTrue(fsm.simulate("WHERE"));
        assertTrue(fsm.simulate("GROUP"));
        assertTrue(fsm.simulate("ORDER"));
        assertTrue(fsm.simulate("JOIN"));

        assertTrue(fsm.simulate("from"));
        assertTrue(fsm.simulate("Where"));

        assertFalse(fsm.simulate("SELECT"));
        assertFalse(fsm.simulate("WHEREE"));
        assertFalse(fsm.simulate(""));
        assertFalse(fsm.simulate("FROMX"));
    }

    @Test
    void testSharedStateInSpecialWords() {
        DmlFSMBuilder dmlBuilder = new DmlFSMBuilder();
        KeywordFSMBuilder keywordBuilder = new KeywordFSMBuilder();

        FSM dmlFsm = dmlBuilder.build();
        FSM keywordFsm = keywordBuilder.build();

        assertTrue(dmlFsm.simulate("SELECT"));
        assertFalse(keywordFsm.simulate("SELECT"));

        assertTrue(keywordFsm.simulate("FROM"));
        assertFalse(dmlFsm.simulate("FROM"));
    }

    @Test
    void testEmptyInput() {
        DmlFSMBuilder builder = new DmlFSMBuilder();
        FSM fsm = builder.build();

        assertFalse(fsm.simulate(""));
    }
}
