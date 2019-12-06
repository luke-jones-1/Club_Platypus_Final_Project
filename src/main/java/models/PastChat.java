package models;


import lombok.Data;

@Data
public class PastChat {
    private String username;
    private String content;
    private String time_created;
    private String platypusColour;

    public PastChat(String username, String content, String time_created, String platypusColour){
        this.username = username;
        this.content = content;
        this.time_created = time_created;
        this.platypusColour = platypusColour;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTime_created() {
        return time_created;
    }

    public String getPlatypusColour() {
        return platypusColour;
    }
}
