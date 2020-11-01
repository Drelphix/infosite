package info.infosite.entities.request;

import lombok.Getter;

@Getter
public enum Status {
    COMPLETED("Выполнена"),
    IN_WORK("В работе"),
    ACTIVE("Активна");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
