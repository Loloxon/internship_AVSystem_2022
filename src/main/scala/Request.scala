class Request(val from: Int, val to: Int, var waiting: Boolean = true, var waitingTime: Int = 0){
  def furtherWaiting(): Unit ={
    waitingTime+=1
  }
  def realise(): Unit ={
    waiting = false
  }
}
