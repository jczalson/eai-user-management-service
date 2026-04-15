package com.eai.user.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO<T> implements Serializable {

  private List<?> content;

  @JsonProperty("isLast")
  private boolean last;

  private long totalElements;
  private long totalPages;
  private int number;
  private int currentPage;

  @JsonProperty("isFirst")
  private boolean first;

  private boolean hasPrevious;

  private boolean hasNext;

  private int numberOfElements;

  @JsonProperty("isEmpty")
  private boolean empty;
  public PageDTO(Page<T> page) {
    this.content = page.getContent();
    setTotalElements(page.getTotalElements());
    setTotalPages(page.getTotalPages());
    setNumber(page.getNumber());
    setEmpty(page.isEmpty());
    setFirst(page.isFirst());
    setLast(page.isLast());
    setNumberOfElements(page.getNumberOfElements());
    setCurrentPage(page.getNumber() + 1);
    setHasNext(page.hasNext());
    setHasPrevious(page.hasPrevious());
  }

  
}
