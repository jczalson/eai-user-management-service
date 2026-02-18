package com.eai.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppUser;
import com.eai.user.messaging.producer.UserActivityProducer;
import com.eai.user.repository.AppUserRepository;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountServiceImplm accountService;

    @Mock
    private AppUserRepository repository;

    @Mock
    UserActivityProducer userActivityProducer;

    @Test
    public void testLoadUserByUsername() {
        AppUser user = new AppUser();
        user.setIdUser(1);
        user.setEmail("jc@mail.com");
        user.setName("jc");
        when(repository.findUserByEmailAndStatus("jc@mail.com")).thenReturn(Optional.of(user));
        UserDTO dto = accountService.loadUserByUsername(user.getEmail());
        assertNotNull(dto);
        assertEquals(dto.getEmail(), user.getEmail());
    }
}
