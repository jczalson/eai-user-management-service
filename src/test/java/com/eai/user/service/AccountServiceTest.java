package com.eai.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.UserEventEntity;
import com.eai.user.entities.UserStatusEnum;
import com.eai.user.messaging.producer.UserActivityProducer;
import com.eai.user.repository.AppRoleRepository;
import com.eai.user.repository.AppUserRepository;
import com.eai.user.repository.UserRoleRepository;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountServiceImplm accountService;

    @Mock
    private AppUserRepository repository;

    @Mock
    UserActivityProducer userActivityProducer;

      @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppRoleRepository appRoleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TwoFactorVerificationsService twoFactorVerificationsService;

    @Test
    public void testLoadUserByUsername() {
        AppUser user = new AppUser();
        user.setIdUser(1);
        user.setEmail("jc@mail.com");
        user.setName("jc");
        user.setIsMfa(Boolean.FALSE);
        user.setUserStatusEnum(UserStatusEnum.CREATED);
        UserEventEntity userEventEntity = new UserEventEntity();
        userEventEntity.setIdUserEvent(1L);
        user.setUserEvents(Arrays.asList(userEventEntity));
        
        user.setUserRoleList(new ArrayList<>());
        when(repository.findUserByEmailAndStatus("jc@mail.com")).thenReturn(Optional.of(user));
        UserDTO dto = accountService.loadUserByUsername(user.getEmail());
        assertNotNull(dto);
        assertEquals(dto.getEmail(), user.getEmail());
    }
}
