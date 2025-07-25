package pieces;

public class Position {
    int r;
    int c;

    public Position(int r, int c){
        this.r = r;
        this.c = c;
    }

    public int getRow() {
        return r;
    }

    public int getCol() {
        return c;
    }

    public int dx(Position other){
        return r-other.r;
    }

    public int dy(Position other){
        return c-other.c;
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof Position && ((Position)obj).r == r && ((Position)obj).c == c;
    }

    public Position add(int x, int y){
        return new Position(r+x, c+y);
    }

    @Override
    public String toString() {
        return "row: "+r+", col: "+c;
    }

    public void reduceOneRow(){
        r--;
    }

    public void reduceOneCol(){
        c--;
    }

    public void addOneRow(){
        r++;
    }

    public void addOneCol(){
        c++;
    }

    public Position copy(){
        return new Position(getRow(), getCol());
    }
}

