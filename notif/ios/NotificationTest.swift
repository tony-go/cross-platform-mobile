import SwiftUI

@main
struct App: SwiftUI.App {
  @Environment(\.scenePhase) private var scenePhase

  var body: some Scene {
    WindowGroup {
      ContentView()
    }
  }
}

struct ContentView: View {
  var body: some View {
    Text("Hello SwiftUI!")
  }
}
