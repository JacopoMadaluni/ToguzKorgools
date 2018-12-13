package com.jacana.toguzkorgool;

/**
 * This class defines useful constants for the rest of the project.
 */
public class Constants {
    // Core rules
    public final static int CONSTRAINT_PLAYER_COUNT = 2;
    public final static int CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE = 9;
    public final static int CONSTRAINT_HOLES_PER_PLAYER = 9;
    // Board constraints
    public final static int CONSTRAINT_TOTAL_KORGOOLS = CONSTRAINT_PLAYER_COUNT * CONSTRAINT_HOLES_PER_PLAYER * CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE;
    public final static int CONSTRAINT_MAX_KORGOOLS_PER_HOLES = CONSTRAINT_TOTAL_KORGOOLS - (CONSTRAINT_PLAYER_COUNT - 1);
    public final static int CONSTRAINT_MIN_KORGOOLS_PER_HOLES = 1;

    // Error messages
    private final static String LINE_END = "!";
    public final static String ERROR_CUSTOM_GUI_CONSTRAINT_TOTAL_KORGOOLS_VIOLATION = "The number of Korgools on the board must be " + CONSTRAINT_TOTAL_KORGOOLS + LINE_END;
    public final static String ERROR_CUSTOM_GUI_CONSTRAINT_MAX_TOTAL_KORGOOLS_PER_HOLES_VIOLATION = "The number of Korgools per player cannot be greater than " + CONSTRAINT_MAX_KORGOOLS_PER_HOLES + LINE_END;
    public final static String ERROR_CUSTOM_GUI_CONSTRAINT_MIN_TOTAL_KORGOOLS_PER_HOLES_VIOLATION = "The number of Korgools per player cannot be less than " + CONSTRAINT_MIN_KORGOOLS_PER_HOLES + LINE_END;
    public final static String ERROR_CUSTOM_GUI_CONSTRAINT_TUZ_IDENTITY_VIOLATION = "Player's cannot have Tuz in holes of equal number" + LINE_END;

}
