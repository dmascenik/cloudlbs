package com.cloudlbs.core.utils.protocol;

import java.util.ArrayList;
import java.util.List;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.Sort;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.platform.protocol.SearchProto.SortMessage;

public class QueryMessageConverter extends
		ProtobufMessageConverter<QueryMessage, Query> {

	@Override
	public Query fromMessage(QueryMessage message) {
		List<SortMessage> sortList = message.getSortList();
		List<Sort> sorts = new ArrayList<Sort>();
		SortMessageConverter sortConverter = new SortMessageConverter();
		for (SortMessage sort : sortList) {
			sorts.add(sortConverter.fromMessage(sort));
		}
		String scopeGuid = null;
		if (message.hasScopeGuid()) {
			scopeGuid = message.getScopeGuid();
		}

		Query query = new Query(message.getQ(), message.getFirstResult(),
				message.getMaxResults(), scopeGuid,
				sorts.toArray(new Sort[sorts.size()]));
		return query;

	}

	@Override
	public QueryMessage toMessage(Query obj) {
		QueryMessage.Builder builder = QueryMessage.newBuilder();
		builder.setQ(obj.getQ());
		builder.setFirstResult((int) obj.getFirstResult());
		builder.setMaxResults((int) (obj.getMaxResults()));
		if (obj.getSorts() != null) {
			if (obj.getSorts().length > 0) {
				SortMessageConverter sortConverter = new SortMessageConverter();
				for (Sort sort : obj.getSorts()) {
					builder.addSort(sortConverter.toMessage(sort));
				}
			}
		}
		if (obj.getScopeGuid() != null) {
			builder.setScopeGuid(obj.getScopeGuid());
		}
		return builder.build();
	}

}
