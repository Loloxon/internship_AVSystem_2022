import scalafx.Includes._
import scalafx.animation._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.geometry.Pos._
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle

import scala.sys.exit


object GUI extends JFXApp{
  val scene_size_x = 800
  val scene_size_y = 700

  var chosenElevator = "[none]"
  var currentFloor = "[none]"
  var destinationFloor = "[none]"
  stage = new JFXApp.PrimaryStage{
    title = "Elevator system"
    val width1 = 1200
    val height1 = 800
    scene = new Scene(width1,height1){
      val menuBar = new MenuBar
      val fileMenu = new Menu("File")
      val exitItem = new MenuItem("Exit")
      fileMenu.onAction = (e:ActionEvent) => {exit(77)}
      fileMenu.items = List(exitItem)
      menuBar.menus = List(fileMenu)

      val settings = new VBox
      settings.alignment = TopCenter
      settings.prefWidth = 200
      settings.spacing = 5

      val currentSettingsLabel = new Label(chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor)

      val elevatorSelectionLabel = new Label("Which elevator you want to use?")
      val elevatorSelection = new ListView(List.tabulate(16)(i => "Elevator no. "+(i+1)))
      elevatorSelection.selectionModel.apply.selectedItems.onChange {
        chosenElevator = elevatorSelection.selectionModel.apply.getSelectedItems.get(0)
        currentSettingsLabel.text = chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor
      }
      elevatorSelection.maxWidth = 100
      settings.children += elevatorSelectionLabel
      settings.children += elevatorSelection

      val floorSelectionLabel = new Label("On which floor are you?")
      val floorSelection = new ComboBox(List.tabulate(10)(i => if(i>=1) (i+1)+". floor" else "Ground floor"))
      floorSelection.onAction = (e:ActionEvent) => {
        currentFloor = floorSelection.value.apply()
        currentSettingsLabel.text = chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor
      }
      settings.children += floorSelectionLabel
      settings.children += floorSelection

      val destinationSelectionLabel = new Label("To which floor are you going?")
      val destinationSelection = new ComboBox(List.tabulate(10)(i => if(i>=1) (i+1)+". floor" else "Ground floor"))
      destinationSelection.onAction = (e:ActionEvent) => {
        destinationFloor = destinationSelection.value.apply()
        currentSettingsLabel.text = chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor
      }
      settings.children += destinationSelectionLabel
      settings.children += destinationSelection

      settings.children += currentSettingsLabel

      val summonElevator = new Button("Summon elevator")
      settings.children += summonElevator



      val controls = new HBox
      controls.alignment = TopCenter
      controls.prefHeight = 100

      val controlButton = new Button("Pause")
      controlButton.prefHeight = 50
      controlButton.prefWidth = 100
      controls.children += controlButton

      val timeScroll = new ScrollBar()
      timeScroll.minHeight = 50
      timeScroll.prefWidth = 800
      controls.children += timeScroll

      val exitButton = new Button("Exit")
      exitButton.onAction = (e:ActionEvent) => {exit(77)}
      exitButton.prefHeight = 50
      exitButton.prefWidth = 100
      controls.children += exitButton



      val allStats = new VBox
      allStats.alignment = TopCenter
      allStats.prefWidth = 200

      val getInfo = new Button("Generate stats")
      getInfo.prefWidth = 200
      allStats.children += getInfo

      val stats = new TextArea()
      stats.prefHeight = 665
      stats.editable = false
      allStats.children +=stats


      val rootPane = new BorderPane
      rootPane.top = menuBar
      rootPane.center = elevatorSimulation(controlButton, summonElevator, getInfo, timeScroll, stats)
      rootPane.right = allStats
      rootPane.left = settings
      rootPane.bottom = controls
      root = rootPane

//      content = List(root)
    }
  }

