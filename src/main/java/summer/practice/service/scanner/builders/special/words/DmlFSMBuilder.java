package summer.practice.service.scanner.builders.special.words;

import summer.practice.service.scanner.finite.automata.FSM;

import java.util.ArrayList;
import java.util.List;

public class DmlFSMBuilder extends SpecialWordsFSMBuilder {

    @Override
    public FSM build() {

        words = new ArrayList<>(List.of(
                "INSERT",
                "DELETE",
                "UPDATE",
                "SELECT"
        ));
        return super.build();
    }
}
