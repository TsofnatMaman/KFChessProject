package moves;

import java.util.function.Predicate;

public enum ECondition {
    NON_CAPTURE(d-> d.board.getPiece(d.to) == null),
    FIRST_TIME(d->d.pieceFrom.getId().equals(d.pieceFrom.getPos().toString())),
    CAPTURE(d->d.board.getPiece(d.to) != null);

    
    private Predicate<Data> condition;

    ECondition(Predicate<Data> condition){
        this.condition = condition;
    }

    public boolean isCanMove(Data data){
        return condition.test(data);
    }
}
