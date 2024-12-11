@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;


    @Test
    void testUpdateStatus(){

        String orderRefNum = "order_12341243";
        String status = "Active";
        when(orderRepository.findByOrderRefNumber("orderRefNumber_414424")).thenReturn(Optional.of(buildOrder()));
        when(orderRepository.save(any(Order.class))).thenReturn(buildOrder());
        when(orderDao.updateOrderStatus(orderRefNum, status)).thenAnswer(invocationOnMock -> {
            OrderDto orderDto = invocationOnMock.getArgument(0);
            orderDto.setStatus(OrderStatus.valueOf("PAID"));
            orderDto.setOrderHash("==asfascjsdi424");
            orderDto.setCurrencyCode("INR");
            orderDto.setOrderAmount(535.4);
            return orderDto;
        });



        TransactionResponse<String> updateResponse = orderService.updateOrderStatus(orderRefNum, status);

//        verify(orderDao, times(1)).updateOrderStatus(anyString(), anyString());

        assertNotNull(updateResponse);
        assertEquals(1, updateResponse.getStatus());
        assertEquals("", updateResponse.getData().get(0));

    }


    private Order buildOrder(){
        Order order = new Order();

        order.setOrderAmount(BigDecimal.valueOf(1000.12));
        order.setId(UUID.randomUUID());
        order.setExpiry(24L);
        order.setCurrencyCode("INR");
        order.setOtherDetails("Other details");
        order.setPaymentMode("UPI");
        order.setCustomerId("CustId_352353");
        order.setStatus(OrderStatus.valueOf("PAID"));
        order.setOrderHash("4345805==dnddcv");
        order.setReturnUrl("https://order.abi.com/");
        order.setSbiOrderRefNumber("sbi_order_number");

        return order;
    }
}

public TransactionResponse<String> updateOrderStatus(String orderRefNumber, String status) {
        orderDao.updateOrderStatus(orderRefNumber, status);
        return TransactionResponse.<String>builder().data(List.of("Order Status updated Successfully.")).status(1).count(1L).build();
    }

public OrderDto updateOrderStatus(String orderRefNumber, String status) {
        //TODO : Need to check status and add some more validation
        System.out.println("inside order dao.....");
        Order order = orderRepository.findByOrderRefNumber(orderRefNumber).orElseThrow(() -> new TransactionException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Order")));
        order.setStatus(OrderStatus.valueOf(status));
        Order saveOrder = orderRepository.save(order);
        return objectMapper.convertValue(saveOrder, OrderDto.class);
    }

class java.lang.String cannot be cast to class com.epay.transaction.dto.OrderDto (java.lang.String is in module java.base of loader 'bootstrap'; com.epay.transaction.dto.OrderDto is in unnamed module of loader 'app')
java.lang.ClassCastException: class java.lang.String cannot be cast to class com.epay.transaction.dto.OrderDto (java.lang.String is in module java.base of loader 'bootstrap'; com.epay.transaction.dto.OrderDto is in unnamed module of loader 'app')
	at com.epay.transaction.service.OrderServiceTest.lambda$testUpdateStatus$0(OrderServiceTest.java:49)
	at org.mockito.internal.stubbing.StubbedInvocationMatcher.answer(StubbedInvocationMatcher.java:42)
	at org.mockito.internal.handler.MockHandlerImpl.handle(MockHandlerImpl.java:103)
	at org.mockito.internal.handler.NullResultGuardian.handle(NullResultGuardian.java:29)
	at org.mockito.internal.handler.InvocationNotifierHandler.handle(InvocationNotifierHandler.java:34)
	at org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.doIntercept(MockMethodInterceptor.java:82)
	at org.mockito.internal.creation.bytebuddy.MockMethodAdvice.handle(MockMethodAdvice.java:134)
	at com.epay.transaction.dao.OrderDao.updateOrderStatus(OrderDao.java:58)
	at com.epay.transaction.service.OrderService.updateOrderStatus(OrderService.java:79)
	at com.epay.transaction.service.OrderServiceTest.testUpdateStatus(OrderServiceTest.java:59)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)


OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
