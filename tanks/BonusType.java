package tanks;

public enum BonusType {
    HEALTH,
    ARMOR,
    SPEED,
    VISION;

    public static BonusType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

    public static String getString(BonusType type) {
        String typeString;
        switch (type) {
            case HEALTH:
                typeString = "health";
                break;
            case ARMOR:
                typeString = "armor";
                break;
            case SPEED:
                typeString = "speed";
                break;
            default:
                typeString = "health";
                break;
        }
        return typeString;
    }
}