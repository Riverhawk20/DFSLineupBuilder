package slate;

import java.util.List;

public class UserLineupResponse {
    public User User;
    public List<Lineup> Lineups;

    public UserLineupResponse(User User, List<Lineup> Lineups) {
        this.User = User;
        this.Lineups = Lineups;
    }
}
