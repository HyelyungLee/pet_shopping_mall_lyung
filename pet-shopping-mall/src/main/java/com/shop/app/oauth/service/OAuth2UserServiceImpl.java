package com.shop.app.oauth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.shop.app.member.dto.MemberCreateDto;
import com.shop.app.member.entity.MemberDetails;
import com.shop.app.member.service.MemberService;
import com.shop.app.oauth.dto.KakaoMemberCreateDto;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService{

	@Autowired
	private MemberService memberService;

	/**
	 * 카카오 인증 후 반환된 code 또는 토큰을 이용하여 사용자 정보를 가져옴.
	 * 만약 해당 사용자가 이미 회원가입된 경우, 해당 회원 정보를 반환.
	 * 아직 회원가입되지 않은 경우, 카카오에서 제공된 정보로 회원가입 처리 후, 회원 정보를 반환.
	 *
	 * @param userRequest OAuth2User의 요청 정보, IDP 정보, 액세스 토큰 등을 포함.
	 * @return OAuth2User 정보. 여기서는 MemberDetails 객체를 반환.
	 * @throws OAuth2AuthenticationException 인증 오류 시 발생
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		ClientRegistration clientRegistration = userRequest.getClientRegistration(); // IDP 정보 가져오기
		OAuth2AccessToken accessToken = userRequest.getAccessToken(); // 액세스 토큰 가져오기
		OAuth2User oauth2User = super.loadUser(userRequest); // 상위 클래스의 사용자 정보 로드 메서드 호출
		
		log.debug("clientRegistration = {}", clientRegistration);
		log.debug("accessToken = {}", accessToken.getTokenValue());
		log.debug("oauth2User = {}", oauth2User);
		
		Map<String, Object> attributes = oauth2User.getAttributes();
		String memberId = attributes.get("id") + "@kakao";
		MemberDetails member = null;

		// 이미 등록된 회원인지 확인
		try {
			member = (MemberDetails) memberService.loadUserByUsername(memberId);
		} catch (UsernameNotFoundException ignore) {
			// 회원이 아니라면 카카오 정보로 회원가입
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
			
			String name = (String) profile.get("nickname");
			String email = (String) kakaoAccount.get("email");
			KakaoMemberCreateDto kakaoCreateDto = 
					KakaoMemberCreateDto.builder()
						.memberId(memberId)
						.password("1234")  // 기본 패스워드 설정
						.name(name)
						.email(email)
						.build();
			
			// DB에 회원 정보 저장
			int result = memberService.insertMember(kakaoCreateDto);
			member = (MemberDetails) memberService.loadUserByUsername(memberId);
		}
		
		return member;
	}
}