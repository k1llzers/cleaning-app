package com.ukma.cleaning.order;

import com.ukma.cleaning.address.AddressRepository;
import com.ukma.cleaning.commercialProposal.CommercialProposalRepository;
import com.ukma.cleaning.order.dto.*;
import com.ukma.cleaning.review.ReviewDto;
import com.ukma.cleaning.user.UserRepository;
import com.ukma.cleaning.utils.exceptions.CantChangeEntityException;
import com.ukma.cleaning.utils.mappers.AddressMapper;
import com.ukma.cleaning.utils.mappers.OrderMapper;
import com.ukma.cleaning.utils.mappers.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
        entity.setClient(userRepository.findById(order.getClientId()).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + order.getClientId())
        ));
        entity.setCommercialProposals(order.getProposals().entrySet().stream()
                .collect(Collectors.toMap(
                        x -> commercialProposalRepository.findById(x.getKey()).get(),
                        Map.Entry::getValue)));
        if (order.getAddress().getId() == null) order.getAddress().setId(0l);
        entity.setAddress(addressRepository.findById(order.getAddress().getId())
                .orElseGet(() -> addressRepository.save(addressMapper.toEntity(order.getAddress()))));
        return orderMapper.toUserDto(orderRepository.save(entity));
    }

    @Override
    public OrderForUserDto updateOrderForUser(OrderForUserDto order) {
        OrderEntity entity = orderRepository.findById(order.getId()).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + order.getId())
        );
        if (entity.getStatus().ordinal() >= Status.PREPARING.ordinal()) {
            throw new CantChangeEntityException("You can`t change order when status is Preparing");
        }
        orderMapper.updateFields(entity, order);
        return orderMapper.toUserDto(orderRepository.save(entity));
    }

    @Override
    public OrderForAdminDto updateOrderForAdmin(OrderForAdminDto order) {
        OrderEntity entity = orderRepository.findById(order.getId()).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + order.getId())
        );
        orderMapper.updateFields(entity, order);
        return orderMapper.toAdminDto(orderRepository.save(entity));
    }

    @Override
    public OrderForUserDto updateReview(ReviewDto review) {
        OrderEntity entity = orderRepository.findById(review.getOrderId()).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + review.getOrderId())
        );
        if (entity.getStatus() != Status.DONE) {
            throw new CantChangeEntityException("You can`t add review when status isn`t Done`");
        }
        entity.setReview(reviewMapper.toEntity(review));
        return orderMapper.toUserDto(orderRepository.save(entity));
    }

    @Override
    public OrderForUserDto getOrderByIdForUser(Long id) {
        OrderEntity entity = orderRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + id)
        );
        return orderMapper.toUserDto(entity);
    }

    @Override
    public OrderForAdminDto getOrderByIdForAdmin(Long id) {
        OrderEntity entity = orderRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + id)
        );
        return orderMapper.toAdminDto(entity);
    }

    @Override
    public Boolean cancelOrderById(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("Can`t find order by id: " + orderId)
        );
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
        Page<OrderEntity> orders = orderRepository.findOrdersByExecutorsId(id, pageable);
        int totalPages = orders.getTotalPages();
        return new OrderPageDto(pageable.getPageNumber(), totalPages, orderMapper.toListDto(orders.stream().toList()));
    }

    @Override
    public OrderPageDto findOrdersByUserId(Long id, Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findOrdersByClientId(id, pageable);
        int totalPages = orders.getTotalPages();
        return new OrderPageDto(pageable.getPageNumber(), totalPages, orderMapper.toListDto(orders.stream().toList()));
    }
}
