package cc.ifinder.novel.utils;
import org.springframework.data.domain.Sort;

/**
 * 排序工具
 */
public class SortUtil {

    //List<User> list = userService.findAll(SortUtil.basicSort(new SortDto("desc", "userName"), new SortDto("id")));

    public static Sort basicSort() {
        return basicSort("desc", "id");
    }

    public static Sort basicSort(String orderType, String orderField) {
        Sort sort = new Sort(Sort.Direction.fromString(orderType), orderField);
        return sort;
    }

    public static Sort basicSort(SortDto... dtos) {
        Sort result = null;
        for(int i=0; i<dtos.length; i++) {
            SortDto dto = dtos[i];
            if(result == null) {
                result = new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }


    public class SortDto {

        //排序方式
        private String orderType;

        //排序字段
        private String orderField;

        public String getOrderField() {
            return orderField;
        }

        public void setOrderField(String orderField) {
            this.orderField = orderField;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public SortDto(String orderType, String orderField) {
            this.orderType = orderType;
            this.orderField = orderField;
        }

        //默认为DESC排序
        public SortDto(String orderField) {
            this.orderField = orderField;
            this.orderType = "desc";
        }
    }
}
