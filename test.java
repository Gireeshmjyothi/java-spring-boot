@Test
void testUpdateStatusInvalidOrderRef() {
    String invalidOrderRefNum = "invalid_order_123";
    String status = "Active";

    when(orderDao.updateOrderStatus(invalidOrderRefNum, status)).thenThrow(new RuntimeException("Order not found"));

    Exception exception = assertThrows(RuntimeException.class, 
        () -> orderService.updateOrderStatus(invalidOrderRefNum, status));

    verify(orderDao, times(1)).updateOrderStatus(invalidOrderRefNum, status);

    assertEquals("Order not found", exception.getMessage());
}

@Test
void testUpdateStatusNullStatus() {
    String orderRefNum = "order_12341243";
    String status = null;

    when(orderDao.updateOrderStatus(orderRefNum, status)).thenThrow(new IllegalArgumentException("Status cannot be null"));

    Exception exception = assertThrows(IllegalArgumentException.class, 
        () -> orderService.updateOrderStatus(orderRefNum, status));

    verify(orderDao, times(1)).updateOrderStatus(orderRefNum, status);

    assertEquals("Status cannot be null", exception.getMessage());
}

@Test
void testUpdateStatusUnsupportedStatus() {
    String orderRefNum = "order_12341243";
    String status = "InvalidStatus";

    when(orderDao.updateOrderStatus(orderRefNum, status)).thenThrow(new IllegalArgumentException("Unsupported status"));

    Exception exception = assertThrows(IllegalArgumentException.class, 
        () -> orderService.updateOrderStatus(orderRefNum, status));

    verify(orderDao, times(1)).updateOrderStatus(orderRefNum, status);

    assertEquals("Unsupported status", exception.getMessage());
}


@Test
void testUpdateStatusEmptyOrderRef() {
    String emptyOrderRefNum = "";
    String status = "Active";

    when(orderDao.updateOrderStatus(emptyOrderRefNum, status)).thenThrow(new IllegalArgumentException("Order reference number cannot be empty"));

    Exception exception = assertThrows(IllegalArgumentException.class, 
        () -> orderService.updateOrderStatus(emptyOrderRefNum, status));

    verify(orderDao, times(1)).updateOrderStatus(emptyOrderRefNum, status);

    assertEquals("Order reference number cannot be empty", exception.getMessage());
}
@Test
void testUpdateStatusNullOrderRef() {
    String orderRefNum = null;
    String status = "Active";

    when(orderDao.updateOrderStatus(orderRefNum, status)).thenThrow(new IllegalArgumentException("Order reference number cannot be null"));

    Exception exception = assertThrows(IllegalArgumentException.class, 
        () -> orderService.updateOrderStatus(orderRefNum, status));

    verify(orderDao, times(1)).updateOrderStatus(orderRefNum, status);

    assertEquals("Order reference number cannot be null", exception.getMessage());
}
@Test
void testUpdateStatusToCompleted() {
    String orderRefNum = "order_12341243";
    String status = "COMPLETED";

    when(orderDao.updateOrderStatus(orderRefNum, status)).thenAnswer(invocationOnMock -> {
        OrderDto orderDto = new OrderDto();
        orderDto.setStatus(OrderStatus.valueOf("COMPLETED"));
        orderDto.setOrderHash("==updatedhash123");
        orderDto.setCurrencyCode("USD");
        orderDto.setOrderAmount(999.99);
        return orderDto;
    });

    TransactionResponse<String> updateResponse = orderService.updateOrderStatus(orderRefNum, status);

    verify(orderDao, times(1)).updateOrderStatus(orderRefNum, status);

    assertNotNull(updateResponse);
    assertEquals(1, updateResponse.getStatus());
    assertEquals("Order Status updated Successfully.", updateResponse.getData().get(0));
}

