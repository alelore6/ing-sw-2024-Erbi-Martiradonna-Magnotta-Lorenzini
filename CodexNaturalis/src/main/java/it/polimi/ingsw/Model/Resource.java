package it.polimi.ingsw.Model;

import it.polimi.ingsw.View.TUI;

import java.io.Serializable;

public enum Resource implements Serializable {
    FUNGI,
    PLANT,
    INSECT,
    ANIMAL,
    QUILL,
    INKWELL,
    MANUSCRIPT;


    @Override
    public String toString(){
        switch(this){
            case FUNGI -> {return TUI.setColorForString("RED", "FUNGI", false);}
            case PLANT -> {return TUI.setColorForString("GREEN", "PLANT", false);}
            case INSECT -> {return TUI.setColorForString("PURPLE", "INSECT", false);}
            case ANIMAL -> {return TUI.setColorForString("BLUE", "ANIMAL", false);}
            case QUILL, INKWELL, MANUSCRIPT -> {return TUI.setColorForString("YELLOW", super.toString(), false);}
        }

        return null;
    }
}
