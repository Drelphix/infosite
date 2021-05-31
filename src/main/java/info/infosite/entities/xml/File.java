package info.infosite.entities.xml;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
@Setter
public class File {
    private String name;
    private String lastDate;
    private boolean expired;
    private String size;

    public void setLastDate(String lastDate) {
        String temp = lastDate;
        try {
            ifExpired(temp);
        } catch (DateTimeParseException exception) {
            String[] date = temp.split(" ");
            if (!date[1].startsWith("0") || date[1].startsWith("0:")) {
                temp = date[0] + " 0" + date[1];
            } else System.out.println(exception);
            ifExpired(temp);
        }
        this.lastDate = temp;
    }

    private void ifExpired(String lastDate) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");//07.04.2021 09:49:20
        System.out.println(lastDate);
        LocalDateTime dateTime = LocalDateTime.parse(lastDate, formatter);
        if (dateTime.plusDays(1).isBefore(LocalDateTime.now())) {
            this.setExpired(true);
        }
    }

    public boolean isExpired() {
        System.out.println(expired);
        return expired;
    }
}
