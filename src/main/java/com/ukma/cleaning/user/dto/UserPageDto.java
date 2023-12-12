package com.ukma.cleaning.user.dto;

import com.ukma.cleaning.order.dto.OrderListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserPageDto {
    // starts from 0
    private long currPage;
    private long numOfPages;
    private List<UserListDto> orderList;
}
