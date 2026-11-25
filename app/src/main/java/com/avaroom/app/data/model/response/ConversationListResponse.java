package com.avaroom.app.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Model phản hồi danh sách cuộc trò chuyện kèm phân trang (GET /api/chat/conversations)
 */
public class ConversationListResponse implements Serializable {

    @SerializedName("conversations")
    private List<ConversationResponse> conversations;

    @SerializedName("pagination")
    private Pagination pagination;

    public List<ConversationResponse> getConversations() {
        return conversations;
    }

    public void setConversations(List<ConversationResponse> conversations) {
        this.conversations = conversations;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public static class Pagination implements Serializable {

        @SerializedName("page")
        private int page;

        @SerializedName("limit")
        private int limit;

        @SerializedName("total")
        private int total;

        @SerializedName("total_pages")
        private int totalPages;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
    }
}
