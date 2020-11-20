package DataStore;

import junit.framework.TestCase;
import org.junit.Assert;

public class CandidateTest extends TestCase {

    public void testTestToString() {
        Candidate candidate = new Candidate();
        candidate.setLogin("User1");
        Candidate candidate2 = new Candidate();
        candidate2.setLogin("User2");
        Assert.assertTrue(!candidate.equals(candidate2));
    }

    public void testTestEquals() {
        Candidate candidate = new Candidate();
        candidate.setLogin("User1");
        candidate.setFirstName("First");
        candidate.setMiddleName("User");
        candidate.setLastName("Name");
        Assert.assertEquals(candidate.toString(), "First User Name");
    }
}