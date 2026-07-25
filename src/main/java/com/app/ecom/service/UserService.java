package com.app.ecom.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


     // 查询所有用户，并把 User 转换成 UserResponse
    public List<UserResponse>fetchAllUser(){
        return userRepository.findAll()
                    .stream()
                    .map(this::mapToUserResponse)
                    .collect(Collectors.toList());
    }
        // 创建用户
        public void addUser(UserRequest userRequest) {
            User user = new User();

            updateUserFromRequest(user, userRequest);

            userRepository.save(user);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {

    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setPhone(userRequest.getPhone());

    if (userRequest.getAddress() != null) {
        Address address = new Address();

        address.setStreet(userRequest.getAddress().getStreet());
        address.setCity(userRequest.getAddress().getCity());
        address.setState(userRequest.getAddress().getState());
        address.setCountry(userRequest.getAddress().getCountry());
        address.setZipCode(userRequest.getAddress().getZipCode());

        user.setAddress(address);
    }
}
    // 根据 ID 查询一个用户
    public Optional<UserResponse> fetchUser(Long id){
        return userRepository.findById(id)
             .map(this::mapToUserResponse);
    }

    // 更新用户
    public boolean updateUser(Long id, UserRequest updatedUserRequest) {
        return userRepository.findById(id)
                .map(existingUser -> {

                    updateUserFromRequest(
                            existingUser,
                            updatedUserRequest
                    );

                    userRepository.save(existingUser);

                    return true;
                })
                .orElse(false);
    }

// 把 User 实体转换为 UserResponse
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();

            addressDTO.setStreet(
                    user.getAddress().getStreet()
            );

            addressDTO.setCity(
                    user.getAddress().getCity()
            );

            addressDTO.setState(
                    user.getAddress().getState()
            );

            addressDTO.setCountry(
                    user.getAddress().getCountry()
            );

            addressDTO.setZipCode(
                    user.getAddress().getZipCode()
            );

            response.setAddress(addressDTO);
        }

        return response;
    }

}