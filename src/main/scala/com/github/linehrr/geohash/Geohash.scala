package com.github.linehrr.geohash
import scala.math._

class Geohash(lat: Double, long: Double, precision: Int) extends Serializable {
  private lazy val hash1: Long = hashGen(90, 90)
  private lazy val hash2: Long =
      hashGen(90 + 180/pow(2, precision + 1), 90 + 180/pow(2, precision + 1))

  private def hashGen(Latoffset: Double, Lngoffset: Double): Long = {
    var adj_lat = lat + Latoffset
    var adj_lng = long/2 + Lngoffset
    var hash: Long = 0L

    (1 to precision).foreach(
      p => {
        if (adj_lat >= 180 / pow(2, p)) {
          hash = hash + (1 << 2*(p - 1))
          adj_lat = adj_lat % (180 / pow(2, p))
        }

        if (adj_lng >= 180 / pow(2, p*cos(toRadians(floor(abs(lat)*precision/32))))) {
          hash = hash + (1 << (2*p - 1))
          adj_lng = adj_lng % (180 / pow(2, p))
        }
      }
    )

    hash
  }

  def get_hash1(): Long = hash1
  def get_hash2(): Long = hash2
}

object Geohash {
  def apply(lat: Double, long: Double, precision: Int = 12): Geohash = new Geohash(lat, long, precision)
}