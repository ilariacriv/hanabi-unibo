package model.utils;

/**
 * Tutte le possibili azioni; sarà a 1 l'azione corrispondente, a 0 le altre
 */
public enum ActionCode {
    PLAY_1st,
    PLAY_2nd,
    PLAY_3rd,
    PLAY_4th,
    PLAY_5th,

    DISCARD_1st,
    DISCARD_2nd,
    DISCARD_3rd,
    DISCARD_4th,
    DISCARD_5th,

    HINT_VALUE_1,
    HINT_VALUE_2,
    HINT_VALUE_3,
    HINT_VALUE_4,
    HINT_VALUE_5,

    HINT_COLOR_1,
    HINT_COLOR_2,
    HINT_COLOR_3,
    HINT_COLOR_4,
    HINT_COLOR_5;
}
