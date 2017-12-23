package pl.kmo2008.saperthegame.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kmo2008.saperthegame.Logic.Board;
import pl.kmo2008.saperthegame.Logic.Field;
import pl.kmo2008.saperthegame.Logic.State;
import pl.kmo2008.saperthegame.Logic.VisibleState;

@Theme("mytheme")
@SpringUI
public class home extends UI {
    @Autowired
    Board gameboard;

    GridLayout game = new GridLayout();

    int[][] ids;

    @Override
    protected void init(VaadinRequest request) {
        /**
         * Game default settings
         */
        gameboard.normalMode();
        /**
         * Vertical layouts
         */
        VerticalLayout head = new VerticalLayout();
        VerticalLayout ranks = new VerticalLayout();

        /**
         * Horizontal layouts
         */
        HorizontalLayout gameAndRanks = new HorizontalLayout();

        /**
         * Layouts merge
         */
        gameAndRanks.addComponents(game, ranks);
        head.addComponents(gameAndRanks);
        /**
         * Content settings
         */
        setContent(head);
        /**
         * Grid settings and listenners
         *
         */
        //game.setStyleName("gridgame");
        game.setSizeUndefined();
        generateMatrixGrid(gameboard.getHeight(), gameboard.getWidth());
        game.addLayoutClickListener(clickEvent -> {
            Component child = clickEvent.getChildComponent();
            String[] tokens = child.getId().split("x");
            int row = Integer.parseInt(tokens[0]);
            int col = Integer.parseInt(tokens[1]);
            if (gameboard.getFieldAt(row, col).getVisibleState() != VisibleState.REVEALED) {
                if (clickEvent.getButton() == MouseEventDetails.MouseButton.RIGHT) {
                    if (gameboard.getFieldAt(row, col).getVisibleState() != VisibleState.FLAGGED
                            && gameboard.getFieldAt(row, col).getVisibleState() != VisibleState.QUESTION_MARK)
                        gameboard.flag(row, col);
                    else if (gameboard.getFieldAt(row, col).getVisibleState() == VisibleState.FLAGGED) {
                        gameboard.unflag(row, col);
                        gameboard.questionmark(row, col);
                    } else {
                        gameboard.unquestionmark(row, col);
                    }
                }
                if (clickEvent.getButton() == MouseEventDetails.MouseButton.LEFT) {
                    gameboard.revealField(row, col);
                }
                generateMatrixGrid(gameboard.getHeight(), gameboard.getWidth());
            }

        });


    }

    private void generateMatrixGrid(final int rows, final int columns) {
        game.removeAllComponents();
        game.setRows(rows);
        game.setColumns(columns);
        Field fieldx = null;
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                final Component field = new Image();
                ThemeResource resource = null;
                fieldx = gameboard.getFieldAt(row, col);
                if (fieldx.getVisibleState() == VisibleState.UNREVEALED)
                    resource = new ThemeResource("img/clear-12.png");
                else if (fieldx.getVisibleState() == VisibleState.REVEALED) {
                    if (fieldx.getState() == State.EMPTY) resource = null;
                    else if (fieldx.getState() == State.MINE) resource = new ThemeResource("img/minenotdetected-12.png");
                    else if (fieldx.getState() == State.ONE) resource = new ThemeResource("img/1.png");
                    else if (fieldx.getState() == State.TWO) resource = new ThemeResource("img/2.png");
                    else if (fieldx.getState() == State.THREE) resource = new ThemeResource("img/3.png");
                    else if (fieldx.getState() == State.FOUR) resource = new ThemeResource("img/4.png");
                    else if (fieldx.getState() == State.FIVE) resource = new ThemeResource("img/5.png");
                    else if (fieldx.getState() == State.SIX) resource = new ThemeResource("img/6.png");
                    else if (fieldx.getState() == State.SEVEN) resource = new ThemeResource("img/7.png");
                    else if (fieldx.getState() == State.EIGHT) resource = new ThemeResource("img/8.png");
                } else if (fieldx.getVisibleState() == VisibleState.FLAGGED)
                    resource = new ThemeResource("img/flagfield-12.png");
                else if (fieldx.getVisibleState() == VisibleState.QUESTION_MARK)
                    resource = new ThemeResource("img/qmark-12.png");

                field.setIcon(resource);
                field.setId(row + "x" + col);
                game.addComponent(field);
            }
        }
    }
}
