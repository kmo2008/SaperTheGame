package pl.kmo2008.saperthegame.Gui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kmo2008.saperthegame.Entities.Rank;
import pl.kmo2008.saperthegame.Logic.*;
import pl.kmo2008.saperthegame.Repos.Rankrepo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@Theme("mytheme")
@SpringUI
@Push
public class Home extends UI {
    @Autowired
    Board gameboard;

    @Autowired
    Rankrepo rankrepo;

    private int flagcount = 0;
    GridLayout game = new GridLayout();
    /**
     * Component declarations
     */
    Label gameTime = new Label("00:00");
    Image smiley = new Image();
    Label minesLeft = new Label("0");
    ThemeResource[][] resources;
    Button easyButton = new Button("Easy Mode");
    Button mediumButton = new Button("Medium Mode");
    Button hardButton = new Button("Hard Mode");
    TextField customWidth = new TextField("Szerokość:");
    TextField customHight = new TextField("Wysokość:");
    TextField customMines = new TextField("Ilosć min: ");
    Button startCustom = new Button("Start Custom Mode");
    Button customMode = new Button("Custom Mode");
    Window customGameWindow = new Window("Custom Game");
    Window saveRecordWindow = new Window("WYGRALES!!!");
    Label recordLabel = new Label("Wygrałeś i znalazłeś się w TOP 10 w tym trybie. Wypełnij, aby zapisać się w rankingu.");
    TextField nickname = new TextField("Twój nick:");
    Button saveRecord = new Button("Zapisz");
    Grid<Rank> gridRankEasy = new Grid<>(Rank.class);
    Grid<Rank> gridRankMedium = new Grid<>(Rank.class);
    Grid<Rank> gridRankHard = new Grid<>(Rank.class);
    Accordion ranks = new Accordion();

    /**
     * Resources
     */
    ThemeResource blank = new ThemeResource("img/blank.png");
    ThemeResource blankfield = new ThemeResource("img/clear-12.png");
    ThemeResource minenot = new ThemeResource("img/mine2-12.png");
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
    ThemeResource wroglyFlaged = new ThemeResource("img/wrongly_flagged-12.png");
    ThemeResource mineBlown = new ThemeResource("img/mine_blown.png");
    ThemeResource smiley1 = new ThemeResource("img/smiley1.png");
    ThemeResource smiley2 = new ThemeResource("img/smiley2.png");
    ThemeResource smiley4 = new ThemeResource("img/smiley4.png");

    long secondsx = 0;
    boolean started = false;
    String timeRecord = null;
    int type = 1;

    public long getSecondsx() {
        return secondsx;
    }

    public void addSecondsx(long secondsx) {
        this.secondsx++;
    }

    UI ui = getUI();
    Timer t = new Timer();
    String gameHeadWidth;

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
        VerticalLayout gameBox = new VerticalLayout();
        VerticalLayout windowLayout = new VerticalLayout();
        VerticalLayout recordLayout = new VerticalLayout();
        VerticalLayout menu = new VerticalLayout();

        /**
         * Horizontal layouts
         */
        HorizontalLayout gameHead = new HorizontalLayout();
        HorizontalLayout gameAndRanks = new HorizontalLayout();

        /**
         * Layouts merge
         *
         */
        recordLayout.addComponents(recordLabel,nickname,saveRecord);
        windowLayout.addComponents(customWidth, customHight, customMines, startCustom);
        customGameWindow.setContent(windowLayout);
        saveRecordWindow.setContent(recordLayout);
        menu.addComponents(easyButton, mediumButton, hardButton, customMode, ranks);
        gameHead.addComponents(gameTime, smiley, minesLeft);
        gameBox.addComponents(gameHead, game);
        gameAndRanks.addComponents(menu, gameBox);
        head.addComponents(gameAndRanks);
        /**
         * Content settings
         */
        setContent(head);

        /**
         * Component settings
         */
        gameTime.setStyleName("labelfont");
        minesLeft.setStyleName("labelfont");
        smiley.setSource(smiley1);

