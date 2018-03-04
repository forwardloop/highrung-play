package highrung.db

import java.sql.Timestamp
import org.joda.time.DateTime
import slick.jdbc.JdbcProfile

trait ColumnMapper {

  protected val driver: JdbcProfile
  import driver.api._

  implicit val timestampMapper = MappedColumnType.base[DateTime, Timestamp](
    dt => new Timestamp(dt.getMillis),
    ts => new DateTime(ts.getTime)
  )
}
