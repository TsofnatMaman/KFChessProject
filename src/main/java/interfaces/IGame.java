package interfaces;

public interface IGame {

    public void addCommand(ICommand cmd);

    public void update() ;

    public IPlayer getPlayer1();

    public IPlayer getPlayer2();

    public IBoard getBoard();

    public void handleSelection(IPlayer player) ;

    public int win();
}
