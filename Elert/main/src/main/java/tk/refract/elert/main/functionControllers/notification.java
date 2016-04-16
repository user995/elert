package tk.refract.elert.main.functionControllers;

/**
 * Created by s212289853 on 16/04/2016.
 */
public class Notification {
    private String name;
    private String location;
    private String cell;

    public Notification(String name, String location, String cell) {
        this.name = name;
        this.location = location;
        this.cell = cell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }
}
