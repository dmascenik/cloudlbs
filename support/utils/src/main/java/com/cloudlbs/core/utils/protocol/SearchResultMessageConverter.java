package com.cloudlbs.core.utils.protocol;

import java.util.ArrayList;
import java.util.List;

import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.Message;

public class SearchResultMessageConverter<M extends Message, T> extends
		AbstractMessageConverter<SearchResultMessage, SearchResult<T>> {

	private GeneratedMessage.GeneratedExtension<SearchResultMessage, List<M>> itemExtension;
	private MessageConverter<M, T> converter;

	public SearchResultMessageConverter(
			GeneratedExtension<SearchResultMessage, List<M>> items,
			MessageConverter<M, T> converter) {
		super();
		this.itemExtension = items;
		this.converter = converter;
	}

	@Override
	public SearchResult<T> fromMessage(SearchResultMessage message) {
		List<T> gList = new ArrayList<T>();
		List<M> protoList = (List<M>) message.getExtension(itemExtension);
		for (M proto : protoList) {
			gList.add(converter.fromMessage(proto));
		}
		QueryMessageConverter queryConverter = new QueryMessageConverter();
		SearchResult<T> searchResult = new SearchResult<T>(gList,
				queryConverter.fromMessage(message.getQuery()),
				message.getQueryTime(), message.getTotalResults());
		return searchResult;
	}

	@Override
	public SearchResultMessage toMessage(SearchResult<T> obj) {
		SearchResultMessage.Builder builder = SearchResultMessage.newBuilder();
		if (obj.getQuery() != null) {
			QueryMessageConverter queryConverter = new QueryMessageConverter();
			builder.setQuery(queryConverter.toMessage(obj.getQuery()));
		}
		builder.setTotalResults(obj.getTotalResults());
		List<T> values = obj.getValues();
		for (T value : values) {
			builder.addExtension(itemExtension, converter.toMessage(value));
		}
		return builder.build();
	}

}
