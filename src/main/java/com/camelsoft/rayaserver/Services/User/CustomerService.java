package com.camelsoft.rayaserver.Services.User;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.DTO.UserShortDto;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Auth.RoleRepository;
import com.camelsoft.rayaserver.Repository.Tools.PersonalInformationRepository;
import com.camelsoft.rayaserver.Repository.User.UserRepository;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonalInformationRepository personalInformationRepository;
    @Autowired
    private RoleRepository roleRepository;


    public List<UserShortDto> getAllUsersWithoutPagination(Boolean active, String name, RoleEnum role, Boolean verified) {
        try {
            Role userRole = roleRepository.findByRole(role);
            List<users> user = null;
            if (name == null && active == null && verified == null)
                user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(userRole, true, false);
            else if (name == null && active == null && verified != null)
                user = this.userRepository.findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(userRole, true, false, verified);
            else if (name != null && active == null && verified == null)
                user = this.userRepository.findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseOrderByTimestmpDesc(userRole, "%" + name + "%", false, "%DELETED%");
            else if (name != null && active == null && verified != null)
                user = this.userRepository.findAllByRoleAndEmailLikeIgnoreCaseAndDeletedAndUsernameNotLikeIgnoreCaseAndVerifiedOrderByTimestmpDesc(userRole, "%" + name + "%", false, "%DELETED%", verified);
            else if (name == null && active != null && verified == null)
                user = this.userRepository.findByRoleAndActiveAndDeletedOrderByTimestmpDesc(userRole, active, false);
            else if (name == null && active != null && verified != null)
                user = this.userRepository.findByRoleAndActiveAndDeletedAndVerifiedOrderByTimestmpDesc(userRole, active, false, verified);
            else if (name != null && active != null && verified == null)
                user = this.userRepository.findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedOrderByTimestmpDesc(userRole, active, "%" + name + "%", false);
            else if (name != null && active != null && verified != null)
                user = this.userRepository.findAllByRoleAndActiveAndEmailLikeIgnoreCaseAndDeletedAndVerifiedOrderByTimestmpDesc(userRole, active, "%" + name + "%", false, verified);
            return  user.stream()
                    .map(UserShortDto::mapToUserShortDTO)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No data found"));
        }

    }

}
