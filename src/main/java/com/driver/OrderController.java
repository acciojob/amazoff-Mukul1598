// OrderController.java
package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId) {
        orderService.addPartner(partnerId);
        return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId) {
        orderService.createOrderPartnerPair(orderId, partnerId);
        return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId) {
        DeliveryPartner deliveryPartner = orderService.getPartnerById(partnerId);
        return new ResponseEntity<>(deliveryPartner, HttpStatus.OK);
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId) {
        Integer orderCount = orderService.getOrderCountByPartnerId(partnerId);
        return new ResponseEntity<>(orderCount, HttpStatus.OK);
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId) {
        List<String> orders = orderService.getOrdersByPartnerId(partnerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders() {
        List<String> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders() {
        Integer countOfOrders = orderService.getCountOfUnassignedOrders();
        return new ResponseEntity<>(countOfOrders, HttpStatus.OK);
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time, @PathVariable String partnerId) {
        Integer countOfOrders = orderService.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
        return new ResponseEntity<>(countOfOrders, HttpStatus.OK);
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId) {
        String time = orderService.getLastDeliveryTimeByPartnerId(partnerId);
        return new ResponseEntity<>(time, HttpStatus.OK);
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId) {
        orderService.deletePartner(partnerId);
        return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.OK);
    }
}
