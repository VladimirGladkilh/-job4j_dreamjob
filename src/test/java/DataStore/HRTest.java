package DataStore;

import junit.framework.TestCase;
import org.junit.Assert;

public class HRTest extends TestCase {

    public void testTestEquals() {
        HR hr = new HR("admin","Ivanov","Ivan","", "password");
        HR hr2 = new HR("admin2","Ivanov","Ivan","", "password");
        Assert.assertTrue(!hr.equals(hr2));
    }

    public void testTestToString() {
        HR hr = new HR("admin","Ivanov","Ivan","", "password");
        Assert.assertEquals(hr.toString(), "Ivanov Ivan ");
    }
}