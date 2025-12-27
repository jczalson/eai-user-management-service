package com.eai.user.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eai.user.dto.TwoFactorVerificationsDTO;
import com.eai.user.dto.UserDTO;
import com.eai.user.entities.AppUser;
import com.eai.user.entities.TwoFactorVerificationsEntity;
import com.eai.user.exception.InvalidateRequestException;
import com.eai.user.repository.AppUserRepository;
import com.eai.user.repository.TwoFactorVerificationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwoFactorVerificationsImpl implements TwoFactorVerificationsService {

    @Autowired
    private TwoFactorVerificationsRepository factorVerificationsRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public TwoFactorVerificationsDTO getUserFromFactorVerificationByCode(String code) {
        TwoFactorVerificationsDTO dto = new TwoFactorVerificationsDTO();
        Optional<TwoFactorVerificationsEntity> tfvc = factorVerificationsRepository.findUserByCodeVerification(code);
        if (tfvc.isPresent()) {
            BeanUtils.copyProperties(tfvc.get().getUser(), dto.getUser());
            dto.getUser().setEmail(tfvc.get().getUser().getEmail());
            BeanUtils.copyProperties(tfvc.get(), dto);
            dto.setExpiryDate(new Timestamp(tfvc.get().getExpiryDate().getTime()).toLocalDateTime());
        }
        return dto;
    }

    @Override
    public TwoFactorVerificationsDTO createVerificationCode(long userId) {
        TwoFactorVerificationsDTO dto = new TwoFactorVerificationsDTO();

        Optional<AppUser> userEntity = appUserRepository.findById(userId);
        Optional<TwoFactorVerificationsEntity> codeVerification = factorVerificationsRepository.findCodeVerificationByIdUser(userId);
        if (codeVerification.isPresent()) {
            factorVerificationsRepository.deleteById(codeVerification.get().getId());
            factorVerificationsRepository.flush();
        }
        if (userEntity.isPresent()) {
            dto.setExpiryDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
            dto.setCode(generateCode().toUpperCase());
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity.get(), userDTO);
            dto.setUser(userDTO);
            TwoFactorVerificationsEntity entity = new TwoFactorVerificationsEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setExpiryDate(Timestamp.valueOf(dto.getExpiryDate()));
            entity.setUser(userEntity.get());
            factorVerificationsRepository.save(entity);
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }
        return null;
    }

    private String generateCode() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    @Override
    public TwoFactorVerificationsDTO verifyCode(String email, String code) {
        TwoFactorVerificationsDTO dto = new TwoFactorVerificationsDTO();
        Optional<TwoFactorVerificationsEntity> tfvc = factorVerificationsRepository
                .findCodeVerificationByIdUserAndCode(email, code);
        if (tfvc.isPresent()) {
            BeanUtils.copyProperties(tfvc.get(), dto);
            BeanUtils.copyProperties(tfvc.get().getUser(), dto.getUser());
            dto.setExpiryDate(new Timestamp(tfvc.get().getExpiryDate().getTime()).toLocalDateTime());
            return dto;
        }else{
            log.warn("No code retrieved {} for the user id {}",code,email);
        throw new InvalidateRequestException("Code is invalid. please try again");
        }
    }

    @Override
    public void deleteCode(TwoFactorVerificationsDTO two) {
        factorVerificationsRepository.deleteById(two.getId());
    }
}
