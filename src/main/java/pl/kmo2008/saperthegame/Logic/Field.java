package pl.kmo2008.saperthegame.Logic;

public class Field {
    /**
     * State of the field
     * Possible states:
     * MINE - The field contains a mine
     * EMPTY - The field isn't adjacent to any mine
     * ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE - The field is adjacent to x number of mines
     */
    private State state;
    /**
     * State of the field visible to player
     * Possible states:
     * REVEALED - The field has been revealed, ether by clicking on it or clicking on neighbouring empty field
     * UNREVEALED - The field hasn't beed reveled, it's default state
     * FLAGED - The field has been marked by a player as potential mine position
     * QUESTION_MARK - The field has been marked by a player as uncertain
     */
    private VisibleState visibleState;

    /**
     * Default constructor, creates empty unrevealed field
     */
    public Field() {
        state = State.EMPTY;
        visibleState = VisibleState.UNREVEALED;
    }

    /**
     * Creates unrevealed field with given state
     * @param state Given state
     *              Possible states:
     *              MINE - The field contains a mine
     *              EMPTY - The field isn't adjacent to any mine
     *              ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE - The field is adjacent to x number of mines
     */
    public Field(State state) {
        this.state = state;
        this.visibleState = VisibleState.UNREVEALED;
    }

    /**
     * Returns state of the field
     * @return state of the field
     */
    public State getState() {
        return state;
    }

    /**
     * Sets state of the field to given state
     * @param state given state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Returns state of the field visible to player
     * @return state of the field visible to player
     */
    public VisibleState getVisibleState() {
        return visibleState;
    }

    /**
     * Sets state of the field visible to player to given state
     * @param visibleState given state
     */
    public void setVisibleState(VisibleState visibleState) {
        this.visibleState = visibleState;
    }
}
