package info.infosite.entities.request;

import lombok.Getter;

@Getter
public enum Status {
    Completed("Выполнена"),
    InWork("В работе"),
    Active("Активна");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
