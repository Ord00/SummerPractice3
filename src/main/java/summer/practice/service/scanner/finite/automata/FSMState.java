package summer.practice.service.scanner.finite.automata;

public class FSMState {

    public String id;

    public Boolean isStart;

    public Boolean isEnd;

    public FSMState(String id, Boolean isStart, Boolean isEnd) {
        this.id = id;
        this.isStart = isStart;
        this.isEnd = isEnd;
    }
}
