package com.eai.user.utilities;

import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.entities.AppUser;

public class AccountUtilities {

  public static UserDTO fromUserEntityToDto(AppUser user) {
    UserDTO dto = new UserDTO();
    dto.setIdUser(user.getIdUser());
    dto.setPwd(user.getPwd());
    dto.setStatus(user.getStatus());
    dto.setEmail(user.getEmail());
    dto.setName(user.getName());
    dto.setIsMfa(user.getIsMfa());
    if (StringUtils.isNotBlank(user.getImageUrl())) {
      dto.setImageUrl(user.getImageUrl());
    }
    if (user != null && !user.getUserRoleList().isEmpty()) {
      dto.getRoles().addAll(user.getUserRoleList().stream()
          .map(role -> role.getAppRole().getRlNm())
          .collect(Collectors.toList()));
    }
    return dto;
  }

  public static AppUser fromUserDtoToEntity(UserDTO userDto) {
    AppUser entity = new AppUser();

    if (userDto.getIdUser() != null)
      entity.setIdUser(userDto.getIdUser());

    if (StringUtils.isNotBlank(userDto.getImageUrl())) {
      entity.setImageUrl(userDto.getImageUrl());
    }
    if (StringUtils.isNotBlank(userDto.getPwd()))
      entity.setPwd(userDto.getPwd());

    if (userDto.getStatus() != null)
      entity.setStatus(userDto.getStatus());

    if (StringUtils.isNotBlank(userDto.getEmail()))
      entity.setEmail(userDto.getEmail());

    if (StringUtils.isNotBlank(userDto.getName()))
      entity.setName(userDto.getName());

    if (userDto.getIsMfa() != null)
      entity.setIsMfa(userDto.getIsMfa());

    //  if (userDto != null && !userDto.getUserRoleList().isEmpty()) {
    //   entity.getUserRoleList().addAll(userDto.getUserRoleList());
    // }

    return entity;
  }

  public static AppUser fromUserDtoInputToEntity(UserDTOInput inputUser) {
    AppUser entity = new AppUser();
    entity.setPwd(inputUser.getPassword());
    entity.setStatus(inputUser.getStatusEnum());
    entity.setEmail(inputUser.getEmail());
    if (StringUtils.isNotBlank(inputUser.getName()))
      entity.setName(inputUser.getName());

    entity.setIsMfa(inputUser.isMfa());
    // BeanUtils.copyProperties(inputUser, entity);
    return entity;
  }

  public static UserDTO updatingExistingUserEntity(UserDTO existingUser, UserDTOInput inputUser) {

    if (StringUtils.isNotBlank(inputUser.getEmail()))
      existingUser.setEmail(inputUser.getEmail());
    if (StringUtils.isNotBlank(inputUser.getName()))
      existingUser.setName(inputUser.getName());
    return existingUser;
  }
}
