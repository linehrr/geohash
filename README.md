# geohash
yet another better geohash algorithm implemented in Scala

## Usage:
```scala
   // constructor is lazy, only if get_hash() is called, otherwise won't evaluate
   // get_hash() caches the result, next call won't recompute 
   val geohash1 = Geohash(0, 0, 23)
   val geohash2 = Geohash(0.00001, .00001, 23)
   assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
```

## encoding:
refer to https://en.wikipedia.org/wiki/Geohash  
algorithm is similar but with some modifications to address some drawbacks  
hash is returned as Long, and the encoding is as following:  
```
MSB 1 0 1 ...1     0     1     1 LSB
             |     |     |     |
             lng2  lat2  lng1  lat1
```
latitudes are encoded in even bits, starting from LSB  
longitudes are encoded in odd bits, starting from LSB  
e.g.  
lat1 means: lat ~ (0, 90]  
lng1 means: lng ~ (0, 180]  
lat2 means: lat ~ (0, 45]  
lng2 means: lng ~ (90, 180]  
...
## Precision:
precision is in [1, 32]. precision is increasing in 2**n pattern.

## North/South pole adjustment:
standard geohash from Gustavo Niemeyer suffers from North/South pole distortion. Meaning on 90N/0W and 90N179.9W would be considered as 2 far places.  
this algorithm adjusts that so that they will be considered very close.  
```scala
test("North pole test, 90N") {
    val geohash1 = Geohash(90, 0, 32)
    val geohash2 = Geohash(90, 179.9, 32)

    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }
```

## Edge case adjustment:
standard geohash also suffers from edge case where 1N/0W and 1S/0W would be considered as very far places.  
this algorithm also tries to fix that by duo-hashing.  
e.g.
```scala
 test("High precision test3(close to tropical, edge case), great circle distance = 3.1m") {
    // 2 points, very close to each other, but lat and lng both crossed the edge
    val geohash1 = Geohash(-0.00001, -0.00001, 23)
    val geohash2 = Geohash(0.00001, .00001, 23)

    // duo hashing, guarantees at least one of the hash(get_hash1() or get_hash2()) will be equal
    assert(geohash1.get_hash1() == geohash2.get_hash1() ||
      geohash1.get_hash2() == geohash2.get_hash2())
  }
```
real usage case could be using 2 group bys and reduce/dedup after:
```scala
  // spark pseudo code
  val groups = df.groupby(geohash.get_hash1())
    unionall
  df.groupby(geohash.get_hash2())
  .reduceByKey(<distinct on NK>)
```

## Precision and max error:
TBD(please refer to test cases to get some sense)

