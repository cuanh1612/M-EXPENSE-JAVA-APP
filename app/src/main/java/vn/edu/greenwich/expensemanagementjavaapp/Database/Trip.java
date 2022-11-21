package vn.edu.greenwich.expensemanagementjavaapp.Database;

public class Trip {
    //  Necessary properties for a trip object
    private int Id;
    private String Name;
    private String Destination;
    private String Date;
    private String Description;
    private String Note;
    private String Topic;
    private int RequiredRiskAssessment;
    private String Image;

    //  The constructor function initializes a trip object
    public Trip(int id,
                String name,
                String destination,
                String date,
                String description,
                String note,
                String topic,
                int requiredRiskAssessment,
                String image) {
        Id = id;
        Name = name;
        Destination = destination;
        Date = date;
        Description = description;
        Note = note;
        Topic = topic;
        RequiredRiskAssessment = requiredRiskAssessment;
        Image = image;
    }

    /*
        The toString function is provided and is overridden to retrieve
        a string containing the basic information of a trip object
    */
    @Override
    public String toString() {
        return "Trip{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Destination='" + Destination + '\'' +
                ", Date='" + Date + '\'' +
                ", Description='" + Description + '\'' +
                ", Note='" + Note + '\'' +
                ", Topic='" + Topic + '\'' +
                ", RequiredRiskAssessment=" + RequiredRiskAssessment +
                ", Image='" + Image + '\'' +
                '}';
    }

    //  Getter and setter
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public int getRequiredRiskAssessment() {
        return RequiredRiskAssessment;
    }

    public void setRequiredRiskAssessment(int requiredRiskAssessment) {
        RequiredRiskAssessment = requiredRiskAssessment;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
