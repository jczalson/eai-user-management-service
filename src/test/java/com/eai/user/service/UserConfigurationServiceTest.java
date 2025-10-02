package com.eai.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eai.user.dto.ConfigAttributeDTO;
import com.eai.user.dto.ConfigurationTypeEnum;
import com.eai.user.repository.CustomerRepository;

@ExtendWith(SpringExtension.class)
public class UserConfigurationServiceTest {

@InjectMocks UserConfigurationServiceImpl  configService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testConfig() {
      String str ="[{\"configurationType\":\"LOCATION_CONFIRMATION\",\"configurationAttributes\":[{\"configurationAttribute\":\"docking\",\"configurationEnabled\":\"true\",\"configurationValue\":null}],\"updatedDate\":\"2025-04-25\",\"createdBy\":\"JC\"}]";
      when(customerRepository.findJsonFile("jc@gmail.com")).thenReturn(Optional.of(str));
      List<ConfigAttributeDTO> userConfigAtt = configService.getUserConfigAtt("jc@gmail.com");
      assertNotNull(userConfigAtt);
      AtomicBoolean attbute = new AtomicBoolean();
 if (userConfigAtt != null && userConfigAtt.size() > 0) {
                userConfigAtt.stream().forEach(cfg -> {
                    if (cfg.getConfigurationType() != null && cfg.getConfigurationType().getConfigType()
                            .equals(ConfigurationTypeEnum.LOCATION_CONFIRMATION.getConfigType())) {
                        cfg.getConfigurationAttributes().stream().forEach(attb -> {
                            if(attb.getConfigurationAttribute() != null
                                    && attb.getConfigurationAttribute().equals("docking")) {
                                attbute.set(attb.getConfigurationEnabled().booleanValue());
                            }
                        });
                    }
                });
                assertEquals(true, attbute.get());
            }
    }

}
