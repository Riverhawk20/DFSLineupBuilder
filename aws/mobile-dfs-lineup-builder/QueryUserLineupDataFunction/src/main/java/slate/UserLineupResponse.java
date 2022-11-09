package slate;
import java.util.List;

public class UserLineupResponse {
    public Object User;
    public List<Object> Lineups;

    public UserLineupResponse(Object User, List<Object> Lineups){
        this.User = User;
        this.Lineups = Lineups;
    }
}
