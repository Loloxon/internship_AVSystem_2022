object Floors{
  object FloorsNo extends Enumeration {
    type FloorsNo = Value
    val ground, first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, none = Value
  }
  import FloorsNo._
  def toString(no: FloorsNo): String ={
    no match {
      case ground => "Ground floor"
      case first => "1st floor"
      case second => "2nd floor"
      case third => "3rd floor"
      case fourth => "4th floor"
      case fifth => "5th floor"
      case sixth => "6th floor"
      case seventh => "7th floor"
      case eighth => "8th floor"
      case ninth => "9th floor"
      case tenth => "10th floor"
      case none => "[none]"
    }
  }
}