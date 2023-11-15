package org.aptech.metube.personal.service;

import org.aptech.metube.personal.controller.request.AccountTypeCreateRequest;
import org.aptech.metube.personal.controller.request.AccountTypeUpdateRequest;
import org.aptech.metube.personal.exception.DuplicateException;

public interface AccountTypeService {
    Long save(AccountTypeCreateRequest request) throws DuplicateException;
    Long update(AccountTypeUpdateRequest request);
    Long disableAccountType(Long id);
}
