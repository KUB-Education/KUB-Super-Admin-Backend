package education.kub.backend.ce.domain.role.service;

import education.kub.backend.ce.domain.role.mapper.RoleMapper;
import education.kub.backend.ce.domain.role.model.RoleDetailsResponse;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public List<RoleDetailsResponse> getRoles() {
        return roleMapper.toDetailsResponseList(roleRepository.findAll());
    }
}
