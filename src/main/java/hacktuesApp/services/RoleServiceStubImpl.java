package hacktuesApp.services;

import hacktuesApp.models.Role;
import hacktuesApp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceStubImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceStubImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRole(String name) {
        return roleRepository.findByName(name);
    }
}
