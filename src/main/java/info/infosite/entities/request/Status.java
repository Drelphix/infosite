package info.infosite.entities.request;

import lombok.Getter;

@Getter
public enum Status {
    completed("Выполнена"),
    inwork("В работе"),
    active("Активна");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
