package com.brannik.system;

public class AdminGetAccounts {


    private final String[][] ranks={
            {"1","Гост"},
            {"2","Потребител"},
            {"3","Администратор"}
    };
    private final String[][] skladNames= {
            {"0","Няма"},
            {"1","Първи"},
            {"2","Втори"},
            {"3","Мострена"},
            {"4","Четвърти"},
            {"5","Победа"},
            {"6","Клетки"}
    };

    //// generate list of accounts

    public String[] getRankNames(){
        String[] ranksNames = new String[ranks.length];
        for(int i=0;i<ranks.length;i++){
            ranksNames[i] = ranks[i][1];
        }
        return ranksNames;
    }

    public String[] getSkladNames(){
        String[] skladNamesText = new String[skladNames.length];
        for(int i=0;i<skladNames.length;i++){
            skladNamesText[i] = skladNames[i][1];
        }
        return skladNamesText;
    }

    public String[] getAccounts(){
        String[] acc = {"one acc","two acc","tree acc","four acc"};
        return acc;
    }

}
