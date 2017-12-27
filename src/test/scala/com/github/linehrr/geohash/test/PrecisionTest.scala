package com.github.linehrr.geohash.test

import com.github.linehrr.geohash.Geohash
import org.scalatest.FunSuite

class PrecisionTest extends FunSuite {
  test("Low precision = 1 should match distance within ~15000 km")  {
    val geohash1 = Geohash(0, 0, 1)
    val geohash2 = Geohash(89, 179, 1)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("Low precision = 2 should match distince within ~7500 km, great circle: 5386km") {
    val geohash1 = Geohash(0, 0, 2)
    val geohash2 = Geohash(30, 40, 2)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("Edge cases with cutoff boundaries, +/- on longitude") {
    val geohash1 = Geohash(-.2, 0, 8)
    val geohash2 = Geohash(.2, 0, 8)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("Edge cases with cutoff boundaries, +/- on latitude, great circle: 44.48km") {
    val geohash1 = Geohash(.2, 0, 8)
    val geohash2 = Geohash(-.2, 0, 8)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("Edge cases with cutoff boundaries, +/- on latitude and longitude, great circle: 62.9km") {
    val geohash1 = Geohash(.2, .2, 8)
    val geohash2 = Geohash(-.2, -.2, 8)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("North pole adjustment, great circle distance = 22.24km") {
    val geohash1 = Geohash(89.9, 0, 8)
    val geohash2 = Geohash(89.9, 179, 8)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("South pole adjustment, great circle distance = 57.56km") {
    val geohash1 = Geohash(-89.9, 0, 8)
    val geohash2 = Geohash(-89.9, 30, 8)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("Near South pole stretch adjustment, great circle distance = 112.7km") {
    val geohash1 = Geohash(-80, 10, 6)
    val geohash2 = Geohash(-81, 11, 6)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("High precision test, great circle distance = .001m") {
    val geohash1 = Geohash(-80, 10, 30)
    val geohash2 = Geohash(-80.00000001, 10.00000001, 30)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("High precision test(close to north pole), great circle distance = 1m") {
    val geohash1 = Geohash(-80, 10, 23)
    val geohash2 = Geohash(-80.00001, 10.00001, 23)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("High precision test2(close to tropical), great circle distance = 1m") {
    val geohash1 = Geohash(0, 0, 23)
    val geohash2 = Geohash(0.00001, .00001, 23)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("High precision test3(close to tropical, edge case), great circle distance = 3.1m") {
    val geohash1 = Geohash(-0.00001, -0.00001, 23)
    val geohash2 = Geohash(0.00001, .00001, 23)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("High precision test4(exact equal), great circle distance = 0m") {
    val geohash1 = Geohash(0.00001, 0.00001, 32)
    val geohash2 = Geohash(0.00001, 0.00001, 32)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }

  test("North pole test, 90N") {
    val geohash1 = Geohash(90, 0, 32)
    val geohash2 = Geohash(90, 179.9, 32)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }
}
