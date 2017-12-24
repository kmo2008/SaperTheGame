package pl.kmo2008.saperthegame.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.AlignmentInfo;
import com.vaadin.shared.ui.MarginInfo;
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
        gameAndRanks.addComponents(ranks, game);
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
        head.setSizeFull();
        game.setHeightUndefined();
        game.setRows(gameboard.getHeight());
        game.setColumns(gameboard.getWidth());
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
                refreshGrid();
            }

        });


    }

    ThemeResource blankfield = new ThemeResource("img/clear-12.png");
    ThemeResource minenot = new ThemeResource("img/minenotdetected-12.png");
    ThemeResource one = new ThemeResource("img/1.png");
    ThemeResource two = new ThemeResource("img/2.png");
    ThemeResource three = new ThemeResource("img/3.png");
    ThemeResource four = new ThemeResource("img/4.png");
    ThemeResource five = new ThemeResource("img/5.png");
    ThemeResource six = new ThemeResource("img/6.png");
    ThemeResource seven = new ThemeResource("img/7.png");
    ThemeResource eigth = new ThemeResource("img/8.png");
    ThemeResource flagfield = new ThemeResource("img/flagfield-12.png");
    ThemeResource markfield = new ThemeResource("img/qmark-12.png");

    private void refreshGrid() {
        Field fieldx = null;
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                final Component field = new Image();
                ThemeResource resource = null;
                fieldx = gameboard.getFieldAt(col, row);
                resource = getFieldResurce(fieldx);
                Component comp = game.getComponent(col, row);
                if (comp.getIcon() != resource) {
                    field.setIcon(resource);
                    field.setId(col + "x" + row);
                    game.removeComponent(col, row);
                    game.addComponent(field, col, row);
                }
            }
        }
    }

    private ThemeResource getFieldResurce(Field fieldx) {
        ThemeResource resource = null;
        if (fieldx.getVisibleState() == VisibleState.UNREVEALED)
            resource = blankfield;
        else if (fieldx.getVisibleState() == VisibleState.REVEALED) {
            if (fieldx.getState() == State.EMPTY) resource = null;
            else if (fieldx.getState() == State.MINE)
                resource = minenot;
            else if (fieldx.getState() == State.ONE) resource = one;
            else if (fieldx.getState() == State.TWO) resource = two;
            else if (fieldx.getState() == State.THREE) resource = three;
            else if (fieldx.getState() == State.FOUR) resource = four;
            else if (fieldx.getState() == State.FIVE) resource = five;
            else if (fieldx.getState() == State.SIX) resource = six;
            else if (fieldx.getState() == State.SEVEN) resource = seven;
            else if (fieldx.getState() == State.EIGHT) resource = eigth;
        } else if (fieldx.getVisibleState() == VisibleState.FLAGGED)
            resource = flagfield;
        else if (fieldx.getVisibleState() == VisibleState.QUESTION_MARK)
            resource = markfield;
        return resource;
    }


    private void generateMatrixGrid(final int rows, final int columns) {
        game.removeAllComponents();
        //game.setRows(rows);
        //game.setColumns(columns);
        game.setHeight("100%");
        Field fieldx = null;
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                final Component field = new Image();
                ThemeResource resource = null;
                fieldx = gameboard.getFieldAt(col, row);
                resource = getFieldResurce(fieldx);
                field.setIcon(resource);
                field.setId(col + "x" + row);
                game.addComponent(field);
            }
        }
    }
}
