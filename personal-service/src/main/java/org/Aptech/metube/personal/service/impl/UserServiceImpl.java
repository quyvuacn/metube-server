package org.aptech.metube.personal.service.impl;

import org.aptech.metube.personal.config.RestTemplateConfig;
import org.aptech.metube.personal.constant.EntityStatusCode;
import org.aptech.metube.personal.controller.request.AdminUpdateUserStatusRequest;
import org.aptech.metube.personal.controller.request.UpdateVideoStatusRequest;
import org.aptech.metube.personal.controller.response.ApiResponse;
import org.aptech.metube.personal.controller.response.UserResponse;
import org.aptech.metube.personal.entity.UserAccountType;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.request.AssignRoleUserRequest;
import org.aptech.metube.personal.controller.request.UserUpdateRequest;
import org.aptech.metube.personal.dto.UserDto;
import org.aptech.metube.personal.entity.Role;
import org.aptech.metube.personal.entity.User;
import org.aptech.metube.personal.entity.UserFollow;
import org.aptech.metube.personal.exception.RequestValidException;
import org.aptech.metube.personal.mapper.UserMapper;
import org.aptech.metube.personal.repository.RoleRepository;
import org.aptech.metube.personal.repository.UserAccountTypeRepository;
import org.aptech.metube.personal.repository.UserFollowRepository;
import org.aptech.metube.personal.repository.UserRepository;
import org.aptech.metube.personal.security.utils.JwtUtils;
import org.aptech.metube.personal.service.UserService;
import org.aptech.metube.personal.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper mapper;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserFollowServiceImpl userFollowService;
    @Autowired
    UserFollowRepository userFollowRepository;
    @Autowired
    UserAccountTypeRepository userAccountTypeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${base.url}")
    private String apiBaseUrl;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public LinkedHashMap<String, Object> findAll(String search, Integer pageNum, Integer pageSize){
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<User> users = userRepository.findAll(
                Specification.where(UserSpecification.hasFullName(search)).or(UserSpecification.hasEmail(search)), paging);

        List<UserResponse> userResponses = users.getContent().stream()
                .map(user -> {
                    UserResponse userResponse = UserResponse.builder()
                            .phone(user.getPhone())
                            .avatar(user.getAvatar())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .address(user.getAddress())
                            .countFollow(user.getCountFollow())
                            .id(user.getId())
                            .fullName(user.getFullName())
                            .statusCode(user.getStatusCode())
                            .roles(user.getRoles())
                            .isVerified(user.getIsVerified())
                            .build();
                    return userResponse;
                })
                .collect(Collectors.toList());

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("pageNum", users.getNumber());
        result.put("pageSize", users.getSize());
        result.put("totalPage", users.getTotalPages());
        result.put("data", userResponses);

        return result;

    }
    @Override
    public UserResponse findById(Long id) throws NotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));
        List<UserAccountType> userAccountType = userAccountTypeRepository.findByUserId(user.getId());
        return UserResponse.builder()
                .username(user.getUsername())
                .countFollow(user.getCountFollow())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .id(user.getId())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .accountTypes(userAccountType)
                .build();
    }
    @Override
    public UserDto findByEmail(String email) {
        return mapper.entityToDto(userRepository.findUserByEmail(email));
    }
    @Override
    public Long updateInfo(UserUpdateRequest request) throws NotFoundException {

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        if (!currentUser.getId().equals(user.getId())){
            throw new RequestValidException(Translator.toLocale("forbidden"));
        }

        user = User.builder()
                .fullName(request.getFullName() != null ? request.getFullName() : user.getFullName())
                .address(request.getAddress() != null ? request.getAddress() : user.getAddress())
                .email(request.getEmail() != null ? request.getEmail() : user.getEmail())
                .password(request.getPassword() != null ? encoder.encode(request.getPassword()) : user.getPassword())
                .phone(request.getPhone() != null ? request.getPhone() : user.getPhone())
                .username(request.getUsername() != null ? request.getUsername() : user.getUsername())
                .avatar(request.getAvatar() != null ? request.getAvatar() : user.getAvatar())
                .build();
        User result = userRepository.save(user);
        return result.getId();
    }
    @Override
    public Long followUser(Long userBeingFollowedId) throws NotFoundException {

        User user = userRepository.findById(userBeingFollowedId)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        List<Long> listFollowerId = userFollowRepository.findFollowerIdByUserId(userBeingFollowedId);
        if (listFollowerId.contains(currentUser.getId())){
            throw new RequestValidException(Translator.toLocale("user.followed"));
        }
        if (!user.getIsVerified() || !user.getStatusCode().equals(EntityStatusCode.ACTIVE)){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        if (userBeingFollowedId.equals(currentUser.getId())){
            throw new RequestValidException(Translator.toLocale("cannot.follow.itself"));
        }

        UserFollow userFollow = UserFollow.builder()
                .userId(userBeingFollowedId)
                .followerId(currentUser.getId())
                .build();
        userFollowService.save(userFollow);
        user.setCountFollow(user.getCountFollow() == null ? 1 : user.getCountFollow() + 1);
        userRepository.save(user);
        return userFollow.getId();
    }

    @Override
    public Long unFollowUser(Long userBeingFollowedId) throws NotFoundException {

        User user = userRepository.findById(userBeingFollowedId)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());

        UserFollow existedUserFollow = userFollowRepository.findByUserIdAndFollowerId(userBeingFollowedId, currentUser.getId());
        if (existedUserFollow == null){
            throw new RequestValidException(Translator.toLocale("user.not.followed"));
        }
        if (userBeingFollowedId.equals(currentUser.getId())){
            throw new RequestValidException(Translator.toLocale("cannot.unfollow.itself"));
        }

        user.setCountFollow(user.getCountFollow()-1);
        userFollowRepository.delete(existedUserFollow);

        return user.getId();
    }
    @Override
    public Long blockUser(AdminUpdateUserStatusRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));
        EntityStatusCode statusCode = request.getStatusCode();
        if (user.getStatusCode().equals(statusCode)){
            throw new RequestValidException(Translator.toLocale("user.already.blocked"));
        }
        user.setStatusCode(statusCode);
        userRepository.save(user);
        UpdateVideoStatusRequest request1 = UpdateVideoStatusRequest.builder()
                .userId(user.getId())
                .statusCode(request.getStatusCode())
                .build();
        updateVideoStatus(request1, httpServletRequest);

        return user.getId();
    }
    private Boolean updateVideoStatus(UpdateVideoStatusRequest videoStatusRequest, HttpServletRequest request){
        String apiUrl = apiBaseUrl + "/api/v1/video/update-status";
        String token = jwtUtils.extractTokenFromRequest(request);

        ParameterizedTypeReference<UpdateVideoStatusRequest> requestType = new ParameterizedTypeReference<UpdateVideoStatusRequest>() {};
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {};
        Map<String, Object> responseEntity = RestTemplateConfig.callApiMethodPUT(
                apiUrl, token, videoStatusRequest, responseType, restTemplate);

        assert responseEntity != null;
        LinkedHashMap<String, Object> responseBody = (LinkedHashMap<String, Object>) responseEntity.get("body");
        if (responseBody != null) {
            Boolean data = (Boolean) responseBody.get("data");
            return data;
        }
        return false;
    }
    @Override
    public Long assignRole(AssignRoleUserRequest request) throws NotFoundException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));
        Set<String> userRoleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Role roleRequest = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new NotFoundException(Translator.toLocale("role.not.found")));
        if (!userRoleNames.contains(roleRequest.getName())) {
            List<Role> currentRole = user.getRoles();
            currentRole.add(roleRequest);
            user.setRoles(currentRole);
            List<User> currentUserList = roleRequest.getUserList();
            currentUserList.add(user);
            roleRepository.save(roleRequest);
        }
        userRepository.save(user);
        return user.getId();
    }
    public UserResponse findByUsername(String username) {

        User user = userRepository.findByUsername(username);
        UserResponse userResponse = UserResponse.builder()
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .id(user.getId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .countFollow(user.getCountFollow())
                .build();

        return userResponse;
    }
}
