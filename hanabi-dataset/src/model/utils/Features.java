package model.utils;

/**
 * Elenco di tutto ciò che sarà nell'array del FinalState chiamato "state"
 */
public enum Features {
    fuse, //ok
    hints, //ok
    //finalround,
    deck, //ok
    handentropy_current, //ok
    handentropy_other, //ok

    firework_color1, //ok
    firework_color2,
    firework_color3,
    firework_color4,
    firework_color5,

    playability_card1_other, //ok
    playability_card2_other,
    playability_card3_other,
    playability_card4_other,
    playability_card5_other,

    cardentropy_card1_other, //ok
    cardentropy_card2_other,
    cardentropy_card3_other,
    cardentropy_card4_other,
    cardentropy_card5_other,

    uselessness_card1_other, //ok
    uselessness_card2_other,
    uselessness_card3_other,
    uselessness_card4_other,
    uselessness_card5_other,

    color_other_card1_col1, //ok
    color_other_card1_col2,
    color_other_card1_col3,
    color_other_card1_col4,
    color_other_card1_col5,

    color_other_card2_col1, //ok
    color_other_card2_col2,
    color_other_card2_col3,
    color_other_card2_col4,
    color_other_card2_col5,

    color_other_card3_col1, //ok
    color_other_card3_col2,
    color_other_card3_col3,
    color_other_card3_col4,
    color_other_card3_col5,

    color_other_card4_col1, //ok
    color_other_card4_col2,
    color_other_card4_col3,
    color_other_card4_col4,
    color_other_card4_col5,

    color_other_card5_col1, //ok
    color_other_card5_col2,
    color_other_card5_col3,
    color_other_card5_col4,
    color_other_card5_col5,

    value_other_card1, //ok
    value_other_card2,
    value_other_card3,
    value_other_card4,
    value_other_card5,

    playability_card1_current, //ok
    playability_card2_current,
    playability_card3_current,
    playability_card4_current,
    playability_card5_current,

    cardentropy_card1_current, //ok
    cardentropy_card2_current,
    cardentropy_card3_current,
    cardentropy_card4_current,
    cardentropy_card5_current,

    uselessness_card1_current, //ok
    uselessness_card2_current,
    uselessness_card3_current,
    uselessness_card4_current,
    uselessness_card5_current,

    poss_card1_oth_color1, //ok
    poss_card1_oth_color2,
    poss_card1_oth_color3,
    poss_card1_oth_color4,
    poss_card1_oth_color5,

    poss_card2_oth_color1, //ok
    poss_card2_oth_color2,
    poss_card2_oth_color3,
    poss_card2_oth_color4,
    poss_card2_oth_color5,

    poss_card3_oth_color1, //ok
    poss_card3_oth_color2,
    poss_card3_oth_color3,
    poss_card3_oth_color4,
    poss_card3_oth_color5,

    poss_card4_oth_color1, //ok
    poss_card4_oth_color2,
    poss_card4_oth_color3,
    poss_card4_oth_color4,
    poss_card4_oth_color5,

    poss_card5_oth_color1, //ok
    poss_card5_oth_color2,
    poss_card5_oth_color3,
    poss_card5_oth_color4,
    poss_card5_oth_color5,

    poss_card1_curr_color1, //ok
    poss_card1_curr_color2,
    poss_card1_curr_color3,
    poss_card1_curr_color4,
    poss_card1_curr_color5,

    poss_card2_curr_color1, //ok
    poss_card2_curr_color2,
    poss_card2_curr_color3,
    poss_card2_curr_color4,
    poss_card2_curr_color5,

    poss_card3_curr_color1, //ok
    poss_card3_curr_color2,
    poss_card3_curr_color3,
    poss_card3_curr_color4,
    poss_card3_curr_color5,

    poss_card4_curr_color1, //ok
    poss_card4_curr_color2,
    poss_card4_curr_color3,
    poss_card4_curr_color4,
    poss_card4_curr_color5,

    poss_card5_curr_color1, //ok
    poss_card5_curr_color2,
    poss_card5_curr_color3,
    poss_card5_curr_color4,
    poss_card5_curr_color5,

    poss_card1_curr_value1, //ok
    poss_card1_curr_value2,
    poss_card1_curr_value3,
    poss_card1_curr_value4,
    poss_card1_curr_value5,

    poss_card2_curr_value1, //ok
    poss_card2_curr_value2,
    poss_card2_curr_value3,
    poss_card2_curr_value4,
    poss_card2_curr_value5,

    poss_card3_curr_value1, //ok
    poss_card3_curr_value2,
    poss_card3_curr_value3,
    poss_card3_curr_value4,
    poss_card3_curr_value5,

    poss_card4_curr_value1, //ok
    poss_card4_curr_value2,
    poss_card4_curr_value3,
    poss_card4_curr_value4,
    poss_card4_curr_value5,

    poss_card5_curr_value1, //ok
    poss_card5_curr_value2,
    poss_card5_curr_value3,
    poss_card5_curr_value4,
    poss_card5_curr_value5,

    poss_card1_oth_value1, //ok
    poss_card1_oth_value2,
    poss_card1_oth_value3,
    poss_card1_oth_value4,
    poss_card1_oth_value5,

    poss_card2_oth_value1, //ok
    poss_card2_oth_value2,
    poss_card2_oth_value3,
    poss_card2_oth_value4,
    poss_card2_oth_value5,

    poss_card3_oth_value1, //ok
    poss_card3_oth_value2,
    poss_card3_oth_value3,
    poss_card3_oth_value4,
    poss_card3_oth_value5,

    poss_card4_oth_value1, //ok
    poss_card4_oth_value2,
    poss_card4_oth_value3,
    poss_card4_oth_value4,
    poss_card4_oth_value5,

    poss_card5_oth_value1, //ok
    poss_card5_oth_value2,
    poss_card5_oth_value3,
    poss_card5_oth_value4,
    poss_card5_oth_value5,

    discarded_1_color1, //ok
    discarded_2_color1,
    discarded_3_color1,
    discarded_4_color1,
    discarded_5_color1,

    discarded_1_color2, //ok
    discarded_2_color2,
    discarded_3_color2,
    discarded_4_color2,
    discarded_5_color2,

    discarded_1_color3, //ok
    discarded_2_color3,
    discarded_3_color3,
    discarded_4_color3,
    discarded_5_color3,

    discarded_1_color4, //ok
    discarded_2_color4,
    discarded_3_color4,
    discarded_4_color4,
    discarded_5_color4,

    discarded_1_color5, //ok
    discarded_2_color5,
    discarded_3_color5,
    discarded_4_color5,
    discarded_5_color5,

}
