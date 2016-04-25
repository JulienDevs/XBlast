package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.RunLengthEncoder;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public class RunLenghtEncoderTest {
    @Test
    public void encoderWorksOnNonTrivialList(){
        List<Byte> l = new ArrayList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 3,(byte) 3));
        
        List<Byte> actual = RunLengthEncoder.encode(l);
        List<Byte> expected = new ArrayList<>(Arrays.asList((byte) -1, (byte) 3, (byte) 2, (byte) 2, (byte) 3,(byte) 3));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void encoderWorksOnLongConstantList(){
        List<Byte> l = new ArrayList<>();
        
        for(int i = 0 ; i < 195 ; i++){
            l.add((byte) 1);
        }
        System.out.println((byte)(-195 + 2));
        System.out.println(RunLengthEncoder.encode(l));
    }
    
    @Test
    public void decoderWorksOnNonTrivialList(){
        List<Byte> l = new ArrayList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 3,(byte) 3));
        
        List<Byte> actual = RunLengthEncoder.decode(RunLengthEncoder.encode(l));
        List<Byte> expected = l;
        
        assertEquals(expected, actual);
    }
}
