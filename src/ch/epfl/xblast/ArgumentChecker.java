package ch.epfl.xblast;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class ArgumentChecker {
    private ArgumentChecker(){}
    
    public static int requireNonNegative(int value) throws IllegalArgumentException{
        if(value < 0){
            throw new IllegalArgumentException();
        } else {
            return value;
        }
    }
}
