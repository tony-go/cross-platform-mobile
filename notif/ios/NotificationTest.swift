import SwiftUI
import UserNotifications
import os

let logger = Logger(subsystem: "com.tonygo.notif", category: "Notification")

@main
struct App: SwiftUI.App {
    @Environment(\.scenePhase) private var scenePhase

    var body: some Scene {
        WindowGroup {
            VStack {
                Text("Hello SwiftUI!")
                    .padding()

                Button("Send Notification") {
                    scheduleNotification()
                }
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(10)
            }
        }
        .onChange(of: scenePhase) { oldPhase, newPhase in
            if newPhase == .active {
                logger.info("App is active")
                requestNotificationPermission()
            } else if newPhase == .background {
                logger.info("App moved to background")
                scheduleNotification(for: "App moved to background", in: 1)
                scheduleNotification(for: "App moved to background 2", in: 3)
            }
        }
    }

    // Request notification permission
    func requestNotificationPermission() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if let error = error {
                logger.info("Error requesting permission: \(error.localizedDescription)")
            } else {
                logger.info("Notification permission granted: \(granted)")
            }
        }
    }

    // Schedule a local notification
    func scheduleNotification(for message: String = "This is a test notification.", in seconds: TimeInterval = 5) {
        let content = UNMutableNotificationContent()
        content.title = message
        content.body = "This is a test \(seconds)"
        content.sound = .default
        content.threadIdentifier = "local-notification"

        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: seconds, repeats: false)
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)
        logger.info("Scheduling notification in 5 seconds...")

        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                logger.error("Error scheduling notification: \(error.localizedDescription)")
            } else {
                logger.info("Notification scheduled in 5 seconds!")
            }
        }
    }
}
