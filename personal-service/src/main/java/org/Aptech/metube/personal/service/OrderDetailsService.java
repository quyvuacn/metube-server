package org.aptech.metube.personal.service;

import org.aptech.metube.personal.controller.request.OrderCreateRequest;
import org.aptech.metube.personal.exception.NotFoundException;

public interface OrderDetailsService {
    Long saveOrder(OrderCreateRequest request) throws NotFoundException;
}
