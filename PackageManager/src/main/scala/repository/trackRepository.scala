package repository

import entities.TrackEntity
import org.springframework.data.repository.CrudRepository

/**
  * Created by arthurn on 26.06.17.
  */
trait trackRepository extends CrudRepository[TrackEntity, Long]{}
