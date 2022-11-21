package vn.edu.greenwich.expensemanagementjavaapp.Database;

public class Expense {
    //  Necessary properties for a expense object
    private int Id;
    private String Type;
    private String Date;
    private String Time;
    private int Amount;
    private String AdditionalComments;
    private String Location;
    private int TripId;

    //  The constructor function initializes a expense object
    public Expense(int id, String type, String date, String time, int amount, String additionalComments, String location, int tripId) {
        Id = id;
        Type = type;
        Date = date;
        Time = time;
        Amount = amount;
        AdditionalComments = additionalComments;
        Location = location;
        TripId = tripId;
    }

    /*
        The toString function is provided and is overridden to
        retrieve a string containing the basic information of a expense object
    */
    @Override
    public String toString() {
        return "Expense{" +
                "Id=" + Id +
                ", Type='" + Type + '\'' +
                ", Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                ", Amount=" + Amount +
                ", AdditionalComments='" + AdditionalComments + '\'' +
                ", Location='" + Location + '\'' +
                ", TripId=" + TripId +
                '}';
    }

    //  Getter and setter
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getAdditionalComments() {
        return AdditionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        AdditionalComments = additionalComments;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getTripId() {
        return TripId;
    }

    public void setTripId(int tripId) {
        TripId = tripId;
    }
}
