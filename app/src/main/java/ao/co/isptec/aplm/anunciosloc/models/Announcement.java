package ao.co.isptec.aplm.anunciosloc.models;

public class Announcement {
    public String title;
    public String content;
    public String location;
    public String date;

    public Announcement(String title, String content, String location, String date) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getLocation() {
        return location;
    }
}
