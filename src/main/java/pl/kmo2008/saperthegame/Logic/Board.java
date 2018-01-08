package pl.kmo2008.saperthegame.Logic;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public final class Board {
    private final int EASY_HEIGHT=8;
    private final int EASY_WIDTH=8;
    private final int EASY_MINES=10;
    private final int NORMAL_HEIGHT=16;
    private final int NORMAL_WIDTH=16;
    private final int NORMAL_MINES=40;
    private final int HARD_HEIGHT=16;
    private final int HARD_WIDTH=31;
    private final int HARD_MINES=99;
    /**
     * Table of fields used to represent game board
     * First dimension is a height of board
     * Second dimension is a width of board
     */
    private Field [][] fields;
    /**
     * Height of game board
     */
    private int height;
    /**
     * Width of game board
     */
    private int width;
    /**
     * Number of unflagged mines
     */
    private int unflagedMines;
    /**
     * Number of mines on board
     */
    private int mines;
    /**
     * Number of revealed fields
     */
    private int fieldsRevealed;
    /**
     * Flags if it's first move
     */
    private boolean firstMove;
    /**
     * Flags if game is won
     */
    private boolean gameWon;
    /**
     * Flags if game is lost
     */
    private boolean gameLost;
    /**
     * Reveals field on given position, returns false if player reveals mine
     * @param x given horizontal position
     * @param y given vertical position
     */
    public void revealField(int x,int y)
    {
        if(firstMove)
        {
            //Reshuffles bord if mine revealed in first move
            firstMove=false;
            while(fields[y][x].getState()==State.MINE)
            {
                shuffle(fields);
            }
            calculateFieldStates();
            fieldsRevealed++;
        }
        if(fields[y][x].getVisibleState()==VisibleState.UNREVEALED)
        {
            fields[y][x].setVisibleState(VisibleState.REVEALED);
            //Checks if player revealed a mine, if yes returns false meaning the game is lost
            if(fields[y][x].getState()==State.MINE)
            {
                fields[y][x].setVisibleState(VisibleState.MINE_BLOWN);
                revealAllFields();
                gameLost=true;
                return;
            }
            //Checks if field is empty, if yes reveals neighbouring fields
            if (fields[y][x].getState() == State.EMPTY) {
                if (y > 0 && x > 0) {revealField(x - 1, y - 1);}
                if (y > 0) {revealField(x, y - 1);}
                if (y > 0 && x < width - 1) {revealField(x + 1, y - 1);}
                if (x > 0) {revealField(x - 1, y);}
                if (x < width - 1) {revealField(x + 1, y);}
                if (y < height - 1 && x > 0) {revealField(x - 1, y + 1);}
                if (y < height - 1) {revealField(x, y + 1);}
                if (y < height - 1 && x < width - 1) {revealField(x + 1, y + 1);}
            }
            fieldsRevealed++;
        }
        gameWon=checkIfGameWon();
    }

    /**
     * Reveals all fields on the board
     */
    private void revealAllFields()
    {
        for(int i = 0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                if(fields[i][j].getVisibleState()==VisibleState.FLAGGED&&fields[i][j].getState()!=State.MINE)
                {
                    fields[i][j].setVisibleState(VisibleState.WRONGLY_FLAGGED);
                }
                if(fields[i][j].getVisibleState()==VisibleState.UNREVEALED||
                        fields[i][j].getVisibleState()==VisibleState.QUESTION_MARK)
                {
                    fields[i][j].setVisibleState(VisibleState.REVEALED);
                }
            }
        }
    }
    /**
     * This method generates game board with given dimensions and with given number of mines
     * @param height Height of board
     * @param width Width of board
     * @param mines Number of mines
     */
    private void generate(int height,int width, int mines)
    {
        firstMove=true;
        gameLost = false;
        gameWon = false;
        fields = new Field[height][width];
        for(int i = 0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                if(mines>0) {
                    fields[i][j] = new Field(State.MINE);
                    mines--;
                }
                else {
                    fields[i][j] = new Field();
                }
            }
        }
        shuffle(fields);
    }

    /**
     * Shuffles given two dimensional array of objects using modified Fisher–Yates algorithm
     * @param array two dimensional array of objects to shuffle
     */
    private void shuffle(Object array [][]) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            for (int j = array[i].length - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                Object temp = array[i][j];
                array[i][j] = array[m][n];
                array[m][n] = temp;
            }
        }
    }

    /**
     * Returns number of adjacent mines to field on given position
     * @param x given horizontal position
     * @param y given vertical position
     * @return number of adjacent mines 0-8
     */
    private int numberOfAdjacentMines(int x,int y)
    {
        int adjacentMines = 0;
        if (y>0&&x>0&&fields[y - 1][x - 1].getState() == State.MINE) { adjacentMines++; }
        if (y>0&&fields[y - 1][x].getState() == State.MINE) { adjacentMines++; }
        if (y>0&&x<width-1&&fields[y - 1][x + 1].getState() == State.MINE) { adjacentMines++; }
        if (x>0&&fields[y][x - 1].getState() == State.MINE) { adjacentMines++; }
        if (x<width-1&&fields[y][x + 1].getState() == State.MINE) { adjacentMines++; }
        if (y<height-1&&x>0&&fields[y + 1][x - 1].getState() == State.MINE) { adjacentMines++; }
        if (y<height-1&&fields[y + 1][x].getState() == State.MINE) { adjacentMines++; }
        if (y<height-1&&x<width-1&&fields[y + 1][x + 1].getState() == State.MINE) { adjacentMines++; }
        return adjacentMines;
    }

    /**
     * Calculates values of fields
     */
    private void calculateFieldStates()
    {
        for(int i = 0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                if(!(fields[i][j].getState() ==State.MINE)) {
                    switch (numberOfAdjacentMines(j,i)) {
                        case 0:
                            fields[i][j].setState(State.EMPTY);
                            break;
                        case 1:
                            fields[i][j].setState(State.ONE);
                            break;
                        case 2:
                            fields[i][j].setState(State.TWO);
                            break;
                        case 3:
                            fields[i][j].setState(State.THREE);
                            break;
                        case 4:
                            fields[i][j].setState(State.FOUR);
                            break;
                        case 5:
                            fields[i][j].setState(State.FIVE);
                            break;
                        case 6:
                            fields[i][j].setState(State.SIX);
                            break;
                        case 7:
                            fields[i][j].setState(State.SEVEN);
                            break;
                        case 8:
                            fields[i][j].setState(State.EIGHT);
                            break;
                    }
                }
            }
        }
    }
    /**
     * Starts game on easy mode
     */
    public void easyMode()
    {
        height=EASY_HEIGHT;
        width=EASY_WIDTH;
        mines=EASY_MINES;
        unflagedMines=mines;
        generate(height,width,mines);
    }

    /**
     * Starts game on normal mode
     */
    public void normalMode()
    {
        height=NORMAL_HEIGHT;
        width=NORMAL_WIDTH;
        mines=NORMAL_MINES;
        unflagedMines=mines;
        generate(height,width,mines);
    }

    /**
     * Starts game on hard mode
     */
    public void hardMode()
    {
        height=HARD_HEIGHT;
        width=HARD_WIDTH;
        mines=HARD_MINES;
        unflagedMines=mines;
        generate(height,width,mines);
    }

    /**
     * Checks if game is won
     * @return if yes true, else false
     */
    private boolean checkIfGameWon()
    {
        if(width*height==mines+fieldsRevealed)
        {
            return true;
        }
        return false;
        /*for(int i = 0;i<height;i++) {
            for (int j = 0; j < width; j++)
            {
                if(!(fields[i][j].getState()==State.MINE||fields[i][j].getVisibleState()==VisibleState.REVEALED))
                {
                    return false;
                }
            }
        }
        return true;*/
    }

    /**
     * Returns height of the board
     * @return height of the board
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns width of the board
     * @return width of the board
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns number of unflagged mines
     * @return number of unflagged mines
     */
    public int getUnflagedMinesines() {
        return unflagedMines;
    }

    /**
     * Returns game winning status
     * @return game winning status
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Returns game loosing status
     * @return game loosing status
     */
    public boolean isGameLost() {
        return gameLost;
    }

    /**
     * Returns field on given position
     * @param x given horizontal position
     * @param y given vertical position
     * @return field on given position
     */
    public Field getFieldAt(int x,int y)
    {
        return fields[y][x];
    }


    //Following methods may be obsolete


    /**
     * Starts game with custom parameters
     * @param height height of the board
     * @param width width of the board
     * @param mines number of mines, must be less than height times width
     */
    public void customMode(int height,int width, int mines) throws TooManyMinesException
    {
        if(mines >= height*width)
        {
            throw new TooManyMinesException();
        }
        this.height=height;
        this.width=width;
        this.mines=mines;
        unflagedMines=mines;
        generate(height,width,mines);
    }

    /**
     * Flags field on given position
     * @param x given horizontal position
     * @param y given vertical position
     */
    public void flag(int x,int y)
    {
        try
        {
            if(fields[y][x].getVisibleState()!=VisibleState.REVEALED) {
                unflagedMines--;
                fields[y][x].setVisibleState(VisibleState.FLAGGED);
            }
        }
        catch (IndexOutOfBoundsException e)
        {

        }
    }

    /**
     * Unflags field on given position
     * @param x given horizontal position
     * @param y given vertical position
     */
    public void unflag(int x,int y)
    {
        try
        {
            if(fields[y][x].getVisibleState()==VisibleState.FLAGGED) {
                fields[y][x].setVisibleState(VisibleState.UNREVEALED);
                unflagedMines++;
            }
        }
        catch (IndexOutOfBoundsException e)
        {

        }
    }

    /**
     * Flags field on given position with question mark
     * @param x given horizontal position
     * @param y given vertical position
     */
    public void questionmark(int x,int y)
    {
        try
        {
            if(fields[y][x].getVisibleState()!=VisibleState.REVEALED) {
                fields[y][x].setVisibleState(VisibleState.QUESTION_MARK);
            }
        }
        catch (IndexOutOfBoundsException e)
        {

        }
    }
    /**
     * Unflags field on given position with question mark
     * @param x given horizontal position
     * @param y given vertical position
     */
    public void unquestionmark(int x,int y)
    {
        try
        {
            if(fields[y][x].getVisibleState()==VisibleState.QUESTION_MARK) {
                fields[y][x].setVisibleState(VisibleState.UNREVEALED);
            }
        }
        catch (IndexOutOfBoundsException e)
        {

        }
    }

    public Field[][] getFields() {
        return fields;
    }

    public void setFields(Field[][] fields) {
        this.fields = fields;
    }

    /**
     * Temporary class used for testing display in console
     * Delete in release version
     */
    public void testDisplay()
    {
        for(int i = 0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                switch (fields[i][j].getState()) {
                    case MINE:
                        System.out.print("*");
                        break;
                    case EMPTY:
                        System.out.print("#");
                        break;
                    case ONE:
                        System.out.print("1");
                        break;
                    case TWO:
                        System.out.print("2");
                        break;
                    case THREE:
                        System.out.print("3");
                        break;
                    case FOUR:
                        System.out.print("4");
                        break;
                    case FIVE:
                        System.out.print("5");
                        break;
                    case SIX:
                        System.out.print("6");
                        break;
                    case SEVEN:
                        System.out.print("7");
                        break;
                    case EIGHT:
                        System.out.print("8");
                        break;
                    default:
                        System.out.print("!");
                        break;
                }
            }
            System.out.print(' ');
            for (int j = 0; j < width; j++) {
                switch (fields[i][j].getVisibleState()) {
                    case UNREVEALED:
                        System.out.print("-");
                        break;
                    case REVEALED:
                        switch (fields[i][j].getState()) {
                            case MINE:
                                System.out.print("*");
                                break;
                            case EMPTY:
                                System.out.print("#");
                                break;
                            case ONE:
                                System.out.print("1");
                                break;
                            case TWO:
                                System.out.print("2");
                                break;
                            case THREE:
                                System.out.print("3");
                                break;
                            case FOUR:
                                System.out.print("4");
                                break;
                            case FIVE:
                                System.out.print("5");
                                break;
                            case SIX:
                                System.out.print("6");
                                break;
                            case SEVEN:
                                System.out.print("7");
                                break;
                            case EIGHT:
                                System.out.print("8");
                                break;
                            default:
                                System.out.print("!");
                                break;
                        }
                        break;
                    case FLAGGED:
                        System.out.print("P");
                        break;
                    case QUESTION_MARK:
                        System.out.print("?");
                        break;
                    default:
                        System.out.print("!");
                        break;
                }
            }
            System.out.println();
        }
    }
}
