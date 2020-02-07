package org.dabs.back.service.impl;

import org.dabs.back.entity.Role;
import org.dabs.back.repository.RoleRepository;
import org.dabs.back.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
	private RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role getDefaultRole() {
		return this.roleRepository.findOneByAuthority(DEFAULT_ROLE);
	}

	@Override
	public Role getRoleByAuthority(String authority) {
		return this.roleRepository.findOneByAuthority(authority);
	}
}
