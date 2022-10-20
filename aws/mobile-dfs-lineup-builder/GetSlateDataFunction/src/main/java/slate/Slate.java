package slate;

import java.util.ArrayList;

public class Slate {
    public String SlateId;
    public String StartDate;
    public String SlateName;
    public ArrayList<Player> Players;

    public Slate(int SlateId, String SlateName, String StartDate, ArrayList<Player> Players) {
        this.SlateId = String.valueOf(SlateId);
        this.StartDate = StartDate;
        this.SlateName = SlateName;
        this.Players = Players;
    }
}
