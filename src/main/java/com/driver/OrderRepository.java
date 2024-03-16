// OrderRepository.java
package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.partnerToOrderMap = new HashMap<>();
        this.orderToPartnerMap = new HashMap<>();
    }

    public void saveOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId) {
        if (!partnerMap.containsKey(partnerId)) {
            partnerMap.put(partnerId, new DeliveryPartner(partnerId));
        }
    }

    public void saveOrderPartnerMap(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            partnerToOrderMap.computeIfAbsent(partnerId, k -> new HashSet<>()).add(orderId);
            orderToPartnerMap.put(orderId, partnerId);
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
        }
    }

    public Order findOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId) {
        HashSet<String> orders = partnerToOrderMap.get(partnerId);
        return orders != null ? orders.size() : 0;
    }

    public List<String> findOrdersByPartnerId(String partnerId) {
        return List.copyOf(partnerToOrderMap.getOrDefault(partnerId, new HashSet<>()));
    }

    public List<String> findAllOrders() {
        return List.copyOf(orderMap.keySet());
    }

    public void deletePartner(String partnerId) {
        partnerMap.remove(partnerId);
        partnerToOrderMap.remove(partnerId);
        orderToPartnerMap.values().removeIf(pid -> pid.equals(partnerId));
    }

    public void deleteOrder(String orderId) {
        orderMap.remove(orderId);
        String partnerId = orderToPartnerMap.remove(orderId);
        if (partnerId != null && partnerMap.containsKey(partnerId)) {
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders() - 1);
            partnerToOrderMap.get(partnerId).remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders() {
        return (int) orderToPartnerMap.values().stream().filter(pid -> pid == null).count();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {
        int time = convertDeliveryTimeToMinutes(timeString);
        int count = 0;
        HashSet<String> orders = partnerToOrderMap.get(partnerId);
        if (orders != null) {
            for (String orderId : orders) {
                if (orderMap.containsKey(orderId) && orderMap.get(orderId).getDeliveryTime() > time) {
                    count++;
                }
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        HashSet<String> orders = partnerToOrderMap.get(partnerId);
        if (orders != null) {
            int maxTime = Integer.MIN_VALUE;
            for (String orderId : orders) {
                if (orderMap.containsKey(orderId) && orderMap.get(orderId).getDeliveryTime() > maxTime) {
                    maxTime = orderMap.get(orderId).getDeliveryTime();
                }
            }
            if (maxTime != Integer.MIN_VALUE) {
                int hours = maxTime / 60;
                int minutes = maxTime % 60;
                return String.format("%02d:%02d", hours, minutes);
            }
        }
        return null;
    }

    private int convertDeliveryTimeToMinutes(String deliveryTime) {
        String[] timeSplit = deliveryTime.split(":");
        int hours = Integer.parseInt(timeSplit[0]);
        int minutes = Integer.parseInt(timeSplit[1]);
        return hours * 60 + minutes;
    }
}
