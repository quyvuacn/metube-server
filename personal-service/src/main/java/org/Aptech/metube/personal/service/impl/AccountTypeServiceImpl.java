package org.aptech.metube.personal.service.impl;

import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.request.AccountTypeCreateRequest;
import org.aptech.metube.personal.controller.request.AccountTypeUpdateRequest;
import org.aptech.metube.personal.entity.AccountType;
import org.aptech.metube.personal.exception.DuplicateException;
import org.aptech.metube.personal.exception.RequestValidException;
import org.aptech.metube.personal.repository.AccountTypeRepository;
import org.aptech.metube.personal.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Override
    public Long save(AccountTypeCreateRequest request) throws DuplicateException {
        if (accountTypeRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(Translator.toLocale("account-type.name.existed"));
        }
        AccountType accountType = AccountType.builder()
                .name(request.getName())
                .price(request.getPrice())
                .activeTime(request.getActiveTime())
                .build();
        accountTypeRepository.save(accountType);
        return accountType.getId();
    }

    @Override
    public Long update(AccountTypeUpdateRequest request) {
        AccountType accountType = accountTypeRepository.findById(request.getId())
                .orElseThrow(() -> new RequestValidException(Translator.toLocale("account-type.not.found")));
        accountType.setName(request.getName() != null ? request.getName() : accountType.getName());
        accountType.setPrice(request.getPrice() != null ? request.getPrice() : accountType.getPrice());
        accountType.setActiveTime(request.getActiveTime() != null ? request.getActiveTime() : accountType.getActiveTime());
        accountTypeRepository.save(accountType);
        return accountType.getId();
    }

    @Override
    public Long disableAccountType(Long id) {
        return null;
    }


}
