class Request(val from: Int, val to: Int, var waiting: Boolean = true, var waitingTime: Int = 0){
  def furtherWaiting(): Unit ={
//    waitingTime+=1
  }

  def goingUp(): Boolean={from<to}

  override def toString: String = {
    from + " -> " + to + "; " + waitingTime
  }

//  def realise(): Unit ={
//    waiting = false
//  }
}
