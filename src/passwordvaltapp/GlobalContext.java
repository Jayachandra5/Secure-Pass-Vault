package passwordvaltapp;

public class GlobalContext {
    private static String currentUserName;
    private static String Otp;

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void setCurrentUserName(String userName) {
        currentUserName = userName;
    }
    
    public static String getCurrentOtp() {
        return Otp;
    }

    public static void setCurrentOtp(String otp) {
        Otp = otp;
    }
    
    
}
