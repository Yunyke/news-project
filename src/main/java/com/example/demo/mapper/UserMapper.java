package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserCert; // 引入 UserCert
import com.example.demo.model.entity.User;

import jakarta.annotation.PostConstruct;

@Component 
public class UserMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostConstruct
    public void init() {
        // 自定義欄位對應：User.id -> UserDto.userId (已有的)
        modelMapper.typeMap(User.class, UserDto.class)
            .addMapping(User::getId, UserDto::setUserId);

        // 反向對應：UserDto.userId -> User.id (已有的)
        modelMapper.typeMap(UserDto.class, User.class)
            .addMapping(UserDto::getUserId, User::setId);

        // ✅ 新增：User.id -> UserCert.userId
        modelMapper.typeMap(User.class, UserCert.class)
            .addMapping(User::getId, UserCert::setUserId);

        // ✅ 新增：UserCert.userId -> User.id (如果 UserCert 也會轉回 User 實體)
        modelMapper.typeMap(UserCert.class, User.class)
            .addMapping(UserCert::getUserId, User::setId);
    }
	
    // 新增 User 到 UserCert 的轉換方法
    public UserCert toCert(User user) {
        return modelMapper.map(user, UserCert.class);
    }

    // 新增 UserCert 到 User 的轉換方法 (如果需要)
    public User toEntity(UserCert userCert) {
        return modelMapper.map(userCert, User.class);
    }
    
    // 以下是原有的 DTO 轉換方法
	public UserDto toDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}
	
	public User toEntity(UserDto userDto) {
		return modelMapper.map(userDto, User.class);
	}
}