package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence-tier")
public class UserAccountFileDAOTest {
    UserAccountFileDAO userAccountFileDAO;
    UserAccount[] testUserAccounts;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupUserAccountFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUserAccounts = new UserAccount[3];
        testUserAccounts[0] = new UserAccount(60,"popop", "heybuddy", false, null, null);
        testUserAccounts[1] = new UserAccount(61,"bobob", "joob123", false, null, null);
        testUserAccounts[2] = new UserAccount(62,"lolol", "baowow", false, null, null);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the hero array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),UserAccount[].class))
                .thenReturn(testUserAccounts);
        userAccountFileDAO = new UserAccountFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testCreateUserAccount() throws IOException {
        UserAccount userAccount = new UserAccount(63, "NEWUSER", "NEWPASS", false, null, null);

        UserAccount result = assertDoesNotThrow(() -> userAccountFileDAO.createUserAccount(userAccount), 
                                "Unexpected exception thrown");

        assertNotNull(result);
        UserAccount actual = userAccountFileDAO.searchUsername("NEWUSER");
        assertEquals(actual.getId(), userAccount.getId());
        assertEquals(actual.getUsername(), userAccount.getUsername());

    }

    @Test
    public void testSearchUsername() throws IOException {
        UserAccount result = assertDoesNotThrow(() -> userAccountFileDAO.searchUsername("popop"), 
                                "Unexpected exception thrown");
        assertNotNull(result);
        UserAccount actual = userAccountFileDAO.searchUsername("popop");
        assertEquals(actual.getId(), 60);
        assertEquals(actual.getUsername(), "popop");
        assertEquals(actual.getPassword(), "heybuddy");

        //null test
        UserAccount resultnull = assertDoesNotThrow(() -> userAccountFileDAO.searchUsername("thisisatest5050"), 
                                "Unexpected exception thrown");
        assertEquals(resultnull, null);
    }
    @Test
    public void testupdateuser() throws IOException
    {
        //null test
        UserAccount userAccount = new UserAccount(63, "NEWUSER", "NEWPASS", false, null, null);
        UserAccount resultnull = assertDoesNotThrow(() -> userAccountFileDAO.updateUser(userAccount), 
                                "Unexpected exception thrown");
        assertEquals(resultnull, null);
        
        userAccountFileDAO.createUserAccount(userAccount);

        UserAccount userAccountupdate = new UserAccount(63, "NEWUSER", "NEWPASSupdate", false, null, null);
        //working test
        UserAccount result = assertDoesNotThrow(() -> userAccountFileDAO.updateUser(userAccountupdate), 
        "Unexpected exception thrown");
        assertNotNull(result);
        UserAccount actual = userAccountFileDAO.updateUser(userAccountupdate);
        assertEquals(actual.getPassword(), "NEWPASSupdate");
    }

    @Test
    public void testsearchPassword() throws IOException
    {
        UserAccount userAccount = new UserAccount(63, "NEWUSER", "NEWPASS", false, null, null);
        UserAccount resultnull = assertDoesNotThrow(() -> userAccountFileDAO.searchPassword(userAccount.getPassword()), 
                                "Unexpected exception thrown");
        assertEquals(resultnull, null);
        
        userAccountFileDAO.createUserAccount(userAccount);

        //working test
        UserAccount result = assertDoesNotThrow(() -> userAccountFileDAO.searchPassword(userAccount.getPassword()), 
        "Unexpected exception thrown");
        assertNotNull(result);

        UserAccount actual = userAccountFileDAO.searchPassword(userAccount.getPassword());
        assertEquals(actual.getId(), 63);
        assertEquals(actual.getUsername(), "NEWUSER");
        assertEquals(actual.getPassword(), "NEWPASS");
    }
}

