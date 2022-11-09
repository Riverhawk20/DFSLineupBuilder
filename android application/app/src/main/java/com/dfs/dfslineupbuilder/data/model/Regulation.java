package com.dfs.dfslineupbuilder.data.model;

import java.util.List;

public class Regulation {
    public String StateName;

    public boolean IsLegal;

    public Regulation(String StateName, boolean IsLegal){
        this.StateName = StateName;
        this.IsLegal = IsLegal;
    }
}
