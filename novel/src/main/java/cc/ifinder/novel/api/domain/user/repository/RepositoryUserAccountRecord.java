package cc.ifinder.novel.api.domain.user.repository;

import cc.ifinder.novel.api.domain.user.User;
import cc.ifinder.novel.api.domain.user.UserAccountRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RepositoryUserAccountRecord extends JpaRepository<UserAccountRecord, Long> {

   /**
    * 查询流水
    */
   Page<UserAccountRecord> findByUserAndType(User user, Integer type, Pageable pageable);

   /**
    * 查询当天的账户增加总额
    * @param user 用户
    * @param type 账户类型
    * @param begin 开始时间
    * @param end 结束时间
    */
   @Query("select sum(record.amountChange) from UserAccountRecord  record " +
           "where  record.amountChange>0 " +
           "and record.user = ?1 " +
           "and record.type = ?2 " +
           "and record.sourceType = ?3 " +
           "and record.createTime between ?4 and ?5")
   Integer getSumByTypeAndUserAndSourceTypeAndTime(User user, Integer type, Integer sourceType, Date begin, Date end);

}
