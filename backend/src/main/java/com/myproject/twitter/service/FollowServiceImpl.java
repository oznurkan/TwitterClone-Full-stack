package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.FollowStatusResponseDto;
import com.myproject.twitter.dto.response.FollowUserResponseDto;
import com.myproject.twitter.entity.Follow;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.repository.FollowRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.security.CustomUserDetails;
import com.myproject.twitter.util.mapper.FollowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    private final UserService userService;

    private final AuthUtils authUtils;

    private final FollowMapper followMapper;

    @Override
    @Transactional
    public FollowStatusResponseDto follow(String targetNickName) {

        User currentUser = authUtils.getCurrentUserReference();
        User targetUser = userService.getEntityByNickName(targetNickName);

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new IllegalStateException("Kendinizi takip edemezsiniz.");
        }

        boolean alreadyFollowing = followRepository.existsByFollowerIdAndFollowingId(
                currentUser.getId(), targetUser.getId()
        );

        if (!alreadyFollowing) {
            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(targetUser);
            followRepository.save(follow);
        }

        Long followerCount = followRepository.countFollowersByUserId(targetUser.getId());
        return new FollowStatusResponseDto(true, followerCount);
    }

    @Override
    @Transactional
    public FollowStatusResponseDto unfollow(String targetNickName) {

        User currentUser = authUtils.getCurrentUserReference();
        User targetUser = userService.getEntityByNickName(targetNickName);

        followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), targetUser.getId())
                .ifPresent(followRepository::delete);

        Long followerCount = followRepository.countFollowersByUserId(targetUser.getId());
        return new FollowStatusResponseDto(false, followerCount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponseDto> getFollowings(String targetNickName) {

        User targetUser = userService.getEntityByNickName(targetNickName);
        List<User> followings = followRepository.findFollowingsByUserId(targetUser.getId());

        String currentUserEmail = authUtils.getCurrentUserReference().getEmail();
        Set<Long> myFollowingIds = getMyFollowingIds(currentUserEmail);

        return followings.stream()
                .map(user -> followMapper.toFollowUserResponseDto(
                        user,
                        myFollowingIds.contains(user.getId())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowUserResponseDto> getFollowers(String targetNickName) {

        User targetUser = userService.getEntityByNickName(targetNickName);
        List<User> followers = followRepository.findFollowersByUserId(targetUser.getId());

        String currentUserEmail = authUtils.getCurrentUserReference().getEmail();
        Set<Long> myFollowingIds = getMyFollowingIds(currentUserEmail);

        return followers.stream()
                .map(user -> followMapper.toFollowUserResponseDto(
                        user,
                        myFollowingIds.contains(user.getId())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {

        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countFollowers(Long userId) {

        return followRepository.countFollowersByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countFollowings(Long userId) {

        return followRepository.countFollowingsByUserId(userId);
    }

    private Set<Long> getMyFollowingIds(String currentUserEmail) {

        if (currentUserEmail == null || currentUserEmail.isBlank()) {
            return Collections.emptySet();
        }
        User currentUser = userService.getEntityByEmail(currentUserEmail);
        return followRepository.findFollowingsByUserId(currentUser.getId()).stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

}


/*


package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.FollowStatusResponseDto;
import com.myproject.twitter.dto.response.FollowUserResponseDto;
import com.myproject.twitter.entity.Follow;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.repository.FollowRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.util.mapper.FollowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final AuthUtils authUtils;
    private final FollowMapper followMapper;

    @Override
    @Transactional
    public FollowStatusResponseDto follow(String targetNickName) {
        User currentUser = authUtils.getCurrentUserReference();
        User targetUser = userService.getEntityByNickName(targetNickName);

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new IllegalStateException("Kendinizi takip edemezsiniz.");
        }

        boolean alreadyFollowing = followRepository.existsByFollowerIdAndFollowingId(
                currentUser.getId(), targetUser.getId()
        );

        if (!alreadyFollowing) {
            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(targetUser);
            followRepository.save(follow);
        }

        Long followerCount = followRepository.countFollowersByUserId(targetUser.getId());
        return new FollowStatusResponseDto(true, followerCount);
    }

    @Override
    @Transactional
    public FollowStatusResponseDto unfollow(String targetNickName) {
        User currentUser = authUtils.getCurrentUserReference();
        User targetUser = userService.getEntityByNickName(targetNickName);

        followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), targetUser.getId())
                .ifPresent(followRepository::delete);

        Long followerCount = followRepository.countFollowersByUserId(targetUser.getId());
        return new FollowStatusResponseDto(false, followerCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowUserResponseDto> getFollowings(String targetNickName, Pageable pageable) {
        User targetUser = userService.getEntityByNickName(targetNickName);
        Page<User> followingsPage = followRepository.findFollowingsByUserId(targetUser.getId(), pageable);

        Set<Long> myFollowingIds = getMyFollowingIds();

        return followingsPage.map(user -> followMapper.toFollowUserResponseDto(
                user,
                myFollowingIds.contains(user.getId())
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowUserResponseDto> getFollowers(String targetNickName, Pageable pageable) {
        User targetUser = userService.getEntityByNickName(targetNickName);
        Page<User> followersPage = followRepository.findFollowersByUserId(targetUser.getId(), pageable);

        Set<Long> myFollowingIds = getMyFollowingIds();

        return followersPage.map(user -> followMapper.toFollowUserResponseDto(
                user,
                myFollowingIds.contains(user.getId())
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countFollowers(Long userId) {
        return followRepository.countFollowersByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countFollowings(Long userId) {
        return followRepository.countFollowingsByUserId(userId);
    }

    private Set<Long> getMyFollowingIds() {
        try {
            User currentUser = authUtils.getCurrentUserReference();
            return new HashSet<>(followRepository.findFollowingIdsByUserId(currentUser.getId()));
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }
}
 */