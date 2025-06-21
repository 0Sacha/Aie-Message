package com.example.co26seq07projet_bilan;

public class CurrentUser {
    private static int id;
    private static String pseudo;

    public static void setUser(int userId, String userPseudo) {
        id = userId;
        pseudo = userPseudo;
    }

    public static int getId() {
        return id;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static void clear() {
        id = 0;
        pseudo = null;
    }
}
