package org.android.jasoncromer.coverme;

/**
 * Created by jason on 6/27/15.
 */
public class UserModel {

    public int userID;
    public String userName;
    public String userCompanyName;
    public String userCompanyLocation;
    public String userDayCovered;
    public String userHoursCovered;

    public UserModel(int userID, String userName, String userCompanyName, String userCompanyLocation,
                     String userDayCovered, String userHoursCovered) {

        this.userID = userID;
        this.userName = userName;
        this.userCompanyName = userCompanyName;
        this.userCompanyLocation = userCompanyLocation;
        this.userDayCovered = userDayCovered;
        this.userHoursCovered = userHoursCovered;
    }

    public UserModel() {

    }
}
