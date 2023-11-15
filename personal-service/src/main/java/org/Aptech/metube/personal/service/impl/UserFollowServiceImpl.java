package org.aptech.metube.personal.service.impl;

import org.aptech.metube.personal.entity.User;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.entity.UserFollow;
import org.aptech.metube.personal.exception.RequestValidException;
import org.aptech.metube.personal.repository.UserFollowRepository;
import org.aptech.metube.personal.repository.UserRepository;
import org.aptech.metube.personal.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    UserFollowRepository userFollowRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Long save(UserFollow userFollow) throws NotFoundException {
        if (userFollow.getUserId() == null || userFollow.getFollowerId() == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        UserFollow userFollow1 = userFollowRepository.findByUserIdAndFollowerId(userFollow.getUserId(), userFollow.getFollowerId());
        if (!Objects.isNull(userFollow1)){
            throw new RequestValidException(Translator.toLocale("user.followed"));
        }
        userFollowRepository.save(userFollow);
        return userFollow.getId();
    }

    @Override
    public Boolean checkFollowedUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        List<Long> listUserId = userFollowRepository.findFollowerIdByUserId(id);
        if (listUserId.contains(currentUser.getId())){
            return true;
        } else {
            return false;
        }
    }
}
