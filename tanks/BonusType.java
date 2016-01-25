package tanks;

public enum BonusType {
    HEALTH,
    ARMOR,
    SPEED,
    VISION;

    public static BonusType getRandomBonus(int numberOfBonuses) {
        return values()[(int) (Math.random() * (numberOfBonuses))];
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
            case VISION:
                typeString = "vision";
                break;
            default:
                typeString = "health";
                break;
        }
        return typeString;
    }
}
