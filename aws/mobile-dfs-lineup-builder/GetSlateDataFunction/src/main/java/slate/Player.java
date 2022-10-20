package slate;

public class Player {
    public int PlayerId;

    public String Name;
    public String Position;
    public String Team;
    public String Opponent;
    public int Salary;

    public Player(int PlayerId, String Name, String Position, String Team, String Opponent, int Salary) {
        this.PlayerId = PlayerId;
        this.Name = Name;
        this.Position = Position;
        this.Team = Team;
        this.Opponent = Opponent;
        this.Salary = Salary;
    }
}
