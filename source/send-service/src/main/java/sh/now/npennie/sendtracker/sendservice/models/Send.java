package sh.now.npennie.sendtracker.sendservice.models;

public class Send {
    private String name;
    private Style style;
    private String grade;
    private TickType tickType;
    private String location;

    public Send(String name, Style style, String grade, TickType tickType, String location) {
        this.name = name;
        this.style = style;
        this.grade = grade;
        this.tickType = tickType;
        this.location = location;
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
