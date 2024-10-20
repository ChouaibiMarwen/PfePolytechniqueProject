package com.smarty.pfeserver.Services.User;

import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Auth.Role;
import com.smarty.pfeserver.Models.DTO.UserShortDto;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Repository.Auth.RoleRepository;
import com.smarty.pfeserver.Repository.Tools.PersonalInformationRepository;
import com.smarty.pfeserver.Repository.User.UserRepository;
import com.smarty.pfeserver.Tools.Exception.NotFoundException;
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
