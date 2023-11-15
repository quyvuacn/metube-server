package org.aptech.metube.personal.service.impl;

import com.netflix.discovery.converters.Auto;
import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.request.OrderCreateRequest;
import org.aptech.metube.personal.controller.response.UserAccountTypeResponse;
import org.aptech.metube.personal.entity.AccountType;
import org.aptech.metube.personal.entity.OrderDetails;
import org.aptech.metube.personal.entity.User;
import org.aptech.metube.personal.entity.UserAccountType;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.repository.AccountTypeRepository;
import org.aptech.metube.personal.repository.OrderDetailsRepository;
import org.aptech.metube.personal.repository.UserAccountTypeRepository;
import org.aptech.metube.personal.repository.UserRepository;
import org.aptech.metube.personal.service.OrderDetailsService;
import org.aptech.metube.personal.service.UserAccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    OrderDetailsRepository orderDetailsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAccountTypeService userAccountTypeService;
    @Autowired
    UserAccountTypeRepository userAccountTypeRepository;
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Override
    @Transactional
    public Long saveOrder(OrderCreateRequest request) throws NotFoundException {
        try {
            OrderDetails orderDetails = OrderDetails.builder()
                    .address(request.getAddress())
                    .payer(request.getPayer())
                    .payee(request.getPayee())
                    .unit(request.getUnit())
                    .amount(request.getAmount())
                    .status(request.getStatus())
                    .userId(request.getUserId())
                    .accountTypeId(request.getAccountTypeId())
                    .createDate(request.getCreateDate())
                    .build();
            User user = userRepository.findById(orderDetails.getUserId())
                    .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));

            List<UserAccountTypeResponse> userAccountType = userAccountTypeService.getUserAccountTypesByUserId(user.getId());

            UserAccountTypeResponse maxExpireDateUser = userAccountType.stream()
                            .max((u1, u2) -> u1.getExpireDate().compareTo(u2.getExpireDate()))
                                    .orElse(null);
            LocalDateTime maxExpireDate = maxExpireDateUser.getExpireDate();
            if (maxExpireDate == null || maxExpireDate.isBefore(LocalDateTime.now())){
                UserAccountType userAccountType1 = UserAccountType.builder()
                        .typeId(request.getAccountTypeId())
                        .userId(request.getUserId())
                        .expireDate(LocalDateTime.now())
                        .build();
                userAccountTypeRepository.save(userAccountType1);
            } else if (maxExpireDate.isAfter(LocalDateTime.now())){

                AccountType accountType = accountTypeRepository.findById(request.getAccountTypeId())
                        .orElseThrow(() -> new NotFoundException(Translator.toLocale("account-type.not.found")));

                UserAccountType userAccountType1 = UserAccountType.builder()
                        .typeId(request.getAccountTypeId())
                        .userId(request.getUserId())
                        .expireDate(maxExpireDate.plusDays(accountType.getActiveTime()))
                        .build();
                userAccountTypeRepository.save(userAccountType1);
            }

            orderDetailsRepository.save(orderDetails);
            return orderDetails.getId();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
