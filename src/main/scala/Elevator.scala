import scala.collection.mutable.ListBuffer

class Elevator(val ID: Int, var moveDirection: Int = 0, var currentFloor: Double = 0,
               var destinationFloor: Int = 0, var requestsSubmitted: ListBuffer[Request] = ListBuffer(),
               var passengers: ListBuffer[Request] = ListBuffer(),
               val reloadingTime: Int = 5, var reloadingProgress: Int = 5){

  def addRequest(r: Request): Unit ={
    requestsSubmitted.append(r)
  }
  def pickUp(): Unit ={
    if(currentFloor%1==0) {
      val currentFloorInt = currentFloor.toInt
      for (r <- requestsSubmitted.indices) {
        if (requestsSubmitted(r).from == currentFloorInt) {
          passengers.append(requestsSubmitted(r))
          requestsSubmitted.remove(r)
          reloadingProgress=0
        }
      }
      for (r <- passengers.indices) {
        if (passengers(r).to == currentFloorInt) {
          passengers.remove(r)
          reloadingProgress=0
        }
      }
    }
  }
  def move(): Unit ={
    if(reloadingProgress==reloadingTime){
      if (currentFloor < destinationFloor) {
        currentFloor += 0.125
      }
      else if (currentFloor > destinationFloor) {
        currentFloor -= 0.125
      }
    }
    else{
      reloadingProgress += 1
    }
  }
  def calculateDestination(): Unit ={
    if(passengers.nonEmpty) {
      destinationFloor = passengers.head.to
    }else if(requestsSubmitted.nonEmpty){
      destinationFloor = requestsSubmitted.head.from
    }
    println("ID: ", ID, ", ", destinationFloor)
  }
  def growImpatience(): Unit ={
    for(r<-requestsSubmitted)
      r.furtherWaiting()
    for(p<-passengers)
      p.furtherWaiting()
  }
}
