package enums;

public enum Properties {
    TITLE("Title:"),
    PUBLISHER("Publisher:"),
    DEVELOPER("Developer:");

    public String value;

    Properties(String value) {
        this.value = value;
    }
}
