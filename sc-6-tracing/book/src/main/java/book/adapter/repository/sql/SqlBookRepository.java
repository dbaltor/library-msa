package book.adapter.repository.sql;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.jdbc.repository.query.Query;

public interface SqlBookRepository extends PagingAndSortingRepository<BookDb, Long> {
    //@Query(value = "select * from BOOK_DB where name = :name")
    public List<BookDb> findByName(@Param("name") String name);

    //@Query(value = "select * from BOOK_DB where reader_id = :readerId")
    public List<BookDb> findByReaderId(@Param("readerId") long readerId);
}
