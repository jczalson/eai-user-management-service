package com.eai.user.utilities;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.eai.user.dto.UserDTO;
import com.eai.user.dto.UserDTOInput;
import com.eai.user.entities.AppUser;

public class AccountUtilities {

    public static UserDTO fromUserEntityToDto(AppUser user) {
        UserDTO dto = new UserDTO();
        dto.setIdUser(user.getIdUser());
        dto.setPassword(user.getPassword());
        dto.setStatusEnum(user.getUserStatusEnum());
        dto.setUserName(user.getEmail());
        dto.setName(user.getName());
        if (StringUtils.isNotBlank(user.getPhoto())) {
            dto.setPhoto(user.getPhoto());
            try {
                dto.setUserPhoto(Files.readAllBytes(Path.of(URI.create(user.getPhoto()))));
            } catch (Exception ex) {

            }
        }
        if (user != null && !user.getUserRoleList().isEmpty()) {
            dto.getRolesOfUser().addAll(user.getUserRoleList().stream()
                    .map(role -> role.getAppRole().getRoleName())
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static AppUser fromUserDtoToEntity(UserDTO userDto) {
        AppUser entity = new AppUser();
        entity.setIdUser(userDto.getIdUser());
        entity.setPassword(userDto.getPassword());
        entity.setUserStatusEnum(userDto.getStatusEnum());
        entity.setEmail(userDto.getUserName());
        if (StringUtils.isNotBlank(userDto.getPhoto())) {
            entity.setPhoto(userDto.getPhoto());
        }

        return entity;
    }

    public static AppUser fromUserDtoInputToEntity(UserDTOInput inputUser) {
        AppUser entity = new AppUser();
        entity.setPassword(inputUser.getPassword());
        entity.setUserStatusEnum(inputUser.getStatusEnum());
        entity.setEmail(inputUser.getEmail());
        if(StringUtils.isNotBlank(inputUser.getName()))
         entity.setName(inputUser.getName());
        if (StringUtils.isNotBlank(inputUser.getPhoto())) {
            entity.setPhoto(inputUser.getPhoto());
        }

        return entity;
    }
}