        /**
         * Layout settings and listenners
         *
         */
        gridRankEasy.setColumnOrder("id","nickname","time","type");
        gridRankEasy.removeColumn("id");
        gridRankEasy.removeColumn("type");
        gridRankMedium.setColumnOrder("id","nickname","time","type");
        gridRankMedium.removeColumn("id");
        gridRankMedium.removeColumn("type");
        gridRankHard.setColumnOrder("id","nickname","time","type");
        gridRankHard.removeColumn("id");
        gridRankHard.removeColumn("type");
        gridRankEasy.setItems(rankrepo.getTopOfEasy(9999999L));
        gridRankMedium.setItems(rankrepo.getTopOfMedium(9999999L));
        gridRankHard.setItems(rankrepo.getTopOfHard(9999999L));
        ranks.addTab(gridRankEasy, "Easy Mode");
        ranks.addTab(gridRankMedium, "Medium Mode");
        ranks.addTab(gridRankHard, "Hard Mode");
        gameBox.setComponentAlignment(gameHead, Alignment.TOP_CENTER);
        gameHead.setComponentAlignment(gameTime, Alignment.BOTTOM_LEFT);
        gameHead.setComponentAlignment(smiley, Alignment.BOTTOM_CENTER);
        gameHead.setComponentAlignment(minesLeft, Alignment.BOTTOM_RIGHT);
        game.setSizeUndefined();
        gameHead.setStyleName("gameHead");
        gameHead.setMargin(true);
        gameHeadWidth = (gameboard.getWidth() + 1) * 32 + "px";
        gameHead.setWidth(gameHeadWidth);
        head.setSizeFull();
        game.setMargin(true);
        game.setHeightUndefined();
        game.setRows(gameboard.getHeight());
        game.setColumns(gameboard.getWidth());
        game.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        generateMatrixGrid();


        easyButton.addClickListener(clickEvent -> {
            gameboard.easyMode();
            type = 0;
            generateMatrixGrid();
            reloadGame();
            t.cancel();
            started = false;
            gameHeadWidth = (gameboard.getWidth() + 1) * 32 + "px";
            gameHead.setWidth(gameHeadWidth);
        });

        mediumButton.addClickListener(clickEvent -> {
            gameboard.normalMode();
            type = 1;
            generateMatrixGrid();
            reloadGame();
            t.cancel();
            started = false;
            gameHeadWidth = (gameboard.getWidth() + 1) * 32 + "px";
            gameHead.setWidth(gameHeadWidth);
        });
        hardButton.addClickListener(clickEvent -> {
            gameboard.hardMode();
            type = 3;
            generateMatrixGrid();
            reloadGame();
            t.cancel();
            started = false;
            gameHeadWidth = (gameboard.getWidth() + 1) * 32 + "px";
            gameHead.setWidth(gameHeadWidth);
        });

        customMode.addClickListener(clickEvent -> {
            customGameWindow.setWidth(300.0f, Unit.PIXELS);
            customGameWindow.setResizable(false);
            customGameWindow.center();
            addWindow(customGameWindow);
        });

        saveRecord.addClickListener(clickEvent -> {
            Long seconds = getSecondsx();
            Rank rank = new Rank();
            rank.setNickname(nickname.getValue());
            rank.setTime(seconds);
            rank.setType(type);
            rankrepo.save(rank);
            saveRecordWindow.close();
            gridRankEasy.setItems(rankrepo.getTopOfEasy(9999999L));
            gridRankMedium.setItems(rankrepo.getTopOfMedium(9999999L));
            gridRankHard.setItems(rankrepo.getTopOfHard(9999999L));
            ranks.addTab(gridRankEasy, "Easy Mode");
            ranks.addTab(gridRankMedium, "Medium Mode");
            ranks.addTab(gridRankHard, "Hard Mode");
        });
        startCustom.addClickListener(clickEvent -> {
            Notification error = new Notification("Zbyt dużo min.");
            final int rows = Integer.parseInt(customHight.getValue());
            final int columns = Integer.parseInt(customWidth.getValue());
            final int mines = Integer.parseInt(customMines.getValue());
            try {
                gameboard.customMode(rows, columns, mines);
                type = -1;
                generateMatrixGrid();
                reloadGame();
                t.cancel();
                started = false;
                gameHeadWidth = (gameboard.getWidth() + 1) * 32 + "px";
                gameHead.setWidth(gameHeadWidth);
                customGameWindow.close();
            } catch (TooManyMinesException e) {
                error.show(Page.getCurrent());
            }
        });

