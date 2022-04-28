import java.lang.Math.round
import scala.collection.mutable.ListBuffer

class ElevatorSystem(val elevators: ListBuffer[Elevator] = ListBuffer()){
  for (i<-0 until 16){
    elevators+=new Elevator(i)
  }
//  elevators = ListBuffer(for (i<-0 until 16){new Elevator(i)})

  def summonElevator(id: Int, from: Int, to: Int): Unit ={
    println(id)
    println(from)
    println(to)
    elevators(id).addRequest(new Request(from, to))
  }

  def iteration(): Unit ={
    for(e<-elevators){
      e.move()
      e.pickUp()
      e.growImpatience()
      e.calculateDestination()
    }
  }

  def checkout(): String ={
    var ans = ""
    for(e <- elevators){
      if(e.destinationFloor==e.currentFloor)
        ans+="Elevator "+e.ID+":\nNear "+round(e.currentFloor)+" floor, not moving\n"
      else
        ans+="Elevator "+e.ID+":\nNear "+round(e.currentFloor)+" floor, going to "+e.destinationFloor+"\n"
    }
    ans
  }
}
