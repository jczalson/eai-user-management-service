package com.eai.user.service;

import com.eai.user.dto.TwoFactorVerificationsDTO;

public interface TwoFactorVerificationsService {

    public TwoFactorVerificationsDTO getUserFromFactorVerificationByCode(String code);

    public TwoFactorVerificationsDTO createVerificationCode(long userId);
    
    public TwoFactorVerificationsDTO verifyCode(String email, String code);
    
    public void deleteCode(TwoFactorVerificationsDTO two);
}
