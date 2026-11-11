package com.myproject.twitter.security;

import com.myproject.twitter.entity.User;
import com.myproject.twitter.exception.UserForbiddenException;
import com.myproject.twitter.exception.UserNotFoundException;
import com.myproject.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public Optional<CustomUserDetails> getCurrentUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    /*
        public Optional<CustomUserDetails> getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        return auth.getPrincipal() instanceof CustomUserDetails details
                ? Optional.of(details)
                : Optional.empty();
    }
     */

    public CustomUserDetails getRequiredCurrentUserDetails() {

        return getCurrentUserDetails()
                .orElseThrow(() -> new UserNotFoundException("Oturum bulunamadı. Lütfen önce giriş yapın."));
    }

    public User getCurrentUserReference() {

        CustomUserDetails userDetails = getRequiredCurrentUserDetails();
        return userRepository.getReferenceById(userDetails.getId());
    }

    public User getCurrentUserEntity() {

        CustomUserDetails userDetails = getRequiredCurrentUserDetails();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + userDetails.getId()));
    }

    public void verifyOwnership(Long resourceOwnerId) {

        CustomUserDetails currentUser = getRequiredCurrentUserDetails();
        if (!currentUser.getId().equals(resourceOwnerId)) {
            throw new UserForbiddenException("Bu işlem için yetkiniz yok!");
        }
    }

    public void verifyCommentDeletionAuth(Long commentOwnerId, Long tweetOwnerId) {

        CustomUserDetails currentUser = getRequiredCurrentUserDetails();
        Long currentUserId = currentUser.getId();

        if (!currentUserId.equals(commentOwnerId) && !currentUserId.equals(tweetOwnerId)) {
            throw new UserForbiddenException("Bu yorumu silme yetkiniz bulunmamaktadır!");
        }
    }


}