        game.addLayoutClickListener(clickEvent -> {
            if (!started) {
                startTimer();
            }
            Component child = clickEvent.getChildComponent();
            if (child != null) {
                String[] tokens = child.getId().split("x");
                int row = Integer.parseInt(tokens[0]);
                int col = Integer.parseInt(tokens[1]);
                if (gameboard.getFieldAt(row, col).getVisibleState() != VisibleState.REVEALED) {
                    if (clickEvent.getButton() == MouseEventDetails.MouseButton.RIGHT) {
                        if (gameboard.getFieldAt(row, col).getVisibleState() != VisibleState.FLAGGED
                                && gameboard.getFieldAt(row, col).getVisibleState() != VisibleState.QUESTION_MARK) {
                            gameboard.flag(row, col);
                            flagcount++;
                        } else if (gameboard.getFieldAt(row, col).getVisibleState() == VisibleState.FLAGGED) {
                            gameboard.unflag(row, col);
                            flagcount--;
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
            }
            if (gameboard.isGameWon()) {
                smiley.setSource(smiley4);
                minesLeft.setValue("0");
                gameboard.revealAllFields();
                refreshGrid();
                timeRecord = getTimeString(getSecondsx());
                if (checkRecords(getSecondsx())) {
                    addWindow(saveRecordWindow);
                }
                t.cancel();


            }
            if (gameboard.isGameLost()) {
                smiley.setSource(smiley2);
                t.cancel();
            }
        });

    }

    public boolean checkRecords(Long seconds) {
        List<Rank> rank = null;
        if (type == 0) {
            rank = rankrepo.getTopOfEasy(seconds);
        } else if (type == 1) {
            rank = rankrepo.getTopOfMedium(seconds);
        } else if (type == 2) {
            rank = rankrepo.getTopOfHard(seconds);
        } else return false;

        if(rank.size() >= 10) return false;
        else return true;
    }

    public void startTimer() {
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                ui.access(new Runnable() {
                    @Override
                    public void run() {
                        addSecondsx(1);
                        String timeString;
                        long seconds = getSecondsx();
                        timeString = getTimeString(seconds);
                        gameTime.setValue(timeString);
                        ui.push();
                    }
                });
            }
        }, 0, 1000);
        started = true;
    }

    public void reloadGame() {
        secondsx = 0;
        flagcount = 0;
        smiley.setSource(smiley1);
        gameTime.setValue("00:00");
    }

    public String getTimeString(Long seconds) {
        String timeString = null;
        long minutes = 0;
        long hours = 0;
        if (seconds < 10) timeString = "00:0" + Long.toString(seconds);
        else if (seconds < 60) timeString = "00:" + Long.toString(seconds);
        else if (seconds < 660) {
            while (seconds > 60) {
                seconds -= 60;
                minutes++;
            }
            timeString = "0" + Long.toString(minutes) + ":" + Long.toString(seconds);
        } else if (seconds < 3600) {
            while (seconds > 60) {
                seconds -= 60;
                minutes++;
            }
            timeString = Long.toString(minutes) + ":" + Long.toString(seconds);
        } else {
            while (seconds > 3600) {
                seconds -= 3600;
                hours++;
            }
            while (seconds > 60) {
                seconds -= 60;
                minutes++;
            }
            timeString = Long.toString(hours) + ":" + Long.toString(minutes) + ":" + Long.toString(seconds);
        }
        return timeString;
    }

    private void refreshGrid() {
        Field fieldx = null;
        int mines = gameboard.getMines() - flagcount;
        minesLeft.setValue(Integer.toString(mines));
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                Image field = new Image();
                ThemeResource resource = null;
                fieldx = gameboard.getFieldAt(col, row);
                resource = getFieldResurce(fieldx);
                if (gameboard.isGameWon() && resource == mineBlown) resource = flagfield;
                ThemeResource comp = resources[col][row];
                if (comp != resource) {
                    field.setSource(resource);
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
            if (fieldx.getState() == State.EMPTY) resource = blank;
            else if (fieldx.getState() == State.MINE) resource = minenot;
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
        else if (fieldx.getVisibleState() == VisibleState.WRONGLY_FLAGGED)
            resource = wroglyFlaged;
        else if (fieldx.getVisibleState() == VisibleState.MINE_BLOWN)
            resource = mineBlown;
        return resource;
    }


    private void generateMatrixGrid() {
        game.removeAllComponents();
        final int rows = gameboard.getHeight();
        final int columns = gameboard.getWidth();
        game.setColumns(columns);
        game.setRows(rows);

        game.setHeight("100%");
        Field fieldx = null;
        minesLeft.setValue(Integer.toString(gameboard.getMines()));
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                Image field = new Image();
                resources = new ThemeResource[columns][rows];
                ThemeResource resource = null;
                fieldx = gameboard.getFieldAt(col, row);
                resource = getFieldResurce(fieldx);
                resources[col][row] = resource;
                field.setSource(resource);
                field.setId(col + "x" + row);
                game.addComponent(field);
            }
        }
    }
}
