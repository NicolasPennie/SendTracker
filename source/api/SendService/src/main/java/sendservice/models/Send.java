package sendservice.models;

import java.util.UUID;

public class Send {
    private UUID id;
    private String name;
    private Style style;
    private String grade;
    private TickType tickType;
    private String location;

    public Send(String name, Style style) {
        this(null, name, style, null, null, null);
    }

    public Send(String name, Style style, String grade, TickType tickType, String location) {
        this(null, name, style, grade, tickType, location);
    }

    public Send(UUID id, String name, Style style, String grade, TickType tickType, String location) {
        this.id = id;
        this.name = name;
        this.style = style;
        this.grade = grade;
        this.tickType = tickType;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public TickType getTickType() {
        return tickType;
    }

    public void setTickType(TickType tickType) {
        this.tickType = tickType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
