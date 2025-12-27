package com.eai.user.utilities;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
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
        dto.setPassword(user.getPassword());
        dto.setStatusEnum(user.getUserStatusEnum());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setIsMfa(user.getIsMfa());
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

        if(StringUtils.isNotBlank(userDto.getPassword()))
        entity.setPassword(userDto.getPassword());

        if(userDto.getStatusEnum() !=null)
        entity.setUserStatusEnum(userDto.getStatusEnum());

        if(StringUtils.isNotBlank(userDto.getEmail()))
        entity.setEmail(userDto.getEmail());

        if(StringUtils.isNotBlank(userDto.getName()))
        entity.setName(userDto.getName());

        if (StringUtils.isNotBlank(userDto.getPhoto())) {
            entity.setPhoto(userDto.getPhoto());
        }
        if(userDto.getIsMfa() != null)
        entity.setIsMfa(userDto.getIsMfa());
    
        BeanUtils.copyProperties(userDto, entity);
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
 BeanUtils.copyProperties(inputUser, entity);
        return entity;
    }
}
