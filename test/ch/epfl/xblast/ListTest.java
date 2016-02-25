package ch.epfl.xblast;

import java.util.List;

import org.junit.Test;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/
public class ListTest {

    @Test(expected = IllegalArgumentException.class)
    public void mirroredMethodThrowsExceptionOnNullArgument() {
        List <Integer> test = null;
        Lists.mirrored(test);
        
    }

}
