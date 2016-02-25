package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    @Test
    public void mirroredMethodIsWorking(){
        
       List<Integer> test = new ArrayList <Integer>();
       test.add(1);
       test.add(2);
       test.add(3);
       List<Integer> result = Lists.mirrored(test);
       List<Integer> expectedResult = new ArrayList <Integer>();
       expectedResult.add(1);
       expectedResult.add(2);
       expectedResult.add(3);
       expectedResult.add(2);
       expectedResult.add(1);
       assertEquals(expectedResult,result);
        
    }

}
