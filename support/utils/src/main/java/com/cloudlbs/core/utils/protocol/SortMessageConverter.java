package com.cloudlbs.core.utils.protocol;

import com.cloudlbs.core.utils.search.Sort;
import com.cloudlbs.core.utils.search.Sort.Direction;
import com.cloudlbs.platform.protocol.SearchProto.SortMessage;
import com.cloudlbs.platform.protocol.SearchProto.SortMessage.Order;


public class SortMessageConverter extends ProtobufMessageConverter<SortMessage, Sort>{

    @Override
    public Sort fromMessage(SortMessage message) {
        Sort sort = new Sort(message.getField(), getDirection(message.getOrder()));
        return sort;
    }

    @Override
    public SortMessage toMessage(Sort obj) {
        SortMessage.Builder builder = SortMessage.newBuilder();
        builder.setField(obj.getField());
        builder.setOrder(getProtoSortOrder(obj.getDirection()));
        return builder.build();
   }

    static Order getProtoSortOrder(Direction direction) {
        if (direction.equals(Direction.Descending)) {
            return Order.Descending;
        } else {
            return Order.Ascending;
        }
    }

    static Direction getDirection(Order order) {
        if (order.equals(Order.Descending)) {
            return Direction.Descending;
        } else {
            return Direction.Ascending;
        }
    }

}
