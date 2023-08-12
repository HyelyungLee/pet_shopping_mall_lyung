package com.shop.app.admin.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.app.admin.repository.AdminRepository;
import com.shop.app.member.entity.Member;


@Transactional(rollbackFor = Exception.class) // 어떤예외가 발생하면 자동으로 롤백
@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;

	@Override
	public List<Member> adminMemberList(Map<String, Object> params) {
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		int offset = (page - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return adminRepository.adminMemberList(rowBounds);
	}

	@Override
	public int insertMember(Member member) {
		return adminRepository.insertMember(member);
	}

	@Override
	public int updateMember(Member member) {
		return adminRepository.updateMember(member);
	}

	@Override
	public int deleteMember(Member member) {
		return adminRepository.deleteMember(member);
	}

	

}