  def elevatorSimulation( controlButton: Button, summonElevator: Button,
                         getInfo: Button, timeScroll: ScrollBar, stats: TextArea): GridPane = {
    val grid = new GridPane
    val rectangle_size = 8
    val width = scene_size_x/rectangle_size
    val height = scene_size_y/rectangle_size
    val rectangles = Array.fill(
      scene_size_x / rectangle_size, scene_size_y / rectangle_size)(Rectangle(rectangle_size, rectangle_size, LightBlue))
    for (i <- 0 until width) {
      for(j <- 0 until height) {
        grid.add(rectangles(i)(j), i, j)
      }
    }
    refill()
    def refill(): Unit = {
      for (i <- 0 until width) {
        for (j <- 0 until height) {
          if (j % 8 == 5) {
            rectangles(i)(j).fill = Black
          }
          if (i < width - 1 && (i % 6 >= 3 && i % 6 <= 5) && (j % 8 >= 2 && j % 8 <= 5)) {
            rectangles(i)(j).fill = Grey
          }
          if (i < width - 1 && (i % 6 >= 3 && i % 6 <= 5) && (j % 8 < 2 || j % 8 > 5)) {
            rectangles(i)(j).fill = LightGrey
          }
          if (j < 2) {
            rectangles(i)(j).fill = Blue
          }
          if (j == height - 1) {
            rectangles(i)(j).fill = LightGreen
          }
        }
      }
    }

    val es = new ElevatorSystem

    getInfo.onAction = (e:ActionEvent)=>{
      stats.setText(es.checkout())
    }

    summonElevator.onAction = (e:ActionEvent)=>{
      if(chosenElevator=="[none]"||currentFloor=="[none]"||destinationFloor=="[none]"){}
      else {
        var id, cf, df = -1
        if(chosenElevator.length==14) {
          id = chosenElevator(chosenElevator.length - 1).toInt-48
        }
        else {
           id = (chosenElevator(chosenElevator.length - 2).toInt-48) * 10 + chosenElevator(chosenElevator.length - 1).toInt-48
        }
        id-=1
        
        if(currentFloor=="Ground floor") {
           cf = 0
        }
        else if(currentFloor.length==8){
           cf = currentFloor(0).toInt-48
        }
        else{
           cf = (currentFloor(0).toInt-48)*10+currentFloor(1).toInt-48
        }

        if(destinationFloor=="Ground floor") {
           df = 0
        }
        else if(destinationFloor.length==8){
           df = destinationFloor(0).toInt-48
        }
        else{
           df = (destinationFloor(0).toInt-48)*10+destinationFloor(1).toInt-48
        }
        es.summonElevator(id, cf, df)
      }
    }

//    val board = new PeopleBoard
//    board.initialize(width,height)

    timeScroll.min = -7
    timeScroll.max = 0
    timeScroll.value = -2

    var lastTime = 0.0
    var total_delay = math.exp(timeScroll.value.apply)
    var updateDelay = total_delay
    var running = true
    val timer:AnimationTimer = AnimationTimer(t => {
      if(lastTime>0){
        val delta = (t-lastTime)/1e9
        updateDelay-=delta
        if(updateDelay<0) {
          es.iteration()
          refill()
          for(e <- es.elevators){
            val bottomCenterX = 6*e.ID+4
            val bottomCenterY = (height-2)-(e.currentFloor*8).toInt
//            println(bottomCenterX)
//            println(bottomCenterY)
            for(i <- bottomCenterX-1 to bottomCenterX+1){
              for(j<- bottomCenterY-3 to bottomCenterY){
                rectangles(i)(j).fill = Red
              }
            }
          }
//          for (i <- 0 until width) {
//            for (j <- 0 until height) {
//              rectangles(i)(j).fill = board.cells(i)(j).color
//            }
//          }
          updateDelay=total_delay
        }
      }
      timeScroll.value.onChange {
        total_delay = math.exp(timeScroll.value.apply)
      }
      lastTime = t
    })
    timer.start()
    controlButton.onAction = (e: ActionEvent) => {
      if(running) {
        timer.stop
        controlButton.text = "Resume"
        running = false
      }
      else{
        timer.start
        controlButton.text = "Pause"
        running = true
      }
    }
    grid
  }
}