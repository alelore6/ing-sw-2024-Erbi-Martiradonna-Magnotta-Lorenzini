package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * CardColor is an enumeration that lists all kind of color of the cards of Codex Naturalis, with the addition of white color used to indicate starting cards.
 * It implements Serializable  because through serialization is possible to transmit object between different machines in a network.
 */

public enum CardColor implements Serializable {
    RED,
    GREEN,
    PURPLE,
    BLUE,
    WHITE
}
