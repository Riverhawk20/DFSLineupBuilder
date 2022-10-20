package slate;

import java.util.function.Predicate;

public class DraftGroup {
    public static int ClassicGametypeId = 1;
    public static Predicate<DraftGroup> DraftGroupFilterOut = dg -> dg.GameTypeId != DraftGroup.ClassicGametypeId
            || (dg.ContestStartTimeSuffix != null
                    && dg.ContestStartTimeSuffix.contains("Madden"));
    public int GameTypeId;
    public int DraftGroupId;
    public String StartDate;
    public String ContestStartTimeSuffix;
    // public ArrayList<Draftable> draftables;
}
