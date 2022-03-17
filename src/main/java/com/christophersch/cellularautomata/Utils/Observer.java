package com.christophersch.cellularautomata.Utils;

import com.christophersch.cellularautomata.AutomataApplication;

import java.util.ArrayList;

public abstract class Observer {
    AutomataApplication automata_application;

    public Observer(AutomataApplication application) {
        this.automata_application = application;
        automata_application.attach(this);
    }
    public abstract void update();


    public void end() {
        automata_application.detach(this);
    }
}
