package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * Token Color is the enumeration that represents all color of tokens.
 * It implements Serializable because through serialization is possible to transmit object between different machines in a network.
 */
public enum TokenColor implements Serializable {

    RED,

    GREEN,

    BLUE,

    YELLOW
}
