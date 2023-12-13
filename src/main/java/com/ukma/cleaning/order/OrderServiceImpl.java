package com.ukma.cleaning.order;

import com.ukma.cleaning.address.AddressRepository;
import com.ukma.cleaning.commercialProposal.CommercialProposalRepository;
import com.ukma.cleaning.order.dto.*;
import com.ukma.cleaning.review.ReviewDto;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.exceptions.AccessDeniedException;
import com.ukma.cleaning.utils.exceptions.CantChangeEntityException;
import com.ukma.cleaning.utils.exceptions.NoSuchEntityException;
import com.ukma.cleaning.utils.mappers.AddressMapper;
import com.ukma.cleaning.utils.mappers.OrderMapper;
import com.ukma.cleaning.utils.mappers.ReviewMapper;
import com.ukma.cleaning.utils.security.SecurityContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CommercialProposalRepository commercialProposalRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public OrderForUserDto createOrder(OrderCreationDto order) {
        OrderEntity entity = orderMapper.toEntity(order);
        log.trace("Creating new order with id = {}", entity.getId());
        entity.setClient(SecurityContextAccessor.getAuthenticatedUser());
        entity.setCommercialProposals(order.getProposals().entrySet().stream()
                .collect(Collectors.toMap(
                        x -> commercialProposalRepository.findById(x.getKey()).get(),
                        Map.Entry::getValue)));
        if (order.getAddress().getId() == null) {
            order.getAddress().setId(0l);
            log.trace("Creating new address for order id = {}", entity.getId());
        }
        entity.setAddress(addressRepository.findById(order.getAddress().getId())
                .orElseGet(() -> addressRepository.save(addressMapper.toEntity(order.getAddress()))));
        OrderForUserDto orderDto = orderMapper.toUserDto(orderRepository.save(entity));
        log.info("Order with id = {} successfully created", entity.getId());
        return orderDto;
    }

    @Override
    public OrderForUserDto updateOrderForUser(OrderForUserDto order) {
        OrderEntity entity = orderRepository.findById(order.getId()).orElseThrow(() ->
                new NoSuchEntityException("Can`t find order by id: " + order.getId())
        );
        if (!Objects.equals(entity.getClient().getId(), SecurityContextAccessor.getAuthenticatedUserId())) {
            log.warn("User id = {} trying to update order of user id = {}", SecurityContextAccessor.getAuthenticatedUserId(), entity.getClient().getId());
            throw new AccessDeniedException("Access denied");
        }
        if (entity.getStatus().ordinal() >= Status.PREPARING.ordinal()) {
            log.warn("User id = {} trying to change order status", SecurityContextAccessor.getAuthenticatedUserId());
            throw new CantChangeEntityException("You can`t change order when status is Preparing");
        }
        orderMapper.updateFields(entity, order);
        OrderForUserDto orderDto = orderMapper.toUserDto(orderRepository.save(entity));
        log.debug("Data of order id = {} successfully updated", order.getId());
        return orderDto;
    }

    @Override
    public OrderForAdminDto updateOrderForAdmin(OrderForAdminDto order) {
        OrderEntity entity = orderRepository.findById(order.getId()).orElseThrow(() ->
                new NoSuchEntityException("Can`t find order by id: " + order.getId())
        );
        orderMapper.updateFields(entity, order);
        OrderForAdminDto orderDto = orderMapper.toAdminDto(orderRepository.save(entity));
        log.debug("Data of order id = {} was updated by administrator id = {}", orderDto.getId(), SecurityContextAccessor.getAuthenticatedUserId());
        return orderDto;
    }

    @Override
    public OrderForUserDto updateReview(ReviewDto review) {
        OrderEntity entity = orderRepository.findById(review.getOrderId()).orElseThrow(() ->
                new NoSuchEntityException("Can`t find order by id: " + review.getOrderId())
        );
        if (!Objects.equals(entity.getClient().getId(), SecurityContextAccessor.getAuthenticatedUserId())) {
            log.warn("User id = {} trying to update order of user id = {}", SecurityContextAccessor.getAuthenticatedUserId(), entity.getClient().getId());
            throw new AccessDeniedException("Access denied");
        }
        if (entity.getStatus() != Status.DONE) {
            throw new CantChangeEntityException("You can`t add review when status isn`t Done`");
        }
        entity.setReview(reviewMapper.toEntity(review));
        OrderForUserDto orderDto = orderMapper.toUserDto(orderRepository.save(entity));
        log.info("User id = {} added review on order id = {}", SecurityContextAccessor.getAuthenticatedUserId(), orderDto.getId());
        return orderDto;
    }

    @Override
    public OrderForUserDto getOrderByIdForUser(Long id) {
        OrderEntity entity = orderRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityException("Can`t find order by id: " + id)
        );
        if (!Objects.equals(entity.getClient().getId(), SecurityContextAccessor.getAuthenticatedUserId())) {
            log.warn("User id = {} trying to get order of user id = {}", SecurityContextAccessor.getAuthenticatedUserId(), entity.getClient().getId());
            throw new AccessDeniedException("Access denied");
        }
        return orderMapper.toUserDto(entity);
    }

    @Override
    public OrderForAdminDto getOrderByIdForAdmin(Long id) {
        OrderEntity entity = orderRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityException("Can`t find order by id: " + id)
        );
        return orderMapper.toAdminDto(entity);
    }

    @Override
    public Boolean cancelOrderById(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElseThrow(() ->
                new NoSuchEntityException("Can`t find order by id: " + orderId)
        );
        if (!Objects.equals(orderId, SecurityContextAccessor.getAuthenticatedUserId())) {
            log.warn("User id = {} trying to cancel order of user id = {}", SecurityContextAccessor.getAuthenticatedUserId(), entity.getClient().getId());
            throw new AccessDeniedException("Access denied");
        }
        log.info("Order id = {} was cancelled", orderId);
        entity.setStatus(Status.CANCELLED);
        return true;
    }

    @Override
    public List<OrderListDto> getAllOrders() {
        return orderMapper.toListDto(orderRepository.findAll());
    }

    @Override
    public List<OrderListDto> getAllOrdersByStatus(Status status) {
        return orderMapper.toListDto(orderRepository.findAllByStatus(status));
    }

    @Override
    public List<OrderListDto> getAllOrdersByUserId(Long id) {
        return orderMapper.toListDto(orderRepository.findAllByStatusNotAndClientIdIs(Status.CANCELLED, id));
    }


    @Override
    public OrderPageDto findOrdersByPage(Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAll(pageable);
        int totalPages = orders.getTotalPages();
        return new OrderPageDto(pageable.getPageNumber(), totalPages, orderMapper.toListDto(orders.stream().toList()));
    }

    @Override
    public OrderPageDto findOrdersByStatusAndPage(Status status, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAllByStatus(status, pageable);
        int totalPages = orders.getTotalPages();
        return new OrderPageDto(pageable.getPageNumber(), totalPages, orderMapper.toListDto(orders.stream().toList()));
    }

    @Override
    public OrderPageDto findOrdersByExecutorId(Long id, Pageable pageable) {
        if (SecurityContextAccessor.getAuthorities().contains("ROLE_EMPLOYEE") && !Objects.equals(SecurityContextAccessor.getAuthenticatedUserId(), id)) {
            log.warn("Employee id = {} trying to get orders of employee id = {}", SecurityContextAccessor.getAuthenticatedUserId(), id);
            throw new AccessDeniedException("Access denied");
        }
        Page<OrderEntity> orders = orderRepository.findOrdersByExecutorsId(id, pageable);
        int totalPages = orders.getTotalPages();
        return new OrderPageDto(pageable.getPageNumber(), totalPages, orderMapper.toListDto(orders.stream().toList()));
    }

    @Override
    public OrderPageDto findOrdersByUserId(Long id, Pageable pageable) {
        if (SecurityContextAccessor.getAuthorities().contains("ROLE_USER") && !Objects.equals(SecurityContextAccessor.getAuthenticatedUserId(), id)) {
            log.warn("User id = {} trying to get orders of user id = {}", SecurityContextAccessor.getAuthenticatedUserId(), id);
            throw new AccessDeniedException("Access denied");
        }
        Page<OrderEntity> orders = orderRepository.findOrdersByClientId(id, pageable);
        int totalPages = orders.getTotalPages();
        return new OrderPageDto(pageable.getPageNumber(), totalPages, orderMapper.toListDto(orders.stream().toList()));
    }
}
