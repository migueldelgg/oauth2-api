package github.migueldelgg.springsecurity.controller.dto;

import java.util.List;

public record FeedDTO(List<FeedItemDTO> feedItems, int page,
                      int pageSize,
                      int totalPages,
                      long totalElements) {
}
