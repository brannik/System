package com.brannik.system;

public class AdminAccountsAdaptor {
    private String id;
    private String username;
    private String name;
    private String s_name;
    private String rank;
    private String sklad;

    public AdminAccountsAdaptor(String id, String username, String name, String s_name, String rank, String sklad) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.s_name = s_name;
        this.rank = rank;
        this.sklad = sklad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSklad() {
        return sklad;
    }

    public void setSklad(String sklad) {
        this.sklad = sklad;
    }

    @Override
    public String toString() {
        String Names = name + " " + s_name;
        return Names;
    }

}
