package fr.uv1.tests.unit;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.*;

import fr.uv1.bettingServices.*;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.SubscriberException;

public class SubscriberTest {

	private Subscriber subs;

	/*
	 * Subscribers should be created with valid strings
	 */
	@Test
	public void testSubscriber() throws BadParametersException, SubscriberException {
		subs = new Subscriber(new String("Duran"), new String("Miguel"),
				new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
		assertTrue(subs.getFirstname().equals("Miguel"));
		assertTrue(subs.getLastname().equals("Duran"));
		assertTrue(subs.getUsername().equals("worldChamp"));
		assertTrue(subs.getPassword() != null);
	}

	@Test(expected = BadParametersException.class)
	public void testNullLastnameSubscriber() throws BadParametersException, SubscriberException {
		new Subscriber(null, new String("Miguel"), new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
	}

	@Test(expected = BadParametersException.class)
	public void testNullFirstnameSubscriber() throws BadParametersException, SubscriberException {
		new Subscriber(new String("Duran"), null, new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
	}

	@Test(expected = BadParametersException.class)
	public void testNullUsernameSubscriber() throws BadParametersException, SubscriberException {
		new Subscriber(new String("Duran"), new String("Miguel"), null, new GregorianCalendar(1991, 06, 31));
	}

	@Test(expected = BadParametersException.class)
	public void testInvalidLastnameSubscriber() throws BadParametersException, SubscriberException {
		new Subscriber(new String(" "), new String("Miguel"), new String(
				"worldChamp"), new GregorianCalendar(1991, 06, 31));
	}

	@Test(expected = BadParametersException.class)
	public void testInvalidFirstnameSubscriber() throws BadParametersException, SubscriberException {
		new Subscriber(new String("Duran"), new String(""), new String(
				"worldChamp"), new GregorianCalendar(1991, 06, 31));
	}

	@Test(expected = BadParametersException.class)
	public void testInvalidUsernameSubscriber() throws BadParametersException, SubscriberException {
		new Subscriber(new String("Duran"), new String("Miguel"),
				new String(""), new GregorianCalendar(1991, 06, 31));
	}

	@Test
	public void testHasUsername() throws BadParametersException, SubscriberException {
		subs = new Subscriber(new String("Duran"), new String("Miguel"),
				new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
		
		assertTrue(subs.getUsername().equals("worldChamp"));
		assertFalse(subs.getUsername().equals("wddfkjddfk"));
	}

	@Test
	public void testEqualsObject() throws BadParametersException, SubscriberException {
		subs = new Subscriber(new String("Duran"), new String("Miguel"),
				new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
		
		// Same subscriber = same username
		Subscriber s = new Subscriber(new String("Duran"),
				new String("Miguel"), new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
		assertTrue(subs.equals(s));

		s = new Subscriber(new String("Durano"), new String("Miguel"),
				new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
		assertTrue(subs.equals(s));

		// Different subscriber = different username
		s = new Subscriber(new String("Duran"), new String("Miguelo"),
				new String("worldC"), new GregorianCalendar(1991, 06, 31));
		assertFalse(subs.equals(s));
	}
	/*************** Homemade Tests !! 
	 * @throws SubscriberException **************/
	@Test
	public void testSetLastname() throws BadParametersException, SubscriberException {
	    subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        subs.setLastname("Durano");
        assertTrue(subs.getLastname().equals("Durano"));
	}
	
	@Test(expected = BadParametersException.class)
    public void testNullSetLastname() throws BadParametersException, SubscriberException {
        subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        
        // Lastname can't be null
        subs.setLastname(null);
    }
    
    @Test(expected = BadParametersException.class)
    public void testInvalidsetLastname() throws BadParametersException, SubscriberException {
        subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        
        //Lastname is invalid
        subs.setLastname(new String(""));
    }
    
    @Test
	public void testSetFirstname() throws BadParametersException, SubscriberException {
	    subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        subs.setFirstname("Miguelo");
        assertTrue(subs.getFirstname().equals("Miguelo"));
	}

    @Test(expected = BadParametersException.class)
    public void testNullSetFirstname() throws BadParametersException, SubscriberException {
        subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        
        // Firstname can't be null
        subs.setFirstname(null);
    }
    
    @Test(expected = BadParametersException.class)
    public void testInvalidSetFirstname() throws BadParametersException, SubscriberException {
        subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        
        // Firstname is invalid
        subs.setFirstname(new String(""));
    }
    
    @Test
	public void testSetUsername() throws BadParametersException, SubscriberException {
	    subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        subs.setUsername("worldC");
        assertTrue(subs.getUsername().equals("worldC"));
	}

    @Test(expected = BadParametersException.class)
    public void testNullSetUsername() throws BadParametersException, SubscriberException {
        subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        
        // Username can't be null
        subs.setUsername(null);
    }
    
    @Test(expected = BadParametersException.class)
    public void testInvalidSetUsername() throws BadParametersException, SubscriberException {
        subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        
        // Username is invalid
        subs.setUsername(new String(""));
    }
    
    @Test
    public void testCrediterSoldeJetons() throws SubscriberException, BadParametersException {
    	subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
    	
    	assertTrue(subs.getTokens() == 0);
    	subs.creditTokens(25);
    	assertTrue(subs.getTokens() == 25);
    }
    
    @Test(expected = BadParametersException.class)
    public void testInvalidCrediterSoldeJetons() throws SubscriberException, BadParametersException {
    	subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
    	
    	subs.creditTokens(-10);
    }
    
    @Test
    public void testDebiterSoldeJetons() throws SubscriberException, BadParametersException {
    	subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
    	
    	assertTrue(subs.getTokens() == 0);
    	subs.creditTokens(25);
    	assertTrue(subs.getTokens() == 25);
    	subs.debitTokens(12);
    	assertTrue(subs.getTokens() == 13);
    }

    @Test(expected = BadParametersException.class)
    public void testInvalidDebiterSoldeJetons() throws SubscriberException, BadParametersException {
    	subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
    	
    	subs.debitTokens(-10);
    }
    
    @Test(expected = SubscriberException.class)
    public void testInsuffisantDebiterSoldeJetons() throws SubscriberException, BadParametersException {
    	subs = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
    	
    	subs.creditTokens(10);
    	subs.debitTokens(20);
    }

}