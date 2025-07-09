package com.example.demo.service.impl;

import java.security.cert.CertSelector;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.*;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CertService;
import com.example.demo.util.HashUtil;

@Service
public class CertServiceImpl implements CertService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public com.example.demo.model.dto.UserCert getCert(String username, String password) throws CertException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new CertException("查無此人");
		}

		try {
			// 用 HashUtil 驗證密碼
			boolean valid = HashUtil.verifyPassword(password, user.getSalt(), user.getPassword());
			if (!valid) {
				throw new CertException("密碼錯誤");
			}
		} catch (Exception e) {
			throw new CertException("驗證失敗：" + e.getMessage());
		}

		return new UserCert(user.getId(), user.getName(), user.getUsername(), null, // 密碼不回傳
				user.getBirthdate(), user.getGender(), user.getEmail(), user.getActive());
	}
}