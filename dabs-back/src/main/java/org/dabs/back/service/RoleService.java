package org.dabs.back.service;

import org.dabs.back.entity.Role;

public interface RoleService {
	
	String DEFAULT_ROLE = "ROLE_USER";
	
    Role getDefaultRole();

    Role getRoleByAuthority(String authority);
}
