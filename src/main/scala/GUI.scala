import scalafx.Includes._
import scalafx.animation._
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.geometry.Pos._
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle

import Floors._

import scala.sys.exit


object GUI extends JFXApp3{
  override def start() : Unit = {
    val scene_size_x = 800
    val scene_size_y = 700

    var chosenElevator = "[none]"
    var currentFloor = "[none]"
//    var currentFloor = FloorsNo(11)
    var destinationFloor = "[none]"
    stage = new JFXApp3.PrimaryStage {
      title = "Elevator system"
      val width1 = 1400
      val height1 = 800
      scene = new Scene(width1, height1) {
        val buffor = new HBox
        buffor.prefHeight = 10
//        val fileMenu = new Menu("File")
//        val exitItem = new MenuItem("Exit")
//        fileMenu.onAction = (e: ActionEvent) => {
//          exit(77)
//        }
//        fileMenu.items = List(exitItem)
//        menuBar.menus = List(fileMenu)

        val settings = new VBox
        settings.alignment = TopCenter
        settings.prefWidth = 300
        settings.spacing = 5

        val currentSettingsLabel = new Label(chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor)

        val elevatorSelectionLabel = new Label("Which elevator you want to use?")
        val elevatorSelection = new ListView(List.tabulate(16)(i => "Elevator no. " + (i + 1)))
        elevatorSelection.selectionModel.apply.selectedItems.onChange {
          chosenElevator = elevatorSelection.selectionModel.apply.getSelectedItems.get(0)
          currentSettingsLabel.text = chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor
        }
        elevatorSelection.maxWidth = 200
        settings.children += elevatorSelectionLabel
        settings.children += elevatorSelection

        val floorSelectionLabel = new Label("On which floor are you?")
        val floorSelection = new ComboBox(List.tabulate(11)(i => if (i >= 1) i + ". floor" else "Ground floor"))
//        val floorSelection = new ComboBox(List.tabulate(11)(i => FloorsNo(i).toString))
        floorSelection.onAction = (e: ActionEvent) => {
          currentFloor = floorSelection.value.apply()
          currentSettingsLabel.text = chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor
        }
        settings.children += floorSelectionLabel
        settings.children += floorSelection

        val destinationSelectionLabel = new Label("To which floor are you going?")
        val destinationSelection = new ComboBox(List.tabulate(11)(i => if (i >= 1) i + ". floor" else "Ground floor"))
        destinationSelection.onAction = (e: ActionEvent) => {
          destinationFloor = destinationSelection.value.apply()
          currentSettingsLabel.text = chosenElevator + "\nfrom " + currentFloor + "\nto " + destinationFloor
        }
        settings.children += destinationSelectionLabel
        settings.children += destinationSelection

        settings.children += currentSettingsLabel

        val summonElevator = new Button("Summon elevator")
        settings.children += summonElevator

        val bottomBox = new VBox
        bottomBox.prefHeight = 90
        bottomBox.alignment = TopCenter
        val bottomLabel = new Label("Change of the simulation speed [increase<-X->decrease]");
        bottomLabel.prefHeight = 20
        bottomBox.children += bottomLabel


        val controls = new HBox
        controls.alignment = TopCenter
        controls.prefHeight = 70

        val controlButton = new Button("Pause")
        controlButton.prefHeight = 70
        controlButton.prefWidth = 100
        controls.children += controlButton

        val timeScroll = new ScrollBar()
        timeScroll.minHeight = 70
        timeScroll.prefWidth = 800
        controls.children += timeScroll

        val exitButton = new Button("Exit")
        exitButton.onAction = (e: ActionEvent) => {exit(0)}
        exitButton.prefHeight = 70
        exitButton.prefWidth = 100
        controls.children += exitButton

        bottomBox.children += controls

        val allStats = new VBox
        allStats.alignment = TopCenter
        allStats.prefWidth = 300

        val getInfo = new Button("Generate stats")
//        getInfo.prefWidth = 200
//        allStats.children += getInfo

        val stats = new TextArea()
        stats.prefHeight = 700
        stats.editable = false
        allStats.children += stats


        val rootPane = new BorderPane
        rootPane.top = buffor
        rootPane.center = elevatorSimulation(controlButton, summonElevator, getInfo, timeScroll, stats)
        rootPane.right = allStats
        rootPane.left = settings
        rootPane.bottom = bottomBox
        root = rootPane

        //      content = List(root)
      }
    }
    stage.resizable = false

    def elevatorSimulation(controlButton: Button, summonElevator: Button,
                           getInfo: Button, timeScroll: ScrollBar, stats: TextArea): GridPane = {
      val grid = new GridPane
      val rectangle_size = 4
      val width = scene_size_x / rectangle_size
      val height = scene_size_y / rectangle_size
      val rectangles = Array.fill(
        scene_size_x / rectangle_size, scene_size_y / rectangle_size)(Rectangle(rectangle_size, rectangle_size, LightBlue))
      for (i <- 0 until width) {
        for (j <- 0 until height) {
          grid.add(rectangles(i)(j), i, j)
        }
      }
      for (i <- 0 until width) {
        for (j <- 0 until height) {
          if (j % 16 == 11 && i >= 3 && i <= width - 5 || (i == 3 || i == width - 5 || j == 2)) {
            rectangles(i)(j).fill = Black
          }
          if (j < 2 || j <= height - 4 && (i < 3 || i > width - 5)) {
            rectangles(i)(j).fill = Blue
          }
          if (j > height - 4) {
            rectangles(i)(j).fill = LightGreen
          }
        }
      }

      def refill(): Unit = {
        for (i <- 0 until width) {
          for (j <- 0 until height) {
            if (i < width - 2 && (i % 12 >= 7 && i % 12 <= 11) && j <= height - 4 && j > 4) {
              rectangles(i)(j).fill = LightGrey
            }
            if (i < width - 2 && (i % 12 >= 7 && i % 12 <= 11) && (j % 16 >= 4 && j % 16 <= 11 && j <= height - 4)) {
              rectangles(i)(j).fill = Grey
            }
          }
        }
      }

      refill()

      val es = new ElevatorSystem

//      getInfo.onAction = (e: ActionEvent) => {
//        stats.setText(es.checkout())
//      }

      summonElevator.onAction = (e: ActionEvent) => {
        if (chosenElevator == "[none]" || currentFloor == "[none]" || destinationFloor == "[none]") {}
//        if (chosenElevator == "[none]" || currentFloor.id == 11 || destinationFloor == "[none]") {}
        else {
          var id, cf, df = -1
          if (chosenElevator.length == 14) {
            id = chosenElevator(chosenElevator.length - 1).toInt - 48
          }
          else {
            id = (chosenElevator(chosenElevator.length - 2).toInt - 48) * 10 + chosenElevator(chosenElevator.length - 1).toInt - 48
          }
          id -= 1

          if (currentFloor == "Ground floor") {
            cf = 0
          }
          else if (currentFloor.length == 8) {
            cf = currentFloor(0).toInt - 48
          }
          else {
            cf = (currentFloor(0).toInt - 48) * 10 + currentFloor(1).toInt - 48
          }
//          cf = currentFloor.id

          if (destinationFloor == "Ground floor") {
            df = 0
          }
          else if (destinationFloor.length == 8) {
            df = destinationFloor(0).toInt - 48
          }
          else {
            df = (destinationFloor(0).toInt - 48) * 10 + destinationFloor(1).toInt - 48
          }
          es.summonElevator(id, cf, df)
        }
      }

      timeScroll.min = -7
      timeScroll.max = -1
      timeScroll.value = -3

      var lastTime = 0.0
      var total_delay = math.exp(timeScroll.value.apply)
      var updateDelay = total_delay
      var running = true
      val timer: AnimationTimer = AnimationTimer(t => {
        if (lastTime > 0) {
          val delta = (t - lastTime) / 1e9
          updateDelay -= delta
          if (updateDelay < 0) {
            es.iteration()
            refill()
            for (e <- es.elevators) {
              val bottomCenterX = 12 * e.ID + 9
              val bottomCenterY = (height - 4) - (e.currentFloor * 16).toInt
              //            println(bottomCenterX)
              //            println(bottomCenterY)
              for (i <- bottomCenterX - 2 to bottomCenterX + 2) {
                for (j <- bottomCenterY - 7 to bottomCenterY) {
                  rectangles(i)(j).fill = Red
                }
              }
            }
            stats.setText(es.checkout())
            updateDelay = total_delay
          }
        }
        timeScroll.value.onChange {
          total_delay = math.exp(timeScroll.value.apply)
        }
        lastTime = t
      })
      timer.start()
      controlButton.onAction = (e: ActionEvent) => {
        if (running) {
          timer.stop
          controlButton.text = "Resume"
          running = false
        }
        else {
          timer.start
          controlButton.text = "Pause"
          running = true
        }
      }
      grid
    }
  }
}