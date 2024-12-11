package com.epay.transaction.service;

import com.epay.transaction.dao.OrderDao;
import com.epay.transaction.dto.OrderDto;
import com.epay.transaction.model.response.TransactionResponse;
import com.epay.transaction.util.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;


    @Test
    void testUpdateStatus(){
        
        String orderRefNum = "order_12341243";
        String status = "Active";
        when(orderDao.updateOrderStatus(orderRefNum, status)).thenAnswer(invocationOnMock -> {
            OrderDto orderDto =new OrderDto();
            orderDto.setStatus(OrderStatus.valueOf("PAID"));
            orderDto.setOrderHash("==asfascjsdi424");
            orderDto.setCurrencyCode("INR");
            orderDto.setOrderAmount(535.4);
            return orderDto;
        });

        TransactionResponse<String> updateResponse = orderService.updateOrderStatus(orderRefNum, status);

        verify(orderDao, times(1)).updateOrderStatus(anyString(), anyString());

        assertNotNull(updateResponse);
        assertEquals(1, updateResponse.getStatus());
        assertEquals("Order Status updated Successfully.", updateResponse.getData().get(0));

    }
}
