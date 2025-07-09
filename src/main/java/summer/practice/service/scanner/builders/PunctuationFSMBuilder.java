package summer.practice.service.scanner.builders;

import summer.practice.service.scanner.finite.automata.FSM;
import summer.practice.service.scanner.finite.automata.FSMState;
import summer.practice.service.scanner.finite.automata.FSMTransition;

import java.util.ArrayList;
import java.util.List;

public class PunctuationFSMBuilder extends FSMBuilder {

    @Override
    public FSM build() {
        var punctuations = new ArrayList<>(List.of(
                '.',
                ',',
                '(',
                ')'
        ));

        var fsm = new FSM();

        FSMState state0 = new FSMState("0", true, false);
        fsm.states.add(state0);
        FSMState stateEnd = new FSMState("end", false, true);
        fsm.states.add(stateEnd);

        for (Character punctuation : punctuations) {
            FSMTransition transition = new FSMTransition(state0, stateEnd, punctuation);
            fsm.transitions.add(transition);
        }

        return fsm;
    }

}
