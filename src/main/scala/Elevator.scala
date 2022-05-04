import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.break

class Elevator(val ID: Int, var moveDirection: Int = 0, var currentFloor: Double = 0,
               var destinationFloor: Int = 0, var requestsSubmitted: ListBuffer[Request] = ListBuffer(),
               var passengers: ListBuffer[Request] = ListBuffer(),
               val reloadingTime: Int = 64, var reloadingProgress: Int = 64, val capacity: Int = 10,
               var goingUp: Boolean = true){

  def addRequest(r: Request): Unit ={
    requestsSubmitted.append(r)
  }
  def showRequests(): Unit ={
    if(requestsSubmitted.nonEmpty)
      println("   R:", requestsSubmitted.length)
    requestsSubmitted.foreach(println)
    if(passengers.nonEmpty)
      println("   P", passengers.length)
    passengers.foreach(println)
  }
  def pickUp(): Unit ={
//    showRequests()
    if(currentFloor%1==0) {
      val tmp: ListBuffer[Int] = ListBuffer()
      val currentFloorInt = math.round(currentFloor)
      for (r <- requestsSubmitted.indices) {
        if (requestsSubmitted(r).from == currentFloorInt && passengers.length<capacity) {
//          println(requestsSubmitted(r).from + " -> " + requestsSubmitted(r).to)
//          println("pick id " + r)
          tmp.append(r)
          passengers.append(requestsSubmitted(r))
          reloadingProgress=0
        }
//        if(passengers.length>=capacity)
//          break
      }
      for(i <- tmp.indices.reverse){
        requestsSubmitted.remove(tmp(i))
        calculateDestination()
      }
      tmp.clear()
      for (p <- passengers.indices) {
        if (passengers(p).to == currentFloorInt) {
//          println(passengers(p).from + " -> " + passengers(p).to)
//          println("put id " + p)
          tmp.append(p)
          reloadingProgress=0
        }
      }
      for(i <- tmp.indices.reverse){
        passengers.remove(tmp(i))
        calculateDestination()
      }
    }
  }
  def move(): Unit ={
    if(reloadingProgress==reloadingTime){
      if (currentFloor < destinationFloor) {
        currentFloor += 0.0625
        goingUp = true
      }
      else if (currentFloor > destinationFloor) {
        currentFloor -= 0.0625
        goingUp = false
      }
    }
    else{
      reloadingProgress += 1
    }
  }
  def calculateDestination(): Unit ={
    var bestDestination = -1
    var distance = 10000.0
    for(p <- passengers){
      if(math.abs(currentFloor-p.to)*16 - p.waitingTime/4<distance){
        distance = math.abs(currentFloor-p.to)*16 - p.waitingTime / 4
        bestDestination = p.to
      }
    }
    if(passengers.length<capacity) {
      for (r <- requestsSubmitted) {
        var directionValue = 1
        if(r.goingUp && goingUp || !r.goingUp && !goingUp){ //w dobrym kierunku
          directionValue = 1
        }
        else{
          directionValue = 2
        }
//        println("dir val: ", directionValue)
        if ((math.abs(currentFloor - r.from) * 16 - r.waitingTime / 4)*directionValue < distance) {
          distance = (math.abs(currentFloor - r.to) * 16 - r.waitingTime / 4) * directionValue
          bestDestination = r.from
        }
      }
    }
    if(distance!= 10000.0)
      destinationFloor = bestDestination
//    if(passengers.nonEmpty) {
//      destinationFloor = passengers.head.to
//    }else if(requestsSubmitted.nonEmpty){
//      destinationFloor = requestsSubmitted.head.from
//    }
//    println("ID: ", ID, ", ", destinationFloor)
  }
  def growImpatience(): Unit ={
    for(r<-requestsSubmitted)
      r.furtherWaiting()
    for(p<-passengers)
      p.furtherWaiting()
  }
}
