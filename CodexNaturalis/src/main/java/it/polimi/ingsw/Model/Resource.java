package it.polimi.ingsw.Model;

import it.polimi.ingsw.View.TUI;

import java.io.Serializable;

/**
 * Resource is an enumeration that represents all kind of resources tha can be used in the game. Each value of the enum represents a specific resource.
 * It implements Serializable to allow the transmission through a network.
 * When toString method is called on a Resource object, it returns the resource name colored based on the resource type.
 */
public enum Resource implements Serializable {
    FUNGI,
    PLANT,
    INSECT,
    ANIMAL,
    QUILL,
    INKWELL,
    MANUSCRIPT;

    /**
     *
     * @return
     */

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
