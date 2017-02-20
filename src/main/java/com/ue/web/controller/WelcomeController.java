package com.ue.web.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
public class WelcomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", "wangxu");
		map.put("birthday", "19820722");
		String jwtToken = Jwts.builder().setSubject("Joe")
				.setClaims(map)
				.compressWith(CompressionCodecs.DEFLATE)
				.signWith(SignatureAlgorithm.HS512, generateKey("secret"))
				.compact();
		Cookie token = new Cookie("token", jwtToken);
		token.setPath("/");
		token.setHttpOnly(true);
		token.setMaxAge(3600);
		response.addCookie(token);
		return "welcome";
	}
	
	public static Key generateKey(final String key) {
		Key ret = new SecretKey() {
			private static final long serialVersionUID = 1L;
			public String getAlgorithm() {
				return "HmacSHA512";
			}

			public String getFormat() {
				return "RAW";
			}
			public byte[] getEncoded() {
				return key.getBytes();
			};
		};
		return ret;
	}

}
