package ui;

public interface Client {
    public String eval(String line);
    public void updateState(State state);
    public State getState();
}
