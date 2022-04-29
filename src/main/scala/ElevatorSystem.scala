import scala.collection.mutable.ListBuffer
import scala.math.round

class ElevatorSystem(val elevators: ListBuffer[Elevator] = ListBuffer()){
  for (i<-0 until 16){
    elevators+=new Elevator(i)
  }
//  elevators = ListBuffer(for (i<-0 until 16){new Elevator(i)})

  def summonElevator(id: Int, from: Int, to: Int): Unit ={
//    println(id)
//    println(from)
//    println(to)
    elevators(id).addRequest(new Request(from, to))
//    Thread.sleep(10)
    elevators(id).calculateDestination()
  }

  def iteration(): Unit ={
    for(e<-elevators){
      e.move()
      e.pickUp()
      e.growImpatience()
    }
  }

  def checkout(): String ={
    var ans = ""
    for(e <- elevators) {
      if (e.reloadingTime != e.reloadingProgress) {
        ans += "```Elevator " + e.ID + " capacity: " + e.passengers.length + "/" + e.capacity +
          ":\nOn " + round(e.currentFloor) + " floor, reloading ["
        for(i<-0 until e.reloadingTime by 5){
          if(i<=e.reloadingProgress)
            ans += "X"
          else
            ans += "  "
        }
        ans += "]\n"
      }
      else {
        if (e.destinationFloor == e.currentFloor)
          ans += "```Elevator " + e.ID + " capacity: " + e.passengers.length + "/" + e.capacity +
            ":\nOn " + round(e.currentFloor) + " floor, not moving\n"
        else
          ans += "```Elevator " + e.ID + " capacity: " + e.passengers.length + "/" + e.capacity +
            ":\nNear " + round(e.currentFloor) + " floor, going to " + e.destinationFloor + "\n"
      }
    }
    ans
  }
}
