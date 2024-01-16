package com.service.gateway.security.sevices;

import com.service.gateway.model.UserDTO;
import com.service.gateway.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    UserDetailsImpl userDetails = new UserDetailsImpl(1L, "eike", "test@gmail.com", "passwort123!YoY");

    @Test
    void testBuild() {
        UserDTO userRequestDTO = new UserDTO(1L, "eike", "test@gmail.com", "passwort123!YoY");
        UserDetailsImpl builtUser = UserDetailsImpl.build(userRequestDTO);
        assertEquals(userDetails, builtUser);
    }

    @Test
    void testGetId() {
        assertEquals(1L, userDetails.id());
    }

    @Test
    void testGetEmail() {
        assertEquals("test@gmail.com", userDetails.getEmail());
    }

    @Test
    void testGetUsername(){
        assertEquals("eike", userDetails.getUsername());
    }

    @Test
    void testGetPassword(){
        assertEquals("passwort123!YoY", userDetails.getPassword());
    }

    @Test
    void testEquals(){
        assertEquals(userDetails, userDetails);
    }

    @Test
    void testEqualsDifferentClass(){
        assertNotEquals("abc", userDetails);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(null, userDetails);
    }

    @Test
    void testDoesNotEqual() {
        assertNotEquals(userDetails, new UserDetailsImpl(7L, "tim", "hello@gmail.com", "passwort123!Y"));
    }

    @Test
    void testIdsAreEqual() {
        assertEquals(userDetails, new UserDetailsImpl(1L, "Max", "matrix@gmail.com", "passwort123!YoY"));
    }

}
