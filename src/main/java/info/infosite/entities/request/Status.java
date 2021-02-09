package info.infosite.entities.request;

import lombok.Getter;

@Getter
public enum Status {
    completed("Выполнена","Выполненные заявки"),
    inwork("В работе","Заявки в работе"),
    active("Активна", "Активные заявки");

    private final String displayValue;
    private final String reportValue;

    Status(String displayValue,String reportValue) {
        this.displayValue = displayValue;
        this.reportValue = reportValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
    public String getReportValue() {return reportValue;}


    public static Status fromString(String text) {
        for (Status status : Status.values()) {
            if (status.reportValue.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }
 }
