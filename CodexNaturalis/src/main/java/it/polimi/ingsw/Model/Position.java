package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * Position represents position of a corner in a generic card.
 * Implements serializable  because through serialization is possible to transmit object between different machines in a network.
 */
public enum Position implements Serializable {
    UP_SX,
    UP_DX,
    DOWN_SX,
    DOWN_DX
}