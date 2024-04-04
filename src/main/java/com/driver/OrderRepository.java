
package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        if (order != null && order.getId() != null) {
            orderMap.put(order.getId(), order);
        }
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        if (partnerId != null) {
            DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
            partnerMap.put(partnerId, deliveryPartner);
        }
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            if (orderId != null && partnerId != null) {
                orderToPartnerMap.put(orderId, partnerId);

                DeliveryPartner partner = partnerMap.get(partnerId);
                partnerToOrderMap.computeIfAbsent(partnerId, k -> new HashSet<>()).add(orderId);

                partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
            }
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderId != null ? orderMap.get(orderId) : null;
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerId != null ? partnerMap.get(partnerId) : null;
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
//        if (partnerToOrderMap.containsKey(partnerId)) {
//            return partnerToOrderMap.get(partnerId).size();
//        }
//        return 0;
        if (partnerId != null && partnerToOrderMap.containsKey(partnerId)) {
            DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
            return deliveryPartner.getNumberOfOrders();
        }
        return 0;
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        if (partnerId != null) {
            Set<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
            return new ArrayList<>(orders);
        }
        return Collections.emptyList();
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        List<String> ordersList = new ArrayList<>();
        for (Order order : orderMap.values()) {
            ordersList.add(order.getId());
        }
        return ordersList;
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if (partnerId != null && partnerMap.containsKey(partnerId)) {
            partnerMap.remove(partnerId);
            Set<String> assignedOrders = partnerToOrderMap.remove(partnerId);
            if (assignedOrders != null) {
                for (String orderId : assignedOrders) {
                    orderToPartnerMap.remove(orderId);
                }
            }
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if (orderId != null && orderMap.containsKey(orderId)) {
            String partnerId = orderToPartnerMap.remove(orderId);
            if (partnerId != null && partnerToOrderMap.containsKey(partnerId)) {
                partnerToOrderMap.get(partnerId).remove(orderId);
                DeliveryPartner partner = partnerMap.get(partnerId);
                if (partner != null) {
                    partner.setNumberOfOrders(partner.getNumberOfOrders() - 1);
                }
            }
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int unassignedCount = 0;
        for (String orderId : orderMap.keySet()) {
            if (orderToPartnerMap.get(orderId) == null) {
                unassignedCount++;
            }
        }
        return unassignedCount;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int ordersLeft = 0;
        String[] timeParts = timeString.split(":");
        if (timeParts.length == 2) {
            int givenTime = Integer.parseInt(timeParts[0]) * 60 + Integer.parseInt(timeParts[1]);
            Set<String> partnerOrders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
            for (String orderId : partnerOrders) {
                Order order = orderMap.get(orderId);
                if (order != null && order.getDeliveryTime() > givenTime) {
                    ordersLeft++;
                }
            }
        }
        return ordersLeft;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int latestTime = Integer.MIN_VALUE;
        String lastDeliveryTime = "";
        int time = 0;
        Set<String> partnerOrders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
        for (String orderId : partnerOrders) {
            Order order = orderMap.get(orderId);
            if (order != null && order.getDeliveryTime() > latestTime) {
                latestTime = order.getDeliveryTime();
                time = order.getDeliveryTime();
            }
        }
        int hours = time / 60;
        int minutes = time % 60;
        lastDeliveryTime = String.format("%02d:%02d", hours, minutes);
        return lastDeliveryTime;
    }
}
