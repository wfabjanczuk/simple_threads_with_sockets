package zad1.dict.application;

import zad1.dict.application.gui.Gui;

public class Application extends Thread {
    private final Gui gui;

    public Application(Gui gui) {
        this.gui = gui;
    }

    public void run() {
        gui.initialize();
    }
}
