package pizzaco.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pizzaco.domain.models.service.UserRoleServiceModel;
import pizzaco.domain.models.service.UserServiceModel;
import pizzaco.domain.models.view.AllUsersViewModel;
import pizzaco.domain.models.view.LogViewModel;
import pizzaco.domain.models.view.UserViewModel;
import pizzaco.service.LogService;
import pizzaco.service.UserService;

import java.util.stream.Collectors;

@Controller
public class AdminController extends BaseController {

    private final UserService userService;
    private final LogService logService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, LogService logService, ModelMapper modelMapper) {
        this.userService = userService;
        this.logService = logService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/profiles/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allProfiles() {
        return super.view("users/all-profiles-user", "profiles",
                this.userService.extractAllUsersOrderedAlphabetically()
                .stream()
                .map(u -> {
                    AllUsersViewModel allUsersViewModel = this.modelMapper.map(u, AllUsersViewModel.class);
                    allUsersViewModel.setFullName(String.format("%s %s", u.getFirstName(), u.getLastName()));

                    return allUsersViewModel;
                })
                .collect(Collectors.toList()));
    }

    @GetMapping("/profiles/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView profile(@PathVariable(name = "id") String id) {
        UserServiceModel userServiceModel = this.userService.extractUserById(id);
        UserViewModel userViewModel = this.modelMapper.map(userServiceModel, UserViewModel.class);
        userViewModel.setRoles(userServiceModel.getAuthorities().stream().map(UserRoleServiceModel::getAuthority).collect(Collectors.toList()));

        return super.view("users/details-user", "userViewModel", userViewModel);
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView logs() {
        return super.view(
                "logs", "logViewModel",
                this.logService.getLogsOrderedByDate()
                        .stream()
                        .map(log -> this.modelMapper.map(log, LogViewModel.class))
                        .collect(Collectors.toList())
        );
    }
}
