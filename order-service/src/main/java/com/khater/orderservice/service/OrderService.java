package com.khater.orderservice.service;

import com.khater.orderservice.dto.InventoryResponse;
import com.khater.orderservice.dto.OrderLineItemsDto;
import com.khater.orderservice.dto.OrderRequest;
import com.khater.orderservice.model.Order;
import com.khater.orderservice.model.OrderLineItems;
import com.khater.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] isInStacks = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory" ,
                        uriBuilder -> uriBuilder.queryParam("skuCode" , skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        if(isInStacks != null && isInStacks.length > 0 && Arrays.stream(isInStacks).allMatch(InventoryResponse::getInStock)){
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("We do not have from this product anymore. try again later!");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
